package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Arrays;

public class Eye extends HandTiles {
    private ArrayList<EnumTile> tiles;

    public Eye(EnumTile tileIn1, EnumTile tileIn2) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2));
    }

    // TODO try and catch
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }
}
