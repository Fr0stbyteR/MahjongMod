package net.fr0stbyter.mahjong.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyEnum;

import java.util.Collection;


public class PropertyDirection12 extends PropertyEnum<EnumFacing12>
{
    protected PropertyDirection12(String name, Collection<EnumFacing12> values)
    {
        super(name, EnumFacing12.class, values);
    }

    /**
     * Create a new PropertyDirection12 with the given name
     */
    public static PropertyDirection12 create(String name)
    {
        /**
         * Create a new PropertyDirection12 with all directions that match the given Predicate
         */
        return create(name, Predicates.<EnumFacing12>alwaysTrue());
    }

    /**
     * Create a new PropertyDirection12 with all directions that match the given Predicate
     */
    public static PropertyDirection12 create(String name, Predicate<EnumFacing12> filter)
    {
        /**
         * Create a new PropertyDirection12 for the given direction values
         */
        return create(name, Collections2.filter(Lists.newArrayList(EnumFacing12.values()), filter));
    }

    /**
     * Create a new PropertyDirection12 for the given direction values
     */
    public static PropertyDirection12 create(String name, Collection<EnumFacing12> values)
    {
        return new PropertyDirection12(name, values);
    }
}