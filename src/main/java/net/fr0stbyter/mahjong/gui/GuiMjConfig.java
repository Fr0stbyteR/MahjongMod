package net.fr0stbyter.mahjong.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiMjConfig extends GuiConfig {
    public GuiMjConfig(GuiScreen parent) {
        super(parent, null, "mahjong", false, false, null);
    }

}
