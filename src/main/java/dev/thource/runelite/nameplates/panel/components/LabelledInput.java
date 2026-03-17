package dev.thource.runelite.nameplates.panel.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.FontManager;

public abstract class LabelledInput extends JPanel {
  protected final JLabel label;
  protected final JPanel inputPanel;

  protected LabelledInput(String name) {
    this(name, false);
  }

  protected LabelledInput(String name, boolean verticalLayout) {
    setOpaque(false);
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(0, 0, 0, 0));

    inputPanel = new JPanel();
    inputPanel.setOpaque(false);
    inputPanel.setLayout(verticalLayout ? new BorderLayout() : new GridLayout(1, 2));
    inputPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
    add(inputPanel, BorderLayout.NORTH);

    label = new JLabel(name);
    label.setForeground(Color.WHITE);
    label.setFont(FontManager.getRunescapeFont());
    label.setToolTipText(name);
    label.setHorizontalAlignment(JLabel.LEFT);
    label.setAlignmentX(LEFT_ALIGNMENT);
    inputPanel.add(label, verticalLayout ? BorderLayout.NORTH : BorderLayout.WEST);
  }

  public abstract void addGenericChangeListener(Runnable listener);
}
