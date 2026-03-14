package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Icon;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconType;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Text;
import java.awt.Color;
import java.util.List;

public class FlatDarkTheme extends NameplateTheme {
  public FlatDarkTheme() {
    name = "Flat Dark";
    width = 120;
    height = 26;
    heightWithPrayerBar = 38;
    stacking = true;

    elements.add(
        new Text(
            "Name",
            new PositionProvider(OffsetAnchor.START, 0),
            new PositionProvider(OffsetAnchor.START, 0),
            Color.WHITE,
            new NameplateTextProvider(NameplateTextType.NAME)));
    elements.add(
        new Text(
            "Combat level",
            new PositionProvider(OffsetAnchor.END, 120),
            new PositionProvider(OffsetAnchor.START, 0),
            Color.WHITE,
            new NameplateTextProvider(NameplateTextType.COMBAT_LEVEL)));
    elements.add(
        new Icon(
            "Overhead",
            new PositionProvider(OffsetAnchor.START, 124),
            new PositionProvider(OffsetAnchor.START, 0),
            new DimensionProvider(DimensionCalcType.STATIC, 26),
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
