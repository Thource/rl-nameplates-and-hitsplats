package dev.thource.runelite.nameplates;

import lombok.Getter;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.hiscore.HiscoreSkill;

@Getter
public class PlayerNameplate extends Nameplate {
  public PlayerNameplate(NameplatesPlugin plugin, Player actor) {
    super(plugin, actor);
    this.maxHealth = 99;
  }

  @Override
  public boolean isAnyPrayerActive() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return super.isAnyPrayerActive();
    }

    return plugin.isAnyPrayerActive();
  }

  @Override
  public int getCurrentPrayer() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return super.getCurrentPrayer();
    }

    return plugin.getClient().getBoostedSkillLevel(Skill.PRAYER);
  }

  @Override
  public int getMaxPrayer() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return super.getMaxPrayer();
    }

    return plugin.getClient().getRealSkillLevel(Skill.PRAYER);
  }

  @Override
  public String getPrayerString() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return null;
    }

    return getCurrentPrayer() + " / " + getMaxPrayer();
  }

  @Override
  public float getPrayerPercentage() {
    if (actor != plugin.getClient().getLocalPlayer()) {
      return 0;
    }

    int currentPrayer = plugin.getClient().getBoostedSkillLevel(Skill.PRAYER);
    int maxPrayer = plugin.getClient().getRealSkillLevel(Skill.PRAYER);

    return (float) currentPrayer / maxPrayer;
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

    return plugin.getClient().getVarpValue(VarPlayerID.DISEASE)  > 0;
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    var client = plugin.getClient();

    if (actor == client.getLocalPlayer()) {
      maxHealth = client.getRealSkillLevel(Skill.HITPOINTS);
    }
  }

  @Override
  public boolean hasHintArrow() {
    return plugin.getClient().getHintArrowPlayer() == actor;
  }

  @Override
  public boolean drawHealthAsPercentage() {
    return actor != plugin.getClient().getLocalPlayer();
  }

  @Override
  public boolean shouldDrawPrayerBar() {
    return actor == plugin.getClient().getLocalPlayer();
  }

  @Override
  public boolean isSameTeam() {
    return ((Player) actor).getTeam() == plugin.getClient().getLocalPlayer().getTeam();
  }
}
