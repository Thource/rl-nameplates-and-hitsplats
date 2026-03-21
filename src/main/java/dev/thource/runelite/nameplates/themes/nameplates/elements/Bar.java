package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.panel.components.PositionProviderInput;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Bar extends Element {
  protected int width;
  protected int height;
  protected int cornerRadius;
  protected int borderSize;
  protected boolean drawText;
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
    drawText = true;
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
      boolean drawText,
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
    this.drawText = drawText;
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
  public void draw(Nameplate nameplate, Graphics2D graphics, int plateX, int plateY) {
    var x = plateX + xPositionProvider.get(nameplate, width);
    var y = plateY + yPositionProvider.get(nameplate, height);

    if (borderSize > 0) {
      Rect.draw(graphics, x, y, width, height, borderColor, cornerRadius);
    }

    var innerWidth = width - borderSize * 2;
    var innerHeight = height - borderSize * 2;

    if (backgroundColor.getAlpha() > 0) {
      Rect.draw(
          graphics,
          x + borderSize,
          y + borderSize,
          innerWidth,
          innerHeight,
          backgroundColor,
          cornerRadius - borderSize);
    }

    Rect.draw(
        graphics,
        x + borderSize,
        y + borderSize,
        (int) (innerWidth * getProgress(nameplate)),
        innerHeight,
        barColorProvider.getColor(nameplate),
        cornerRadius - borderSize);

    if (drawText) {
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

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new IntInput("Width", width, 1, 999, value -> width = value));
    editInputs.add(new IntInput("Height", height, 1, 999, value -> height = value));
    editInputs.add(
        new IntInput("Corner radius", cornerRadius, 0, 999, value -> cornerRadius = value));
    editInputs.add(new IntInput("Border size", borderSize, 0, 999, value -> borderSize = value));
    editInputs.add(
        new ColorInput("Border color", borderColor, value -> borderColor = value, plugin));
    editInputs.add(new CheckboxInput("Text", drawText, value -> drawText = value));
    editInputs.add(new PositionProviderInput("Text X position", textXPositionProvider, false));
    editInputs.add(new PositionProviderInput("Text Y position", textYPositionProvider, true));
    editInputs.add(new ColorInput("Text color", textColor, value -> textColor = value, plugin));
    editInputs.add(
        new ColorInput(
            "Background color", backgroundColor, value -> backgroundColor = value, plugin));

    // todo: add consumable colors

    editInputs.addAll(barColorProvider.getEditInputs(plugin));

    // todo: add indicator colors

    return editInputs;
  }
}
