package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.List;

public abstract class ColorProvider {
  public abstract Color getColor(Nameplate nameplate);

  public abstract List<LabelledInput> getEditInputs(NameplatesPlugin plugin);
}
