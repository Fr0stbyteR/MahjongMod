package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private GameState gameState;
    private GameType gameType;
    private ArrayList<Player> players;
    private River river;
    private Mountain mountain;
    private int[] dices;
    public Game(ArrayList<String> playersIdIn, GameType gameTypeIn) {
        gameType = gameTypeIn;
        gameState = new GameState();
        players = new ArrayList<Player>();
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
        if (gameTypeIn.getPlayerCount() == 4) players.add(new Player(this, playersIdIn.get(0), EnumPosition.NORTH));
        playersIdIn.remove(0);
        // create mountain
        mountain = new Mountain(gameTypeIn);
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
        players.get(0).getTile(mountain.getNextThenRemove());
        // wait discard
    }
}
