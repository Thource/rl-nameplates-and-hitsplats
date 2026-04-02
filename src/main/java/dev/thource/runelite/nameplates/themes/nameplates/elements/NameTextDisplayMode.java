package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.panel.Nameable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NameTextDisplayMode implements Nameable {
  WITH_COMBAT_LEVEL("With combat level"),
  WITHOUT_COMBAT_LEVEL("Without combat level"),
  ALWAYS("Always");

  private final String name;
}
