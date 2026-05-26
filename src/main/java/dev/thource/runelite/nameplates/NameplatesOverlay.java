package dev.thource.runelite.nameplates;

import dev.thource.runelite.nameplates.themes.nameplates.NameplateTheme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.IndexedSprite;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@Slf4j
public class NameplatesOverlay extends Overlay {
  private static final Pattern CHAT_FORMAT_PATTERN =
      Pattern.compile("<(col|img)=([0-9a-zA-Z]{6}|\\d+)>");
  private static final Pattern CHAT_IMG_PATTERN = Pattern.compile("<img=(\\d+)>");
  private static final float SPACE_CHAR_WIDTH = 4f;
  private static final int EMPTY_IMG_TAG_LENGTH = 6;
  private static final int COL_OPENING_TAG_LENGTH = 12;
  private static final int COL_CLOSING_TAG_LENGTH = 6;

  @Inject private Client client;
  @Inject private NameplatesPlugin plugin;
  private long lastRender;
  @Getter private Actor hoveredActor;

  // modIcons is a short-lived var that is conditionally populated, this is so that it's not being
  // populated unnecessary when no chat images are being rendered
  private IndexedSprite[] modIcons;

  @Inject
  NameplatesOverlay() {
    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ABOVE_SCENE);
    setPriority(PRIORITY_HIGH);

