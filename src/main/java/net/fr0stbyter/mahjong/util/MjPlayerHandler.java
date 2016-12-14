package net.fr0stbyter.mahjong.util;

import java.util.HashMap;

public class MjPlayerHandler {
    private int isInGame;
    private String gameId;
    private int[] gameState; // playersCount, int round, int hand, int extra, int length, int riichibou
    private int curPos;
    private String[] playersName;
    private int[] playersScore;
    private HashMap<Integer, int[]> options;
    private boolean furiTen;

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

    public void setGame(int isInGame, String gameId) {
        this.isInGame = isInGame;
        this.gameId = gameId;
    }

    public int getIsInGame() {
        return isInGame;
    }

    public String getGameId() {
        return gameId;
    }

    public int[] getGameState() {
        return gameState;
    }

    public int getCurPos() {
        return curPos;
    }

    public String[] getPlayersName() {
        return playersName;
    }

    public int[] getPlayersScore() {
        return playersScore;
    }

    public HashMap<Integer, int[]> getOptions() {
        return options;
    }

    public boolean isFuriTen() {
        return furiTen;
    }

    public void setFuriTen(boolean furiTen) {
        this.furiTen = furiTen;
    }
}
