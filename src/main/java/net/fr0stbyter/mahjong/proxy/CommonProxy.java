package net.fr0stbyter.mahjong.proxy;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.gui.GuiHandler;
import net.fr0stbyter.mahjong.init.MahjongRegister;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.util.MjGameHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MahjongRegister.define();
        MahjongRegister.register();
        NetworkHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Mahjong.instance, new GuiHandler());
        MahjongRegister.registerRecipe();
    }

    public void postInit(FMLPostInitializationEvent event) {
        Mahjong.mjGameHandler = new MjGameHandler();
    }
}
