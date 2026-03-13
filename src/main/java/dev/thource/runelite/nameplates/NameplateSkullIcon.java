package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Actor;
import net.runelite.api.Player;
import net.runelite.api.SkullIcon;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;

@RequiredArgsConstructor
public enum NameplateSkullIcon implements Nameable {
  NONE(-1, "None"),
  SKULL(SkullIcon.SKULL, "Skull"),
  SKULL_FIGHT_PIT(SkullIcon.SKULL_FIGHT_PIT, "Fight Pit"),
  SKULL_HIGH_RISK(SkullIcon.SKULL_HIGH_RISK, "High Risk"),
  SKULL_DEADMAN(SkullIcon.SKULL_DEADMAN, "Deadman"),
  LOOT_KEYS_ONE(SkullIcon.LOOT_KEYS_ONE, "1 Key"),
  LOOT_KEYS_TWO(SkullIcon.LOOT_KEYS_TWO, "2 Keys"),
  LOOT_KEYS_THREE(SkullIcon.LOOT_KEYS_THREE, "3 Keys"),
  LOOT_KEYS_FOUR(SkullIcon.LOOT_KEYS_FOUR, "4 Keys"),
  LOOT_KEYS_FIVE(SkullIcon.LOOT_KEYS_FIVE, "5 Keys"),
  FORINTHRY_SURGE(SkullIcon.FORINTHRY_SURGE, "Forinthry Surge"),
  FORINTHRY_SURGE_DEADMAN(SkullIcon.FORINTHRY_SURGE_DEADMAN, "Deadman (fs)"),
  FORINTHRY_SURGE_KEYS_ONE(SkullIcon.FORINTHRY_SURGE_KEYS_ONE, "1 Key (fs)"),
  FORINTHRY_SURGE_KEYS_TWO(SkullIcon.FORINTHRY_SURGE_KEYS_TWO, "2 Keys (fs)"),
  FORINTHRY_SURGE_KEYS_THREE(SkullIcon.FORINTHRY_SURGE_KEYS_THREE, "3 Keys (fs)"),
  FORINTHRY_SURGE_KEYS_FOUR(SkullIcon.FORINTHRY_SURGE_KEYS_FOUR, "4 Keys (fs)"),
  FORINTHRY_SURGE_KEYS_FIVE(SkullIcon.FORINTHRY_SURGE_KEYS_FIVE, "5 Keys (fs)"),;

  @Getter private final int skullIcon;
  @Getter private final String name;
  @Getter private BufferedImage image;

  private static final Map<Integer, NameplateSkullIcon> map;

  static {
    map = new HashMap<>();
    for (NameplateSkullIcon v : NameplateSkullIcon.values()) {
      map.put(v.skullIcon, v);
    }
  }

  public static NameplateSkullIcon get(int skullIcon) {
    return map.get(skullIcon);
  }
  public static NameplateSkullIcon get(Player player) {
    return map.get(player.getSkullIcon());
  }

  public static NameplateSkullIcon get(Actor actor) {
    if (actor instanceof Player) {
      return get((Player) actor);
    }

    return null;
  }

  public void loadImage(SpriteManager spriteManager) {
    if (image != null || skullIcon == -1) {
      return;
    }

    image = spriteManager.getSprite(SpriteID.PLAYER_KILLER_SKULL, skullIcon);
  }
}
