package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Collections;

public class Handing implements HandTiles, Cloneable {
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

    public Handing add(EnumTile tileIn) {
        tiles.add(tileIn);
        return this;
    }

    public Handing addAll(ArrayList<EnumTile> tilesIn) {
        tiles.addAll(tilesIn);
        return this;
    }
    public Handing remove(EnumTile tileIn) {
        tiles.remove(tileIn);
        return this;
    }

    public Handing sort() {
        Collections.sort(tiles, EnumTile.tilesComparator);
        return this;
    }

    public Handing toNormal() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.set(i, tiles.get(i).getNormal());
        }
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Handing handTiles = (Handing) super.clone();
        handTiles.setTiles((ArrayList<EnumTile>) tiles.clone());
        return handTiles;
    }

    @Override
    public int getOrientation() {
        return 0;
    }
}