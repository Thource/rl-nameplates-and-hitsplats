package dev.thource.runelite.nameplates.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class StringInput extends LabelledInput {
    private final JTextField input;

    public StringInput(String name, String defaultValue, Consumer<String> onChange) {
        super(name, true);

        input = new JTextField(defaultValue);
        input.getDocument().addDocumentListener(new DocumentListener() {
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
}
