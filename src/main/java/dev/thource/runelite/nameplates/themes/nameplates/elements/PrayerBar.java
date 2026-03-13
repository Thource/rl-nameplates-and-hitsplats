package dev.thource.runelite.nameplates.themes.nameplates.elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import java.awt.Graphics2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrayerBar extends Bar {

    public PrayerBar() {
        super();

        barColorProvider = new PrayerBarColorProvider();
    }

    public PrayerBar(String name, PositionProvider xPositionProvider, PositionProvider yPositionProvider, int width, int height, int borderSize, PositionProvider textXPositionProvider, PositionProvider textYPositionProvider, Color borderColor, Color backgroundColor, Color textColor, HealthBarColorProvider barColorProvider) {
        super(name, xPositionProvider, yPositionProvider, width, height, borderSize, textXPositionProvider, textYPositionProvider, borderColor, backgroundColor, textColor, barColorProvider);
    }

    @Override
    public void draw(Nameplate nameplate, Graphics2D graphics, int plateX, int plateY, int plateWidth, int plateHeight) {
        if (!nameplate.shouldDrawPrayerBar()) {
            return;
        }

        super.draw(nameplate, graphics, plateX, plateY, plateWidth, plateHeight);
    }

    @Override
    protected int getCurrentValue(Nameplate nameplate) {
        return nameplate.getCurrentPrayer();
    }

    @Override
    protected int getMaxValue(Nameplate nameplate) {
        return nameplate.getMaxPrayer();
    }

    public static PrayerBar deserialize(JsonObject obj, Gson gson) {
        var providerJson = obj.getAsJsonObject("barColorProvider");
        // We have to remove the barColorProvider so that we can specify the right class manually
        obj.remove("barColorProvider");

        var prayerBar = gson.fromJson(obj, PrayerBar.class);
        prayerBar.setBarColorProvider(gson.fromJson(providerJson, PrayerBarColorProvider.class));
        return prayerBar;
    }
}
