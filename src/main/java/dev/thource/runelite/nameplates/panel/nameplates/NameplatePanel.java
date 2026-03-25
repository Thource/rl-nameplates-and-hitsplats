package dev.thource.runelite.nameplates.panel.nameplates;

import com.google.gson.Gson;
import dev.thource.runelite.nameplates.NameplateHeadIcon;
import dev.thource.runelite.nameplates.NameplateSkullIcon;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.CollapsiblePanel;
import dev.thource.runelite.nameplates.panel.components.DropdownInput;
import dev.thource.runelite.nameplates.panel.components.IntInput;
import dev.thource.runelite.nameplates.panel.components.ScrollableContainer;
import dev.thource.runelite.nameplates.panel.components.StringInput;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Point;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

public class NameplatePanel extends JPanel {
  private final NameplateThemeSelector themeListSelector;
  private final NameplateEditPanel editPanel;
  private final NameplatesPlugin plugin;
  private final PreviewPanel preview;
  private NameplateTheme previewTheme;

  void editTheme(NameplateTheme theme, Gson gson) {
    if (theme != null && theme.isEditable() && theme instanceof CustomNameplateTheme) {
      editPanel.editTheme((CustomNameplateTheme) theme, gson);

      themeListSelector.setVisible(false);
      editPanel.setVisible(true);

      return;
    }

    themeListSelector.setVisible(true);
    editPanel.setVisible(false);
    editPanel.editTheme(null, null);
  }

  void saveEdit(NameplateTheme theme) {
    editTheme(null, null);

    plugin.addNameplateTheme(theme);
    plugin.saveNameplateThemes();
    themeListSelector.updateValues();
    themeListSelector.selectValue(theme);
  }

