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
public class ScrollingDisplayType extends HitsplatDisplayType {
  @Getter @Setter protected int horizontalPadding = -4;
  @Getter @Setter protected int verticalPadding = -4;
  @Getter @Setter protected int horizontalCap = 3;
  @Getter @Setter protected int verticalCap = 3;
  @Getter @Setter protected int scrollSpeed = 120;
  @Getter @Setter protected int durationStaggering = 20;

  @Override
  public void render(
      Graphics2D graphics,
      Point point,
      List<PluginHitsplat> hitsplats,
      int width,
      int height,
      Map<Integer, HitsplatOptions> hitsplatOptionsMap) {

    var x = point.getX();
    var y = point.getY();

    var totalCap = verticalCap == 0 ? Integer.MAX_VALUE : horizontalCap * verticalCap;
    hitsplats.stream()
        .collect(Collectors.groupingBy(PluginHitsplat::getGameCycle))
        .forEach(
            (gameCycle, cycleSplats) -> {
              var xOffset = -((Math.min(cycleSplats.size(), horizontalCap) - 1) * width) / 2;

              for (int i = 0; i < Math.min(cycleSplats.size(), totalCap); i++) {
                var hitsplat = cycleSplats.get(i);
                var currentGameCycle = hitsplat.getClient().getGameCycle();
                var gameCycleIndex = hitsplat.getGameCycleIndex();

                var column = gameCycleIndex % horizontalCap;
                var row = gameCycleIndex / horizontalCap;
                var timePassed = (currentGameCycle - hitsplat.getGameCycle()) * 20;
                if (timePassed < gameCycleIndex * durationStaggering) {
                  break;
                }

                var hitsplatOptions = hitsplatOptionsMap.get(hitsplat.getHitsplatType());
                if (hitsplatOptions != null) {
                  hitsplatOptions.draw(
                      graphics,
                      String.valueOf(hitsplat.getAmount()),
                      x + xOffset + (width + horizontalPadding) * column,
                      y
                          + (height + verticalPadding) * row
                          - (int) ((timePassed / 1000f) * scrollSpeed),
                      width,
                      height);
                } else {
                  log.warn(
                      "No hitsplat options defined for hitsplat type: {}",
                      hitsplat.getHitsplatType());

                  graphics.drawString(
                      String.valueOf(hitsplat.getAmount()),
                      x + xOffset + (width + horizontalPadding) * column,
                      y
                          + (height + verticalPadding) * row
                          - (int) ((timePassed / 1000f) * scrollSpeed));
                }
              }
            });
  }
}
