package dev.thource.runelite.nameplates.themes.hitsplats;

import dev.thource.runelite.nameplates.PluginHitsplat;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.Point;

public abstract class HitsplatDisplayType {
  protected final HashMap<Integer, List<PluginHitsplat>> hitsplatsMap = new HashMap<>();

  public void render(
      int gameCycle,
      int hitsplatLifetime,
      Graphics2D graphics,
      Point point,
      int actorId,
      int width,
      int height,
      Map<Integer, HitsplatOptions> hitsplatOptionsMap) {
    cleanUpHitsplats(gameCycle, hitsplatLifetime, actorId);

    var hitsplats = hitsplatsMap.get(actorId);
    if (hitsplats == null || hitsplats.isEmpty()) {
      return;
    }

    render(graphics, point, hitsplats, width, height, hitsplatOptionsMap);
  }

  protected void cleanUpHitsplats(int gameCycle, int hitsplatLifetime, int actorId) {
    var hitsplats = hitsplatsMap.get(actorId);
    if (hitsplats == null || hitsplats.isEmpty()) {
      return;
    }

    hitsplats.removeIf(hitsplat -> hitsplat.getGameCycle() + (hitsplatLifetime / 20) < gameCycle);
  }

  protected abstract void render(
      Graphics2D graphics,
      Point point,
      List<PluginHitsplat> hitsplats,
      int width,
      int height,
      Map<Integer, HitsplatOptions> hitsplatOptionsMap);

  // Returns true if the hitsplat was successfully added AND there is still room for more hitsplats (optionally)
  public boolean addHitsplat(int gameCycle, int hitsplatLifetime, int actorId, PluginHitsplat hitsplat) {
    hitsplatsMap.computeIfAbsent(actorId, k -> new ArrayList<>()).add(hitsplat);

    return true;
  }

  public void unloadActor(int actorId) {
    hitsplatsMap.remove(actorId);
  }
}
