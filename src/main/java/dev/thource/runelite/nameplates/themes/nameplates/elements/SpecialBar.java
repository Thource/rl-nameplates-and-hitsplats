package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.client.plugins.itemstats.StatChange;

@Getter
@Setter
@SuperBuilder
public class SpecialBar extends Bar {
  @Builder.Default SpecialBarColorProvider barColorProvider = new SpecialBarColorProvider();

  @Override
  public boolean shouldDraw(Nameplate nameplate) {
    return super.shouldDraw(nameplate) && nameplate.shouldDrawSpecialBar();
  }

  @Override
  protected int getCurrentValue(Nameplate nameplate) {
    return nameplate.getCurrentSpecial();
  }

  @Override
  protected int getMaxValue(Nameplate nameplate) {
    return 100;
  }

  @Override
  protected StatChange getStatChange(Nameplate nameplate) {
    return null;
  }
}
