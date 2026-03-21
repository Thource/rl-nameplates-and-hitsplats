package dev.thource.runelite.nameplates.panel.nameplates;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.PoisonStatus;

public class DummyNameplate extends Nameplate {
  private String dummyName;
  private int dummyLevel;
  private boolean dummyHintArrow;
  private boolean dummyHovered;
  private boolean dummyDrawPrayerBar;
  private int dummyCurrentHealth;
  private int dummyMaxHealth;
  private int dummyCurrentPrayer;
  private int dummyMaxPrayer;
  private HealthState dummyHealthState = HealthState.NONE;

  public DummyNameplate(NameplatesPlugin plugin, DummyPlayer dummyActor) {
    super(plugin, dummyActor);
    dummyName = "Player name";
    dummyLevel = 123;
    dummyHintArrow = true;
    dummyHovered = true;
    dummyDrawPrayerBar = true;
    dummyCurrentHealth = 73;
    dummyMaxHealth = 99;
    dummyCurrentPrayer = 73;
    dummyMaxPrayer = 99;
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

  @Override
  public float getHealthPercentage() {
    return getCurrentHealth() / (float) getMaxHealth();
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

  @Override
  public boolean drawHealthAsPercentage() {
    return false;
  }

  public void setCurrentPrayer(int currentPrayer) {
    dummyCurrentPrayer = currentPrayer;
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

  public void setDrawPrayerBar(boolean drawPrayerBar) {
    dummyDrawPrayerBar = drawPrayerBar;
  }

  @Override
  public boolean shouldDrawPrayerBar() {
    return dummyDrawPrayerBar;
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
}
