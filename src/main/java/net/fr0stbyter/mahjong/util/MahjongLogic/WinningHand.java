package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Hand;

import java.util.ArrayList;

public class WinningHand {
    private int playerCount;
    private Player player;
    private Hand hand;
    private boolean isTsumo;
    private ArrayList<AnalyzeResult> yakuList = new ArrayList<AnalyzeResult>();
    private int fan = 0;
    private Fu fu;
    private int score = 0;
    private int baseScore = 0;
    private ScoreLevel scoreLevel = null;
    private boolean dirty;
    WinningHand(GameType gameTypeIn, Player playerIn, Hand handIn, boolean isTsumoIn, Fu fuIn) {
        playerCount = gameTypeIn.getPlayerCount();
        player = playerIn;
        hand = handIn;
        isTsumo = isTsumoIn;
        fu = fuIn;
        dirty = false;
    }

    public WinningHand add(AnalyzeResult analyzeResult) {
        if (analyzeResult.getHandStatus() != HandStatus.WIN) return this;
        if (analyzeResult.getWinningHand().isYakuman()) {
            for (AnalyzeResult result : yakuList) {
                if (result.getWinningHand() == EnumWinningHand.CHIITOITSU) {
                    yakuList.remove(result);
                    break;
                }
            }
        }
        yakuList.add(analyzeResult);
        dirty = true;
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

    public boolean getIsTsumo() {
        return isTsumo;
    }

    public ArrayList<AnalyzeResult> getyakuList() {
        return yakuList;
    }

    public int getFan() {
        if (!dirty) return fan;
        fan = 0;
        for (AnalyzeResult analyzeResult : yakuList) {
            if (analyzeResult.getHandStatus() == HandStatus.WIN) fan += analyzeResult.getFan();
        }
        return fan;
    }

    public Fu getFu() {
        return fu;
    }

    public int getScore() {
        getFan();
        baseScore = (int) (getFu().getCount() * Math.pow(2, fan + 2));
        if (fan < 0) {
            baseScore = 8000 * -1 * fan;
            scoreLevel = ScoreLevel.YAKUMAN;
        } else if (fan > 0 && fan < 6 && baseScore > 2000) {
            baseScore = 2000;
            scoreLevel = ScoreLevel.MANKAN;
        } else if (fan >= 6 && fan <= 7) {
            baseScore = 3000;
            scoreLevel = ScoreLevel.HANEMAN;
        } else if (fan >= 8 && fan <= 10) {
            baseScore = 4000;
            scoreLevel = ScoreLevel.BAIMAN;
        } else if (fan >= 11 && fan <= 12) {
            baseScore = 6000;
            scoreLevel = ScoreLevel.SANBAIMAN;
        } else if (fan >= 13) {
            baseScore = 8000;
            scoreLevel = ScoreLevel.YAKUMAN;
        } else {
            scoreLevel = null;
        }
        score = baseScore * (isTsumo ? (getPlayer().isOya() ? (playerCount - 1) * 2 : playerCount) : 4);
        score = (int) (Math.ceil(((double) score) / 100) * 100);
        dirty = false;
        return score;
    }

    public int getBaseScore() {
        if (dirty) getScore();
        return baseScore;
    }

    public ScoreLevel getScoreLevel() {
        if (dirty) getScore();
        return scoreLevel;
    }

    public enum HandStatus {WIN, TEN, NOTEN}
    public enum ScoreLevel {MANKAN, HANEMAN, BAIMAN, SANBAIMAN, YAKUMAN}
}
