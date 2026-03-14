package dev.thource.runelite.nameplates.themes.nameplates;

import java.awt.Color;
import java.awt.Graphics2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorFill extends FillType {
  protected Color color;

  public ColorFill(Color color) {
    this.color = color;
  }

  @Override
  public void draw(Graphics2D graphics, int x, int y, int width, int height) {
    graphics.setColor(color);
    graphics.fillRect(x, y, width, height);
  }
}
