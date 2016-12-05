package net.fr0stbyter.mahjong.proxy;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.init.MahjongRenderer;
import net.fr0stbyter.mahjong.util.MjPlayerHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MahjongRenderer.render();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Mahjong.mjPlayerHandler = new MjPlayerHandler();
        super.postInit(event);
    }
}
