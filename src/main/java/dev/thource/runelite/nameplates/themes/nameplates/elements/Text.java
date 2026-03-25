package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
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

    draw(graphics, x, y, xPositionProvider, yPositionProvider, text, getColor(nameplate));
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      String text,
      Color color) {
    if (text.trim().isEmpty()) {
      return;
    }

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
