package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Arrays;

import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile.*;

public class TileGroup {
    public static ArrayList<EnumTile> man = new ArrayList<EnumTile>(Arrays.asList(M1, M2, M3, M4, M5, M5R, M6, M7, M8, M9));
    public static ArrayList<EnumTile> pin = new ArrayList<EnumTile>(Arrays.asList(P1, P2, P3, P4, P5, P5R, P6, P7, P8, P9));
    public static ArrayList<EnumTile> sou = new ArrayList<EnumTile>(Arrays.asList(S1, S2, S3, S4, S5, S5R, S6, S7, S8, S9));
    public static ArrayList<EnumTile> hua = new ArrayList<EnumTile>(Arrays.asList(H1, H2, H3, H4, H5, H6, H7, H8));
    public static ArrayList<EnumTile> wind = new ArrayList<EnumTile>(Arrays.asList(F1, F2, F3, F4));
    public static ArrayList<EnumTile> dragon = new ArrayList<EnumTile>(Arrays.asList(D1, D2, D3));
    public static ArrayList<EnumTile> routou = new ArrayList<EnumTile>(Arrays.asList(M1, M9, P1, P9, S1, S9));
    public static ArrayList<EnumTile> yaochyuu = new ArrayList<EnumTile>(Arrays.asList(M1, M9, P1, P9, S1, S9, F1, F2, F3, F4, D1, D2, D3));
    public static ArrayList<EnumTile> zi = new ArrayList<EnumTile>(Arrays.asList(F1, F2, F3, F4, D1, D2, D3));
    public static ArrayList<EnumTile> green = new ArrayList<EnumTile>(Arrays.asList(S2, S3, S4, S6, S8, D2));
}
