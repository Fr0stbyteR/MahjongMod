package net.fr0stbyter.mahjong.util.MahjongLogic;

public class GameState {
    private Game game;
    private EnumPosition curRound; // with Prevailing Wind
    private int curHand; // 1-4
    private int curExtra; // 1-8
    private int curDeal; // 1-? xun
    private EnumPosition curPlayer;
    private boolean isHaitei;
    private boolean allLast;
    private Phase phase;
    private int countRiichi;

    public GameState(Game gameIn) {
        game = gameIn;
        curRound = EnumPosition.EAST;
        curHand = 1;
        curExtra = 0;
        curDeal = 1;
        curPlayer = EnumPosition.EAST;
        isHaitei = false;
        allLast = game.getGameType().getLength() == 0;
        phase = Phase.SHUFFLE;
        countRiichi = 0;
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

    public GameState nextRound() {
        curRound = curRound.getNext();
        curHand = 1;
        isHaitei = false;
        curExtra = 0;
        curDeal = 0;
        return this;
    }

    public void setCurHand(int curHandIn) {
        curHand = curHandIn;
    }

    public GameState nextHand() {
        curHand++;
        isHaitei = false;
        curExtra = 0;
        curDeal = 1;
        if (game.getGameType().getLength() == 0) allLast = true;
        else if (curRound.getIndex() + 1 == game.getGameType().getLength() && curHand == game.getGameType().getPlayerCount()) allLast = true;
        if (curHand > game.getGameType().getPlayerCount()) nextRound();
        return this;
    }

    public void setCurExtra(int curExtraIn) {
        curExtra = curExtraIn;
    }

    public GameState nextExtra() {
        isHaitei = false;
        curExtra++;
        curDeal = 1;
        return this;
    }

    public void setCurDeal(int curDealIn) {
        curDeal = curDealIn;
    }

    public GameState nextDeal() {
        curDeal++;
        return this;
    }

    public GameState setCurPlayer(EnumPosition curPlayerIn) {
        curPlayer = curPlayerIn;
        return this;
    }

    public GameState nextPlayer() {
        curPlayer = game.getGameType().getPlayerCount() == 3 ? curPlayer.getNextNoNorth() : curPlayer.getNext();
        if (curPlayer == EnumPosition.EAST) nextDeal();
        return this;
    }

    public boolean isAllLast() {
        return allLast;
    }

    public void setHaitei(boolean haiteiIn) {
        isHaitei = haiteiIn;
    }

    public void setPhase(Phase phaseIn) {
        phase = phaseIn;
    }

    public int getCountRiichi() {
        return countRiichi;
    }

    public void setCountRiichi(int countRiichiIn) {
        countRiichi = countRiichiIn;
    }

    public void newRiichi() {
        countRiichi++;
    }

    public enum Phase {SHUFFLE, DEAL, WAIT_DISCARD, WAIT_MELD, AGARI, DRAW, GAME_OVER}
}
