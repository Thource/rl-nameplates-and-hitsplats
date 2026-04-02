package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import dev.thource.runelite.nameplates.NameplateSkullIcon;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import net.runelite.api.coords.Direction;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.ImageUtil;

@SuperBuilder
public class Icon extends Element {
  private static final BufferedImage NO_LOOT_IMAGE =
      ImageUtil.loadImageResource(NameplatesPlugin.class, "no_loot_indicator.png");
  private static BufferedImage DOWN_ARROW_IMAGE;
  private static BufferedImage UP_ARROW_IMAGE;
  private static BufferedImage RIGHT_ARROW_IMAGE;
  private static BufferedImage LEFT_ARROW_IMAGE;
  private static BufferedImage VENGEANCE_IMAGE;

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
    VENGEANCE_IMAGE = spriteManager.getSprite(1961, 0);
  }

  @Builder.Default protected int size = 26;
  @Builder.Default protected IconType iconType = IconType.OVERHEAD;

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
      case VENGEANCE:
        if (!nameplate.hasVengeance()) {
          return null;
        }

        return VENGEANCE_IMAGE;
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
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    var direction = Direction.NORTH;
    if (xPositionProvider.getAnchor() == OffsetAnchor.END) {
      direction = Direction.EAST;
    } else if (xPositionProvider.getAnchor() == OffsetAnchor.START) {
      direction = Direction.WEST;
    } else if (yPositionProvider.getAnchor() == OffsetAnchor.START) {
      direction = Direction.SOUTH;
    }

    draw(
        nameplate,
        graphics,
        x + xPositionProvider.get(size),
        y + yPositionProvider.get(size),
        size,
        iconType,
        direction);
  }

  public static boolean shouldDraw(Nameplate nameplate, IconType iconType) {
    return getImage(nameplate, iconType, Direction.EAST) != null;
  }

  public static void draw(
      Nameplate nameplate,
      Graphics2D graphics,
      int x,
      int y,
      int size,
      IconType iconType,
      Direction direction) {

    var image = getImage(nameplate, iconType, direction);
    if (image == null) {
      return;
    }

    // Draw image scaled to fit in size x size, preserving aspect ratio and centering
    var imgW = image.getWidth();
    var imgH = image.getHeight();
    var scale = Math.min(1.0, Math.min((double) size / imgW, (double) size / imgH));
    var drawW = (int) Math.round(imgW * scale);
    var drawH = (int) Math.round(imgH * scale);
    var drawX = x + (size - drawW) / 2;
    var drawY = y + (size - drawH) / 2;
    graphics.drawImage(image, drawX, drawY, drawW, drawH, null);
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var inputs = super.getEditInputs(plugin);

    inputs.add(new IntInput("Icon size", size, 1, 999, value -> size = value));

    inputs.add(
        new DropdownInput<>("Icon type", iconType, IconType.values(), value -> iconType = value));

    return inputs;
  }
}
