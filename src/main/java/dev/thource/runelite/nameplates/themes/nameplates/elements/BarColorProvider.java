package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class BarColorProvider extends ColorProvider {
  protected Color normalColor;

  public BarColorProvider(Color normalColor) {
    this.normalColor = normalColor;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = new ArrayList<LabelledInput>();

    editInputs.add(
        new ColorInput("Normal fill color", normalColor, value -> normalColor = value, plugin));

    return editInputs;
  }
}
