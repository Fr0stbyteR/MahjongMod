package net.fr0stbyter.mahjong.items;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.blocks.BlockMj;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.network.message.MessageMj;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class ItemMjOption extends Item {
    private String option;
    public ItemMjOption(String optionIn) {
        if (!optionIn.equals("ryuu")
                && !optionIn.equals("rii")
                && !optionIn.equals("he")
                && !optionIn.equals("kita")
                && !optionIn.equals("chi")
                && !optionIn.equals("peng")
                && !optionIn.equals("gang")
                && !optionIn.equals("no")
                && !optionIn.equals("ok")) throw new IllegalArgumentException("Invalid option");
        option = optionIn;
        setCreativeTab(tabMahjong);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = worldIn.getBlockState(pos).getBlock();
        int indexTile = -1;
        if (block instanceof BlockMj) indexTile = ((BlockMj) block).getEnumTile().getIndex();
        if (option.equals("ryuu") && Mahjong.mjPlayerHandler.getOptions().containsKey(0)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageMj(0, -1));
            return EnumActionResult.SUCCESS;
        }
        if (option.equals("rii") && Mahjong.mjPlayerHandler.getOptions().containsKey(1)) {
            if (indexTile < 1) return EnumActionResult.FAIL;
            NetworkHandler.INSTANCE.sendToServer(new MessageMj(1, indexTile));
            return EnumActionResult.SUCCESS;
        }
        if (option.equals("kita") && Mahjong.mjPlayerHandler.getOptions().containsKey(2)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageMj(2, 34));
            return EnumActionResult.SUCCESS;
        }
        if (option.equals("gang")) {
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(3)) { //angang
                if (Mahjong.mjPlayerHandler.getOptions(3).contains(indexTile)) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageMj(3, indexTile));
                    return EnumActionResult.SUCCESS;
                }
            }
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(4)) { //gang
                if (Mahjong.mjPlayerHandler.getOptions(4).contains(indexTile)) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageMj(4, indexTile));
                    return EnumActionResult.SUCCESS;
                }
            }
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(5)) { //plusgang
                if (Mahjong.mjPlayerHandler.getOptions(5).contains(indexTile)) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageMj(5, indexTile));
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        if (option.equals("peng")) {
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(6)) {
                if (Mahjong.mjPlayerHandler.getOptions(6).contains(indexTile)) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageMj(6, indexTile));
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        if (option.equals("chi")) {
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(7)) {
                if (Mahjong.mjPlayerHandler.getOptions(7).contains(indexTile)) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageMj(7, indexTile));
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        if (option.equals("he")) {
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(8)) { //ron
                NetworkHandler.INSTANCE.sendToServer(new MessageMj(8, Mahjong.mjPlayerHandler.getOptions(8).get(0)));
                return EnumActionResult.SUCCESS;
            }
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(9)) { // tsumo
                NetworkHandler.INSTANCE.sendToServer(new MessageMj(9, Mahjong.mjPlayerHandler.getOptions(9).get(0)));
                return EnumActionResult.SUCCESS;
            }
        }
        if (option.equals("no")) {
            //TODO testonly
            NetworkHandler.INSTANCE.sendToServer(new MessageMj(10, -1));
            return EnumActionResult.SUCCESS;
/*
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(10)) {
                NetworkHandler.INSTANCE.sendToServer(new MessageMj(10, -1));
                return EnumActionResult.SUCCESS;
            }*/
        }
        if (option.equals("ok")) {
            if (Mahjong.mjPlayerHandler.getOptions().containsKey(11)) {
                NetworkHandler.INSTANCE.sendToServer(new MessageMj(11, -1));
                return EnumActionResult.SUCCESS;
            } else {
                NetworkHandler.INSTANCE.sendToServer(new MessageMj(-1, indexTile));
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
