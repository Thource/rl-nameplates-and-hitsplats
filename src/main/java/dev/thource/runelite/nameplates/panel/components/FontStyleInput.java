package dev.thource.runelite.nameplates.panel.components;

import java.awt.Dimension;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

public class FontStyleInput extends LabelledInput {
  private static final int BUTTON_SIZE = 24;
  private static final ImageIcon BOLD_ICON =
      new ImageIcon(ImageUtil.loadImageResource(FontStyleInput.class, "bold.png"));
  private static final ImageIcon ITALIC_ICON =
      new ImageIcon(ImageUtil.loadImageResource(FontStyleInput.class, "italic.png"));

  private final JPanel horizontalPanel;
  private final JToggleButton boldButton;
  private final JToggleButton italicButton;
  private final Consumer<Integer> onChange;

  private boolean bold;
  private boolean italic;

  public FontStyleInput(String name, int value, Consumer<Integer> onChange) {
    super(name, false);
    this.onChange = onChange;

    horizontalPanel = new JPanel();
    horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
    horizontalPanel.add(Box.createHorizontalGlue());
    inputPanel.add(horizontalPanel);

    boldButton = addButton("Bold", BOLD_ICON, this::setBold, (value & 1) == 1);
    italicButton = addButton("Italic", ITALIC_ICON, this::setItalic, (value & 2) == 2);
  }

  private void setItalic(boolean val) {
    italic = val;
    onChange.accept(getValue());
  }

  private void setBold(boolean val) {
    bold = val;
    onChange.accept(getValue());
  }

  public int getValue() {
    var state = 0;

    if (bold) {
      state |= 1;
    }

    if (italic) {
      state |= 2;
    }

    return state;
  }

  private JToggleButton addButton(
      String name, ImageIcon icon, Consumer<Boolean> onClick, boolean selected) {
    var totalPadding = (BUTTON_SIZE - icon.getIconHeight());
    var topPadding = totalPadding / 2;
    var bottomPadding = (int) Math.ceil(totalPadding / 2f);

    var button = new JToggleButton();
    button.setIcon(icon);
    button.setToolTipText(name);
    button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    button.setBorder(
        new CompoundBorder(
            new LineBorder(ColorScheme.BORDER_COLOR, 1),
            new EmptyBorder(topPadding, 0, bottomPadding, 0)));
    button.setSelected(selected);
    button.addActionListener(e -> onClick.accept(button.isSelected()));
    horizontalPanel.add(button);

    return button;
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    boldButton.addActionListener(e -> listener.run());
    italicButton.addActionListener(e -> listener.run());
  }
}
