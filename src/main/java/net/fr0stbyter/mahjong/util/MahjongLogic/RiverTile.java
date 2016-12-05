package net.fr0stbyter.mahjong.util.MahjongLogic;


public class RiverTile {
    private EnumTile tile;
    private EnumPosition position;
    private boolean isTsumoSetsuri;
    private boolean isHorizontal;
    private boolean isWaiting;
    private boolean isShown;

    public RiverTile(EnumTile tileIn, EnumPosition positionIn, boolean isTsumoSetsuriIn, boolean isHorizontalIn, boolean isWaitingIn) {
        tile = tileIn;
        position = positionIn;
        isTsumoSetsuri = isTsumoSetsuriIn;
        isHorizontal = isHorizontalIn;
        isWaiting = isWaitingIn;
        isShown = true;
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

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    @Override
    public String toString() {
        return "RiverTile{" +
                "tile=" + tile +
                ", position=" + position +
                ", isTsumoSetsuri=" + isTsumoSetsuri +
                ", isHorizontal=" + isHorizontal +
                ", isWaiting=" + isWaiting +
                ", isShown=" + isShown +
                '}';
    }
}