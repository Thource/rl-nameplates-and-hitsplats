package dev.thource.runelite.nameplates.themes.nameplates;

import dev.thource.runelite.nameplates.Nameplate;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PositionProvider {
    private OffsetAnchor anchor = OffsetAnchor.START;
    private int value = 0;

    public PositionProvider() {}

    public PositionProvider(OffsetAnchor anchor, int value) {
        this.anchor = anchor;
        this.value = value;
    }

    protected int getValue(Nameplate nameplate) {
        return value;
    }

    public int get(Nameplate nameplate, int dimensionValue) {
        var value = getValue(nameplate);

        switch (anchor) {
            case START:
                return value;
            case MIDDLE:
                return value - dimensionValue / 2;
            case END:
                return value - dimensionValue;
        }

        return value;
    }
}
