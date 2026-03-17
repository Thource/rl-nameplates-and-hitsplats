package dev.thource.runelite.nameplates.panel.components;

import dev.thource.runelite.nameplates.NameplatesPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;
import net.runelite.api.Client;
import net.runelite.client.ui.components.ColorJButton;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.util.ColorUtil;

public class ColorInput extends LabelledInput {
  private final ColorJButton colorPickerBtn;
  private final boolean alphaHidden;
  private Runnable genericChangeListener;

  public ColorInput(String label, Color color, Consumer<Color> setter, NameplatesPlugin plugin) {
    this(label, color, false, setter, plugin.getColorPickerManager(), plugin.getClient());
  }

  public ColorInput(
      String name,
      Color color,
      boolean alphaHidden,
      Consumer<Color> onChange,
      ColorPickerManager colorPickerManager,
      Client client) {
    super(name);

    this.alphaHidden = alphaHidden;

    colorPickerBtn =
        new ColorJButton(
            "#"
                + (alphaHidden
                        ? ColorUtil.colorToHexCode(color)
                        : ColorUtil.colorToAlphaHexCode(color))
                    .toUpperCase(),
            color);
    colorPickerBtn.setFocusable(false);
    colorPickerBtn.addActionListener(
        e -> {
          RuneliteColorPicker colorPicker =
              colorPickerManager.create(client, colorPickerBtn.getColor(), name, alphaHidden);
          colorPicker.setLocationRelativeTo(colorPickerBtn);
          colorPicker.setOnColorChange(
              c -> {
                colorPickerBtn.setColor(c);
                colorPickerBtn.setText(
                    "#"
                        + (alphaHidden
                                ? ColorUtil.colorToHexCode(c)
                                : ColorUtil.colorToAlphaHexCode(c))
                            .toUpperCase());
                onChange.accept(c);

                if (genericChangeListener != null) {
                  genericChangeListener.run();
                }
              });
          colorPicker.setOnClose(onChange);
          colorPicker.setVisible(true);
        });
    inputPanel.add(colorPickerBtn, BorderLayout.EAST);
  }

  public void setValue(Color value) {
    colorPickerBtn.setColor(value);
    colorPickerBtn.setText(
        "#"
            + (alphaHidden ? ColorUtil.colorToHexCode(value) : ColorUtil.colorToAlphaHexCode(value))
                .toUpperCase());
  }

  @Override
  public void addGenericChangeListener(Runnable listener) {
    genericChangeListener = listener;
  }
}
