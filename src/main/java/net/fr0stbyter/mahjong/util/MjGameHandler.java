package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.util.MahjongLogic.Game;
import net.fr0stbyter.mahjong.util.MahjongLogic.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class MjGameHandler {
    private HashMap<Long, Game> games;
    private HashMap<EntityPlayer, PlayerGameStatus> gameStatusMap;

    public MjGameHandler() {
        this.games = new HashMap<Long, Game>();
        this.gameStatusMap = new HashMap<EntityPlayer, PlayerGameStatus>();
    }

    public void addWaitingPlayer(EntityPlayer playerIn, EnumFacing positionIn, BlockPos mjTablePos, GameType gameType) {
        long gameId = mjTablePos.toLong();
        if (games.get(gameId) == null) games.put(mjTablePos.toLong(), new Game(gameType, new MjMCUI(playerIn.getEntityWorld(), mjTablePos)));
        gameStatusMap.put(playerIn, new PlayerGameStatus(false, true, positionIn, mjTablePos.toLong()));
        checkGame(gameId);
    }

    public boolean checkGame(long gameIdIn) {
        if (games.get(gameIdIn) == null) return false;
        Game game = games.get(gameIdIn);
        int targetPlayerCount = game.getGameType().getPlayerCount();
        HashMap<String, EnumFacing> playerWaiting = new HashMap<String, EnumFacing>();
        for (EntityPlayer player : gameStatusMap.keySet()) {
            PlayerGameStatus playerGameStatus = gameStatusMap.get(player);
            if (playerGameStatus.isWaiting()) playerWaiting.put(player.getDisplayNameString(), playerGameStatus.getPosition());
            ///TEST ONLY
            playerWaiting.put("A", playerGameStatus.getPosition().rotateY());
            playerWaiting.put("B", playerGameStatus.getPosition().rotateYCCW());
            if (playerWaiting.size() == targetPlayerCount) {
                game.initGame(playerWaiting);
                playerGameStatus.setInGame(true);
                playerGameStatus.setWaiting(false);
                return true;
            }
        }
        return false;
    }

    public boolean isInGame(EntityPlayer playerIn) {
        return gameStatusMap.containsKey(playerIn) && gameStatusMap.get(playerIn).isInGame();
    }

    public Game getGame(EntityPlayer playerIn) {
        if (isInGame(playerIn)) return games.get(gameStatusMap.get(playerIn).getGame());
        return null;
    }
    public HashMap<EntityPlayer, PlayerGameStatus> getGameStatusMap() {
        return gameStatusMap;
    }

    public HashMap<Long, Game> getGames() {
        return games;
    }
}
