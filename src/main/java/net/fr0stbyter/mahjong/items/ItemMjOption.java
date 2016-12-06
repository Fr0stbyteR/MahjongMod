package net.fr0stbyter.mahjong.items;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.blocks.BlockMj;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.network.message.MessageMj;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Game;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class ItemMjOption extends Item {
    private String option;
    private final String[] options = new String[]{"ok", "no", "kita", "chi", "peng", "gang", "ryuu", "rii",  "he"};

    public ItemMjOption(String optionIn) {
        boolean flag = false;
        for (String string : options) {
            if (optionIn.equals(string)) flag = true;
        }
        if (!flag) throw new IllegalArgumentException("Invalid option");
        option = optionIn;
        setCreativeTab(tabMahjong);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        int optionInt = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(option)) optionInt = i;
        }
        if (optionInt == -1) return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        //NetworkHandler.INSTANCE.sendToServer(new MessageMj(optionInt, null));
        if (Mahjong.mjGameHandler.isInGame(playerIn)) {
            Game game = Mahjong.mjGameHandler.getGame(playerIn);
            game.getUi().chooseInt(game.getPlayer(playerIn.getDisplayNameString()), optionInt, null);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = worldIn.getBlockState(pos).getBlock();
        int indexTile = -1;
        if (block instanceof BlockMj) indexTile = ((BlockMj) block).getEnumTile().getIndex();
        int optionInt = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(option)) optionInt = i;
        }
        if (optionInt == -1) return EnumActionResult.FAIL;
        //NetworkHandler.INSTANCE.sendToServer(new MessageMj(optionInt, indexTile));
        if (Mahjong.mjGameHandler.isInGame(playerIn)) {
            Game game = Mahjong.mjGameHandler.getGame(playerIn);
            game.getUi().chooseInt(game.getPlayer(playerIn.getDisplayNameString()), optionInt, indexTile == -1 ? null : EnumTile.getTile(indexTile));
        }
        return EnumActionResult.SUCCESS;
    }
}
