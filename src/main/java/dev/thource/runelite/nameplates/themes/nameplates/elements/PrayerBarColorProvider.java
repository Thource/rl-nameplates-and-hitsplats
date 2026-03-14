package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import java.awt.Color;

public class PrayerBarColorProvider extends BarColorProvider {
  protected Color activeColor;

  public PrayerBarColorProvider() {
    this(new Color(20, 120, 110), new Color(30, 180, 160));
  }

  public PrayerBarColorProvider(Color normalColor, Color activeColor) {
    super(normalColor);

    this.activeColor = activeColor;
  }

  @Override
  public Color getColor(Nameplate nameplate) {
    if (nameplate.isAnyPrayerActive()) {
      return activeColor;
    }

    return normalColor;
  }
}
