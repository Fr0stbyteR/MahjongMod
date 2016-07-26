package net.fr0stbyter.mahjong.util.MahjongLogic;


public class RiverTile {
    private EnumTile tile;
    private EnumPosition position;
    private boolean isTsumoSetsuri;
    private boolean isHorizontal;
    private boolean isWaiting;

    public RiverTile(EnumTile tileIn, EnumPosition positionIn, boolean isTsumoSetsuriIn, boolean isHorizontalIn, boolean isWaitingIn) {
        tile = tileIn;
        position = positionIn;
        isTsumoSetsuri = isTsumoSetsuriIn;
        isHorizontal = isHorizontalIn;
        isWaiting = isWaitingIn;
    }

    public EnumTile getTile() {
        return tile;
    }

    public EnumPosition getPosition() {
        return position;
    }

    public boolean getTsumoSetsuri() {
        return isTsumoSetsuri;
    }

    public boolean getHorizontal() {
        return isHorizontal;
    }

    public boolean getWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }
}