package dev.thource.runelite.nameplates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActorType {
  SELF("Self", "self"),
  PARTY("Party", "party members"),
  FRIEND("Friend", "friends"),
  FRIEND_CHAT("FriendChat", "friends chat members"),
  CLAN("Clan", "clan members"),
  PLAYER("Players", "other players"),
  NPC("NPCs", "NPCs");

  private final String key;
  private final String description;
}
