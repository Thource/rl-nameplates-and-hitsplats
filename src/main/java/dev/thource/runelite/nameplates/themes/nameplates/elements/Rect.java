package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Rect extends Element {
  @Builder.Default protected int width = 10;
  @Builder.Default protected int height = 10;
  @Builder.Default protected int cornerRadius = 0;
  @Builder.Default protected Color color = Color.BLACK;

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    Rect.draw(
        graphics,
        x + xPositionProvider.get(width),
        y + yPositionProvider.get(height),
        width,
        height,
        color,
        cornerRadius);
  }

  public static void draw(
      Graphics2D graphics, int x, int y, int width, int height, Color color, int cornerRadius) {
    graphics.setColor(color);
    if (cornerRadius > 0) {
      graphics.setRenderingHint(
          java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.fillRoundRect(x, y, width, height, cornerRadius * 2, cornerRadius * 2);
      graphics.setRenderingHint(
          java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
    } else {
      graphics.fillRect(x, y, width, height);
    }
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new IntInput("Width", width, 1, 999, val -> width = val));
    editInputs.add(new IntInput("Height", height, 1, 999, val -> height = val));
    editInputs.add(new IntInput("Corner radius", cornerRadius, 0, 999, val -> cornerRadius = val));
    editInputs.add(new ColorInput("Color", color, val -> color = val, plugin));

    return editInputs;
  }
}
