package net.fr0stbyter.mahjong.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class BlockRiichibou extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    // Define bounds
    protected static final AxisAlignedBB boundNS = new AxisAlignedBB(-1.0D, 0.0D, 0.4375D, 2.0D, 0.0625D, 0.5625D);
    protected static final AxisAlignedBB boundWE = new AxisAlignedBB(0.4375D, 0.0D, -1.0D, 0.5625D, 0.0625D, 2.0D);

    public BlockRiichibou(Material material) {
        super(material);
        this.setHardness(0.05f);
        this.setStepSound(SoundType.STONE);
        this.setCreativeTab(tabMahjong);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean isOpaqueCube(IBlockState bs) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState bs) {
        return false;
    }

    // See BlockChest
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing EnumFacing = state.getValue(FACING);
        switch (EnumFacing) {
            case NORTH: return boundNS;
            case SOUTH: return boundNS;
            case WEST: return boundWE;
            case EAST: return boundWE;
            default: return boundNS;
        }
    }

    // See BlockDispenser
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        state = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        return state;
    }

    private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing EnumFacing = state.getValue(FACING);
            boolean flag = worldIn.getBlockState(pos.north()).isFullBlock();
            boolean flag1 = worldIn.getBlockState(pos.south()).isFullBlock();

            if (EnumFacing == EnumFacing.NORTH && flag && !flag1) {
                EnumFacing = EnumFacing.SOUTH;
            } else if (EnumFacing == EnumFacing.SOUTH && flag1 && !flag) {
                EnumFacing = EnumFacing.NORTH;
            } else {
                boolean flag2 = worldIn.getBlockState(pos.west()).isFullBlock();
                boolean flag3 = worldIn.getBlockState(pos.east()).isFullBlock();

                if (EnumFacing == EnumFacing.WEST && flag2 && !flag3) {
                    EnumFacing = EnumFacing.EAST;
                } else if (EnumFacing == EnumFacing.EAST && flag3 && !flag2) {
                    EnumFacing = EnumFacing.WEST;
                }
            }
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing), 2);
        }
    }


    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

}
