package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.panel.Nameable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IconType implements Nameable {
  OVERHEAD("Overhead"),
  SKULL("Skull"),
  NO_LOOT("No-loot"),
  HOVERED("Hovered"),
  HINT_ARROW("Hint arrow"),
  VENGEANCE("Vengeance"),
  ;

  @Getter private final String name;
}
