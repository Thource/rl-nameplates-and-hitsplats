package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.party.data.PartyData;
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

  public abstract boolean isPercentageHealth();

  public Nameplate(NameplatesPlugin plugin, Actor actor) {
    this.actor = actor;
    this.plugin = plugin;
    updateFromActor(plugin);
    currentHealth = maxHealth;
    hpAnimationData.startAnimation(maxHealth, maxHealth, 0);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    name = Text.removeTags(actor.getName());
    combatLevel = actor.getCombatLevel();
  }

  public int getCombatLevelDifference() {
    return getCombatLevel() - plugin.getClient().getLocalPlayer().getCombatLevel();
  }

  public boolean shouldDrawHealthBar() {
    return true;
  }

  public boolean shouldDrawPrayerBar() {
    return false;
  }

  public boolean shouldDrawEnergyBar() {
    return false;
  }

  public boolean shouldDrawSpecialBar() {
    return false;
  }

  public PoisonStatus getPoisonStatus() {
    return null;
  }

  public boolean isDiseased() {
    return false;
  }

  public boolean isAnyPrayerActive() {
    return false;
  }

  public boolean isRunActive() {
    return false;
  }

  public boolean isSpecialActive() {
    return false;
  }

  public int getCurrentPrayer() {
    return -1;
  }

  public int getMaxPrayer() {
    return -1;
  }

  public int getCurrentEnergy() {
    return -1;
  }

  public int getCurrentSpecial() {
    return -1;
  }

  public boolean isInCombat(Client client) {
    return client.getTickCount() - lastHitsplat <= 10;
  }

  public boolean isInLocalCombat(Client client) {
    return client.getTickCount() - lastLocalHitsplat <= 10;
  }

  public abstract boolean hasHintArrow();

  public boolean isHovered() {
    return plugin.getNameplatesOverlay().getHoveredActor() == this.actor;
  }

  public boolean isSameTeam() {
    return false;
  }

  public StatChange getHoveredItemStatChange(Stat stat) {
    return null;
  }

  public boolean hasVengeance() {
    return false;
  }

  public PartyData getPartyData() {
    return null;
  }
}
