package net.fr0stbyter.mahjong.util.MahjongLogic;

public class GameState {
    private EnumPosition curRound; // with Prevailing Wind
    private int curHand; // 1-4
    private int curExtra; // 1-8
    private int curDeal; // 1-? xun
    private EnumPosition curPlayer;
    private boolean isHaitei;
    private Phase phase;
    public GameState() {
        curRound = EnumPosition.EAST;
        curHand = 1;
        curExtra = 1;
        curDeal = 1;
        curPlayer = EnumPosition.EAST;
        isHaitei = false;
        phase = Phase.SHUFFLE;
    }

    public EnumPosition getCurRound() {
        return curRound;
    }

    public int getCurHand() {
        return curHand;
    }

    public int getCurExtra() {
        return curExtra;
    }

    public boolean isHaitei() {
        return isHaitei;
    }

    public EnumPosition getCurPlayer() {
        return curPlayer;
    }

    public int getCurDeal() {
        return curDeal;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setCurRound(EnumPosition curRoundIn) {
        curRound = curRoundIn;
    }

    public void setCurHand(int curHandIn) {
        curHand = curHandIn;
    }

    public void setCurExtra(int curExtraIn) {
        curExtra = curExtraIn;
    }

    public void setCurDeal(int curDealIn) {
        curDeal = curDealIn;
    }

    public void setCurPlayer(EnumPosition curPlayerIn) {
        curPlayer = curPlayerIn;
    }

    public void setHaitei(boolean haiteiIn) {
        isHaitei = haiteiIn;
    }

    public void setPhase(Phase phaseIn) {
        phase = phaseIn;
    }

    public enum Phase {SHUFFLE, DEAL, WAIT_DISCARD, WAIT_MELD, WIN, DRAW}
}
