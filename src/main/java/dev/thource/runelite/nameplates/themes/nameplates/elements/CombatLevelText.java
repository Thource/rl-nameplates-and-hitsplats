package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.util.List;

public class CombatLevelText extends Text {
  protected final CombatLevelColorProvider colorProvider;

  public CombatLevelText() {
    super();

    colorProvider = new CombatLevelColorProvider();
  }

  public CombatLevelText(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      CombatLevelColorProvider colorProvider) {
    super(name, xPositionProvider, yPositionProvider);

    this.colorProvider = colorProvider;
  }

  @Override
  protected String getText(Nameplate nameplate) {
    return String.valueOf(nameplate.getCombatLevel());
  }

  @Override
  protected Color getColor(Nameplate nameplate) {
    return colorProvider.getColor(nameplate);
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.addAll(colorProvider.getEditInputs(plugin));

    return editInputs;
  }
}
