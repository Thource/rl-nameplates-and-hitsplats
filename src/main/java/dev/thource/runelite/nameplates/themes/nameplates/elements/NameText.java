package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NameText extends Text {
  @Builder.Default protected NameColorProvider colorProvider = new NameColorProvider();
  @Setter @Builder.Default protected NameTextDisplayMode displayMode = NameTextDisplayMode.ALWAYS;

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if ((displayMode == NameTextDisplayMode.WITH_COMBAT_LEVEL && nameplate.getCombatLevel() <= 0)
        || (displayMode == NameTextDisplayMode.WITHOUT_COMBAT_LEVEL
            && nameplate.getCombatLevel() > 0)) {
      return;
    }

    super.draw(nameplate, graphics, x, y);
  }

  @Override
  protected String getText(Nameplate nameplate) {
    return nameplate.getName();
  }

  @Override
  protected Color getColor(Nameplate nameplate) {
    return colorProvider.getColor(nameplate);
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(
        new DropdownInput<>(
            "Display mode", displayMode, NameTextDisplayMode.values(), this::setDisplayMode));
    editInputs.addAll(colorProvider.getEditInputs(plugin));

    return editInputs;
  }
}
