package dev.thource.runelite.nameplates.themes.nameplates;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.HitsplatID;
import net.runelite.api.gameval.SpriteID;

@RequiredArgsConstructor
@Getter
public enum HitsplatDefaultSprite {
  BLOCK_ME(HitsplatID.BLOCK_ME, SpriteID.Hitmark.HITSPLAT_BLUE_MISS),
  BLOCK_OTHER(HitsplatID.BLOCK_OTHER, SpriteID.Hitmark._9),
  DAMAGE_ME(HitsplatID.DAMAGE_ME, SpriteID.Hitmark._1),
  DAMAGE_OTHER(HitsplatID.DAMAGE_OTHER, SpriteID.Hitmark._10),
  POISON(HitsplatID.POISON, SpriteID.Hitmark.HITSPLAT_GREEN_POISON),
  DISEASE(HitsplatID.DISEASE, SpriteID.Hitmark._3),
  DISEASE_BLOCKED(HitsplatID.DISEASE_BLOCKED, SpriteID.Hitmark._12),
  VENOM(HitsplatID.VENOM, SpriteID.Hitmark.HITSPLAT_DARK_GREEN_VENOM),
  HEAL(HitsplatID.HEAL, SpriteID.Hitmark._8),
  CYAN_UP(HitsplatID.CYAN_UP, SpriteID.Hitmark._20),
  CYAN_DOWN(HitsplatID.CYAN_DOWN, SpriteID.Hitmark._21),
  DAMAGE_ME_CYAN(HitsplatID.DAMAGE_ME_CYAN, SpriteID.Hitmark._6),
  DAMAGE_OTHER_CYAN(HitsplatID.DAMAGE_OTHER_CYAN, SpriteID.Hitmark._15),
  DAMAGE_ME_ORANGE(HitsplatID.DAMAGE_ME_ORANGE, SpriteID.Hitmark._7),
  DAMAGE_OTHER_ORANGE(HitsplatID.DAMAGE_OTHER_ORANGE, SpriteID.Hitmark._16),
  DAMAGE_ME_YELLOW(HitsplatID.DAMAGE_ME_YELLOW, SpriteID.Hitmark._4),
  DAMAGE_OTHER_YELLOW(HitsplatID.DAMAGE_OTHER_YELLOW, SpriteID.Hitmark._13),
  DAMAGE_ME_WHITE(HitsplatID.DAMAGE_ME_WHITE, SpriteID.Hitmark._5),
  DAMAGE_OTHER_WHITE(HitsplatID.DAMAGE_OTHER_WHITE, SpriteID.Hitmark._14),
  DAMAGE_MAX_ME(HitsplatID.DAMAGE_MAX_ME, SpriteID.Hitmark._24),
  DAMAGE_MAX_ME_CYAN(HitsplatID.DAMAGE_MAX_ME_CYAN, SpriteID.Hitmark._27),
  DAMAGE_MAX_ME_ORANGE(HitsplatID.DAMAGE_MAX_ME_ORANGE, SpriteID.Hitmark._28),
  DAMAGE_MAX_ME_YELLOW(HitsplatID.DAMAGE_MAX_ME_YELLOW, SpriteID.Hitmark._25),
  DAMAGE_MAX_ME_WHITE(HitsplatID.DAMAGE_MAX_ME_WHITE, SpriteID.Hitmark._26),
  DAMAGE_ME_POISE(HitsplatID.DAMAGE_ME_POISE, SpriteID.Hitmark._29),
  DAMAGE_OTHER_POISE(HitsplatID.DAMAGE_OTHER_POISE, SpriteID.Hitmark._30),
  DAMAGE_MAX_ME_POISE(HitsplatID.DAMAGE_MAX_ME_POISE, SpriteID.Hitmark._31),
  CORRUPTION(HitsplatID.CORRUPTION, SpriteID.Hitmark._17),
  PRAYER_DRAIN(HitsplatID.PRAYER_DRAIN, SpriteID.Hitmark._32),
//  PRAYER_DRAIN_OTHER(HitsplatID.PRAYER_DRAIN_OTHER, SpriteID.Hitmark._33),
//  PRAYER_DRAIN_MAX(HitsplatID.PRAYER_DRAIN_MAX, SpriteID.Hitmark._34),
  BLEED(HitsplatID.BLEED, SpriteID.Hitmark._35),
  SANITY_DRAIN(HitsplatID.SANITY_DRAIN, SpriteID.Hitmark._38),
  SANITY_RESTORE(HitsplatID.SANITY_RESTORE, SpriteID.Hitmark._39),
  DOOM(HitsplatID.DOOM, SpriteID.Hitmark.COLOSSEUM_DOOM),
  BURN(HitsplatID.BURN, SpriteID.Hitmark.BURN_DAMAGE);

  // _18 = dodged me
  // _19 = dodged other
  // _22 = cyan up other
  // _23 = cyan down other
  // _37 = poison max ??
  // _42 = todt me
  // _43 = todt other
  // _44 = corrosion
  // _45 = sailing me
  // _46 = sailing other
  // _47 = sailing max


  private final int hitsplatType;
  private final int spriteId;

  public static Map<Integer, HitsplatOptions> defaultMap() {
    return Arrays.stream(HitsplatDefaultSprite.values())
        .collect(
            Collectors.toMap(
                HitsplatDefaultSprite::getHitsplatType,
                e -> new HitsplatOptions(e.hitsplatType, e.spriteId)));
  }
}
