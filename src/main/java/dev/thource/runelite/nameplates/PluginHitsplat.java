package dev.thource.runelite.nameplates;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.HitsplatID;
import net.runelite.api.gameval.VarbitID;

@RequiredArgsConstructor
@Getter
public class PluginHitsplat {
  private static final Map<Integer, Integer> maxToNormalHitsplatTypes = new HashMap<>();
  private static final Map<Integer, Integer> otherToNormalHitsplatTypes = new HashMap<>();

  private static void registerDamageHitsplat(int normalType, int maxType, int otherType) {
    otherToNormalHitsplatTypes.put(otherType, normalType);
    maxToNormalHitsplatTypes.put(maxType, normalType);
  }

  static {
    registerDamageHitsplat(HitsplatID.DAMAGE_ME, HitsplatID.DAMAGE_MAX_ME, HitsplatID.DAMAGE_OTHER);
    registerDamageHitsplat(
        HitsplatID.DAMAGE_ME_CYAN, HitsplatID.DAMAGE_MAX_ME_CYAN, HitsplatID.DAMAGE_OTHER_CYAN);
    registerDamageHitsplat(
        HitsplatID.DAMAGE_ME_ORANGE,
        HitsplatID.DAMAGE_MAX_ME_ORANGE,
        HitsplatID.DAMAGE_OTHER_ORANGE);
    registerDamageHitsplat(
        HitsplatID.DAMAGE_ME_YELLOW,
        HitsplatID.DAMAGE_MAX_ME_YELLOW,
        HitsplatID.DAMAGE_OTHER_YELLOW);
    registerDamageHitsplat(
        HitsplatID.DAMAGE_ME_WHITE, HitsplatID.DAMAGE_MAX_ME_WHITE, HitsplatID.DAMAGE_OTHER_WHITE);
    registerDamageHitsplat(
        HitsplatID.DAMAGE_ME_POISE, HitsplatID.DAMAGE_MAX_ME_POISE, HitsplatID.DAMAGE_OTHER_POISE);
  }

  private final Client client;
  private final int hitsplatType;
  private final int amount;
  private final long serverTick;
  private final int tickIndex;
  @Setter private Long createdAt = System.currentTimeMillis();

  private boolean isMaxHitsplat() {
    return maxToNormalHitsplatTypes.containsKey(hitsplatType);
  }

  private boolean isOtherHitsplat() {
    return otherToNormalHitsplatTypes.containsKey(hitsplatType);
  }

  private int getNormalHitsplatType() {
    return maxToNormalHitsplatTypes.getOrDefault(
        hitsplatType, otherToNormalHitsplatTypes.getOrDefault(hitsplatType, hitsplatType));
  }

  public int getHitsplatType() {
    if (isMaxHitsplat()) {
      var maxHitDisabled = client.getVarbitValue(VarbitID.HITSPLAT_MAXHIT_DISABLED) == 1;
      var maxHitThreshold = client.getVarbitValue(VarbitID.SETTINGS_HITSPLAT_THRESHOLD);
      if (maxHitDisabled || amount < maxHitThreshold) {
        return getNormalHitsplatType();
      }
    } else if (isOtherHitsplat()) {
      var tintDisabled = client.getVarbitValue(VarbitID.HITSPLAT_TINT_DISABLED) == 1;
      if (tintDisabled) {
        return getNormalHitsplatType();
      }
    }

    return hitsplatType;
  }
}
