package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;

public class River {
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
    public ArrayList<RiverTile> riverTiles = new ArrayList<RiverTile>();
    public boolean add(EnumTile tile, EnumPosition position, boolean isTsumoSetsuri, boolean isHorizontal) {
        return riverTiles.add(new RiverTile(tile, position, isTsumoSetsuri, isHorizontal, true));
    }
    public RiverTile getLast() {
        return riverTiles.get(riverTiles.size() - 1);
    }
    public void removeWaiting() {
        riverTiles.get(riverTiles.size() - 1).setWaiting(false);
    }
}
