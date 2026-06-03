package dev.thource.runelite.nameplates;

import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ParamID;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;

@Getter
public class NPCNameplate extends Nameplate {
  private static final List<Integer> BOAT_IDS =
      List.of(
          NpcID.BOAT_HP_NPC_TINY,
          NpcID.BOAT_HP_NPC_SMALL,
          NpcID.BOAT_HP_NPC_MEDIUM,
          NpcID.BOAT_HP_NPC_LARGE,
          NpcID.BOAT_HP_NPC_COLOSSAL);

  private static final HashSet<Integer> BOSS_IDS =
      new HashSet<>(
          List.of(
              // Last update: https://oldschool.runescape.wiki/w/Boss?oldid=15210418
              // World bosses
              NpcID.BARROWS_AHRIM,
              NpcID.BARROWS_KARIL,
              NpcID.BARROWS_DHAROK,
              NpcID.BARROWS_GUTHAN,
              NpcID.BARROWS_TORAG,
              NpcID.BARROWS_VERAC,
              NpcID.GEMSTONE_CRAB,
              NpcID.RAT_BOSS_NORMAL,
              NpcID.RAT_BOSS_INSTANCE,
              NpcID.MOLE_GIANT,
              NpcID.FOSSIL_CRAZY_ARCHAEOLOGIST,
              NpcID.DAGCAVE_RANGED_BOSS,
              NpcID.DAGCAVE_MELEE_BOSS,
              NpcID.DAGCAVE_MAGIC_BOSS,
              NpcID.SARACHNIS,
              NpcID.PMOON_BOSS_BLOOD_MOON_VIS,
              NpcID.PMOON_BOSS_BLUE_MOON_VIS,
              NpcID.PMOON_BOSS_ECLIPSE_MOON_VIS,
              NpcID.KALPHITE_QUEEN,
              NpcID.GODWARS_ARMADYL_AVATAR,
              NpcID.GODWARS_SARADOMIN_AVATAR,
              NpcID.GODWARS_BANDOS_AVATAR,
              NpcID.GODWARS_ZAMORAK_AVATAR,
              NpcID.HUEY_HEAD,
              NpcID.HUEY_HEAD_ENRAGED,
              NpcID.CORP_BEAST,
              NpcID.NEX,
              NpcID.NEX_SPAWNING,
              NpcID.NEX_SOULSPLIT,
              NpcID.NEX_DEFLECT,
              NpcID.NEX_DYING,
              // Wilderness bosses
              NpcID.CHAOS_FANATIC,
              NpcID.CRAZY_ARCHAEOLOGIST,
              NpcID.SCORPIA,
              NpcID.KING_DRAGON,
              NpcID.CHAOSELEMENTAL,
              NpcID.WILD_CAVE_SUPERIOR,
              NpcID.VETION_SINGLE,
              NpcID.VETION,
              NpcID.VENENATIS_SINGLES,
              NpcID.VENENATIS,
              NpcID.CALLISTO_SINGLES,
              NpcID.CALLISTO,
              // Instanced bosses
              NpcID.COWBOSS,
              NpcID.COWBOSS_ROUTEFIND,
              NpcID.COWBOSS_HARDMODE,
              NpcID.HILLGIANT_BOSS,
              NpcID.GB_MOSSGIANT,
              NpcID.AMOXLIATL,
              NpcID.RT_ICE_KING,
              NpcID.RT_FIRE_QUEEN,
              NpcID.DOM_BOSS,
              NpcID.DOM_BOSS_SHIELDED,
              NpcID.DOM_BOSS_BURROWED,
              NpcID.SNAKEBOSS_BOSS_MAGIC,
              NpcID.SNAKEBOSS_BOSS_RANGED,
              NpcID.SNAKEBOSS_BOSS_MELEE,
              NpcID.VORKATH,
              NpcID.MUSPAH,
              NpcID.NIGHTMARE_PHASE_01,
              NpcID.NIGHTMARE_PHASE_02,
              NpcID.NIGHTMARE_PHASE_03,
              NpcID.NIGHTMARE_WEAK_PHASE_01,
              NpcID.NIGHTMARE_WEAK_PHASE_02,
              NpcID.NIGHTMARE_WEAK_PHASE_03,
              NpcID.NIGHTMARE_WEAK_PHASE_03,
              NpcID.NIGHTMARE_BLAST,
              NpcID.NIGHTMARE_CHALLENGE_PHASE_01,
              NpcID.NIGHTMARE_CHALLENGE_PHASE_02,
              NpcID.NIGHTMARE_CHALLENGE_PHASE_03,
              NpcID.NIGHTMARE_CHALLENGE_PHASE_04,
              NpcID.NIGHTMARE_CHALLENGE_PHASE_05,
              NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_01,
              NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_02,
              NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_03,
              NpcID.NIGHTMARE_CHALLENGE_WEAK_PHASE_04,
              NpcID.NIGHTMARE_CHALLENGE_BLAST,
              NpcID.YAMA,
              // The Forgotten Four
              NpcID.DUKE_SUCELLUS_AWAKE,
              NpcID.LEVIATHAN,
              NpcID.WHISPERER,
              NpcID.WHISPERER_MELEE,
              NpcID.VARDORVIS,
              NpcID.VARDORVIS_BASE_POSTQUEST,
              // Sporadic bosses
              NpcID.TRAIL_MIMIC_COMBAT,
              NpcID.HESPORI,
              NpcID.CATA_BOSS,
              // Slayer bosses
              NpcID.GRYPHON_BOSS,
              NpcID.GARGBOSS_DAWN_PHASE1,
              NpcID.GARGBOSS_DAWN_PHASE1_TRANSITION,
              NpcID.GARGBOSS_DAWN_PHASE3,
              NpcID.GARGBOSS_DUSK_PHASE1_DEFENSIVE,
              NpcID.GARGBOSS_DUSK_PHASE1_TRANSITION,
              NpcID.GARGBOSS_DUSK_PHASE1_FLYTRANSITION,
              NpcID.GARGBOSS_DUSK_PHASE2_ATTACKING,
              NpcID.GARGBOSS_DUSK_PHASE3_DEFENSIVE,
              NpcID.GARGBOSS_DUSK_PHASE3_TRANSITION,
              NpcID.GARGBOSS_DUSK_PHASE4_SPAWN,
              NpcID.GARGBOSS_DUSK_PHASE4,
              NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING,
              NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE,
              NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED,
              NpcID.ABYSSALSIRE_SIRE_PUPPET,
              NpcID.ABYSSALSIRE_SIRE_WANDERING,
              NpcID.ABYSSALSIRE_SIRE_PANICKING,
              NpcID.ABYSSALSIRE_SIRE_APOCALYPSE,
              NpcID.SLAYER_KRAKEN_BOSS,
              NpcID.CERBERUS_ATTACKING,
              NpcID.CERBERUS_SITTING,
              NpcID.CERBERUS_RESETTING,
              NpcID.ARAXXOR,
              NpcID.SMOKE_DEVIL_BOSS,
              NpcID.HYDRABOSS,
              NpcID.HYDRABOSS_P1_TRANSITION,
              NpcID.HYDRABOSS_4,
              NpcID.HYDRABOSS_P2_TRANSITION,
              NpcID.HYDRABOSS_3,
              NpcID.HYDRABOSS_P3_TRANSITION,
              NpcID.HYDRABOSS_2,
              // Minigame bosses
              NpcID.CRYSTAL_HUNLLEF_MELEE,
              NpcID.CRYSTAL_HUNLLEF_RANGED,
              NpcID.CRYSTAL_HUNLLEF_MAGIC,
              NpcID.CRYSTAL_HUNLLEF_MELEE_HM,
              NpcID.CRYSTAL_HUNLLEF_RANGED_HM,
              NpcID.CRYSTAL_HUNLLEF_MAGIC_HM,
              NpcID.TZHAAR_FIGHTCAVE_SWARM_BOSS,
              NpcID.COLOSSEUM_SOL_P1,
              // Skilling bosses
              NpcID.TEMPOROSS_BOSS_READY,
              NpcID.TEMPOROSS_BOSS_ENRAGED,
              NpcID.ZALCANO,
              NpcID.ZALCANO_WEAK,
              // Raids
              // Chambers of Xeric
              NpcID.RAIDS_TEKTON_WAITING,
              NpcID.RAIDS_TEKTON_WALKING_STANDARD,
              NpcID.RAIDS_TEKTON_FIGHTING_STANDARD,
              NpcID.RAIDS_TEKTON_WALKING_ENRAGED,
              NpcID.RAIDS_TEKTON_HAMMERING,
              NpcID.RAIDS_TEKTON_FIGHTING_ENRAGED,
              NpcID.RAIDS_VANGUARD_DORMANT,
              NpcID.RAIDS_VANGUARD_WALKING,
              NpcID.RAIDS_VANGUARD_MELEE,
              NpcID.RAIDS_VANGUARD_RANGED,
              NpcID.RAIDS_VANGUARD_MAGIC,
              NpcID.RAIDS_VESPULA_FLYING,
              NpcID.RAIDS_VESPULA_ENRAGED,
              NpcID.RAIDS_VESPULA_WALKING,
              NpcID.RAIDS_VASANISTIRIO_WALKING,
              NpcID.RAIDS_VASANISTIRIO_HEALING,
              NpcID.RAIDS_DOGODILE_JUNIOR,
              NpcID.RAIDS_DOGODILE,
              NpcID.RAIDS_DOGODILE_SUBMERGED,
              NpcID.OLM_HEAD_SPAWNING,
              NpcID.OLM_HEAD,
              NpcID.OLM_HAND_LEFT_SPAWNING,
              NpcID.OLM_HAND_LEFT,
              NpcID.OLM_HAND_RIGHT_SPAWNING,
              NpcID.OLM_HAND_RIGHT,
              // Theatre of Blood
              NpcID.TOB_MAIDEN_100,
              NpcID.TOB_MAIDEN_70,
              NpcID.TOB_MAIDEN_50,
              NpcID.TOB_MAIDEN_30,
              NpcID.TOB_MAIDEN_100_HARD,
              NpcID.TOB_MAIDEN_70_HARD,
              NpcID.TOB_MAIDEN_50_HARD,
              NpcID.TOB_MAIDEN_30_HARD,
              NpcID.TOB_MAIDEN_100_STORY,
              NpcID.TOB_MAIDEN_70_STORY,
              NpcID.TOB_MAIDEN_50_STORY,
              NpcID.TOB_MAIDEN_30_STORY,
              NpcID.TOB_BLOAT,
              NpcID.TOB_BLOAT_HARD,
              NpcID.TOB_BLOAT_STORY,
              NpcID.NYLOCAS_BOSS_MELEE,
              NpcID.NYLOCAS_BOSS_MAGIC,
              NpcID.NYLOCAS_BOSS_RANGED,
              NpcID.NYLOCAS_BOSS_SPAWNING,
              NpcID.NYLOCAS_BOSS_MELEE_HARD,
              NpcID.NYLOCAS_BOSS_MAGIC_HARD,
              NpcID.NYLOCAS_BOSS_RANGED_HARD,
              NpcID.NYLOCAS_BOSS_SPAWNING_HARD,
              NpcID.NYLOCAS_BOSS_MELEE_STORY,
              NpcID.NYLOCAS_BOSS_MAGIC_STORY,
              NpcID.NYLOCAS_BOSS_RANGED_STORY,
              NpcID.NYLOCAS_BOSS_SPAWNING_STORY,
              NpcID.TOB_SOTETSEG_COMBAT,
              NpcID.TOB_SOTETSEG_COMBAT_HARD,
              NpcID.TOB_SOTETSEG_COMBAT_STORY,
              NpcID.TOB_XARPUS_STATIC,
              NpcID.TOB_XARPUS_FEEDING,
              NpcID.TOB_XARPUS_COMBAT,
              NpcID.TOB_XARPUS_STATIC_HARD,
              NpcID.TOB_XARPUS_FEEDING_HARD,
              NpcID.TOB_XARPUS_COMBAT_HARD,
              NpcID.TOB_XARPUS_STATIC_STORY,
              NpcID.TOB_XARPUS_FEEDING_STORY,
              NpcID.TOB_XARPUS_COMBAT_STORY,
              NpcID.VERZIK_INITIAL,
              NpcID.VERZIK_PHASE1,
              NpcID.VERZIK_PHASE1_TO2_TRANSITION,
              NpcID.VERZIK_PHASE2,
              NpcID.VERZIK_PHASE2_TO3_TRANSITION,
              NpcID.VERZIK_PHASE3,
              NpcID.VERZIK_INITIAL_HARD,
              NpcID.VERZIK_PHASE1_HARD,
              NpcID.VERZIK_PHASE1_TO2_TRANSITION_HARD,
              NpcID.VERZIK_PHASE2_HARD,
              NpcID.VERZIK_PHASE2_TO3_TRANSITION_HARD,
              NpcID.VERZIK_PHASE3_HARD,
              NpcID.VERZIK_INITIAL_STORY,
              NpcID.VERZIK_PHASE1_STORY,
              NpcID.VERZIK_PHASE1_TO2_TRANSITION_STORY,
              NpcID.VERZIK_PHASE2_STORY,
              NpcID.VERZIK_PHASE2_TO3_TRANSITION_STORY,
              NpcID.VERZIK_PHASE3_STORY,
              // Tombs of Amascut
              NpcID.AKKHA_SPAWN,
              NpcID.AKKHA_MELEE,
              NpcID.AKKHA_RANGE,
              NpcID.AKKHA_MAGE,
              NpcID.AKKHA_ENRAGE_SPAWN,
              NpcID.AKKHA_ENRAGE_INITIAL,
              NpcID.AKKHA_ENRAGE,
              NpcID.AKKHA_ENRAGE_DUMMY,
              NpcID.TOA_BABA,
              NpcID.TOA_BABA_COFFIN,
              NpcID.TOA_BABA_DIGGING,
              NpcID.TOA_KEPHRI_BOSS_ENRAGE,
              NpcID.TOA_KEPHRI_BOSS_SHIELDED,
              NpcID.TOA_KEPHRI_BOSS_WEAK,
              NpcID.TOA_ZEBAK,
              NpcID.TOA_ZEBAK_ENRAGED,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE1,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE2_MAGE,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE2_RANGE,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE2_EXPOSED,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE3,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE3_CHARGING,
              NpcID.TOA_WARDEN_TUMEKEN_PHASE3_INACTIVE,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE1,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE2_MAGE,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE2_RANGE,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE2_EXPOSED,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE3,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE3_CHARGING,
              NpcID.TOA_WARDEN_ELIDINIS_PHASE3_INACTIVE,
              // Quest bosses
              NpcID.MYQ4_ABOMINATION,
              NpcID.AGRITH_NAAR,
              NpcID.BIM_GOLEM_BOSS,
              NpcID.DWARF_ROCK_AVATAR_WARRIOR,
              NpcID.DWARF_ROCK_AVATAR_WARRIOR_GREEN,
              NpcID.DWARF_ROCK_AVATAR_WARRIOR_YELLOW,
              NpcID.DWARF_ROCK_AVATAR_MAGE,
              NpcID.DWARF_ROCK_AVATAR_MAGE_GREEN,
              NpcID.DWARF_ROCK_AVATAR_MAGE_YELLOW,
              NpcID.DWARF_ROCK_AVATAR_ARCHER,
              NpcID.DWARF_ROCK_AVATAR_ARCHER_GREEN,
              NpcID.DWARF_ROCK_AVATAR_ARCHER_YELLOW,
              NpcID.TROLLROMANCE_ARRG_ATTACKABLE,
              NpcID.BRAIN_BARREL_CHEST,
              NpcID.GRANDTREE_BLACKDEMON,
              NpcID.BLACK_KNIGHT_TITAN,
              NpcID.ARENA_BOUNCER,
              NpcID.SHADOW_MAJ_BOUNCER,
              NpcID.CHRONOZON,
              NpcID.SHAYZIENQUEST_LIZARDMAN_BOSS,
              NpcID.COUNT_DRAYNOR,
              NpcID.HUNDRED_CULINAROMANCER_FINAL,
              NpcID.FROG_QUEST_CUTHBERT_COMBAT,
              NpcID.TROLL_CHAMPION,
              NpcID.HORROR_DAGGANOTH_AIR,
              NpcID.HORROR_DAGGANOTH_WATER,
              NpcID.HORROR_DAGGANOTH_FIRE,
              NpcID.HORROR_DAGGANOTH_EARTH,
              NpcID.HORROR_DAGGANOTH_RANGED,
              NpcID.HORROR_DAGGANOTH_MELEE,
              NpcID.DELRITH,
              NpcID.TREE_SPIRIT,
              NpcID.VIKING_DRAUGEN,
              NpcID.ELVARG_ALIVE,
              NpcID.CHICKENQUEST_EVIL_CHICKEN,
              NpcID.DEAL_EVIL_SPIRIT,
              NpcID.DARK_SEREN,
              NpcID.DARK_SEREN_PHANTOM,
              NpcID.BURGH_GADDERANKS_ATTACKABLE,
              NpcID.GALVEK_FIRE,
              NpcID.GALVEK_EARTH,
              NpcID.GALVEK_WATER,
              NpcID.GALVEK_WIND,
              NpcID.GENERAL_KHAZARD,
              NpcID.MYARM_GIANT_ROC,
              NpcID.CONTACT_SCARAB_BOSS,
              NpcID.ROYAL_SEA_SNAKE_MOTHER_SMALLER,
              NpcID.GRIM_GLOD,
              NpcID.MM2_DEMON_GLOUGH,
              NpcID.MM2_DEMON_GLOUGH_NOMOVE,
              NpcID.FRIS_TROLL_KING_TRUE,
              NpcID.CORSCURS_NAVIGATOR_COMBAT,
              NpcID.MM_DEMON,
              NpcID.ARENA_OGRE,
              NpcID.VIKING_ENEMY1,
              NpcID.VIKING_ENEMY2,
              NpcID.VIKING_ENEMY3,
              NpcID.VIKING_ENEMY4,
              NpcID.QUEST_LUNAR_MIRROR_OF_PLAYER,
              NpcID.QUEST_LUNAR_MIRROR_OF_PLAYER_FEMALE,
              NpcID.MELZAR_THE_MAD,
              NpcID.ROVING_MOSSGIANT,
              NpcID.NEZIKCHENED,
              NpcID.MYQ4_RANIS_VAMPYRE_COMBAT,
              NpcID.MYQ4_RANIS_VAMPYRE_COMBAT_ENRAGE,
              NpcID.HOSIDIUSQUEST_SNAKE,
              NpcID.SWAN_SEATROLL_QUEEN,
              NpcID.DTTD_SIGMUND_MELEE,
              NpcID.DTTD_SIGMUND_RANGED,
              NpcID.DTTD_SIGMUND_MAGIC,
              NpcID.DTTD_SIGMUND_RANGED_VS_ZANIK,
              NpcID.SLICE_SIGMUND_SHOWDOWN,
              NpcID.SLICE_SIGMUND_MELEE,
              NpcID.SLICE_SIGMUND_RANGED,
              NpcID.SLICE_SIGMUND_MAGIC,
              NpcID.SLICE_SIGMUND_NOPRAYER,
              NpcID.RD_COMBAT_NPC_ROOM_3,
              NpcID.SIR_MORDRED,
              NpcID.SLAGILITH,
              NpcID.ZOGRE_SLASH_BASH,
              NpcID.LOTR_TRAN_RAZORLOR_MUTANT,
              NpcID.LOTR_TRAN_RAZORLOR_GHOST,
              NpcID.SOULBANE_FINAL_TOLNA1,
              NpcID.SOULBANE_FINAL_TOLNA2,
              NpcID.SOULBANE_FINAL_TOLNA3,
              NpcID.HAUNTEDMINE_BOSS_GHOST,
              NpcID.OLAF2_ULFRIC,
              NpcID.ELID_GOLEM_BLACK,
              NpcID.ELID_GOLEM_GREY,
              NpcID.ELID_GOLEM_WHITE,
              NpcID.BLOODDIAMOND_VAMPIREWARRIOR,
              NpcID.BLOODDIAMOND_VAMPIREWARRIOR_UNKILLABLE,
              NpcID.ICEDIAMOND_ICEWARRIOR,
              NpcID.FIREDIAMOND_FIREWARRIOR,
              NpcID.FD_DAMIS_NORMAL,
              NpcID.FD_DAMIS_TOUGHER,
              NpcID.HUNDRED_MINION1,
              NpcID.HUNDRED_MINION2,
              NpcID.HUNDRED_MINION3,
              NpcID.HUNDRED_MINION4,
              NpcID.HUNDRED_MINION5_AIR,
              NpcID.HUNDRED_MINION5_FIRE,
              NpcID.HUNDRED_MINION5_WATER,
              NpcID.HUNDRED_MINION5_EARTH,
              NpcID.HUNDRED_MINION5_MELEE,
              NpcID.HUNDRED_MINION5_RANGED,
              NpcID.DREAM_INADEQUACY,
              NpcID.DREAM_EVERLASTING,
              NpcID.DREAM_UNTOUCHABLE,
              NpcID.DREAM_ILLUSIVE,
              NpcID.MM2_KRUK_COMBAT,
              NpcID.MM2_GENERAL_KOB_COMBAT,
              NpcID.MM2_CHIEFTAN_KEEF_COMBAT,
              NpcID.DREAM_ROBERT_COMBAT,
              NpcID.VORKATH_QUEST,
              NpcID.SOTE_ARIANWYN_COMBAT,
              NpcID.SOTE_ESSYLLT_COMBAT,
              NpcID.TOBQUEST_MAIDEN,
              NpcID.TOBQUEST_BLOAT,
              NpcID.TOBQUEST_NYLOBOSS,
              NpcID.TOBQUEST_NYLOCAS_1,
              NpcID.TOBQUEST_NYLOCAS_2,
              NpcID.TOBQUEST_NYLOCAS_3,
              NpcID.TOBQUEST_SOTETSEG,
              NpcID.TOBQUEST_XARPUS,
              NpcID.TOBQUEST_VERZIK,
              NpcID.TOBQUEST_HESPORI,
              NpcID.VARDORVIS_QUEST,
              NpcID.VARDORVIS_BASE_QUEST,
              NpcID.LEVIATHAN_QUEST,
              NpcID.WHISPERER_QUEST,
              NpcID.WHISPERER_MELEE_QUEST,
              NpcID.DUKE_SUCELLUS_AWAKE_QUEST,
              NpcID.DARK_SQUALL_COMBAT,
              NpcID.WGS_BALANCE_ELEMENTAL,
              NpcID.VMQ3_TOWER_TRIAL_4_BOSS,
              NpcID.AMOXLIATL_QUEST,
              NpcID.COA_MASTABA_GOLEM,
              NpcID.COA_ARRAV_COMBAT,
              NpcID.VMQ4_TEMPLE_GUARD_BOSS_FIGHT,
              NpcID.VMQ4_KEYSTONE_CHAMBER_BOSS_MAGIC,
              NpcID.VMQ4_KEYSTONE_CHAMBER_BOSS_RANGED,
              NpcID.VMQ4_CRYPT_ENNIUS_BOSS,
              NpcID.VMQ4_METZLI_BOSS,
              // TODO: NMZ
              // Last update:
              // https://oldschool.runescape.wiki/w/Superior_slayer_monster?oldid=15175912
              NpcID.SUPERIOR_CRAWLING_HAND,
              NpcID.SUPERIOR_CAVE_CRAWLER,
              NpcID.SUPERIOR_CAVE_CRAWLER_ICE,
              NpcID.SUPERIOR_BANSHEE,
              NpcID.SUPERIOR_KOUREND_BANSHEE,
              NpcID.SUPERIOR_ROCKSLUG,
              NpcID.SUPERIOR_COCKATRICE,
              NpcID.SUPERIOR_PYREFIEND,
              NpcID.SUPERIOR_PYRELORD,
              NpcID.SUPERIOR_BASILISK,
              NpcID.SUPERIOR_INFERNAL_MAGE,
              NpcID.SUPERIOR_BLOODVELD,
              NpcID.SUPERIOR_KOUREND_BLOODVELD,
              NpcID.SUPERIOR_GRYPHON,
              NpcID.SUPERIOR_JELLY,
              NpcID.SUPERIOR_KOUREND_JELLY,
              NpcID.SUPERIOR_CHILLED_JELLY,
              NpcID.SUPERIOR_TUROTH,
              NpcID.SUPERIOR_WARPED_TERRORBIRD,
              NpcID.SUPERIOR_WARPED_TORTOISE,
              NpcID.SUPERIOR_CAVE_HORROR,
              NpcID.SUPERIOR_ABBERANT_SPECTRE,
              NpcID.SUPERIOR_KOUREND_SPECTRE,
              NpcID.SUPERIOR_BASILISK_KNIGHT,
              NpcID.SUPERIOR_WYRM_DARK,
              NpcID.SUPERIOR_WYRM_LIGHT,
              NpcID.SUPERIOR_LAVA_STRYKEWYRM,
              NpcID.SUPERIOR_DUSTDEVIL,
              NpcID.SUPERIOR_KURASK,
              NpcID.SUPERIOR_GARGOYLE,
              NpcID.SUPERIOR_CUSTODIAN,
              NpcID.SUPERIOR_AQUANITE,
              NpcID.SUPERIOR_AQUANITE_NOLURE,
              NpcID.SUPERIOR_NECHRYAEL,
              NpcID.SUPERIOR_DRAKE,
              NpcID.SUPERIOR_ABYSSAL_DEMON,
              NpcID.SUPERIOR_DARK_BEAST,
              NpcID.SUPERIOR_ARAXYTE,
              NpcID.SUPERIOR_SMOKE_DEVIL,
              NpcID.SUPERIOR_HYDRA));

