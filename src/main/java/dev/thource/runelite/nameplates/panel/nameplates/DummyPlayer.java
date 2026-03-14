package dev.thource.runelite.nameplates.panel.nameplates;

import java.awt.*;
import java.awt.image.BufferedImage;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class DummyPlayer implements Player {
  private HeadIcon dummyOverhead = HeadIcon.MELEE;
  private int dummySkull = SkullIcon.SKULL;

  public void setOverhead(HeadIcon overhead) {
    dummyOverhead = overhead;
  }

  public void setSkull(int skull) {
    dummySkull = skull;
  }

  @Override
  public Node getNext() {
    return null;
  }

  @Override
  public Node getPrevious() {
    return null;
  }

  @Override
  public long getHash() {
    return -123L;
  }

  @Override
  public int getId() {
    return 0;
  }

  @Override
  public WorldView getWorldView() {
    return null;
  }

  @Override
  public LocalPoint getCameraFocus() {
    return null;
  }

  @Override
  public int getCombatLevel() {
    return 1;
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public boolean isInteracting() {
    return false;
  }

  @Override
  public Actor getInteracting() {
    return null;
  }

  @Override
  public int getHealthRatio() {
    return 0;
  }

  @Override
  public int getHealthScale() {
    return 0;
  }

  @Override
  public WorldPoint getWorldLocation() {
    return null;
  }

  @Override
  public LocalPoint getLocalLocation() {
    return null;
  }

  @Override
  public int getOrientation() {
    return 0;
  }

  @Override
  public int getCurrentOrientation() {
    return 0;
  }

  @Override
  public int getAnimation() {
    return 0;
  }

  @Override
  public int getPoseAnimation() {
    return 0;
  }

  @Override
  public void setPoseAnimation(int animation) {}

  @Override
  public int getPoseAnimationFrame() {
    return 0;
  }

  @Override
  public void setPoseAnimationFrame(int frame) {}

  @Override
  public int getIdlePoseAnimation() {
    return 0;
  }

  @Override
  public void setIdlePoseAnimation(int animation) {}

  @Override
  public int getIdleRotateLeft() {
    return 0;
  }

  @Override
  public void setIdleRotateLeft(int animationID) {}

  @Override
  public int getIdleRotateRight() {
    return 0;
  }

  @Override
  public void setIdleRotateRight(int animationID) {}

  @Override
  public int getWalkAnimation() {
    return 0;
  }

  @Override
  public void setWalkAnimation(int animationID) {}

  @Override
  public int getWalkRotateLeft() {
    return 0;
  }

  @Override
  public void setWalkRotateLeft(int animationID) {}

  @Override
  public int getWalkRotateRight() {
    return 0;
  }

  @Override
  public void setWalkRotateRight(int animationID) {}

  @Override
  public int getWalkRotate180() {
    return 0;
  }

  @Override
  public void setWalkRotate180(int animationID) {}

  @Override
  public int getRunAnimation() {
    return 0;
  }

  @Override
  public void setRunAnimation(int animationID) {}

  @Override
  public void setAnimation(int animation) {}

  @Override
  public int getAnimationFrame() {
    return 0;
  }

  @Override
  public void setActionFrame(int frame) {}

  @Override
  public void setAnimationFrame(int frame) {}

  @Override
  public IterableHashTable<ActorSpotAnim> getSpotAnims() {
    return null;
  }

  @Override
  public boolean hasSpotAnim(int spotAnimId) {
    return false;
  }

  @Override
  public void createSpotAnim(int id, int spotAnimId, int height, int delay) {}

  @Override
  public void removeSpotAnim(int id) {}

  @Override
  public void clearSpotAnims() {}

  @Override
  public int getGraphic() {
    return 0;
  }

  @Override
  public void setGraphic(int graphic) {}

  @Override
  public int getGraphicHeight() {
    return 0;
  }

  @Override
  public void setGraphicHeight(int height) {}

  @Override
  public int getSpotAnimFrame() {
    return 0;
  }

  @Override
  public void setSpotAnimFrame(int spotAnimFrame) {}

  @Override
  public Polygon getCanvasTilePoly() {
    return null;
  }

  @Override
  public net.runelite.api.Point getCanvasTextLocation(
      Graphics2D graphics, String text, int heightOffset) {
    return null;
  }

  @Override
  public net.runelite.api.Point getCanvasImageLocation(BufferedImage image, int heightOffset) {
    return null;
  }

  @Override
  public net.runelite.api.Point getCanvasSpriteLocation(SpritePixels sprite, int heightOffset) {
    return null;
  }

  @Override
  public net.runelite.api.Point getMinimapLocation() {
    return null;
  }

  @Override
  public int getLogicalHeight() {
    return 0;
  }

  @Override
  public Shape getConvexHull() {
    return null;
  }

  @Override
  public WorldArea getWorldArea() {
    return null;
  }

  @Override
  public String getOverheadText() {
    return "";
  }

  @Override
  public void setOverheadText(String overheadText) {}

  @Override
  public int getOverheadCycle() {
    return 0;
  }

  @Override
  public void setOverheadCycle(int cycles) {}

  @Override
  public boolean isDead() {
    return false;
  }

  @Override
  public void setDead(boolean dead) {}

  @Override
  public Model getModel() {
    return null;
  }

  @Override
  public int getModelHeight() {
    return 0;
  }

  @Override
  public void setModelHeight(int modelHeight) {}

  @Override
  public int getAnimationHeightOffset() {
    return 0;
  }

  @Override
  public int getRenderMode() {
    return 0;
  }

  @Override
  public PlayerComposition getPlayerComposition() {
    return null;
  }

  public Polygon[] getPolygons() {
    return new Polygon[0];
  }

  @Override
  public int getTeam() {
    return 0;
  }

  @Override
  public boolean isFriendsChatMember() {
    return false;
  }

  @Override
  public boolean isFriend() {
    return false;
  }

  @Override
  public boolean isClanMember() {
    return false;
  }

  @Override
  public HeadIcon getOverheadIcon() {
    return dummyOverhead;
  }

  @Override
  public int getSkullIcon() {
    return dummySkull;
  }

  @Override
  public void setSkullIcon(int skullIcon) {}

  @Override
  public int getFootprintSize() {
    return 0;
  }
}
