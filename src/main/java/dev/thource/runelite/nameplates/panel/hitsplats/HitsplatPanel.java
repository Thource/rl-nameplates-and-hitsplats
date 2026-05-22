package dev.thource.runelite.nameplates.panel.hitsplats;

import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.ScrollableContainer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public class HitsplatPanel extends JPanel {

  public HitsplatPanel(NameplatesPlugin plugin) {
    super(new BorderLayout());

    var themes = plugin.getHitsplatThemes();

    setBorder(new EmptyBorder(0, 0, 0, 0));
    setBackground(ColorScheme.DARK_GRAY_COLOR);

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

    var themeListSelector =
        new HitsplatThemeSelector(plugin, plugin.getActiveHitsplatTheme(), themes);
    scrollPanel.add(themeListSelector);

    scrollPanel.add(new JLabel("Customisation coming soon!"));
  }
}
