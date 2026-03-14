package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTextProvider;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import net.runelite.client.ui.FontManager;

public class Text extends Element {
  protected Color color;
  protected NameplateTextProvider textProvider;

  public Text(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      Color color,
      NameplateTextProvider textProvider) {
    super(name, xPositionProvider, yPositionProvider);

    this.color = color;
    this.textProvider = textProvider;
  }

  @Override
  public void draw(
      Nameplate nameplate, Graphics2D graphics, int x, int y, int plateWidth, int plateHeight) {
    var text = textProvider.get(nameplate);
    if (text == null || text.trim().isEmpty()) {
      return;
    }

    graphics.setFont(FontManager.getRunescapeSmallFont());
    var fontMetrics = graphics.getFontMetrics();
    var textBounds = fontMetrics.getStringBounds(text, graphics);
    graphics.setColor(color);

    var textXOffset = -1;
    if (this.xPositionProvider.getAnchor() == OffsetAnchor.MIDDLE) {
      textXOffset = (int) -textBounds.getWidth() / 2;
    } else if (this.xPositionProvider.getAnchor() == OffsetAnchor.END) {
      textXOffset = (int) -textBounds.getWidth();
    }

    var textYOffset = (int) textBounds.getHeight() - 2;
    if (this.yPositionProvider.getAnchor() == OffsetAnchor.MIDDLE) {
      textYOffset = (int) textBounds.getHeight() / 2;
    } else if (this.yPositionProvider.getAnchor() == OffsetAnchor.END) {
      textYOffset = -2;
    }

    graphics.drawString(
        text,
        x + xPositionProvider.getValue() + textXOffset,
        y + yPositionProvider.getValue() + textYOffset);
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      String text,
      Color color) {
    graphics.setFont(FontManager.getRunescapeSmallFont());
    var fontMetrics = graphics.getFontMetrics();
    var textBounds = fontMetrics.getStringBounds(text, graphics);
    graphics.setColor(color);
    graphics.drawString(
        text,
        x + xPositionProvider.get(null, (int) textBounds.getWidth()),
        y
            + (int) textBounds.getHeight()
            + yPositionProvider.get(null, (int) textBounds.getHeight()));
  }
}
