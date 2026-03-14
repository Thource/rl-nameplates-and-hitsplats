package dev.thource.runelite.nameplates.panel.nameplates;

import com.google.gson.JsonParseException;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import dev.thource.runelite.nameplates.NameplateSkullIcon;
import dev.thource.runelite.nameplates.panel.CheckboxInput;
import dev.thource.runelite.nameplates.panel.CollapsiblePanel;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.DropdownInput;
import dev.thource.runelite.nameplates.panel.IntInput;
import dev.thource.runelite.nameplates.panel.ListSelector;
import dev.thource.runelite.nameplates.panel.NameplatesPluginPanel;
import dev.thource.runelite.nameplates.panel.ScrollableContainer;
import dev.thource.runelite.nameplates.panel.StringInput;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.FlatDarkTheme;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.OSRSTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Point;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

public class NameplatesPanel extends JPanel {
    private final List<NameplateTheme> themes = new ArrayList<>();
    private final ListSelector<NameplateTheme> themeListSelector;
    private final NameplateEditPanel editPanel;
    private NameplateTheme previewTheme;

    private void editTheme(NameplateTheme theme) {
        if (!theme.isEditable() && !(theme instanceof CustomNameplateTheme)) {
            return;
        }

        editPanel.editTheme((CustomNameplateTheme) theme);

        themeListSelector.setVisible(false);
        editPanel.setVisible(true);
    }

    public NameplatesPanel(NameplatesPlugin plugin) {
        super(new BorderLayout());

        var flatDarkTheme = new FlatDarkTheme();
        var osrsTheme = new OSRSTheme();
        themes.add(flatDarkTheme);
        themes.add(osrsTheme);
        previewTheme = themes.get(0);

        // spam test data
        for (int i = 0; i < 10; i++) {
            themes.add(new CustomNameplateTheme());
        }

        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        var previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        previewPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        add(previewPanel, BorderLayout.NORTH);

        var titleLabel = new JLabel("Nameplate preview");
        titleLabel.setBorder(new EmptyBorder(0, 4, 2, 0));
        titleLabel.setForeground(Color.WHITE);
        previewPanel.add(titleLabel, BorderLayout.NORTH);

        var bgImage = ImageUtil.loadImageResource(this.getClass(), "nameplate-preview.png");
        var dummyActor = new DummyPlayer();
        var nameplate = new DummyNameplate(plugin, dummyActor);

        final var previewWidth = 220;
        final var maxOffset = bgImage.getWidth() - previewWidth;

        var preview = new JPanel() {
            private int offsetX = 0;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, -(maxOffset / 2) + offsetX, 0, null);

                plugin.getNameplatesOverlay().renderNameplate((Graphics2D) g, nameplate, new Point((previewWidth / 2) + offsetX, 132), previewTheme);
            }
        };
        preview.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                var newOffset = (int) ((e.getX() - (previewWidth / 2f)) / (previewWidth / 2f) * -(maxOffset / 2f));

                if (newOffset != preview.offsetX) {
                    preview.offsetX = newOffset;
                    preview.repaint();
                }
            }
        });
        preview.setLayout(new BorderLayout());
        preview.setBorder(new EmptyBorder(2, 2, 2, 2));
        preview.setPreferredSize(new java.awt.Dimension(previewWidth, 196));
        previewPanel.add(preview, BorderLayout.CENTER);

        var scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));

