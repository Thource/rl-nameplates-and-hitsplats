package dev.thource.runelite.nameplates;

import lombok.Getter;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.party.data.PartyData;

@Getter
public class PlayerNameplate extends Nameplate {
  private PartyData partyData;

  public PlayerNameplate(NameplatesPlugin plugin, Player actor) {
    super(plugin, actor);
    this.maxHealth = 100;
  }

  @Override
  public boolean isAnyPrayerActive() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return super.isAnyPrayerActive();
    }

    return plugin.isAnyPrayerActive();
  }

  @Override
  public boolean hasVengeance() {
    if (actor == plugin.getClient().getLocalPlayer()) {
      return plugin.getClient().getVarbitValue(VarbitID.VENGEANCE_REBOUND) == 1;
    }

    if (partyData != null) {
      return partyData.isVengeanceActive();
    }

    return false;
  }

  @Override
  public int getCurrentPrayer() {
    if (actor == plugin.getClient().getLocalPlayer()) {
      return plugin.getClient().getBoostedSkillLevel(Skill.PRAYER);
    }

    if (partyData != null) {
      return partyData.getPrayer();
    }

    return super.getCurrentPrayer();
  }

  @Override
  public int getMaxPrayer() {
    if (actor == plugin.getClient().getLocalPlayer()) {
      return plugin.getClient().getRealSkillLevel(Skill.PRAYER);
    }

    if (partyData != null) {
      return partyData.getMaxPrayer();
    }

    return super.getMaxPrayer();
  }

  @Override
  public PoisonStatus getPoisonStatus() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return null;
    }

    return plugin.getPoisonStatus();
  }

  @Override
  public boolean isDiseased() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return false;
    }

    return plugin.getClient().getVarpValue(VarPlayerID.DISEASE) > 0;
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    var client = plugin.getClient();

    if (actor == client.getLocalPlayer()) {
      maxHealth = client.getRealSkillLevel(Skill.HITPOINTS);
      return;
    }

    var partyMember = plugin.getPartyService().getMemberByDisplayName(name);
    if (partyMember == null) {
      partyData = null;

      return;
    }

    partyData = plugin.getPartyDataMap().get(partyMember.getMemberId());
    if (partyData != null) {
      maxHealth = partyData.getMaxHitpoints();
    }
  }

  @Override
  public boolean hasHintArrow() {
    return plugin.getClient().getHintArrowPlayer() == actor;
  }

  @Override
  public boolean shouldDrawPrayerBar() {
    return getMaxPrayer() != -1;
  }

  @Override
  public boolean shouldDrawEnergyBar() {
    return getCurrentEnergy() != -1;
  }

  @Override
  public int getCurrentEnergy() {
    var client = plugin.getClient();
    if (actor == client.getLocalPlayer()) {
      return client.getEnergy() / 100;
    }

    if (partyData != null) {
      return partyData.getRunEnergy();
    }

    return -1;
  }

  @Override
  public boolean isRunActive() {
    var client = plugin.getClient();
    if (actor == client.getLocalPlayer()) {
      return client.getVarpValue(VarPlayerID.OPTION_RUN) == 1;
    }

    return false;
  }

  @Override
  public int getCurrentSpecial() {
    var client = plugin.getClient();
    if (actor == client.getLocalPlayer()) {
      return client.getVarpValue(VarPlayerID.SA_ENERGY) / 10;
    }

    if (partyData != null) {
      return partyData.getSpecEnergy();
    }

    return -1;
  }

  @Override
  public boolean isSpecialActive() {
    var client = plugin.getClient();
    if (actor == client.getLocalPlayer()) {
      return client.getVarpValue(VarPlayerID.SA_ATTACK) == 1;
    }

    return false;
  }

  @Override
  public boolean shouldDrawSpecialBar() {
    return getCurrentSpecial() != -1;
  }

  @Override
  public boolean isSameTeam() {
    var team = ((Player) actor).getTeam();
    return team > 0 && team == plugin.getClient().getLocalPlayer().getTeam();
  }

  @Override
  public StatChange getHoveredItemStatChange(Stat stat) {
    if (actor == plugin.getClient().getLocalPlayer()) {
      return plugin.getHoveredItemStatChange(stat);
    }

    return null;
  }

  @Override
  public boolean isPercentageHealth() {
    return maxHealth == 100;
  }
}
