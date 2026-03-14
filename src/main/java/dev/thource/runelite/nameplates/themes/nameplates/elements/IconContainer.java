package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.coords.Direction;

public class IconContainer extends Element {
  protected int iconSize;
  protected int padding;
  protected boolean isVertical = false;
  protected final List<IconType> iconTypes = new ArrayList<>();
  protected final transient Icon icon = new Icon();

  public IconContainer() {
    super();

    iconSize = 26;
    padding = 4;
  }

  public IconContainer(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      int iconSize,
      int padding,
      boolean isVertical,
      List<IconType> iconTypes) {
    super(name, xPositionProvider, yPositionProvider);

    this.iconSize = iconSize;
    this.padding = padding;
    this.isVertical = isVertical;
    this.iconTypes.addAll(iconTypes);
  }

  @Override
  public void draw(
      Nameplate nameplate, Graphics2D graphics, int x, int y, int plateWidth, int plateHeight) {
    var iconsToDraw =
        iconTypes.stream()
            .filter(iconType -> Icon.shouldDraw(nameplate, iconType))
            .toArray(IconType[]::new);
    var totalSize = iconSize * iconsToDraw.length + (iconsToDraw.length - 1) * padding;

    if (isVertical) {
      x += xPositionProvider.get(nameplate, iconSize);
      y += yPositionProvider.get(nameplate, totalSize);
    } else {
      x += xPositionProvider.get(nameplate, totalSize);
      y += yPositionProvider.get(nameplate, iconSize);
    }

    var direction = Direction.SOUTH;
    if (isVertical) {
      if (yPositionProvider.getAnchor() == OffsetAnchor.START) {
        direction = Direction.NORTH;
      }
    } else {
      if (xPositionProvider.getAnchor() == OffsetAnchor.END) {
        direction = Direction.EAST;
      } else {
        direction = Direction.WEST;
      }
    }

    for (IconType type : iconsToDraw) {
      icon.draw(nameplate, graphics, x, y, plateWidth, plateHeight, type, direction);

      if (isVertical) {
        y += iconSize + padding;
      } else {
        x += iconSize + padding;
      }
    }
  }
}
