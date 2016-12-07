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
    private boolean furiTen;
    private Game game;
    private Hand hand;
    private WinningHand winningHand;
    private ArrayList<EnumTile> waiting;
    private HashMap<Option, ArrayList<EnumTile>> options;

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
        furiTen = false;
        winningHand = null;
        waiting = new ArrayList<EnumTile>();
        options = new HashMap<Option, ArrayList<EnumTile>>();
    }

    public Player reset() {
        hand = new Hand();
        isMenzen = true;
        isRiichi = false;
        isDoubleRiichi = false;
        canIbbatsu = false;
        canRinshyan = false;
        canChyankan = false;
        furiTen = false;
        winningHand = null;
        waiting = new ArrayList<EnumTile>();
        options = new HashMap<Option, ArrayList<EnumTile>>();
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
        if (game.getMountain().getNextProp() == MountainTile.Prop.DORA) {
            game.ryuukyoku(this, Game.Ryuukyoku.KOUHAIHEIKYOKU);
            return this;
        }
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
            if (countYaochyuu >= 9) options.put(Option.KYUUSHYUKYUUHAI, null);
        }
        if (waiting.contains(tile)) {
            winningHand = Analyze.analyzeWin(game, this, game.getMountain().getDora(), game.getMountain().getUra(), hand, null);
            if (winningHand != null && winningHand.isWon()) {
                options.put(Option.TSUMO, null);
            }
        }
        if (!game.getGameState().isHaitei() && !hand.findAnGang().isEmpty()) {
            options.put(Option.ANGANG, hand.findAnGang());
        }
        if (!game.getGameState().isHaitei() && !hand.findPlusGang().isEmpty()) {
            options.put(Option.PLUSGANG, hand.findPlusGang());
        }
        if (game.getGameType().getPlayerCount() == 3) {
            if (hand.getHanding().contains(EnumTile.F4)) options.put(Option.KITA, null);
            else if (hand.getGet() == EnumTile.F4) options.put(Option.KITA, null);
        }
        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = analyzeRiichi();
        if (!isRiichi && dropWait != null && !dropWait.isEmpty() && hand.isMenzen() && score >= 1000) {
            options.put(Option.RIICHI, new ArrayList<EnumTile>(dropWait.keySet()));
        }
        game.getUi().options();
        game.checkRiichibou();
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
        if (tile == null || game.getGameState().getCurPlayer() != curWind || game.getGameState().getPhase() != GameState.Phase.WAIT_DISCARD || (!hand.getHanding().contains(tile) && tile != hand.getGet())) return null;
        boolean hasHorizontal = false;
        if (isRiichi) {
            if (tile != hand.getGet()) return this;
            canIbbatsu = false;
            for (RiverTile riverTile : getRiver()) {
                if (riverTile.getHorizontal() && riverTile.isShown()) hasHorizontal = true;
            }
        }
        canRinshyan = false;
        game.getGameState().setPhase(GameState.Phase.WAIT_MELD);
        game.getRiver().add(tile, curWind, tile == hand.getGet(), !hasHorizontal && isRiichi);
        if (tile == hand.getGet()) hand.removeGet();
        else hand.removeFromHanding(tile).addToHandingFromGet();
        analyzeWaiting();
        if (!isRiichi) resetFuriten();
        options.clear();
        game.checkOptions(this, tile, false);
        return this;
    }

    public Player discardforRiichi(EnumTile tile) {
        if (tile == null || game.getGameState().getCurPlayer() != curWind || game.getGameState().getPhase() != GameState.Phase.WAIT_DISCARD || (!hand.getHanding().contains(tile) && tile != hand.getGet())) return null;
        canRinshyan = false;
        game.getGameState().setPhase(GameState.Phase.WAIT_MELD);
        game.getRiver().add(tile, curWind, tile == hand.getGet(), true);
        if (tile == hand.getGet()) hand.removeGet();
        else hand.removeFromHanding(tile).addToHandingFromGet();
        analyzeWaiting();
        resetFuriten();
        options.clear();
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
        if (analyzeTen != null && analyzeTen.size() > 0) {
            waiting.clear();
            waiting.addAll(analyzeTen);
        }
        return this;
    }

    public HashMap<Option, ArrayList<EnumTile>> getOptions() {
        return options;
    }

    public void clearOptions() {
        options.clear();
    }

    public HashMap<Option, ArrayList<EnumTile>> getOptions(Player playerIn, EnumTile tileIn, boolean isChyankanIn) { // other players discard
        options.clear();
        if (playerIn == this) return options;
        if (!furiTen && waiting.contains(tileIn)) {
            winningHand = Analyze.analyzeWin(game, this, game.getMountain().getDora(), game.getMountain().getUra(), hand, tileIn);
            if (winningHand != null && winningHand.isWon()) {
                canChyankan = isChyankanIn;
                options.put(Option.RON, tileIn.toSingletonList());
            } else furiTen = true;
        }
        if (isChyankanIn) return options;
        if (!isRiichi && game.getGameType().getPlayerCount() == 4 && getCurWind() == playerIn.getCurWind().getNext() && !hand.findChi(tileIn).isEmpty()) {
            options.put(Option.CHI, hand.findChi(tileIn));
        }
        if (!isRiichi && hand.findPeng(tileIn)) {
            options.put(Option.PENG, tileIn.toSingletonList());
        }
        if (!isRiichi && hand.findGang(tileIn)) {
            options.put(Option.GANG, tileIn.toSingletonList());
        }
        if (!options.isEmpty()) options.put(Option.CANCEL, null);
        return options;
    }

    public Player selectOption(Option optionIn, EnumTile tileIn, boolean isChyankanIn) {
        if (!options.containsKey(optionIn) || (options.get(optionIn) != null && !options.get(optionIn).contains(tileIn))) return null;
        options.clear();
        if (optionIn == Option.NEXT) {
            confirm();
            return this;
        }
        if (game.getGameState().getCurPlayer() != curWind) {
            HashMap<Option, EnumTile> option = new HashMap<Option, EnumTile>();
            option.put(optionIn, tileIn);
            game.selectOption(this, option, isChyankanIn);
            return this;
        }
        if (optionIn == Option.KYUUSHYUKYUUHAI) {
            if (game.getGameState().getCurDeal() == 1) {
                int countYaochyuu = 0;
                for (EnumTile yaochyuu : TileGroup.yaochyuu) {
                    if (hand.getAll().contains(yaochyuu)) countYaochyuu++;
                }
                if (countYaochyuu >= 9) game.ryuukyoku(this, Game.Ryuukyoku.KYUUSHYUKYUUHAI);
            }
            return this;
        }
        if (optionIn == Option.TSUMO) {
            tsumo();
            return this;
        }
        if (optionIn == Option.ANGANG) {
            angang(tileIn);
            game.getUi().choosed(this, optionIn);
            return this;
        }
        if (optionIn == Option.PLUSGANG) {
            plusGang(tileIn);
            game.getUi().choosed(this, optionIn);
            return this;
        }
        if (optionIn == Option.KITA) {
            kita(tileIn);
            game.getUi().choosed(this, optionIn);
            return this;
        }
        if (optionIn == Option.RIICHI) {
            riichi(tileIn);
            game.getUi().choosed(this, optionIn);
            return this;
        }
        return null;
    }

    public HashMap<EnumTile, ArrayList<EnumTile>> analyzeRiichi() {
        return Analyze.baseAnalyzeTen(hand, null);
    }

    public void kita(EnumTile tileIn) {
        if (game.getGameType().getPlayerCount() != 3 || (!hand.getHanding().contains(EnumTile.F4) && hand.getGet() != EnumTile.F4)) return;
        hand.kita();
        game.checkOptions(this, EnumTile.F4, true);
        canRinshyan = true;
        getTileFromMountain();
    }

    public void riichi(EnumTile tileIn) {
        if (isRiichi || score < 1000) return;
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
        if (game.getCountGang() > 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        game.checkOptions(this, tileIn, true);
        getTileFromRinshyan();
    }

    public void angang(EnumTile tileIn) {
        if (game.getGameState().isHaitei() || hand.findAnGang().isEmpty()) return;
        hand.anGang(tileIn);
        game.getMountain().extraDora();
        if (game.getCountGang() > 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        canRinshyan = true;
        getTileFromRinshyan();
    }

    public void tsumo() {
        if (!waiting.contains(hand.getGet())) return;
        else {
            winningHand = Analyze.analyzeWin(game, this, game.getMountain().getDora(), game.getMountain().getUra(), hand, null);
            if (winningHand == null) return;
            if (!winningHand.isWon()) return;
        }
        game.agari(this, null, winningHand);
    }

    public void ron(EnumPosition curPlayerIn, EnumTile tileIn) {
        if (furiTen || !waiting.contains(tileIn)) return;
        else {
            winningHand = Analyze.analyzeWin(game, this, game.getMountain().getDora(), game.getMountain().getUra(), hand, tileIn);
            if (winningHand == null) return;
            if (!winningHand.isWon()) return;
        }
        game.agari(this, game.getPlayer(curPlayerIn), winningHand);
    }

    public void gang(EnumPosition curPlayerIn, EnumTile tileIn) {
        if (!hand.findGang(tileIn)) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.gang(tileGot, getCurWind().getRelation(curPlayerIn), false);
        game.getMountain().extraDora();
        if (game.getCountGang() > 4) {
            game.ryuukyoku(this, Game.Ryuukyoku.SUUKANSANRA);
            return;
        }
        game.checkRiichibou();
        getTileFromRinshyan();
        game.getGameState().setPhase(GameState.Phase.WAIT_DISCARD);
    }

    public void peng(EnumPosition curPlayerIn, EnumTile tileIn) {
        if (!hand.findPeng(tileIn)) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.peng(tileGot, getCurWind().getRelation(curPlayerIn));
        game.checkRiichibou();
        game.getGameState().setPhase(GameState.Phase.WAIT_DISCARD);
    }

    public void chi(EnumPosition curPlayerIn, EnumTile tileIn) {
        if (game.getGameType().getPlayerCount() != 4 || getCurWind() != curPlayerIn.getNext() || hand.findChi(tileIn).isEmpty()) return;
        EnumTile tileGot = game.getRiver().removeWaiting();
        game.getGameState().setCurPlayer(curWind).nextDeal();
        hand.chi(tileIn, getCurWind().getRelation(curPlayerIn), tileGot);
        game.checkRiichibou();
        game.getGameState().setPhase(GameState.Phase.WAIT_DISCARD);
    }

    public Player requestConfirm() {
        options.clear();
        options.put(Option.NEXT, null);
        return this;
    }

    public Player confirm() {
        game.confirm(this);
        return this;
    }

    public enum Option {
        KYUUSHYUKYUUHAI, RIICHI, KITA, ANGANG, GANG, PLUSGANG, PENG, CHI, RON, TSUMO, CANCEL, NEXT;

        public static Option getOption(String name) {
            for (Option option : Option.values()) {
                if (name.equalsIgnoreCase(option.name())) return option;
            }
            return null;
        }

        public static Option getOption(int ordinalIn) {
            if (ordinalIn < 0 || ordinalIn >= Option.values().length) {
                throw new IndexOutOfBoundsException("Invalid ordinal");
            }
            return Option.values()[ordinalIn];
        }

        public static int getOptionIndex(String name) {
            for (Option option : Option.values()) {
                if (name.equalsIgnoreCase(option.name())) return option.ordinal();
            }
            return -1;
        }
    }

}
