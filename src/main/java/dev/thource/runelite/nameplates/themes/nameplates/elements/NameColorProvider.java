package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Player;

public class NameColorProvider extends ColorProvider {
  protected boolean dynamicColors;
  protected Color normalColor;
  protected Color teamColor;
  protected Color friendColor;
  protected Color friendsChatColor;
  protected Color clanColor;

  public NameColorProvider() {
    dynamicColors = true;
    normalColor = Color.WHITE;
    teamColor = new Color(17, 99, 223);
    friendColor = Color.GREEN;
    friendsChatColor = new Color(169, 0, 189);
    clanColor = new Color(252, 137, 1);
  }

  public NameColorProvider(Color normalColor) {
    super();

    this.dynamicColors = false;
    this.normalColor = normalColor;
  }

  public NameColorProvider(
      boolean dynamicColors,
      Color normalColor,
      Color teamColor,
      Color friendColor,
      Color friendsChatColor,
      Color clanColor) {
    this.dynamicColors = dynamicColors;
    this.normalColor = normalColor;
    this.teamColor = teamColor;
    this.friendColor = friendColor;
    this.friendsChatColor = friendsChatColor;
    this.clanColor = clanColor;
  }

  @Override
  public Color getColor(Nameplate nameplate) {
    var actor = nameplate.getActor();
    if (dynamicColors && actor instanceof Player) {
      var player = (Player) actor;
      if (player.isFriend()) {
        return friendColor;
      }

      var localPlayer = nameplate.getPlugin().getClient().getLocalPlayer();
      if (player != localPlayer) {
        if (player.isClanMember()) {
          return clanColor;
        }
        if (player.isFriendsChatMember()) {
          return friendsChatColor;
        }
        if (nameplate.isSameTeam()) {
          return teamColor;
        }
      }
    }

    return normalColor;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = new ArrayList<LabelledInput>();

    editInputs.add(
        new CheckboxInput("Dynamic coloring", dynamicColors, (value) -> dynamicColors = value));
    editInputs.add(new ColorInput("Normal", normalColor, (value) -> normalColor = value, plugin));
    editInputs.add(new ColorInput("Team", teamColor, (value) -> teamColor = value, plugin));
    editInputs.add(new ColorInput("Friend", friendColor, (value) -> friendColor = value, plugin));
    editInputs.add(
        new ColorInput(
            "Friends chat", friendsChatColor, (value) -> friendsChatColor = value, plugin));
    editInputs.add(new ColorInput("Clan", clanColor, (value) -> clanColor = value, plugin));

    return editInputs;
  }
}
