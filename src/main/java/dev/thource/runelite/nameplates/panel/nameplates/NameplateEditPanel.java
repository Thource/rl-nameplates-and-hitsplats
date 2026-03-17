package dev.thource.runelite.nameplates.panel.nameplates;

import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.ADD_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.DELETE_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.MOVE_DOWN_ICON;
import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.MOVE_UP_ICON;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.EnhancedSwingUtilities;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.ListSelector;
import dev.thource.runelite.nameplates.panel.components.StringInput;
import dev.thource.runelite.nameplates.panel.components.TabSwitcherPanel;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.elements.CombatLevelText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import dev.thource.runelite.nameplates.themes.nameplates.elements.HealthBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Icon;
import dev.thource.runelite.nameplates.themes.nameplates.elements.IconContainer;
import dev.thource.runelite.nameplates.themes.nameplates.elements.NameText;
import dev.thource.runelite.nameplates.themes.nameplates.elements.PrayerBar;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Rect;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.DynamicGridLayout;

@Slf4j
public class NameplateEditPanel extends JPanel {
  private final JLabel editingLabel;
  private final StringInput nameInput;
  private final IntInput widthInput;
  private final IntInput heightInput;
  private final IntInput heightWithPrayerBarInput;
  private final CheckboxInput stackInput;
  private final ListSelector<Element> elementsList;
  private final JPanel elementInputsPanel;
  @Getter private CustomNameplateTheme editingTheme;

  public NameplateEditPanel(
      NameplatesPanel nameplatesPanel, NameplatesPlugin plugin, JPanel preview) {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    editingLabel = new JLabel("Editing: ");
    add(editingLabel);

    var buttonContainer = new JPanel();
    buttonContainer.setBorder(new EmptyBorder(8, 0, 0, 0));
    buttonContainer.setLayout(new GridLayout(1, 2));
    add(buttonContainer);

    var cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> nameplatesPanel.editTheme(null, null));
    buttonContainer.add(cancelButton);

    var saveButton = new JButton("Save");
    saveButton.addActionListener(e -> nameplatesPanel.saveEdit(editingTheme));
    buttonContainer.add(saveButton);

    var themeConfigTab = new JPanel();
    themeConfigTab.setLayout(new DynamicGridLayout(0, 1, 0, 5));
    themeConfigTab.setBorder(new EmptyBorder(8, 0, 0, 0));

    var elementsTab = new JPanel();
    elementsTab.setLayout(new DynamicGridLayout(0, 1, 0, 5));
    elementsTab.setBorder(new EmptyBorder(8, 0, 0, 0));
    elementsTab.setVisible(false);

    var tabSwitcher =
        new TabSwitcherPanel(
            List.of("Theme config", "Elements"), List.of(themeConfigTab, elementsTab));
    add(tabSwitcher);

    add(themeConfigTab);
    add(elementsTab);

    nameInput = new StringInput("Name", "Name", (value) -> editingTheme.setName(value.trim()));
    themeConfigTab.add(nameInput);

    widthInput =
        new IntInput(
            "Width",
            0,
            0,
            999,
            (value) -> {
              editingTheme.setWidth(value);
              preview.repaint();
            });
    themeConfigTab.add(widthInput);

    heightInput =
        new IntInput(
            "Height",
            0,
            0,
            999,
            (value) -> {
              editingTheme.setHeight(value);
              preview.repaint();
            });
    themeConfigTab.add(heightInput);

    heightWithPrayerBarInput =
        new IntInput(
            "Height with prayer bar",
            0,
            0,
            999,
            (value) -> {
              editingTheme.setHeightWithPrayerBar(value);
              preview.repaint();
            });
    themeConfigTab.add(heightWithPrayerBarInput);

    stackInput =
        new CheckboxInput("Stack nameplates", true, (value) -> editingTheme.setStacking(value));
    themeConfigTab.add(stackInput);

    elementsList = new ListSelector<>("Elements", null, List.of());
    elementsTab.add(elementsList);

