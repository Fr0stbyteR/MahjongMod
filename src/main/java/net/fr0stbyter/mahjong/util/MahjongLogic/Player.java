package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String id;
    private EnumPosition curWind;
    private int score;
    private boolean isMenzen = false;
    private boolean isRiichi = false;
    private boolean isDoubleRiichi = false;
    private boolean canIbbatsu = false;
    private boolean canRinshyan = false;
    private boolean canChyankan = false;
    private boolean isTenpai = false;
    private int furiTen = 0;
    private ArrayList<ArrayList<EnumTile>> handTiles = new ArrayList<ArrayList<EnumTile>>();
    private ArrayList<HashMap<WinningHand, Integer>> winningHand = new ArrayList<HashMap<WinningHand, Integer>>();

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

    public boolean isMenzen() {
        return isMenzen;
    }

    public boolean isRiichi() {
        return isRiichi;
    }

    public boolean isDoubleRiichi() {
        return isDoubleRiichi;
    }

    public boolean canIbbatsu() {
        return canIbbatsu;
    }

    public boolean canRinshyan() {
        return canRinshyan;
    }

    public boolean canChyankan() {
        return canChyankan;
    }

    public boolean isTenpai() {
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

    public boolean isOya() {
        return curWind == EnumPosition.NORTH;
    }

}
