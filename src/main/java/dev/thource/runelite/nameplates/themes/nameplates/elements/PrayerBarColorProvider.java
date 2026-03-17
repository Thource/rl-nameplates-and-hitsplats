package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.List;

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

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(
        new ColorInput("Active fill color", activeColor, value -> activeColor = value, plugin));

    return editInputs;
  }
}
