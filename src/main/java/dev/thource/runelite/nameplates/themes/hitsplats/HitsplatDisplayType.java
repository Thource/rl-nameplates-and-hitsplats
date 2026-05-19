package dev.thource.runelite.nameplates.themes.hitsplats;

import dev.thource.runelite.nameplates.PluginHitsplat;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import net.runelite.api.Point;

public abstract class HitsplatDisplayType {
  public abstract void drawHitsplats(
      Graphics2D graphics, List<PluginHitsplat> hitsplats, Point point, int width, int height, Map<Integer, HitsplatOptions> hitsplatOptionsMap);
}
