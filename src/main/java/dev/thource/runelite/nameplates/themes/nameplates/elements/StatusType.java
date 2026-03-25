package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.panel.Nameable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType implements Nameable {
  HEALTH("Health"),
  PRAYER("Prayer");

  private final String name;
}
