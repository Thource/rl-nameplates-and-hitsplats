package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ElementContainer {
  @Setter protected int xOffset;
  @Setter protected int yOffset;
  @Setter protected int width;
  @Setter protected int height;
  protected final List<Element> elements = new ArrayList<>();

  public void draw(Graphics2D graphics, Nameplate nameplate, int x, int y) {
    elements.forEach(e -> e.draw(nameplate, graphics, x + xOffset, y + yOffset));
  }

  public void add(Element element) {
    elements.add(element);
  }
}
