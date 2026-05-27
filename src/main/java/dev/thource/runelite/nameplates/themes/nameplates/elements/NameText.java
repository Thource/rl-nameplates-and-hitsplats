package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.SwingUtilities;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.client.ui.FontManager;

@SuperBuilder
public class NameText extends Text {
  @Setter @Builder.Default protected int maxWidth = 0;
  @Builder.Default protected NameColorProvider colorProvider = new NameColorProvider();
  @Setter @Builder.Default protected NameTextDisplayMode displayMode = NameTextDisplayMode.ALWAYS;

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if ((displayMode == NameTextDisplayMode.WITH_COMBAT_LEVEL && nameplate.getCombatLevel() <= 0)
        || (displayMode == NameTextDisplayMode.WITHOUT_COMBAT_LEVEL
            && nameplate.getCombatLevel() > 0)) {
      return;
    }

    var text = getText(nameplate);
    if (text.trim().isEmpty() || text.equals("null")) {
      return;
    }

    if (maxWidth > 0) {
      graphics.setFont(FontManager.getRunescapeSmallFont());
      var fontMetrics = graphics.getFontMetrics();
      text =
          SwingUtilities.layoutCompoundLabel(
              fontMetrics,
              text,
              null,
              0,
              0,
              0,
              0,
              new Rectangle(0, 0, maxWidth, 1000),
              new Rectangle(0, 0, 0, 0),
              new Rectangle(0, 0, 1000, 100),
              0);
    }

    draw(graphics, x, y, xPositionProvider, yPositionProvider, text, getColor(nameplate));
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
    editInputs.add(new IntInput("Max width", maxWidth, 0, 1000, this::setMaxWidth, "px"));
    editInputs.addAll(colorProvider.getEditInputs(plugin));

    return editInputs;
  }
}
