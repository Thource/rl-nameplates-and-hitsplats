package dev.thource.runelite.nameplates;

import com.google.gson.Gson;
import com.google.inject.Provides;
import dev.thource.runelite.nameplates.panel.NameplatesPluginPanel;
import dev.thource.runelite.nameplates.themes.hitsplats.HitsplatTheme;
import dev.thource.runelite.nameplates.themes.hitsplats.OSRSHitsplatTheme;
import dev.thource.runelite.nameplates.themes.hitsplats.RingHitsplatTheme;
import dev.thource.runelite.nameplates.themes.hitsplats.ScrollingHitsplatTheme;
import dev.thource.runelite.nameplates.themes.nameplates.CustomNameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.FlatDarkExtendedTheme;
import dev.thource.runelite.nameplates.themes.nameplates.FlatDarkFullInfoTheme;
import dev.thource.runelite.nameplates.themes.nameplates.FlatDarkTheme;
import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import dev.thource.runelite.nameplates.themes.nameplates.OSRSTheme;
import dev.thource.runelite.nameplates.themes.nameplates.elements.Icon;
import java.awt.Component;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.HitsplatID;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Renderable;
import net.runelite.api.Skill;
import net.runelite.api.WorldView;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.party.PartyMember;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.events.UserJoin;
import net.runelite.client.party.events.UserPart;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChanges;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.party.data.PartyData;
import net.runelite.client.plugins.party.messages.StatusUpdate;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

/** NameplatesPlugin is a RuneLite plugin designed to add WoW style nameplates to NPCs. */
@Slf4j
@PluginDescriptor(
    name = "Nameplates",
    description = "Adds nameplates to NPCs.",
    tags = {"nameplates", "health", "npcs"})
public class NameplatesPlugin extends Plugin {
  private static final int NORMAL_HP_REGEN_TICKS = 100;
  @Getter @Inject private Client client;
  @Getter @Inject private Gson gson;
  @Getter @Inject private ClientThread clientThread;
  @Inject private ClientToolbar clientToolbar;
  @Getter @Inject private NameplatesConfig config;
  @Inject private OverlayManager overlayManager;
  @Getter @Inject private NameplatesOverlay nameplatesOverlay;
  @Inject private ItemStatChanges statChanges;
  @Getter @Inject private NPCManager npcManager;
  @Getter @Inject private SpriteManager spriteManager;
  @Getter @Inject private HiscoreClient hiscoreClient;
  @Getter @Inject private ConfigManager configManager;
  @Getter @Inject private ColorPickerManager colorPickerManager;
  @Getter @Inject private PartyService partyService;
  @Inject private RenderCallbackManager renderCallbackManager;
  @Inject private Hooks hooks;

  @Getter private final HashMap<Integer, HpCacheEntry> hpCache = new HashMap<>();
  @Getter private final HashMap<Integer, PluginActor> actors = new HashMap<>();

  @Getter
  private final Map<Long, PartyData> partyDataMap = Collections.synchronizedMap(new HashMap<>());

  private NameplatesPluginPanel panel;
  private NavigationButton navButton;

  private int cacheCleaningTick;
  private Instant startOfLastTick = Instant.now();
  private int ticksSinceHPRegen;
  @Getter private Instant nextPoisonTick;
  private final List<PluginHitsplat> hitsplatsCreatedThisCycle = new ArrayList<>();

  @Getter private final Map<String, NameplateTheme> nameplateThemes = new HashMap<>();
  @Getter private NameplateTheme activeNameplateThemeForSelf;
  @Getter private NameplateTheme activeNameplateThemeForParty;
  @Getter private NameplateTheme activeNameplateThemeForPlayers;
  @Getter private NameplateTheme activeNameplateThemeForNPCs;

  @Getter private final Map<String, HitsplatTheme> hitsplatThemes = new HashMap<>();
  @Getter private HitsplatTheme activeHitsplatTheme;

  private boolean accessListsDirty;
  private Set<Integer> npcIdAllowlist = new HashSet<>();
  private Set<String> npcNameAllowlist = new HashSet<>();
  private Set<Integer> npcIdDenylist = new HashSet<>();
  private Set<String> npcNameDenylist = new HashSet<>();

  private boolean isCheckingShouldDraw;
  private final RenderCallback renderCallback =
      new RenderCallback() {
        @Override
        public boolean addEntity(Renderable renderable, boolean ui) {
          return isCheckingShouldDraw
              || !ui
              || (!(renderable instanceof Player) && !(renderable instanceof NPC));
        }
      };

