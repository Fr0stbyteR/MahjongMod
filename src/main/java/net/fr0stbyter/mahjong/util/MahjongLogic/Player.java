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
    private HashMap<Options, EnumTile> options;

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
        options = new HashMap<Options, EnumTile>();
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
        if (!game.getGameState().isHaitei()) {
            for (EnumTile found : hand.findAnGang()) {
                options.put(Options.ANGANG, found);
            }
        }
        if (!game.getGameState().isHaitei()) {
            for (EnumTile found : hand.findPlusGang()) {
                options.put(Options.PLUSGANG, found);
            }
        }
        if (game.getGameType().getPlayerCount() == 3) {
            if (hand.getHanding().contains(EnumTile.F4)) options.put(Options.KITA, null);
            else if (hand.getGet() == EnumTile.F4) options.put(Options.KITA, null);
        }
        HashMap<EnumTile, ArrayList<EnumTile>> dropWait = Analyze.baseAnalyzeTen(hand, null);
        if (dropWait != null && !dropWait.isEmpty()) {
            for (EnumTile drop : dropWait.keySet()) {
                options.put(Options.RIICHI, drop);
            }
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
        analyzeWaiting();
        furiTen = false;
        for (EnumTile tileWaiting : waiting) {
            if (getTilesFromRiver().contains(tileWaiting)) furiTen = true;
        }
        boolean horizontal = false;
        if (isRiichi) {
            boolean hasHorizontal = false;
            for (RiverTile riverTile : getRiver()) {
                if (riverTile.getHorizontal()) hasHorizontal = true;
            }
            horizontal = !hasHorizontal;
        }
        game.getRiver().add(tile, curWind, tile == hand.getGet(), horizontal);
        return this;
    }

    public Player discardforRiichi(EnumTile tile) {

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

    public HashMap<EnumTile, ArrayList<EnumTile>> analyzeRiichi() {
        return Analyze.baseAnalyzeTen(hand, null);
    }
    public enum Options {KYUUSHYUKYUUHAI, RIICHI, KITA, ANGANG, GANG, PLUSGANG, PENG, CHI, RON, TSUMO, CANCEL}
}
