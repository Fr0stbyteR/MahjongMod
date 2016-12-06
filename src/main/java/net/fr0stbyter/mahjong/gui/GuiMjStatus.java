package net.fr0stbyter.mahjong.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiMjStatus extends GuiScreen {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private int height;

    public GuiMjStatus(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
        this.height = 0;
    }

    public void draw() {
        int lineWidth = 10;
        int line = 0;
        ScaledResolution scaled = new ScaledResolution(this.mc);
        drawRect(scaled.getScaledWidth() - 60, 100, scaled.getScaledWidth(), 100 + this.height, Integer.MIN_VALUE);
        drawString(fontRenderer, "", scaled.getScaledWidth() - 60, 100, Integer.parseInt("0088FF", 16));
    }

    public void updateState(int playersCount, int round, int hand, int extra, int tilesCount, int curPos, String[] playersName, int[] playersScore) {
    }
}
