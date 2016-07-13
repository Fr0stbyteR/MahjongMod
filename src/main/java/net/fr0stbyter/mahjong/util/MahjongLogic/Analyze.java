package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup.*;
import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumWinningHand.*;
import static net.fr0stbyter.mahjong.util.MahjongLogic.WinningHand.Status.*;

public class Analyze {
    public AnalyzeResult analyzeYaku(Hand handIn, EnumTile extraTileIn, Hand handAnalyzed) {
        boolean isMenzen = handIn.isMenzen();
        boolean isTsumo = handIn.hasGet();
        // special yaku
        AnalyzeResult gokushimusou13 = gokushimusou13(handIn, extraTileIn);
        AnalyzeResult gokushimusou = gokushimusou(handIn, extraTileIn);
        AnalyzeResult chiitoitsu = chiitoitsu(handIn, extraTileIn);
        // yakuman
        AnalyzeResult suuankoudanki = suuankoudanki(handIn, extraTileIn, handAnalyzed);
        AnalyzeResult suuankou = suuankou(handAnalyzed);
        AnalyzeResult daisangen = daisangen(handAnalyzed);
        AnalyzeResult tsuuiisou = tsuuiisou(handAnalyzed);
        AnalyzeResult daisuushii = daisuushii(handAnalyzed);
        AnalyzeResult shyousuushii = shyousuushii(handAnalyzed);
        AnalyzeResult ryuuiisou = ryuuiisou(handAnalyzed);

        return null;
    }

