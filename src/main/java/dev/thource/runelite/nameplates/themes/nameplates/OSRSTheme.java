package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBarColorProvider;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconType;
import java.awt.Color;
import java.util.List;

public class OSRSTheme extends NameplateTheme {
  public static final String ID = "osrsTheme";

  public OSRSTheme() {
    super(ID);

    name = "OSRS";
    width = 30;
    height = 4;
    heightWithPrayerBar = 4;
    order = -99;

    elements.add(
        new IconContainer(
            "Overhead skull hint",
            new PositionProvider(OffsetAnchor.MIDDLE, 15),
            new PositionProvider(OffsetAnchor.END, -4),
            26,
            4,
            true,
            List.of(IconType.HINT_ARROW, IconType.SKULL, IconType.OVERHEAD)));

    elements.add(
        new HealthBar(
            "Health bar",
            new PositionProvider(OffsetAnchor.START, 0),
            new PositionProvider(OffsetAnchor.START, 0),
            30,
            4,
            0,
            false,
            new PositionProvider(OffsetAnchor.START, 0),
            new PositionProvider(OffsetAnchor.START, 0),
            Color.WHITE,
            Color.RED,
            Color.GREEN,
            new HealthBarColorProvider(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)));
  }
}