  boolean shouldDrawOverlay(Actor actor) {
    isCheckingShouldDraw = true;
    var draw = hooks.draw(actor, true);
    isCheckingShouldDraw = false;

    return draw;
  }

  public static boolean getConfirmation(
      Component parentComponent, String text, String confirmText, int messageType) {
    int result = JOptionPane.CANCEL_OPTION;

    try {
      //noinspection MagicConstant
      result =
          JOptionPane.showConfirmDialog(
              parentComponent, text, confirmText, JOptionPane.OK_CANCEL_OPTION, messageType);
    } catch (Exception err) {
      log.warn("Unexpected exception occurred while check for confirm required", err);
    }

    return result == JOptionPane.OK_OPTION;
  }

  private void migrateAccessLists() {
    var idAllowlist =
        configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsWhitelist");
    if (idAllowlist != null) {
      configManager.setConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsAllowlist", idAllowlist);
      configManager.unsetConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsWhitelist");
    }
    var nameAllowlist =
        configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, "npcNamesWhitelist");
    if (nameAllowlist != null) {
      configManager.setConfiguration(
          NameplatesConfig.CONFIG_GROUP, "npcNamesAllowlist", nameAllowlist);
      configManager.unsetConfiguration(NameplatesConfig.CONFIG_GROUP, "npcNamesWhitelist");
    }

