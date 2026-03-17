package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rect extends Element {
  protected int width;
  protected int height;
  protected Color color;

  public Rect() {
    super();

    width = 10;
    height = 10;
    color = Color.BLACK;
  }

  public Rect(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      int width,
      int height,
      Color color) {
    super(name, xPositionProvider, yPositionProvider);

    this.width = width;
    this.height = height;
    this.color = color;
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    Rect.draw(
        graphics,
        x + xPositionProvider.get(nameplate, width),
        y + yPositionProvider.get(nameplate, height),
        width,
        height,
        color);
  }

  public static void draw(Graphics2D graphics, int x, int y, int width, int height, Color color) {
    graphics.setColor(color);
    graphics.fillRect(x, y, width, height);
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new IntInput("Width", width, 1, 999, val -> width = val));
    editInputs.add(new IntInput("Height", height, 1, 999, val -> height = val));
    editInputs.add(new ColorInput("Color", color, val -> color = val, plugin));

    return editInputs;
  }
}
