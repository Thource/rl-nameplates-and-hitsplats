package dev.thource.runelite.nameplates.panel.components;

import dev.thource.runelite.nameplates.panel.Nameable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FontFamily implements Nameable {
  private final String value;
  private final String name;
}
