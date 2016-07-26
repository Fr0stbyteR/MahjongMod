package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;


import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Arrays;

public class Shun implements HandTiles {
    private ArrayList<EnumTile> tiles;

    public Shun(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3));
    }

    @Override
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    @Override
    public EnumTile getTile() {
        return tiles.get(0);
    }

}
