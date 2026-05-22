package dev.thource.runelite.nameplates.themes.hitsplats;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NameplatesConfig;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.Graphics2D;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;

@Slf4j
public abstract class HitsplatTheme implements Nameable {
  protected transient NameplatesPlugin plugin;
  protected transient NameplatesConfig config;

  @Getter protected String id;
  @Getter @Setter protected int order;
  @Getter @Setter protected String name;
  @Getter @Setter protected int width = 25;
  @Getter @Setter protected int height = 25;
  @Getter @Setter protected HitsplatDisplayType displayType = new OSRSDisplayType();

  @Getter
  protected Map<Integer, HitsplatOptions> hitsplatOptionsMap = HitsplatDefaultSprite.defaultMap();

  protected HitsplatTheme(String id) {
    this.id = id;
  }

  public void setPlugin(NameplatesPlugin plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();

    var clientThread = plugin.getClientThread();
    var spriteManager = plugin.getSpriteManager();
    hitsplatOptionsMap.forEach(
        (id, options) -> options.background.initialize(clientThread, spriteManager));
  }

  public void drawHitsplats(Graphics2D graphics, Actor actor) {
    var point = actor.getCanvasTextLocation(graphics, " ", actor.getLogicalHeight() / 2);
    if (point == null) {
      return;
    }

    displayType.render(
        plugin.getClient().getGameCycle(),
        config.hitsplatLifetime(),
        graphics,
        point,
        NameplatesPlugin.getActorId(actor),
        width,
        height,
        hitsplatOptionsMap);
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
