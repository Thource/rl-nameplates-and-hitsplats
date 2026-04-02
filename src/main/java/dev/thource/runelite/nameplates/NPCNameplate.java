package dev.thource.runelite.nameplates;

import com.google.common.base.Strings;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ParamID;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;

@Getter
public class NPCNameplate extends Nameplate {
  private static final List<Integer> BOAT_IDS =
      List.of(
          NpcID.BOAT_HP_NPC_TINY,
          NpcID.BOAT_HP_NPC_SMALL,
          NpcID.BOAT_HP_NPC_MEDIUM,
          NpcID.BOAT_HP_NPC_LARGE,
          NpcID.BOAT_HP_NPC_COLOSSAL);

  private boolean percentageHealth;
  private int percentageHealthOverride;
  private float firstPercentageHealth = -1f;
  @Setter private int damageTaken;

  public NPCNameplate(NameplatesPlugin plugin, NPC actor) {
    super(plugin, actor);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    var client = plugin.getClient();
    NPC npc = (NPC) actor;
    if (BOAT_IDS.contains(npc.getId())) {
      currentHealth = client.getVarbitValue(VarbitID.SAILING_SIDEPANEL_BOAT_HP);
      maxHealth = client.getVarbitValue(VarbitID.SAILING_SIDEPANEL_BOAT_HP_MAX);
      hpAnimationData.startAnimation(currentHealth, currentHealth, 0);

      var name2 =
          client
              .getDBTableField(
                  DBTableID.SailingBoatNameOptions.Row.SAILING_BOAT_NAME_DESCRIPTOR_OPTIONS, 1, 0)[
              client.getVarbitValue(VarbitID.SAILING_BOARDED_BOAT_NAME_2) - 1];
      var name3 =
          client
              .getDBTableField(
                  DBTableID.SailingBoatNameOptions.Row.SAILING_BOAT_NAME_NOUN_OPTIONS, 1, 0)[
              client.getVarbitValue(VarbitID.SAILING_BOARDED_BOAT_NAME_3) - 1];
      name = name2 + " " + name3;

      return;
    }

    if (npc.getId() == client.getVarpValue(VarPlayerID.HPBAR_HUD_NPC)) {
      maxHealth = client.getVarbitValue(VarbitID.HPBAR_HUD_BASEHP);
      percentageHealth = false;
      percentageHealthOverride = 0;
      firstPercentageHealth = -1f;
      return;
    }

    int maxHealth = 0;

    Integer health = plugin.getNpcManager().getHealth(npc.getId());
    if (health != null) {
      maxHealth = health;
    }

    NPCComposition composition = npc.getTransformedComposition();
    if (composition != null) {
      String longName = composition.getStringValue(ParamID.NPC_HP_NAME);
      if (!Strings.isNullOrEmpty(longName)) {
        this.name = longName;
      }
    }

    this.percentageHealth = maxHealth <= 0 && percentageHealthOverride <= 0;
    this.maxHealth = this.percentageHealth ? 100 : maxHealth;
  }

  @Override
  public boolean hasHintArrow() {
    return plugin.getClient().getHintArrowNpc() == actor;
  }

  public void recalculatePercentageHealth(NameplatesPlugin plugin) {
    if (((NPC) actor).getId() == NpcID.GEMSTONE_CRAB) {
      return;
    }

    if (firstPercentageHealth == -1f) {
      firstPercentageHealth = (float) currentHealth / maxHealth;
    }

    float currentPercentage = plugin.getCurrentHealth(actor, 1000) / 1000f;
    float percentageDifference = firstPercentageHealth - currentPercentage;
    int estimatedMaxHealth = Math.round(damageTaken / percentageDifference);

    if (this.percentageHealth) {
      this.currentHealth = Math.round(estimatedMaxHealth * firstPercentageHealth);
    }
    this.percentageHealth = false;
    this.percentageHealthOverride = estimatedMaxHealth;
  }

  public int getMaxHealth() {
    if (percentageHealthOverride > 0) {
      return percentageHealthOverride;
    }

    return maxHealth;
  }
}
