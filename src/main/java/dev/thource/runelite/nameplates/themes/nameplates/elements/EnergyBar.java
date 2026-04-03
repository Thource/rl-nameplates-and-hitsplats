package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import java.awt.Graphics2D;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.client.plugins.itemstats.StatChange;

@Getter
@Setter
@SuperBuilder
public class EnergyBar extends Bar {
  @Builder.Default EnergyBarColorProvider barColorProvider = new EnergyBarColorProvider();

  @Override
  public boolean shouldDraw(Nameplate nameplate) {
    return super.shouldDraw(nameplate) && nameplate.shouldDrawEnergyBar();
  }

  @Override
  protected int getCurrentValue(Nameplate nameplate) {
    return nameplate.getCurrentEnergy();
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