    var idDenylist =
        configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsBlacklist");
    if (idDenylist != null) {
      configManager.setConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsDenylist", idDenylist);
      configManager.unsetConfiguration(NameplatesConfig.CONFIG_GROUP, "npcIdsBlacklist");
    }
    var nameDenylist =
        configManager.getConfiguration(NameplatesConfig.CONFIG_GROUP, "npcNamesBlacklist");
    if (nameDenylist != null) {
      configManager.setConfiguration(
          NameplatesConfig.CONFIG_GROUP, "npcNamesDenylist", nameDenylist);
      configManager.unsetConfiguration(NameplatesConfig.CONFIG_GROUP, "npcNamesBlacklist");
    }
  }

  private void loadSprites() {
    for (NameplateHeadIcon icon : NameplateHeadIcon.values()) {
      icon.loadImage(spriteManager);
    }

    for (NameplateSkullIcon icon : NameplateSkullIcon.values()) {
      icon.loadImage(spriteManager);
    }

    Icon.initImages(spriteManager);
  }

  @Override
  protected void startUp() {
    loadThemes();

    migrateAccessLists();
    updateNpcAccessLists();
    loadSprites();

    if (panel == null) {
      // edt
      SwingUtilities.invokeLater(
          () -> {
            panel = new NameplatesPluginPanel(this);

            navButton =
                NavigationButton.builder()
                    .tooltip("Nameplates & Hitsplats")
                    .icon(ImageUtil.loadImageResource(getClass(), "icon-28.png"))
                    .panel(panel)
                    .priority(8)
                    .build();
            clientToolbar.addNavigation(navButton);
          });
    }

    overlayManager.add(nameplatesOverlay);

    if (navButton != null) {
      clientToolbar.addNavigation(navButton);
    }

    renderCallbackManager.register(renderCallback);
  }

  @Subscribe
  public void onConfigChanged(ConfigChanged configChanged) {
    if (!configChanged.getGroup().equals(NameplatesConfig.CONFIG_GROUP)) {
      return;
    }

    var key = configChanged.getKey();
    if (!key.equals("npcIdsAllowlist")
        && !key.equals("npcNamesAllowlist")
        && !key.equals("npcIdsDenylist")
        && !key.equals("npcNamesDenylist")) {
      return;
    }

    updateNpcAccessLists();
  }

  private void updateNpcAccessLists() {
    npcIdAllowlist =
        Stream.of(config.npcIdsAllowlist().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(
                s -> {
                  try {
                    return Integer.parseInt(s);
                  } catch (NumberFormatException e) {
                    return -1;
                  }
                })
            .filter(i -> i >= 0)
            .collect(Collectors.toCollection(HashSet::new));

    npcNameAllowlist =
        Stream.of(config.npcNamesAllowlist().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toCollection(HashSet::new));

    npcIdDenylist =
        Stream.of(config.npcIdsDenylist().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(
                s -> {
                  try {
                    return Integer.parseInt(s);
                  } catch (NumberFormatException e) {
                    return -1;
                  }
                })
            .filter(i -> i >= 0)
            .collect(Collectors.toCollection(HashSet::new));

    npcNameDenylist =
        Stream.of(config.npcNamesDenylist().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toCollection(HashSet::new));

    accessListsDirty = true;
  }

  private void loadThemes() {
    nameplateThemes.clear();
    hitsplatThemes.clear();

    // add static themes
    nameplateThemes.put(FlatDarkTheme.ID, new FlatDarkTheme());
    nameplateThemes.put(FlatDarkFullInfoTheme.ID, new FlatDarkFullInfoTheme());
    nameplateThemes.put(FlatDarkExtendedTheme.ID, new FlatDarkExtendedTheme());
    nameplateThemes.put(OSRSTheme.ID, new OSRSTheme());

    hitsplatThemes.put(OSRSHitsplatTheme.ID, new OSRSHitsplatTheme());
    hitsplatThemes.put(RingHitsplatTheme.ID, new RingHitsplatTheme());
    hitsplatThemes.put(ScrollingHitsplatTheme.ID, new ScrollingHitsplatTheme());

    // load user-defined themes
    configManager
        .getConfigurationKeys(NameplatesConfig.CONFIG_GROUP + ".themes.nameplates.")
        .forEach(
            key -> {
              var themeJson =
                  configManager.getConfiguration(
                      NameplatesConfig.CONFIG_GROUP, key.replaceFirst("nameplates.", ""));
              if (themeJson == null) {
                return;
              }

              try {
                var theme = CustomNameplateTheme.deserialize(themeJson, gson, false);
                nameplateThemes.put(theme.getId(), theme);
              } catch (RuntimeException e) {
                log.warn("Failed to load custom nameplate theme {}", key, e);
              }
            });

    nameplateThemes.values().forEach(theme -> theme.setPlugin(this));
    hitsplatThemes.values().forEach(theme -> theme.setPlugin(this));

    activeNameplateThemeForSelf =
        nameplateThemes.getOrDefault(
            config.activeNameplateThemeForSelfId(), nameplateThemes.get(FlatDarkTheme.ID));
    activeNameplateThemeForParty =
        nameplateThemes.getOrDefault(
            config.activeNameplateThemeForPartyId(), nameplateThemes.get(FlatDarkTheme.ID));
    activeNameplateThemeForPlayers =
        nameplateThemes.getOrDefault(
            config.activeNameplateThemeForPlayersId(), nameplateThemes.get(FlatDarkTheme.ID));
    activeNameplateThemeForNPCs =
        nameplateThemes.getOrDefault(
            config.activeNameplateThemeForNPCsId(), nameplateThemes.get(FlatDarkTheme.ID));

    activeHitsplatTheme =
        hitsplatThemes.getOrDefault(
            config.activeHitsplatThemeId(), hitsplatThemes.get(OSRSHitsplatTheme.ID));
  }

  @Override
  protected void shutDown() {
    renderCallbackManager.unregister(renderCallback);

    if (navButton != null) {
      clientToolbar.removeNavigation(navButton);
    }

    overlayManager.remove(nameplatesOverlay);
  }

  public int getCurrentHealth(Actor actor, int maxHealth) {
    if (actor instanceof Player) {
      if (actor == client.getLocalPlayer()) {
        return client.getBoostedSkillLevel(Skill.HITPOINTS);
      }

      var partyMember = partyService.getMemberByDisplayName(actor.getName());
      if (partyMember != null) {
        return partyDataMap.get(partyMember.getMemberId()).getHitpoints();
      }
    }

    if (actor instanceof NPC
        && ((NPC) actor).getId() == client.getVarpValue(VarPlayerID.HPBAR_HUD_NPC)) {
      return client.getVarbitValue(VarbitID.HPBAR_HUD_HP);
    }

    if (actor.getHealthScale() == -1) {
      HpCacheEntry cacheEntry = getHpCacheEntryForActor(actor);
      if (cacheEntry != null) {
        return cacheEntry.getHp();
      }

      return maxHealth;
    }

    if (actor.getHealthRatio() == 0) {
      return 0;
    }

    int min = 1;
    int max;
    if (actor.getHealthScale() > 1) {
      if (actor.getHealthRatio() > 1) {
        min =
            (maxHealth * (actor.getHealthRatio() - 1) + actor.getHealthScale() - 2)
                / (actor.getHealthScale() - 1);
      }
      max =
          Math.min(
              (maxHealth * actor.getHealthRatio() - 1) / (actor.getHealthScale() - 1), maxHealth);
    } else {
      max = maxHealth;
    }

    return (min + max + 1) / 2;
  }

  public static int getActorId(Actor actor) {
    if (actor instanceof NPC) {
      // Offset this by 2048 to avoid collisions with player ids
      return ((NPC) actor).getIndex() + 2048;
    }

    return ((Player) actor).getId();
  }

  HpCacheEntry getHpCacheEntryForActor(Actor actor) {
    return hpCache.get(getActorId(actor));
  }

  Nameplate getNameplateForActor(Actor actor) {
    var actorId = getActorId(actor);
    if (!actors.containsKey(actorId)) {
      return null;
    }

    return actors.get(actorId).getNameplate();
  }

  private void updateHpCache(Actor actor) {
    if (actor.isDead() || client.getLocalPlayer() == actor) {
      return;
    }

    Integer maxHealth = 100;
    Nameplate nameplate = getNameplateForActor(actor);
    if (nameplate != null) {
      if (nameplate instanceof NPCNameplate) {
        if ((nameplate.isPercentageHealth()
                || ((NPCNameplate) nameplate).getPercentageHealthOverride() > 0)
            && ((NPCNameplate) nameplate).getDamageTaken() > 0) {
          ((NPCNameplate) nameplate).recalculatePercentageHealth(this);
          var hpCacheEntry = getHpCacheEntryForActor(actor);
          if (hpCacheEntry != null) {
            hpCacheEntry.setHp(nameplate.getMaxHealth());
          }
        }
      }

      maxHealth = nameplate.getMaxHealth();
    } else {
      if (actor instanceof NPC) {
        maxHealth = npcManager.getHealth(((NPC) actor).getId());
      }
      if (maxHealth == null) {
        return;
      }
    }

    int actorId = getActorId(actor);
    int currentHealth = getCurrentHealth(actor, maxHealth);
    if (currentHealth > 0) {
      HpCacheEntry cacheEntry = hpCache.computeIfAbsent(actorId, (k) -> new HpCacheEntry(actorId));
      if (actor.getHealthScale() != -1) {
        cacheEntry.setHealthScale(actor.getHealthScale());
      }
      cacheEntry.setHp(currentHealth);
      cacheEntry.setLastUpdate(client.getTickCount());
    }
  }

  private PluginActor instantiateActor(Actor actor) {
    Nameplate nameplate;
    if (actor instanceof Player) {
      nameplate = new PlayerNameplate(this, (Player) actor);
    } else {
      nameplate = new NPCNameplate(this, (NPC) actor);
    }

    var pluginActor = new PluginActor(actor, nameplate);
    actors.put(getActorId(actor), pluginActor);
    return pluginActor;
  }

  private void updateNameplate(Actor actor) {
    if (actor instanceof NPC) {
      var npc = (NPC) actor;

      // Don't make nameplates for pets
      if (npc.getComposition().isFollower()) {
        return;
      }
    }

    Nameplate nameplate = getNameplateForActor(actor);
    if (nameplate == null) {
      nameplate = instantiateActor(actor).getNameplate();
    }

    int hp;
    HpCacheEntry cacheEntry = getHpCacheEntryForActor(actor);
    if (cacheEntry == null) {
      if (client.getLocalPlayer() != actor) {
        nameplate.getHpAnimationData().startAnimation(nameplate.getCurrentHealth(), 0, 200);
        nameplate.setCurrentHealth(0);

        return;
      }

      hp = getCurrentHealth(actor, nameplate.getMaxHealth());
    } else {
      hp = cacheEntry.getHp();
      nameplate.setHealthScale(cacheEntry.getHealthScale());
    }

    if (hp != nameplate.getCurrentHealth()) {
      nameplate.getHpAnimationData().startAnimation(nameplate.getCurrentHealth(), hp, 200);
      nameplate.setCurrentHealth(hp);
    }
    nameplate.updateFromActor(this);
  }

  @Subscribe
  public void onGameStateChanged(GameStateChanged gameStateChanged) {
    if (gameStateChanged.getGameState() == GameState.HOPPING
        || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN) {
      ticksSinceHPRegen = -2; // For some reason this makes this accurate
    }

    if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
      return;
    }

    hpCache.clear();
    actors.clear();
  }

  @Subscribe
  public void onGameTick(GameTick tick) {
    startOfLastTick = Instant.now();

    int ticksPerHPRegen = NORMAL_HP_REGEN_TICKS;
    if (client.getVarbitValue(VarbitID.PRAYER_RAPIDHEAL) == 1) {
      ticksPerHPRegen /= 2;
    }

    ticksSinceHPRegen = (ticksSinceHPRegen + 1) % ticksPerHPRegen;

    if (client.getBoostedSkillLevel(Skill.HITPOINTS) == client.getRealSkillLevel(Skill.HITPOINTS)) {
      ticksSinceHPRegen = 0;
    }

    WorldView worldView = client.getLocalPlayer().getWorldView();
    Stream.of(worldView.npcs(), worldView.players())
        .flatMap(IndexedObjectSet::stream)
        .forEach(
            (actor) -> {
              updateHpCache(actor);
              updateNameplate(actor);
            });

    cacheCleaningTick++;

    if (cacheCleaningTick >= 10) {
      hpCache.values().removeIf((entry) -> client.getTickCount() >= entry.getLastUpdate() + 500);

      cacheCleaningTick = 0;
    }
  }

  @Subscribe
  private void onVarbitChanged(VarbitChanged varbitChanged) {
    if (varbitChanged.getVarpId() == VarPlayerID.POISON) {
      nextPoisonTick =
          Instant.now().plus(Duration.of(PoisonStatus.POISON_TICK_MILLIS, ChronoUnit.MILLIS));
    } else if (varbitChanged.getVarbitId() == VarbitID.PRAYER_RAPIDHEAL) {
      ticksSinceHPRegen = 0;
    }
  }

  @Subscribe
  public void onNpcDespawned(NpcDespawned npcDespawned) {
    var actorId = getActorId(npcDespawned.getNpc());
    actors.remove(actorId);
    activeHitsplatTheme.getDisplayType().unloadActor(actorId);
  }

  @Subscribe
  public void onPlayerDespawned(PlayerDespawned playerDespawned) {
    var actorId = getActorId(playerDespawned.getPlayer());
    actors.remove(actorId);
    activeHitsplatTheme.getDisplayType().unloadActor(actorId);
  }

  @Subscribe
  public void onActorDeath(ActorDeath actorDeath) {
    Actor actor = actorDeath.getActor();

    hpCache.remove(getActorId(actor));
  }

  @Subscribe
  public void onClientTick(ClientTick clientTick) {
    final var hideZeroHitsplats = config.hideZeroHitsplats();
    var actorHitsplatsMap =
        hitsplatsCreatedThisCycle.stream()
            .filter(h -> h.getAmount() > 0 || !hideZeroHitsplats)
            .collect(Collectors.groupingBy(PluginHitsplat::getActorId));
    final var combineHitsplats = config.combineHitsplats();

    actorHitsplatsMap.forEach(
        (actorId, hitsplats) -> {
          if (combineHitsplats) {
            hitsplats =
                hitsplats.stream()
                    .collect(
                        Collectors.groupingBy(
                            PluginHitsplat::getGameCycle,
                            Collectors.groupingBy(
                                PluginHitsplat::getHitsplatType,
                                Collectors.summingInt(PluginHitsplat::getAmount))))
                    .entrySet()
                    .stream()
                    .flatMap(
                        tickEntry -> {
                          var gameCycle = tickEntry.getKey();
                          var tickIndex = new AtomicInteger();
                          return tickEntry.getValue().entrySet().stream()
                              .map(
                                  entry ->
                                      new PluginHitsplat(
                                          client,
                                          actorId,
                                          entry.getKey(),
                                          entry.getValue(),
                                          gameCycle,
                                          tickIndex.getAndIncrement()));
                        })
                    .sorted(
                        Comparator.comparingInt(PluginHitsplat::getGameCycle)
                            .thenComparingInt(PluginHitsplat::getGameCycleIndex))
                    .collect(Collectors.toList());
          } else {
            hitsplats =
                hitsplats.stream()
                    .sorted(
                        Comparator.comparingInt(PluginHitsplat::getGameCycle)
                            .thenComparingInt(PluginHitsplat::getGameCycleIndex))
                    .collect(Collectors.toList());
          }

          var gameCycle = client.getGameCycle();
          var hitsplatLifetime = config.hitsplatLifetime();
          var hitsplatDisplayType = activeHitsplatTheme.getDisplayType();
          for (PluginHitsplat hitsplat : hitsplats) {
            if (!hitsplatDisplayType.addHitsplat(gameCycle, hitsplatLifetime, actorId, hitsplat)) {
              break;
            }
          }
        });

    hitsplatsCreatedThisCycle.clear();
  }

  @Subscribe
  public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
    var hitsplat = hitsplatApplied.getHitsplat();
    var actor = hitsplatApplied.getActor();

    var nameplate = getNameplateForActor(actor);
    if (nameplate != null) {
      nameplate.setLastHitsplat(client.getTickCount());

      if (hitsplat.isMine()) {
        nameplate.setLastLocalHitsplat(client.getTickCount());
      }

      if (actor instanceof NPC) {
        checkHitsplatForNoLoot(hitsplat, (NPC) actor);

        if (nameplate.isPercentageHealth()
            || ((NPCNameplate) nameplate).getPercentageHealthOverride() > 0) {
          ((NPCNameplate) nameplate)
              .setDamageTaken(((NPCNameplate) nameplate).getDamageTaken() + hitsplat.getAmount());
        }
      }
    }

    var gameCycle = client.getGameCycle();
    var actorId = getActorId(actor);
    hitsplatsCreatedThisCycle.add(
        new PluginHitsplat(
            client,
            actorId,
            hitsplat.getHitsplatType(),
            hitsplat.getAmount(),
            gameCycle,
            (int)
                hitsplatsCreatedThisCycle.stream().filter(h -> h.getActorId() == actorId).count()));
  }

  @Subscribe
  public void onUserJoin(UserJoin userJoin) {
    // this has a side effect of creating the party data
    getPartyData(userJoin.getMemberId());
  }

  @Subscribe
  public void onUserPart(UserPart userPart) {
    partyDataMap.remove(userPart.getMemberId());
  }

  @Subscribe
  public void onStatusUpdate(final StatusUpdate event) {
    final PartyData partyData = getPartyData(event.getMemberId());
    if (partyData == null) {
      return;
    }

    if (event.getHealthCurrent() != null) {
      partyData.setHitpoints(event.getHealthCurrent());
    }
    if (event.getHealthMax() != null) {
      partyData.setMaxHitpoints(event.getHealthMax());
    }
    if (event.getPrayerCurrent() != null) {
      partyData.setPrayer(event.getPrayerCurrent());
    }
    if (event.getPrayerMax() != null) {
      partyData.setMaxPrayer(event.getPrayerMax());
    }
    if (event.getRunEnergy() != null) {
      partyData.setRunEnergy(event.getRunEnergy());
    }
    if (event.getSpecEnergy() != null) {
      partyData.setSpecEnergy(event.getSpecEnergy());
    }
    if (event.getVengeanceActive() != null) {
      partyData.setVengeanceActive(event.getVengeanceActive());
    }
    if (event.getMemberColor() != null) {
      partyData.setColor(event.getMemberColor());
    }

    final PartyMember member = partyService.getMemberById(event.getMemberId());
    if (event.getCharacterName() != null) {
      final String name = Text.removeTags(Text.toJagexName(event.getCharacterName()));
      if (!name.isEmpty()) {
        member.setDisplayName(name);
      }
    }
  }

  @Nullable PartyData getPartyData(final long uuid) {
    final PartyMember memberById = partyService.getMemberById(uuid);

    if (memberById == null) {
      // This happens when you are not in party but you still receive message.
      // Can happen if you just left party and you received message before message went through
      // in ws service
      return null;
    }

    return partyDataMap.computeIfAbsent(uuid, (u) -> new PartyData(uuid, null));
  }

  private void checkHitsplatForNoLoot(Hitsplat hitsplat, NPC npc) {
    int accountType = client.getVarbitValue(VarbitID.IRONMAN);
    if (accountType == 0) {
      return;
    }

    boolean isNoLoot = hitsplat.getHitsplatType() == 1;
    boolean isGIM = accountType == 4 || accountType == 5 || accountType == 6;
    if (!isGIM || config.enableNoLootOtherGIMSplats()) {
      if (hitsplat.getHitsplatType() == HitsplatID.DAMAGE_OTHER
          || hitsplat.getHitsplatType() == HitsplatID.DAMAGE_OTHER_POISE) {
        isNoLoot = true;
      }
    }

    if (isNoLoot) {
      var nameplate = (NPCNameplate) getNameplateForActor(npc);
      if (nameplate != null) {
        nameplate.setNoLoot(true);
      }
    }
  }

  public double getTickProgress() {
    long timeSinceLastTick = Duration.between(startOfLastTick, Instant.now()).toMillis();

    return (timeSinceLastTick % Constants.GAME_TICK_LENGTH) / (float) Constants.GAME_TICK_LENGTH;
  }

  public double getHpRegenProgress() {
    int ticksPerHPRegen = NORMAL_HP_REGEN_TICKS;
    if (client.getVarbitValue(VarbitID.PRAYER_RAPIDHEAL) == 1) {
      ticksPerHPRegen /= 2;
    }

    return (double) ticksSinceHPRegen / ticksPerHPRegen;
  }

  public boolean isAnyPrayerActive() {
    return Arrays.stream(Prayer.values()).anyMatch(p -> client.getVarbitValue(p.getVarbit()) == 1);
  }

  private StatChange[] getHoveredItemStatChanges() {
    if (client.isMenuOpen()) {
      return new StatChange[0];
    }

    final MenuEntry[] menu = client.getMenu().getMenuEntries();
    final int menuSize = menu.length;
    if (menuSize == 0) {
      return new StatChange[0];
    }

    final MenuEntry entry = menu[menuSize - 1];
    final Widget widget = entry.getWidget();
    if (widget == null || widget.getId() != InterfaceID.Inventory.ITEMS) {
      return new StatChange[0];
    }

    final Effect change = statChanges.get(widget.getItemId());
    if (change == null) {
      return new StatChange[0];
    }

    return change.calculate(client).getStatChanges();
  }

  public StatChange getHoveredItemStatChange(Stat stat) {
    StatChange[] changes = getHoveredItemStatChanges();
    if (changes.length == 0) {
      return null;
    }

    return Arrays.stream(changes).filter(c -> c.getStat() == stat).findFirst().orElse(null);
  }

  public PoisonStatus getPoisonStatus() {
    final int poisonValue = client.getVarpValue(VarPlayerID.POISON);

    if (poisonValue > 0) {
      return new PoisonStatus(poisonValue);
    }

    return null;
  }

  private NameplateDisplayMode getDisplayMode(Nameplate nameplate) {
    var actor = nameplate.getActor();
    if (actor.getHash() == -123L) {
      return NameplateDisplayMode.ALWAYS;
    }

    if (actor instanceof Player) {
      if (actor == client.getLocalPlayer()) {
        return config.ownNameplateDisplayMode();
      }

      if (nameplate.getPartyData() != null) {
        return config.partyNameplateDisplayMode();
      }

      var player = (Player) actor;
      if (player.isFriend() && config.alwaysDrawFriendNames()) {
        return config.friendNameplateDisplayMode();
      }

      if (player.isFriendsChatMember() && config.alwaysDrawFriendChatNames()) {
        return config.friendChatNameplateDisplayMode();
      }

      if (player.isClanMember() && config.alwaysDrawClanNames()) {
        return config.clanNameplateDisplayMode();
      }

      return config.playerNameplateDisplayMode();
    }

    return config.npcNameplateDisplayMode();
  }

  public boolean getAlwaysDrawName(Nameplate nameplate) {
    var accessListStatus = nameplate.getAccessListStatus();
    if (accessListStatus == AccessListStatus.ALLOWLISTED) {
      return true;
    }
    if (accessListStatus == AccessListStatus.DENYLISTED) {
      return false;
    }

    var actor = nameplate.getActor();
    if (actor instanceof Player) {
      if ((actor == client.getLocalPlayer() && config.alwaysDrawOwnName())
          || (nameplate.getPartyData() != null && config.alwaysDrawPartyNames())) {
        return true;
      }

      var player = (Player) actor;
      if ((player.isFriend() && config.alwaysDrawFriendNames())
          || (player.isFriendsChatMember() && config.alwaysDrawFriendChatNames())
          || (player.isClanMember() && config.alwaysDrawClanNames())) {
        return true;
      }

      return config.alwaysDrawPlayerNames();
    }

    return config.alwaysDrawNPCNames();
  }

  public boolean shouldDrawBars(Nameplate nameplate) {
    var accessListStatus = nameplate.getAccessListStatus();
    if (accessListStatus == AccessListStatus.ALLOWLISTED) {
      return true;
    }
    if (accessListStatus == AccessListStatus.DENYLISTED) {
      return false;
    }

    return getDisplayMode(nameplate).shouldDraw(client, nameplate);
  }

  public void updateNameplatesAccessListStatuses() {
    actors.values().stream()
        .map(PluginActor::getNameplate)
        .filter(Objects::nonNull)
        .forEach(this::updateNameplateAccessListStatus);

    accessListsDirty = false;
  }

  private void updateNameplateAccessListStatus(Nameplate nameplate) {
    var actor = nameplate.getActor();
    if (!(actor instanceof NPC)) {
      return;
    }

    // Only refresh the status if the lists have changed or the status is currently unchecked
    if (!accessListsDirty && nameplate.getAccessListStatus() != AccessListStatus.UNCHECKED) {
      return;
    }

    var id = ((NPC) actor).getId();
    if (npcIdAllowlist.contains(id)) {
      nameplate.setAccessListStatus(AccessListStatus.ALLOWLISTED);
      return;
    }

    if (npcIdDenylist.contains(id)) {
      nameplate.setAccessListStatus(AccessListStatus.DENYLISTED);
      return;
    }

    var name = nameplate.getName();
    if (name != null) {
      var lowerName = name.toLowerCase();
      if (npcNameAllowlist.contains(lowerName)) {
        nameplate.setAccessListStatus(AccessListStatus.ALLOWLISTED);
        return;
      }

      if (npcNameDenylist.contains(lowerName)) {
        nameplate.setAccessListStatus(AccessListStatus.DENYLISTED);
        return;
      }
    }

    nameplate.setAccessListStatus(AccessListStatus.UNLISTED);
  }

  public boolean shouldDrawFor(Nameplate nameplate) {
    var accessListStatus = nameplate.getAccessListStatus();
    if (accessListStatus == AccessListStatus.ALLOWLISTED) {
      return true;
    }
    if (accessListStatus == AccessListStatus.DENYLISTED) {
      return false;
    }

    var overheadIcon = NameplateHeadIcon.get(nameplate.getActor());
    if (overheadIcon != null && overheadIcon != NameplateHeadIcon.NONE) {
      return true;
    }

    var skullIcon = NameplateSkullIcon.get(nameplate.getActor());
    if (skullIcon != null && skullIcon != NameplateSkullIcon.NONE) {
      return true;
    }

    return nameplate.isNoLoot()
        || nameplate.isHovered()
        || nameplate.hasVengeance()
        || nameplate.hasHintArrow()
        || shouldDrawBars(nameplate);
  }

  public void saveNameplateThemes() {
    nameplateThemes.values().stream()
        .filter(NameplateTheme::isEditable)
        .forEach(
            nameplateTheme ->
                configManager.setConfiguration(
                    NameplatesConfig.CONFIG_GROUP,
                    "themes.nameplates." + nameplateTheme.getId(),
                    nameplateTheme.serialize(gson, false)));
  }

  public void addNameplateTheme(NameplateTheme theme) {
    nameplateThemes.put(theme.getId(), theme);

    if (activeNameplateThemeForSelf.getId().equals(theme.getId())) {
      activeNameplateThemeForSelf = theme;
    }
    if (activeNameplateThemeForParty.getId().equals(theme.getId())) {
      activeNameplateThemeForParty = theme;
    }
    if (activeNameplateThemeForPlayers.getId().equals(theme.getId())) {
      activeNameplateThemeForPlayers = theme;
    }
    if (activeNameplateThemeForNPCs.getId().equals(theme.getId())) {
      activeNameplateThemeForNPCs = theme;
    }
  }

  public void deleteNameplateTheme(String id) {
    configManager.unsetConfiguration(NameplatesConfig.CONFIG_GROUP, "themes.nameplates." + id);
  }

  public void setActiveNameplateThemeForSelf(NameplateTheme theme) {
    configManager.setConfiguration(
        NameplatesConfig.CONFIG_GROUP, "activeNameplateThemeForSelfId", theme.getId());
    activeNameplateThemeForSelf = theme;
  }

  public void setActiveNameplateThemeForParty(NameplateTheme theme) {
    configManager.setConfiguration(
        NameplatesConfig.CONFIG_GROUP, "activeNameplateThemeForPartyId", theme.getId());
    activeNameplateThemeForParty = theme;
  }

  public void setActiveNameplateThemeForPlayers(NameplateTheme theme) {
    configManager.setConfiguration(
        NameplatesConfig.CONFIG_GROUP, "activeNameplateThemeForPlayersId", theme.getId());
    activeNameplateThemeForPlayers = theme;
  }

  public void setActiveNameplateThemeForNPCs(NameplateTheme theme) {
    configManager.setConfiguration(
        NameplatesConfig.CONFIG_GROUP, "activeNameplateThemeForNPCsId", theme.getId());
    activeNameplateThemeForNPCs = theme;
  }

  public void setActiveHitsplatTheme(HitsplatTheme theme) {
    configManager.setConfiguration(
        NameplatesConfig.CONFIG_GROUP, "activeHitsplatThemeId", theme.getId());
    activeHitsplatTheme = theme;
  }

  @Provides
  NameplatesConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(NameplatesConfig.class);
  }
}
