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
    order = -98;

    elements.add(
        IconContainer.builder()
            .name("Overhead skull hint")
            .xPositionProvider(new PositionProvider(OffsetAnchor.MIDDLE, 15))
            .yPositionProvider(new PositionProvider(OffsetAnchor.END, -4))
            .isVertical(true)
            .iconTypes(List.of(IconType.HINT_ARROW, IconType.SKULL, IconType.OVERHEAD))
            .build());

    elements.add(
        HealthBar.builder()
            .name("Health bar")
            .width(30)
            .height(4)
            .barColorProvider(
                new HealthBarColorProvider(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN))
            .backgroundColor(Color.RED)
            .borderSize(0)
            .drawConsumableIndicator(false)
            .build());
  }
}