    private AnalyzeResult gokushimusou13(Hand handIn, EnumTile extraTileIn) {
        if (!handIn.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = GOKUSHIMUSOU13;
        int fan = winningHand.getFan();
        HashMap<ArrayList<EnumTile>, EnumTile> waitDrop = new HashMap<ArrayList<EnumTile>, EnumTile>();
        EnumTile waiting = null;

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);

        for (EnumTile tile : TileGroup.yaochyuu) {
            if (hand.getHanding().contains(tile)) hand.removeFromHanding(tile);
            else if (waiting == null) waiting = tile;
            else return new AnalyzeResult(NOTEN, null, null, 0);
        }
        if (waiting == null) {
            if (TileGroup.yaochyuu.contains(hand.getGet())) return new AnalyzeResult(WIN, null, winningHand, fan); // win
            else {
                waitDrop.put(TileGroup.yaochyuu, hand.getGet());
                return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
            }
        }
        if (waiting == hand.getGet()) {
            waitDrop.put(TileGroup.yaochyuu, hand.getHanding().get(0));
            return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult gokushimusou(Hand handIn, EnumTile extraTileIn) {
        if (!handIn.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = GOKUSHIMUSOU;
        int fan = winningHand.getFan();
        HashMap<ArrayList<EnumTile>, EnumTile> waitDrop = new HashMap<ArrayList<EnumTile>, EnumTile>();
        ArrayList<EnumTile> waiting = new ArrayList<EnumTile>();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);
        hand.addToHandingFromGet();

        for (EnumTile tile : TileGroup.yaochyuu) {
            if (hand.getHanding().contains(tile)) hand.removeFromHanding(tile);
            else if (waiting.isEmpty()) waiting.add(tile);
            else return new AnalyzeResult(NOTEN, null, null, 0);
        }
        if (waiting.isEmpty() && (TileGroup.yaochyuu.contains(hand.getHanding().get(0)))) return new AnalyzeResult(WIN, null, winningHand, fan); // win
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(0)) && TileGroup.yaochyuu.contains(hand.getHanding().get(1))) {
            waitDrop.put(waiting, hand.getHanding().get(0));
            waitDrop.put(waiting, hand.getHanding().get(1));
            return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
        }
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(0))) {
            waitDrop.put(waiting, hand.getHanding().get(1));
            return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
        }
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(1))) {
            waitDrop.put(waiting, hand.getHanding().get(0));
            return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult chiitoitsu(Hand handIn, EnumTile extraTileIn) {
        if (!handIn.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = CHIITOITSU;
        int fan = winningHand.getFan();
        HashMap<ArrayList<EnumTile>, EnumTile> waitDrop = new HashMap<ArrayList<EnumTile>, EnumTile>();
        ArrayList<EnumTile> single = new ArrayList<EnumTile>();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);
        hand.addToHandingFromGet();

        while (hand.getHanding().size() > 0) {
            if (hand.getHanding().get(0) == hand.getHanding().get(1)) hand.eye(hand.getHanding().get(0));
            else if (single.size() <= 2) {
                single.add(hand.getHanding().get(0));
                hand.removeFromHanding(hand.getHanding().get(0));
            } else return new AnalyzeResult(NOTEN, null, null, 0);
        }
        if (single.size() == 0) return new AnalyzeResult(WIN, null, winningHand, fan);
        waitDrop.put(new ArrayList<EnumTile>(Collections.singletonList(single.get(0))), single.get(1));
        waitDrop.put(new ArrayList<EnumTile>(Collections.singletonList(single.get(1))), single.get(0));
        return new AnalyzeResult(TEN, waitDrop, winningHand, fan);
    }

    private AnalyzeResult suuankoudanki(Hand handIn, EnumTile extraTileIn, Hand handAnalyzed) {
        if (!handIn.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = SUUANKOUDANKI;
        int fan = winningHand.getFan();

        if ((handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() == 4)
                && ((handIn.getGet() == handAnalyzed.getTileEye()) || (extraTileIn == handAnalyzed.getTileEye()))) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult suuankou(Hand handAnalyzed) {
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = SUUANKOU;
        int fan = winningHand.getFan();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() == 4) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult daisangen(Hand handAnalyzed) {
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = DAISANGEN;
        int fan = winningHand.getFan();
        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() + handAnalyzed.getCountGang() + handAnalyzed.getCountPeng() >= 3) {
            keTiles.addAll(handAnalyzed.getTileAnGang());
            keTiles.addAll(handAnalyzed.getTileGang());
            keTiles.addAll(handAnalyzed.getTilePeng());
            keTiles.addAll(handAnalyzed.getTileKe());
            if (keTiles.containsAll(TileGroup.dragon)) return new AnalyzeResult(WIN, null, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult tsuuiisou(Hand handAnalyzed) {
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = TSUUIISOU;
        int fan = winningHand.getFan();
        if (TileGroup.zi.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult daisuushii(Hand handAnalyzed) {
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = DAISUUSHII;
        int fan = winningHand.getFan();
        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();
        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() + handAnalyzed.getCountGang() + handAnalyzed.getCountPeng() == 4) {
            keTiles.addAll(handAnalyzed.getTileAnGang());
            keTiles.addAll(handAnalyzed.getTileGang());
            keTiles.addAll(handAnalyzed.getTilePeng());
            keTiles.addAll(handAnalyzed.getTileKe());
            if (keTiles.containsAll(TileGroup.wind)) return new AnalyzeResult(WIN, null, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult shyousuushii(Hand handAnalyzed) { // if not daisuushi
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = SHYOUSUUSHII;
        int fan = winningHand.getFan();
        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();
        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() + handAnalyzed.getCountGang() + handAnalyzed.getCountPeng() >= 3) {
            keTiles.addAll(handAnalyzed.getTileAnGang());
            keTiles.addAll(handAnalyzed.getTileGang());
            keTiles.addAll(handAnalyzed.getTilePeng());
            keTiles.addAll(handAnalyzed.getTileKe());
            keTiles.add(handAnalyzed.getTileEye());
            if (keTiles.containsAll(TileGroup.wind)) return new AnalyzeResult(WIN, null, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private AnalyzeResult ryuuiisou(Hand handAnalyzed) {
        if (!handAnalyzed.isMenzen()) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumWinningHand winningHand = RYUUIISOU;
        int fan = winningHand.getFan();
        if (TileGroup.green.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private Hand baseAnalyzeWin(Hand handIn, EnumTile extraTileIn) {
        HashMap<ArrayList<EnumTile>, EnumTile> waitDrop = new HashMap<ArrayList<EnumTile>, EnumTile>();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);
        hand.addToHandingFromGet();

        if (hand.getHandingCount() == 2) {
            if (hand.getHanding().get(0).getNormal() == hand.getHanding().get(1).getNormal()) {
                hand.eye(hand.getHanding().get(0));
                return hand;
            } else return null;
        }
        int manCount = hand.getHandingByGroup(MAN).size();
        int pinCount = hand.getHandingByGroup(PIN).size();
        int souCount = hand.getHandingByGroup(SOU).size();
        int windCount = hand.getHandingByGroup(WIND).size();
        int dragonCount = hand.getHandingByGroup(DRAGON).size();
        if ((manCount % 3 == 2 ? 1 : 0) + (pinCount % 3 == 2 ? 1 : 0) + (souCount % 3 == 2 ? 1 : 0) + (windCount % 3 == 2 ? 1 : 0) + (dragonCount % 3 == 2 ? 1 : 0) != 1) return null;
        if ((manCount % 3 == 1) || (pinCount % 3 == 1) || (souCount % 3 == 1) || (windCount % 3 == 1) || (dragonCount % 3 == 1)) return null;
        return baseAnalyzeWinGroup(hand, false);
    }

    private Hand baseAnalyzeWinGroup(Hand handIn, boolean hasEyeIn) { //hand in with no get
        boolean hasEye = hasEyeIn;
        if (handIn.getHanding().isEmpty()) return handIn;
        EnumTile tile0 = handIn.getHanding().get(0);
        Hand handOut;
        if (!hasEye && tile0.getNormal() == handIn.getHanding().get(1).getNormal()) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.eye(tile0);
            hasEye = true;
            handOut = baseAnalyzeWinGroup(hand, hasEye);
            if (handOut != null) return handOut;
        }
        if (tile0.getNormal() == handIn.getHanding().get(1).getNormal()
                && handIn.getHanding().get(1).getNormal() == handIn.getHanding().get(2).getNormal()) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.ke(tile0);
            handOut = baseAnalyzeWinGroup(hand, hasEye);
            if (handOut != null) return handOut;
        }
        if ((tile0.getNumber() <= 7) && ((tile0.getGroup() == MAN) || (tile0.getGroup() == PIN) || (tile0.getGroup() == SOU))
                && (handIn.getHanding().contains(tile0.getNext().getNormal()) || handIn.getHanding().contains(tile0.getNext().getRed()))
                && (handIn.getHanding().contains(tile0.getNext().getNext().getNormal()) || handIn.getHanding().contains(tile0.getNext().getNext().getRed()))) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.shun(tile0);
            handOut = baseAnalyzeWinGroup(hand, hasEye);
            if (handOut != null) return handOut;
        }
        return null;
    }

    private HashMap<ArrayList<EnumTile>, EnumTile> baseAnalyzeTen(Hand handIn, EnumTile extraTileIn) {
        HashMap<ArrayList<EnumTile>, EnumTile> waitDrop = new HashMap<ArrayList<EnumTile>, EnumTile>();
        EnumTile wait;

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);
        hand.addToHandingFromGet();

        int manCount = hand.getHandingByGroup(MAN).size();
        int pinCount = hand.getHandingByGroup(PIN).size();
        int souCount = hand.getHandingByGroup(SOU).size();
        int windCount = hand.getHandingByGroup(WIND).size();
        int dragonCount = hand.getHandingByGroup(DRAGON).size();
        if (((manCount % 3 == 0 ? 1 : 0) + (pinCount % 3 == 0 ? 1 : 0) + (souCount % 3 == 0 ? 1 : 0) + (windCount % 3 == 0 ? 1 : 0) + (dragonCount % 3 == 0 ? 1 : 0) < 2)) return null;
        for (EnumTile drop : hand.getHanding()) {
            hand.getHanding().remove(drop);
            for (EnumTile tile : hand.getHanding()) {
                wait = tile.getNormal();
                if (baseAnalyzeWinGroup(hand.get(wait).addToHandingFromGet(), false) != null) waitDrop.put(new ArrayList<EnumTile>(Collections.singletonList(wait)), drop);
                hand.removeFromHanding(wait);
                if (tile.getPrev() != null) wait = tile.getPrev();
                if (baseAnalyzeWinGroup(hand.get(wait).addToHandingFromGet(), false) != null) waitDrop.put(new ArrayList<EnumTile>(Collections.singletonList(wait)), drop);
                hand.removeFromHanding(wait);
                if (tile.getNext() != null) wait = tile.getNext();
                if (baseAnalyzeWinGroup(hand.get(wait).addToHandingFromGet(), false) != null) waitDrop.put(new ArrayList<EnumTile>(Collections.singletonList(wait)), drop);
                hand.removeFromHanding(wait);
            }
            hand.get(drop).addToHandingFromGet();
        }
        return waitDrop;
    }
}
