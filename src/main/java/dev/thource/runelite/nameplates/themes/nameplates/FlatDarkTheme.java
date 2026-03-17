package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelColorProvider;
import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Icon;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconType;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameColorProvider;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import java.awt.Color;
import java.util.List;

public class FlatDarkTheme extends NameplateTheme {
  public static final String ID = "flatDarkTheme";

  public FlatDarkTheme() {
    super(ID);

    name = "Flat Dark";
    width = 120;
    height = 26;
    heightWithPrayerBar = 38;
    order = -100;
    stacking = true;

    elements.add(
        new NameText(
            "Name",
            new PositionProvider(OffsetAnchor.START, 0),
            new PositionProvider(OffsetAnchor.START, 0),
            new NameColorProvider(Color.WHITE)));
    elements.add(
        new CombatLevelText(
            "Combat level",
            new PositionProvider(OffsetAnchor.END, 120),
            new PositionProvider(OffsetAnchor.START, 0),
            new CombatLevelColorProvider()));
    elements.add(
        new Icon(
            "Overhead",
            new PositionProvider(OffsetAnchor.START, 124),
            new PositionProvider(OffsetAnchor.START, 0),
            26,
            IconType.OVERHEAD));
    elements.add(
        new IconContainer(
            "No-loot skull hover hint",
            new PositionProvider(OffsetAnchor.END, -4),
            new PositionProvider(OffsetAnchor.START, 0),
            26,
            4,
            false,
            List.of(IconType.NO_LOOT, IconType.SKULL, IconType.HOVERED, IconType.HINT_ARROW)));

    var healthBar = new HealthBar();
    healthBar.getYPositionProvider().setValue(12);
    elements.add(healthBar);

    var prayerBar = new PrayerBar();
    prayerBar.getYPositionProvider().setValue(24);
    elements.add(prayerBar);
  }
}