  private boolean percentageHealth;
  private int percentageHealthOverride;
  private float firstPercentageHealth = -1f;
  @Setter private int damageTaken;

  public NPCNameplate(NameplatesPlugin plugin, NPC actor) {
    super(plugin, actor);
  }

  public void updateFromActor(NameplatesPlugin plugin) {
    super.updateFromActor(plugin);

    var client = plugin.getClient();
    NPC npc = (NPC) actor;
    if (BOAT_IDS.contains(npc.getId())) {
      currentHealth = client.getVarbitValue(VarbitID.SAILING_SIDEPANEL_BOAT_HP);
      maxHealth = client.getVarbitValue(VarbitID.SAILING_SIDEPANEL_BOAT_HP_MAX);
      hpAnimationData.startAnimation(currentHealth, currentHealth, 0);

      var name2 =
          client
              .getDBTableField(
                  DBTableID.SailingBoatNameOptions.Row.SAILING_BOAT_NAME_DESCRIPTOR_OPTIONS, 1, 0)[
              client.getVarbitValue(VarbitID.SAILING_BOARDED_BOAT_NAME_2) - 1];
      var name3 =
          client
              .getDBTableField(
                  DBTableID.SailingBoatNameOptions.Row.SAILING_BOAT_NAME_NOUN_OPTIONS, 1, 0)[
              client.getVarbitValue(VarbitID.SAILING_BOARDED_BOAT_NAME_3) - 1];
      name = name2 + " " + name3;

      return;
    }

    if (npc.getId() == client.getVarpValue(VarPlayerID.HPBAR_HUD_NPC)) {
      maxHealth = client.getVarbitValue(VarbitID.HPBAR_HUD_BASEHP);
      percentageHealth = false;
      percentageHealthOverride = 0;
      firstPercentageHealth = -1f;
      return;
    }

    int maxHealth = 0;

    Integer health = plugin.getNpcManager().getHealth(npc.getId());
    if (health != null) {
      maxHealth = health;
    }

    NPCComposition composition = npc.getTransformedComposition();
    if (composition != null) {
      String longName = composition.getStringValue(ParamID.NPC_HP_NAME);
      if (!Strings.isNullOrEmpty(longName)) {
        this.name = longName;
      }
    }

    this.percentageHealth = maxHealth <= 0 && percentageHealthOverride <= 0;
    if (percentageHealth && composition != null) {
      var isAttackable = false;
      for (String action : composition.getActions()) {
        if (action != null && action.equals("Attack")) {
          isAttackable = true;
          break;
        }
      }

      // If the NPC doesn't have an attack option, it's likely not a combat NPC and we shouldn't
      // show a health bar for it. By setting percentageHealth to false, maxHealth will be set to 0,
      // hiding the health bar.
      if (!isAttackable) {
        this.percentageHealth = false;
      }
    }

    this.maxHealth = this.percentageHealth ? 100 : maxHealth;
  }

  @Override
  public boolean hasHintArrow() {
    return plugin.getClient().getHintArrowNpc() == actor;
  }

  public void recalculatePercentageHealth(NameplatesPlugin plugin) {
    if (((NPC) actor).getId() == NpcID.GEMSTONE_CRAB) {
      return;
    }

    if (firstPercentageHealth == -1f) {
      firstPercentageHealth = (float) currentHealth / maxHealth;
    }

    float currentPercentage = plugin.getCurrentHealth(actor, 1000) / 1000f;
    float percentageDifference = firstPercentageHealth - currentPercentage;
    int estimatedMaxHealth = Math.round(damageTaken / percentageDifference);

    if (this.percentageHealth) {
      this.currentHealth = Math.round(estimatedMaxHealth * firstPercentageHealth);
    }
    this.percentageHealth = false;
    this.percentageHealthOverride = estimatedMaxHealth;
  }

  public int getMaxHealth() {
    if (percentageHealthOverride > 0) {
      return percentageHealthOverride;
    }

    return maxHealth;
  }

  @Override
  public boolean isBoss() {
    return BOSS_IDS.contains(((NPC) actor).getId());
  }
}
