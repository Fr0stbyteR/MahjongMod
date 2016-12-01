package net.fr0stbyter.mahjong.tileentity;

import net.fr0stbyter.mahjong.inventory.SlotMj;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityMjTable extends TileEntity implements IInventory {
    private ItemStack[] itemStacks = new ItemStack[37];
    private String customName;

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= getSizeInventory()) return null;
        return itemStacks[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemStack = getStackInSlot(index);
        if (itemStack != null) {
            if (itemStack.stackSize <= count) {
                setInventorySlotContents(index, null);
                markDirty();
                return itemStack;
            }
            ItemStack itemStack1 = itemStack.splitStack(count);
            if (itemStack.stackSize <= 0) setInventorySlotContents(index, null);
            else setInventorySlotContents(index, itemStack);
            markDirty();
            return itemStack1;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = getStackInSlot(index);
        if (itemStack != null) {
            setInventorySlotContents(index, null);
            return itemStack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= getSizeInventory()) return;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        if (stack != null && stack.stackSize == 0) stack = null;
        itemStacks[index] = stack;
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return SlotMj.isItemMj(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = null;
        }
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "MjTable";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !this.customName.equals("");
    }

    public void setCustomName(String name)
    {
        customName = name;
    }

    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        itemStacks = new ItemStack[getSizeInventory()];
        if (compound.hasKey("CustomName", 8)) customName = compound.getString("CustomName");
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            if (j >= 0 && j < itemStacks.length) itemStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (hasCustomName()) compound.setString("CustomName", customName);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < itemStacks.length; ++i) {
            if (itemStacks[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        compound.setTag("Items", nbttaglist);
    }

}
