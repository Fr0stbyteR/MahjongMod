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

    private enum Phase {SHUFFLE, DEAL, WAIT_DISCARD, WAIT_MELD, WIN, DRAW}
}
