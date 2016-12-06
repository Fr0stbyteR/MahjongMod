package net.fr0stbyter.mahjong.util;

import java.util.HashMap;

public class MjPlayerHandler {
    int[] gameState; // playersCount, int round, int hand, int extra, int tilesRemaining, int riichibou
    int curPos;
    String[] playersName;
    int[] playersScore;
    HashMap<Integer, int[]> options;

    public MjPlayerHandler() {
        this.options = new HashMap<Integer, int[]>();
        int[] gameState = new int[6];
    }

    public void addOption(int option, int[] indexTiles) {
        options.put(option, indexTiles);
    }

    public void clearOptions() {
        options.clear();
    }

    public void updateState(int[] gameState, String[] playersName, int[] playersScore) {
        this.gameState = gameState;
        this.playersName = playersName;
        this.playersScore = playersScore;
    }

    public void updateCurPos(int curPos) {
        this.curPos = curPos;
    }
}
