package dev.thource.runelite.nameplates.panel.nameplates;

import dev.thource.runelite.nameplates.panel.Nameable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HealthState implements Nameable {
  NONE("None"),
  POISON("Poison"),
  VENOM("Venom"),
  DISEASE("Disease"),
  ;

  private final String name;
}
