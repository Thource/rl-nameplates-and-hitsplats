package dev.thource.runelite.nameplates;

import lombok.Getter;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.client.hiscore.HiscoreSkill;

@Getter
public class PlayerNameplate extends Nameplate {
  private boolean hiscoreLookupStarted;

  public PlayerNameplate(NameplatesPlugin plugin, Player actor) {
    super(plugin, actor);
    this.maxHealth = 99;

    // No need to lookup lvl 126 players, they are always 99 hp
    this.hiscoreLookupStarted = actor.getCombatLevel() == 126;
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

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    var client = plugin.getClient();
    if (client == null) {
      return;
    }

    if (actor == client.getLocalPlayer()) {
      maxHealth = client.getRealSkillLevel(Skill.HITPOINTS);
      return;
    }

    if (!hiscoreLookupStarted && plugin.getConfig().lookupPlayerHp()) {
      hiscoreLookupStarted = true;
      final Nameplate nameplate = this;
      plugin
          .getHiscoreClient()
          .lookupAsync(name, plugin.getHiscoreEndpoint())
          .whenCompleteAsync(
              (result, ex) -> {
                if (nameplate == null || result == null) {
                  return;
                }

                int hp = Math.max(10, result.getSkill(HiscoreSkill.HITPOINTS).getLevel());
                nameplate.maxHealth = hp;

                if (nameplate.currentHealth > hp) {
                  nameplate.currentHealth = hp;
                  hpAnimationData.startAnimation(hp, hp, 0);

                  HpCacheEntry cacheEntry = plugin.getHpCacheEntryForActor(actor);
                  if (cacheEntry != null) {
                    cacheEntry.setHp(hp);
                  }
                }
              });
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
}
