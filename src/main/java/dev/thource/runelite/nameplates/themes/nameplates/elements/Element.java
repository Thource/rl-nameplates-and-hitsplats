package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.panel.Nameable;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Element implements Nameable {
  protected final String elementType = getClass().getSimpleName();
  protected String name;
  protected final PositionProvider xPositionProvider;
  protected final PositionProvider yPositionProvider;

  protected Element() {
    name = getClass().getSimpleName();
    xPositionProvider = new PositionProvider();
    yPositionProvider = new PositionProvider();
  }

  protected Element(
      String name, PositionProvider xPositionProvider, PositionProvider yPositionProvider) {
    this.name = name;
    this.xPositionProvider = xPositionProvider;
    this.yPositionProvider = yPositionProvider;
  }

  public abstract void draw(
      Nameplate nameplate, Graphics2D graphics, int x, int y, int plateWidth, int plateHeight);
}
