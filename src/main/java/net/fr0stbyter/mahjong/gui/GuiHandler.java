package net.fr0stbyter.mahjong.gui;

import net.fr0stbyter.mahjong.inventory.ContainerMjTable;
import net.fr0stbyter.mahjong.tileentity.TileEntityMjTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileentity instanceof TileEntityMjTable) return new ContainerMjTable(player.inventory, (TileEntityMjTable) tileentity);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileentity instanceof TileEntityMjTable) return new GuiMjTable(player.inventory, (TileEntityMjTable) tileentity);
        return null;
    }
}
