package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.client.plugins.itemstats.StatChange;

@Getter
@Setter
@SuperBuilder
public abstract class Bar extends Element {
  @Builder.Default protected int width = 120;
  @Builder.Default protected int height = 14;
  @Builder.Default protected int heightAddedWhenDrawn = 14;
  @Builder.Default protected int cornerRadius = 0;
  @Builder.Default protected int borderSize = 2;
  @Builder.Default protected Color borderColor = new Color(0.1f, 0.1f, 0.1f);
  @Builder.Default protected Color backgroundColor = new Color(0.3f, 0.3f, 0.3f);
  @Builder.Default protected boolean drawConsumableIndicator = true;
  @Builder.Default protected Color consumablePositiveColor = new Color(60, 120, 60);
  @Builder.Default protected Color consumableCappedPositiveColor = new Color(100, 80, 0);
  @Builder.Default protected Color consumableNegativeColor = new Color(80, 30, 20);

  public boolean shouldDraw(Nameplate nameplate) {
    return nameplate.getPlugin().shouldDrawFor(nameplate);
  }

  protected abstract BarColorProvider getBarColorProvider();

  protected abstract int getCurrentValue(Nameplate nameplate);

  protected abstract int getMaxValue(Nameplate nameplate);

  protected abstract StatChange getStatChange(Nameplate nameplate);

  protected float getProgress(Nameplate nameplate) {
    return Math.min(1, (float) getCurrentValue(nameplate) / (float) getMaxValue(nameplate));
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int plateX, int plateY) {
    if (!shouldDraw(nameplate)) {
      return;
    }

    var x = plateX + xPositionProvider.get(width);
    var y = plateY + yPositionProvider.get(height);

    if (borderSize > 0) {
      Rect.draw(graphics, x, y, width, height, borderColor, cornerRadius);
    }

    var innerWidth = width - borderSize * 2;
    var innerHeight = height - borderSize * 2;

    if (backgroundColor.getAlpha() > 0) {
      Rect.draw(
          graphics,
          x + borderSize,
          y + borderSize,
          innerWidth,
          innerHeight,
          backgroundColor,
          cornerRadius - borderSize);
    }

    var fillWidth = (int) (innerWidth * getProgress(nameplate));

    var statChange = drawConsumableIndicator ? getStatChange(nameplate) : null;
    if (statChange != null && statChange.getRelative() > 0) {
      Rect.draw(
          graphics,
          x + borderSize,
          y + borderSize,
          (int) (innerWidth * (statChange.getRelative() / (float) getMaxValue(nameplate)))
              + fillWidth,
          innerHeight,
          statChange.getRelative() != statChange.getTheoretical()
              ? consumableCappedPositiveColor
              : consumablePositiveColor,
          cornerRadius - borderSize);
    }

    Rect.draw(
        graphics,
        x + borderSize,
        y + borderSize,
        fillWidth,
        innerHeight,
        getBarColorProvider().getColor(nameplate),
        cornerRadius - borderSize);

    if (statChange != null && statChange.getRelative() < 0) {
      var consumableWidth =
          (int) (innerWidth * (-statChange.getRelative() / (float) getMaxValue(nameplate)));
      Rect.draw(
          graphics,
          x + borderSize + fillWidth - consumableWidth,
          y + borderSize,
          consumableWidth,
          innerHeight,
          consumableNegativeColor,
          cornerRadius - borderSize);
    }
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new IntInput("Width", width, 1, 999, this::setWidth));
    editInputs.add(new IntInput("Height", height, 1, 999, this::setHeight));
    editInputs.add(
        new IntInput(
            "Height added to nameplate when drawn",
            heightAddedWhenDrawn,
            1,
            999,
            this::setHeightAddedWhenDrawn));
    editInputs.add(new IntInput("Corner radius", cornerRadius, 0, 999, this::setCornerRadius));
    editInputs.add(new IntInput("Border size", borderSize, 0, 999, this::setBorderSize));
    editInputs.add(new ColorInput("Border color", borderColor, this::setBorderColor, plugin));
    editInputs.add(
        new ColorInput("Background color", backgroundColor, this::setBackgroundColor, plugin));

    editInputs.add(
        new ColorInput(
            "Consumable positive color",
            consumablePositiveColor,
            this::setConsumablePositiveColor,
            plugin));
    editInputs.add(
        new ColorInput(
            "Consumable positive capped color",
            consumableCappedPositiveColor,
            this::setConsumableCappedPositiveColor,
            plugin));
    editInputs.add(
        new ColorInput(
            "Consumable negative color",
            consumableNegativeColor,
            this::setConsumableNegativeColor,
            plugin));

    editInputs.addAll(getBarColorProvider().getEditInputs(plugin));

    // todo: add indicator colors

    return editInputs;
  }
}
