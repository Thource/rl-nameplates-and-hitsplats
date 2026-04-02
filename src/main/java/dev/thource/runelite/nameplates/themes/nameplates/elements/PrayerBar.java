package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stats;

@Getter
@Setter
@SuperBuilder
public class PrayerBar extends Bar {
  @Builder.Default PrayerBarColorProvider barColorProvider = new PrayerBarColorProvider();

  @Override
  public boolean shouldDraw(Nameplate nameplate) {
    return nameplate.shouldDrawPrayerBar();
  }

  @Override
  protected int getCurrentValue(Nameplate nameplate) {
    return nameplate.getCurrentPrayer();
  }

  @Override
  protected int getMaxValue(Nameplate nameplate) {
    return nameplate.getMaxPrayer();
  }

  @Override
  protected StatChange getStatChange(Nameplate nameplate) {
    return nameplate.getHoveredItemStatChange(Stats.PRAYER);
  }
}
