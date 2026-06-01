package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.FontFamily;
import dev.thource.runelite.nameplates.panel.components.FontStyleInput;
import dev.thource.runelite.nameplates.panel.components.Fonts;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import net.runelite.client.ui.FontManager;

@SuperBuilder
public abstract class Text extends Element {
  @Builder.Default protected String fontFamily = FontManager.getRunescapeSmallFont().getFamily();
  @Builder.Default protected int fontSize = FontManager.getRunescapeSmallFont().getSize();
  @Builder.Default protected int fontStyle = 0;
  protected transient Font font;

  protected abstract String getText(Nameplate nameplate);

  protected abstract Color getColor(Nameplate nameplate);

  protected String getFontFamily() {
    // If the font family is null (like for themes created before this update), fallback to the
    // default.
    return Objects.requireNonNullElse(fontFamily, FontManager.getRunescapeSmallFont().getFamily());
  }

  protected int getFontSize() {
    // If the font size is 0 (like for themes created before this update), fallback to the default.
    return fontSize == 0 ? FontManager.getRunescapeSmallFont().getSize() : fontSize;
  }

  protected void loadFont() {
    font = FontManager.getFallbackFont(getFontFamily(), fontStyle, getFontSize());
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if (font == null) {
      loadFont();
    }

    var text = getText(nameplate);
    if (text.trim().isEmpty() || text.equals("null")) {
      return;
    }

    draw(graphics, x, y, xPositionProvider, yPositionProvider, text, getColor(nameplate), font);
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      String text,
      Color color,
      Font font) {
    if (text.trim().isEmpty()) {
      return;
    }

    graphics.setFont(font);
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
      Color color,
      Font font) {
    graphics.setFont(font);
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

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var inputs = super.getEditInputs(plugin);

    inputs.add(
        new DropdownInput<>(
            "Text font",
            Fonts.getFontFamilies().stream()
                .filter(f -> f.getName().equals(getFontFamily()))
                .findFirst()
                .orElse(Fonts.getFontFamilies().get(0)),
            Fonts.getFontFamilies().toArray(new FontFamily[0]),
            fontFamily -> {
              this.fontFamily = fontFamily.getName();
              font = null;
            }));

    inputs.add(
        new IntInput(
            "Text size",
            getFontSize(),
            1,
            100,
            val -> {
              fontSize = val;
              font = null;
            }));

    inputs.add(
        new FontStyleInput(
            "Font style",
            fontStyle,
            val -> {
              fontStyle = val;
              font = null;
            }));

    return inputs;
  }
}
