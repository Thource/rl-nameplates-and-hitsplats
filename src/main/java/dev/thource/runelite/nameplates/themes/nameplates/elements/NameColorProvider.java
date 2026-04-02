package dev.thource.runelite.nameplates.themes.nameplates.elements;

import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.NameplatesPlugin;
import dev.thource.runelite.nameplates.PlayerNameplate;
import dev.thource.runelite.nameplates.panel.components.CheckboxInput;
import dev.thource.runelite.nameplates.panel.components.ColorInput;
import dev.thource.runelite.nameplates.panel.components.LabelledInput;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Setter;
import net.runelite.api.Player;

@Setter(AccessLevel.PRIVATE)
public class NameColorProvider extends ColorProvider {
  protected boolean partyColors;
  protected boolean dynamicColors;
  protected Color normalColor;
  protected Color teamColor;
  protected Color friendColor;
  protected Color friendsChatColor;
  protected Color clanColor;

  public NameColorProvider() {
    partyColors = true;
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

  @Override
  public Color getColor(Nameplate nameplate) {
    var actor = nameplate.getActor();

    if (actor instanceof Player) {
      if (partyColors) {
        var partyData = nameplate.getPartyData();

        if (partyData != null) {
          return partyData.getColor();
        }
      }

      if (dynamicColors) {
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
    }

    return normalColor;
  }

  @Override
  public List<LabelledInput> getEditInputs(NameplatesPlugin plugin) {
    var editInputs = new ArrayList<LabelledInput>();

    editInputs.add(new CheckboxInput("Party colors", partyColors, this::setPartyColors));
    editInputs.add(new CheckboxInput("Dynamic colors", dynamicColors, this::setDynamicColors));
    editInputs.add(new ColorInput("Normal", normalColor, this::setNormalColor, plugin));
    editInputs.add(new ColorInput("Team", teamColor, this::setTeamColor, plugin));
    editInputs.add(new ColorInput("Friend", friendColor, this::setFriendColor, plugin));
    editInputs.add(
        new ColorInput("Friends chat", friendsChatColor, this::setFriendsChatColor, plugin));
    editInputs.add(new ColorInput("Clan", clanColor, this::setClanColor, plugin));

    return editInputs;
  }
}
