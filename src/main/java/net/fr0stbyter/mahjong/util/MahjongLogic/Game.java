package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private GameState gameState;
    private GameType gameType;
    private ArrayList<Player> players;
    private River river;
    private Mountain mountain;
    private Dices dices;
    private ArrayList<Player> playersHasOptions;
    private HashMap<Player, HashMap<Player.Options, EnumTile>> optionsSelected;
    public Game(ArrayList<String> playersIdIn, GameType gameTypeIn) {
        gameType = gameTypeIn;
        gameState = new GameState(this);
        players = new ArrayList<Player>();
        river = new River();
        optionsSelected = new HashMap<Player, HashMap<Player.Options, EnumTile>>();
        dices = new Dices(2);
        playersHasOptions = new ArrayList<Player>();
        // sitting down
        // decide oya with dices
        //Collections.shuffle(playersIdIn);
        int oyaIndex = (dices.roll().getSum() - 1) % gameType.getPlayerCount(); // roll temp Oya
        oyaIndex = (oyaIndex + dices.roll().getSum() - 1) % gameType.getPlayerCount(); // Oya roll
        for (int i = 0; i < gameType.getPlayerCount(); i++) {
            players.add(new Player(this, playersIdIn.get(i), EnumPosition.getPosition(i)));
        }
        while (players.get(oyaIndex).getCurWind() != EnumPosition.EAST) {
            nextOya();
        }
        deal();
    }


    public Game deal() {
        // create mountain
        mountain = new Mountain(this);
        int openPositionIndex = (dices.roll().getSum() - 1) % gameType.getPlayerCount();
        mountain.open(EnumPosition.getPosition(openPositionIndex), dices.getSum() - 1);
        // first deal
        gameState.setPhase(GameState.Phase.DEAL);
        for (int i = 0; i < 3; i++) {
            for (Player player : players) {
                for (int j = 0; j < 4; j++) {
                    player.addToHanding(mountain.getNextThenRemove());
                }
            }
        }
        for (Player player : players) {
            player.addToHanding(mountain.getNextThenRemove());
            player.analyzeWaiting();
        }
        getPlayer(gameState.getCurPlayer()).getTileFromMountain();
        // wait discard
        return this;
    }

    public Game nextDeal() {
        river.cancelWaiting();
        gameState.nextPlayer();
        for (Player player : players) {
            player.setCanChyankan(false);
        }
        gameState.setPhase(GameState.Phase.DEAL);
        getPlayer(gameState.getCurPlayer()).getTileFromMountain();
        return this;
    }

    public ArrayList<Player> checkOptions(Player playerIn, EnumTile tileIn, boolean isChyankanIn) {
        optionsSelected.clear();
        playersHasOptions.clear();
        for (Player player : players) {
            if (player == playerIn) continue;
            if (!player.getOptions(playerIn, tileIn, isChyankanIn).isEmpty()) playersHasOptions.add(player);
        }
        if (playersHasOptions.isEmpty() && !isChyankanIn) nextDeal();
        return playersHasOptions;
    }

    public Game selectOption(Player playerIn, HashMap<Player.Options, EnumTile> optionIn, boolean isChyankanIn) {
        playerIn.clearOptions();
        if (!optionIn.containsKey(Player.Options.CANCEL)) optionsSelected.put(playerIn, optionIn);
        playersHasOptions.remove(playerIn);
        for (Player player : players) {
            player.setCanChyankan(isChyankanIn);
        }
        if (playersHasOptions.isEmpty()) {
            boolean isRon = false;
            boolean isRenchyan = false;
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Options.RON)) {
                    player.ron(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Options.RON));
                    if (player.getCurWind() == EnumPosition.EAST) isRenchyan = true;
                    isRon = true;
                }
            }
            if (isRon) {
                //TODO show bill
                nextPlay(isRenchyan);
                return this;
            }
            if (isChyankanIn) {
                nextDeal();
                return this;
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Options.GANG)) {
                    player.gang(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Options.GANG));
                    return this;
                }
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Options.PENG)) {
                    player.peng(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Options.PENG));
                    return this;
                }
            }
            for (Player player : optionsSelected.keySet()) {
                if (optionsSelected.get(player).containsKey(Player.Options.CHI)) {
                    player.chi(gameState.getCurPlayer(), optionsSelected.get(player).get(Player.Options.CHI));
                    return this;
                }
            }
        }
        nextDeal();
        return this;
    }

    private void nextPlay(boolean isRenchyanIn) {
        if (isRenchyanIn) gameState.nextExtra();
        else {
            gameState.nextHand();
            nextOya();
        }
        deal();
    }

    public void agari(Player playerIn, WinningHand winninghandIn) {
        //TODO select OK
    }

    public Game ryuukyoku(Player playerIn, Ryuukyoku ryuukyokuIn) {
        //TODO nagashimankan
        deal();
        return this;
    }

    public Game nextOya() {
        for (Player player : players) {
            player.nextWind();
        }
        return this;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameType getGameType() {
        return gameType;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(EnumPosition enumPosition) {
        for (Player player : players) {
            if (player.getCurWind() == enumPosition) return player;
        }
        return null;
    }

    public River getRiver() {
        return river;
    }

    public Mountain getMountain() {
        return mountain;
    }

    public Dices getDices() {
        return dices;
    }

    public enum Ryuukyoku {KOUHAIHEIKYOKU, KYUUSHYUKYUUHAI, SUUFONRENTA, SUUKANSANRA, SUUCHYARIICHI, SANCHYAHOU}
}
