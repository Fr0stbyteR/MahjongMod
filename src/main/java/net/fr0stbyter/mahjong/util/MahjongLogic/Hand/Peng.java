package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Peng extends HandTiles {
    private ArrayList<EnumTile> tiles;
    private Player fromPlayer;

    public Peng(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, Player fromPlayerIn) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3));
        fromPlayer = fromPlayerIn;
    }

    // TODO try and catch
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }
}