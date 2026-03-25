package dev.thource.runelite.nameplates.themes.nameplates.elements;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.Nameable;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.panel.components.PositionProviderInput;
import dev.thource.runelite.nameplates.panel.components.StringInput;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Element implements Nameable {
  protected final String elementType = getClass().getSimpleName();
  protected String name;
  protected final PositionProvider xPositionProvider;
  protected final PositionProvider yPositionProvider;

  protected Element() {
    name = getClass().getSimpleName();
    xPositionProvider = new PositionProvider();
    yPositionProvider = new PositionProvider();
  }

  protected Element(
      String name, PositionProvider xPositionProvider, PositionProvider yPositionProvider) {
    this.name = name;
    this.xPositionProvider = xPositionProvider;
    this.yPositionProvider = yPositionProvider;
  }

  public abstract void draw(Nameplate nameplate, Graphics2D graphics, int x, int y);

  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var list = new ArrayList<LabelledInput>();

    list.add(new StringInput("Name", name, this::setName));
    list.add(new PositionProviderInput("X position", xPositionProvider, false));
    list.add(new PositionProviderInput("Y position", yPositionProvider, true));

    return list;
  }

  public static Element deserialize(Gson gson, JsonElement el) {
    var elObj = el.getAsJsonObject();
    var type = elObj.get("elementType").getAsString();
    Class<? extends Element> clazz;
    switch (type) {
      case "NameText":
        clazz = NameText.class;
        break;
      case "CombatLevelText":
        clazz = CombatLevelText.class;
        break;
      case "Icon":
        clazz = Icon.class;
        break;
      case "IconContainer":
        clazz = IconContainer.class;
        break;
      case "HealthBar":
        clazz = HealthBar.class;
        break;
      case "PrayerBar":
        clazz = PrayerBar.class;
        break;
      case "StatusText":
        clazz = StatusText.class;
        break;
      // add other subclasses here
      default:
        throw new IllegalArgumentException("Unknown elementType: " + type);
    }

    if (clazz == HealthBar.class) {
      return HealthBar.deserialize(elObj, gson);
    } else if (clazz == PrayerBar.class) {
      return PrayerBar.deserialize(elObj, gson);
    } else {
      return gson.fromJson(el, clazz);
    }
  }
}
