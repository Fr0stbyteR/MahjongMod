package net.fr0stbyter.mahjong.proxy;

import net.fr0stbyter.mahjong.init.MahjongRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MahjongRegister.define();
        MahjongRegister.register();
    }

    public void init(FMLInitializationEvent event) {
        MahjongRegister.registerRecipe();

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
