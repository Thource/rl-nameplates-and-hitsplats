package dev.thource.runelite.nameplates.panel;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.runelite.client.ui.UnitFormatterFactory;

public class IntInput extends LabelledInput {
  private final JSpinner input;
  private String unit = null;

  public IntInput(
      String name, int defaultValue, int minValue, int maxValue, Consumer<Integer> onChange) {
    super(name);

    var model = new SpinnerNumberModel(defaultValue, minValue, maxValue, 1);
    input = new JSpinner(model);
    JFormattedTextField inputEditor = (JFormattedTextField) input.getEditor().getComponent(0);
    input.addChangeListener(e -> onChange.accept((Integer) input.getValue()));
    if (unit != null) {
      inputEditor.setFormatterFactory(
          new UnitFormatterFactory(inputEditor.getFormatterFactory(), unit));
    }
    inputPanel.add(input, BorderLayout.EAST);
  }

  public void setValue(int value) {
    input.setValue(value);
  }
}
