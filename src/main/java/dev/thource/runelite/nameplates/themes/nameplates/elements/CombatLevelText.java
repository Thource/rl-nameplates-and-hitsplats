package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CombatLevelText extends Text {
  @Builder.Default
  protected final CombatLevelColorProvider colorProvider = new CombatLevelColorProvider();

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if (nameplate.getCombatLevel() <= 0) {
      return;
    }

    super.draw(nameplate, graphics, x, y);
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
