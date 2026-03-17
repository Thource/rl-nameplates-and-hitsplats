package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;
import net.runelite.client.util.Text;

@Getter
public abstract class Nameplate {
  @Setter private boolean noLoot;
  protected final Actor actor;
  protected String name;
  protected int maxHealth = 0;
  protected int combatLevel;
  @Setter protected int healthScale = 30;
  @Setter protected int currentHealth;
  @Setter protected int lastUpdate;
  @Setter protected int lastHitsplat = -100;
  @Setter protected int lastLocalHitsplat = -100;
  protected final AnimationData hpAnimationData = new AnimationData();
  protected final NameplatesPlugin plugin;

  public Nameplate(NameplatesPlugin plugin, Actor actor) {
    this.actor = actor;
    this.plugin = plugin;
    updateFromActor(plugin);
    currentHealth = maxHealth;
    hpAnimationData.startAnimation(maxHealth, maxHealth, 0);
  }

  public String getHealthString() {
    String healthString = this.getCurrentHealth() + " / " + this.getMaxHealth();
    if (this instanceof NPCNameplate && ((NPCNameplate) this).getPercentageHealthOverride() > 0) {
      healthString += "~";
    }

    boolean forcePercentage = this.drawHealthAsPercentage();

    HitpointsDisplayStyle displayStyle = plugin.getConfig().hitpointsDisplayStyle();
    if (forcePercentage || displayStyle != HitpointsDisplayStyle.HITPOINTS) {
      double percentage =
          Math.ceil((float) this.getCurrentHealth() / this.getMaxHealth() * 1000f) / 10f;

      if (forcePercentage || displayStyle == HitpointsDisplayStyle.PERCENTAGE) {
        healthString = percentage + "%";
      } else {
        healthString += " (" + percentage + "%)";
      }
    }

    return healthString;
  }

  public String getPrayerString() {
    return null;
  }

  public PoisonStatus getPoisonStatus() {
    return null;
  }

  public int getCurrentPrayer() {
    return -1;
  }

  public int getMaxPrayer() {
    return -1;
  }

  public boolean isAnyPrayerActive() {
    return false;
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    this.name = Text.removeTags(actor.getName());
    combatLevel = actor.getCombatLevel();
  }

  public int getCombatLevelDifference() {
    return getCombatLevel() - plugin.getClient().getLocalPlayer().getCombatLevel();
  }

  public float getHealthPercentage() {
    return hpAnimationData.getCurrentValue() / getMaxHealth();
  }

  public boolean shouldDrawPrayerBar() {
    return false;
  }

  public float getPrayerPercentage() {
    return 0;
  }

  public boolean isInCombat(Client client) {
    return client.getTickCount() - lastHitsplat <= 10;
  }

  public boolean isInLocalCombat(Client client) {
    return client.getTickCount() - lastLocalHitsplat <= 10;
  }

  public abstract boolean hasHintArrow();

  public abstract boolean drawHealthAsPercentage();

  public boolean isHovered() {
    return plugin.getNameplatesOverlay().getHoveredActor() == this.actor;
  }

  public boolean isSameTeam() {
    return false;
  }
}
