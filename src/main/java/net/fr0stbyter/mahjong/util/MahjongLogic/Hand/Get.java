package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

public class Get extends HandTiles {
    private EnumTile tile;

    public Get(EnumTile tileIn) {
        tile = tileIn;
    }

    // TODO try and catch
    public EnumTile getTile() {
        return tile;
    }
}
