package dev.thource.runelite.nameplates.panel;

import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.nameplates.NameplatesPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class NameplatesPluginPanel extends PluginPanel {

    public NameplatesPluginPanel(NameplatesPlugin plugin) {
        super(false);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel tabsPanel = new JPanel();
        tabsPanel.setLayout(new BoxLayout(tabsPanel, BoxLayout.X_AXIS));
        tabsPanel.setPreferredSize(new Dimension(230, 34));
        tabsPanel.setBackground(new Color(0, 0, 0, 0));
        tabsPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
        add(tabsPanel, BorderLayout.NORTH);

        var bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        var nameplatesPanel = new NameplatesPanel(plugin);

        JButton wideButton1 = new JButton("Nameplates");
        wideButton1.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        wideButton1.setForeground(Color.WHITE);
        wideButton1.setPreferredSize(new Dimension(92, 24));
        wideButton1.setBorder(new EmptyBorder(6, 6, 6, 6));
        wideButton1.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        wideButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                wideButton1.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                wideButton1.setBackground(nameplatesPanel.getParent() != null ? ColorScheme.MEDIUM_GRAY_COLOR : ColorScheme.DARKER_GRAY_COLOR);
            }
        });
        tabsPanel.add(wideButton1, BorderLayout.WEST);

        JButton wideButton2 = new JButton("Hitsplats");
        wideButton2.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        wideButton2.setForeground(Color.WHITE);
        wideButton2.setPreferredSize(new Dimension(92, 24));
        wideButton2.setBorder(new EmptyBorder(6, 6, 6, 6));
        wideButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                wideButton2.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                wideButton2.setBackground(nameplatesPanel.getParent() == null ? ColorScheme.MEDIUM_GRAY_COLOR : ColorScheme.DARKER_GRAY_COLOR);
            }
        });
        wideButton1.addActionListener(e -> {
            if (nameplatesPanel.getParent() != null) {
                return;
            }

            EnhancedSwingUtilities.fastRemoveAll(bodyPanel);
            bodyPanel.add(nameplatesPanel);
            bodyPanel.revalidate();
            bodyPanel.repaint();

            wideButton1.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
            wideButton2.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        });
        wideButton2.addActionListener(e -> {
            if (nameplatesPanel.getParent() == null) {
                return;
            }

            // todo: add hitsplats panel
            EnhancedSwingUtilities.fastRemoveAll(bodyPanel);
            bodyPanel.revalidate();
            bodyPanel.repaint();

            wideButton1.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            wideButton2.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        });
        tabsPanel.add(wideButton2, BorderLayout.WEST);

        JButton supportButton = new JButton("");
        supportButton.setIcon(new ImageIcon(ImageUtil.loadImageResource(NameplatesPluginPanel.class, "kofi.png")));
        supportButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        supportButton.setForeground(Color.WHITE);
        supportButton.setPreferredSize(new Dimension(30, 24));
        supportButton.setBorder(new EmptyBorder(6, 6, 6, 6));
        supportButton.addActionListener(e -> LinkBrowser.browse("https://ko-fi.com/thource"));
        supportButton.setToolTipText("Buy me a coffee? :)");
        supportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                supportButton.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                supportButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            }
        });
        tabsPanel.add(supportButton, BorderLayout.EAST);

        add(bodyPanel, BorderLayout.CENTER);

        bodyPanel.add(nameplatesPanel);
    }

    @Override
    public void onActivate() {
    }

    @Override
    public void onDeactivate() {
    }
}
