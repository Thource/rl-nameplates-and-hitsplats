package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.panel.Nameable;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.SpriteManager;

@Slf4j
public class SpriteOrImage implements Nameable {
  protected int spriteArchiveId = -1;
  protected String imageBase64;
  protected transient BufferedImage image;

  public SpriteOrImage(int spriteArchiveId) {
    this.spriteArchiveId = spriteArchiveId;
  }

  public void initialize(ClientThread clientThread, SpriteManager spriteManager) {
    if (spriteArchiveId == -1) {
      return;
    }

    clientThread.invokeLater(() -> image = spriteManager.getSprite(spriteArchiveId, 0));
  }

  public SpriteOrImage(String imageBase64) {
    this.imageBase64 = imageBase64;

    final Base64.Decoder decoder = Base64.getDecoder();
    final byte[] input = decoder.decode(imageBase64);
    try {
      image = ImageIO.read(new ByteArrayInputStream(input));
    } catch (Exception e) {
      log.error("Failed to read image from base64 string", e);
    }
  }

  @Override
  public String getName() {
    return imageBase64 != null ? "Image" : "Sprite";
  }

  public void draw(Graphics2D graphics, int x, int y, int width, int height) {
    if (image != null) {
      graphics.drawImage(image, x, y, width, height, null);
    }
  }
}
