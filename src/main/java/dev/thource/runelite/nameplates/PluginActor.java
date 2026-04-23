package dev.thource.runelite.nameplates;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Actor;

@RequiredArgsConstructor
@Getter
public class PluginActor {
  private final Actor actor;
  private final Nameplate nameplate;
  private final List<PluginHitsplat> hitsplats = new ArrayList<>();
}
