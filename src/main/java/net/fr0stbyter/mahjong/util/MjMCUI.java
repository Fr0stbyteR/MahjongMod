package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.util.MahjongLogic.*;
import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.HandTiles;
import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.Handing;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;


public class MjMCUI implements UI {
    private Game game;
    private World world;
    private BlockPos centerPos;
    private HashMap<String, EnumFacing> playerPositions;
    private static final PropertyDirection12 FACING = PropertyDirection12.create("facing");
    private final HashMap<EnumFacing, BlockPos> HANDTILES_POS = new HashMap<EnumFacing, BlockPos>();
    private final HashMap<EnumFacing, BlockPos> RIVER_POS = new HashMap<EnumFacing, BlockPos>();

    public MjMCUI(World world, BlockPos centerPos) {
        this.world = world;
        this.centerPos = centerPos;
        HANDTILES_POS.put(EnumFacing.EAST, centerPos.east(9).south(8));
        HANDTILES_POS.put(EnumFacing.SOUTH, centerPos.south(9).west(8));
        HANDTILES_POS.put(EnumFacing.WEST, centerPos.west(9).north(8));
        HANDTILES_POS.put(EnumFacing.NORTH, centerPos.north(9).east(8));
        RIVER_POS.put(EnumFacing.EAST, centerPos.east(4).south(3));
        RIVER_POS.put(EnumFacing.SOUTH, centerPos.south(4).west(3));
        RIVER_POS.put(EnumFacing.WEST, centerPos.west(4).north(3));
        RIVER_POS.put(EnumFacing.NORTH, centerPos.north(4).east(3));
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
        uiCheckHands();
        printGameState();
        printTable();
        discard(game.getCurPlayer());
    }

    @Override
    public void nextDealOver() {

    }

    @Override
    public void options() {

    }

    @Override
    public void discard(Player playerIn) {
        HashMap<Player.Option, ArrayList<EnumTile>> options = game.getPlayer(game.getGameState().getCurPlayer()).getOptions();
        if (!options.isEmpty()) {
            for (Player.Option option : options.keySet()) {
                System.out.print("Option: " + option.name().toLowerCase() + (options.get(option) != null ? options.get(option) : "") + "\n");
            }
        }
    }

    @Override
    public void choose(Player playerIn, Player.Option option, EnumTile enumTile) {

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
    public void melded() {

    }

    @Override
    public void requestConfirm() {

    }

    @Override
    public void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn) {

    }

    @Override
    public void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn) {

    }

    @Override
    public void showReport() {

    }

    @Override
    public void riichi(Player playerIn) {

    }

    @Override
    public void gameOver() {

    }

    @Override
    public void setPositions(HashMap<String, EnumFacing> playersIn) {
        playerPositions = playersIn;
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
        for (HandTiles handTiles : playerIn.getHand().getTiles()) {
            if (handTiles instanceof Handing) {
                for (EnumTile enumTile : handTiles.getTiles()) {
                    Block block = Block.getBlockFromName("mahjong:mj" + enumTile.name().toLowerCase());
                    IBlockState blockState = block.getDefaultState().withProperty(FACING, EnumFacing12.byName(enumFacing.getName()));
                    blockStates.put(htPos, blockState);
                    htPos = htPos.offset(enumFacing.rotateYCCW(), 1);
                }
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
                else blockStates.put(rvPos, blockState);
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
}
