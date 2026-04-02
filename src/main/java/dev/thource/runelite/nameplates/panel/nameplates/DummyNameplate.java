package dev.thource.runelite.nameplates.panel.nameplates;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.PoisonStatus;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class DummyNameplate extends Nameplate {
  private String dummyName = "Player name";
  private int dummyLevel = 123;
  private boolean dummyHintArrow = true;
  private boolean dummyHovered = true;
  private boolean dummyVengeance = true;
  private boolean dummyDrawHealthBar = true;
  private boolean dummyDrawPrayerBar = true;
  private boolean dummyDrawEnergyBar = true;
  private boolean dummyDrawSpecialBar = true;
  private int dummyCurrentHealth = 73;
  private int dummyMaxHealth = 99;
  private int dummyCurrentPrayer = 73;
  private int dummyMaxPrayer = 99;
  private int dummyCurrentEnergy = 73;
  private int dummyCurrentSpecial = 73;
  private HealthState dummyHealthState = HealthState.NONE;
  private StatChange dummyItemHpChange;
  private StatChange dummyItemPrayerChange;
  private StatChange dummyItemEnergyChange;

  public DummyNameplate(NameplatesPlugin plugin, DummyPlayer dummyActor) {
    super(plugin, dummyActor);
    setNoLoot(true);
  }

  @Override
  public int getCombatLevelDifference() {
    return getCombatLevel() - 126;
  }

  public void setHovered(boolean hovered) {
    dummyHovered = hovered;
  }

  @Override
  public boolean isHovered() {
    return dummyHovered;
  }

  public void setHintArrow(boolean hintArrow) {
    dummyHintArrow = hintArrow;
  }

  @Override
  public boolean hasHintArrow() {
    return dummyHintArrow;
  }

  public void setCurrentHealth(int currentHealth) {
    dummyCurrentHealth = currentHealth;
  }

  @Override
  public int getCurrentHealth() {
    return dummyCurrentHealth;
  }

  public void setMaxHealth(int maxHealth) {
    dummyMaxHealth = maxHealth;
  }

  @Override
  public int getMaxHealth() {
    return dummyMaxHealth;
  }

  public void setName(String name) {
    dummyName = name;
  }

  @Override
  public String getName() {
    return dummyName;
  }

  public void setCombatLevel(int level) {
    dummyLevel = level;
  }

  @Override
  public int getCombatLevel() {
    return dummyLevel;
  }

  public void setCurrentPrayer(int currentPrayer) {
    dummyCurrentPrayer = currentPrayer;
  }

  public void setCurrentEnergy(int current) {
    dummyCurrentEnergy = current;
  }

  public void setCurrentSpecial(int current) {
    dummyCurrentSpecial = current;
  }

  @Override
  public int getCurrentPrayer() {
    return dummyCurrentPrayer;
  }

  public void setMaxPrayer(int maxPrayer) {
    dummyMaxPrayer = maxPrayer;
  }

  @Override
  public int getMaxPrayer() {
    return dummyMaxPrayer;
  }

  public void setHealthState(HealthState state) {
    dummyHealthState = state;
  }

  @Override
  public PoisonStatus getPoisonStatus() {
    if (dummyHealthState == HealthState.POISON) {
      return new PoisonStatus(100);
    }

    if (dummyHealthState == HealthState.VENOM) {
      return new PoisonStatus(1_000_100);
    }

    return null;
  }

  @Override
  public boolean isDiseased() {
    return dummyHealthState == HealthState.DISEASE;
  }

  @Override
  public StatChange getHoveredItemStatChange(Stat stat) {
    if (stat == Stats.HITPOINTS) {
      return dummyItemHpChange;
    }

    if (stat == Stats.PRAYER) {
      return dummyItemPrayerChange;
    }

    if (stat == Stats.RUN_ENERGY) {
      return dummyItemEnergyChange;
    }

    return null;
  }

  public void refreshConsumableRelatives() {
    dummyItemHpChange.setRelative(
        Math.max(
            -getCurrentHealth(),
            Math.min(
                dummyItemHpChange.getTheoretical(),
                Math.max(0, getMaxHealth() - getCurrentHealth()))));
    dummyItemPrayerChange.setRelative(
        Math.max(
            -getCurrentPrayer(),
            Math.min(
                dummyItemPrayerChange.getTheoretical(),
                Math.max(0, getMaxPrayer() - getCurrentPrayer()))));
    dummyItemEnergyChange.setRelative(
        Math.max(
            -getCurrentEnergy(),
            Math.min(
                dummyItemEnergyChange.getTheoretical(), Math.max(0, 100 - getCurrentEnergy()))));
  }

  public void setHoveredItemStatChange(StatChange statChange) {
    dummyItemHpChange = new StatChange();
    dummyItemHpChange.setTheoretical(statChange.getTheoretical());
    dummyItemPrayerChange = new StatChange();
    dummyItemPrayerChange.setTheoretical(statChange.getTheoretical());
    dummyItemEnergyChange = new StatChange();
    dummyItemEnergyChange.setTheoretical(statChange.getTheoretical());

    refreshConsumableRelatives();
  }

  public void setVengeance(boolean value) {
    dummyVengeance = value;
  }

  @Override
  public boolean hasVengeance() {
    return dummyVengeance;
  }

  @Override
  public int getCurrentEnergy() {
    return dummyCurrentEnergy;
  }

  @Override
  public int getCurrentSpecial() {
    return dummyCurrentSpecial;
  }

  @Override
  public boolean shouldDrawHealthBar() {
    return dummyDrawHealthBar;
  }

  public void setDrawHealthBar(boolean draw) {
    dummyDrawHealthBar = draw;
  }

  @Override
  public boolean shouldDrawPrayerBar() {
    return dummyDrawPrayerBar;
  }

  public void setDrawPrayerBar(boolean draw) {
    dummyDrawPrayerBar = draw;
  }

  @Override
  public boolean shouldDrawEnergyBar() {
    return dummyDrawEnergyBar;
  }

  public void setDrawEnergyBar(boolean draw) {
    dummyDrawEnergyBar = draw;
  }

  @Override
  public boolean shouldDrawSpecialBar() {
    return dummyDrawSpecialBar;
  }

  public void setDrawSpecialBar(boolean draw) {
    dummyDrawSpecialBar = draw;
  }

  @Override
  public boolean isPercentageHealth() {
    return false;
  }
}
