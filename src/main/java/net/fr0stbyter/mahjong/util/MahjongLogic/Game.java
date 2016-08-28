package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Game {
    private GameState gameState;
    private GameType gameType;
    private ArrayList<Player> players;
    private River river;
    private Mountain mountain;
    private int[] dices;
    private ArrayList<Player> playersHasOptions;
    private HashMap<Player, HashMap<Player.Options, EnumTile>> optionsSelected;
    public Game(ArrayList<String> playersIdIn, GameType gameTypeIn) {
        gameType = gameTypeIn;
        gameState = new GameState(this);
        players = new ArrayList<Player>();
        river = new River();
        optionsSelected = new HashMap<Player, HashMap<Player.Options, EnumTile>>();
        dices = new int[]{(int) (Math.floor(Math.random() * 6) + 1)
                , (int) (Math.floor(Math.random() * 6) + 1)
                , (int) (Math.floor(Math.random() * 6) + 1)
                , (int) (Math.floor(Math.random() * 6) + 1)
                , (int) (Math.floor(Math.random() * 6) + 1)
                , (int) (Math.floor(Math.random() * 6) + 1)};
        // sitting down
        // decide oya with dices
        Collections.shuffle(playersIdIn);
        int oyaIndex = 0;
        for (int i = 0; i < (dices[0] + dices[1] - 1) % gameTypeIn.getPlayerCount(); i++) { // count from Oya
            oyaIndex++;
            if (oyaIndex == gameTypeIn.getPlayerCount()) oyaIndex = 0;
        }
        for (int i = 0; i < (dices[2] + dices[3] - 1) % gameTypeIn.getPlayerCount(); i++) {
            oyaIndex++;
            if (oyaIndex == gameTypeIn.getPlayerCount()) oyaIndex = 0;
        }
        players.add(new Player(this, playersIdIn.get(oyaIndex), EnumPosition.EAST));
        playersIdIn.remove(oyaIndex);
        players.add(new Player(this, playersIdIn.get(0), EnumPosition.SOUTH));
        playersIdIn.remove(0);
        players.add(new Player(this, playersIdIn.get(0), EnumPosition.WEST));
        playersIdIn.remove(0);
        if (gameTypeIn.getPlayerCount() == 4) {
            players.add(new Player(this, playersIdIn.get(0), EnumPosition.NORTH));
            playersIdIn.remove(0);
        }
        // create mountain
        mountain = new Mountain(this);
        int openPositionIndex = 0;
        for (int i = 0; i < (dices[4] + dices[5] - 1) % gameTypeIn.getPlayerCount(); i++) { //open position
            openPositionIndex++;
            if (openPositionIndex == gameTypeIn.getPlayerCount()) openPositionIndex = 0;
        }
        mountain.open(EnumPosition.getPosition(openPositionIndex), dices[4] + dices[5] - 1);
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
    }

    public Game nextDeal() {
        river.cancelWaiting();
        gameState.nextPlayer();
        for (Player player : players) {
            player.setCanChyankan(false);
        }
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
        else gameState.nextHand();
        //TODO deal
    }

    public void agari(Player playerIn, WinningHand winninghandIn) {
        //TODO select OK
    }

    public Game ryuukyoku(Player playerIn, Ryuukyoku ryuukyokuIn) {
        //TODO nagashimankan
        //TODO redeal
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

    public int[] getDices() {
        return dices;
    }

    public enum Ryuukyoku {KOUHAIHEIKYOKU, KYUUSHYUKYUUHAI, SUUFONRENTA, SUUKANSANRA, SUUCHYARIICHI, SANCHYAHOU}
}
