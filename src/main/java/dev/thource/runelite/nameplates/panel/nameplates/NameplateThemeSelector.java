package dev.thource.runelite.nameplates.panel.nameplates;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.NameplatesPluginPanel;
import dev.thource.runelite.nameplates.panel.components.ListSelector;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.runelite.client.util.ImageUtil;

public class NameplateThemeSelector extends ListSelector<NameplateTheme> {
  private final NameplatesPlugin plugin;
  private final JPanel preview;
  private final Map<String, NameplateTheme> themes;
  private final Gson gson;
  private final Consumer<NameplateTheme> onPreviewThemeSelected;
  private final BiConsumer<NameplateTheme, Gson> editThemeAction;

  NameplateThemeSelector(
      NameplatesPlugin plugin,
      NameplateTheme defaultTheme,
      JPanel preview,
      Map<String, NameplateTheme> themes,
      Consumer<NameplateTheme> onPreviewThemeSelected,
      BiConsumer<NameplateTheme, Gson> editThemeAction) {
    super("Themes", defaultTheme, sortNameplateThemes(themes));
    this.plugin = plugin;
    this.preview = preview;
    this.themes = themes;
    this.gson = plugin.getGson();
    this.onPreviewThemeSelected = onPreviewThemeSelected;
    this.editThemeAction = editThemeAction;

    addChangeListener(this::onThemeChanged);
  }

  private static List<NameplateTheme> sortNameplateThemes(Map<String, NameplateTheme> themes) {
    return themes.values().stream()
        .sorted(Comparator.comparingInt(NameplateTheme::getOrder))
        .collect(Collectors.toList());
  }

  public void updateValues() {
    setValues(sortNameplateThemes(themes));
  }

  private void onThemeChanged(NameplateTheme sel) {
    if (sel != null) {
      onPreviewThemeSelected.accept(sel);
      preview.repaint();
    }

    clearButtons();

    final var themesList = sortNameplateThemes(themes);
    var selectedIndex = themesList.indexOf(sel);
    var staticThemes = themesList.stream().filter(t -> !t.isEditable()).count();
    if (sel != null && sel.isEditable()) {
      if (selectedIndex > staticThemes) {
        addButton(
            "Move up",
            new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "move-up.png")),
            () -> {
              var previousTheme = themesList.get(selectedIndex - 1);
              sel.setOrder(previousTheme.getOrder());
              previousTheme.setOrder(previousTheme.getOrder() + 1);

              updateValues();
            });
      }
      if (selectedIndex < themes.size() - 1) {
        addButton(
            "Move down",
            new ImageIcon(
                ImageUtil.loadImageResource(NameplatesPluginPanel.class, "move-down.png")),
            () -> {
              var nextTheme = themesList.get(selectedIndex + 1);
              nextTheme.setOrder(sel.getOrder());
              sel.setOrder(sel.getOrder() + 1);

              updateValues();
            });
      }
    }

    addButtonGlue();

    if (sel != null) {
      addButton(
          "Export theme (copy to clipboard)",
          new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "export.png")),
          () -> {
            Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(gson.toJson(sel)), null);

            JOptionPane.showMessageDialog(
                this,
                "Theme \"" + sel.getName() + "\" has been copied to your clipboard.",
                "Theme exported",
                JOptionPane.INFORMATION_MESSAGE);
          });

      if (sel.isEditable()) {
        addButton(
            "Edit theme",
            new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "edit.png")),
            () -> editThemeAction.accept(sel, gson));
        addButton(
            "Delete theme",
            new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "trash.png")),
            () -> {
              if (NameplatesPlugin.getConfirmation(
                  this,
                  "Are you sure you want to delete theme \""
                      + sel.getName()
                      + "\"?\nThis cannot be undone.",
                  "Confirm deletion",
                  JOptionPane.WARNING_MESSAGE)) {
                themes.remove(sel.getId());
                plugin.deleteNameplateTheme(sel.getId());
                updateValues();
              }
            });
      }
    }

    addButton(
        "Import theme",
        new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "import.png")),
        () -> {
          var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
          if (clipboard == null) {
            return;
          }

          String clipboardText;
          try {
            clipboardText = (String) clipboard.getTransferData(DataFlavor.stringFlavor);

            if (clipboardText.isBlank()) {
              throw new IOException("Clipboard is empty");
            }
          } catch (UnsupportedFlavorException | IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Theme failed to import. (clipboard error)",
                "Theme import error",
                JOptionPane.ERROR_MESSAGE);
            return;
          }

          CustomNameplateTheme theme = null;
          try {
            theme = CustomNameplateTheme.deserialize(clipboardText, gson, true);
          } catch (JsonParseException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                this,
                "Theme failed to import. (bad data)",
                "Theme import error",
                JOptionPane.ERROR_MESSAGE);
          }

          if (theme == null
              || !NameplatesPlugin.getConfirmation(
                  this,
                  "Import theme \""
                      + theme.getName()
                      + "\" with "
                      + theme.getElements().size()
                      + " elements?",
                  "Confirm import",
                  JOptionPane.INFORMATION_MESSAGE)) {
            return;
          }

          theme.setOrder(Math.max(0, themesList.get(themesList.size() - 1).getOrder() + 1));
          plugin.addNameplateTheme(theme);
          updateValues();
          selectValue(theme);

          plugin.saveNameplateThemes();
        });

    if (sel != null) {
      addButton(
          "Clone theme",
          new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "clone.png")),
          () -> {
            var theme = CustomNameplateTheme.deserialize(sel.serialize(gson, true), gson, true);
            theme.setName(theme.getName() + " (copy)");
            theme.setOrder(Math.max(0, themesList.get(themesList.size() - 1).getOrder() + 1));
            editThemeAction.accept(theme, gson);
          });
    }

    addButton(
        "Create new theme",
        new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "create.png")),
        () -> {
          var theme = new CustomNameplateTheme();
          theme.setName("New theme");
          theme.setOrder(Math.max(0, themesList.get(themesList.size() - 1).getOrder() + 1));
          editThemeAction.accept(theme, gson);
        });

    revalidate();
    repaint();
  }
}
