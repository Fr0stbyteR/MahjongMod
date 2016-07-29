package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Hand;

public class Player {
    private String id;
    private EnumPosition curWind;
    private int score;
    private boolean isMenzen;
    private boolean isRiichi;
    private boolean isDoubleRiichi;
    private boolean canIbbatsu;
    private boolean canRinshyan;
    private boolean canChyankan;
    private boolean isTenpai;
    private int furiTen;
    private Hand hand;
    private WinningHand winningHand;

    public Player(String idIn, EnumPosition curWindIn) {
        id = idIn;
        curWind = curWindIn;
        hand = new Hand();
        score = 0;
        isMenzen = false;
        isRiichi = false;
        isDoubleRiichi = false;
        canIbbatsu = false;
        canRinshyan = false;
        canChyankan = false;
        isTenpai = false;
        furiTen = 0;
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

    public Hand getHand() {
        return hand;
    }

    public WinningHand getWinningHand() {
        return winningHand;
    }

    public boolean isOya() {
        return curWind == EnumPosition.EAST;
    }

    public void setCurWind(EnumPosition curWindIn) {
        curWind = curWindIn;
    }

    public void setScore(int scoreIn) {
        score = scoreIn;
    }

    public void setMenzen(boolean menzen) {
        isMenzen = menzen;
    }

    public void setRiichi(boolean riichi) {
        isRiichi = riichi;
    }

    public void setDoubleRiichi(boolean doubleRiichi) {
        isDoubleRiichi = doubleRiichi;
    }

    public void setCanIbbatsu(boolean canIbbatsuIn) {
        canIbbatsu = canIbbatsuIn;
    }

    public void setCanRinshyan(boolean canRinshyanIn) {
        canRinshyan = canRinshyanIn;
    }

    public void setCanChyankan(boolean canChyankanIn) {
        canChyankan = canChyankanIn;
    }

    public void setTenpai(boolean tenpai) {
        isTenpai = tenpai;
    }

    public void setFuriTen(int furiTenIn) {
        furiTen = furiTenIn;
    }

    public void setHandTiles(Hand handIn) {
        hand = handIn;
    }

    public void addToHanding(EnumTile tile) {
        hand.addToHanding(tile);
    }

    public void getTile(EnumTile tile) {
        hand.get(tile);
        //TODO analyzeS
    }

    public void setWinningHand(WinningHand winningHandIn) {
        winningHand = winningHandIn;
    }
}
