package dev.thource.runelite.nameplates.panel.components;

import dev.thource.runelite.nameplates.panel.EnhancedSwingUtilities;
import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;

public class ListSelector<T extends Nameable> extends LabelledInput {
  private static final int BUTTON_SIZE = 24;

  private final JList<T> list;
  private final JPanel buttonContainer;

  public ListSelector(String name, T defaultValue, List<T> values) {
    super(name, true);

    @SuppressWarnings("unchecked")
    var array = (T[]) values.toArray(new Nameable[0]);
    list = new JList<>(array);
    list.setSelectedValue(defaultValue, true);
    list.setCellRenderer(
        new DefaultListCellRenderer() {

          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            //        noinspection unchecked
            return super.getListCellRendererComponent(
                list, ((T) value).getName(), index, isSelected, cellHasFocus);
          }
        });
    list.setSelectionBackground(ColorScheme.MEDIUM_GRAY_COLOR);

    var scrollPane = new JScrollPane(list);
    scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 108));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
    inputPanel.add(scrollPane, BorderLayout.CENTER);

    buttonContainer = new JPanel();
    buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
    buttonContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
    inputPanel.add(buttonContainer, BorderLayout.SOUTH);
  }

  public void setValues(List<T> values) {
    var selected = list.getSelectedValue();

    @SuppressWarnings("unchecked")
    var array = (T[]) values.toArray(new Nameable[0]);
    list.setListData(array);

    list.setSelectedValue(selected, true);
  }

  public void addChangeListener(Consumer<T> onChange) {
    list.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            onChange.accept(list.getSelectedValue());
          }
        });

    onChange.accept(list.getSelectedValue());
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    addChangeListener((v) -> listener.run());
  }

  public void clearButtons() {
    EnhancedSwingUtilities.fastRemoveAll(buttonContainer);
  }

  public void addButton(String name, ImageIcon icon, Runnable onClick) {
    var totalPadding = (BUTTON_SIZE - icon.getIconHeight());
    var topPadding = totalPadding / 2;
    var bottomPadding = (int) Math.ceil(totalPadding / 2f);

    var button = new JButton();
    button.setIcon(icon);
    button.setToolTipText(name);
    button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
    button.setBorder(
        new CompoundBorder(
            new LineBorder(ColorScheme.BORDER_COLOR, 1),
            new EmptyBorder(topPadding, 0, bottomPadding, 0)));
    button.addActionListener(e -> onClick.run());
    buttonContainer.add(button);
  }

  public void addButtonGlue() {
    buttonContainer.add(Box.createHorizontalGlue());
  }

  public void selectValue(T value) {
    list.setSelectedValue(value, true);
  }

  public T getSelectedValue() {
    return list.getSelectedValue();
  }
}
