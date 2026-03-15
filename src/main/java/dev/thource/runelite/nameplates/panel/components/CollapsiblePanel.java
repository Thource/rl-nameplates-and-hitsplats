package dev.thource.runelite.nameplates.panel.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class CollapsiblePanel extends JPanel {
  private static final ImageIcon SECTION_EXPAND_ICON;
  private static final ImageIcon SECTION_RETRACT_ICON;

  static {
    BufferedImage sectionRetractIcon =
        ImageUtil.loadImageResource(CollapsiblePanel.class, "arrow_right.png");
    sectionRetractIcon = ImageUtil.luminanceOffset(sectionRetractIcon, -121);
    SECTION_EXPAND_ICON = new ImageIcon(sectionRetractIcon);
    final BufferedImage sectionExpandIcon = ImageUtil.rotateImage(sectionRetractIcon, Math.PI / 2);
    SECTION_RETRACT_ICON = new ImageIcon(sectionExpandIcon);
  }

  private final JPanel contents = new JPanel();
  private final JButton button;
  private boolean isOpen = false;

  public CollapsiblePanel(String title, int width) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(ColorScheme.DARK_GRAY_COLOR);

    final var header = new JPanel();
    header.setLayout(new BorderLayout());
    header.setMinimumSize(new Dimension(width, 0));
    header.setPreferredSize(new Dimension(width, 30));
    header.setMaximumSize(new Dimension(width, 30));
    // For whatever reason, the header extends out by a single pixel when closed. Adding a single
    // pixel of
    // border on the right only affects the width when closed, fixing the issue.
    header.setBackground(ColorScheme.DARK_GRAY_COLOR);
    header.setBorder(
        new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, ColorScheme.MEDIUM_GRAY_COLOR),
            new EmptyBorder(0, 0, 3, 1)));
    add(header, BorderLayout.NORTH);

    button = new JButton(SECTION_EXPAND_ICON);
    button.setPreferredSize(new Dimension(18, 0));
    button.setBorder(new EmptyBorder(0, 0, 0, 5));
    button.setToolTipText("Expand");
    button.setBackground(ColorScheme.DARK_GRAY_COLOR);
    SwingUtil.removeButtonDecorations(button);
    header.add(button, BorderLayout.WEST);

    final JLabel panelName = new JLabel(title);
    panelName.setForeground(ColorScheme.BRAND_ORANGE);
    panelName.setFont(FontManager.getRunescapeBoldFont());
    panelName.setToolTipText(title);
    header.add(panelName, BorderLayout.CENTER);

    final MouseAdapter adapter =
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            toggle();
          }
        };
    button.addActionListener(actionEvent -> toggle());
    panelName.addMouseListener(adapter);
    header.addMouseListener(adapter);

    contents.setLayout(new DynamicGridLayout(0, 1, 0, 5));
    contents.setMinimumSize(new Dimension(width, 0));
    contents.setBorder(
        new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, ColorScheme.MEDIUM_GRAY_COLOR),
            new EmptyBorder(6, 0, 6, 0)));
    contents.setVisible(isOpen);
    add(contents, BorderLayout.SOUTH);
  }

  public Component addToPanel(Component comp) {
    contents.add(comp);

    return comp;
  }

  public void toggle() {
    isOpen = !isOpen;

    contents.setVisible(isOpen);
    button.setIcon(isOpen ? SECTION_RETRACT_ICON : SECTION_EXPAND_ICON);
    button.setToolTipText(isOpen ? "Retract" : "Expand");
    SwingUtilities.invokeLater(contents::revalidate);
  }
}
