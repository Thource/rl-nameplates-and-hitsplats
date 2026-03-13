package dev.thource.runelite.nameplates.panel.nameplates;

import dev.thource.runelite.nameplates.panel.CheckboxInput;
import dev.thource.runelite.nameplates.panel.IntInput;
import dev.thource.runelite.nameplates.panel.ListSelector;
import dev.thource.runelite.nameplates.panel.StringInput;
import dev.thource.runelite.nameplates.panel.components.TabSwitcherPanel;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Element;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class NameplateEditPanel extends JPanel {
    private final JLabel editingLabel;
    private final StringInput nameInput;
    private final IntInput widthInput;
    private final IntInput heightInput;
    private final IntInput heightWithPrayerBarInput;
    private final CheckboxInput stackInput;
    private final ListSelector<Element> elementsList;
    private CustomNameplateTheme editingTheme;

    public NameplateEditPanel() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        editingLabel = new JLabel("Editing: ");
        add(editingLabel);

        var buttonContainer = new JPanel();
        buttonContainer.setBorder(new EmptyBorder(8, 0, 0, 0));
        buttonContainer.setLayout(new GridLayout(1, 2));
        add(buttonContainer);

        var cancelButton = new JButton("Cancel");
        buttonContainer.add(cancelButton);

        var saveButton = new JButton("Save");
        buttonContainer.add(saveButton);

        var themeConfigTab = new JPanel();
        themeConfigTab.setLayout(new BoxLayout(themeConfigTab, BoxLayout.Y_AXIS));
        themeConfigTab.setBorder(new EmptyBorder(8, 0, 0, 0));

        var elementsTab = new JPanel();
        elementsTab.setLayout(new BoxLayout(elementsTab, BoxLayout.Y_AXIS));
        elementsTab.setBorder(new EmptyBorder(8, 0, 0, 0));
        elementsTab.setVisible(false);

        var tabSwitcher = new TabSwitcherPanel(
                List.of("Theme config", "Elements"),
                List.of(elementsTab, elementsTab)
        );
        add(tabSwitcher);

        add(themeConfigTab);
        add(elementsTab);

        nameInput = new StringInput("Name", "Name", (value) -> editingTheme.setName(value));
        themeConfigTab.add(nameInput);

        widthInput = new IntInput("Width", 0, 0, 999, (value) -> editingTheme.setWidth(value));
        themeConfigTab.add(widthInput);

        heightInput = new IntInput("Height", 0, 0, 999, (value) -> editingTheme.setHeight(value));
        themeConfigTab.add(heightInput);

        heightWithPrayerBarInput = new IntInput("Height with prayer bar", 0, 0, 999, (value) -> editingTheme.setHeightWithPrayerBar(value));
        themeConfigTab.add(heightWithPrayerBarInput);

        stackInput = new CheckboxInput("Stack nameplates", true, (value) -> editingTheme.setStacking(value));
        themeConfigTab.add(stackInput);

        elementsList = new ListSelector<>("Elements", null, List.of());
        elementsTab.add(elementsList);
    }

    public void editTheme(CustomNameplateTheme theme) {
        editingTheme = theme;

        editingLabel.setText("Editing: " + theme.getName());
        nameInput.setValue(theme.getName());
        widthInput.setValue(theme.getWidth());
        heightInput.setValue(theme.getHeight());
        heightWithPrayerBarInput.setValue(theme.getHeightWithPrayerBar());
        stackInput.setValue(theme.isStacking());

        elementsList.setValues(theme.getElements());
    }
}
