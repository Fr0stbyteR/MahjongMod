package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.blocks.BlockMj;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.network.message.MessageMjStatus;
import net.fr0stbyter.mahjong.tileentity.TileEntityMjTable;
import net.fr0stbyter.mahjong.util.MahjongLogic.*;
import net.fr0stbyter.mahjong.util.MahjongLogic.Hand.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MjMCUI implements UI {
    private Game game;
    private World world;
    private BlockPos centerPos;
    private HashMap<String, EnumFacing> playerPositions;
    private static final PropertyDirection12 FACING12 = PropertyDirection12.create("facing");
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final HashMap<EnumFacing, BlockPos> HANDTILES_POS = new HashMap<EnumFacing, BlockPos>();
    private final HashMap<EnumFacing, BlockPos> RIVER_POS = new HashMap<EnumFacing, BlockPos>();
    private final HashMap<EnumFacing, BlockPos> MOUNTAIN_POS = new HashMap<EnumFacing, BlockPos>();
    private ItemStack[] tempItemStack = null;
    private BlockPos riverWaitingPos;
    private Timer[] autoPlayTimer;

    public World getWorld() {
        return world;
    }

    public MjMCUI(World world, BlockPos centerPos) {
        this.world = world;
        this.centerPos = centerPos;
        int[] htRvHeight = new int[]{0, 0, 0};
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(11).south(10).up(i)).getMaterial() == Material.air) break;
            if (world.getBlockState(centerPos.east(11).south(10).up(i)).getBlock().getClass() == BlockMj.class) break;
            else htRvHeight[0]++;
        }
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(4).south(3).up(i)).getMaterial() == Material.air) break;
            if (world.getBlockState(centerPos.east(4).south(3).up(i)).getBlock().getClass() == BlockMj.class) break;
            else htRvHeight[1]++;
        }
        for (int i = 0; i < 10; i++) {
            if (world.getBlockState(centerPos.east(9).south(8).up(i)).getMaterial() == Material.air) break;
            if (world.getBlockState(centerPos.east(9).south(8).up(i)).getBlock().getClass() == BlockMj.class) break;
            else htRvHeight[2]++;
        }
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
        //registerLanguageForServer();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game gameIn) {
        game = gameIn;
        autoPlayTimer = new Timer[game.getGameType().getPlayerCount()];
        for (int i = 0; i < autoPlayTimer.length; i++) {
            autoPlayTimer[i] = new Timer();
        }
    }

    @Override
    public void dealOver() {
        //TODO test
        //testOnly();
        clearContainer();
        uiClearSpace();
        uiCheckHands();
        uiCheckMountains();
        printGameState();
        printTable();
        sendGameStateToPlayers();
        sendCurPosToPlayers();
        discard(game.getCurPlayer());
        //gameOver();
    }

    private void testOnly() {
        Hand hand = new Hand();
        hand.addToHanding(EnumTile.S3)
                .addToHanding(EnumTile.S3)
                .addToHanding(EnumTile.S3)
                .addToHanding(EnumTile.S9)
                .addToHanding(EnumTile.S9)
                .addToHanding(EnumTile.P5)
                .addToHanding(EnumTile.P6)
                .addToHanding(EnumTile.P6)
                .addToHanding(EnumTile.P7)
                .addToHanding(EnumTile.P7)
                .addToHanding(EnumTile.P7)
                .addToHanding(EnumTile.P8)
                .addToHanding(EnumTile.P9);
        for (Player player : game.getPlayers()) {
            if (!player.getId().equals("A") && !player.getId().equals("B")) {
                player.setHandTiles(hand);
                player.analyzeWaiting();
            }
        }
    }

    @Override
    public void nextDealOver() {
        uiCheckHands();
        uiCheckRivers();
        uiCheckMountains();
        sendCurPosToPlayers();
        discard(game.getCurPlayer());
    }

    @Override
    public void options() {
        sendOptionsToPlayers();
    }

    @Override
    public void discard(final Player playerIn) {
        if (playerIn.isRiichi() && playerIn.getOptions().size() == 0) { //riichi autoplay
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    discard(playerIn, playerIn.getHand().getGet());
                }
            };
            int timerNo = game.getPlayers().indexOf(playerIn);
            if (autoPlayTimer[timerNo] != null) {
                autoPlayTimer[timerNo].cancel();
                autoPlayTimer[timerNo] = new Timer();
                autoPlayTimer[timerNo].schedule(task, 1500);
            }
            return;
        }
        if (playerIn.isOffline()) { //offline autoplay
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    discard(playerIn, playerIn.getHand().getGet());
                }
            };
            int timerNo = game.getPlayers().indexOf(playerIn);
            if (autoPlayTimer[timerNo] != null) {
                autoPlayTimer[timerNo].cancel();
                autoPlayTimer[timerNo] = new Timer();
                autoPlayTimer[timerNo].schedule(task, 1500);
            }
            return;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                playerIn.setOffline(true);
                discard(playerIn, playerIn.getHand().getGet());
            }
        };
        int timerNo = game.getPlayers().indexOf(playerIn);
        if (autoPlayTimer[timerNo] != null) {
            autoPlayTimer[timerNo].cancel();
            autoPlayTimer[timerNo] = new Timer();
            autoPlayTimer[timerNo].schedule(task, 30000);
        }
        //sendToPlayersChat("Now " + playerIn.getId() + "'s turn.");
    }

    @Override
    public void chooseInt(Player playerIn, int optionIn, EnumTile enumTileIn) {
        playerIn.setOffline(false);
        boolean canChyankan = playerIn.canChyankan();
        HashMap<Player.Option, ArrayList<EnumTile>> options = playerIn.getOptions();
        if (optionIn == 0) {
            if (options.containsKey(Player.Option.NEXT)) {
                playerIn.selectOption(Player.Option.NEXT, null, canChyankan);
            }
            //TODO TEST
            //else if (enumTileIn != null) discard(game.getCurPlayer(), enumTileIn);
            else if (enumTileIn != null) discard(playerIn, enumTileIn);
            //
            else {
                for (Player.Option option : options.keySet()) {
                    if (option != Player.Option.CANCEL) {
                        if (options.containsKey(Player.Option.TSUMO)) playerIn.selectOption(Player.Option.TSUMO, null, canChyankan);
                        if (options.containsKey(Player.Option.RON)) playerIn.selectOption(Player.Option.RON, null, canChyankan);
                        playerIn.selectOption(option, options.get(option) == null || options.get(option).size() == 0 ? null : options.get(option).get(0), canChyankan);
                        return;
                    }
                }
            }
            return;
        }
        if (optionIn == 1) {
            //TODO TEST
            //for (Player player : game.getPlayers()) player.selectOption(Player.Option.CANCEL, enumTileIn, playerIn.canChyankan());
            if (!options.containsKey(Player.Option.CANCEL)) return;
            playerIn.selectOption(Player.Option.CANCEL, null, canChyankan);
            //
            return;
        }
        if (optionIn == 2) {
            if (!options.containsKey(Player.Option.KITA)) return;
            playerIn.selectOption(Player.Option.KITA, EnumTile.F4, canChyankan);
            return;
        }
        if (optionIn == 3) {
            if (!options.containsKey(Player.Option.CHI) || options.get(Player.Option.CHI).size() == 0) return;
            if (enumTileIn != null && options.get(Player.Option.CHI).contains(enumTileIn)) playerIn.selectOption(Player.Option.CHI, enumTileIn, canChyankan);
            else playerIn.selectOption(Player.Option.CHI, options.get(Player.Option.CHI).get(0), canChyankan);
            return;
        }
        if (optionIn == 4) {
            if (!options.containsKey(Player.Option.PENG) || options.get(Player.Option.PENG).size() == 0) return;
            if (enumTileIn != null && options.get(Player.Option.PENG).contains(enumTileIn)) playerIn.selectOption(Player.Option.PENG, enumTileIn, canChyankan);
            else playerIn.selectOption(Player.Option.PENG, options.get(Player.Option.PENG).get(0), canChyankan);
            return;
        }
        if (optionIn == 5) {
            if (enumTileIn == null) {
                for (Player.Option option : options.keySet()) {
                    if (option == Player.Option.ANGANG || option == Player.Option.GANG || option == Player.Option.PLUSGANG) {
                        if (options.get(option) != null || options.get(option).size() > 0)
                        playerIn.selectOption(option, options.get(option).get(0), canChyankan);
                        return;
                    }
                }
            }
            if (options.containsKey(Player.Option.ANGANG) && options.get(Player.Option.ANGANG).size() > 0) {
                if (enumTileIn != null && options.get(Player.Option.ANGANG).contains(enumTileIn)) {
                    playerIn.selectOption(Player.Option.ANGANG, enumTileIn, canChyankan);
                    return;
                }
            }
            if (options.containsKey(Player.Option.GANG) && options.get(Player.Option.GANG).size() > 0) {
                if (enumTileIn != null && options.get(Player.Option.GANG).contains(enumTileIn)) {
                    playerIn.selectOption(Player.Option.GANG, enumTileIn, canChyankan);
                    return;
                }
            }
            if (options.containsKey(Player.Option.PLUSGANG) && options.get(Player.Option.PLUSGANG).size() > 0) {
                if (enumTileIn != null && options.get(Player.Option.PLUSGANG).contains(enumTileIn)) {
                    playerIn.selectOption(Player.Option.PLUSGANG, enumTileIn, canChyankan);
                    return;
                }
            }
        }
        if (optionIn == 6) {
            if (!options.containsKey(Player.Option.KYUUSHYUKYUUHAI)) return;
            playerIn.selectOption(Player.Option.KYUUSHYUKYUUHAI, null, canChyankan);
            return;
        }
        if (optionIn == 7) {
            if (!options.containsKey(Player.Option.RIICHI) || options.get(Player.Option.RIICHI).size() == 0) return;
            if (enumTileIn != null && options.get(Player.Option.RIICHI).contains(enumTileIn)) playerIn.selectOption(Player.Option.RIICHI, enumTileIn, canChyankan);
            else playerIn.selectOption(Player.Option.RIICHI, options.get(Player.Option.RIICHI).get(0), canChyankan);
            return;
        }
        if (optionIn == 8) {
            if (options.containsKey(Player.Option.RON) && options.get(Player.Option.RON).size() > 0) {
                playerIn.selectOption(Player.Option.RON, options.get(Player.Option.RON).get(0), canChyankan);
                return;
            }
            if (options.containsKey(Player.Option.TSUMO)) {
                playerIn.selectOption(Player.Option.TSUMO, null, canChyankan);
                return;
            }
        }
    }

    @Override
    public void agari(Player playerIn) {
        EntityLightningBolt entitybolt = null;
        EnumFacing enumFacing = playerPositions.get(playerIn.getId());
        BlockPos htPos = HANDTILES_POS.get(enumFacing);
        final HashMap<BlockPos, IBlockState> blockStates = new HashMap<BlockPos, IBlockState>();
        for (HandTiles handTiles : playerIn.getHand().getTiles()) {
            if (handTiles instanceof Handing) {
                for (EnumTile enumTile : handTiles.getTiles()) {
                    Block block = Block.getBlockFromName("mahjong:mj" + enumTile.name().toLowerCase());
                    IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "u"));
                    blockStates.put(htPos, blockState);
                    htPos = htPos.offset(enumFacing.rotateYCCW(), 1);
                }
            }
            if (handTiles instanceof Get) {
                htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), playerIn.getHand().getHandingCount());
                Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTile().name().toLowerCase());
                IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "u"));
                blockStates.put(htPos, blockState);
                entitybolt = new EntityLightningBolt(world, htPos.getX(), htPos.getY(), htPos.getZ(), true);
            }
        }
        if (entitybolt == null) entitybolt = new EntityLightningBolt(world, riverWaitingPos.getX(), riverWaitingPos.getY(), riverWaitingPos.getZ(), true);
        final EntityLightningBolt finalEntitybolt = entitybolt;
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                for (BlockPos blockPos : blockStates.keySet()) {
                    world.setBlockState(blockPos, blockStates.get(blockPos));
                }
                world.addWeatherEffect(finalEntitybolt);
            }
        });
    }

    @Override
    public void hosted(boolean hostedIn, Player playerIn) {
        sendToPlayersChat((hostedIn
                ? new TextComponentTranslation("gui.text.hosted")
                : new TextComponentTranslation(("gui.text.unhosted"))).appendSibling(new TextComponentString(" " + playerIn.getId())));
    }


    @Override
    public void choose(Player playerIn, Player.Option optionIn, EnumTile enumTileIn) {
 /*       //
        if (optionIn == Player.Option.CANCEL) {
            for (Player player : game.getPlayers()) player.selectOption(optionIn, enumTileIn, playerIn.canChyankan());
        }
        else if (optionIn != null) {
            playerIn.selectOption(optionIn, enumTileIn, playerIn.canChyankan());
        }*/
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
        sendToPlayersChat(
                new TextComponentString(TextFormatting.BOLD + playerIn.getId() + ":")
                .appendSibling(
                        new TextComponentTranslation("gui.option." + optionIn.name().toLowerCase())
                                .setChatStyle(new Style().setColor(TextFormatting.BOLD))));
        printTable();
        sendCurPosToPlayers();
        sendOptionsToPlayers();
        uiCheckHand(curPlayer);
        discard(curPlayer);
    }

    @Override
    public void requestConfirm() {
        options();
    }

    @Override
    public void showReport(Player playerIn, HashMap<Player, Integer> scoreChangeIn) { //agari
        WinningHand winningHand = playerIn.getWinningHand();
        ArrayList<ITextComponent> strings1 = new ArrayList<ITextComponent>();
        final ArrayList<ITextComponent> strings2 = new ArrayList<ITextComponent>();
        sendToPlayersChat(new TextComponentTranslation("gui.text.agari").setChatStyle(new Style().setBold(true).setColor(TextFormatting.GOLD)));
        sendToPlayersChat(new TextComponentTranslation("gui.position." + playerIn.getCurWind().getName().toLowerCase()).setChatStyle(new Style().setColor(TextFormatting.GOLD))
                .appendSibling(new TextComponentString(TextFormatting.WHITE + ": " + playerIn.getId() + " : " + playerIn.getHand().toString() + (winningHand.getIsTsumo() ? "" : " + " + game.getRiver().getLast().getTile().name().toLowerCase()))));
        for (AnalyzeResult analyzeResult : winningHand.getyakuList()) {
            if (analyzeResult.getHandStatus() == WinningHand.HandStatus.WIN) {
                int fan = analyzeResult.getFan();
                TextFormatting color;
                if (fan == 1) color = TextFormatting.WHITE;
                else if (fan == 2) color = TextFormatting.AQUA;
                else if (fan == 3) color = TextFormatting.GREEN;
                else color = TextFormatting.LIGHT_PURPLE;
                ITextComponent sFan = fan > 0 ? new TextComponentString(Integer.toString(fan)).setChatStyle(new Style().setColor(color).setBold(true).setItalic(true))
                        .appendSibling(new TextComponentTranslation("gui.text.fan").setChatStyle(new Style().setColor(color).setBold(true).setItalic(true)))
                        : fan == -1 ? new TextComponentTranslation("gui.text.yakuman").setChatStyle(new Style().setColor(color).setBold(true).setItalic(true))
                        : fan == -2 ? new TextComponentTranslation("gui.text.doubleyakuman").setChatStyle(new Style().setColor(color).setBold(true).setItalic(true))
                        : new TextComponentString("");
                strings1.add(sFan.appendSibling(new TextComponentString(" ")).setChatStyle(new Style().setColor(color).setBold(true).setItalic(true))
                        .appendSibling(new TextComponentTranslation("gui.winninghand." + analyzeResult.getWinningHand().name().toLowerCase()).setChatStyle(new Style().setColor(color).setBold(true).setItalic(true))));
            }
        }
        strings2.add(new TextComponentTranslation("gui.text.dora")
                .appendSibling(new TextComponentString(":" + game.getMountain().getDora() + " "))
                .appendSibling(new TextComponentTranslation("gui.text.ura"))
                .appendSibling(new TextComponentString(":"  + game.getMountain().getUra())));
        TextFormatting color = TextFormatting.WHITE;
        if (winningHand.getScoreLevel() == WinningHand.ScoreLevel.MANKAN) color = TextFormatting.AQUA;
        else if (winningHand.getScoreLevel() == WinningHand.ScoreLevel.HANEMAN) color = TextFormatting.GREEN;
        else if (winningHand.getScoreLevel() == WinningHand.ScoreLevel.BAIMAN) color = TextFormatting.RED;
        else if (winningHand.getScoreLevel() == WinningHand.ScoreLevel.SANBAIMAN) color = TextFormatting.LIGHT_PURPLE;
        else if (winningHand.getScoreLevel() == WinningHand.ScoreLevel.YAKUMAN) color = TextFormatting.GOLD;
        strings2.add(new TextComponentString(Integer.toString(winningHand.getFu().getCount())).setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color))
                .appendSibling(new TextComponentTranslation("gui.text.fu").setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color)))
                .appendSibling(new TextComponentString(Integer.toString(winningHand.getFan())).setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color)))
                .appendSibling(new TextComponentTranslation("gui.text.fan").setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color)))
                .appendSibling(new TextComponentString(" ").setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color)))
                .appendSibling(winningHand.getScoreLevel() == null
                        ? new TextComponentString("")
                        : new TextComponentTranslation("gui.scorelevel." + winningHand.getScoreLevel().name().toLowerCase()).setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color)))
                .appendSibling(new TextComponentString(color + TextFormatting.BOLD.toString() + TextFormatting.UNDERLINE + winningHand.getScore()))
                .appendSibling(new TextComponentTranslation("gui.text.points").setChatStyle(new Style().setBold(true).setUnderlined(true).setColor(color))));
        for (Player player : game.getPlayers()) {
            int change = scoreChangeIn.get(player);
            color = TextFormatting.WHITE;
            if (change < 0) color = TextFormatting.RED;
            else if (change > 0) color = TextFormatting.GREEN;
            strings2.add(new TextComponentTranslation("gui.position." + player.getCurWind().getName().toLowerCase()).setChatStyle(new Style().setColor(color))
                    .appendSibling(new TextComponentString(color + " " + player.getId() + " " + Integer.toString(player.getScore() - change) + (change == 0 ? "" : change > 0 ? " +" + change : " " + change))));
        }

        winningHand.getyakuList().size();
        Timer timer = new Timer();
        for (int i = 0; i < strings1.size(); i++) {
            final ITextComponent string = strings1.get(i);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    sendToPlayersChat(string);
                }
            };
            timer.schedule(task, (i + 1) * 750);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (ITextComponent string : strings2) sendToPlayersChat(string);
            }
        };
        timer.schedule(task, (strings1.size() + 1) * 750);
    }

    @Override
    public void showReport(Game.Ryuukyoku ryuukyokuIn, HashMap<Player, Integer> scoreChangeIn) {
        sendToPlayersChat(new TextComponentTranslation("gui.text.ryuukyoku").setChatStyle(new Style().setColor(TextFormatting.GRAY).setBold(true))
                .appendSibling(new TextComponentString(": ").setChatStyle(new Style().setColor(TextFormatting.GRAY).setBold(true)))
                .appendSibling(new TextComponentTranslation("gui.ryuukyoku." + ryuukyokuIn.name().toLowerCase()).setChatStyle(new Style().setColor(TextFormatting.GRAY).setBold(true))));
        for (Player player : game.getPlayers()) {
            int change = scoreChangeIn.get(player);
            TextFormatting color = TextFormatting.WHITE;
            if (change < 0) color = TextFormatting.RED;
            else if (change > 0) color = TextFormatting.GREEN;
            sendToPlayersChat(new TextComponentTranslation("gui.position." + player.getCurWind().getName().toLowerCase()).setChatStyle(new Style().setColor(color))
                    .appendSibling(new TextComponentString(color + " " + player.getId() + " " + Integer.toString(player.getScore() - change) + (change == 0 ? "" : change > 0 ? " +" + change : " " + change))));
        }
    }

    @Override
    public void showReport() { //when gameover
        for (Player player : game.getPlayers()) {
            TextFormatting color = TextFormatting.WHITE;
            sendToPlayersChat(new TextComponentTranslation("gui.position." + player.getCurWind().getName().toLowerCase()).setChatStyle(new Style().setColor(color))
                    .appendSibling(new TextComponentString(color + " " + player.getId() + " " + Integer.toString(player.getScore()))));
        }
    }

    @Override
    public void riichi(Player playerIn) {
        EnumFacing enumFacing = playerPositions.get(playerIn.getId());
        final BlockPos blockPos = centerPos.offset(enumFacing, 2);
        final IBlockState blockState = Block.getBlockFromName("mahjong:riichibou").getDefaultState().withProperty(FACING, enumFacing);
        world.getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                world.setBlockState(blockPos, blockState);
            }
        });
    }

    @Override
    public void gameOver() {
        for (Timer timer : autoPlayTimer) timer.cancel();
        game.getGameState().setPhase(GameState.Phase.GAME_OVER);
        uiClearSpace();
        refillContainer();
        for (Player player : game.getPlayers()) {
            Mahjong.mjGameHandler.quitGame(player.getId());
        }
        Mahjong.mjGameHandler.removeGame(this.centerPos.toLong());
    }

    @Override
    public void setPositions(HashMap<EnumFacing, String> playersIn) {
        playerPositions = new HashMap<String, EnumFacing>();
        for (EnumFacing facing : playersIn.keySet()) {
            playerPositions.put(playersIn.get(facing), facing);
        }
    }

    @Override
    public void newDora() {
        //sendToPlayersChat("ドラ: " + game.getMountain().getDora().toString());
    }


    private void printTable() {
        for (Player player : game.getPlayers()) {
            printTable(player);
        }
    }

    private void printTable(Player player) {
        //System.out.println(player.getId() + " " + player.getCurWind() + " " + player.getScore() + (player.isRiichi() ? "riichi" : ""));
        //System.out.println(player.getHand().toString() + "\n");
    }

    private void printGameState() {
        /*sendToPlayersChat(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + "局 " + game.getGameState().getCurExtra() + "本场");
        sendToPlayersChat("ドラ: " + game.getMountain().getDora().toString());
        sendToPlayersChat("里ドラ: " + game.getMountain().getDora().toString());
        for (Player player : game.getPlayers()) {
            for (Player player1 : game.getPlayers()) {
                sendToPlayerChat(player, player1.getId() + ":" + player1.getCurWind() + ":" + player1.getScore() + (player1.isRiichi() ? " riichi" : ""));
            }
        }
        System.out.println(game.getGameState().getCurRound().getName() + game.getGameState().getCurHand() + " " + game.getGameState().getCurExtra());*/
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
                    IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName()));
                    blockStates.put(htPos, blockState);
                    htPos = htPos.offset(enumFacing.rotateYCCW(), 1);
                }
            }
            if (handTiles instanceof Get) {
                htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), playerIn.getHand().getHandingCount());
                Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTile().name().toLowerCase());
                IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName()));
                blockStates.put(htPos, blockState);
            }
            if (handTiles instanceof Kita) {
                htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 20).up(kita);
                Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTile().name().toLowerCase());
                IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "u"));
                blockStates.put(htPos, blockState);
                kita++;
            }
            if (handTiles instanceof AnGang) {
                for (int i = 0; i < 4; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    IBlockState blockState = block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + (i == 0 || i == 3 ? "d" : "u")));
                    blockStates.put(htPos, blockState);
                }
                fuuro += 4;
            }
            if (handTiles instanceof Gang) {
                if (((Gang) handTiles).getPlusGang()) {
                    for (int i = 0; i < 4; i++) {
                        if (i == 3) htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - handTiles.getOrientation() + 1).offset(enumFacing.getOpposite(), 1);
                        else htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - i);
                        Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                        EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                        if (handTiles.getOrientation() == i + 1 || i == 3) enumFacing12 = enumFacing12.rotateY();
                        IBlockState blockState = block.getDefaultState().withProperty(FACING12, enumFacing12);
                        blockStates.put(htPos, blockState);
                    }
                    fuuro += 3;
                } else {
                    for (int i = 0; i < 4; i++) {
                        htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - i);
                        Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                        EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                        if (handTiles.getOrientation() == 1 && i == 0) enumFacing12 = enumFacing12.rotateY();
                        if (handTiles.getOrientation() == 2 && i == 1) enumFacing12 = enumFacing12.rotateY();
                        if (handTiles.getOrientation() == 3 && i == 3) enumFacing12 = enumFacing12.rotateY();
                        IBlockState blockState = block.getDefaultState().withProperty(FACING12, enumFacing12);
                        blockStates.put(htPos, blockState);
                    }
                    fuuro += 4;
                }
            }
            if (handTiles instanceof Chi) {
                for (int i = 0; i < 3; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                    if (i == 2) enumFacing12 = enumFacing12.rotateY();
                    IBlockState blockState = block.getDefaultState().withProperty(FACING12, enumFacing12);
                    blockStates.put(htPos, blockState);
                }
                fuuro += 3;
            }
            if (handTiles instanceof Peng) {
                for (int i = 0; i < 3; i++) {
                    htPos = HANDTILES_POS.get(enumFacing).offset(enumFacing.rotateYCCW(), 19 - fuuro - i);
                    Block block = Block.getBlockFromName("mahjong:mj" + handTiles.getTiles().get(i).name().toLowerCase());
                    EnumFacing12 enumFacing12 = EnumFacing12.byName(enumFacing.getName() + "u");
                    if (handTiles.getOrientation() == i + 1) enumFacing12 = enumFacing12.rotateY();
                    IBlockState blockState = block.getDefaultState().withProperty(FACING12, enumFacing12);
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
                    ? block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.rotateY().getName() + "u"))
                    : block.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "u"));
            if (riverTile.isShown()) {
                if (riverTile.getWaiting()) {
                    blockStates.put(rvPos.up(1), blockState);
                    riverWaitingPos = rvPos;
                }
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
                IBlockState blockStateD = blockD.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "d"));
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
                        ? blockU.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "u"))
                        : blockU.getDefaultState().withProperty(FACING12, EnumFacing12.byName(enumFacing.getName() + "d"));
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

    private void sendToPlayerChat(Player playerIn, ITextComponent chatComponent) {
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (player == null) {
            //playerIn.setOffline(true);
            return;
        }
        player.addChatComponentMessage(chatComponent);
    }

    private void sendToPlayersChat(ITextComponent chatComponent) {
        for (Player player : game.getPlayers()) {
            sendToPlayerChat(player, chatComponent);
        }
    }

    private void sendToPlayerChat(Player playerIn, String stringIn) {
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (player == null) {
            //playerIn.setOffline(true);
            return;
        }
        player.addChatComponentMessage(new TextComponentString(stringIn));
    }

    private void sendOptionsToPlayers() {
        for (Player player : game.getPlayers()) {
            sendOptionsToPlayer(player);
        }
    }

    private void sendOptionsToPlayer(final Player playerIn) {
        EntityPlayerMP playerMP = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (playerMP == null) {
            if (playerIn.getOptions().containsKey(Player.Option.CANCEL)) playerIn.selectOption(Player.Option.CANCEL, null, playerIn.canChyankan());
            else playerIn.selectOption(Player.Option.NEXT, null, playerIn.canChyankan());
            playerIn.setOffline(true);
            return;
        }
        NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(-1), playerMP);
        NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(3, playerIn.getFuriTen()), playerMP);
        if (!playerIn.getOptions().isEmpty()) {
            for (Player.Option option : playerIn.getOptions().keySet()) {
                //sendToPlayerChat(playerIn, "Option: " + option.name().toLowerCase() + (playerIn.getOptions().get(option) != null ? playerIn.getOptions().get(option) : ""));
                if (playerIn.getOptions().get(option) == null)
                    NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(2, option.ordinal(), 0, new int[0]), playerMP);
                else {
                    int[] tiles = new int[playerIn.getOptions().get(option).size()];
                    ArrayList<EnumTile> enumTiles = playerIn.getOptions().get(option);
                    for (int i = 0; i < enumTiles.size(); i++) {
                        EnumTile enumTile = enumTiles.get(i);
                        tiles[i] = enumTile == null ? -1 : enumTile.getIndex();
                    }
                    NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(2, option.ordinal(), tiles.length, tiles), playerMP);
                }
            }
        }
        TimerTask task;
        if (playerIn.isOffline()) {
            task = new TimerTask() {
                @Override
                public void run() {
                    if (playerIn.getOptions().containsKey(Player.Option.CANCEL)) playerIn.selectOption(Player.Option.CANCEL, null, playerIn.canChyankan());
                    else playerIn.selectOption(Player.Option.NEXT, null, playerIn.canChyankan());
                }
            };
        } else {
            task = new TimerTask() {
                @Override
                public void run() {
                    if (playerIn.getOptions().containsKey(Player.Option.CANCEL)) playerIn.selectOption(Player.Option.CANCEL, null, playerIn.canChyankan());
                    else playerIn.selectOption(Player.Option.NEXT, null, playerIn.canChyankan());
                    playerIn.setOffline(true);
                }
            };
        }
        int timerNo = game.getPlayers().indexOf(playerIn);
        if (autoPlayTimer[timerNo] != null) {
            autoPlayTimer[timerNo].cancel();
            autoPlayTimer[timerNo] = new Timer();
            autoPlayTimer[timerNo].schedule(task, playerIn.isOffline() ? 1500 : 20000);
        }
    }

    private void sendGameStateToPlayers() {
        for (Player player : game.getPlayers()) {
            sendGameStateToPlayer(player);
        }
    }

    private void sendGameStateToPlayer(Player playerIn) {
        EntityPlayerMP playerMP = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (playerMP == null) {
            playerIn.setOffline(true);
            return;
        }
        int[] gameState = new int[6]; // int playersCount, round, hand, extra, tilesRemaining; riichibou
        gameState[0] = game.getGameType().getPlayerCount();
        gameState[1] = game.getGameState().getCurRound().ordinal();
        gameState[2] = game.getGameState().getCurHand();
        gameState[3] = game.getGameState().getCurExtra();
        gameState[4] = game.getGameType().getLength();
        gameState[5] = game.getGameState().getCountRiichi();
        String[] players = new String[gameState[0]];
        int[] scores = new int[gameState[0]];
        for (int i = 0; i < gameState[0]; i++) {
            EnumPosition enumPosition = EnumPosition.values()[i];
            players[i] = game.getPlayer(enumPosition).getId();
            scores[i] = game.getPlayer(enumPosition).getScore();
        }
        NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(0, gameState[0], gameState[1], gameState[2], gameState[3], gameState[4], gameState[5], players, scores), playerMP);
    }

    private void sendCurPosToPlayers() {
        for (Player player : game.getPlayers()) {
            sendCurPosToPlayer(player);
        }
    }

    private void sendCurPosToPlayer(Player playerIn) {
        EntityPlayerMP playerMP = (EntityPlayerMP) world.getPlayerEntityByName(playerIn.getId());
        if (playerMP == null) {
            playerIn.setOffline(true);
            return;
        }
        NetworkHandler.INSTANCE.sendTo(new MessageMjStatus(1, game.getGameState().getCurPlayer().ordinal()), playerMP);
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

    private void clearContainer() {
        if (tempItemStack != null) return;
        TileEntity tileentity = world.getTileEntity(centerPos);
        TileEntityMjTable tableInventory;
        if (tileentity instanceof TileEntityMjTable) {
            tableInventory = (TileEntityMjTable) tileentity;
            tempItemStack = new ItemStack[tableInventory.getSizeInventory()];
            for (int i = 0; i < tableInventory.getSizeInventory(); i++) {
                tempItemStack[i] = tableInventory.getStackInSlot(i);
            }
            tableInventory.clear();
        }
    }

    private void refillContainer() {
        if (tempItemStack == null) return;
        TileEntity tileentity = world.getTileEntity(centerPos);
        TileEntityMjTable tableInventory;
        if (tileentity instanceof TileEntityMjTable) {
            tableInventory = (TileEntityMjTable) tileentity;
            for (int i = 0; i < tempItemStack.length; i++) {
                tableInventory.setInventorySlotContents(i, tempItemStack[i]);
            }
        }
    }

/*
    public static void registerLanguageForServer() {
        translations.put("gui.position.east", "东");
        translations.put("gui.position.south", "南");
        translations.put("gui.position.west", "西");
        translations.put("gui.position.north", "北");
        translations.put("gui.text.waiting", "等待中");
        translations.put("gui.text.playing", "游戏中");
        translations.put("gui.text.riichibou", "立直棒");
        translations.put("gui.text.hand", "局");
        translations.put("gui.text.extra", "本场");
        translations.put("gui.text.start", "预约");
        translations.put("gui.text.sanma", "三麻");
        translations.put("gui.text.fourp", "四麻");
        translations.put("gui.length.east", "东风战");
        translations.put("gui.length.south", "东南战");
        translations.put("gui.length.all", "一庄战");
        translations.put("gui.text.playerscount", "人数");
        translations.put("gui.text.akadora", "赤宝牌");
        translations.put("gui.text.cancelwaiting", "取消预约");
        translations.put("gui.text.stopgame", "终止游戏");
        translations.put("gui.text.tilesnotenough", "麻将牌不齐全");
        translations.put("gui.text.hosted", "托管");
        translations.put("gui.text.unhosted", "取消托管");
        translations.put("gui.option.has", "选项：");
        translations.put("gui.option.kyuushyukyuuhai", "流局");
        translations.put("gui.option.riichi", "立直");
        translations.put("gui.option.kita", "拨北");
        translations.put("gui.option.angang", "暗杠");
        translations.put("gui.option.gang", "杠");
        translations.put("gui.option.plusgang", "加杠");
        translations.put("gui.option.peng", "碰");
        translations.put("gui.option.chi", "吃");
        translations.put("gui.option.ron", "食和");
        translations.put("gui.option.tsumo", "自摸");
        translations.put("gui.option.cancel", "取消");
        translations.put("gui.option.next", "继续");
        translations.put("gui.text.agari", "和牌");
        translations.put("gui.text.fu", "符");
        translations.put("gui.text.fan", "番");
        translations.put("gui.text.yakuman", "役满");
        translations.put("gui.text.doubleyakuman", "双役满");
        translations.put("gui.text.dora", "宝牌");
        translations.put("gui.text.ura", "里宝牌");
        translations.put("gui.text.points", "分");
        translations.put("gui.text.ryuukyoku", "流局");
        translations.put("gui.tile.m1", "一万");
        translations.put("gui.tile.m2", "二万");
        translations.put("gui.tile.m3", "三万");
        translations.put("gui.tile.m4", "四万");
        translations.put("gui.tile.m5", "五万");
        translations.put("gui.tile.m5r", "赤五万");
        translations.put("gui.tile.m6", "六万");
        translations.put("gui.tile.m7", "七万");
        translations.put("gui.tile.m8", "八万");
        translations.put("gui.tile.m9", "九万");
        translations.put("gui.tile.p1", "一饼");
        translations.put("gui.tile.p2", "二饼");
        translations.put("gui.tile.p3", "三饼");
        translations.put("gui.tile.p4", "四饼");
        translations.put("gui.tile.p5", "五饼");
        translations.put("gui.tile.p5r", "赤五饼");
        translations.put("gui.tile.p6", "六饼");
        translations.put("gui.tile.p7", "七饼");
        translations.put("gui.tile.p8", "八饼");
        translations.put("gui.tile.p9", "九饼");
        translations.put("gui.tile.s1", "一索");
        translations.put("gui.tile.s2", "二索");
        translations.put("gui.tile.s3", "三索");
        translations.put("gui.tile.s4", "四索");
        translations.put("gui.tile.s5", "五索");
        translations.put("gui.tile.s5r", "赤五索");
        translations.put("gui.tile.s6", "六索");
        translations.put("gui.tile.s7", "七索");
        translations.put("gui.tile.s8", "八索");
        translations.put("gui.tile.s9", "九索");
        translations.put("gui.tile.chun", "春");
        translations.put("gui.tile.xia", "夏");
        translations.put("gui.tile.qiu", "秋");
        translations.put("gui.tile.dong", "冬");
        translations.put("gui.tile.mei", "梅");
        translations.put("gui.tile.lan", "兰");
        translations.put("gui.tile.zhu", "竹");
        translations.put("gui.tile.ju", "菊");
        translations.put("gui.tile.east", "东");
        translations.put("gui.tile.south", "南");
        translations.put("gui.tile.west", "西");
        translations.put("gui.tile.north", "北");
        translations.put("gui.tile.bai", "白");
        translations.put("gui.tile.fa", "发");
        translations.put("gui.tile.zhong", "中");
        translations.put("gui.ryuukyoku.kouhaiheikyoku", "荒牌流局");
        translations.put("gui.ryuukyoku.kyuushyukyuuhai", "九种九牌");
        translations.put("gui.ryuukyoku.suufonrenta", "四风连打");
        translations.put("gui.ryuukyoku.suukansanra", "四杠散了");
        translations.put("gui.ryuukyoku.suuchyariichi", "四家立直");
        translations.put("gui.ryuukyoku.sanchyahou", "三家和");
        translations.put("gui.ryuukyoku.nagashimankan", "流局满贯");
        translations.put("gui.scorelevel.mankan", "满贯");
        translations.put("gui.scorelevel.haneman", "跳满");
        translations.put("gui.scorelevel.baiman", "倍满");
        translations.put("gui.scorelevel.sanbaiman", "三倍满");
        translations.put("gui.scorelevel.yakuman", "役满");
        translations.put("gui.winninghand.gokushimusou", "国士无双");
        translations.put("gui.winninghand.gokushimusou13", "国士无双十三面听");
        translations.put("gui.winninghand.suuankou", "四暗刻");
        translations.put("gui.winninghand.suuankoudanki", "四暗刻单骑");
        translations.put("gui.winninghand.daisangen", "大三元");
        translations.put("gui.winninghand.tsuuiisou", "字一色");
        translations.put("gui.winninghand.shyousuushii", "小四喜");
        translations.put("gui.winninghand.daisuushii", "大四喜");
        translations.put("gui.winninghand.ryuuiisou", "绿一色");
        translations.put("gui.winninghand.chinroutou", "清老头");
        translations.put("gui.winninghand.suukantsu", "四杠子");
        translations.put("gui.winninghand.chyuurenpoutou", "九莲宝灯");
        translations.put("gui.winninghand.chyuurenpoutou9", "纯正九莲宝灯");
        translations.put("gui.winninghand.tenhou", "天和");
        translations.put("gui.winninghand.chiihou", "地和");
        translations.put("gui.winninghand.chiniisou", "清一色");
        translations.put("gui.winninghand.honiisou", "混一色");
        translations.put("gui.winninghand.jyunchyantaiyaochyuu", "纯全带幺九");
        translations.put("gui.winninghand.ryanbeekou", "两杯口");
        translations.put("gui.winninghand.sanshyokudoujyun", "三色同顺");
        translations.put("gui.winninghand.ikkitsuukan", "一气通贯");
        translations.put("gui.winninghand.honchyantaiyaochyuu", "混全带幺九");
        translations.put("gui.winninghand.chiitoitsu", "七对子");
        translations.put("gui.winninghand.toitoihou", "对对和");
        translations.put("gui.winninghand.sanankou", "三暗刻");
        translations.put("gui.winninghand.honroutou", "混老头");
        translations.put("gui.winninghand.sanshyokudoukou", "三色同刻");
        translations.put("gui.winninghand.sankantsu", "三杠子");
        translations.put("gui.winninghand.shyousangen", "小三元");
        translations.put("gui.winninghand.dabururiichi", "两立直");
        translations.put("gui.winninghand.riichi", "立直");
        translations.put("gui.winninghand.ibbatsu", "一发");
        translations.put("gui.winninghand.menzenchintsumohou", "门前清自摸和");
        translations.put("gui.winninghand.danyaochyuu", "断幺九");
        translations.put("gui.winninghand.binhu", "平和");
        translations.put("gui.winninghand.iibeekou", "一杯口");
        translations.put("gui.winninghand.yakuhai", "役牌");
        translations.put("gui.winninghand.rinshyankaihou", "岭上开花");
        translations.put("gui.winninghand.chyankan", "抢杠");
        translations.put("gui.winninghand.haiteimouyue", "海底摸月");
        translations.put("gui.winninghand.houteiraoyui", "河底捞鱼");
        translations.put("gui.winninghand.kita", "北宝牌");
        translations.put("gui.winninghand.aka", "赤宝牌");
        translations.put("gui.winninghand.dora", "宝牌");
        translations.put("gui.winninghand.ura", "里宝牌");
    }*/
}
