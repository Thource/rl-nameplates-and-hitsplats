package dev.thource.runelite.nameplates.panel.components;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.runelite.client.ui.FontManager;

public class Fonts {
  @Getter private static final List<FontFamily> fontFamilies = new ArrayList<>();

  static {
    var rsSmall = FontManager.getRunescapeSmallFont();
    var rs = FontManager.getRunescapeFont();

    fontFamilies.add(new FontFamily(rsSmall.getFamily()));
    fontFamilies.add(new FontFamily(rs.getFamily()));

    FontManager.getCustomFonts().forEach((name) -> fontFamilies.add(new FontFamily(name)));
    FontManager.getSystemFonts().forEach((name) -> fontFamilies.add(new FontFamily(name)));
  }
}
