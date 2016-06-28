package net.fr0stbyter.mahjong.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public enum EnumFacing12 implements IStringSerializable
{
    NORTHD(0, 9, -1, "northd", new Vec3i(0, -1, -1)),
    SOUTHD(1, 8, -1, "southd", new Vec3i(0, -1, 1)),
    WESTD(2, 11, -1, "westd", new Vec3i(-1, -1, 0)),
    EASTD(3, 10, -1, "eastd",new Vec3i(1, -1, 0)),
    NORTH(4, 5, 2, "north", new Vec3i(0, 0, -1)),
    SOUTH(5, 4, 0, "south", new Vec3i(0, 0, 1)),
    WEST(6, 7, 1, "west", new Vec3i(-1, 0, 0)),
    EAST(7, 6, 3, "east", new Vec3i(1, 0, 0)),
    NORTHU(8, 1, -1, "northu", new Vec3i(0, 1, -1)),
    SOUTHU(9, 0, -1, "southu", new Vec3i(0, 1, 1)),
    WESTU(10, 3, -1, "westu", new Vec3i(-1, 1, 0)),
    EASTU(11, 2, -1, "eastu", new Vec3i(1, 1, 0));

    private final int index;
    /** Index of the opposite Facing in the VALUES array */
    private final int opposite;
    /** Ordering index for the HORIZONTALS field (S-W-N-E) */
    private final int horizontalIndex;
    private final String name;
    /** Normalized Vector that points in the direction of this Facing */
    private final Vec3i directionVec;
    /** All facings in D-U-N-S-W-E order */
    public static final EnumFacing12[] VALUES = new EnumFacing12[12];
    /** All Facings with horizontal axis in order S-W-N-E */
    public static final EnumFacing12[] HORIZONTALS = new EnumFacing12[4];
    private static final Map<String, EnumFacing12> NAME_LOOKUP = Maps.newHashMap();

    EnumFacing12(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, Vec3i directionVecIn)
    {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
        this.directionVec = directionVecIn;
    }

    /**
     * Get the Index of this Facing (0-5). The order is D-U-N-S-W-E
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * Get the index of this horizontal facing (0-3). The order is S-W-N-E
     */
    public int getHorizontalIndex()
    {
        return this.horizontalIndex;
    }

    /**
     * Get the opposite Facing (e.g. DOWN => UP)
     */
    public EnumFacing12 getOpposite()
    {
        /**
         * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
         */
        return getFront(this.opposite);
    }

    /**
     * Same as getName, but does not override the method from Enum.
     */
    public String getName2()
    {
        return this.name;
    }

    /**
     * Get the facing specified by the given name
     */
    public static EnumFacing12 byName(String name)
    {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase());
    }

    /**
     * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
     */
    public static EnumFacing12 getFront(int index)
    {
        return VALUES[MathHelper.abs_int(index % VALUES.length)];
    }

    /**
     * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
     */
    public static EnumFacing12 getHorizontal(int p_176731_0_)
    {
        return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
    }

    /**
     * Get the Facing corresponding to the given angle (0-360). An angle of 0 is SOUTH, an angle of 90 would be WEST.
     */
    public static EnumFacing12 fromAngle(double angle)
    {
        /**
         * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
         */
        return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
    }

    public float getHorizontalAngle()
    {
        return (float)((this.horizontalIndex & 3) * 90);
    }

    /**
     * Choose a random Facing using the given Random
     */
    public static EnumFacing12 random(Random rand)
    {
        return values()[rand.nextInt(values().length)];
    }

    public static EnumFacing12 getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_)
    {
        EnumFacing12 EnumFacing12 = NORTH;
        float f = Float.MIN_VALUE;

        for (EnumFacing12 EnumFacing121 : values())
        {
            float f1 = p_176737_0_ * (float)EnumFacing121.directionVec.getX() + p_176737_1_ * (float)EnumFacing121.directionVec.getY() + p_176737_2_ * (float)EnumFacing121.directionVec.getZ();

            if (f1 > f)
            {
                f = f1;
                EnumFacing12 = EnumFacing121;
            }
        }

        return EnumFacing12;
    }

    public String toString()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name;
    }

    /**
     * Get a normalized Vector that points in the direction of this Facing.
     */
    public Vec3i getDirectionVec()
    {
        return this.directionVec;
    }

    static
    {
        for (EnumFacing12 EnumFacing12 : values())
        {
            VALUES[EnumFacing12.index] = EnumFacing12;

            if (EnumFacing12.horizontalIndex >= 0)
            {
                HORIZONTALS[EnumFacing12.horizontalIndex] = EnumFacing12;
            }

            NAME_LOOKUP.put(EnumFacing12.getName2().toLowerCase(), EnumFacing12);
        }
    }

}