//        var scrollWrapper = new JPanel(new BorderLayout());
//        scrollWrapper.add(scrollPanel, BorderLayout.NORTH);
//        scrollWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);

        var scrollPane = new JScrollPane(new ScrollableContainer(scrollPanel));
        scrollPane.setBorder(new EmptyBorder(4, 0, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(16, 0));
        scrollPane.getVerticalScrollBar().setBorder(new EmptyBorder(0, 9, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(21);

        add(scrollPane, BorderLayout.CENTER);

        var previewOptionsWrapper = new JPanel(new BorderLayout());
        previewOptionsWrapper.setBackground(Color.RED);
        scrollPanel.add(previewOptionsWrapper);

        var previewOptions = new CollapsiblePanel("Preview options", 224);
        previewOptionsWrapper.add(previewOptions, BorderLayout.NORTH);

        var nameInput = new StringInput("Name", nameplate.getName(), (newName) -> {
            nameplate.setName(newName);
            preview.repaint();
        });
        previewOptions.addToPanel(nameInput);

        var levelInput = new IntInput("Combat level", nameplate.getCombatLevel(), 1, 9_999, (level) -> {
            nameplate.setCombatLevel(level);
            preview.repaint();
        });
        previewOptions.addToPanel(levelInput);

        var currentHealthInput = new IntInput("Current HP", nameplate.getCurrentHealth(), 1, 99, (value) -> {
            nameplate.setCurrentHealth(value);
            preview.repaint();
        });
        previewOptions.addToPanel(currentHealthInput);

        var maxHealthInput = new IntInput("Max HP", nameplate.getMaxHealth(), 1, 99, (value) -> {
            nameplate.setMaxHealth(value);
            preview.repaint();
        });
        previewOptions.addToPanel(maxHealthInput);

        var prayerBarInput = new CheckboxInput("Prayer bar", nameplate.shouldDrawPrayerBar(), (value) -> {
            nameplate.setDrawPrayerBar(value);
            preview.repaint();
        });
        previewOptions.addToPanel(prayerBarInput);

        var currentPrayerInput = new IntInput("Current PP", nameplate.getCurrentPrayer(), 1, 99, (value) -> {
            nameplate.setCurrentPrayer(value);
            preview.repaint();
        });
        previewOptions.addToPanel(currentPrayerInput);

        var maxPrayerInput = new IntInput("Max PP", nameplate.getMaxPrayer(), 1, 99, (value) -> {
            nameplate.setMaxPrayer(value);
            preview.repaint();
        });
        previewOptions.addToPanel(maxPrayerInput);

        var overheadInput = new DropdownInput<>("Overhead icon", NameplateHeadIcon.get(dummyActor.getOverheadIcon()), NameplateHeadIcon.values(), (icon) -> {
            dummyActor.setOverhead(icon.getHeadIcon());
            preview.repaint();
        });
        previewOptions.addToPanel(overheadInput);

        var skullInput = new DropdownInput<>("Skull icon", NameplateSkullIcon.get(dummyActor.getSkullIcon()), NameplateSkullIcon.values(), (icon) -> {
            dummyActor.setSkull(icon.getSkullIcon());
            preview.repaint();
        });
        previewOptions.addToPanel(skullInput);

        var noLootInput = new CheckboxInput("No-loot", nameplate.isNoLoot(), (value) -> {
            nameplate.setNoLoot(value);
            preview.repaint();
        });
        previewOptions.addToPanel(noLootInput);

        var hoveredInput = new CheckboxInput("Hovered", nameplate.isHovered(), (value) -> {
            nameplate.setHovered(value);
            preview.repaint();
        });
        previewOptions.addToPanel(hoveredInput);

        var hintArrowInput = new CheckboxInput("Hint arrow", nameplate.hasHintArrow(), (value) -> {
            nameplate.setHintArrow(value);
            preview.repaint();
        });
        previewOptions.addToPanel(hintArrowInput);

        themeListSelector = createThemeSelector(plugin, flatDarkTheme, preview);
        scrollPanel.add(themeListSelector);

        editPanel = new NameplateEditPanel();
        editPanel.setVisible(false);
        scrollPanel.add(editPanel);
    }

    private void updateThemeSelector() {
        themeListSelector.setValues(themes);
    }

    @Nonnull
    private ListSelector<NameplateTheme> createThemeSelector(NameplatesPlugin plugin, FlatDarkTheme flatDarkTheme, JPanel preview) {
        var themeListSelector = new ListSelector<>("Themes", flatDarkTheme, themes);
        themeListSelector.addChangeListener(sel -> {
            if (sel != null) {
                previewTheme = sel;
                preview.repaint();
            }

            themeListSelector.clearButtons();

            var selectedIndex = themes.indexOf(sel);
            var staticThemes = themes.stream().filter(t -> !t.isEditable()).count();
            if (sel != null && sel.isEditable()) {
                if (selectedIndex > staticThemes) {
                    themeListSelector.addButton("Move up", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "move-up.png")), () -> {
                        themes.remove(sel);
                        themes.add(selectedIndex - 1, sel);

                        updateThemeSelector();
                    });
                }
                if (selectedIndex < themes.size() - 1) {
                    themeListSelector.addButton("Move down", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "move-down.png")), () -> {
                        themes.remove(sel);
                        themes.add(selectedIndex + 1, sel);

                        updateThemeSelector();
                    });
                }
            }
            themeListSelector.addButtonGlue();
            if (sel != null) {
                themeListSelector.addButton("Export theme (copy to clipboard)", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "export.png")), () -> {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(plugin.getGson().toJson(sel)), null);

                    JOptionPane.showMessageDialog(themeListSelector, "Theme \"" + sel.getName() + "\" has been copied to your clipboard.", "Theme exported", JOptionPane.INFORMATION_MESSAGE);
                });

                if (sel.isEditable()) {
                    themeListSelector.addButton("Edit theme", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "edit.png")), () -> {
                        editTheme(sel);
                    });
                    themeListSelector.addButton("Delete theme", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "trash.png")), () -> {
                        if (NameplatesPlugin.getConfirmation(themeListSelector, "Are you sure you want to delete theme \"" + sel.getName() + "\"?\nThis cannot be undone.", "Confirm deletion", JOptionPane.WARNING_MESSAGE)) {
                            themes.remove(sel);

                            updateThemeSelector();
                        }
                    });
                }
            }
            themeListSelector.addButton("Import theme", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "import.png")), () -> {
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
                    JOptionPane.showMessageDialog(themeListSelector, "Theme failed to import. (clipboard error)", "Theme import error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CustomNameplateTheme theme = null;
                try {
                    theme = CustomNameplateTheme.deserialize(clipboardText, plugin.getGson());
                } catch (JsonParseException | IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(themeListSelector, "Theme failed to import. (bad data)", "Theme import error", JOptionPane.ERROR_MESSAGE);
                }

                if (theme == null || !NameplatesPlugin.getConfirmation(themeListSelector, "Import theme \"" + theme.getName() + "\" with " + theme.getElements().size() + " elements?", "Confirm import", JOptionPane.INFORMATION_MESSAGE)) {
                    return;
                }

                themes.add(theme);
                updateThemeSelector();
                themeListSelector.selectValue(theme);

                // todo: save the theme
            });
            if (sel != null) {
                themeListSelector.addButton("Clone theme", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "clone.png")), () -> {
                    var theme = CustomNameplateTheme.deserialize(plugin.getGson().toJson(sel), plugin.getGson());
                    theme.setName(theme.getName() + " (copy)");
                    editTheme(theme);
                });
            }
            themeListSelector.addButton("Create new theme", new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "create.png")), () -> {
                var theme = new CustomNameplateTheme();
                theme.setName("New theme");
                editTheme(theme);
            });
            themeListSelector.revalidate();
        });
        return themeListSelector;
    }
}
