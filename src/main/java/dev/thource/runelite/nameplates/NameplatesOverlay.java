package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class NameplatesOverlay extends Overlay {
  @Inject private Client client;
  @Inject private NameplatesPlugin plugin;
  private long lastRender;
  @Getter private Actor hoveredActor;

  @Inject
  NameplatesOverlay() {
    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ABOVE_SCENE);
    setPriority(PRIORITY_MED);

    lastRender = System.currentTimeMillis();
  }

  private Map<LocalPoint, List<Actor>> getLocalPointActorMap() {
    HashMap<LocalPoint, List<Actor>> map = new HashMap<>();

    WorldView worldView = client.getLocalPlayer().getWorldView();

    Stream.of(worldView.players(), worldView.npcs())
        .flatMap(IndexedObjectSet::stream)
        .forEach(
            (actor) ->
                map.computeIfAbsent(actor.getLocalLocation(), (k) -> new ArrayList<>()).add(actor));

    return map;
  }

  private void renderOverheadStack(Graphics2D graphics, List<Actor> actors, long deltaMs) {
    var firstActorHeight = actors.get(0).getLogicalHeight();

    var stackHeight = 0;

    // Text has to be drawn here, because to hide overheads and skulls, we need to hide every 2d
    // ui element
    graphics.setFont(FontManager.getRunescapeBoldFont());
    var fontMetrics = graphics.getFontMetrics();
    for (Actor actor : actors) {
      if (actor.getOverheadCycle() <= 0) {
        continue;
      }

      var point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight + 15);
      if (point == null) {
        continue;
      }

      var text = actor.getOverheadText();
      if (text == null || text.isEmpty()) {
        continue;
      }

      var textBounds = fontMetrics.getStringBounds(text, graphics);
      graphics.setColor(Color.BLACK);
      graphics.drawString(
          text,
          point.getX() - ((int) (textBounds.getWidth() / 2)) + 1,
          point.getY() - stackHeight + 1);
      graphics.setColor(Color.YELLOW);
      graphics.drawString(
          text, point.getX() - ((int) (textBounds.getWidth() / 2)), point.getY() - stackHeight);
      stackHeight += (int) textBounds.getHeight();
    }

    for (Actor actor : actors) {
      Nameplate nameplate = plugin.getNameplateForActor(actor);
      if (nameplate == null) {
        continue;
      }

      var point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight + 15);
      if (point == null) {
        continue;
      }

      stackHeight +=
          renderNameplate(graphics, nameplate, new Point(point.getX(), point.getY() - stackHeight))
              + 4;
      nameplate.getHpAnimationData().progressBy(deltaMs);
    }
  }

  private void renderHitsplats(Graphics2D graphics, List<Actor> actors) {
    var firstActor = actors.get(0);
    if (firstActor == null) {
      return;
    }

    var point = firstActor.getCanvasTextLocation(graphics, " ", firstActor.getLogicalHeight() / 2);
    if (point == null) {
      return;
    }

    var currentTime = System.currentTimeMillis();
    for (Actor actor : actors) {
      var hitsplats = plugin.getHitsplatsForActor(actor);
      if (hitsplats == null) {
        continue;
      }

      var hitsplatIterator = hitsplats.iterator();
      while (hitsplatIterator.hasNext()) {
        var hitsplat = hitsplatIterator.next();
        if (hitsplat.getCreatedAt() + 1200 >= currentTime) {
          break;
        }

        hitsplatIterator.remove();
      }
    }

    actors.stream()
        .map(actor -> plugin.getHitsplatsForActor(actor))
        .filter(Objects::nonNull)
        .flatMap(List::stream)
        .sorted(Comparator.comparingLong(PluginHitsplat::getCreatedAt))
        .collect(
            Collectors.groupingBy(PluginHitsplat::getServerTick, TreeMap::new, Collectors.toList()))
        .values()
        .forEach(hitsplats -> renderHitsplats(graphics, hitsplats, point));
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    updateHoveredActor();

    long deltaMs = System.currentTimeMillis() - lastRender;

    LocalPoint cameraPoint =
        new LocalPoint(
            client.getCameraX(), client.getCameraY(), client.getLocalPlayer().getWorldView());

    var actorEntrySets =
        getLocalPointActorMap().entrySet().stream()
            .sorted(
                Comparator.comparingInt(
                        (Map.Entry<LocalPoint, List<Actor>> entry) ->
                            entry.getKey().distanceTo(cameraPoint))
                    .reversed())
            .collect(Collectors.toList());

    for (var entry : actorEntrySets) {
      var actors = entry.getValue();
      renderOverheadStack(graphics, actors, deltaMs);
      renderHitsplats(graphics, actors);
    }

    lastRender = System.currentTimeMillis();

    return null;
  }

  private void updateHoveredActor() {
    MenuEntry[] menuEntries = client.getMenuEntries();
    if (menuEntries.length == 0) {
      hoveredActor = null;
      return;
    }

    HoverIndicatorMode hoverIndicatorMode = plugin.getConfig().hoverIndicatorMode();
    if ((hoverIndicatorMode == HoverIndicatorMode.RIGHT_CLICK
            || hoverIndicatorMode == HoverIndicatorMode.BUSY_RIGHT_CLICK)
        && !client.isMenuOpen()) {
      hoveredActor = null;
      return;
    }

    if (hoverIndicatorMode == HoverIndicatorMode.BUSY
        || hoverIndicatorMode == HoverIndicatorMode.BUSY_RIGHT_CLICK) {
      long uniqueActors =
          Arrays.stream(menuEntries)
              .map(MenuEntry::getActor)
              .filter(Objects::nonNull)
              .distinct()
              .count();

      if (uniqueActors <= 1) {
        hoveredActor = null;
        return;
      }
    }

    MenuEntry entry =
        client.isMenuOpen()
            ? getHoveredMenuEntry(menuEntries)
            : menuEntries[menuEntries.length - 1];
    MenuAction menuAction = entry.getType();
    switch (menuAction) {
      case WIDGET_TARGET_ON_NPC:
      case NPC_FIRST_OPTION:
      case NPC_SECOND_OPTION:
      case NPC_THIRD_OPTION:
      case NPC_FOURTH_OPTION:
      case NPC_FIFTH_OPTION:
      case EXAMINE_NPC:
      case WIDGET_TARGET_ON_PLAYER:
      case PLAYER_FIRST_OPTION:
      case PLAYER_SECOND_OPTION:
      case PLAYER_THIRD_OPTION:
      case PLAYER_FOURTH_OPTION:
      case PLAYER_FIFTH_OPTION:
      case PLAYER_SIXTH_OPTION:
      case PLAYER_SEVENTH_OPTION:
      case PLAYER_EIGHTH_OPTION:
      case RUNELITE_PLAYER:
        hoveredActor = entry.getActor();
        return;
      default:
        break;
    }

    hoveredActor = null;
  }

  public void renderHitsplats(Graphics2D graphics, List<PluginHitsplat> hitsplats, Point point) {
    if (hitsplats.isEmpty()) {
      return;
    }

    // TODO: config option for combining hitsplats
    // TODO: config option for hiding BLOCK_OTHER hitsplats
    var tickIndex = new AtomicInteger();
    var firstSplat = hitsplats.get(0);
    var combinedHitsplats =
        hitsplats.stream()
            .collect(
                Collectors.groupingBy(
                    PluginHitsplat::getHitsplatType,
                    Collectors.summingInt(PluginHitsplat::getAmount)))
            .entrySet()
            .stream()
            .map(
                entry -> {
                  var splat =
                      new PluginHitsplat(
                          client,
                          entry.getKey(),
                          entry.getValue(),
                          firstSplat.getServerTick(),
                          tickIndex.getAndIncrement());

                  splat.setCreatedAt(firstSplat.getCreatedAt());

                  return splat;
                })
            .sorted(Comparator.comparingLong(PluginHitsplat::getCreatedAt))
            .collect(Collectors.toList());

    plugin.getActiveHitsplatTheme().drawHitsplats(graphics, combinedHitsplats, point);
  }

  // returns rendered nameplate height
  public int renderNameplate(Graphics2D graphics, Nameplate nameplate, Point point) {
    return plugin.getActiveNameplateTheme().drawNameplate(graphics, nameplate, point);
  }

  public void renderNameplate(
      Graphics2D graphics, Nameplate nameplate, Point point, NameplateTheme theme) {
    theme.drawNameplate(graphics, nameplate, point);
  }

  private MenuEntry getHoveredMenuEntry(final MenuEntry[] menuEntries) {
    final int menuX = client.getMenuX();
    final int menuY = client.getMenuY();
    final int menuWidth = client.getMenuWidth();
    final Point mousePosition = client.getMouseCanvasPosition();

    int dy = mousePosition.getY() - menuY;
    dy -= 19; // Height of Choose Option
    if (dy < 0) {
      return menuEntries[menuEntries.length - 1];
    }

    int idx = dy / 15; // Height of each menu option
    idx = menuEntries.length - 1 - idx;

    if (mousePosition.getX() > menuX
        && mousePosition.getX() < menuX + menuWidth
        && idx >= 0
        && idx < menuEntries.length) {
      return menuEntries[idx];
    }
    return menuEntries[menuEntries.length - 1];
  }
}