    lastRender = System.currentTimeMillis();
  }

  private Map<LocalPoint, List<Actor>> getLocalPointActorMap() {
    var localPlayer = client.getLocalPlayer();
    var worldView = localPlayer.getWorldView();

    // TODO: try to sort so that nameplate stack matches the right click menu
    var map = new HashMap<LocalPoint, List<Actor>>();
    Stream.of(worldView.players(), worldView.npcs())
        .flatMap(IndexedObjectSet::stream)
        .filter(plugin::shouldDrawOverlay)
        .sorted(
            Comparator.comparingInt(
                a -> {
                  if (a instanceof Player && a == localPlayer) {
                    return 0;
                  }

                  return 1;
                }))
        .forEach(
            (actor) ->
                map.computeIfAbsent(actor.getLocalLocation(), (k) -> new ArrayList<>()).add(actor));

    return map;
  }

  private int drawOverheadTexts(Graphics2D graphics, List<Actor> actors, int firstActorHeight) {
    graphics.setFont(FontManager.getRunescapeBoldFont());
    var fontMetrics = graphics.getFontMetrics();
    var stackHeight = 0;
    for (Actor actor : actors) {
      stackHeight += drawOverheadText(graphics, actor, firstActorHeight, fontMetrics, stackHeight);
    }
    // reset the short-lived var
    modIcons = null;

    return stackHeight;
  }

  private void drawModIcon(Graphics2D graphics, IndexedSprite icon, int drawX, int drawY) {
    var iconPixels = icon.getPixels();
    var iconPalette = icon.getPalette();

    for (int y = 0; y < icon.getHeight(); y++) {
      for (int x = 0; x < icon.getWidth(); x++) {
        var paletteIndex = iconPixels[y * icon.getWidth() + x] & 0xff;
        if (paletteIndex == 0) {
          continue;
        }

        var pixelColor = new Color(iconPalette[paletteIndex]);
        graphics.setColor(pixelColor);
        graphics.drawLine(drawX + x, drawY + y, drawX + x, drawY + y);
      }
    }
  }

  /** Record-like carrier for Java 11 that groups icon expansion outputs. */
  @Getter
  @RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
  private static final class IconExpansionResult {
    private final String expandedText;
    private final int maxIconHeight;
  }

  private IconExpansionResult expandTextForIcons(String text) {
    var formatMatcher = CHAT_IMG_PATTERN.matcher(text);
    var maxIconHeight = 0;
    var addedChars = 0;

    while (formatMatcher.find()) {
      var imgId = formatMatcher.group(1);
      var matcherStart = formatMatcher.start();

      if (modIcons == null) {
        modIcons = client.getModIcons();
      }

      var imgIdInt = Integer.parseInt(imgId);
      if (imgIdInt < 0 || imgIdInt >= modIcons.length) {
        continue;
      }

      var icon = modIcons[imgIdInt];
      if (icon == null) {
        continue;
      }

      maxIconHeight = Math.max(maxIconHeight, icon.getHeight());

      var charsWide = (int) Math.ceil(icon.getWidth() / SPACE_CHAR_WIDTH);
      var stringBlank = " ".repeat(charsWide);
      var tagLength = EMPTY_IMG_TAG_LENGTH + imgId.length();
      text =
          new StringBuilder(text)
              .insert(matcherStart + addedChars + tagLength, stringBlank)
              .toString();
      addedChars += charsWide;
    }

    return new IconExpansionResult(text, maxIconHeight);
  }

  private int drawOverheadText(
      Graphics2D graphics,
      Actor actor,
      int firstActorHeight,
      FontMetrics fontMetrics,
      int stackHeight) {
    if (actor.getOverheadCycle() <= 0) {
      return 0;
    }

    var point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight + 15);
    if (point == null) {
      return 0;
    }

    var text = actor.getOverheadText();
    if (text == null || text.isEmpty()) {
      return 0;
    }

    var formatMatcher = CHAT_FORMAT_PATTERN.matcher(text);
    if (!formatMatcher.find()) {
      var strippedText = Text.removeFormattingTags(text).replace("<gt>", ">").replace("<lt>", "<");
      var textBounds = fontMetrics.getStringBounds(strippedText, graphics);

      graphics.setColor(Color.BLACK);
      graphics.drawString(
          strippedText,
          point.getX() - ((int) (textBounds.getWidth() / 2)) + 1,
          point.getY() - stackHeight + 1);

      graphics.setColor(Color.YELLOW);
      graphics.drawString(
          strippedText,
          point.getX() - ((int) (textBounds.getWidth() / 2)),
          point.getY() - stackHeight);

      return (int) textBounds.getHeight();
    }

    var expansion = expandTextForIcons(text);
    text = expansion.getExpandedText();
    var maxIconHeight = expansion.getMaxIconHeight();

    var strippedText = Text.removeFormattingTags(text).replace("<gt>", ">").replace("<lt>", "<");
    var textBounds = fontMetrics.getStringBounds(strippedText, graphics);
    var textHeight = (int) textBounds.getHeight();
    var lineHeight = Math.max(textHeight, maxIconHeight);

    var attributedString = new AttributedString(strippedText);
    if (!strippedText.isEmpty()) {
      attributedString.addAttribute(TextAttribute.FONT, graphics.getFont());
    }

    var charOffset = 0;
    // text was modified, so re-create the matcher
    formatMatcher = CHAT_FORMAT_PATTERN.matcher(text);
    while (formatMatcher.find()) {
      var tag = formatMatcher.group(1);
      var value = formatMatcher.group(2);
      var matcherStart = formatMatcher.start();

      if (tag.equals("col")) {
        if (strippedText.isEmpty()) {
          continue;
        }

        var endColIndex = text.indexOf("</col>", matcherStart + COL_OPENING_TAG_LENGTH);
        if (endColIndex == -1) {
          charOffset += COL_OPENING_TAG_LENGTH;
          continue;
        }

        var colColor = ColorUtil.fromHex(value);
        var startIndex = matcherStart - charOffset;
        var endIndex = endColIndex - charOffset - COL_OPENING_TAG_LENGTH;
        if (endIndex > startIndex) {
          attributedString.addAttribute(TextAttribute.FOREGROUND, colColor, startIndex, endIndex);
        }

        charOffset += COL_OPENING_TAG_LENGTH + COL_CLOSING_TAG_LENGTH;
      } else /* img */ {
        var valueInt = Integer.parseInt(value);
        if (valueInt >= 0 && valueInt < modIcons.length) {
          var icon = modIcons[valueInt];
          if (icon != null) {
            var currentTextBounds =
                fontMetrics.getStringBounds(
                    strippedText.substring(0, matcherStart - charOffset), graphics);
            var spacingOffset =
                ((int) Math.ceil(icon.getWidth() / SPACE_CHAR_WIDTH) * SPACE_CHAR_WIDTH
                        - icon.getWidth())
                    / 2;
            var drawX =
                (int)
                    (point.getX()
                        - textBounds.getWidth() / 2
                        + currentTextBounds.getWidth()
                        + spacingOffset);
            var drawY =
                point.getY() - stackHeight - lineHeight + (lineHeight - icon.getHeight()) / 2;

            drawModIcon(graphics, icon, drawX, drawY);
          }
        }

        charOffset += EMPTY_IMG_TAG_LENGTH + value.length();
      }
    }

    // If the entire overhead text is just an image tag, there will be no stripped text to draw, so
    // skip drawing the text in that case
    if (!strippedText.isBlank()) {
      graphics.setColor(Color.BLACK);
      graphics.drawString(
          strippedText,
          point.getX() - ((int) (textBounds.getWidth() / 2)) + 1,
          point.getY() - stackHeight + 1);

      graphics.setColor(Color.YELLOW);
      graphics.drawString(
          attributedString.getIterator(),
          point.getX() - ((int) (textBounds.getWidth() / 2)),
          point.getY() - stackHeight);
    }

    return lineHeight;
  }

  private void renderOverheadStack(Graphics2D graphics, List<Actor> actors, long deltaMs) {
    var firstActorHeight = actors.get(0).getLogicalHeight();

    // Text has to be drawn here, because to hide overheads and skulls, we need to hide every 2d
    // ui element
    var stackHeight = drawOverheadTexts(graphics, actors, firstActorHeight);

    // Initial drawing loop draws only non-stacking nameplates
    var maxNonStackingHeight = 0;
    for (Actor actor : actors) {
      Nameplate nameplate = plugin.getNameplateForActor(actor);
      if (nameplate == null) {
        continue;
      }

      var point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight + 15);
      if (point == null) {
        continue;
      }

      if (!plugin.getAlwaysDrawName(nameplate) && !plugin.shouldDrawFor(nameplate)) {
        continue;
      }

      var theme = getActiveNameplateThemeForNameplate(nameplate);
      if (theme.isStacking()) {
        continue;
      }

      nameplate.getHpAnimationData().progressBy(deltaMs);

      var plateHeight =
          theme.drawNameplate(
              graphics, nameplate, new Point(point.getX(), point.getY() - stackHeight));
      maxNonStackingHeight = Math.max(maxNonStackingHeight, plateHeight);
    }

    if (maxNonStackingHeight > 0) {
      stackHeight += maxNonStackingHeight + 4;
    }

    // Second drawing loop draws only stacking nameplates
    for (Actor actor : actors) {
      Nameplate nameplate = plugin.getNameplateForActor(actor);
      if (nameplate == null) {
        continue;
      }

      var point = actor.getCanvasTextLocation(graphics, " ", firstActorHeight + 15);
      if (point == null) {
        continue;
      }

      if (!plugin.getAlwaysDrawName(nameplate) && !plugin.shouldDrawFor(nameplate)) {
        continue;
      }

      var theme = getActiveNameplateThemeForNameplate(nameplate);
      if (!theme.isStacking()) {
        continue;
      }

      nameplate.getHpAnimationData().progressBy(deltaMs);

      var plateHeight =
          theme.drawNameplate(
              graphics, nameplate, new Point(point.getX(), point.getY() - stackHeight));

      stackHeight += plateHeight + 4;
    }
  }

  private void renderHitsplats(Graphics2D graphics, Actor actor) {
    plugin.getActiveHitsplatTheme().drawHitsplats(graphics, actor);
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

    plugin.updateNameplatesAccessListStatuses();

    for (var entry : actorEntrySets) {
      var actors = entry.getValue();
      renderOverheadStack(graphics, actors, deltaMs);

      actors.stream()
          .sorted(
              Comparator.comparingInt(
                      (Actor actor) -> {
                        // Draw own hitsplats last (on top)
                        if (actor instanceof Player && actor == client.getLocalPlayer()) {
                          return Integer.MIN_VALUE;
                        }

                        return actor.getLocalLocation().distanceTo(cameraPoint);
                      })
                  .reversed())
          .forEachOrdered(actor -> renderHitsplats(graphics, actor));
    }

    lastRender = System.currentTimeMillis();

    return null;
  }

  private void updateHoveredActor() {
    var menu = client.getMenu();
    var menuEntries = menu.getMenuEntries();
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
      case WALK:
        hoveredActor = entry.getActor();
        return;
      default:
        break;
    }

    hoveredActor = null;
  }

  public NameplateTheme getActiveNameplateThemeForNameplate(Nameplate nameplate) {
    var actor = nameplate.getActor();
    if (actor instanceof Player) {
      if (nameplate.getActor() == client.getLocalPlayer()) {
        return plugin.getActiveNameplateThemeForSelf();
      }

      if (nameplate.getPartyData() != null) {
        return plugin.getActiveNameplateThemeForParty();
      }

      return plugin.getActiveNameplateThemeForPlayers();
    }

    return plugin.getActiveNameplateThemeForNPCs();
  }

  public void renderNameplate(
      Graphics2D graphics, Nameplate nameplate, Point point, NameplateTheme theme) {
    theme.drawNameplate(graphics, nameplate, point);
  }

  private MenuEntry getHoveredMenuEntry(final MenuEntry[] menuEntries) {
    var menu = client.getMenu();
    var menuX = menu.getMenuX();
    var menuY = menu.getMenuY();
    var menuWidth = menu.getMenuWidth();
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
