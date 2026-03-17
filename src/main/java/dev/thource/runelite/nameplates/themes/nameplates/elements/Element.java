package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.Nameable;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.panel.components.PositionProviderInput;
import dev.thource.runelite.nameplates.panel.components.StringInput;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
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

  public abstract void draw(Nameplate nameplate, Graphics2D graphics, int x, int y);

  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var list = new ArrayList<LabelledInput>();

    list.add(new StringInput("Name", name, this::setName));
    list.add(new PositionProviderInput("X position", xPositionProvider, false));
    list.add(new PositionProviderInput("Y position", yPositionProvider, true));

    return list;
  }
}
