package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Collections;

public class Handing implements HandTiles {
    private ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();

    @Override
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    @Override
    public EnumTile getTile() {
        return tiles.get(0);
    }


    public void setTiles(ArrayList<EnumTile> tilesIn) {
        tiles = tilesIn;
    }

    public Handing add(EnumTile tile) {
        tiles.add(tile);
        return this;
    }

    public Handing remove(EnumTile tile) {
        tiles.remove(tile);
        return this;
    }

    public void sort() {
        Collections.sort(tiles, EnumTile.tilesComparator);
    }
}