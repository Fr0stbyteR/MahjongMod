package net.fr0stbyter.mahjong;

import net.fr0stbyter.mahjong.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Mahjong.MODID, name = Mahjong.NAME, version = Mahjong.VERSION, acceptedMinecraftVersions = "[1.9,)")
public class Mahjong {
    public static final String MODID = "mahjong";
    public static final String NAME = "Mahjong";
    public static final String VERSION = "0.2.0";

    @Mod.Instance(Mahjong.MODID)
    public static Mahjong instance;

    @SidedProxy(clientSide = "net.fr0stbyter.mahjong.proxy.ClientProxy",
            serverSide = "net.fr0stbyter.mahjong.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
