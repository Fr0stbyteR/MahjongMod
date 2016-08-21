package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Hand;

import java.util.ArrayList;

public class WinningHand {
    private Player player;
    private Hand hand;
    private EnumTile extraTile;
    private ArrayList<AnalyzeResult> yakuList = new ArrayList<AnalyzeResult>();
    private int fan = 0;
    private Fu fu;
    private int score = 0;
    private int baseScore = 0;
    private ScoreLevel scoreLevel = null;
    WinningHand(Player playerIn, Hand handIn, EnumTile extraTileIn, Fu fuIn) {
        player = playerIn;
        hand = handIn;
        extraTile = extraTileIn;
        fu = fuIn;
    }

    public WinningHand add(AnalyzeResult analyzeResult) {
        yakuList.add(analyzeResult);
        return this;
    }

    public boolean isWon() {
        for (AnalyzeResult analyzeResult : yakuList) {
            if (analyzeResult.getHandStatus() == HandStatus.WIN) return true;
        }
        return false;
    }

    public boolean isWon(EnumWinningHand enumWinningHand) {
        for (AnalyzeResult analyzeResult : yakuList) {
            if (analyzeResult.getHandStatus() == HandStatus.WIN && analyzeResult.getWinningHand() == enumWinningHand) return true;
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }

    public EnumTile getExtraTile() {
        return extraTile;
    }

    public ArrayList<AnalyzeResult> getyakuList() {
        return yakuList;
    }

    public int getFan() {
        for (AnalyzeResult analyzeResult : yakuList) {
            if (analyzeResult.getHandStatus() == HandStatus.WIN) fan += analyzeResult.getFan();
        }
        return fan;
    }

    public Fu getFu() {
        return fu;
    }

    public int getScore() {
        baseScore = (int) (getFu().getCount() * Math.pow(2, getFan() + 2));
        if (getFan() < 0) {
            score = 32000 * -1 * getFan();
            baseScore = 8000 * -1 * getFan();
            scoreLevel = ScoreLevel.YAKUMAN;
        }
        if (getFan() > 0 && getFan() < 5 && baseScore > 2000) {
            score = 8000;
            baseScore = 2000;
            scoreLevel = ScoreLevel.MANKAN;
        }
        if (getFan() >= 6 && getFan() <= 7) {
            score = 12000;
            baseScore = 3000;
            scoreLevel = ScoreLevel.HANEMAN;
        }
        if (getFan() >= 8 && getFan() <= 10) {
            score = 16000;
            baseScore = 4000;
            scoreLevel = ScoreLevel.BAIMAN;
        }
        if (getFan() >= 11 && getFan() <= 12) {
            score = 24000;
            baseScore = 6000;
            scoreLevel = ScoreLevel.SANBAIMAN;
        }
        if (getFan() >= 13) {
            score = 32000;
            baseScore = 8000;
            scoreLevel = ScoreLevel.YAKUMAN;
        }
        if (getPlayer().isOya()) score = (int) (score * 1.5);
        return score;
    }

    public int getBaseScore() {
        if (score == 0) getScore();
        return baseScore;
    }

    public ScoreLevel getScoreLevel() {
        if (score == 0) getScore();
        return scoreLevel;
    }

    public enum HandStatus {WIN, TEN, NOTEN}
    public enum ScoreLevel {MANKAN, HANEMAN, BAIMAN, SANBAIMAN, YAKUMAN}
}
