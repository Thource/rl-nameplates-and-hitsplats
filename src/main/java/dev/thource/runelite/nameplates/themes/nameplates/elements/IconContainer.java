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
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import net.runelite.api.coords.Direction;

public class IconContainer extends Element {
  protected int iconSize;
  protected int padding;
  protected boolean isVertical = false;
  protected final List<IconType> iconTypes = new ArrayList<>();

  public IconContainer() {
    super();

    iconSize = 26;
    padding = 4;
  }

  public IconContainer(
      String name,
      PositionProvider xPositionProvider,
      PositionProvider yPositionProvider,
      int iconSize,
      int padding,
      boolean isVertical,
      List<IconType> iconTypes) {
    super(name, xPositionProvider, yPositionProvider);

    this.iconSize = iconSize;
    this.padding = padding;
    this.isVertical = isVertical;
    this.iconTypes.addAll(iconTypes);
  }

  @Override
  public void draw(Nameplate nameplate, Graphics2D graphics, int x, int y) {
    var iconsToDraw =
        iconTypes.stream()
            .filter(iconType -> Icon.shouldDraw(nameplate, iconType))
            .toArray(IconType[]::new);
    var totalSize = iconSize * iconsToDraw.length + (iconsToDraw.length - 1) * padding;

    if (isVertical) {
      x += xPositionProvider.get(nameplate, iconSize);
      y += yPositionProvider.get(nameplate, totalSize);
    } else {
      x += xPositionProvider.get(nameplate, totalSize);
      y += yPositionProvider.get(nameplate, iconSize);
    }

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

    editInputs.add(new CheckboxInput("Vertical", isVertical, value -> isVertical = value));
    editInputs.add(new IntInput("Icon size", iconSize, 1, 999, value -> iconSize = value));
    editInputs.add(new IntInput("Icon spacing", padding, 0, 999, value -> padding = value));
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
