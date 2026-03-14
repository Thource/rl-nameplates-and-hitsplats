package dev.thource.runelite.nameplates.panel.components;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** A reusable panel for switching between N tabs using buttons. */
public class TabSwitcherPanel extends JPanel {
  private final List<JButton> tabButtons = new ArrayList<>();
  private final List<JPanel> tabs;
  private int selectedIndex = 0;

  public TabSwitcherPanel(List<String> tabNames, List<JPanel> tabs) {
    super();
    this.tabs = tabs;
    setLayout(new GridLayout(1, tabNames.size()));
    setBorder(new EmptyBorder(8, 0, 0, 0));

    ActionListener listener =
        e -> {
          for (int i = 0; i < tabButtons.size(); i++) {
            if (e.getSource() == tabButtons.get(i)) {
              setSelectedIndex(i);
              break;
            }
          }
        };

    for (String name : tabNames) {
      JButton button = new JButton(name);
      button.addActionListener(listener);
      tabButtons.add(button);
      add(button);
    }
    updateButtonStates();
  }

  public void setSelectedIndex(int index) {
    if (index < 0 || index >= tabButtons.size() || index == selectedIndex) return;
    selectedIndex = index;
    updateButtonStates();

    for (int i = 0; i < tabs.size(); i++) {
      tabs.get(i).setVisible(i == selectedIndex);
    }
  }

  private void updateButtonStates() {
    for (int i = 0; i < tabButtons.size(); i++) {
      tabButtons.get(i).setEnabled(i != selectedIndex);
    }
  }
}
