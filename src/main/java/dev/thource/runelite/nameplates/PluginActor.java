package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Actor;

@RequiredArgsConstructor
@Getter
public class PluginActor {
  private final Actor actor;
  private final Nameplate nameplate;
}
