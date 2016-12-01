package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup.*;

public enum EnumTile {
    M1(MAN, 1, "m1", false, 1),
    M2(MAN, 2, "m2", false, 2),
    M3(MAN, 3, "m3", false, 3),
    M4(MAN, 4, "m4", false, 4),
    M5(MAN, 5, "m5", false, 5),
    M5R(MAN, 5, "m5r", true, 6),
    M6(MAN, 6, "m6", false, 7),
    M7(MAN, 7, "m7", false, 8),
    M8(MAN, 8, "m8", false, 9),
    M9(MAN, 9, "m9", false, 10),
    P1(PIN, 1, "p1", false, 11),
    P2(PIN, 2, "p2", false, 12),
    P3(PIN, 3, "p3", false, 13),
    P4(PIN, 4, "p4", false, 14),
    P5(PIN, 5, "p5", false, 15),
    P6(PIN, 6, "p6", false, 16),
    P5R(PIN, 5, "p5r", true, 17),
    P7(PIN, 7, "p7", false, 18),
    P8(PIN, 8, "p8", false, 19),
    P9(PIN, 9, "p9", false, 20),
    S1(SOU, 1, "s1", false, 21),
    S2(SOU, 2, "s2", false, 22),
    S3(SOU, 3, "s3", false, 23),
    S4(SOU, 4, "s4", false, 24),
    S5(SOU, 5, "s5", false, 25),
    S5R(SOU, 5, "s5r", true, 26),
    S6(SOU, 6, "s6", false, 27),
    S7(SOU, 7, "s7", false, 28),
    S8(SOU, 8, "s8", false, 29),
    S9(SOU, 9, "s9", false, 30),
    F1(WIND, 1, "east", false, 31),
    F2(WIND, 2, "south", false, 32),
    F3(WIND, 3, "west", false, 33),
    F4(WIND, 4, "north", false, 34),
    D1(DRAGON, 1, "bai", false, 40),
    D2(DRAGON, 2, "fa", false, 41),
    D3(DRAGON, 3, "zhong", false, 42),
    H1(HUA, 1, "h1", false, 51),
    H2(HUA, 2, "h2", false, 52),
    H3(HUA, 3, "h3", false, 53),
    H4(HUA, 4, "h4", false, 54),
    H5(HUA, 5, "h5", false, 55),
    H6(HUA, 6, "h6", false, 56),
    H7(HUA, 7, "h7", false, 57),
    H8(HUA, 8, "h8", false, 58);
    private EnumTileGroup group;
    private int number;
    private String name;
    private boolean isRed;
    private int index;

    EnumTile(EnumTileGroup groupIn, int numberIn, String nameIn, boolean isRedIn, int indexIn) {
        group = groupIn;
        number = numberIn;
        name = nameIn;
        isRed = isRedIn;
        index = indexIn;
    }

    public static EnumTile getTile(String name) {
        for (EnumTile tile : values()) {
            if (name.equalsIgnoreCase(tile.name())) return tile;
        }
        return null;
    }

    public static EnumTile getTile(int indexIn) {
        for (EnumTile tile : values()) {
            if (indexIn == tile.index) return tile;
        }
        return null;
    }

    public static EnumTile getTile(EnumTileGroup group, int number, boolean isRed) {
        for (EnumTile tile : values()) {
            if ((group == tile.group) && (number == tile.number) && (isRed == tile.isRed)) return tile;
        }
        return null;
    }

    public static ArrayList<EnumTile> getTiles(EnumTileGroup group) {
        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        for (EnumTile tile : values()) {
            if (group == tile.group) tiles.add(tile);
        }
        return tiles;
    }

    public EnumTileGroup getGroup() {
        return group;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public boolean isRed() {
        return isRed;
    }

    public int getIndex() {
        return index;
    }

    public EnumTile getRed() {
        if ((getNumber() == 5) && ((getGroup() == MAN) || (getGroup() == PIN) || (getGroup() == SOU))) return getTile(getGroup(), getNumber(), true);
        else return null;
    }

    public EnumTile getNormal() {
        if ((getNumber() == 5) && ((getGroup() == MAN) || (getGroup() == PIN) || (getGroup() == SOU))) return getTile(getGroup(), getNumber(), false);
        else return this;
    }

    public EnumTile getNext() {
        if ((getNumber() <= 8) && (getGroup() == MAN || getGroup() == PIN || getGroup() == SOU)) return getTile(getGroup(), getNumber() + 1, false);
        else return null;
    }

    public EnumTile getPrev() {
        if ((getNumber() >= 2) && (getGroup() == MAN || getGroup() == PIN || getGroup() == SOU)) return getTile(getGroup(), getNumber() - 1, false);
        else return null;
    }

    public EnumTile getDora(int playerCountIn) {
        int tileNumber = getNumber() + 1;
        if (getGroup() == MAN || getGroup() == PIN || getGroup() == SOU) {
            if (tileNumber == 10) tileNumber = 1;
        } else if (getGroup() == WIND) {
            if (tileNumber == 5) tileNumber = 1;
        } else if (getGroup() == DRAGON) {
            if (tileNumber == 4) tileNumber = 1;
        } else return null;
        if (playerCountIn == 3 && getGroup() == MAN && tileNumber == 2) tileNumber = 9;
        return getTile(getGroup(), tileNumber, false);
    }

    public String toString() {
        return name;
    }

    public ArrayList<EnumTile> toSingletonList() {
        return new ArrayList<EnumTile>(Collections.singletonList(this));
    }

    public static Comparator<EnumTile> tilesComparator = new Comparator<EnumTile>(){
        public int compare(EnumTile tileIn1, EnumTile tileIn2) {
            return tileIn1.getIndex() - tileIn2.getIndex();
        }
    };
}
