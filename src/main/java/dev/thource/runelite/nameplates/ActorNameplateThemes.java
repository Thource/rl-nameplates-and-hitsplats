package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.FlatDarkTheme;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.config.ConfigManager;

public class ActorNameplateThemes {
  @Getter private final ActorType actorType;
  @Getter @Setter private NameplateTheme inCombatTheme;
  @Getter @Setter private NameplateTheme outOfCombatTheme;

  public ActorNameplateThemes(
      ConfigManager configManager,
      Map<String, NameplateTheme> nameplateThemes,
      ActorType actorType) {
    this.actorType = actorType;

    var inCombatThemeId =
        Objects.requireNonNullElse(
            configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, getInCombatConfigId()),
            FlatDarkTheme.ID);
    inCombatTheme =
        nameplateThemes.getOrDefault(inCombatThemeId, nameplateThemes.get(FlatDarkTheme.ID));

    var outOfCombatThemeId =
        Objects.requireNonNullElse(
            configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, getOutOfCombatConfigId()),
            FlatDarkTheme.ID);
    outOfCombatTheme =
        nameplateThemes.getOrDefault(outOfCombatThemeId, nameplateThemes.get(FlatDarkTheme.ID));
  }

  public String getInCombatConfigId() {
    return "activeNameplateThemeFor" + actorType.getKey() + "InCombatId";
  }

  public String getOutOfCombatConfigId() {
    return "activeNameplateThemeFor" + actorType.getKey() + "OutOfCombatId";
  }

  public void onThemeAdded(NameplateTheme theme) {
    if (theme.getId().equals(inCombatTheme.getId())) {
      inCombatTheme = theme;
    }

    if (theme.getId().equals(outOfCombatTheme.getId())) {
      outOfCombatTheme = theme;
    }
  }
}
