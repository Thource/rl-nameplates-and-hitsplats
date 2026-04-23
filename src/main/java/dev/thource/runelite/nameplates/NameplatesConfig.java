package dev.thource.runelite.nameplates;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/** ImprovedContextMenuConfig manages the config for the plugin. */
@SuppressWarnings("SameReturnValue")
@ConfigGroup("nameplates")
public interface NameplatesConfig extends Config {

  // TODO list
  // =========
  // add options for when nameplates should show up
  // add another theme (possibly a minimal one)
  // add combat level to name only draws

  // RL dependency list
  // NPC.getOverheadIcon() - required so that overheads can be drawn on NPC nameplates
  // Player.getSkullIcon() for non-local players (including blue Forinthry Surge buff skull)
  // Overriding of overhead icons and skull icons

  // draw nameplates for players
  // draw nameplates for npcs
  // draw nameplates on hover
  // draw nameplates for npcs in combat
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)

  // show full nameplate when hovering "attack"

  String CONFIG_GROUP = "nameplates";

  @ConfigItem(
      keyName = "activeNameplateThemeId",
      name = "Active nameplate theme ID",
      description = "The ID of the active nameplate theme.",
      hidden = true)
  default String activeNameplateThemeId() {
    return "flatDarkTheme";
  }

  @ConfigItem(
      keyName = "npcNameplateDisplayMode",
      name = "NPC nameplate display behaviour",
      description = "Defines when NPC nameplates will be visible.")
  default NameplateDisplayMode npcNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "playerNameplateDisplayMode",
      name = "Player nameplate display behaviour",
      description = "Defines when player nameplates will be visible.")
  default NameplateDisplayMode playerNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "ownNameplateDisplayMode",
      name = "Own nameplate display behaviour",
      description = "Defines when local player nameplate will be visible.")
  default NameplateDisplayMode ownNameplateDisplayMode() {
    return NameplateDisplayMode.ALWAYS;
  }

  @ConfigItem(
      keyName = "alwaysDrawNPCNames",
      name = "Always draw NPC names",
      description =
          "Still draw NPC names when nameplate display behaviour is set to hide the nameplates.")
  default boolean alwaysDrawNPCNames() {
    return true;
  }

  @ConfigItem(
      keyName = "alwaysDrawPlayerNames",
      name = "Always draw player names",
      description =
          "Still draw player names when nameplate display behaviour is set to hide the nameplates.")
  default boolean alwaysDrawPlayerNames() {
    return true;
  }

  @ConfigItem(
      keyName = "alwaysDrawOwnName",
      name = "Always draw own name",
      description =
          "Still draw own name when nameplate display behaviour is set to hide the nameplate.")
  default boolean alwaysDrawOwnName() {
    return false;
  }

  @ConfigItem(
      keyName = "enableNoLootOtherGIMSplats",
      name = "[GIM] Enable no-loot indicator for other hitsplats",
      description =
          "If enabled, NPCs will be tagged as no-loot if a tinted damage hitsplat appears<br>on"
              + " them.<br><br>Disable this if you frequently PVM with your group members"
              + " and the false<br>positive no-loot indicators are bothering you.")
  default boolean enableNoLootOtherGIMSplats() {
    return true;
  }

  @ConfigItem(
      keyName = "hoverIndicatorMode",
      name = "Hover indicator mode",
      description =
          "Sets when the hover indicator should show up.<br><br>Always: Always show while hovering"
              + " any NPC/player<br>Right-click: Only show when hovering entries in the right"
              + " click menu<br>Busy: Only show when multiple NPC/players are present in the"
              + " right-click menu<br>Busy right-click: Combines Busy and Right-click mode")
  default HoverIndicatorMode hoverIndicatorMode() {
    return HoverIndicatorMode.BUSY_RIGHT_CLICK;
  }
}
