package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import dev.thource.runelite.nameplates.NameplateSkullIcon;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.DimensionProvider;
import dev.thource.runelite.nameplates.themes.nameplates.ImageFill;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import net.runelite.api.coords.Direction;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.ImageUtil;

public class Icon extends Element {
  private static final BufferedImage NO_LOOT_IMAGE =
      ImageUtil.loadImageResource(NameplatesPlugin.class, "no_loot_indicator.png");
  private static BufferedImage DOWN_ARROW_IMAGE;
  private static BufferedImage UP_ARROW_IMAGE;
  private static BufferedImage RIGHT_ARROW_IMAGE;
  private static BufferedImage LEFT_ARROW_IMAGE;

  public static void initImages(SpriteManager spriteManager) {
    DOWN_ARROW_IMAGE = spriteManager.getSprite(441, 0);
    RIGHT_ARROW_IMAGE = spriteManager.getSprite(772, 0);
    // Initialize LEFT_ARROW_IMAGE and UP_ARROW_IMAGE by rotating the existing ones
    if (RIGHT_ARROW_IMAGE != null) {
      LEFT_ARROW_IMAGE = ImageUtil.rotateImage(RIGHT_ARROW_IMAGE, Math.PI); // 180 degrees
    }
    if (DOWN_ARROW_IMAGE != null) {
      UP_ARROW_IMAGE = ImageUtil.rotateImage(DOWN_ARROW_IMAGE, Math.PI); // 180 degrees
    }
  }

  protected int size;
  protected IconType iconType;
  protected final transient ImageFill imageFill = new ImageFill();

  public Icon() {
    super();

    size = 26;
    iconType = IconType.OVERHEAD;
  }

  public Icon(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      int size,
      IconType iconType) {
    super(name, xPositionProvider, yPositionProvider);

    this.size = size;
    this.iconType = iconType;
  }

  private static BufferedImage getImage(
      Nameplate nameplate, IconType iconType, Direction direction) {
    switch (iconType) {
      case OVERHEAD:
        var overheadIcon = NameplateHeadIcon.get(nameplate.getActor());
        if (overheadIcon == null || overheadIcon == NameplateHeadIcon.NONE) {
          break;
        }

        return overheadIcon.getImage();
      case SKULL:
        var skullIcon = NameplateSkullIcon.get(nameplate.getActor());
        if (skullIcon == null || skullIcon == NameplateSkullIcon.NONE) {
          break;
        }

        return skullIcon.getImage();
      case NO_LOOT:
        if (!nameplate.isNoLoot()) {
          return null;
        }

        return NO_LOOT_IMAGE;
      case HOVERED:
      case HINT_ARROW:
        if (iconType == IconType.HINT_ARROW) {
          if (!nameplate.hasHintArrow()) {
            return null;
          }
        } else {
          if (!nameplate.isHovered()) {
            return null;
          }
        }

        if (direction == Direction.WEST) {
          return LEFT_ARROW_IMAGE;
        } else if (direction == Direction.EAST) {
          return RIGHT_ARROW_IMAGE;
        } else if (direction == Direction.NORTH) {
          return UP_ARROW_IMAGE;
        }

        return DOWN_ARROW_IMAGE;
    }

    return null;
  }

  @Override
  public void draw(
      Nameplate nameplate, Graphics2D graphics, int x, int y, int plateWidth, int plateHeight) {
    this.draw(nameplate, graphics, x, y, plateWidth, plateHeight, iconType, null);
  }

  public static boolean shouldDraw(Nameplate nameplate, IconType iconType) {
    return getImage(nameplate, iconType, Direction.EAST) != null;
  }

  public void draw(
      Nameplate nameplate,
      Graphics2D graphics,
      int x,
      int y,
      int plateWidth,
      int plateHeight,
      IconType iconType,
      Direction direction) {
    var image = getImage(nameplate, iconType, direction);
    if (image == null) {
      return;
    }

    var xOffset = 0;
    if (this.xPositionProvider.getAnchor() == OffsetAnchor.MIDDLE) {
      xOffset = -size / 2;
    } else if (this.xPositionProvider.getAnchor() == OffsetAnchor.END) {
      xOffset = -size;
    }

    var yOffset = 0;
    if (this.yPositionProvider.getAnchor() == OffsetAnchor.MIDDLE) {
      yOffset = -size / 2;
    } else if (this.yPositionProvider.getAnchor() == OffsetAnchor.END) {
      yOffset = -size;
    }

    imageFill.setImage(image);

    // Draw image scaled to fit in size x size, preserving aspect ratio and centering
    int imgW = image.getWidth();
    int imgH = image.getHeight();
    double scale = Math.max(1.0, Math.min((double) size / imgW, (double) size / imgH));
    int drawW = (int) Math.round(imgW * scale);
    int drawH = (int) Math.round(imgH * scale);
    int drawX = x + xPositionProvider.getValue() + xOffset + (size - drawW) / 2;
    int drawY = y + yPositionProvider.getValue() + yOffset + (size - drawH) / 2;
    graphics.drawImage(image, drawX, drawY, drawW, drawH, null);
  }

  @Override
  public List<LabelledInput> getEditInputs() {
    var inputs = super.getEditInputs();

    inputs.add(
        new IntInput(
            "Size",
            size,
            1,
            999,
            value -> {
              size = value;
            }));

    inputs.add(
        new DropdownInput<IconType>(
            "Icon type",
            iconType,
            IconType.values(),
            value -> {
              iconType = value;
            }));

    return inputs;
  }
}
