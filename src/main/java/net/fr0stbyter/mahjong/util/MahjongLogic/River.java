package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;

public class River {
    private Game game;
    private ArrayList<RiverTile> riverTiles = new ArrayList<RiverTile>();

    public River(Game gameIn) {
        game = gameIn;
    }

    public boolean add(EnumTile tile, EnumPosition position, boolean isTsumoSetsuri, boolean isHorizontal) {
        if (riverTiles.size() == 3
                && tile.getGroup() == EnumTileGroup.WIND
                && riverTiles.get(0).getTile() == tile
                && riverTiles.get(1).getTile() == tile
                && riverTiles.get(2).getTile() == tile) game.ryuukyoku(game.getPlayer(position), Game.Ryuukyoku.SUUFONRENTA);
        return riverTiles.add(new RiverTile(tile, position, isTsumoSetsuri, isHorizontal, true));
    }
    public int getCount() {
        return riverTiles.size();
    }
    public RiverTile getLast() {
        if (riverTiles.isEmpty()) return null;
        return riverTiles.get(riverTiles.size() - 1);
    }
    public EnumTile removeWaiting() {
        EnumTile tile = getLast().getTile();
        getLast().setShown(false);
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
    public static ArrayList<EnumTile> getTiles(ArrayList<RiverTile> riverTiles) {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (RiverTile riverTile : riverTiles) {
            tiles.add(riverTile.getTile());
        }
        return tiles;
    }
}
