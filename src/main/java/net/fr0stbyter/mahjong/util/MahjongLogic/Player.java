package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final String id;
    private final EnumPosition curWind;
    private final int score;
    private final boolean isMenzen = false;
    private final boolean isRiichi = false;
    private final boolean canIbbatsu = false;
    private final boolean isTenpai = false;
    private final int furiTen = 0;
    private final ArrayList<ArrayList<EnumTile>> handTiles = new ArrayList<ArrayList<EnumTile>>();
    private final ArrayList<HashMap<WinningHand, Integer>> winningHand = new ArrayList<HashMap<WinningHand, Integer>>();

    public Player(String idIn, EnumPosition curWindIn, int scoreIn) {
        id = idIn;
        curWind = curWindIn;
        score = scoreIn;
    }

    public String getId() {
        return id;
    }

    public EnumPosition getCurWind() {
        return curWind;
    }

    public int getScore() {
        return score;
    }

    public boolean getMenzen() {
        return isMenzen;
    }

    public boolean getRiichi() {
        return isRiichi;
    }

    public boolean getCanIbbatsu() {
        return canIbbatsu;
    }

    public boolean getTenpai() {
        return isTenpai;
    }

    public int getFuriTen() {
        return furiTen;
    }

    public ArrayList<ArrayList<EnumTile>> getHandTiles() {
        return handTiles;
    }

    public ArrayList<HashMap<WinningHand, Integer>> getWinningHand() {
        return winningHand;
    }
}
