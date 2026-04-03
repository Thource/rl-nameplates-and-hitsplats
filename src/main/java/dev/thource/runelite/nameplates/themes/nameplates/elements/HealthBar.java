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
public class HealthBar extends Bar {
  @Builder.Default HealthBarColorProvider barColorProvider = new HealthBarColorProvider();

  @Override
  public boolean shouldDraw(Nameplate nameplate) {
    return super.shouldDraw(nameplate) && nameplate.shouldDrawHealthBar();
  }

  @Override
  protected int getCurrentValue(Nameplate nameplate) {
    return nameplate.getCurrentHealth();
  }

  @Override
  protected int getMaxValue(Nameplate nameplate) {
    return nameplate.getMaxHealth();
  }

  @Override
  protected StatChange getStatChange(Nameplate nameplate) {
    return nameplate.getHoveredItemStatChange(Stats.HITPOINTS);
  }
}
