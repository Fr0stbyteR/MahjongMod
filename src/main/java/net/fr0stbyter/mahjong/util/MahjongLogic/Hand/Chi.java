package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Chi implements HandTiles, Cloneable {
    private ArrayList<EnumTile> tiles;
    private EnumTile tileGot;

    public Chi(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, EnumTile tileGotIn) {
        tileGot = tileGotIn;
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn3, tileIn2, tileIn1));
        EnumTile temp = tiles.get(2);
        tiles.set(tiles.indexOf(tileGotIn), temp);
        tiles.set(2, tileGotIn);
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int getOrientation() {
        return 1;
    }
}
