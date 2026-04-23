package dev.thource.runelite.nameplates.themes.nameplates;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NameplatesConfig;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.PluginHitsplat;
import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;

@Slf4j
public abstract class HitsplatTheme implements Nameable {
  protected transient NameplatesPlugin plugin;
  protected transient NameplatesConfig config;

  @Getter protected String id;
  @Getter @Setter protected int order;
  @Getter @Setter protected String name;
  @Getter @Setter protected int width = 25;
  @Getter @Setter protected int height = 25;
  @Getter @Setter protected int horizontalPadding = -2;
  @Getter @Setter protected int verticalPadding = -2;
  @Getter @Setter protected int horizontalCap = 3;
  @Getter @Setter protected int verticalCap = 0;
//  @Getter @Setter protected int scrollSpeed = 120;
//  @Getter @Setter protected int lifetime = 1200;
//  @Getter @Setter protected int fadeOutDuration = 200;
//  @Getter @Setter protected int durationStaggering = 20;
  @Getter @Setter protected int scrollSpeed = 60;
  @Getter @Setter protected int lifetime = 600;
  @Getter @Setter protected int fadeOutDuration = 200;
  @Getter @Setter protected int durationStaggering = 40;

  @Getter protected Map<Integer, HitsplatOptions> hitsplatOptionsMap = HitsplatDefaultSprite.defaultMap();

  protected HitsplatTheme(String id) {
    this.id = id;
  }

  public void setPlugin(NameplatesPlugin plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();

    var clientThread = plugin.getClientThread();
    var spriteManager = plugin.getSpriteManager();
    hitsplatOptionsMap.forEach((id, options) -> {
      options.background.initialize(clientThread, spriteManager);
    });
  }

  public void drawHitsplats(Graphics2D graphics, List<PluginHitsplat> hitsplats, Point point) {
    var x = point.getX();
    var y = point.getY();

    var currentTime = System.currentTimeMillis();
    var totalCap = verticalCap == 0 ? Integer.MAX_VALUE : horizontalCap * verticalCap;
    var xOffset = -(Math.min(hitsplats.size(), horizontalCap) * width) / 2;
    for (int i = 0; i < Math.min(totalCap, hitsplats.size()); i++) {
      var column = i % horizontalCap;
      var row = i / horizontalCap;
      var hitsplat = hitsplats.get(i);

      var adjustedCreatedAt = hitsplat.getCreatedAt() + i * 20L;
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

  public boolean isEditable() {
    return false;
  }

  public String serialize(Gson gson, boolean forExport) {
    var themeEl = gson.toJsonTree(this).getAsJsonObject();

    if (forExport) {
      themeEl.remove("id");
      themeEl.remove("order");
    }

    return gson.toJson(themeEl);
  }
}
