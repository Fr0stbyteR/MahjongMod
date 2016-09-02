package net.fr0stbyter.mahjong.util.MahjongLogic.Hand;

import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;

import java.util.ArrayList;

public interface HandTiles {
    ArrayList<EnumTile> getTiles();
    EnumTile getTile();
    Object clone() throws CloneNotSupportedException;
    int getOrientation();
}
