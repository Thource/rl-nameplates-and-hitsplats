package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import java.awt.Color;

public class HealthBarColorProvider extends BarColorProvider {
  protected Color poisonColor;
  protected Color venomColor;
  protected Color diseaseColor;

  public HealthBarColorProvider() {
    this(new Color(120, 50, 40), new Color(50, 120, 40), new Color(50, 100, 80), Color.YELLOW);
  }

  public HealthBarColorProvider(
      Color normalColor, Color poisonColor, Color venomColor, Color diseaseColor) {
    super(normalColor);

    this.poisonColor = poisonColor;
    this.venomColor = venomColor;
    this.diseaseColor = diseaseColor;
  }

  @Override
  public Color getColor(Nameplate nameplate) {
    var poisonStatus = nameplate.getPoisonStatus();

    if (poisonStatus != null) {
      if (poisonStatus.isVenom()) {
        return venomColor;
      } else {
        return poisonColor;
      }
    }

    return normalColor;
  }
}
