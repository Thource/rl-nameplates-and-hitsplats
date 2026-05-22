package dev.thource.runelite.nameplates.themes.hitsplats;

public class ScrollingHitsplatTheme extends HitsplatTheme {
  public static final String ID = "scrollingTheme";

  public ScrollingHitsplatTheme() {
    super(ID);

    displayType = new ScrollingDisplayType();
    name = "Scrolling";
    order = -98;
  }
}
