package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Chi implements HandTiles, Cloneable {
    private ArrayList<EnumTile> tiles;
    private EnumTile tileGot;
    private int orientation; // 1 right, 2 opposite, 3 left

    public Chi(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, int orientationIn, EnumTile tileGotIn) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3));
        orientation = orientationIn;
        tileGot = tileGotIn;
    }

    @Override
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    @Override
    public EnumTile getTile() {
        return tiles.get(0);
    }

    public EnumTile getTileGot() {
        return tileGot;
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
