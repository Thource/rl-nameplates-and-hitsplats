package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconType;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameColorProvider;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameTextDisplayMode;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.StatusText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.StatusType;
import java.awt.Color;
import java.util.List;

public class FlatDarkTheme extends NameplateTheme {
  public static final String ID = "flatDarkTheme";

  public FlatDarkTheme() {
    super(ID);

    name = "Flat Dark";
    width = 120;
    height = 12;
    order = -100;
    stacking = true;

    elements.add(
        NameText.builder()
            .name("Name without level")
            .displayMode(NameTextDisplayMode.WITHOUT_COMBAT_LEVEL)
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 60))
            .build());
    elements.add(
        NameText.builder()
            .name("Name with level")
            .displayMode(NameTextDisplayMode.WITH_COMBAT_LEVEL)
            .build());
    elements.add(
        CombatLevelText.builder()
            .name("Combat level")
            .xPositionProvider(new PositionProvider(OffsetAnchor.END, 120))
            .build());

    elements.add(
        IconContainer.builder()
            .name("Overhead vengeance")
            .xPositionProvider(new PositionProvider(OffsetAnchor.START, 124))
            .iconTypes(List.of(IconType.OVERHEAD, IconType.VENGEANCE))
            .build());
    elements.add(
        IconContainer.builder()
            .name("No-loot skull hover hint")
            .xPositionProvider(new PositionProvider(OffsetAnchor.END, -4))
            .iconTypes(
                List.of(IconType.NO_LOOT, IconType.SKULL, IconType.HOVERED, IconType.HINT_ARROW))
            .build());

    elements.add(
        HealthBar.builder()
            .name("Health bar")
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 12))
            .build());
    elements.add(
        StatusText.builder()
            .name("Health text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 60))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 19))
            .build());

    elements.add(
        PrayerBar.builder()
            .name("Prayer bar")
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 24))
            .build());
    elements.add(
        StatusText.builder()
            .name("Prayer text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 60))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 31))
            .statusType(StatusType.PRAYER)
            .build());
  }
}
