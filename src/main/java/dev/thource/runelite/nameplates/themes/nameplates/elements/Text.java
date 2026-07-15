package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
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
  @Builder.Default protected boolean dropShadow = true;
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

    draw(
        graphics,
        x,
        y,
        xPositionProvider,
        yPositionProvider,
        text,
        getColor(nameplate),
        font,
        dropShadow);
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      String text,
      Color color,
      Font font,
      boolean dropShadow) {
    if (text.trim().isEmpty()) {
      return;
    }

    graphics.setFont(font);
    var fontMetrics = graphics.getFontMetrics();
    var textBounds = fontMetrics.getStringBounds(text, graphics);
    var textX = x + xPositionProvider.get((int) textBounds.getWidth());
    var textY =
        y + (int) textBounds.getHeight() + yPositionProvider.get((int) textBounds.getHeight());
    if (dropShadow) {
      drawDropShadow(graphics, text, textX, textY);
    }

    graphics.setColor(color);
    graphics.drawString(text, textX, textY);
  }

  public static void draw(
      Graphics2D graphics,
      int x,
      int y,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      AttributedCharacterIterator attributedCharacterIterator,
      Color color,
      Font font,
      boolean dropShadow) {
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

    var textX = x + xPositionProvider.get((int) textBounds.getWidth());
    var textY =
        y + (int) textBounds.getHeight() + yPositionProvider.get((int) textBounds.getHeight());

    if (dropShadow) {
      var textBuilder = new StringBuilder();
      for (char c = attributedCharacterIterator.first();
          c != AttributedCharacterIterator.DONE;
          c = attributedCharacterIterator.next()) {
        textBuilder.append(c);
      }
      var text = textBuilder.toString();

      drawDropShadow(graphics, text, textX, textY);
    }

    graphics.setColor(color);
    graphics.drawString(attributedCharacterIterator, textX, textY);
  }

  protected static void drawDropShadow(Graphics2D graphics, String text, int textX, int textY) {
    graphics.setColor(Color.BLACK);
    graphics.drawString(text, textX + 1, textY + 1);
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

    inputs.add(new CheckboxInput("Drop shadow", dropShadow, val -> dropShadow = val));

    return inputs;
  }
}
