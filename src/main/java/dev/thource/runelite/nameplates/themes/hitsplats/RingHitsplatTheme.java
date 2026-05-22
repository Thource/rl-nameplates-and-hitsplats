package dev.thource.runelite.nameplates.themes.hitsplats;

public class RingHitsplatTheme extends HitsplatTheme {
  public static final String ID = "ringTheme";

  public RingHitsplatTheme() {
    super(ID);

    displayType = new RingDisplayType();
    name = "Ring";
    order = -99;
  }
}
