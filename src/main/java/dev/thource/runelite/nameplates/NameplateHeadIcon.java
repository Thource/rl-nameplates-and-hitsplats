package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Actor;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.gameval.SpriteID;
import net.runelite.client.game.SpriteManager;

@RequiredArgsConstructor
public enum NameplateHeadIcon implements Nameable {
  NONE(null, -1, "None"),
  MELEE(HeadIcon.MELEE, 0, "Prot. Melee"),
  RANGED(HeadIcon.RANGED, 1, "Prot. Range"),
  MAGIC(HeadIcon.MAGIC, 2, "Prot. Magic"),
  RETRIBUTION(HeadIcon.RETRIBUTION, 3, "Retribution"),
  SMITE(HeadIcon.SMITE, 4, "Smite"),
  REDEMPTION(HeadIcon.REDEMPTION, 5, "Redemption"),
  RANGE_MAGE(HeadIcon.RANGE_MAGE, 6, "Prot. Ran/Mag"),
  RANGE_MELEE(HeadIcon.RANGE_MELEE, 7, "Prot. Ran/Mel"),
  MAGE_MELEE(HeadIcon.MAGE_MELEE, 8, "Prot. Mag/Mel"),
  RANGE_MAGE_MELEE(HeadIcon.RANGE_MAGE_MELEE, 9, "Prot. Ran/Mag/Mel"),
  WRATH(HeadIcon.WRATH, 10, "Wrath"),
  SOUL_SPLIT(HeadIcon.SOUL_SPLIT, 11, "Soul Split"),
  DEFLECT_MELEE(HeadIcon.DEFLECT_MELEE, 12, "Def. Melee"),
  DEFLECT_RANGE(HeadIcon.DEFLECT_RANGE, 13, "Def. Range"),
  DEFLECT_MAGE(HeadIcon.DEFLECT_MAGE, 14, "Def. Magic"),
  ;

  @Getter private final HeadIcon headIcon;
  private final int overheadFileId;
  @Getter private final String name;
  @Getter private BufferedImage image;

  private static final Map<HeadIcon, NameplateHeadIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateHeadIcon v : NameplateHeadIcon.values()) {
      map.put(v.headIcon, v);
    }
  }

  public static NameplateHeadIcon get(HeadIcon headIcon) {
    return map.get(headIcon);
  }

  public static NameplateHeadIcon get(Player player) {
    return map.get(player.getOverheadIcon());
  }

  public static NameplateHeadIcon get(NPC npc) {
    var overheadArchives = npc.getOverheadArchiveIds();
    var overheadSprites = npc.getOverheadSpriteIds();

    if (overheadArchives != null && overheadArchives.length != 0) {
      assert overheadSprites != null;

      for (int archiveIndex = 0; archiveIndex < overheadArchives.length; archiveIndex++) {
        if (overheadArchives[archiveIndex] != SpriteID.HEADICONS_PRAYER) {
          continue;
        }

        var spriteId = overheadSprites[archiveIndex];
        if (archiveIndex >= HeadIcon.values().length) {
          continue;
        }

        return map.get(HeadIcon.values()[spriteId]);
      }
    }

    return null;
  }

  public static NameplateHeadIcon get(Actor actor) {
    if (actor instanceof Player) {
      return get((Player) actor);
    }

    return get((NPC) actor);
  }

  public void loadImage(SpriteManager spriteManager) {
    if (image != null || overheadFileId == -1) {
      return;
    }

    image = spriteManager.getSprite(SpriteID.HEADICONS_PRAYER, overheadFileId);
  }
}
