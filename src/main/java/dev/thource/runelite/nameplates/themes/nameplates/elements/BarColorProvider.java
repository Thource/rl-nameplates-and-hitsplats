package dev.thource.runelite.nameplates.themes.nameplates.elements;

import java.awt.Color;

public abstract class BarColorProvider extends ColorProvider {
  protected Color normalColor;

  public BarColorProvider(Color normalColor) {
    this.normalColor = normalColor;
  }
}
