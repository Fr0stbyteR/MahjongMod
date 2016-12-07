package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.network.message.MessageMjIsInGame;
import net.fr0stbyter.mahjong.network.message.MessageMjStatus;
import net.fr0stbyter.mahjong.util.MahjongLogic.Game;
import net.fr0stbyter.mahjong.util.MahjongLogic.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class MjGameHandler {
    private HashMap<Long, Game> games;
    private HashMap<String, PlayerGameStatus> gameStatusMap;
    private HashMap<String, EntityPlayerMP> playerMPs;

    public MjGameHandler() {
        this.games = new HashMap<Long, Game>();
        this.gameStatusMap = new HashMap<String, PlayerGameStatus>();
        this.playerMPs = new HashMap<String, EntityPlayerMP>();
    }

    public void addWaitingPlayer(EntityPlayer playerIn, EnumFacing positionIn, BlockPos mjTablePos, GameType gameType) {
        long gameId = mjTablePos.toLong();
        if (games.get(gameId) == null) games.put(mjTablePos.toLong(), new Game(gameType, new MjMCUI(playerIn.getEntityWorld(), mjTablePos)));
        gameStatusMap.put(playerIn.getName(), new PlayerGameStatus(false, true, positionIn, mjTablePos.toLong()));
        playerMPs.put(playerIn.getName(), (EntityPlayerMP) playerIn);
        NetworkHandler.INSTANCE.sendTo(new MessageMjIsInGame(1, mjTablePos.toLong()), (EntityPlayerMP) playerIn);
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
            //TODO TEST ONLY
            //playerWaiting.put(playerGameStatus.getPosition().rotateY(), "A");
            //playerWaiting.put(playerGameStatus.getPosition().rotateYCCW(), "B");
            //
            if (playerWaiting.size() == targetPlayerCount) {
                for (String player1 : gameStatusMap.keySet()) {
                    if (((MjMCUI)game.getUi()).getWorld().getPlayerEntityByName(player1) == null) {
                        quitGame(player1);
                        return false;
                    }
                }
                for (String player1 : gameStatusMap.keySet()) {
                    NetworkHandler.INSTANCE.sendTo(new MessageMjIsInGame(2, gameIdIn), playerMPs.get(player1));
                    gameStatusMap.get(player1).setInGame(true);
                    gameStatusMap.get(player1).setWaiting(false);
                }
                HashMap<String, EnumFacing> playersIn = new HashMap<String, EnumFacing>();
                for (EnumFacing enumFacing : playerWaiting.keySet()) {
                    playersIn.put(playerWaiting.get(enumFacing), enumFacing);
                }
                game.initGame(playersIn);
                return true;
            }
        }
        return false;
    }

    public void quitGame(String playerIn) {
        gameStatusMap.remove(playerIn);
        if (playerMPs.containsKey(playerIn)) NetworkHandler.INSTANCE.sendTo(new MessageMjIsInGame(0), playerMPs.get(playerIn));
    }

    public boolean isInGame(EntityPlayer playerIn) {
        return gameStatusMap.containsKey(playerIn.getName()) && gameStatusMap.get(playerIn.getName()).isInGame();
    }

    public Game getGame(EntityPlayer playerIn) {
        if (isInGame(playerIn)) return games.get(gameStatusMap.get(playerIn.getName()).getGame());
        return null;
    }

    public void checkPlayers() {
        ArrayList<String> kick = new ArrayList<String>();
        for (String playerId : gameStatusMap.keySet()) {
            EntityPlayerMP playerMP = playerMPs.get(playerId);
            if (playerMP == null || playerMP.getEntityWorld().getPlayerEntityByName(playerId) == null) {
                if (gameStatusMap.get(playerId).isInGame()) kick.add(playerId);
                if (gameStatusMap.get(playerId).isWaiting()) gameStatusMap.get(playerId).setWaiting(false);;
            }
        }
        for (String playerId : kick) {
            games.get(gameStatusMap.get(playerId).getGame()).getUi().gameOver();
        }
    }

    public void kickPlayer(EntityPlayer player) {
        String playerId = player.getName();
        if (gameStatusMap.get(playerId).isInGame()) games.get(gameStatusMap.get(playerId).getGame()).getUi().gameOver();
        if (gameStatusMap.get(playerId).isWaiting()) gameStatusMap.get(playerId).setWaiting(false);
    }

    public HashMap<String, PlayerGameStatus> getGameStatusMap() {
        return gameStatusMap;
    }

    public HashMap<Long, Game> getGames() {
        return games;
    }

    public void removeGame(long gameId) {
        games.remove(gameId);
    }
}