  public NameplatePanel(NameplatesPlugin plugin) {
    super(new BorderLayout());
    this.plugin = plugin;

    var themes = plugin.getNameplateThemes();
    previewTheme = plugin.getActiveNameplateTheme();

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

    final var itemHpChange = new StatChange();
    itemHpChange.setRelative(4);
    itemHpChange.setTheoretical(4);
    nameplate.setHoveredItemHpChange(itemHpChange);
    final var itemPrayerChange = new StatChange();
    itemPrayerChange.setRelative(4);
    itemPrayerChange.setTheoretical(4);
    nameplate.setHoveredItemPrayerChange(itemPrayerChange);

    final var previewWidth = 220;
    final var maxOffset = bgImage.getWidth() - previewWidth;

    preview = new PreviewPanel(bgImage, maxOffset, plugin, nameplate, previewWidth);
    preview.addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseMoved(MouseEvent e) {
            var newOffset =
                (int) ((e.getX() - (previewWidth / 2f)) / (previewWidth / 2f) * -(maxOffset / 2f));

            if (newOffset != preview.getOffsetX()) {
              preview.setOffsetX(newOffset);
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

    var scrollWrapper = new JPanel(new BorderLayout());
    scrollWrapper.add(scrollPanel, BorderLayout.NORTH);
    scrollWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);

    var scrollPane = new JScrollPane(new ScrollableContainer(scrollWrapper));
    scrollPane.setBorder(new EmptyBorder(4, 0, 0, 0));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(16, 0));
    scrollPane.getVerticalScrollBar().setBorder(new EmptyBorder(0, 9, 0, 0));
    scrollPane.getVerticalScrollBar().setUnitIncrement(21);

    add(scrollPane, BorderLayout.CENTER);

    var previewOptions = new CollapsiblePanel("Preview options", 224);
    scrollPanel.add(previewOptions);

    var nameInput =
        new StringInput(
            "Name",
            nameplate.getName(),
            (newName) -> {
              nameplate.setName(newName);
              preview.repaint();
            });
    previewOptions.addToPanel(nameInput);

    var levelInput =
        new IntInput(
            "Combat level",
            nameplate.getCombatLevel(),
            1,
            9_999,
            (level) -> {
              nameplate.setCombatLevel(level);
              preview.repaint();
            });
    previewOptions.addToPanel(levelInput);

    var healthState =
        new DropdownInput<>(
            "Health state",
            HealthState.NONE,
            HealthState.values(),
            state -> {
              nameplate.setHealthState(state);
              preview.repaint();
            });
    previewOptions.addToPanel(healthState);

    var currentHealthInput =
        new IntInput(
            "Current HP",
            nameplate.getCurrentHealth(),
            1,
            99,
            (value) -> {
              nameplate.setCurrentHealth(value);
              itemHpChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentHealth(),
                      Math.min(
                          itemHpChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxHealth() - nameplate.getCurrentHealth()))));
              preview.repaint();
            });
    previewOptions.addToPanel(currentHealthInput);

    var maxHealthInput =
        new IntInput(
            "Max HP",
            nameplate.getMaxHealth(),
            1,
            99,
            (value) -> {
              nameplate.setMaxHealth(value);
              itemHpChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentHealth(),
                      Math.min(
                          itemHpChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxHealth() - nameplate.getCurrentHealth()))));
              preview.repaint();
            });
    previewOptions.addToPanel(maxHealthInput);

    var healthConsumableDelta =
        new IntInput(
            "HP consumable delta",
            itemHpChange.getTheoretical(),
            -99,
            99,
            value -> {
              itemHpChange.setTheoretical(value);
              itemHpChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentHealth(),
                      Math.min(
                          itemHpChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxHealth() - nameplate.getCurrentHealth()))));
              preview.repaint();
            });
    previewOptions.addToPanel(healthConsumableDelta);

    var prayerBarInput =
        new CheckboxInput(
            "Prayer bar",
            nameplate.shouldDrawPrayerBar(),
            (value) -> {
              nameplate.setDrawPrayerBar(value);
              preview.repaint();
            });
    previewOptions.addToPanel(prayerBarInput);

    var currentPrayerInput =
        new IntInput(
            "Current PP",
            nameplate.getCurrentPrayer(),
            1,
            99,
            (value) -> {
              nameplate.setCurrentPrayer(value);
              itemPrayerChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentPrayer(),
                      Math.min(
                          itemPrayerChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxPrayer() - nameplate.getCurrentPrayer()))));
              preview.repaint();
            });
    previewOptions.addToPanel(currentPrayerInput);

    var maxPrayerInput =
        new IntInput(
            "Max PP",
            nameplate.getMaxPrayer(),
            1,
            99,
            (value) -> {
              nameplate.setMaxPrayer(value);
              itemPrayerChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentPrayer(),
                      Math.min(
                          itemPrayerChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxPrayer() - nameplate.getCurrentPrayer()))));
              preview.repaint();
            });
    previewOptions.addToPanel(maxPrayerInput);

    var prayerConsumableDelta =
        new IntInput(
            "PP consumable delta",
            itemPrayerChange.getTheoretical(),
            -99,
            99,
            value -> {
              itemPrayerChange.setTheoretical(value);
              itemPrayerChange.setRelative(
                  Math.max(
                      -nameplate.getCurrentPrayer(),
                      Math.min(
                          itemPrayerChange.getTheoretical(),
                          Math.max(0, nameplate.getMaxPrayer() - nameplate.getCurrentPrayer()))));
              preview.repaint();
            });
    previewOptions.addToPanel(prayerConsumableDelta);

    var overheadInput =
        new DropdownInput<>(
            "Overhead icon",
            NameplateHeadIcon.get(dummyActor.getOverheadIcon()),
            NameplateHeadIcon.values(),
            (icon) -> {
              dummyActor.setOverhead(icon.getHeadIcon());
              preview.repaint();
            });
    previewOptions.addToPanel(overheadInput);

    var skullInput =
        new DropdownInput<>(
            "Skull icon",
            NameplateSkullIcon.get(dummyActor.getSkullIcon()),
            NameplateSkullIcon.values(),
            (icon) -> {
              dummyActor.setSkull(icon.getSkullIcon());
              preview.repaint();
            });
    previewOptions.addToPanel(skullInput);

    var noLootInput =
        new CheckboxInput(
            "No-loot",
            nameplate.isNoLoot(),
            (value) -> {
              nameplate.setNoLoot(value);
              preview.repaint();
            });
    previewOptions.addToPanel(noLootInput);

    var hoveredInput =
        new CheckboxInput(
            "Hovered",
            nameplate.isHovered(),
            (value) -> {
              nameplate.setHovered(value);
              preview.repaint();
            });
    previewOptions.addToPanel(hoveredInput);

    var hintArrowInput =
        new CheckboxInput(
            "Hint arrow",
            nameplate.hasHintArrow(),
            (value) -> {
              nameplate.setHintArrow(value);
              preview.repaint();
            });
    previewOptions.addToPanel(hintArrowInput);

    themeListSelector =
        new NameplateThemeSelector(
            plugin,
            previewTheme,
            preview,
            themes,
            (theme) -> previewTheme = theme,
            this::editTheme);
    scrollPanel.add(themeListSelector);

    editPanel = new NameplateEditPanel(this, plugin, preview);
    editPanel.setVisible(false);
    scrollPanel.add(editPanel);
  }

  private class PreviewPanel extends JPanel {
    private final BufferedImage bgImage;
    private final int maxOffset;
    private final NameplatesPlugin plugin;
    private final DummyNameplate nameplate;
    private final int previewWidth;
    @Getter @Setter private int offsetX;

    public PreviewPanel(
        BufferedImage bgImage,
        int maxOffset,
        NameplatesPlugin plugin,
        DummyNameplate nameplate,
        int previewWidth) {
      this.bgImage = bgImage;
      this.maxOffset = maxOffset;
      this.plugin = plugin;
      this.nameplate = nameplate;
      this.previewWidth = previewWidth;
      offsetX = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(bgImage, -(maxOffset / 2) + offsetX, 0, null);

      plugin
          .getNameplatesOverlay()
          .renderNameplate(
              (Graphics2D) g,
              nameplate,
              new Point((previewWidth / 2) + offsetX, 132),
              editPanel.getEditingTheme() != null ? editPanel.getEditingTheme() : previewTheme);
    }
  }
}
