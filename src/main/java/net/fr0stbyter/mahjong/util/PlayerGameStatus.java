package net.fr0stbyter.mahjong.util;

import net.minecraft.util.EnumFacing;

public class PlayerGameStatus {
    private boolean isInGame;
    private boolean isWaiting;
    private EnumFacing position;
    private long gameID;

    public PlayerGameStatus(boolean isInGame, boolean isWaiting, EnumFacing position, long gameID) {
        this.isInGame = isInGame;
        this.isWaiting = isWaiting;
        this.position = position;
        this.gameID = gameID;
    }

    public EnumFacing getPosition() {
        return position;
    }

    public void setPosition(EnumFacing position) {
        this.position = position;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public long getGame() {
        return gameID;
    }

    public void setGame(long gameID) {
        this.gameID = gameID;
    }

}
