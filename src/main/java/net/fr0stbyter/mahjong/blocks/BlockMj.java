package net.fr0stbyter.mahjong.blocks;

import net.fr0stbyter.mahjong.util.EnumFacing12;
import net.fr0stbyter.mahjong.util.PropertyDirection12;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.fr0stbyter.mahjong.init.MahjongRegister.tabMahjong;

public class BlockMj extends Block {
    public static final PropertyDirection12 FACING = PropertyDirection12.create("facing");
    // Define bounds
    protected static final AxisAlignedBB boundNSUD = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 0.5D, 1.0D);
    protected static final AxisAlignedBB boundWEUD = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 0.5D, 0.875D);
    protected static final AxisAlignedBB boundN = new AxisAlignedBB(0.125D, 0.0D, 0.5D, 0.875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB boundS = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 0.5D);
    protected static final AxisAlignedBB boundW = new AxisAlignedBB(0.5D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
    protected static final AxisAlignedBB boundE = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 0.5D, 1.0D, 0.875D);

    public BlockMj(Material material) {
        super(material);
        this.setHardness(0.05f);
        this.setStepSound(SoundType.STONE);
        this.setCreativeTab(tabMahjong);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing12.NORTH));
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
        EnumFacing12 EnumFacing12 = state.getValue(FACING);
        switch (EnumFacing12) {
            case NORTH: return boundN;
            case SOUTH: return boundS;
            case WEST: return boundW;
            case EAST: return boundE;
            case NORTHU: return boundNSUD;
            case NORTHD: return boundNSUD;
            case SOUTHD: return boundNSUD;
            case SOUTHU: return boundNSUD;
            case WESTU: return boundWEUD;
            case WESTD: return boundWEUD;
            case EASTD: return boundWEUD;
            case EASTU: return boundWEUD;
            default: return boundN;
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
        state = state.withProperty(FACING, getFacingFromEntity(pos, placer));
        return state;
    }

    private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing12 EnumFacing12 = state.getValue(FACING);
            boolean flag = worldIn.getBlockState(pos.north()).isFullBlock();
            boolean flag1 = worldIn.getBlockState(pos.south()).isFullBlock();

            if (EnumFacing12 == EnumFacing12.NORTH && flag && !flag1) {
                EnumFacing12 = EnumFacing12.SOUTH;
            } else if (EnumFacing12 == EnumFacing12.SOUTH && flag1 && !flag) {
                EnumFacing12 = EnumFacing12.NORTH;
            } else {
                boolean flag2 = worldIn.getBlockState(pos.west()).isFullBlock();
                boolean flag3 = worldIn.getBlockState(pos.east()).isFullBlock();

                if (EnumFacing12 == EnumFacing12.WEST && flag2 && !flag3) {
                    EnumFacing12 = EnumFacing12.EAST;
                } else if (EnumFacing12 == EnumFacing12.EAST && flag3 && !flag2) {
                    EnumFacing12 = EnumFacing12.WEST;
                }
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12), 2);
        }
    }

    // convert EnumFacing
    public static EnumFacing12 convertEnumFacing(EnumFacing facing) {
        switch (facing) {
            case NORTH: return EnumFacing12.NORTH;
            case SOUTH: return EnumFacing12.SOUTH;
            case WEST: return EnumFacing12.WEST;
            case EAST: return EnumFacing12.EAST;
            case UP: return EnumFacing12.NORTHU;
            case DOWN: return EnumFacing12.NORTHD;
            default: return EnumFacing12.NORTH;
        }
    }

    public static EnumFacing12 getFacingFromEntity(BlockPos pos, EntityLivingBase player) {
        EnumFacing12 facing12 = convertEnumFacing(player.getHorizontalFacing().getOpposite());
        if (MathHelper.abs((float) player.posX - (float) pos.getX()) < 2.0F && MathHelper.abs((float) player.posZ - (float) pos.getZ()) < 2.0F) {
            double d0 = player.posY + (double) player.getEyeHeight();
            if (d0 - (double) pos.getY() > 2.0D) {
                switch (facing12) {
                    case NORTH: return EnumFacing12.NORTHU;
                    case SOUTH: return EnumFacing12.SOUTHU;
                    case WEST: return EnumFacing12.WESTU;
                    case EAST: return EnumFacing12.EASTU;
                    default: return EnumFacing12.NORTHU;
                }
            }

            if ((double) pos.getY() - d0 > 0.0D) {
                switch (facing12) {
                    case NORTH: return EnumFacing12.NORTHD;
                    case SOUTH: return EnumFacing12.SOUTHD;
                    case WEST: return EnumFacing12.WESTD;
                    case EAST: return EnumFacing12.EASTD;
                    default: return EnumFacing12.NORTHD;
                }
            }
        }

        return facing12;
    }

    public static EnumFacing12 getFacing(int meta) {
        return EnumFacing12.getFront(meta & 15);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    // Flip
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
        if (heldItem == null) {
            EnumFacing12 blockFacing = worldIn.getBlockState(pos).getValue(FACING);
            if (blockFacing == EnumFacing12.NORTHD) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.NORTH));
            else if (blockFacing == EnumFacing12.SOUTHD) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.SOUTH));
            else if (blockFacing == EnumFacing12.WESTD) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.WEST));
            else if (blockFacing == EnumFacing12.EASTD) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.EAST));
            else if (blockFacing == EnumFacing12.NORTH) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.NORTHU));
            else if (blockFacing == EnumFacing12.SOUTH) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.SOUTHU));
            else if (blockFacing == EnumFacing12.WEST) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.WESTU));
            else if (blockFacing == EnumFacing12.EAST) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.EASTU));
            else if (blockFacing == EnumFacing12.NORTHU) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.NORTHD));
            else if (blockFacing == EnumFacing12.SOUTHU) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.SOUTHD));
            else if (blockFacing == EnumFacing12.WESTU) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.WESTD));
            else if (blockFacing == EnumFacing12.EASTU) worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing12.EASTD));
            return true;
        }
        return false;
    }
}
