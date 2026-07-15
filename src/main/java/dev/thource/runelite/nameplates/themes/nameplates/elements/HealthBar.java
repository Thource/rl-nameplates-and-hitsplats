package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
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
  @Builder.Default protected Color poisonDamageColor = new Color(30, 90, 20);

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

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int plateX, int plateY) {
    super.draw(nameplate, graphics, plateX, plateY);

    var poisonStatus = nameplate.getPoisonStatus();
    if (!shouldDraw(nameplate) || poisonStatus == null) {
      return;
    }

    // If the player has boosted health and the poison damage won't take them under their max
    // health, there is nothing to draw
    var visiblePoisonDamage =
        poisonStatus.getDamage()
            - Math.max(0, nameplate.getCurrentHealth() - nameplate.getMaxHealth());
    if (visiblePoisonDamage <= 0) {
      return;
    }

    var x = plateX + xPositionProvider.get(width);
    var y = plateY + yPositionProvider.get(height);
    var innerWidth = width - borderSize * 2;
    var innerHeight = height - borderSize * 2;

    var fillWidth = (int) Math.ceil(innerWidth * getProgress(nameplate));
    var poisonDamageWidth =
        (int) Math.ceil(innerWidth * (visiblePoisonDamage / (float) nameplate.getMaxHealth()));

    Rect.draw(
        graphics,
        x + borderSize + fillWidth - poisonDamageWidth,
        y + borderSize,
        poisonDamageWidth,
        innerHeight,
        poisonDamageColor,
        cornerRadius - borderSize);
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var inputs = super.getEditInputs(plugin);

    inputs.add(
        new ColorInput(
            "Poison damage color", poisonDamageColor, this::setPoisonDamageColor, plugin));

    return inputs;
  }
}
