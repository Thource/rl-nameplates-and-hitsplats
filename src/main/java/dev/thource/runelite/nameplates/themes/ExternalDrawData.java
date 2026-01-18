package dev.thource.runelite.nameplates.themes;

import lombok.Getter;

@Getter
public class ExternalDrawData {
  private int leftOffset;
  private int rightOffset;
  private int topOffset;

  public void addLeftOffset(int overheadSize) {
    leftOffset += overheadSize;
  }

  public void addRightOffset(int overheadSize) {
    rightOffset += overheadSize;
  }

  public void addTopOffset(int overheadSize) {
    topOffset += overheadSize;
  }
}
