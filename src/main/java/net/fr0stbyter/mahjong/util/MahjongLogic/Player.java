package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Hand;

import java.util.ArrayList;
import java.util.HashMap;

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
    private Game game;
    private Hand hand;
    private WinningHand winningHand;
    private ArrayList<EnumTile> waiting;

    public Player(Game gameIn, String idIn, EnumPosition curWindIn) {
        game = gameIn;
        id = idIn;
        curWind = curWindIn;
        hand = new Hand();
        score = 0;
        isMenzen = true;
        isRiichi = false;
        isDoubleRiichi = false;
        canIbbatsu = false;
        canRinshyan = false;
        canChyankan = false;
        isTenpai = false;
        furiTen = 0;
        waiting = new ArrayList<EnumTile>();
    }

    public Game getGame() {
        return game;
    }

    public String getId() {
        return id;
    }

    public EnumPosition getCurWind() {
        return curWind;
    }

    public void setCurWind(EnumPosition curWindIn) {
        curWind = curWindIn;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int scoreIn) {
        score = scoreIn;
    }

    public boolean isMenzen() {
        return isMenzen;
    }

    public void setMenzen(boolean menzen) {
        isMenzen = menzen;
    }

    public boolean isRiichi() {
        return isRiichi;
    }

    public void setRiichi(boolean riichi) {
        isRiichi = riichi;
    }

    public boolean isDoubleRiichi() {
        return isDoubleRiichi;
    }

    public void setDoubleRiichi(boolean doubleRiichi) {
        isDoubleRiichi = doubleRiichi;
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

    public void setTenpai(boolean tenpai) {
        isTenpai = tenpai;
    }

    public int getFuriTen() {
        return furiTen;
    }

    public void setFuriTen(int furiTenIn) {
        furiTen = furiTenIn;
    }

    public Hand getHand() {
        return hand;
    }

    public WinningHand getWinningHand() {
        return winningHand;
    }

    public void setWinningHand(WinningHand winningHandIn) {
        winningHand = winningHandIn;
    }

    public boolean isOya() {
        return curWind == EnumPosition.EAST;
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

    public void setHandTiles(Hand handIn) {
        hand = handIn;
    }

    public void addToHanding(EnumTile tile) {
        hand.addToHanding(tile);
    }

    public void getTile(EnumTile tile) {
        hand.get(tile);
        //TODO analyzes
        //TODO 99
        //TODO options
    }

    public ArrayList<EnumTile> getWaiting() {
        return waiting;
    }

    public void setWaiting(ArrayList<EnumTile> waitingIn) {
        waiting = waitingIn;
    }

    public Player analyzeWaiting() {
        ArrayList<EnumTile> analyzeTen = Analyze.baseAnalyzeTen(hand);
        if (analyzeTen != null) {
            waiting.clear();
            waiting.addAll(analyzeTen);
        }
        return this;
    }

    public HashMap<EnumTile, ArrayList<EnumTile>> analyzeRiichi() {
        return Analyze.baseAnalyzeTen(hand, null);
    }
}
