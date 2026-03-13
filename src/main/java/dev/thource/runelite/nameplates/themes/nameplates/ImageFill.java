package dev.thource.runelite.nameplates.themes.nameplates;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageFill extends FillType {
    @Nullable
    protected BufferedImage image;

    public ImageFill() {}

    public ImageFill(@Nullable BufferedImage image) {
        this.image = image;
    }

    @Override
    public void draw(Graphics2D graphics, int x, int y, int width, int height) {
        if (image == null) {
            return;
        }

        graphics.drawImage(image, x, y, width, height, null);
    }
}
