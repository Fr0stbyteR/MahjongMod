package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Chi implements HandTiles, Cloneable {
    private ArrayList<EnumTile> tiles;
    private EnumTile tileGot;
    private Player fromPlayer;

    public Chi(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, Player fromPlayerIn, EnumTile tileGotIn) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3));
        fromPlayer = fromPlayerIn;
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

    public Player getFromPlayer() {
        return fromPlayer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
