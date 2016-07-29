package net.fr0stbyter.mahjong.util.MahjongLogic;


public class MountainTile {
    private EnumTile tile;
    private EnumPosition position;
    private boolean isShown;

    public MountainTile(EnumTile tileIn) {
        tile = tileIn;
        position = EnumPosition.NORTH;
        isShown = false;
    }

    public MountainTile(EnumTile tileIn, EnumPosition positionIn, boolean isShownIn) {
        tile = tileIn;
        position = positionIn;
        isShown = isShownIn;
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

    public MountainTile setShown(boolean setShownIn) {
        isShown = setShownIn;
        return this;
    }
}