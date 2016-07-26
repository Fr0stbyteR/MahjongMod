package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Collections;

public class Get implements HandTiles {
    private EnumTile tile;

    public Get(EnumTile tileIn) {
        tile = tileIn;
    }

    @Override
    public ArrayList<EnumTile> getTiles() {
        return new ArrayList<EnumTile>(Collections.singletonList(tile));
    }

    @Override
    public EnumTile getTile() {
        return tile;
    }

}
