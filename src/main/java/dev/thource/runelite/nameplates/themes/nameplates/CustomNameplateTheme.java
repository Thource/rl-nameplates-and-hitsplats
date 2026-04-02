package dev.thource.runelite.nameplates.themes.nameplates;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import java.util.ArrayList;
import java.util.UUID;

public class CustomNameplateTheme extends NameplateTheme {
  public CustomNameplateTheme() {
    super(UUID.randomUUID().toString());

    name = "Custom Theme";
    width = 120;
    height = 26;
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
      elements.add(Element.deserialize(gson, el));
    }
    theme.getElements().addAll(elements);
    return theme;
  }
}
