package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.DimensionProvider;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;

public class Rect extends Element {
  protected final DimensionProvider widthProvider;
  protected final DimensionProvider heightProvider;
  protected Color color;

  public Rect() {
    super();

    widthProvider = new DimensionProvider();
    heightProvider = new DimensionProvider();
    color = Color.BLACK;
  }

  public Rect(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      DimensionProvider widthProvider,
      DimensionProvider heightProvider,
      Color color) {
    super(name, xPositionProvider, yPositionProvider);

    this.widthProvider = widthProvider;
    this.heightProvider = heightProvider;
    this.color = color;
  }

  private int getWidth(Nameplate nameplate) {
    return widthProvider.get(nameplate);
  }

  private int getHeight(Nameplate nameplate) {
    return heightProvider.get(nameplate);
  }

  @Override
  public void draw(
      Nameplate nameplate, Graphics2D graphics, int x, int y, int plateWidth, int plateHeight) {
    var width = getWidth(nameplate);
    var height = getHeight(nameplate);

    Rect.draw(
        graphics,
        x + xPositionProvider.get(nameplate, plateWidth),
        y + yPositionProvider.get(nameplate, plateHeight),
        width,
        height,
        color);
  }

  public static void draw(Graphics2D graphics, int x, int y, int width, int height, Color color) {
    graphics.setColor(color);
    graphics.fillRect(x, y, width, height);
  }
}
