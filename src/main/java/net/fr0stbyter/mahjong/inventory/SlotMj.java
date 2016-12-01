package net.fr0stbyter.mahjong.inventory;

import net.fr0stbyter.mahjong.blocks.BlockMj;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMj extends Slot {
    public SlotMj(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return isItemMj(stack);
    }

    public static boolean isItemMj(ItemStack stackIn) {
        if (Block.getBlockFromItem(stackIn.getItem()) == null) return false;
        return Block.getBlockFromItem(stackIn.getItem()).getClass() == BlockMj.class;
    }
}
