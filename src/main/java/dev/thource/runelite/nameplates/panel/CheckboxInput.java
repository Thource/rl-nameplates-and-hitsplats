package dev.thource.runelite.nameplates.panel;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JCheckBox;

public class CheckboxInput extends LabelledInput {
  private final JCheckBox input;

  public CheckboxInput(String name, boolean selected, Consumer<Boolean> onChange) {
    super(name);

    input = new JCheckBox();
    input.setSelected(selected);
    input.addChangeListener(e -> onChange.accept(input.isSelected()));
    inputPanel.add(input, BorderLayout.EAST);
  }

  public void setValue(boolean value) {
    input.setSelected(value);
  }
}
