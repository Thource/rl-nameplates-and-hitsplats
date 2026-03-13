package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.Nameplate;

public class DimensionProvider {
    private DimensionCalcType calcType = DimensionCalcType.STATIC;
    private int value = 26;

    public DimensionProvider() {
        super();
    }

    public DimensionProvider(DimensionCalcType calcType, int value) {
        this.calcType = calcType;
        this.value = value;
    }

    public int get(Nameplate nameplate) {
        switch (calcType) {
            case STATIC:
                return value;
            case DYNAMIC_HEALTH:
                return (int) (value * nameplate.getHealthPercentage());
            case DYNAMIC_PRAYER:
                return (int) (value * nameplate.getPrayerPercentage());
        }

        return value;
    }
}
