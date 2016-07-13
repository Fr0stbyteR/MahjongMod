package net.fr0stbyter.mahjong.blocks;

import net.fr0stbyter.mahjong.util.EnumFacing12;
import net.fr0stbyter.mahjong.util.PropertyDirection12;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.blockMjd1;
import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class BlockMjTable extends Block {
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
        if(worldIn.isRemote) {
            return false;
        }
        worldIn.setBlockState(pos.north(10), blockMjd1.getDefaultState().withProperty(PropertyDirection12.create("facing"), EnumFacing12.NORTHD), 2);
        return true;

    }
}
