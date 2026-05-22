package dev.thource.runelite.nameplates.themes.hitsplats;

public class OSRSHitsplatTheme extends HitsplatTheme {
  public static final String ID = "osrsTheme";

  public OSRSHitsplatTheme() {
    super(ID);

    displayType = new OSRSDisplayType();
    name = "OSRS";
    order = -100;
  }
}
