package dev.thource.runelite.nameplates.panel.hitsplats;

import static dev.thource.runelite.nameplates.panel.NameplatesPluginPanel.SET_ACTIVE_ICON;

import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ListSelector;
import dev.thource.runelite.nameplates.themes.hitsplats.HitsplatTheme;
import java.awt.Component;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class HitsplatThemeSelector extends ListSelector<HitsplatTheme> {
  private final NameplatesPlugin plugin;
  private final Map<String, HitsplatTheme> themes;

  HitsplatThemeSelector(
      NameplatesPlugin plugin, HitsplatTheme defaultTheme, Map<String, HitsplatTheme> themes) {
    super("Themes", defaultTheme, sortThemes(themes));
    this.plugin = plugin;
    this.themes = themes;

    list.setCellRenderer(
        new DefaultListCellRenderer() {
          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            var name = ((HitsplatTheme) value).getName();

            if (plugin.getActiveHitsplatTheme() == value) {
              name = "* " + name;
            }

            return super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
          }
        });

    addChangeListener(this::onThemeChanged);
  }

  private static List<HitsplatTheme> sortThemes(Map<String, HitsplatTheme> themes) {
    return themes.values().stream()
        .sorted(Comparator.comparingInt(HitsplatTheme::getOrder))
        .collect(Collectors.toList());
  }

  public void updateValues() {
    setValues(sortThemes(themes));
  }

  private void onThemeChanged(HitsplatTheme sel) {
    clearButtons();

    addButtonGlue();

    if (sel != null) {
      addButton(
          "Set active theme",
          SET_ACTIVE_ICON,
          () -> {
            plugin.setActiveHitsplatTheme(sel);
            updateValues();
          });
    }

    revalidate();
    repaint();
  }
}
