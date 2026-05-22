package dev.thource.runelite.nameplates.themes.hitsplats;

import dev.thource.runelite.nameplates.PluginHitsplat;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;

@Slf4j
public class OSRSDisplayType extends CappedDisplayType {
  @Override
  public void render(
      Graphics2D graphics,
      Point point,
      List<PluginHitsplat> hitsplats,
      int width,
      int height,
      Map<Integer, HitsplatOptions> hitsplatOptionsMap) {

    var cap = Math.min(hitsplatCap, hitsplats.size());
    for (int i = 0; i < cap; i++) {
      var hitsplat = hitsplats.get(i);
      if (hitsplat == null) {
        continue;
      }

      var hitsplatOptions = hitsplatOptionsMap.get(hitsplat.getHitsplatType());

      var xDistance = width / 2 + 4;
      var yDistance = height / 2 - 2;

      // TODO: find a way to support > 4 splats
      var yMul = i == 0 ? 1 : i == 1 ? -1 : 0;
      var xMul = i == 2 ? -1 : i == 3 ? 1 : 0;

      var x = (point.getX() + xMul * xDistance);
      var y = (int) (point.getY() + (yMul - 0.6f) * yDistance);

      if (hitsplatOptions != null) {
        hitsplatOptions.draw(graphics, String.valueOf(hitsplat.getAmount()), x, y, width, height);
      } else {
        log.warn("No hitsplat options defined for hitsplat type: {}", hitsplat.getHitsplatType());

        graphics.drawString(String.valueOf(hitsplat.getAmount()), x, y);
      }
    }
  }
}
