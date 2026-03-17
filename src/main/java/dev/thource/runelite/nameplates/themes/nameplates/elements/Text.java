package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import net.runelite.client.ui.FontManager;

public abstract class Text extends Element {
  protected abstract String getText(Nameplate nameplate);

  protected abstract Color getColor(Nameplate nameplate);

  public Text() {
    super();
  }

  public Text(String name, PositionProvider xPositionProvider, PositionProvider yPositionProvider) {
    super(name, xPositionProvider, yPositionProvider);
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    var text = getText(nameplate);
    if (text.trim().isEmpty()) {
      return;
    }

    graphics.setFont(FontManager.getRunescapeSmallFont());
    var fontMetrics = graphics.getFontMetrics();
    var textBounds = fontMetrics.getStringBounds(text, graphics);
    graphics.setColor(getColor(nameplate));

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
