package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.Nameplate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameplateTextProvider {
  private NameplateTextType textType;
  private String text;

  public NameplateTextProvider(NameplateTextType textType) {
    this.textType = textType;
  }

  public String get(Nameplate nameplate) {
    switch (textType) {
      case HEALTH:
        return nameplate.getHealthString();
      case PRAYER_POINTS:
        return nameplate.getPrayerString();
      case COMBAT_LEVEL:
        if (nameplate.getCombatLevel() == 0) {
          return null;
        }

        return String.valueOf(nameplate.getCombatLevel());
      case NAME:
        return nameplate.getName();
    }

    return text;
  }
}
