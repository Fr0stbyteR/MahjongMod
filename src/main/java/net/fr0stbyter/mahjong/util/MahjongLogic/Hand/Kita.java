package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

public class Kita extends HandTiles {
    private EnumTile tile;

    public Kita() {
        tile = EnumTile.F4;
    }

    // TODO try and catch
    public EnumTile getTile() {
        return tile;
    }
}
