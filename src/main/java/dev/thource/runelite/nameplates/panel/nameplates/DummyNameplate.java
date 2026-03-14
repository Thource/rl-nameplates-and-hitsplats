package dev.thource.runelite.nameplates.panel.nameplates;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;

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
}
