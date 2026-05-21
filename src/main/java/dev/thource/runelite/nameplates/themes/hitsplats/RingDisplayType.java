package dev.thource.runelite.nameplates.themes.hitsplats;

import dev.thource.runelite.nameplates.PluginHitsplat;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;

@Slf4j
public class RingDisplayType extends CappedDisplayType {
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

      var xDistance = cap == 1 ? 0 : (width / 2 + 2);
      var yDistance = cap == 1 ? 0 : (height / 2 + 2);

      if (cap > 4) {
        xDistance = (int) (xDistance * (1f + (cap - 4) * 0.15f));
        yDistance = (int) (yDistance * (1f + (cap - 4) * 0.15f));
      }

      var progress = ((float) i / cap) * Math.PI * 2;
      var x = (int) (point.getX() + Math.cos(progress) * xDistance);
      var y = (int) (point.getY() + Math.sin(progress) * yDistance);

      if (hitsplatOptions != null) {
        hitsplatOptions.draw(graphics, String.valueOf(hitsplat.getAmount()), x, y, width, height);
      } else {
        log.warn("No hitsplat options defined for hitsplat type: {}", hitsplat.getHitsplatType());

        graphics.drawString(String.valueOf(hitsplat.getAmount()), x, y);
      }
    }
  }
}
