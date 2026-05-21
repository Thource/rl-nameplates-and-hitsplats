package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.EnergyBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconType;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameTextDisplayMode;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.SpecialBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.StatusText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.StatusType;
import java.util.List;

public class FlatDarkFullInfoTheme extends NameplateTheme {
  public static final String ID = "flatDarkFullInfoTheme";

  public FlatDarkFullInfoTheme() {
    super(ID);

    name = "Flat Dark Full Info";
    width = 60;
    height = 13;
    order = -100;
    stacking = true;

    elements.add(
        NameText.builder()
            .name("Name without level")
            .maxWidth(60)
            .displayMode(NameTextDisplayMode.WITHOUT_COMBAT_LEVEL)
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 30))
            .build());
    elements.add(
        NameText.builder()
            .name("Name with level")
            .maxWidth(40)
            .displayMode(NameTextDisplayMode.WITH_COMBAT_LEVEL)
            .build());
    elements.add(
        CombatLevelText.builder()
            .name("Combat level")
            .xPositionProvider(new PositionProvider(OffsetAnchor.END, 60))
            .build());

    elements.add(
        IconContainer.builder()
            .name("Overhead vengeance")
            .xPositionProvider(new PositionProvider(OffsetAnchor.START, 64))
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
            .width(60)
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 13))
            .build());
    elements.add(
        StatusText.builder()
            .name("Health text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 30))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 20))
            .showMax(false)
            .build());

    elements.add(
        PrayerBar.builder()
            .name("Prayer bar")
            .width(60)
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 25))
            .build());
    elements.add(
        StatusText.builder()
            .name("Prayer text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 30))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 32))
            .showMax(false)
            .statusType(StatusType.PRAYER)
            .build());

    elements.add(
        EnergyBar.builder()
            .name("Energy bar")
            .width(60)
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 37))
            .build());
    elements.add(
        StatusText.builder()
            .name("Energy text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 30))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 44))
            .showMax(false)
            .statusType(StatusType.ENERGY)
            .build());

    elements.add(
        SpecialBar.builder()
            .name("Special bar")
            .width(60)
            .yPositionProvider(new PositionProvider(OffsetAnchor.START, 49))
            .build());
    elements.add(
        StatusText.builder()
            .name("Special text")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 30))
            .yPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 56))
            .showMax(false)
            .statusType(StatusType.SPECIAL)
            .build());
  }
}
