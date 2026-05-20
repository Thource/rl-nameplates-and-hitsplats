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
public class ScrollingDisplayType extends HitsplatDisplayType {
  @Getter @Setter protected int horizontalPadding = -2;
  @Getter @Setter protected int verticalPadding = -2;
  @Getter @Setter protected int horizontalCap = 3;
  @Getter @Setter protected int verticalCap = 0;
  @Getter @Setter protected int scrollSpeed = 60;
  @Getter @Setter protected int lifetime = 600;
  @Getter @Setter protected int fadeOutDuration = 200;
  @Getter @Setter protected int durationStaggering = 40;

  @Override
  public void drawHitsplats(
      Graphics2D graphics,
      List<PluginHitsplat> hitsplats,
      Point point,
      int width,
      int height,
      Map<Integer, HitsplatOptions> hitsplatOptionsMap) {

    var x = point.getX();
    var y = point.getY();

    var currentTime = System.currentTimeMillis();
    var totalCap = verticalCap == 0 ? Integer.MAX_VALUE : horizontalCap * verticalCap;
    var xOffset = -(Math.min(hitsplats.size(), horizontalCap) * width) / 2;
    for (int i = 0; i < Math.min(totalCap, hitsplats.size()); i++) {
      var column = i % horizontalCap;
      var row = i / horizontalCap;
      var hitsplat = hitsplats.get(i);

      var adjustedCreatedAt = hitsplat.getCreatedAt() + (long) i * durationStaggering;
      if (adjustedCreatedAt > currentTime) {
        return;
      }

      var lifetime = currentTime - adjustedCreatedAt;

      var hitsplatOptions = hitsplatOptionsMap.get(hitsplat.getHitsplatType());
      if (hitsplatOptions != null) {
        hitsplatOptions.draw(
            graphics,
            String.valueOf(hitsplat.getAmount()),
            x + xOffset + (width + horizontalPadding) * column,
            y + (height + verticalPadding) * row - (int) ((lifetime / 1000f) * scrollSpeed),
            width,
            height);
      } else {
        log.warn("No hitsplat options defined for hitsplat type: {}", hitsplat.getHitsplatType());

        graphics.drawString(
            String.valueOf(hitsplat.getAmount()),
            x + xOffset + (width + horizontalPadding) * column,
            y + (height + verticalPadding) * row - (int) ((lifetime / 1000f) * scrollSpeed));
      }
    }
  }
}
