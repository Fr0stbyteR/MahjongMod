package net.fr0stbyter.mahjong.blocks;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.tileentity.TileEntityMjTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class BlockMjTable extends BlockContainer {
    public BlockMjTable(Material material) {
        super(material);
        this.setHardness(1.0f);
        this.setStepSound(SoundType.WOOD);
        this.setCreativeTab(tabMahjong);
    }

    @Override
    public boolean isOpaqueCube(IBlockState bs) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState bs) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityMjTable) {
            playerIn.openGui(Mahjong.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            //playerIn.addStat(StatList.dropperInspected);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMjTable();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
        TileEntityMjTable tileEntity = (TileEntityMjTable) world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, tileEntity);
        super.breakBlock(world, pos, blockstate);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) ((TileEntityMjTable) worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());
    }
}
