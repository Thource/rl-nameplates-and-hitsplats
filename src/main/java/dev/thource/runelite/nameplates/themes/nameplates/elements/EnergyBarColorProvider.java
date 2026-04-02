package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.List;
import lombok.AccessLevel;
import lombok.Setter;

public class EnergyBarColorProvider extends BarColorProvider {
  @Setter(AccessLevel.PRIVATE)
  protected Color activeColor;

  public EnergyBarColorProvider() {
    this(new Color(140, 125, 0), new Color(200, 180, 0));
  }

  public EnergyBarColorProvider(Color normalColor, Color activeColor) {
    super(normalColor);

    this.activeColor = activeColor;
  }

  @Override
  public Color getColor(Nameplate nameplate) {
    if (nameplate.isRunActive()) {
      return activeColor;
    }

    return normalColor;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new ColorInput("Active fill color", activeColor, this::setActiveColor, plugin));

    return editInputs;
  }
}
