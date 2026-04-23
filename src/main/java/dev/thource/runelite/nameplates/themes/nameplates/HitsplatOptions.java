package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.Color;
import java.awt.Graphics2D;
import lombok.Builder;
import net.runelite.api.HitsplatID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;

public class HitsplatOptions implements Nameable {
  protected final int hitsplatType;
  protected SpriteOrImage background;

  public HitsplatOptions(int hitsplatType, int spriteId) {
    this.hitsplatType = hitsplatType;
    this.background = new SpriteOrImage(spriteId);
  }

  @Override
  public String getName() {
    switch (hitsplatType) {
      case HitsplatID.BLOCK_ME:
        return "Block (Me)";
      case HitsplatID.BLOCK_OTHER:
        return "Block (Other)";
      case HitsplatID.DAMAGE_ME:
        return "Damage (Me)";
      case HitsplatID.DAMAGE_OTHER:
        return "Damage (Other)";
      case HitsplatID.POISON:
        return "Poison";
      case HitsplatID.DISEASE:
        return "Disease";
      case HitsplatID.DISEASE_BLOCKED:
        return "Disease Blocked";
      case HitsplatID.VENOM:
        return "Venom";
      case HitsplatID.HEAL:
        return "Heal";
      case HitsplatID.CYAN_UP:
        return "Cyan Up";
      case HitsplatID.CYAN_DOWN:
        return "Cyan Down";
      case HitsplatID.DAMAGE_ME_CYAN:
        return "Damage (Me, Cyan)";
      case HitsplatID.DAMAGE_OTHER_CYAN:
        return "Damage (Other, Cyan)";
      case HitsplatID.DAMAGE_ME_ORANGE:
        return "Damage (Me, Orange)";
      case HitsplatID.DAMAGE_OTHER_ORANGE:
        return "Damage (Other, Orange)";
      case HitsplatID.DAMAGE_ME_YELLOW:
        return "Damage (Me, Yellow)";
      case HitsplatID.DAMAGE_OTHER_YELLOW:
        return "Damage (Other, Yellow)";
      case HitsplatID.DAMAGE_ME_WHITE:
        return "Damage (Me, White)";
      case HitsplatID.DAMAGE_OTHER_WHITE:
        return "Damage (Other, White)";
      case HitsplatID.DAMAGE_MAX_ME:
        return "Damage (Max, Me)";
      case HitsplatID.DAMAGE_MAX_ME_CYAN:
        return "Damage (Max, Me, Cyan)";
      case HitsplatID.DAMAGE_MAX_ME_ORANGE:
        return "Damage (Max, Me, Orange)";
      case HitsplatID.DAMAGE_MAX_ME_YELLOW:
        return "Damage (Max, Me, Yellow)";
      case HitsplatID.DAMAGE_MAX_ME_WHITE:
        return "Damage (Max, Me, White)";
      case HitsplatID.DAMAGE_ME_POISE:
        return "Damage (Me, Poison)";
      case HitsplatID.DAMAGE_OTHER_POISE:
        return "Damage (Other, Poison)";
      case HitsplatID.DAMAGE_MAX_ME_POISE:
        return "Damage (Max, Me, Poison)";
      case HitsplatID.CORRUPTION:
        return "Corruption";
      case HitsplatID.PRAYER_DRAIN:
        return "Prayer Drain";
      case HitsplatID.BLEED:
        return "Bleed";
      case HitsplatID.SANITY_DRAIN:
        return "Sanity Drain";
      case HitsplatID.SANITY_RESTORE:
        return "Sanity Restore";
      case HitsplatID.DOOM:
        return "Doom";
      case HitsplatID.BURN:
        return "Burn";
      default:
        return "Unknown";
    }
  }

  public void draw(
      Graphics2D graphics, String hitsplatAmount, int centerX, int centerY, int width, int height) {
    var adjustedX = centerX - width / 2;
    var adjustedY = centerY - height / 2;

    if (background != null) {
      background.draw(graphics, adjustedX, adjustedY, width, height);
    }

    graphics.setFont(FontManager.getRunescapeSmallFont());
    var fontMetrics = graphics.getFontMetrics();
    var textBounds = fontMetrics.getStringBounds(hitsplatAmount, graphics);

    var textX = centerX - (int) (textBounds.getWidth() / 2);
    var textY = centerY + (int) (textBounds.getHeight() / 2);
    graphics.setColor(Color.BLACK);
    graphics.drawString(hitsplatAmount, textX + 1, textY + 1);
    graphics.setColor(Color.WHITE);
    graphics.drawString(hitsplatAmount, textX, textY);
  }
}
