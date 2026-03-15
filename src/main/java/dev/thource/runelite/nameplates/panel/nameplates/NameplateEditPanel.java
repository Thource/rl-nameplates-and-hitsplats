package dev.thource.runelite.nameplates.panel.nameplates;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.ListSelector;
import dev.thource.runelite.nameplates.panel.components.StringInput;
import dev.thource.runelite.nameplates.panel.components.TabSwitcherPanel;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.client.ui.DynamicGridLayout;

public class NameplateEditPanel extends JPanel {
  private final JLabel editingLabel;
  private final StringInput nameInput;
  private final IntInput widthInput;
  private final IntInput heightInput;
  private final IntInput heightWithPrayerBarInput;
  private final CheckboxInput stackInput;
  private final ListSelector<Element> elementsList;
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
    saveButton.addActionListener(
        e -> {
          nameplatesPanel.saveEdit(editingTheme);
        });
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
  }

  public void editTheme(CustomNameplateTheme theme, Gson gson) {
    if (theme == null) {
      editingTheme = null;
      return;
    }

    editingTheme = CustomNameplateTheme.deserialize(theme.serialize(gson, false), gson, false);

    editingLabel.setText("Editing: " + theme.getName());
    nameInput.setValue(theme.getName());
    widthInput.setValue(theme.getWidth());
    heightInput.setValue(theme.getHeight());
    heightWithPrayerBarInput.setValue(theme.getHeightWithPrayerBar());
    stackInput.setValue(theme.isStacking());

    elementsList.setValues(theme.getElements());
  }
}
