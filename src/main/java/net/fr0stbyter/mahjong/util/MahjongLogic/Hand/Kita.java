package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;
import java.util.Collections;

public class Kita implements HandTiles, Cloneable {
    private EnumTile tile;

    public Kita() {
        tile = EnumTile.F4;
    }

    @Override
    public ArrayList<EnumTile> getTiles() {
        return tile.toSingletonList();
    }

    @Override
    public EnumTile getTile() {
        return tile;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
