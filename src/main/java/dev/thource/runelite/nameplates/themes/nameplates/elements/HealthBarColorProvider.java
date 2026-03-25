package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.List;

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

    if (nameplate.isDiseased()) {
      return diseaseColor;
    }

    return normalColor;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(
        new ColorInput("Poison fill color", poisonColor, value -> poisonColor = value, plugin));
    editInputs.add(
        new ColorInput("Venom fill color", venomColor, value -> venomColor = value, plugin));
    editInputs.add(
        new ColorInput("Disease fill color", diseaseColor, value -> diseaseColor = value, plugin));

    return editInputs;
  }
}
