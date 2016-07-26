package net.fr0stbyter.mahjong.util.MahjongLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Game {
    private GameState gameState;
    private GameType gameType;
    private HashMap<EnumPosition, Player> players;
    private River river;
    private Mountain mountain;
    private int[] dices = {(int) (Math.floor(Math.random() * 6) + 1)
            , (int) (Math.floor(Math.random() * 6) + 1)
            , (int) (Math.floor(Math.random() * 6) + 1)
            , (int) (Math.floor(Math.random() * 6) + 1)};
    Game(ArrayList<Player> playersIn, GameType gameTypeIn) {
        gameType = gameTypeIn;
        Collections.shuffle(playersIn);
        players.put(EnumPosition.EAST, playersIn.get(0));
        players.put(EnumPosition.SOUTH, playersIn.get(1));
        players.put(EnumPosition.WEST, playersIn.get(2));
        if (gameTypeIn.getPlayerCount() == 4) players.put(EnumPosition.NORTH, playersIn.get(3));
        for (int i = 0; i < (dices[0] + dices[1]) % gameTypeIn.getPlayerCount(); i++) {
            EnumPosition tempOyaPosition = EnumPosition.EAST;
        }


    }
    public static void main(String[] args) {
        System.out.println("Hello World.");
    }
}
