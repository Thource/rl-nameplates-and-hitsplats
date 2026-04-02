package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stats;
import net.runelite.client.ui.FontManager;

@SuperBuilder
public class StatusText extends Text {
  @Builder.Default protected Color color = Color.WHITE;
  @Builder.Default protected Color consumablePositiveColor = new Color(60, 200, 40);
  @Builder.Default protected Color consumableCappedPositiveColor = new Color(200, 200, 60);
  @Builder.Default protected Color consumableNegativeColor = new Color(200, 50, 50);
  @Builder.Default protected StatusType statusType = StatusType.HEALTH;
  @Builder.Default protected boolean showCurrent = true;
  @Builder.Default protected boolean showMax = true;
  @Builder.Default protected boolean showPercentage = false;
  @Builder.Default protected boolean showConsumableDelta = true;

  protected String getTextWithoutConsumableDelta(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH
        && nameplate.isPercentageHealth()
        && (showCurrent || showMax || showPercentage)) {
      return getTextForPercentage(nameplate, true);
    }

    return Stream.of(
            getTextForCurrent(nameplate),
            getTextForMax(nameplate),
            getTextForPercentage(nameplate, false))
        .filter(text -> text != null && !text.trim().isEmpty())
        .collect(Collectors.joining(" "));
  }

  @Override
  protected String getText(Nameplate nameplate) {
    return null;
  }

  protected int getCurrent(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH) {
      return nameplate.getCurrentHealth();
    }

    if (statusType == StatusType.PRAYER) {
      return nameplate.getCurrentPrayer();
    }

    if (statusType == StatusType.ENERGY) {
      return nameplate.getCurrentEnergy();
    }

    if (statusType == StatusType.SPECIAL) {
      return nameplate.getCurrentSpecial();
    }

    return 0;
  }

  protected int getMax(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH) {
      return nameplate.getMaxHealth();
    }

    if (statusType == StatusType.PRAYER) {
      return nameplate.getMaxPrayer();
    }

    if (statusType == StatusType.ENERGY || statusType == StatusType.SPECIAL) {
      return 100;
    }

    return 0;
  }

  protected StatChange getConsumableDelta(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH) {
      return nameplate.getHoveredItemStatChange(Stats.HITPOINTS);
    }

    if (statusType == StatusType.PRAYER) {
      return nameplate.getHoveredItemStatChange(Stats.PRAYER);
    }

    if (statusType == StatusType.ENERGY) {
      return nameplate.getHoveredItemStatChange(Stats.RUN_ENERGY);
    }

    return null;
  }

  protected String getTextForCurrent(Nameplate nameplate) {
    if (!showCurrent) {
      return null;
    }

    return String.valueOf(getCurrent(nameplate));
  }

  protected String getTextForMax(Nameplate nameplate) {
    if (!showMax) {
      return null;
    }

    return (showCurrent ? "/ " : "") + getMax(nameplate);
  }

  protected String getTextForPercentage(Nameplate nameplate, boolean forced) {
    if (!showPercentage && !forced) {
      return null;
    }

    var percentage = Math.ceil((float) getCurrent(nameplate) / getMax(nameplate) * 1000f) / 10f;

    if (!forced && (showCurrent || showMax)) {
      return "(" + percentage + "%)";
    }

    return percentage + "%";
  }

  protected String getTextForConsumableDelta(Nameplate nameplate) {
    if (!showConsumableDelta) {
      return null;
    }

    var consumableDelta = getConsumableDelta(nameplate);
    if (consumableDelta == null || consumableDelta.getRelative() == 0) {
      return null;
    }

    return consumableDelta.getFormattedRelative();
  }

  @Override
  protected Color getColor(Nameplate nameplate) {
    return color;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new ColorInput("Text color", color, value -> color = value, plugin));
    editInputs.add(
        new DropdownInput<>(
            "Status type", statusType, StatusType.values(), val -> statusType = val));
    editInputs.add(new CheckboxInput("Show current", showCurrent, val -> showCurrent = val));
    editInputs.add(new CheckboxInput("Show max", showMax, val -> showMax = val));
    editInputs.add(
        new CheckboxInput("Show percentage", showPercentage, val -> showPercentage = val));
    editInputs.add(
        new CheckboxInput(
            "Show consumable delta", showConsumableDelta, val -> showConsumableDelta = val));
    editInputs.add(
        new ColorInput(
            "Positive consumable delta text color",
            consumablePositiveColor,
            value -> consumablePositiveColor = value,
            plugin));
    editInputs.add(
        new ColorInput(
            "Capped positive consumable delta text color",
            consumableCappedPositiveColor,
            value -> consumableCappedPositiveColor = value,
            plugin));
    editInputs.add(
        new ColorInput(
            "Negative consumable delta text color",
            consumableNegativeColor,
            value -> consumableNegativeColor = value,
            plugin));

    return editInputs;
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if ((statusType == StatusType.HEALTH && !nameplate.shouldDrawHealthBar())
        || (statusType == StatusType.PRAYER && !nameplate.shouldDrawPrayerBar())
        || (statusType == StatusType.ENERGY && !nameplate.shouldDrawEnergyBar())
        || (statusType == StatusType.SPECIAL && !nameplate.shouldDrawSpecialBar())) {
      return;
    }

    var textWithoutConsumableDelta = getTextWithoutConsumableDelta(nameplate);
    var consumableDeltaText = getTextForConsumableDelta(nameplate);
    if ((textWithoutConsumableDelta == null || textWithoutConsumableDelta.isEmpty())
        && (consumableDeltaText == null || consumableDeltaText.isEmpty())) {
      return;
    }

    var text = textWithoutConsumableDelta;
    if (consumableDeltaText != null && !consumableDeltaText.isEmpty()) {
      var consumableTextColor = consumablePositiveColor;
      var consumableDelta = getConsumableDelta(nameplate);
      if (consumableDelta.getRelative() < 0) {
        consumableTextColor = consumableNegativeColor;
      } else if (consumableDelta.getRelative() < consumableDelta.getTheoretical()) {
        consumableTextColor = consumableCappedPositiveColor;
      }

      text += " " + consumableDeltaText;
      var attributedString = new AttributedString(text);
      attributedString.addAttribute(TextAttribute.FONT, FontManager.getRunescapeSmallFont());
      attributedString.addAttribute(
          TextAttribute.FOREGROUND,
          consumableTextColor,
          text.length() - consumableDeltaText.length(),
          text.length());

      draw(
          graphics,
          x,
          y,
          xPositionProvider,
          yPositionProvider,
          attributedString.getIterator(),
          getColor(nameplate));
      return;
    }

    draw(
        graphics,
        x,
        y,
        xPositionProvider,
        yPositionProvider,
        textWithoutConsumableDelta,
        getColor(nameplate));
  }
}
