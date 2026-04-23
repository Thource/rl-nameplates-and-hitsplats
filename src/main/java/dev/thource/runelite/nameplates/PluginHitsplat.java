package dev.thource.runelite.nameplates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class PluginHitsplat {
  private final int hitsplatType;
  private final int amount;
  private final long serverTick;
  private final int tickIndex;
  @Setter private Long createdAt = System.currentTimeMillis();
}
