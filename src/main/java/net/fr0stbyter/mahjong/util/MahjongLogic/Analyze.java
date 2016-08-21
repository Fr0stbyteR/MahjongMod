package net.fr0stbyter.mahjong.util.MahjongLogic;

import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumTileGroup.*;
import static net.fr0stbyter.mahjong.util.MahjongLogic.EnumWinningHand.*;
import static net.fr0stbyter.mahjong.util.MahjongLogic.WinningHand.HandStatus.*;

public class Analyze {
    public static WinningHand analyzeWin(GameType gameType, GameState gameState, Player player, ArrayList<EnumTile> doraIn, ArrayList<EnumTile> uraIn, Hand handIn, EnumTile extraTileIn) {
        ArrayList<Hand> hands = baseAnalyzeWin(handIn, extraTileIn);
        WinningHand winningHand = null;
        if (hands != null) {
            for (Hand handAnalyzed : hands) {
                WinningHand tempWinningHand;
                tempWinningHand = analyzeYaku(gameType, gameState, player, doraIn, uraIn, handIn, extraTileIn, handAnalyzed);
                if (winningHand == null) winningHand = tempWinningHand;
                else if (tempWinningHand.getScore() > winningHand.getScore()) winningHand = tempWinningHand;
            }
        }
        return winningHand;
    }

    private static WinningHand analyzeYaku(GameType gameType, GameState gameState, Player player, ArrayList<EnumTile> doraIn, ArrayList<EnumTile> uraIn, Hand handIn, EnumTile extraTileIn, Hand handAnalyzed) {
        boolean isMenzen = handIn.isMenzen();
        boolean isTsumo = handIn.hasGet();
        // fu
        Fu fu = Fu.analyze(gameState, player, handAnalyzed, extraTileIn, isTsumo);
        WinningHand winningHand = new WinningHand(player, handIn, extraTileIn, fu);
        // special yaku
        if (winningHand.add(gokushimusou13(handIn, extraTileIn)).isWon()) return winningHand;
        if (winningHand.add(gokushimusou(handIn, extraTileIn)).isWon()) return winningHand;
        winningHand.add(chiitoitsu(handIn, extraTileIn));
        // yakuman
        winningHand.add(suuankoudanki(handIn, extraTileIn, handAnalyzed))
                .add(suuankou(handAnalyzed))
                .add(daisangen(handAnalyzed))
                .add(tsuuiisou(handAnalyzed))
                .add(daisuushii(handAnalyzed))
                .add(shyousuushii(handAnalyzed))
                .add(ryuuiisou(handAnalyzed))
                .add(chinroutou(handAnalyzed))
                .add(suukantsu(handAnalyzed))
                .add(chyuurenpoutou9(handIn, extraTileIn))
                .add(chyuurenpoutou(handIn, extraTileIn))
                .add(tenhou(handAnalyzed, gameState))
                .add(chiihou(handAnalyzed, gameState));
        if (winningHand.isWon()) return winningHand;
        // 6fan
        winningHand.add(chiniisou(handAnalyzed))
                // 3fan
                .add(honiisou(handAnalyzed))
                .add(jyunchyantaiyaochyuu(handAnalyzed))
                .add(ryanbeekou(handAnalyzed))
                // 2fan
                .add(sanshyokudoujyun(handAnalyzed))
                .add(ikkitsuukan(handAnalyzed))
                .add(honchyantaiyaochyuu(handAnalyzed))
                .add(toitoihou(handAnalyzed))
                .add(sanankou(handAnalyzed))
                .add(honroutou(handAnalyzed))
                .add(sanshyokudoukou(handAnalyzed))
                .add(sankantsu(handAnalyzed))
                .add(shyousangen(handAnalyzed))
                .add(dabururiichi(player, handAnalyzed))
                // 1fan
                .add(riichi(player, handAnalyzed))
                .add(ibbatsu(player, handAnalyzed))
                .add(menzenchintsumohou(isTsumo, handAnalyzed))
                .add(danyaochyuu(handAnalyzed))
                .add(binhu(fu, handAnalyzed))
                .add(iibeekou(handAnalyzed))
                .add(rinshyankaihou(player, handAnalyzed))
                .add(chyankan(player, handAnalyzed))
                .add(haiteimouyue(gameState, isTsumo, handAnalyzed))
                .add(houteiraoyui(gameState, isTsumo, handAnalyzed));
        for (AnalyzeResult analyzeResult : yakuhai(gameState, player, handAnalyzed).values()) {
            winningHand.add(analyzeResult);
        }
        if (!winningHand.isWon()) return null;
        for (AnalyzeResult analyzeResult : kita(gameType, handAnalyzed)) {
            winningHand.add(analyzeResult);
        }
        for (AnalyzeResult analyzeResult : dora(doraIn, handAnalyzed)) {
            winningHand.add(analyzeResult);
        }
        for (AnalyzeResult analyzeResult : ura(uraIn, handAnalyzed)) {
            winningHand.add(analyzeResult);
        }
        return winningHand;
    }

