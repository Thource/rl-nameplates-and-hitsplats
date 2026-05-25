package dev.thource.runelite.nameplates;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

/** NameplatesConfig manages the config for the plugin. */
@SuppressWarnings("SameReturnValue")
@ConfigGroup("nameplates")
public interface NameplatesConfig extends Config {

  // TODO list
  // =========
  // draw nameplates on hover
  // draw nameplates for npcs targetting you
  // draw nameplates for npcs that recently targetted you
  // nameplate draw range (excluding hover)

  // show full nameplate when hovering "attack"

  String CONFIG_GROUP = "nameplates";

  @ConfigSection(name = "Nameplates", description = "Settings related to nameplates", position = 1)
  String NAMEPLATES_SECTION = "Nameplates";

  @ConfigSection(name = "Hitsplats", description = "Settings related to hitsplats", position = 2)
  String HITSPLATS_SECTION = "Hitsplats";

  @ConfigItem(
      keyName = "activeNameplateThemeForSelfId",
      name = "Active nameplate theme for self ID",
      description = "The ID of the active nameplate theme for self.",
      hidden = true)
  default String activeNameplateThemeForSelfId() {
    return "flatDarkTheme";
  }

  @ConfigItem(
      keyName = "activeNameplateThemeForPartyId",
      name = "Active nameplate theme for party ID",
      description = "The ID of the active nameplate theme for party members.",
      hidden = true)
  default String activeNameplateThemeForPartyId() {
    return "flatDarkTheme";
  }

  @ConfigItem(
      keyName = "activeNameplateThemeForPlayersId",
      name = "Active nameplate theme for players ID",
      description = "The ID of the active nameplate theme for players.",
      hidden = true)
  default String activeNameplateThemeForPlayersId() {
    return "flatDarkTheme";
  }

  @ConfigItem(
      keyName = "activeNameplateThemeForNPCsId",
      name = "Active nameplate theme for NPCs ID",
      description = "The ID of the active nameplate theme for NPCs.",
      hidden = true)
  default String activeNameplateThemeForNPCsId() {
    return "flatDarkTheme";
  }

  @ConfigItem(
      keyName = "activeHitsplatThemeId",
      name = "Active hitsplat theme ID",
      description = "The ID of the active hitsplat theme.",
      hidden = true)
  default String activeHitsplatThemeId() {
    return "osrsTheme";
  }

  @ConfigItem(
      keyName = "alwaysDrawOwnName",
      name = "Always draw own name",
      description =
          "Still draw own name when nameplate display behaviour is set to hide the nameplate.",
      position = 1,
      section = NAMEPLATES_SECTION)
  default boolean alwaysDrawOwnName() {
    return false;
  }

