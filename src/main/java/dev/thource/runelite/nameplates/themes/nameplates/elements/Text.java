package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import java.text.AttributedCharacterIterator;
import lombok.experimental.SuperBuilder;
import net.runelite.client.ui.FontManager;

@SuperBuilder
public abstract class Text extends Element {
  protected abstract String getText(Nameplate nameplate);

  protected abstract Color getColor(Nameplate nameplate);

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
        x + xPositionProvider.get((int) textBounds.getWidth()),
        y + (int) textBounds.getHeight() + yPositionProvider.get((int) textBounds.getHeight()));
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      AttributedCharacterIterator attributedCharacterIterator,
      Color color) {
    graphics.setFont(FontManager.getRunescapeSmallFont());
    var textBounds =
        graphics
            .getFont()
            .getStringBounds(
                attributedCharacterIterator,
                attributedCharacterIterator.getBeginIndex(),
                attributedCharacterIterator.getEndIndex(),
                graphics.getFontRenderContext());

    if (textBounds.getWidth() <= 0) {
      return;
    }

    graphics.setColor(color);
    graphics.drawString(
        attributedCharacterIterator,
        x + xPositionProvider.get((int) textBounds.getWidth()),
        y + (int) textBounds.getHeight() + yPositionProvider.get((int) textBounds.getHeight()));
  }
}
