package dev.thource.runelite.nameplates.themes.nameplates;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NPCNameplate;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesConfig;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.Nameable;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Point;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;

public abstract class NameplateTheme implements Nameable {
  protected transient NameplatesPlugin plugin;
  protected transient NameplatesConfig config;

  @Getter protected String id;
  @Getter @Setter protected int order;
  @Getter @Setter protected String name;
  @Getter @Setter protected int width;
  @Getter @Setter protected int height;
  @Getter @Setter protected int heightWithPrayerBar;
  @Getter @Setter protected boolean stacking;
  @Getter protected final List<Element> elements = new ArrayList<>();

  protected NameplateTheme(String id) {
    this.id = id;
  }

  public void setPlugin(NameplatesPlugin plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  protected String getHealthString(Nameplate nameplate) {
    String healthString = nameplate.getCurrentHealth() + " / " + nameplate.getMaxHealth();
    if (nameplate instanceof NPCNameplate
        && ((NPCNameplate) nameplate).getPercentageHealthOverride() > 0) {
      healthString += "~";
    }

    boolean forcePercentage = nameplate.drawHealthAsPercentage();
    //        (nameplate.getActor() instanceof Player
    //                && !config.lookupPlayerHp()
    //                && nameplate.getActor() != plugin.getClient().getLocalPlayer())
    //            || (nameplate instanceof NPCNameplate
    //                && ((NPCNameplate) nameplate).isPercentageHealth());

    HitpointsDisplayStyle displayStyle = config.hitpointsDisplayStyle();
    if (forcePercentage || displayStyle != HitpointsDisplayStyle.HITPOINTS) {
      double percentage =
          Math.ceil((float) nameplate.getCurrentHealth() / nameplate.getMaxHealth() * 1000f) / 10f;

      if (forcePercentage || displayStyle == HitpointsDisplayStyle.PERCENTAGE) {
        healthString = percentage + "%";
      } else {
        healthString += " (" + percentage + "%)";
      }
    }

    return healthString;
  }

  public void drawNameplate(Graphics2D graphics, Nameplate nameplate, Point point) {
    var x = point.getX();
    var y = point.getY();
    var drawPrayerBar = nameplate.shouldDrawPrayerBar();

    x -= width / 2;
    if (drawPrayerBar) {
      y -= heightWithPrayerBar;
    } else {
      y -= height;
    }

    for (Element element : elements) {
      element.draw(nameplate, graphics, x, y);
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
