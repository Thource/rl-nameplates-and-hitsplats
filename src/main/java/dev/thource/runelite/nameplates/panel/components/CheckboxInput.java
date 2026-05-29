package dev.thource.runelite.nameplates.panel.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.function.Consumer;
import javax.swing.JCheckBox;
import lombok.Getter;

public class CheckboxInput extends LabelledInput {
  @Getter private final JCheckBox input;

  public CheckboxInput(String name, boolean selected, Consumer<Boolean> onChange) {
    super(name);

    input = new JCheckBox();
    input.setSelected(selected);
    input.setFocusable(false);
    input.addChangeListener(e -> onChange.accept(input.isSelected()));

    label.setPreferredSize(new Dimension(192, label.getPreferredSize().height));

    inputPanel.setLayout(new BorderLayout());
    inputPanel.add(label, BorderLayout.WEST);
    inputPanel.add(input, BorderLayout.EAST);
  }

  public void setValue(boolean value) {
    input.setSelected(value);
  }

  public boolean getValue() {
    return input.isSelected();
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    input.addChangeListener(e -> listener.run());
  }
}
