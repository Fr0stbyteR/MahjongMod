package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;

public class River {
    private ArrayList<RiverTile> riverTiles = new ArrayList<RiverTile>();
    public boolean add(EnumTile tile, EnumPosition position, boolean isTsumoSetsuri, boolean isHorizontal) {
        return riverTiles.add(new RiverTile(tile, position, isTsumoSetsuri, isHorizontal, true));
    }
    public RiverTile getLast() {
        return riverTiles.get(riverTiles.size() - 1);
    }
    public EnumTile removeWaiting() {
        EnumTile tile = getLast().getTile();
        riverTiles.remove(riverTiles.size() - 1);
        return tile;
    }
    public River cancelWaiting() {
        getLast().setWaiting(false);
        return this;
    }
    public ArrayList<RiverTile> getTilesFromPosition(EnumPosition enumPosition) {
        ArrayList<RiverTile> tiles = new ArrayList<RiverTile>();
        for (RiverTile riverTile : riverTiles) {
            if (riverTile.getPosition() == enumPosition) tiles.add(riverTile);
        }
        return tiles;
    }
}
