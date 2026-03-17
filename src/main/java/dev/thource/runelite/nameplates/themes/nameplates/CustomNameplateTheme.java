package dev.thource.runelite.nameplates.themes.nameplates;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Icon;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import java.util.ArrayList;
import java.util.UUID;

public class CustomNameplateTheme extends NameplateTheme {
  public CustomNameplateTheme() {
    super(UUID.randomUUID().toString());

    name = "Custom Theme";
    width = 120;
    height = 26;
    heightWithPrayerBar = 38;
  }

  @Override
  public boolean isEditable() {
    return true;
  }

  public static CustomNameplateTheme deserialize(String json, Gson gson, boolean fromImport)
      throws RuntimeException {
    var themeEl = new JsonParser().parse(json);
    if (!themeEl.isJsonObject()) {
      throw new JsonSyntaxException("Expected a JSON object");
    }

    var themeObj = themeEl.getAsJsonObject();

    if (fromImport) {
      // IDs should be random to avoid collisions
      themeObj.remove("id");
      themeObj.remove("order");
    }

    var elementsArray = themeObj.getAsJsonArray("elements");
    // We have to remove the elements before deserializing the theme itself, otherwise Gson will try
    // to deserialize
    //   the elements as part of the theme and fail since it doesn't know about the element
    // subclasses
    themeObj.remove("elements");

    var theme = gson.fromJson(themeObj, CustomNameplateTheme.class);

    var elements = new ArrayList<Element>();
    for (var el : elementsArray) {
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
        // add other subclasses here
        default:
          throw new IllegalArgumentException("Unknown elementType: " + type);
      }

      if (clazz == HealthBar.class) {
        elements.add(HealthBar.deserialize(elObj, gson));
      } else if (clazz == PrayerBar.class) {
        elements.add(PrayerBar.deserialize(elObj, gson));
      } else {
        elements.add(gson.fromJson(el, clazz));
      }
    }
    theme.getElements().addAll(elements);
    return theme;
  }
}
