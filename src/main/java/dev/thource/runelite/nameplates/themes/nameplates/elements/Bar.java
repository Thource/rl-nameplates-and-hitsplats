package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Bar extends Element {
  protected int width;
  protected int height;
  protected int borderSize;
  protected final PositionProvider textXPositionProvider;
  protected final PositionProvider textYPositionProvider;
  protected Color borderColor;
  protected Color backgroundColor;
  protected Color textColor;
  protected BarColorProvider barColorProvider;

  public Bar() {
    super();

    width = 120;
    height = 14;
    borderSize = 2;
    textXPositionProvider = new PositionProvider(OffsetAnchor.MIDDLE, 60);
    textYPositionProvider = new PositionProvider(OffsetAnchor.MIDDLE, 7);
    borderColor = new Color(0.1f, 0.1f, 0.1f);
    backgroundColor = new Color(0.3f, 0.3f, 0.3f);
    textColor = Color.WHITE;
  }

  public Bar(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      int width,
      int height,
      int borderSize,
      PositionProvider textXPositionProvider,
      PositionProvider textYPositionProvider,
      Color borderColor,
      Color backgroundColor,
      Color textColor,
      BarColorProvider barColorProvider) {
    super(name, xPositionProvider, yPositionProvider);

    this.width = width;
    this.height = height;
    this.borderSize = borderSize;
    this.textXPositionProvider = textXPositionProvider;
    this.textYPositionProvider = textYPositionProvider;
    this.borderColor = borderColor;
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
    this.barColorProvider = barColorProvider;
  }

  protected abstract int getCurrentValue(Nameplate nameplate);

  protected abstract int getMaxValue(Nameplate nameplate);

  protected String getText(Nameplate nameplate) {
    return getCurrentValue(nameplate) + " / " + getMaxValue(nameplate);
  }

  protected float getProgress(Nameplate nameplate) {
    return (float) getCurrentValue(nameplate) / (float) getMaxValue(nameplate);
  }

  @Override
  public void draw(
      Nameplate nameplate,
      Graphics2D graphics,
      int plateX,
      int plateY,
      int plateWidth,
      int plateHeight) {
    var x = plateX + xPositionProvider.get(nameplate, width);
    var y = plateY + yPositionProvider.get(nameplate, height);

    if (borderSize > 0) {
      Rect.draw(graphics, x, y, width, height, borderColor);
    }

    var innerWidth = width - borderSize * 2;
    var innerHeight = height - borderSize * 2;

    if (backgroundColor.getAlpha() > 0) {
      Rect.draw(graphics, x + borderSize, y + borderSize, innerWidth, innerHeight, backgroundColor);
    }

    Rect.draw(
        graphics,
        x + borderSize,
        y + borderSize,
        (int) (innerWidth * getProgress(nameplate)),
        innerHeight,
        barColorProvider.getColor(nameplate));

    Text.draw(
        graphics,
        x,
        y,
        textXPositionProvider,
        textYPositionProvider,
        getText(nameplate),
        textColor);
  }
}
