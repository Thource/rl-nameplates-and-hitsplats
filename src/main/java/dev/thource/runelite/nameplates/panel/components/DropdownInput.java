package dev.thource.runelite.nameplates.panel.components;

import dev.thource.runelite.nameplates.panel.BoundsPopupMenuListener;
import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

public class DropdownInput<T extends Nameable> extends LabelledInput {
  private final JComboBox<T> input;

  public DropdownInput(String name, T defaultValue, T[] values, Consumer<T> onChange) {
    super(name);

    var model = new DefaultComboBoxModel<>(values);
    input = new JComboBox<>(model);
    //        noinspection unchecked
    input.addActionListener(e -> onChange.accept((T) input.getSelectedItem()));
    input.setSelectedItem(defaultValue);
    input.setRenderer(
        new DefaultListCellRenderer() {

          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            //        noinspection unchecked
            return super.getListCellRendererComponent(
                list, ((T) value).getName(), index, isSelected, cellHasFocus);
          }
        });
    BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true, false);
    input.addPopupMenuListener(listener);
    inputPanel.add(input, BorderLayout.EAST);
  }
}
