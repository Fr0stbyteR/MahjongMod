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
    private boolean furiTen;
    private Game game;
    private Hand hand;
    private WinningHand winningHand;
    private ArrayList<EnumTile> waiting;
    private HashMap<Options, ArrayList<EnumTile>> options;

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
        furiTen = false;
        waiting = new ArrayList<EnumTile>();
        options = new HashMap<Options, ArrayList<EnumTile>>();
    }

    public Player reset() {
        hand = new Hand();
        score = 0;
        isMenzen = true;
        isRiichi = false;
        isDoubleRiichi = false;
        canIbbatsu = false;
        canRinshyan = false;
        canChyankan = false;
        isTenpai = false;
        furiTen = false;
        waiting = new ArrayList<EnumTile>();
        options = new HashMap<Options, ArrayList<EnumTile>>();
        return this;
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

    public Player nextWind() {
        curWind = game.getGameType().getPlayerCount() == 4 ? curWind.getNext() : curWind.getNextNoNorth();
        return this;
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

    public boolean getFuriTen() {
        return furiTen;
    }

    public void setFuriTen(boolean furiTenIn) {
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

    public ArrayList<RiverTile> getRiver() {
        return game.getRiver().getTilesFromPosition(curWind);
    }

    public ArrayList<EnumTile> getTilesFromRiver() {
        return River.getTiles(game.getRiver().getTilesFromPosition(curWind));
    }

    public Player getTileFromMountain() {
        if (game.getMountain().getNextProp() == MountainTile.Prop.FIXED) game.ryuukyoku(this, Game.Ryuukyoku.KOUHAIHEIKYOKU);
        getTile(game.getMountain().getNextThenRemove());
        return this;
    }

    public Player getTileFromRinshyan() {
        getTile(game.getMountain().getNextRinshyanThenRemove());
        return this;
    }

    public Player getTile(EnumTile tile) {
        hand.get(tile);
        options.clear();
        if (game.getGameState().getCurDeal() == 1) {
            int countYaochyuu = 0;
            for (EnumTile yaochyuu : TileGroup.yaochyuu) {
                if (hand.getAll().contains(yaochyuu)) countYaochyuu++;
            }
            if (countYaochyuu >= 9) options.put(Options.KYUUSHYUKYUUHAI, null);
        }
        if (waiting.contains(tile)) {
            winningHand = Analyze.analyzeWin(game.getGameType(), game.getGameState(), this, game.getMountain().getDora(), game.getMountain().getUra(), hand, null);
            if (winningHand.isWon()) {
                options.put(Options.TSUMO, null);
            }
        }
        if (!game.getGameState().isHaitei() && !hand.findAnGang().isEmpty()) {
            options.put(Options.ANGANG, hand.findAnGang());
        }
        if (!game.getGameState().isHaitei() && !hand.findPlusGang().isEmpty()) {
            options.put(Options.PLUSGANG, hand.findPlusGang());
        }
        if (game.getGameType().getPlayerCount() == 3) {
            if (hand.getHanding().contains(EnumTile.F4)) options.put(Options.KITA, null);
            else if (hand.getGet() == EnumTile.F4) options.put(Options.KITA, null);
        }
        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = analyzeRiichi();
        if (dropWait != null && !dropWait.isEmpty()) {
            options.put(Options.RIICHI, new ArrayList<EnumTile>(dropWait.keySet()));
        }
        game.getGameState().setPhase(GameState.Phase.WAIT_DISCARD);
/*        if (options.isEmpty() && isRiichi && !waiting.contains(tile)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            discard(tile);
        }*/
        return this;
    }

    public Player discard(EnumTile tile) {
        boolean hasHorizontal = false;
        if (isRiichi) {
            if (tile != hand.getGet()) return this;
            canIbbatsu = false;
            for (RiverTile riverTile : getRiver()) {
                if (riverTile.getHorizontal() && riverTile.isShown()) hasHorizontal = true;
            }
        }
        game.getGameState().setPhase(GameState.Phase.WAIT_MELD);
        game.getRiver().add(tile, curWind, tile == hand.getGet(), !hasHorizontal);
        if (tile == hand.getGet()) hand.removeGet();
        else hand.removeFromHanding(tile);
        analyzeWaiting();
        resetFuriten();
        game.checkOptions(this, tile, false);
        return this;
    }

    public Player discardforRiichi(EnumTile tile) {
        game.getGameState().setPhase(GameState.Phase.WAIT_MELD);
        game.getRiver().add(tile, curWind, tile == hand.getGet(), true);
        if (tile == hand.getGet()) hand.removeGet();
        else hand.removeFromHanding(tile);
        analyzeWaiting();
        resetFuriten();
        game.checkOptions(this, tile, false);
        return this;
    }

    public Player resetFuriten() {
        if (isRiichi) return this;
        furiTen = false;
        for (EnumTile tileWaiting : waiting) {
            if (getTilesFromRiver().contains(tileWaiting)) furiTen = true;
        }
        return this;
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

    public HashMap<Options, ArrayList<EnumTile>> getOptions() {
        return options;
    }

    public void clearOptions() {
        options.clear();
    }

    public HashMap<Options, ArrayList<EnumTile>> getOptions(Player playerIn, EnumTile tileIn, boolean isChyankanIn) { // other players discard
        options.clear();
        if (playerIn == this) return options;
        if (!furiTen && waiting.contains(tileIn)) {
            winningHand = Analyze.analyzeWin(game.getGameType(), game.getGameState(), this, game.getMountain().getDora(), game.getMountain().getUra(), hand, tileIn);
            if (winningHand.isWon()) {
                options.put(Options.RON, tileIn.toSingletonList());
            }
        }
        if (isChyankanIn) return options;
        if (game.getGameType().getPlayerCount() == 4 && getCurWind() == playerIn.getCurWind().getNext() && !hand.findChi(tileIn).isEmpty()) {
            options.put(Options.CHI, hand.findChi(tileIn));
        }
        if (hand.findPeng(tileIn)) {
            options.put(Options.PENG, tileIn.toSingletonList());
        }
        if (hand.findGang(tileIn)) {
            options.put(Options.GANG, tileIn.toSingletonList());
        }
        if (!options.isEmpty()) options.put(Options.CANCEL, null);
        return options;
    }

    public Player selectOption(Options optionIn, EnumTile tileIn, boolean isChyankanIn) {
        if (!options.containsKey(optionIn) || !options.get(optionIn).contains(tileIn)) return this;
        if (game.getGameState().getCurPlayer() != curWind) {
            HashMap<Options, EnumTile> option = new HashMap<Options, EnumTile>();
            option.put(optionIn, tileIn);
            game.selectOption(this, option, isChyankanIn);
            return this;
        }
        if (optionIn == Options.KYUUSHYUKYUUHAI) {
            if (game.getGameState().getCurDeal() == 1) {
                int countYaochyuu = 0;
                for (EnumTile yaochyuu : TileGroup.yaochyuu) {
                    if (hand.getAll().contains(yaochyuu)) countYaochyuu++;
                }
                if (countYaochyuu >= 9) game.ryuukyoku(this, Game.Ryuukyoku.KYUUSHYUKYUUHAI);
            }
        }
        if (optionIn == Options.TSUMO) tsumo(tileIn);
        if (optionIn == Options.ANGANG) angang(tileIn);
        if (optionIn == Options.PLUSGANG) plusGang(tileIn);
        if (optionIn == Options.KITA) kita(tileIn);
        if (optionIn == Options.RIICHI) riichi(tileIn);
        options.clear();
        return this;
    }

    public HashMap<EnumTile, ArrayList<EnumTile>> analyzeRiichi() {
        return Analyze.baseAnalyzeTen(hand, null);
    }

    public void kita(EnumTile tileIn) {
        options.clear();
        if (game.getGameType().getPlayerCount() != 3 || (!hand.getHanding().contains(EnumTile.F4) && hand.getGet() != EnumTile.F4)) return;
        hand.kita();
        game.checkOptions(this, EnumTile.F4, true);
        getTileFromMountain();
    }

    public void riichi(EnumTile tileIn) {
        options.clear();
        if (isRiichi) return;
        if (game.getCountRiichi() == 3) game.ryuukyoku(this, Game.Ryuukyoku.SUUCHYARIICHI);
        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = analyzeRiichi();
        if (dropWait == null || dropWait.isEmpty()) return;
        isRiichi = true;
        if (game.getGameState().getCurDeal() == 1) isDoubleRiichi = true;
        canIbbatsu = true;
        discardforRiichi(tileIn);
    }

    public void plusGang(EnumTile tileIn) {
        options.clear();
        if (game.getGameState().isHaitei() || hand.findPlusGang().isEmpty()) return;
        hand.plusGang(tileIn);
        game.getMountain().extraDora();
        if (game.getMountain().getCountDora() == 4 && hand.getCountAnGang() + hand.getCountGang() != 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        game.checkOptions(this, tileIn, true);
        getTileFromRinshyan();
    }

    public void angang(EnumTile tileIn) {
        options.clear();
        if (game.getGameState().isHaitei() || hand.findAnGang().isEmpty()) return;
        hand.anGang(tileIn);
        game.getMountain().extraDora();
        if (game.getMountain().getCountDora() == 4 && hand.getCountAnGang() + hand.getCountGang() != 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        getTileFromRinshyan();
    }

    public void tsumo(EnumTile tileIn) {
        options.clear();
        if (!waiting.contains(tileIn)) return;
        else {
            winningHand = Analyze.analyzeWin(game.getGameType(), game.getGameState(), this, game.getMountain().getDora(), game.getMountain().getUra(), hand, null);
            if (winningHand == null) return;
            if (!winningHand.isWon()) return;
        }
        game.agari(this, null, winningHand);
        //TODO
    }

    public void ron(EnumPosition curPlayerIn, EnumTile tileIn) {
        options.clear();
        if (furiTen || !waiting.contains(tileIn)) return;
        else {
            winningHand = Analyze.analyzeWin(game.getGameType(), game.getGameState(), this, game.getMountain().getDora(), game.getMountain().getUra(), hand, tileIn);
            if (winningHand == null) return;
            if (!winningHand.isWon()) return;
        }
        game.agari(this, game.getPlayer(curPlayerIn), winningHand);
        //TODO
    }

    public void gang(EnumPosition curPlayerIn, EnumTile tileIn) {
        options.clear();
        if (!hand.findGang(tileIn)) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.gang(tileGot, getCurWind().getRelation(curPlayerIn), false);
        if (game.getMountain().getCountDora() == 4 && hand.getCountAnGang() + hand.getCountGang() != 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        getTileFromRinshyan();
    }

    public void peng(EnumPosition curPlayerIn, EnumTile tileIn) {
        options.clear();
        if (!hand.findPeng(tileIn)) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.peng(tileGot, getCurWind().getRelation(curPlayerIn));
        getTileFromMountain();
    }

    public void chi(EnumPosition curPlayerIn, EnumTile tileIn) {
        options.clear();
        if (game.getGameType().getPlayerCount() != 4 || getCurWind() != curPlayerIn.getNext() || hand.findChi(tileIn).isEmpty()) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.chi(tileIn, getCurWind().getRelation(curPlayerIn), tileGot);
        getTileFromMountain();
    }

    public Player requestConfirm() {
        options.clear();
        options.put(Options.NEXT, null);
        return this;
    }

    public Player confirm() {
        game.confirm(this);
        return this;
    }

    public enum Options {KYUUSHYUKYUUHAI, RIICHI, KITA, ANGANG, GANG, PLUSGANG, PENG, CHI, RON, TSUMO, CANCEL, NEXT}
}
