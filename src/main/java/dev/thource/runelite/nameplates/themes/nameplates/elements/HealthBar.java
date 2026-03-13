package dev.thource.runelite.nameplates.themes.nameplates.elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.thource.runelite.nameplates.Nameplate;
import dev.thource.runelite.nameplates.themes.nameplates.PositionProvider;
import java.awt.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthBar extends Bar {

    public HealthBar() {
        super();

        barColorProvider = new HealthBarColorProvider();
    }

    public HealthBar(String name, PositionProvider xPositionProvider, PositionProvider yPositionProvider, int width, int height, int borderSize, PositionProvider textXPositionProvider, PositionProvider textYPositionProvider, Color borderColor, Color backgroundColor, Color textColor, HealthBarColorProvider barColorProvider) {
        super(name, xPositionProvider, yPositionProvider, width, height, borderSize, textXPositionProvider, textYPositionProvider, borderColor, backgroundColor, textColor, barColorProvider);
    }

    @Override
    protected int getCurrentValue(Nameplate nameplate) {
        return nameplate.getCurrentHealth();
    }

    @Override
    protected int getMaxValue(Nameplate nameplate) {
        return nameplate.getMaxHealth();
    }

    // This override is required so that the health bar can animate without the text also being animated
    @Override
    protected float getProgress(Nameplate nameplate) {
        return nameplate.getHealthPercentage();
    }

    public static HealthBar deserialize(JsonObject obj, Gson gson) {
        var providerJson = obj.getAsJsonObject("barColorProvider");
        // We have to remove the barColorProvider so that we can specify the right class manually
        obj.remove("barColorProvider");

        var healthBar = gson.fromJson(obj, HealthBar.class);
        healthBar.setBarColorProvider(gson.fromJson(providerJson, HealthBarColorProvider.class));
        return healthBar;
    }
}
