package dev.thource.runelite.nameplates.panel.components;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StringInput extends LabelledInput {
  private final JTextField input;

  public StringInput(String name, String defaultValue, Consumer<String> onChange) {
    super(name, true);

    input = new JTextField(defaultValue);
    input
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                onChange.accept(input.getText());
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                onChange.accept(input.getText());
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                onChange.accept(input.getText());
              }
            });
    inputPanel.add(input, BorderLayout.CENTER);
  }

  public void setValue(String value) {
    input.setText(value);
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    input
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                listener.run();
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                listener.run();
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                listener.run();
              }
            });
  }
}
