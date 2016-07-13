package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;


import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Gang extends HandTiles {
    private ArrayList<EnumTile> tiles;
    private Player fromPlayer;
    private boolean plusGang;

    public Gang(EnumTile tileIn1, EnumTile tileIn2, EnumTile tileIn3, EnumTile tileIn4, Player fromPlayerIn, boolean plusGangIn) {
        tiles = new ArrayList<EnumTile>(Arrays.asList(tileIn1, tileIn2, tileIn3, tileIn4));
        fromPlayer = fromPlayerIn;
        plusGang = plusGangIn;
    }
    // TODO try and catch
    public ArrayList<EnumTile> getTiles() {
        return tiles;
    }

    public EnumTile getTile() {
        return tiles.get(0);
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public boolean getPlusGang() {
        return plusGang;
    }
}
