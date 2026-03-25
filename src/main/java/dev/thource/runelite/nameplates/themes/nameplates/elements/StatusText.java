package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.runelite.client.plugins.itemstats.StatChange;

public class StatusText extends Text {
  protected Color color = Color.WHITE;
  protected Color consumablePositiveColor = new Color(60, 200, 40);
  protected Color consumableCappedPositiveColor = new Color(200, 200, 60);
  protected Color consumableNegativeColor = new Color(200, 50, 50);
  protected StatusType statusType = StatusType.HEALTH;
  protected boolean showCurrent = true;
  protected boolean showMax = true;
  protected boolean showPercentage;
  protected boolean showConsumableDelta;

  public StatusText() {
    super();
  }

  protected String getTextWithoutConsumableDelta(Nameplate nameplate) {
    return Stream.of(
            getTextForCurrent(nameplate), getTextForMax(nameplate), getTextForPercentage(nameplate))
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

    return 0;
  }

  protected int getMax(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH) {
      return nameplate.getMaxHealth();
    }

    if (statusType == StatusType.PRAYER) {
      return nameplate.getMaxPrayer();
    }

    return 0;
  }

  protected StatChange getConsumableDelta(Nameplate nameplate) {
    if (statusType == StatusType.HEALTH) {
      return nameplate.getHoveredItemHpChange();
    }

    if (statusType == StatusType.PRAYER) {
      return nameplate.getHoveredItemPrayerChange();
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

  protected String getTextForPercentage(Nameplate nameplate) {
    if (!showPercentage) {
      return null;
    }

    var percentage = Math.ceil((float) getCurrent(nameplate) / getMax(nameplate) * 1000f) / 10f;

    if (showCurrent || showMax) {
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
    if (statusType == StatusType.PRAYER && !nameplate.shouldDrawPrayerBar()) {
      return;
    }

    var textWithoutConsumableDelta = getTextWithoutConsumableDelta(nameplate);
    var consumableDeltaText = getTextForConsumableDelta(nameplate);
    if ((textWithoutConsumableDelta == null || textWithoutConsumableDelta.isEmpty())
        && (consumableDeltaText == null || consumableDeltaText.isEmpty())) {
      return;
    }

    var consumableOffset = 0;
    if (textWithoutConsumableDelta != null && !textWithoutConsumableDelta.isEmpty()) {
      draw(
          graphics,
          x,
          y,
          xPositionProvider,
          yPositionProvider,
          textWithoutConsumableDelta,
          getColor(nameplate));

      var fontMetrics = graphics.getFontMetrics();
      consumableOffset =
          (int) fontMetrics.getStringBounds(textWithoutConsumableDelta, graphics).getWidth();
    }

    if (consumableDeltaText != null) {
      var consumableTextColor = consumablePositiveColor;
      var consumableDelta = getConsumableDelta(nameplate);
      if (consumableDelta.getRelative() < 0) {
        consumableTextColor = consumableNegativeColor;
      } else if (consumableDelta.getRelative() < consumableDelta.getTheoretical()) {
        consumableTextColor = consumableCappedPositiveColor;
      }

      if (consumableOffset > 0) {
        var consumableXPositionProvider =
            new PositionProvider(OffsetAnchor.START, xPositionProvider.getValue() + 4);
        if (xPositionProvider.getAnchor() == OffsetAnchor.MIDDLE) {
          consumableXPositionProvider.setValue(
              consumableXPositionProvider.getValue() + consumableOffset / 2);
        } else if (xPositionProvider.getAnchor() == OffsetAnchor.START) {
          consumableXPositionProvider.setValue(
              consumableXPositionProvider.getValue() + consumableOffset);
        }
        draw(
            graphics,
            x,
            y,
            consumableXPositionProvider,
            yPositionProvider,
            consumableDeltaText,
            consumableTextColor);
      } else {
        draw(
            graphics,
            x,
            y,
            xPositionProvider,
            yPositionProvider,
            consumableDeltaText,
            consumableTextColor);
      }
    }
  }
}
