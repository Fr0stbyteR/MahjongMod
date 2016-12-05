package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.blocks.BlockRiichibou;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.network.message.MessageMjPlayer;
import net.fr0stbyter.mahjong.util.MahjongLogic.*;
import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MjMCUI implements UI {
    private Game game;
    private World world;
    private BlockPos centerPos;
    private HashMap<String, EnumFacing> playerPositions;
    private static final PropertyDirection12 FACING = PropertyDirection12.create("facing");
    private final HashMap<EnumFacing, BlockPos> HANDTILES_POS = new HashMap<EnumFacing, BlockPos>();
    private final HashMap<EnumFacing, BlockPos> RIVER_POS = new HashMap<EnumFacing, BlockPos>();
    private final HashMap<EnumFacing, BlockPos> MOUNTAIN_POS = new HashMap<EnumFacing, BlockPos>();

    public MjMCUI(World world, BlockPos centerPos) {
        this.world = world;
        this.centerPos = centerPos;
        int[] htRvHeight = new int[]{0, 0, 0};/*
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(11).south(10).up(i)).getMaterial() == Material.air) break;
            else htRvHeight[0]++;
        }
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(4).south(3).up(i)).getMaterial() == Material.air) break;
            else htRvHeight[1]++;
        }
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(9).south(8).up(i)).getMaterial() == Material.air) break;
            else htRvHeight[2]++;
        }*/
        HANDTILES_POS.put(EnumFacing.EAST, centerPos.east(11).south(10).up(htRvHeight[0]));
        HANDTILES_POS.put(EnumFacing.SOUTH, centerPos.south(11).west(10).up(htRvHeight[0]));
        HANDTILES_POS.put(EnumFacing.WEST, centerPos.west(11).north(10).up(htRvHeight[0]));
        HANDTILES_POS.put(EnumFacing.NORTH, centerPos.north(11).east(10).up(htRvHeight[0]));
        RIVER_POS.put(EnumFacing.EAST, centerPos.east(4).south(3).up(htRvHeight[1]));
        RIVER_POS.put(EnumFacing.SOUTH, centerPos.south(4).west(3).up(htRvHeight[1]));
        RIVER_POS.put(EnumFacing.WEST, centerPos.west(4).north(3).up(htRvHeight[1]));
        RIVER_POS.put(EnumFacing.NORTH, centerPos.north(4).east(3).up(htRvHeight[1]));
        MOUNTAIN_POS.put(EnumFacing.EAST, centerPos.east(9).south(8).up(htRvHeight[2]));
        MOUNTAIN_POS.put(EnumFacing.SOUTH, centerPos.south(9).west(8).up(htRvHeight[2]));
        MOUNTAIN_POS.put(EnumFacing.WEST, centerPos.west(9).north(8).up(htRvHeight[2]));
        MOUNTAIN_POS.put(EnumFacing.NORTH, centerPos.north(9).east(8).up(htRvHeight[2]));
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game gameIn) {
        game = gameIn;
    }

    @Override
    public void dealOver() {
        uiClearSpace();
        uiCheckHands();
        uiCheckMountains();
        printGameState();
        printTable();
        discard(game.getCurPlayer());
    }

    @Override
    public void nextDealOver() {
        uiCheckHands();
        uiCheckRivers();
        uiCheckMountains();
        discard(game.getCurPlayer());
    }

    @Override
    public void options() {
        for (Player player : game.getPlayers()) {
            sendToPlayer(player, -1, -1);
            if (!player.getOptions().isEmpty()) {
                for (Player.Option option : player.getOptions().keySet()) {
                    sendToPlayerChat(player, "Option: " + option.name().toLowerCase() + (player.getOptions().get(option) != null ? player.getOptions().get(option) : ""));
                    if (player.getOptions().get(option) == null) sendToPlayer(player, option.ordinal(), -1);
                    else for (EnumTile enumTile : player.getOptions().get(option)) {
                        sendToPlayer(player, option.ordinal(), enumTile == null ? -1 : enumTile.getIndex());
                    }
                    //System.out.print(player.getId() + " option: " + option.name().toLowerCase() + (player.getOptions().get(option) != null ? player.getOptions().get(option) : "") + "\n");
                }
            }
        }
    }

    @Override
    public void discard(Player playerIn) {
        
    }

    @Override
    public void choose(Player playerIn, Player.Option optionIn, EnumTile enumTileIn) {
        //TODO testONLY
        if (optionIn == Player.Option.CANCEL) {
            for (Player player : game.getPlayers()) player.selectOption(optionIn, enumTileIn, playerIn.canChyankan());
        }
        else if (optionIn != null) {
            playerIn.selectOption(optionIn, enumTileIn, playerIn.canChyankan());
        }
    }

    @Override
    public void discard(Player playerIn, EnumTile tileIn) {
        if (tileIn == null) return;
        System.out.print(playerIn.getId() + " discards " + tileIn + "\n");
        playerIn.discard(tileIn);
        uiCheckHand(playerIn);
        uiCheckRiver(playerIn);
    }

    @Override
    public void choosed(Player playerIn, Player.Option optionIn) {
        Player curPlayer = game.getPlayer(game.getGameState().getCurPlayer());
        if (optionIn == Player.Option.CHI || optionIn == Player.Option.PENG || optionIn == Player.Option.GANG) uiCheckRivers();
        if (optionIn == Player.Option.KITA || optionIn == Player.Option.ANGANG || optionIn == Player.Option.PLUSGANG || optionIn == Player.Option.GANG) uiCheckMountains();
        printTable();
        uiCheckHand(curPlayer);
        discard(curPlayer);
    }

    @Override
    public void requestConfirm() {
        options();
    }

    @Override
    public void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn) {
        WinningHand winningHand = playerIn.getWinningHand();
        sendToPlayersChat("AGARI: ");
        sendToPlayersChat(playerIn.getId() + " " + playerIn.getCurWind() + " " + playerIn.getHand().toString() + (winningHand.getIsTsumo() ? "" : " +" + game.getRiver().getLast().getTile().name().toLowerCase()));
        for (AnalyzeResult analyzeResult : winningHand.getyakuList()) {
            if (analyzeResult.getHandStatus() == WinningHand.HandStatus.WIN) sendToPlayersChat(analyzeResult.getWinningHand() + " " + analyzeResult.getFan() + "fan");
        }
        sendToPlayersChat("DORA:" + game.getMountain().getDora() + "URA:" + game.getMountain().getUra());
        sendToPlayersChat(winningHand.getFu().getCount() + " fu, " + winningHand.getFan() + " fan");
        sendToPlayersChat(winningHand.getScoreLevel() + " " + winningHand.getScore());
        for (Player player : game.getPlayers()) {
            sendToPlayersChat(player.getId() + " " + player.getCurWind() + " " + (player.getScore() - scoreChangeIn.get(player)) + " " + scoreChangeIn.get(player));
        }
    }

    @Override
    public void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn) {
        sendToPlayersChat("RYUKYOKU: " + ryuukyokuIn.name());
        for (Player player : game.getPlayers()) {
            sendToPlayersChat(player.getId() + " " + player.getCurWind() + " " + player.getScore() + scoreChangeIn.get(player) + " " + scoreChangeIn.get(player));
        }
    }

    @Override
    public void showReport() {
        for (Player player : game.getPlayers()) {
            sendToPlayersChat(player.getId() + " " + player.getCurWind() + " " + player.getScore());
        }
    }

    @Override
    public void riichi(Player playerIn) {
        EnumFacing enumFacing = playerPositions.get(playerIn.getId());
        final BlockPos blockPos = centerPos.offset(enumFacing, 2);
        final IBlockState blockState = Block.getBlockFromName("mahjong:riichibou").getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName()));
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                world.setBlockState(blockPos, blockState);
            }
        });
    }

    @Override
    public void gameOver() {
        uiClearSpace();
    }

    @Override
    public void setPositions(HashMap<String, EnumFacing> playersIn) {
        playerPositions = playersIn;
    }

    @Override
    public void newDora() {
        sendToPlayersChat("ドラ: " + game.getMountain().getDora().toString());
    }


    private void printTable() {
        for (Player player : game.getPlayers()) {
            printTable(player);
        }
    }

    private void printTable(Player player) {
        System.out.println(player.getId() + " " + player.getCurWind() + " " + player.getScore() + (player.isRiichi() ? "riichi" : ""));
        System.out.println(player.getHand().toString() + "\n");
    }

    private void printGameState() {
        sendToPlayersChat(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + "局 " + game.getGameState().getCurExtra() + "本场");
        sendToPlayersChat("ドラ: " + game.getMountain().getDora().toString());
        for (Player player : game.getPlayers()) {
            for (Player player1 : game.getPlayers()) {
                sendToPlayerChat(player, player1.getId() + ":" + player1.getCurWind() + ":" + player1.getScore() + (player1.isRiichi() ? " riichi" : ""));
            }
        }
        System.out.println(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + " " + game.getGameState().getCurExtra());
    }

    private void uiCheckHands() {
        for (Player player : game.getPlayers()) {
            uiCheckHand(player);
        }
    }

    private void uiCheckHand(Player playerIn) {
        EnumFacing enumFacing = playerPositions.get(playerIn.getId());
        BlockPos htPos = HANDTILES_POS.get(enumFacing);
        final HashMap<BlockPos, IBlockState> blockStates = new HashMap<BlockPos, IBlockState>();
        for (int i = 0; i < 21; i++) {
            blockStates.put(HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i), Blocks.air.getDefaultState());
            if (i == 20) {
                for (int j = 1; j < 4; j++) {
                    blockStates.put(HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20).up(j), Blocks.air.getDefaultState());
                }
            }
        }
        int fuuro = 0;
        int kita = 0;
        for (HandTiles handTiles : playerIn.getHand().getTiles()) {
            if (handTiles instanceof Handing) {
                for (EnumTile enumTile : handTiles.getTiles()) {
                    Block block = Block.getBlockFromName("mahjong:mj" + enumTile.name().toLowerCase());
                    IBlockState blockState = block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName()));
                    blockStates.put(htPos, blockState);
                    htPos = htPos.offset(enumFacing.rotateYCCW(), 1);
                }
            }
            if (handTiles instanceof Get) {
                htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), playerIn.getHand().getHandingCount());
                Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTile().name().toLowerCase());
                IBlockState blockState = block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName()));
                blockStates.put(htPos, blockState);
            }
            if (handTiles instanceof Kita) {
                htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20).up(kita);
                Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTile().name().toLowerCase());
                IBlockState blockState = block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + "u"));
                blockStates.put(htPos, blockState);
                if (kita == 0) fuuro++;
                kita++;
            }
            if (handTiles instanceof AnGang) {
                for (int i = 0; i < 4; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    IBlockState blockState = block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + (i == 0 || i == 3 ? "d" : "u")));
                    blockStates.put(htPos, blockState);
                }
                fuuro += 4;
            }
            if (handTiles instanceof Gang) {
                if (((Gang) handTiles).getPlusGang()) {
                    for (int i = 0; i < 4; i++) {
                        if (i == 3) htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - 3 + handTiles.getOrientation());
                        else htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - i);
                        Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                        EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                        if (handTiles.getOrientation() == 3 - i) enumFacing12 = enumFacing12.rotateY();
                        IBlockState blockState = block.getDefaultState().withProperty(FACING, enumFacing12);
                        blockStates.put(htPos, blockState);
                    }
                    fuuro += 3;
                } else {
                    for (int i = 0; i < 4; i++) {
                        htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - i);
                        Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                        EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                        if (handTiles.getOrientation() == 1 && i == 3) enumFacing12 = enumFacing12.rotateY();
                        if (handTiles.getOrientation() == 2 && i == 1) enumFacing12 = enumFacing12.rotateY();
                        if (handTiles.getOrientation() == 3 && i == 0) enumFacing12 = enumFacing12.rotateY();
                        IBlockState blockState = block.getDefaultState().withProperty(FACING, enumFacing12);
                        blockStates.put(htPos, blockState);
                    }
                    fuuro += 4;
                }
            }
            if (handTiles instanceof Chi) {
                for (int i = 0; i < 3; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                    if (handTiles.getOrientation() == 3 - i) enumFacing12 = enumFacing12.rotateY();
                    IBlockState blockState = block.getDefaultState().withProperty(FACING, enumFacing12);
                    blockStates.put(htPos, blockState);
                }
                fuuro += 3;
            }
            if (handTiles instanceof Peng) {
                for (int i = 0; i < 3; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                    if (handTiles.getOrientation() == 3 - i) enumFacing12 = enumFacing12.rotateY();
                    IBlockState blockState = block.getDefaultState().withProperty(FACING, enumFacing12);
                    blockStates.put(htPos, blockState);
                }
                fuuro += 3;
            }
        }
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
            }
        });
    }

    private void uiCheckRivers() {
        for (Player player : game.getPlayers()) {
            uiCheckRiver(player);
        }
    }

    private void uiCheckRiver(Player playerIn) {
        EnumFacing enumFacing = playerPositions.get(playerIn.getId());
        BlockPos rvPos = RIVER_POS.get(enumFacing);
        final HashMap<BlockPos, IBlockState> blockStates = new HashMap<BlockPos, IBlockState>();
        ArrayList<RiverTile> tilesFromPosition = game.getRiver().getTilesFromPosition(playerIn.getCurWind());
        for (int i = 0; i < tilesFromPosition.size(); i++) {
            RiverTile riverTile = tilesFromPosition.get(i);
            Block block = Block.getBlockFromName("mahjong:mj" + riverTile.getTile().name().toLowerCase());
            IBlockState blockState = riverTile.getHorizontal()
                    ? block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.rotateY().getName() + "u"))
                    : block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + "u"));
            if (riverTile.isShown()) {
                if (riverTile.getWaiting()) blockStates.put(rvPos.up(1), blockState);
                else {
                    blockStates.put(rvPos, blockState);
                    blockStates.put(rvPos.up(1), Blocks.air.getDefaultState());
                }
            } else {
                blockStates.put(rvPos, Blocks.air.getDefaultState());
                blockStates.put(rvPos.up(1), Blocks.air.getDefaultState());
            }
            if (i == 5 || i == 11) {
                rvPos = rvPos.offset(enumFacing, 1);
                rvPos = rvPos.offset(enumFacing.rotateY(), 5);
            } else {
                rvPos = rvPos.offset(enumFacing.rotateYCCW(), 1);
            }
        }
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
            }
        });
    }

    private void uiCheckMountains() {
        final HashMap<BlockPos, IBlockState> blockStates = new HashMap<BlockPos, IBlockState>();
        for (EnumPosition enumPosition : EnumPosition.values()) {
            EnumFacing enumFacing = EnumFacing.byName(enumPosition.name());
            ArrayList<MountainTile> tilesD = game.getMountain().getTilesDFromPosition(enumPosition);
            ArrayList<MountainTile> tilesU = game.getMountain().getTilesUFromPosition(enumPosition);
            for (int i = 0; i < tilesD.size(); i++) {
                MountainTile tileD = tilesD.get(i);
                if (tileD.isNull()) {
                    blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i), Blocks.air.getDefaultState());
                    continue;
                }
                Block blockD = Block.getBlockFromName("mahjong:mj" + tileD.getTile().name().toLowerCase());
                IBlockState blockStateD = blockD.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + "d"));
                blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i), blockStateD);
            }
            for (int i = 0; i < tilesU.size(); i++) {
                MountainTile tileU = tilesU.get(i);
                if (tileU.isNull()) {
                    blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i).up(1), Blocks.air.getDefaultState());
                    continue;
                }
                Block blockU = Block.getBlockFromName("mahjong:mj" + tileU.getTile().name().toLowerCase());
                IBlockState blockStateU = tileU.isShown()
                        ? blockU.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + "u"))
                        : blockU.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName() + "d"));
                blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i).up(1), blockStateU);
            }
        }
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
            }
        });
    }

    private void uiUpdateBlocks(final HashMap<BlockPos, IBlockState> blockStates) {
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
            }
        });
    }

    private void sendToPlayersChat(String stringIn) {
        for (Player player : game.getPlayers()) {
            sendToPlayerChat(player, stringIn);
        }
    }

    private void sendToPlayerChat(Player playerIn, String stringIn) {
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (player != null) player.addChatComponentMessage(new TextComponentString(stringIn));
    }

    private void sendToPlayers(int optionIn, int indexTile) {
        for (Player player : game.getPlayers()) {
            sendToPlayer(player, optionIn, indexTile);
        }
    }

    private void sendToPlayer(Player playerIn, int optionIn, int indexTile) {
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (player != null) NetworkHandler.INSTANCE.sendTo(new MessageMjPlayer(optionIn, indexTile), player);
    }

    private void uiClearSpace() {
        final HashMap<BlockPos, IBlockState> blockStates = new HashMap<BlockPos, IBlockState>();
        for (EnumFacing enumFacing : HANDTILES_POS.keySet()) {
            for (int i = 0; i < 21; i++) {
                blockStates.put(HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i), Blocks.air.getDefaultState());
                blockStates.put(HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i).offset(enumFacing.getOpposite(), 1), Blocks.air.getDefaultState());
                if (i == 20) {
                    for (int j = 0; j < 4; j++) {
                        blockStates.put(HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20).up(i), Blocks.air.getDefaultState());
                    }
                }
            }
        }

        for (EnumFacing enumFacing : RIVER_POS.keySet()) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    blockStates.put(RIVER_POS.get(enumFacing).offset(enumFacing, j).offset(enumFacing.rotateYCCW(), i), Blocks.air.getDefaultState());
                    blockStates.put(RIVER_POS.get(enumFacing).offset(enumFacing, j).offset(enumFacing.rotateYCCW(), i).up(1), Blocks.air.getDefaultState());
                }
            }
            blockStates.put(RIVER_POS.get(enumFacing).offset(enumFacing, 2).offset(enumFacing.rotateYCCW(), 6), Blocks.air.getDefaultState());
            blockStates.put(RIVER_POS.get(enumFacing).offset(enumFacing, 2).offset(enumFacing.rotateYCCW(), 7), Blocks.air.getDefaultState());
        }
        for (EnumFacing enumFacing : MOUNTAIN_POS.keySet()) {
            for (int i = 0; i < 17; i++) {
                blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i), Blocks.air.getDefaultState());
                blockStates.put(MOUNTAIN_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), i).up(1), Blocks.air.getDefaultState());
            }
        }
        for (EnumFacing enumFacing : EnumFacing.HORIZONTALS) {
            blockStates.put(centerPos.offset(enumFacing, 2), Blocks.air.getDefaultState());
        }
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
            }
        });
    }
}
