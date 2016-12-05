package net.fr0stbyter.mahjong.util.MahjongLogic;


public class MountainTile {
    private EnumTile tile;
    private EnumPosition position;
    private boolean isShown;
    private Prop prop;

    public MountainTile(EnumTile tileIn) {
        tile = tileIn;
        position = EnumPosition.NORTH;
        isShown = false;
        prop = Prop.NORMAL;
    }

    public MountainTile(EnumTile tileIn, EnumPosition positionIn, boolean isShownIn) {
        tile = tileIn;
        position = positionIn;
        isShown = isShownIn;
        prop = Prop.NORMAL;
    }

    public EnumTile getTile() {
        return tile;
    }

    public EnumPosition getPosition() {
        return position;
    }

    public boolean isShown() {
        return isShown;
    }

    public Prop getProp() {
        return prop;
    }

    public MountainTile setShown(boolean setShownIn) {
        isShown = setShownIn;
        return this;
    }

    public void setProp(Prop propIn) {
        prop = propIn;
    }

    public void setNull() {
        tile = null;
    }

    public boolean isNull() {
        return tile == null;
    }

    public enum Prop {RINSHYAN, DORA, URA, HAITEI, NORMAL}

    @Override
    public String toString() {
        return "{" + tile +
                ", position=" + position +
                ", isShown=" + isShown +
                ", prop=" + prop +
                '}';
    }
}