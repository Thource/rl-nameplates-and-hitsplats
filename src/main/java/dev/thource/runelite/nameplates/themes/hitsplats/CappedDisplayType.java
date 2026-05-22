package dev.thource.runelite.nameplates.themes.hitsplats;

import dev.thource.runelite.nameplates.PluginHitsplat;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public abstract class CappedDisplayType extends HitsplatDisplayType {
  @Getter @Setter int hitsplatCap = 4;

  @Override
  protected void cleanUpHitsplats(int gameCycle, int hitsplatLifetime, int actorId) {
    var hitsplats = hitsplatsMap.get(actorId);
    if (hitsplats == null || hitsplats.isEmpty()) {
      return;
    }

    var iterator = hitsplats.listIterator();
    while (iterator.hasNext()) {
      var hitsplat = iterator.next();

      if (hitsplat != null && hitsplat.getGameCycle() + (hitsplatLifetime / 20) < gameCycle) {
        iterator.set(null);
      }
    }
  }

  @Override
  public boolean addHitsplat(
      int gameCycle, int hitsplatLifetime, int actorId, PluginHitsplat hitsplat) {
    var hitsplats = hitsplatsMap.computeIfAbsent(actorId, k -> new ArrayList<>());
    if (hitsplats.size() < hitsplatCap) {
      hitsplats.add(hitsplat);
      return true;
    }

    var iterator = hitsplats.listIterator();
    while (iterator.hasNext()) {
      var oldHitsplat = iterator.next();

      if (oldHitsplat == null || oldHitsplat.getGameCycle() + (hitsplatLifetime / 20) < gameCycle) {
        iterator.set(hitsplat);

        return true;
      }
    }

    return false;
  }
}
