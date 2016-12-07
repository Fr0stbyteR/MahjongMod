package net.fr0stbyter.mahjong.util;

import net.fr0stbyter.mahjong.gui.GuiMjStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPlayerHandler {
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null) {
            GuiMjStatus mjStatus = new GuiMjStatus(mc);
        }
    }
}