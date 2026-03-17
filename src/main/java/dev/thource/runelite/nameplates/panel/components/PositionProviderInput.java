package dev.thource.runelite.nameplates.panel.components;

import dev.thource.runelite.nameplates.themes.nameplates.OffsetAnchor;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.UnitFormatterFactory;
import net.runelite.client.util.ImageUtil;

public class PositionProviderInput extends LabelledInput {
  private static final int BUTTON_SIZE = 24;
  private static final ImageIcon HORIZONTAL_START_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-h-start.png"));
  private static final ImageIcon HORIZONTAL_MIDDLE_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-h-middle.png"));
  private static final ImageIcon HORIZONTAL_END_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-h-end.png"));
  private static final ImageIcon VERTICAL_START_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-v-start.png"));
  private static final ImageIcon VERTICAL_MIDDLE_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-v-middle.png"));
  private static final ImageIcon VERTICAL_END_ICON =
      new ImageIcon(ImageUtil.loadImageResource(PositionProviderInput.class, "align-v-end.png"));

  private final JPanel horizontalPanel;
  private final JButton startButton;
  private final JButton middleButton;
  private final JButton endButton;
  private final JSpinner input;
  private final PositionProvider positionProvider;

  public PositionProviderInput(String name, PositionProvider positionProvider, boolean isVertical) {
    super(name, true);
    this.positionProvider = positionProvider;

    horizontalPanel = new JPanel();
    horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
    horizontalPanel.add(Box.createHorizontalGlue());
    inputPanel.add(horizontalPanel, BorderLayout.CENTER);

    startButton =
        addButton(
            (isVertical ? "Top" : "Left") + " aligned",
            isVertical ? VERTICAL_START_ICON : HORIZONTAL_START_ICON,
            () -> setAnchor(OffsetAnchor.START));
    middleButton =
        addButton(
            "Middle aligned",
            isVertical ? VERTICAL_MIDDLE_ICON : HORIZONTAL_MIDDLE_ICON,
            () -> setAnchor(OffsetAnchor.MIDDLE));
    endButton =
        addButton(
            (isVertical ? "Bottom" : "Right") + " aligned",
            isVertical ? VERTICAL_END_ICON : HORIZONTAL_END_ICON,
            () -> setAnchor(OffsetAnchor.END));
    setAnchor(positionProvider.getAnchor());

    input = new JSpinner(new SpinnerNumberModel(positionProvider.getValue(), -999, 999, 1));
    input.setPreferredSize(new Dimension(80, 20));
    input.setMaximumSize(new Dimension(80, Integer.MAX_VALUE));
    var inputEditor = (JFormattedTextField) input.getEditor().getComponent(0);
    input.addChangeListener(e -> positionProvider.setValue((Integer) input.getValue()));
    inputEditor.setFormatterFactory(
        new UnitFormatterFactory(inputEditor.getFormatterFactory(), "px"));
    horizontalPanel.add(input);
  }

  private JButton addButton(String name, ImageIcon icon, Runnable onClick) {
    var totalPadding = (BUTTON_SIZE - icon.getIconHeight());
    var topPadding = totalPadding / 2;
    var bottomPadding = (int) Math.ceil(totalPadding / 2f);

    var button = new JButton();
    button.setIcon(icon);
    button.setToolTipText(name);
    button.setMinimumSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    button.setBorder(
        new CompoundBorder(
            new LineBorder(ColorScheme.BORDER_COLOR, 1),
            new EmptyBorder(topPadding, 0, bottomPadding, 0)));
    button.addActionListener(e -> onClick.run());
    horizontalPanel.add(button);

    return button;
  }

  public void setAnchor(OffsetAnchor anchor) {
    startButton.setEnabled(anchor != OffsetAnchor.START);
    middleButton.setEnabled(anchor != OffsetAnchor.MIDDLE);
    endButton.setEnabled(anchor != OffsetAnchor.END);

    positionProvider.setAnchor(anchor);
  }

  public void setValue(int value) {
    input.setValue(value);

    positionProvider.setValue(value);
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    input.addChangeListener(e -> listener.run());

    startButton.addActionListener(e -> listener.run());
    middleButton.addActionListener(e -> listener.run());
    endButton.addActionListener(e -> listener.run());
  }
}
