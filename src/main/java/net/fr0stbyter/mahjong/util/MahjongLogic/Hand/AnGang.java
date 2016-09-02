package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Arrays;


public class AnGang implements HandTiles, Cloneable {
    private ArrayList<EnumTile> tiles;

    public AnGang(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, EnumTile tileIn4) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3, tileIn4));
    }

    @Override
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    @Override
    public EnumTile getTile() {
        return tiles.get(0);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int getOrientation() {
        return 0;
    }
}
