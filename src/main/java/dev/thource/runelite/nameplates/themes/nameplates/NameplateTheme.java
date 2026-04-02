package dev.thource.runelite.nameplates.themes.nameplates;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesConfig;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.Nameable;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Bar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Point;

public abstract class NameplateTheme implements Nameable {
  protected transient NameplatesPlugin plugin;
  protected transient NameplatesConfig config;

  @Getter protected String id;
  @Getter @Setter protected int order;
  @Getter @Setter protected String name;
  @Getter @Setter protected int width;
  @Getter @Setter protected int height;
  @Getter @Setter protected boolean stacking;
  @Getter protected final List<Element> elements = new ArrayList<>();

  protected NameplateTheme(String id) {
    this.id = id;
  }

  public void setPlugin(NameplatesPlugin plugin) {
    this.plugin = plugin;
    config = plugin.getConfig();
  }

  public int drawNameplate(Graphics2D graphics, Nameplate nameplate, Point point) {
    var x = point.getX();
    var y = point.getY();

    var totalHeight = height;
    for (Element element : elements) {
      if (element instanceof Bar) {
        var bar = (Bar) element;

        if (bar.shouldDraw(nameplate)) {
          totalHeight += bar.getHeightAddedWhenDrawn();
        }
      }
    }

    for (Element element : elements) {
      element.draw(nameplate, graphics, x - width / 2, y - totalHeight);
    }

    if (!stacking) {
      return 0;
    }

    return totalHeight;
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
