package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.util.MahjongLogic.Game;
import net.fr0stbyter.mahjong.util.MahjongLogic.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class MjGameHandler {
    private HashMap<Long, Game> games;
    private HashMap<String, PlayerGameStatus> gameStatusMap;

    public MjGameHandler() {
        this.games = new HashMap<Long, Game>();
        this.gameStatusMap = new HashMap<String, PlayerGameStatus>();
    }

    public void addWaitingPlayer(EntityPlayer playerIn, EnumFacing positionIn, BlockPos mjTablePos, GameType gameType) {
        long gameId = mjTablePos.toLong();
        if (games.get(gameId) == null) games.put(mjTablePos.toLong(), new Game(gameType, new MjMCUI(playerIn.getEntityWorld(), mjTablePos)));
        gameStatusMap.put(playerIn.getName(), new PlayerGameStatus(false, true, positionIn, mjTablePos.toLong()));
        checkGame(gameId);
    }

    public boolean checkGame(long gameIdIn) {
        if (games.get(gameIdIn) == null) return false;
        Game game = games.get(gameIdIn);
        int targetPlayerCount = game.getGameType().getPlayerCount();
        HashMap<EnumFacing, String> playerWaiting = new HashMap<EnumFacing, String>();
        for (String player : gameStatusMap.keySet()) {
            PlayerGameStatus playerGameStatus = gameStatusMap.get(player);
            if (playerGameStatus.isWaiting() && playerGameStatus.getGame() == gameIdIn) playerWaiting.put(playerGameStatus.getPosition(), player);
            if (playerWaiting.size() == targetPlayerCount) {
                HashMap<String, EnumFacing> playersIn = new HashMap<String, EnumFacing>();
                for (EnumFacing enumFacing : playerWaiting.keySet()) {
                    playersIn.put(playerWaiting.get(enumFacing), enumFacing);
                }
                game.initGame(playersIn);
                for (String player1 : gameStatusMap.keySet()) {
                    gameStatusMap.get(player1).setInGame(true);
                    gameStatusMap.get(player1).setWaiting(false);
                }
                return true;
            }
        }
        return false;
    }

    public void quitGame(String playerIn) {
        gameStatusMap.remove(playerIn);
    }

    public boolean isInGame(EntityPlayer playerIn) {
        return gameStatusMap.containsKey(playerIn.getName()) && gameStatusMap.get(playerIn.getName()).isInGame();
    }

    public Game getGame(EntityPlayer playerIn) {
        if (isInGame(playerIn)) return games.get(gameStatusMap.get(playerIn.getName()).getGame());
        return null;
    }
    public HashMap<String, PlayerGameStatus> getGameStatusMap() {
        return gameStatusMap;
    }

    public HashMap<Long, Game> getGames() {
        return games;
    }
}