    elementInputsPanel = new JPanel(new DynamicGridLayout(0, 1, 0, 5));
    elementsTab.add(elementInputsPanel);
    elementsList.addChangeListener(
        el -> {
          EnhancedSwingUtilities.fastRemoveAll(elementInputsPanel);
          elementsList.clearButtons();

          if (el != null) {
            var selectedIndex = editingTheme.getElements().indexOf(el);
            if (selectedIndex > 0) {
              elementsList.addButton(
                  "Move up",
                  MOVE_UP_ICON,
                  () -> {
                    var previousEl = editingTheme.getElements().get(selectedIndex - 1);
                    editingTheme.getElements().set(selectedIndex - 1, el);
                    editingTheme.getElements().set(selectedIndex, previousEl);

                    elementsList.setValues(editingTheme.getElements());
                  });
            }
            if (selectedIndex < editingTheme.getElements().size() - 1) {
              elementsList.addButton(
                  "Move down",
                  MOVE_DOWN_ICON,
                  () -> {
                    var previousEl = editingTheme.getElements().get(selectedIndex + 1);
                    editingTheme.getElements().set(selectedIndex + 1, el);
                    editingTheme.getElements().set(selectedIndex, previousEl);

                    elementsList.setValues(editingTheme.getElements());
                  });
            }
          }
          elementsList.addButtonGlue();

          if (el != null) {
            el.getEditInputs(plugin)
                .forEach(
                    comp -> {
                      elementInputsPanel.add(comp);
                      comp.addGenericChangeListener(preview::repaint);
                    });

            elementsList.addButton(
                "Remove element",
                DELETE_ICON,
                () -> {
                  editingTheme.getElements().remove(el);
                  elementsList.setValues(editingTheme.getElements());
                  preview.repaint();
                });
          }

          elementsList.addButton(
              "Add new element",
              ADD_ICON,
              () -> {
                var popupMenu = new JPopupMenu();

                List.of(
                        NameText.class,
                        CombatLevelText.class,
                        HealthBar.class,
                        PrayerBar.class,
                        Icon.class,
                        IconContainer.class,
                        Rect.class)
                    .forEach(
                        clazz -> {
                          var menuItem = new JMenuItem(clazz.getSimpleName());
                          menuItem.addActionListener(
                              e -> {
                                try {
                                  var newEl = clazz.getDeclaredConstructor().newInstance();
                                  editingTheme.getElements().add(newEl);

                                  elementsList.setValues(editingTheme.getElements());
                                  elementsList.selectValue(newEl);
                                  preview.repaint();
                                } catch (InstantiationException
                                    | IllegalAccessException
                                    | InvocationTargetException
                                    | NoSuchMethodException ex) {
                                  log.error("Failed to create new element", ex);
                                }
                              });
                          popupMenu.add(menuItem);
                        });

                popupMenu.show(elementsList, 0, elementsList.getHeight());
                SwingUtilities.invokeLater(
                    () -> {
                      int x = elementsList.getWidth() - popupMenu.getWidth();
                      popupMenu.setLocation(
                          elementsList.getLocationOnScreen().x + x,
                          elementsList.getLocationOnScreen().y + elementsList.getHeight());
                    });
              });

          elementInputsPanel.revalidate();
          elementInputsPanel.repaint();

          elementsList.revalidate();
          elementsList.repaint();
        });
  }

  public void editTheme(CustomNameplateTheme theme, Gson gson) {
    if (theme == null) {
      editingTheme = null;
      return;
    }

    editingTheme = CustomNameplateTheme.deserialize(theme.serialize(gson, false), gson, false);

    editingLabel.setText("Editing: " + editingTheme.getName());
    nameInput.setValue(editingTheme.getName());
    widthInput.setValue(editingTheme.getWidth());
    heightInput.setValue(editingTheme.getHeight());
    heightWithPrayerBarInput.setValue(editingTheme.getHeightWithPrayerBar());
    stackInput.setValue(editingTheme.isStacking());

    elementsList.setValues(editingTheme.getElements());
  }
}
