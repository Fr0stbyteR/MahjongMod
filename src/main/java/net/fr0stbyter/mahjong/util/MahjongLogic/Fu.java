package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Hand;

public class Fu {
    private int count;
    private int countElse;
    private int countMentsu;
    private int countEye;
    private int countMachi;
    private SpecYaku specYaku;

    Fu(int countElseIn, int countMentsuIn, int countEyeIn, int countMachiIn, SpecYaku specYakuIn) {
        countElse = countElseIn;
        countMentsu = countMentsuIn;
        countEye = countEyeIn;
        countMachi = countMachiIn;
        if (specYakuIn == SpecYaku.TSUMOBINHU) count = 20;
        else if (specYakuIn == SpecYaku.CHIITOITSU) count = 25;
        else if (specYakuIn == SpecYaku.KUIBINHU) count = 30;
        else count = 20 + countElse + countMentsu + countEye + countMachi;
    }

    public static Fu analyze(GameState gameState, Player player, Hand handAnalyzed, EnumTile tileIn, boolean isTsumo) {
        int countElse = 0;
        int countMentsu = 0;
        int countEye = 0;
        int countMachi = 0;

        if (isTsumo) countElse += 2;
        if (handAnalyzed.isMenzen()) countElse += 10;

        for (EnumTile tile : handAnalyzed.getTilePeng()) {
            if (TileGroup.yaochyuu.contains(tile)) countMentsu += 4;
            else countMentsu += 2;
        }
        for (EnumTile tile : handAnalyzed.getTileKe()) {
            if (TileGroup.yaochyuu.contains(tile)) countMentsu += 8;
            else countMentsu += 4;
        }
        for (EnumTile tile : handAnalyzed.getTileGang()) {
            if (TileGroup.yaochyuu.contains(tile)) countMentsu += 16;
            else countMentsu += 8;
        }
        for (EnumTile tile : handAnalyzed.getTileAnGang()) {
            if (TileGroup.yaochyuu.contains(tile)) countMentsu += 32;
            else countMentsu += 16;
        }

        if (gameState.getCurRound().getName().equals(handAnalyzed.getTileEye().getName())) countEye += 2;
        if (player.getCurWind().getName().equals(handAnalyzed.getTileEye().getName())) countEye += 2;
        if (TileGroup.dragon.contains(handAnalyzed.getTileEye())) countEye += 2;

        for (EnumTile tile : handAnalyzed.getTileShun()) {
            //if ((tile == tileIn.getNormal()) && (tileIn.getNumber() != 7)) break; //2menten not (7)89
            //if ((tile.getNext().getNext() == tileIn.getNormal()) && (tileIn.getNumber() != 3)) break; //2menten not12(3)
            if (tile.getNext() == tileIn.getNormal()) { //kanten
                countMachi += 2;
                break;
            }
            if ((tile.getNumber() == 1) && (tileIn.getNormal() == tile.getNext().getNext())) {  //12(3)
                countMachi += 2;
                break;
            }
            if ((tile.getNumber() == 7) && (tileIn.getNormal() == tile)) { //(7)89
                countMachi += 2;
                break;
            }
        }
        if ((countMachi == 0) && (tileIn.getNormal() == handAnalyzed.getTileEye())) countMachi += 2; //danki

        return new Fu(countElse, countMentsu, countEye, countMachi, null);
    }

    public int getCount() {
        if (specYaku == SpecYaku.TSUMOBINHU) count = 20;
        else if (specYaku == SpecYaku.CHIITOITSU) count = 25;
        else if (specYaku == SpecYaku.KUIBINHU) count = 30;
        else count = 20 + countElse + countMentsu + countEye + countMachi;
        return count;
    }

    public int getCountElse() {
        return countElse;
    }

    public int getCountMentsu() {
        return countMentsu;
    }

    public int getCountEye() {
        return countEye;
    }

    public int getCountMachi() {
        return countMachi;
    }

    public SpecYaku getSpecYaku() {
        return specYaku;
    }

    public Fu setSpecYaku(SpecYaku specYakuIn) {
        specYaku = specYakuIn;
        return this;
    }

    private enum SpecYaku {TSUMOBINHU, CHIITOITSU, KUIBINHU}
}