    private static AnalyzeResult gokushimusou13(Hand handIn, EnumTile extraTileIn) {
        EnumWinningHand winningHand = GOKUSHIMUSOU13;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = new HashMap<EnumTile, ArrayList<EnumTile>>();
        EnumTile wait = null;

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);

        for (EnumTile tile : TileGroup.yaochyuu) {
            if (hand.getHanding().contains(tile)) hand.removeFromHanding(tile);
            else if (wait == null) wait = tile;
            else return new AnalyzeResult(NOTEN, null, null, 0);
        }
        if (wait == null) {
            if (TileGroup.yaochyuu.contains(hand.getGet())) return new AnalyzeResult(WIN, null, winningHand, fan); // win
            else {
                dropWait.put(hand.getGet(), TileGroup.yaochyuu);
                return new AnalyzeResult(TEN, dropWait, winningHand, fan);
            }
        }
        if (wait == hand.getGet()) {
            dropWait.put(hand.getHanding().get(0), TileGroup.yaochyuu);
            return new AnalyzeResult(TEN, dropWait, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult gokushimusou(Hand handIn, EnumTile extraTileIn) {
        EnumWinningHand winningHand = GOKUSHIMUSOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = new HashMap<EnumTile, ArrayList<EnumTile>>();
        ArrayList<EnumTile> wait = new ArrayList<EnumTile>();

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
            else if (wait.isEmpty()) wait.add(tile);
            else return new AnalyzeResult(NOTEN, null, null, 0);
        }
        if (wait.isEmpty() && (TileGroup.yaochyuu.contains(hand.getHanding().get(0)))) return new AnalyzeResult(WIN, null, winningHand, fan); // win
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(0)) && TileGroup.yaochyuu.contains(hand.getHanding().get(1))) {
            dropWait.put(hand.getHanding().get(0), wait);
            dropWait.put(hand.getHanding().get(1), wait);
            return new AnalyzeResult(TEN, dropWait, winningHand, fan);
        }
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(0))) {
            dropWait.put(hand.getHanding().get(1), wait);
            return new AnalyzeResult(TEN, dropWait, winningHand, fan);
        }
        if (TileGroup.yaochyuu.contains(hand.getHanding().get(1))) {
            dropWait.put(hand.getHanding().get(0), wait);
            return new AnalyzeResult(TEN, dropWait, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chiitoitsu(Hand handIn, EnumTile extraTileIn) {
        EnumWinningHand winningHand = CHIITOITSU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = new HashMap<EnumTile, ArrayList<EnumTile>>();
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
        dropWait.put(single.get(1), new ArrayList<EnumTile>(Collections.singletonList(single.get(0))));
        dropWait.put(single.get(0), new ArrayList<EnumTile>(Collections.singletonList(single.get(1))));
        return new AnalyzeResult(TEN, dropWait, winningHand, fan);
    }

    private static AnalyzeResult suuankoudanki(Hand handIn, EnumTile extraTileIn, Hand handAnalyzed) {
        EnumWinningHand winningHand = SUUANKOUDANKI;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if ((handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() == 4)
                && ((handIn.getGet() == handAnalyzed.getTileEye()) || (extraTileIn == handAnalyzed.getTileEye()))) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult suuankou(Hand handAnalyzed) {
        EnumWinningHand winningHand = SUUANKOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() == 4) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult daisangen(Hand handAnalyzed) {
        EnumWinningHand winningHand = DAISANGEN;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

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

    private static AnalyzeResult tsuuiisou(Hand handAnalyzed) {
        EnumWinningHand winningHand = TSUUIISOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (TileGroup.zi.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult daisuushii(Hand handAnalyzed) {
        EnumWinningHand winningHand = DAISUUSHII;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

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

    private static AnalyzeResult shyousuushii(Hand handAnalyzed) { // if not daisuushi
        EnumWinningHand winningHand = SHYOUSUUSHII;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

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

    private static AnalyzeResult ryuuiisou(Hand handAnalyzed) {
        EnumWinningHand winningHand = RYUUIISOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (TileGroup.green.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chinroutou(Hand handAnalyzed) {
        EnumWinningHand winningHand = CHINROUTOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (TileGroup.routou.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult suukantsu(Hand handAnalyzed) {
        EnumWinningHand winningHand = SUUKANTSU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountGang() == 4) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chyuurenpoutou9(Hand handIn, EnumTile extraTileIn) {
        EnumWinningHand winningHand = CHYUURENPOUTOU9;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);

        EnumTileGroup group = hand.getHanding().get(0).getGroup();
        if ((group != MAN) && (group != PIN) && (group != SOU)) return new AnalyzeResult(NOTEN, null, null, 0);
        if (hand.getHandingByGroup(group).size() != 13) return new AnalyzeResult(NOTEN, null, null, 0);
        if (!hand.getHanding().containsAll(TileGroup.getGroupByEnum(group))) return new AnalyzeResult(NOTEN, null, null, 0);
        if ((hand.getHanding().get(0).getNumber() == 1)
                && (hand.getHanding().get(1).getNumber() == 1)
                && (hand.getHanding().get(2).getNumber() == 1)
                && (hand.getHanding().get(10).getNumber() == 9)
                && (hand.getHanding().get(11).getNumber() == 9)
                && (hand.getHanding().get(12).getNumber() == 9)
                && (hand.getGet().getGroup() == group)) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chyuurenpoutou(Hand handIn, EnumTile extraTileIn) {
        EnumWinningHand winningHand = CHYUURENPOUTOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handIn.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handIn.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (extraTileIn != null && !hand.hasGet()) hand.get(extraTileIn);
        hand.addToHandingFromGet();

        EnumTileGroup group = hand.getHanding().get(0).getGroup();
        if ((group != MAN) && (group != PIN) && (group != SOU)) return new AnalyzeResult(NOTEN, null, null, 0);
        if (hand.getHandingByGroup(group).size() != 14) return new AnalyzeResult(NOTEN, null, null, 0);
        if (!hand.getHanding().containsAll(TileGroup.getGroupByEnum(group))) return new AnalyzeResult(NOTEN, null, null, 0);
        if ((hand.getHanding().get(0).getNumber() == 1)
                && (hand.getHanding().get(1).getNumber() == 1)
                && (hand.getHanding().get(2).getNumber() == 1)
                && (hand.getHanding().get(11).getNumber() == 9)
                && (hand.getHanding().get(12).getNumber() == 9)
                && (hand.getHanding().get(13).getNumber() == 9)) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult tenhou(Hand handAnalyzed, GameState gameState) {
        EnumWinningHand winningHand = TENHOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (gameState.getCurDeal() == 1 && gameState.getCurPlayer() == EnumPosition.EAST) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chiihou(Hand handAnalyzed, GameState gameState) {
        EnumWinningHand winningHand = CHIIHOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if ((gameState.getCurDeal() == 1) && (gameState.getCurPlayer() != EnumPosition.EAST)) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chiniisou(Hand handAnalyzed) {
        EnumWinningHand winningHand = CHINIISOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();


        EnumTileGroup group = handAnalyzed.getAll().get(0).getGroup();
        if ((group != MAN) && (group != PIN) && (group != SOU)) return new AnalyzeResult(NOTEN, null, null, 0);
        if (TileGroup.getGroupByEnum(group).containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult honiisou(Hand handAnalyzed) {
        EnumWinningHand winningHand = HONIISOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> tileAllowed = new ArrayList<EnumTile>();

        EnumTileGroup group = handAnalyzed.getAll().get(0).getGroup();
        if ((group != MAN) && (group != PIN) && (group != SOU)) return new AnalyzeResult(NOTEN, null, null, 0);
        tileAllowed.addAll(TileGroup.getGroupByEnum(group));
        tileAllowed.addAll(TileGroup.wind);
        tileAllowed.addAll(TileGroup.dragon);

        if (tileAllowed.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult jyunchyantaiyaochyuu(Hand handAnalyzed) {
        EnumWinningHand winningHand = JYUNCHYANTAIYAOCHYUU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> tileAllowed = new ArrayList<EnumTile>();

        tileAllowed.addAll(TileGroup.man);
        tileAllowed.addAll(TileGroup.pin);
        tileAllowed.addAll(TileGroup.sou);
        if (!tileAllowed.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(NOTEN, null, null, 0);

        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        shunTiles.addAll(handAnalyzed.getTileChi());
        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();
        keTiles.addAll(handAnalyzed.getTileKe());
        keTiles.addAll(handAnalyzed.getTilePeng());
        keTiles.addAll(handAnalyzed.getTileGang());
        keTiles.addAll(handAnalyzed.getTileAnGang());
        for (EnumTile tile : shunTiles) {
            if ((tile.getNumber() != 1) && (tile.getNumber() != 7)) return new AnalyzeResult(NOTEN, null, null, 0);
        }
        for (EnumTile tile : keTiles) {
            if ((tile.getNumber() != 1) && (tile.getNumber() != 9)) return new AnalyzeResult(NOTEN, null, null, 0);
        }
        return new AnalyzeResult(WIN, null, winningHand, fan);
    }

    private static AnalyzeResult ryanbeekou(Hand handAnalyzed) {
        EnumWinningHand winningHand = RYANBEEKOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        if (shunTiles.size() != 4) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile shun1 = shunTiles.get(0);
        shunTiles.remove(shun1);
        if (!shunTiles.remove(shun1)) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile shun2 = shunTiles.get(0);
        shunTiles.remove(shun2);
        if (!shunTiles.remove(shun2)) return new AnalyzeResult(NOTEN, null, null, 0);
        return new AnalyzeResult(WIN, null, winningHand, fan);
    }

    private static AnalyzeResult sanshyokudoujyun(Hand handAnalyzed) {
        EnumWinningHand winningHand = SANSHYOKUDOUJYUN;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        shunTiles.addAll(handAnalyzed.getTileChi());
        if (shunTiles.size() < 3) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile shun0 = shunTiles.get(0);
        if (shunTiles.contains(EnumTile.getTile(MAN, shun0.getNumber(), false))
                && shunTiles.contains(EnumTile.getTile(PIN, shun0.getNumber(), false))
                && shunTiles.contains(EnumTile.getTile(SOU, shun0.getNumber(), false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        shunTiles.remove(shun0);
        shun0 = shunTiles.get(0);
        if (shunTiles.contains(EnumTile.getTile(MAN, shun0.getNumber(), false))
                && shunTiles.contains(EnumTile.getTile(PIN, shun0.getNumber(), false))
                && shunTiles.contains(EnumTile.getTile(SOU, shun0.getNumber(), false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult ikkitsuukan(Hand handAnalyzed) {
        EnumWinningHand winningHand = IKKITSUUKAN;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();


        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        shunTiles.addAll(handAnalyzed.getTileChi());
        if (shunTiles.size() < 3) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile shun0 = shunTiles.get(0);
        if (shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 1, false))
                && shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 4, false))
                && shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 7, false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        shunTiles.remove(shun0);
        shun0 = shunTiles.get(0);
        if (shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 1, false))
                && shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 4, false))
                && shunTiles.contains(EnumTile.getTile(shun0.getGroup(), 7, false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult honchyantaiyaochyuu(Hand handAnalyzed) {
        EnumWinningHand winningHand = HONCHYANTAIYAOCHYUU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();


        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        shunTiles.addAll(handAnalyzed.getTileChi());
        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();
        keTiles.addAll(handAnalyzed.getTileKe());
        keTiles.addAll(handAnalyzed.getTilePeng());
        keTiles.addAll(handAnalyzed.getTileGang());
        keTiles.addAll(handAnalyzed.getTileAnGang());
        for (EnumTile tile : shunTiles) {
            if ((tile.getNumber() != 1) && (tile.getNumber() != 7) && !TileGroup.zi.contains(tile)) return new AnalyzeResult(NOTEN, null, null, 0);
        }
        for (EnumTile tile : keTiles) {
            if ((tile.getNumber() != 1) && (tile.getNumber() != 9) && !TileGroup.zi.contains(tile)) return new AnalyzeResult(NOTEN, null, null, 0);
        }
        return new AnalyzeResult(WIN, null, winningHand, fan);
    }

    private static AnalyzeResult toitoihou(Hand handAnalyzed) {
        EnumWinningHand winningHand = TOITOIHOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (handAnalyzed.getCountShun() + handAnalyzed.getCountChi() != 0) return new AnalyzeResult(NOTEN, null, null, 0);
        if (handAnalyzed.getCountKe() + handAnalyzed.getCountPeng() + handAnalyzed.getCountGang() + handAnalyzed.getCountAnGang() == 4) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult sanankou(Hand handAnalyzed) {
        EnumWinningHand winningHand = SANANKOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() == 3) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult honroutou(Hand handAnalyzed) {
        EnumWinningHand winningHand = HONROUTOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (TileGroup.yaochyuu.containsAll(handAnalyzed.getAll())) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult sanshyokudoukou(Hand handAnalyzed) {
        EnumWinningHand winningHand = SANSHYOKUDOUKOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();
        keTiles.addAll(handAnalyzed.getTileKe());
        keTiles.addAll(handAnalyzed.getTilePeng());
        keTiles.addAll(handAnalyzed.getTileGang());
        keTiles.addAll(handAnalyzed.getTileAnGang());
        if (keTiles.size() < 3) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile shun0 = keTiles.get(0);
        if (keTiles.contains(EnumTile.getTile(MAN, shun0.getNumber(), false))
                && keTiles.contains(EnumTile.getTile(PIN, shun0.getNumber(), false))
                && keTiles.contains(EnumTile.getTile(SOU, shun0.getNumber(), false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        keTiles.remove(shun0);
        shun0 = keTiles.get(0);
        if (keTiles.contains(EnumTile.getTile(MAN, shun0.getNumber(), false))
                && keTiles.contains(EnumTile.getTile(PIN, shun0.getNumber(), false))
                && keTiles.contains(EnumTile.getTile(SOU, shun0.getNumber(), false))) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult sankantsu(Hand handAnalyzed) {
        EnumWinningHand winningHand = SANKANTSU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountGang() == 3) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult shyousangen(Hand handAnalyzed) {
        EnumWinningHand winningHand = SHYOUSANGEN;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> keTiles = new ArrayList<EnumTile>();

        if (handAnalyzed.getCountAnGang() + handAnalyzed.getCountKe() + handAnalyzed.getCountGang() + handAnalyzed.getCountPeng() >= 2) {
            keTiles.addAll(handAnalyzed.getTileAnGang());
            keTiles.addAll(handAnalyzed.getTileGang());
            keTiles.addAll(handAnalyzed.getTilePeng());
            keTiles.addAll(handAnalyzed.getTileKe());
            keTiles.add(handAnalyzed.getTileEye());
            if (keTiles.containsAll(TileGroup.dragon)) return new AnalyzeResult(WIN, null, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult dabururiichi(Player player, Hand handAnalyzed) {
        EnumWinningHand winningHand = DABURURIICHI;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (player.isDoubleRiichi()) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult riichi(Player player, Hand handAnalyzed) {
        EnumWinningHand winningHand = RIICHI;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (!player.isDoubleRiichi() && player.isRiichi()) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult ibbatsu(Player player, Hand handAnalyzed) {
        EnumWinningHand winningHand = IBBATSU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (player.canIbbatsu() && player.isRiichi()) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult menzenchintsumohou(boolean isTsumoIn, Hand handAnalyzed) {
        EnumWinningHand winningHand = MENZENCHINTSUMOHOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (isTsumoIn) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult danyaochyuu(Hand handAnalyzed) {
        EnumWinningHand winningHand = DANYAOCHYUU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        for (EnumTile tile : handAnalyzed.getAll()) {
            if (TileGroup.yaochyuu.contains(tile)) return new AnalyzeResult(NOTEN, null, null, 0);
        }
        return new AnalyzeResult(WIN, null, winningHand, fan);
    }

    private static AnalyzeResult binhu(Fu fuIn, Hand handAnalyzed) {
        EnumWinningHand winningHand = BINHU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (fuIn.getCountMentsu() != 0) return new AnalyzeResult(NOTEN, null, null, 0);
        if (fuIn.getCountEye() != 0) return new AnalyzeResult(NOTEN, null, null, 0);
        if (fuIn.getCountMachi() != 0) return new AnalyzeResult(NOTEN, null, null, 0);
        return new AnalyzeResult(WIN, null, winningHand, fan);
    }

    private static AnalyzeResult iibeekou(Hand handAnalyzed) {
        EnumWinningHand winningHand = IIBEEKOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        ArrayList<EnumTile> shunTiles = new ArrayList<EnumTile>();
        shunTiles.addAll(handAnalyzed.getTileShun());
        if (shunTiles.size() < 2) return new AnalyzeResult(NOTEN, null, null, 0);
        EnumTile tile0;
        while (shunTiles.size() < 2) {
            tile0 = shunTiles.get(0);
            shunTiles.remove(tile0);
            if (shunTiles.contains(tile0)) return new AnalyzeResult(WIN, null, winningHand, fan);
        }
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static HashMap<String, AnalyzeResult> yakuhai(GameState gameState, Player player, Hand handAnalyzed) {
        HashMap<String, AnalyzeResult> result = new HashMap<String, AnalyzeResult>();
        EnumWinningHand winningHand = YAKUHAI;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return result;
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        String roundWindName = gameState.getCurRound().getName();
        String playerWindName = player.getCurWind().getName();

        ArrayList<EnumTile> tiles = new ArrayList<EnumTile>();
        tiles.addAll(handAnalyzed.getTileAnGang());
        tiles.addAll(handAnalyzed.getTileGang());
        tiles.addAll(handAnalyzed.getTileKe());
        tiles.addAll(handAnalyzed.getTilePeng());
        for (EnumTile tile : tiles) {
            if (roundWindName.equals(tile.getName())) result.put(roundWindName, new AnalyzeResult(WIN, null, winningHand, fan));
            if (playerWindName.equals(tile.getName())) result.put(roundWindName, new AnalyzeResult(WIN, null, winningHand, fan));
            if (TileGroup.dragon.contains(tile)) result.put(roundWindName, new AnalyzeResult(WIN, null, winningHand, fan));
        }
        return result;
    }

    private static AnalyzeResult rinshyankaihou(Player player, Hand handAnalyzed) {
        EnumWinningHand winningHand = RINSHYANKAIHOU;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (player.canRinshyan()) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult chyankan(Player player, Hand handAnalyzed) {
        EnumWinningHand winningHand = CHYANKAN;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (player.canChyankan()) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult haiteimouyue(GameState gameState, boolean isTsumo, Hand handAnalyzed) {
        EnumWinningHand winningHand = HAITEIMOUYUE;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (gameState.isHaitei() && isTsumo) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static AnalyzeResult houteiraoyui(GameState gameState, boolean isTsumo, Hand handAnalyzed) {
        EnumWinningHand winningHand = HOUTEIRAOYUI;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return new AnalyzeResult(NOTEN, null, null, 0);
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        if (gameState.isHaitei() && !isTsumo) return new AnalyzeResult(WIN, null, winningHand, fan);
        return new AnalyzeResult(NOTEN, null, null, 0);
    }

    private static ArrayList<AnalyzeResult> kita(GameType gameType, Hand handAnalyzed) {
        ArrayList<AnalyzeResult> result = new ArrayList<AnalyzeResult>();
        if (gameType.getPlayerCount() != 3) return result;
        EnumWinningHand winningHand = KITA;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return result;
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        for (int i = 0; i < handAnalyzed.getCountKita(); i++) {
            result.add(new AnalyzeResult(WIN, null, winningHand, fan));
        }
        return result;
    }

    private static ArrayList<AnalyzeResult> dora(ArrayList<EnumTile> doraIn, Hand handAnalyzed) {
        ArrayList<AnalyzeResult> result = new ArrayList<AnalyzeResult>();
        EnumWinningHand winningHand = DORA;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return result;
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();

        for (EnumTile tile : handAnalyzed.getAll()) {
            for (EnumTile dora : doraIn) {
                if (tile == dora) result.add(new AnalyzeResult(WIN, null, winningHand, fan));
            }
        }
        return result;
    }

    private static ArrayList<AnalyzeResult> ura(ArrayList<EnumTile> uraIn, Hand handAnalyzed) {
        ArrayList<AnalyzeResult> result = new ArrayList<AnalyzeResult>();
        EnumWinningHand winningHand = URA;
        int fan;
        if (winningHand.isMenZenYaku()) {
            if (handAnalyzed.isMenzen()) fan = winningHand.getFan();
            else return result;
        } else fan = handAnalyzed.isMenzen() ? winningHand.getFan() : winningHand.getNakuFan();
        for (EnumTile tile : handAnalyzed.getAll()) {
            for (EnumTile ura : uraIn) {
                if (tile == ura) result.add(new AnalyzeResult(WIN, null, winningHand, fan));
            }
        }
        return result;
    }

    public static ArrayList<Hand> baseAnalyzeWin(Hand handIn, EnumTile extraTileIn) {
        ArrayList<Hand> hands = new ArrayList<Hand>();
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
                hands.add(hand);
                return hands;
            } else return null;
        }
        int manCount = hand.getHandingByGroup(MAN).size();
        int pinCount = hand.getHandingByGroup(PIN).size();
        int souCount = hand.getHandingByGroup(SOU).size();
        int windCount = hand.getHandingByGroup(WIND).size();
        int dragonCount = hand.getHandingByGroup(DRAGON).size();
        if ((manCount % 3 == 2 ? 1 : 0) + (pinCount % 3 == 2 ? 1 : 0) + (souCount % 3 == 2 ? 1 : 0) + (windCount % 3 == 2 ? 1 : 0) + (dragonCount % 3 == 2 ? 1 : 0) != 1) return null;
        if ((manCount % 3 == 1) || (pinCount % 3 == 1) || (souCount % 3 == 1) || (windCount % 3 == 1) || (dragonCount % 3 == 1)) return null;
        Hand hand1 = baseAnalyzeWinGroup1(hand, false);
        Hand hand2 = baseAnalyzeWinGroup2(hand, false);
        Hand hand3 = baseAnalyzeWinGroup3(hand, false);
        if (hand1 != null) hands.add(hand1);
        if ((hand2 != null) && (!hand2.equals(hand1))) hands.add(hand2);
        if ((hand3 != null) && !hand3.equals(hand1) && !hand3.equals(hand2)) hands.add(hand3);
        return hands;
    }

    private static Hand baseAnalyzeWinGroup1(Hand handIn, boolean hasEyeIn) { //hand in with no get Eye>Ke>Shun
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
            handOut = baseAnalyzeWinGroup1(hand, hasEye);
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
            handOut = baseAnalyzeWinGroup1(hand, hasEye);
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
            handOut = baseAnalyzeWinGroup1(hand, hasEye);
            if (handOut != null) return handOut;
        }
        return null;
    }

    private static Hand baseAnalyzeWinGroup2(Hand handIn, boolean hasEyeIn) { //hand in with no get Ke>Shun>Eye
        boolean hasEye = hasEyeIn;
        if (handIn.getHanding().isEmpty()) return handIn;
        EnumTile tile0 = handIn.getHanding().get(0);
        Hand handOut;
        if (tile0.getNormal() == handIn.getHanding().get(1).getNormal()
                && handIn.getHanding().get(1).getNormal() == handIn.getHanding().get(2).getNormal()) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.ke(tile0);
            handOut = baseAnalyzeWinGroup2(hand, hasEye);
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
            handOut = baseAnalyzeWinGroup2(hand, hasEye);
            if (handOut != null) return handOut;
        }
        if (!hasEye && tile0.getNormal() == handIn.getHanding().get(1).getNormal()) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.eye(tile0);
            hasEye = true;
            handOut = baseAnalyzeWinGroup2(hand, hasEye);
            if (handOut != null) return handOut;
        }
        return null;
    }

    private static Hand baseAnalyzeWinGroup3(Hand handIn, boolean hasEyeIn) { //hand in with no get Shun>Eye>Ke
        boolean hasEye = hasEyeIn;
        if (handIn.getHanding().isEmpty()) return handIn;
        EnumTile tile0 = handIn.getHanding().get(0);
        Hand handOut;
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
            handOut = baseAnalyzeWinGroup3(hand, hasEye);
            if (handOut != null) return handOut;
        }
        if (!hasEye && tile0.getNormal() == handIn.getHanding().get(1).getNormal()) {
            Hand hand = new Hand();
            try {
                hand = (Hand) handIn.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            hand.eye(tile0);
            hasEye = true;
            handOut = baseAnalyzeWinGroup3(hand, hasEye);
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
            handOut = baseAnalyzeWinGroup3(hand, hasEye);
            if (handOut != null) return handOut;
        }
        return null;
    }

    public static HashMap<EnumTile, ArrayList<EnumTile>> baseAnalyzeTen(Hand handIn, EnumTile extraTileIn) {
        if (extraTileIn == null && !handIn.hasGet()) return null; // 14 tiles analyze
        AnalyzeResult gokushimusou13 = gokushimusou13(handIn, extraTileIn);
        if (gokushimusou13.getHandStatus() == TEN) return gokushimusou13.getDropWait();
        AnalyzeResult gokushimusou = gokushimusou(handIn, extraTileIn);
        if (gokushimusou.getHandStatus() == TEN) return gokushimusou.getDropWait();

        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = new HashMap<EnumTile, ArrayList<EnumTile>>();
        EnumTile wait;

        AnalyzeResult chiitoitsu = chiitoitsu(handIn, extraTileIn);
        if (gokushimusou13.getHandStatus() == TEN) dropWait.putAll(chiitoitsu.getDropWait());

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

        ArrayList<EnumTile> handingUnique = new ArrayList<EnumTile>();
        ArrayList<EnumTile> dropedHandingUnique = new ArrayList<EnumTile>();
        ArrayList<EnumTile> testTiles = new ArrayList<EnumTile>();
        for (EnumTile tile : hand.getHanding()) {
            if (!handingUnique.contains(tile)) handingUnique.add(tile);
        }
        for (EnumTile drop : handingUnique) {
            hand.getHanding().remove(drop);
            dropedHandingUnique.clear();
            for (EnumTile tile : hand.getHanding()) {
                if (!dropedHandingUnique.contains(tile)) dropedHandingUnique.add(tile);
            }
            testTiles.clear();
            for (EnumTile tile : dropedHandingUnique) {
                if (tile == drop) continue;
                if (!testTiles.contains(tile.getNormal())) testTiles.add(tile.getNormal());
                if (tile.getPrev() != null && !testTiles.contains(tile.getPrev())) testTiles.add(tile.getPrev());
                if (tile.getNext() != null && !testTiles.contains(tile.getNext())) testTiles.add(tile.getNext());
            }
            for (EnumTile tile : testTiles) {
                wait = tile;
                if (baseAnalyzeWinGroup1(hand.get(wait).addToHandingFromGet(), false) != null) {
                    if (dropWait.containsKey(drop)) dropWait.get(drop).add(wait);
                    else dropWait.put(drop, new ArrayList<EnumTile>(Collections.singletonList(wait)));
                }
                hand.removeFromHanding(wait);
            }
            hand.get(drop).addToHandingFromGet();
        }
        return dropWait;
    }

    public static ArrayList<EnumTile> baseAnalyzeTen(Hand handIn) {
        if (handIn.hasGet()) return null; // 13 tiles analyze

        if (handIn.isMenzen() && handIn.getHanding().containsAll(TileGroup.yaochyuu)) return TileGroup.yaochyuu; //gsms13

        ArrayList<EnumTile> waits = new ArrayList<EnumTile>();

        int gokushimusouCount = 0; //gsms
        if (handIn.isMenzen() && TileGroup.yaochyuu.containsAll(handIn.getHanding())) {
            for (EnumTile tile : TileGroup.yaochyuu) {
                if (handIn.getHanding().contains(tile)) gokushimusouCount++;
                else if (waits.isEmpty()) waits.add(tile);
                else break;
            }
            if (gokushimusouCount == 12) return waits;
        }
        waits.clear();

        boolean chiitoitsuFlag = true; //chiitoitsu
        if (handIn.isMenzen()) {
            for (int i = 0; i < 13; i++) {
                if (handIn.getHanding().get(i) == handIn.getHanding().get(i + 1)) i++;
                else if (waits.isEmpty()) waits.add(handIn.getHanding().get(i));
                else {
                    chiitoitsuFlag = false;
                    break;
                }
            }
            if (chiitoitsuFlag) return waits;
        }
        waits.clear();

        Hand hand = new Hand();
        try {
            hand = (Hand) handIn.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        int manCount = hand.getHandingByGroup(MAN).size();
        int pinCount = hand.getHandingByGroup(PIN).size();
        int souCount = hand.getHandingByGroup(SOU).size();
        int windCount = hand.getHandingByGroup(WIND).size();
        int dragonCount = hand.getHandingByGroup(DRAGON).size();
        if (((manCount % 3 == 0 ? 1 : 0) + (pinCount % 3 == 0 ? 1 : 0) + (souCount % 3 == 0 ? 1 : 0) + (windCount % 3 == 0 ? 1 : 0) + (dragonCount % 3 == 0 ? 1 : 0) < 2)) return null;

        ArrayList<EnumTile> handingUnique = new ArrayList<EnumTile>();
        ArrayList<EnumTile> testTiles = new ArrayList<EnumTile>();
        for (EnumTile tile : hand.getHanding()) {
            if (!handingUnique.contains(tile)) handingUnique.add(tile);
        }
        for (EnumTile tile : handingUnique) {
            if (!testTiles.contains(tile.getNormal())) testTiles.add(tile.getNormal());
            if (tile.getPrev() != null && !testTiles.contains(tile.getPrev())) testTiles.add(tile.getPrev());
            if (tile.getNext() != null && !testTiles.contains(tile.getNext())) testTiles.add(tile.getNext());
        }
        for (EnumTile wait : testTiles) {
            if (baseAnalyzeWinGroup1(hand.get(wait).addToHandingFromGet(), false) != null) waits.add(wait);
            hand.removeFromHanding(wait);
        }
        return waits;
    }

}
