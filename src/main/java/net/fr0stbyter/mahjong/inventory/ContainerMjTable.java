package net.fr0stbyter.mahjong.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMjTable extends Container {
    private IInventory mjTableInventory;

    public ContainerMjTable(IInventory playerInventoryIn, IInventory mjTableInventoryIn) {
        mjTableInventory = mjTableInventoryIn;
        mjTableInventory.openInventory(null);
        int count = mjTableInventory.getSizeInventory();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                int index = j + i * 10;
                if (index == count) break;
                addSlotToContainer(new SlotMj(mjTableInventoryIn, index, j * 18 + 8, i * 18 + 17));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(playerInventoryIn, j + i * 9 + 9, j * 18 + 17, i * 18 + 152));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(playerInventoryIn, i, i * 18 + 17, 210));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < mjTableInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, mjTableInventory.getSizeInventory(), inventorySlots.size(), true)) return null;
            } else if (!this.mergeItemStack(itemstack1, 0, mjTableInventory.getSizeInventory(), false)) return null;

            if (itemstack1.stackSize == 0) slot.putStack(null);
            else slot.onSlotChanged();
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return mjTableInventory.isUseableByPlayer(playerIn);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        mjTableInventory.closeInventory(entityPlayer);
    }


}