  @ConfigItem(
      keyName = "ownNameplateDisplayMode",
      name = "Own nameplate display behaviour",
      description = "Defines when local player nameplate will be visible.",
      position = 1,
      section = NAMEPLATES_SECTION)
  default NameplateDisplayMode ownNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "alwaysDrawPartyNames",
      name = "Always draw party member names",
      description =
          "Still draw party member names when nameplate display behaviour is set to hide the"
              + " nameplate.",
      position = 2,
      section = NAMEPLATES_SECTION)
  default boolean alwaysDrawPartyNames() {
    return false;
  }

  @ConfigItem(
      keyName = "partyNameplateDisplayMode",
      name = "Party member nameplate display behaviour",
      description = "Defines when party member nameplates will be visible.",
      position = 2,
      section = NAMEPLATES_SECTION)
  default NameplateDisplayMode partyNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "alwaysDrawPlayerNames",
      name = "Always draw player names",
      description =
          "Still draw player names when nameplate display behaviour is set to hide the nameplates.",
      position = 3,
      section = NAMEPLATES_SECTION)
  default boolean alwaysDrawPlayerNames() {
    return true;
  }

  @ConfigItem(
      keyName = "playerNameplateDisplayMode",
      name = "Player nameplate display behaviour",
      description = "Defines when player nameplates will be visible.",
      position = 3,
      section = NAMEPLATES_SECTION)
  default NameplateDisplayMode playerNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "alwaysDrawNPCNames",
      name = "Always draw NPC names",
      description =
          "Still draw NPC names when nameplate display behaviour is set to hide the nameplates.",
      position = 4,
      section = NAMEPLATES_SECTION)
  default boolean alwaysDrawNPCNames() {
    return true;
  }

  @ConfigItem(
      keyName = "npcNameplateDisplayMode",
      name = "NPC nameplate display behaviour",
      description = "Defines when NPC nameplates will be visible.",
      position = 4,
      section = NAMEPLATES_SECTION)
  default NameplateDisplayMode npcNameplateDisplayMode() {
    return NameplateDisplayMode.IN_COMBAT;
  }

  @ConfigItem(
      keyName = "npcIdsWhitelist",
      name = "NPC IDs whitelist",
      description =
          "ALWAYS draw nameplates or names for NPCs whose IDs are in this comma-separated list.",
      position = 5,
      section = NAMEPLATES_SECTION)
  default String npcIdsWhitelist() {
    return "";
  }

  @ConfigItem(
      keyName = "npcNamesWhitelist",
      name = "NPC names whitelist",
      description =
          "ALWAYS draw nameplates or names for NPCs whose names are in this comma-separated list."
              + " Case-insensitive.",
      position = 5,
      section = NAMEPLATES_SECTION)
  default String npcNamesWhitelist() {
    return "";
  }

  @ConfigItem(
      keyName = "npcIdsBlacklist",
      name = "NPC IDs blacklist",
      description =
          "NEVER draw nameplates or names for NPCs whose IDs are in this comma-separated list.",
      position = 6,
      section = NAMEPLATES_SECTION)
  default String npcIdsBlacklist() {
    return "";
  }

  @ConfigItem(
      keyName = "npcNamesBlacklist",
      name = "NPC names blacklist",
      description =
          "NEVER draw nameplates or names for NPCs whose names are in this comma-separated list."
              + " Case-insensitive.",
      position = 6,
      section = NAMEPLATES_SECTION)
  default String npcNamesBlacklist() {
    return "";
  }

  @ConfigItem(
      keyName = "hoverIndicatorMode",
      name = "Hover indicator mode",
      description =
          "Sets when the hover indicator should show up.<br><br>Always: Always show while hovering"
              + " any NPC/player<br>Right-click: Only show when hovering entries in the right"
              + " click menu<br>Busy: Only show when multiple NPC/players are present in the"
              + " right-click menu<br>Busy right-click: Combines Busy and Right-click mode",
      position = 7,
      section = NAMEPLATES_SECTION)
  default HoverIndicatorMode hoverIndicatorMode() {
    return HoverIndicatorMode.BUSY_RIGHT_CLICK;
  }

  @ConfigItem(
      keyName = "enableNoLootOtherGIMSplats",
      name = "[GIM] Enable no-loot indicator for other hitsplats",
      description =
          "If enabled, NPCs will be tagged as no-loot if a tinted damage hitsplat appears<br>on"
              + " them.<br><br>Disable this if you frequently PVM with your group members"
              + " and the false<br>positive no-loot indicators are bothering you.",
      position = 10,
      section = NAMEPLATES_SECTION)
  default boolean enableNoLootOtherGIMSplats() {
    return true;
  }

  @ConfigItem(
      keyName = "combineHitsplats",
      name = "Combine same type hitsplats",
      description =
          "Whether to combine hitsplats of the same type into one hitsplat with combined"
              + " value.<br><br>Example: If an NPC takes 2 separate hits of 4 damage, instead of"
              + " showing 2 hitsplats of 4 damage, it will show 1 hitsplat of 8 damage.",
      section = HITSPLATS_SECTION)
  default boolean combineHitsplats() {
    return false;
  }

  @ConfigItem(
      keyName = "hideZeroHitsplats",
      name = "Hide zero hitsplats",
      description = "Whether to hide hitsplats that have a value of 0 (like misses).",
      section = HITSPLATS_SECTION)
  default boolean hideZeroHitsplats() {
    return false;
  }

  @Range(min = 100, max = 60000)
  @ConfigItem(
      keyName = "hitsplatLifetime",
      name = "Hitsplat lifetime (ms)",
      description = "How long hitsplats should remain visible for.",
      section = HITSPLATS_SECTION)
  default int hitsplatLifetime() {
    return 1000;
  }
}
