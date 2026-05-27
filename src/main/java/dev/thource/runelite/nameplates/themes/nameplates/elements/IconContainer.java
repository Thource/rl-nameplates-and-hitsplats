package dev.thource.runelite.nameplates.themes.nameplates.elements;

import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.ADD_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.DELETE_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.MOVE_DOWN_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.MOVE_UP_ICON;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import dev.thource.runelite.nameplates.panel.components.ListSelector;
import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.api.coords.Direction;

@SuperBuilder
public class IconContainer extends Element {
  @Setter(AccessLevel.PRIVATE)
  @Builder.Default
  private int iconSize = 26;

  @Setter(AccessLevel.PRIVATE)
  @Builder.Default
  private int padding = 4;

  @Setter(AccessLevel.PRIVATE)
  @Builder.Default
  private boolean isVertical = false;

  @Setter(AccessLevel.PRIVATE)
  @Builder.Default
  private boolean addHeightWhenDrawn = false;

  @Setter(AccessLevel.PRIVATE)
  @Getter
  @Builder.Default
  private int heightAddedWhenDrawn = 0;

  @Builder.Default private final List<IconType> iconTypes = new ArrayList<>();

  private IconType[] iconsToDraw(Nameplate nameplate) {
    return iconTypes.stream()
        .filter(iconType -> Icon.shouldDraw(nameplate, iconType))
        .toArray(IconType[]::new);
  }

  public int getHeight(Nameplate nameplate) {
    if (!isVertical) {
      return iconSize;
    }

    var iconsToDraw = iconsToDraw(nameplate);
    return iconSize * iconsToDraw.length + (iconsToDraw.length - 1) * padding;
  }

  public int getWidth(Nameplate nameplate) {
    if (isVertical) {
      return iconSize;
    }

    var iconsToDraw = iconsToDraw(nameplate);
    return iconSize * iconsToDraw.length + (iconsToDraw.length - 1) * padding;
  }

  public boolean shouldDraw(Nameplate nameplate) {
    return iconsToDraw(nameplate).length > 0;
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    if (!shouldDraw(nameplate)) {
      return;
    }

    x += xPositionProvider.get(getWidth(nameplate));
    y += yPositionProvider.get(getHeight(nameplate));

    var direction = Direction.SOUTH;
    if (isVertical) {
      if (yPositionProvider.getAnchor() == OffsetAnchor.START) {
        direction = Direction.NORTH;
      }
    } else {
      if (xPositionProvider.getAnchor() == OffsetAnchor.END) {
        direction = Direction.EAST;
      } else {
        direction = Direction.WEST;
      }
    }

    var iconsToDraw = iconsToDraw(nameplate);
    for (IconType type : iconsToDraw) {
      Icon.draw(nameplate, graphics, x, y, iconSize, type, direction);

      if (isVertical) {
        y += iconSize + padding;
      } else {
        x += iconSize + padding;
      }
    }
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = super.getEditInputs(plugin);

    editInputs.add(new CheckboxInput("Vertical", isVertical, this::setVertical));
    editInputs.add(
        new CheckboxInput(
            "Add height above nameplate in stack when drawn",
            addHeightWhenDrawn,
            this::setAddHeightWhenDrawn));
    editInputs.add(
        new IntInput(
            "Extra height added when drawn",
            heightAddedWhenDrawn,
            0,
            999,
            this::setHeightAddedWhenDrawn,
            "px"));
    editInputs.add(new IntInput("Icon size", iconSize, 1, 999, this::setIconSize, "px"));
    editInputs.add(new IntInput("Icon spacing", padding, 0, 999, this::setPadding, "px"));
    var iconTypesSelector = new ListSelector<>("Icons", null, iconTypes);
    iconTypesSelector.addChangeListener(
        sel -> {
          iconTypesSelector.clearButtons();

          if (sel != null) {
            var selectedIndex = iconTypes.indexOf(sel);
            if (selectedIndex > 0) {
              iconTypesSelector.addButton(
                  "Move up",
                  MOVE_UP_ICON,
                  () -> {
                    var previousEl = iconTypes.get(selectedIndex - 1);
                    iconTypes.set(selectedIndex - 1, sel);
                    iconTypes.set(selectedIndex, previousEl);

                    iconTypesSelector.setValues(iconTypes);
                  });
            }
            if (selectedIndex < iconTypes.size() - 1) {
              iconTypesSelector.addButton(
                  "Move down",
                  MOVE_DOWN_ICON,
                  () -> {
                    var previousEl = iconTypes.get(selectedIndex + 1);
                    iconTypes.set(selectedIndex + 1, sel);
                    iconTypes.set(selectedIndex, previousEl);

                    iconTypesSelector.setValues(iconTypes);
                  });
            }
          }
          iconTypesSelector.addButtonGlue();

          if (sel != null) {
            iconTypesSelector.addButton(
                "Remove icon",
                DELETE_ICON,
                () -> {
                  iconTypes.remove(sel);
                  iconTypesSelector.setValues(iconTypes);
                });
          }

          var iconsToAdd =
              Arrays.stream(IconType.values())
                  .filter(type -> !iconTypes.contains(type))
                  .collect(Collectors.toList());
          if (!iconsToAdd.isEmpty()) {
            iconTypesSelector.addButton(
                "Add icon",
                ADD_ICON,
                () -> {
                  var popupMenu = new JPopupMenu();

                  iconsToAdd.forEach(
                      iconType -> {
                        var menuItem = new JMenuItem(iconType.getName());
                        menuItem.addActionListener(
                            e -> {
                              iconTypes.add(iconType);

                              iconTypesSelector.setValues(iconTypes);
                              iconTypesSelector.selectValue(iconType);
                            });
                        popupMenu.add(menuItem);
                      });

                  popupMenu.show(iconTypesSelector, 0, iconTypesSelector.getHeight());
                  SwingUtilities.invokeLater(
                      () -> {
                        int x = iconTypesSelector.getWidth() - popupMenu.getWidth();
                        popupMenu.setLocation(
                            iconTypesSelector.getLocationOnScreen().x + x,
                            iconTypesSelector.getLocationOnScreen().y
                                + iconTypesSelector.getHeight());
                      });
                });
          }

          iconTypesSelector.revalidate();
          iconTypesSelector.repaint();
        });
    editInputs.add(iconTypesSelector);

    return editInputs;
  }
}
