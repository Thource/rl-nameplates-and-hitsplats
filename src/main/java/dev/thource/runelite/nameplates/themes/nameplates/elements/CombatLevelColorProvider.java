package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CombatLevelColorProvider extends ColorProvider {
  protected boolean relativeColors;
  protected Color plus10Color;
  protected Color plus7Color;
  protected Color plus4Color;
  protected Color plus1Color;
  protected Color normalColor;
  protected Color minus1Color;
  protected Color minus4Color;
  protected Color minus7Color;
  protected Color minus10Color;

  public CombatLevelColorProvider() {
    relativeColors = true;

    plus10Color = new Color(0xff0000);
    plus7Color = new Color(0xff3000);
    plus4Color = new Color(0xff7000);
    plus1Color = new Color(0xffb000);
    normalColor = new Color(0xffff00);
    minus1Color = new Color(0xc0ff00);
    minus4Color = new Color(0x80ff00);
    minus7Color = new Color(0x40ff00);
    minus10Color = new Color(0x00ff00);
  }

  public CombatLevelColorProvider(Color normalColor) {
    super();

    this.relativeColors = false;
    this.normalColor = normalColor;
  }

  @Override
  public Color getColor(Nameplate nameplate) {
    if (!relativeColors) {
      return normalColor;
    }

    int delta = nameplate.getCombatLevelDifference();

    if (delta >= 10) {
      return plus10Color;
    }
    if (delta >= 7) {
      return plus7Color;
    }
    if (delta >= 4) {
      return plus4Color;
    }
    if (delta >= 1) {
      return plus1Color;
    }
    if (delta == 0) {
      return normalColor;
    }
    if (delta >= -3) {
      return minus1Color;
    }
    if (delta >= -6) {
      return minus4Color;
    }
    if (delta >= -9) {
      return minus7Color;
    }
    return minus10Color;
  }

  private ColorInput colorInput(
      String label, Color color, Consumer<Color> setter, NameplatesPlugin plugin) {
    return new ColorInput(
        label, color, false, setter, plugin.getColorPickerManager(), plugin.getClient());
  }

  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = new ArrayList<LabelledInput>();

    editInputs.add(
        new CheckboxInput(
            "Relative level coloring", relativeColors, (value) -> relativeColors = value));
    editInputs.add(
        colorInput("Difference >= 10", plus10Color, (value) -> plus10Color = value, plugin));
    editInputs.add(
        colorInput("Difference >= 7", plus7Color, (value) -> plus7Color = value, plugin));
    editInputs.add(
        colorInput("Difference >= 4", plus4Color, (value) -> plus4Color = value, plugin));
    editInputs.add(
        colorInput("Difference >= 1", plus1Color, (value) -> plus1Color = value, plugin));
    editInputs.add(
        colorInput("Difference == 0", normalColor, (value) -> normalColor = value, plugin));
    editInputs.add(
        colorInput("Difference <= -1", minus1Color, (value) -> minus1Color = value, plugin));
    editInputs.add(
        colorInput("Difference <= -4", minus4Color, (value) -> minus4Color = value, plugin));
    editInputs.add(
        colorInput("Difference <= -7", minus7Color, (value) -> minus7Color = value, plugin));
    editInputs.add(
        colorInput("Difference <= -10", minus10Color, (value) -> minus10Color = value, plugin));

    return editInputs;
  }
}
