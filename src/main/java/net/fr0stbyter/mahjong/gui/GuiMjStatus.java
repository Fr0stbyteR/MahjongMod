package net.fr0stbyter.mahjong.gui;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumPosition;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.util.HashMap;

public class GuiMjStatus extends GuiScreen {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private final int isInGame;
    private final String gameId;
    private final int[] gameState;
    private final int[] scores;
    private final String[] names;
    private final boolean isFuriten;
    private final int curPos;
    private final HashMap<Integer, int[]> options;

    public GuiMjStatus(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = mc.fontRendererObj;
        this.isInGame = Mahjong.mjPlayerHandler.getIsInGame();
        this.gameId = Mahjong.mjPlayerHandler.getGameId();
        this.gameState = Mahjong.mjPlayerHandler.getGameState();
        this.names = Mahjong.mjPlayerHandler.getPlayersName();
        this.scores = Mahjong.mjPlayerHandler.getPlayersScore();
        this.isFuriten = Mahjong.mjPlayerHandler.isFuriTen();
        this.curPos = Mahjong.mjPlayerHandler.getCurPos();
        this.options = (HashMap<Integer, int[]>) Mahjong.mjPlayerHandler.getOptions().clone();
        draw();
    }

    public void draw() {
        if (isInGame == 0) return;
        int top = 60;
        int lineWidth = 10;
        int line = 0;
        String text;
        ScaledResolution scaled = new ScaledResolution(this.mc);
        int left = scaled.getScaledWidth() - 98;

        if (isInGame == 1) {
            text = I18n.translateToLocal("gui.text.waiting") + "#" + gameId;
            drawString(fontRenderer, text, left, top, 0x0088FF);
            line++;
            drawGradientRect(left - 2, top - 2, scaled.getScaledWidth(), top + line * lineWidth + 2, 0x04000000, 0x04000000);
            return;
        }
        if (gameState == null) return;
        text = I18n.translateToLocal("gui.text.playing") + "#" + gameId;
        drawString(fontRenderer, text, left, top, 0x0088FF);
        line++;
        text = (gameState[0] == 3 ? I18n.translateToLocal("gui.text.sanma") : I18n.translateToLocal("gui.text.fourp"))
                + (gameState[4] == 0 ? I18n.translateToLocal("gui.length.one")
                : gameState[4] == 1 ? I18n.translateToLocal("gui.length.east")
                : gameState[4] == 2 ? I18n.translateToLocal("gui.length.south")
                : I18n.translateToLocal("gui.length.all"));
        drawString(fontRenderer, text, left, top + line * lineWidth, 0x0088FF);
        line++;
        text = TextFormatting.BOLD + I18n.translateToLocal("gui.position." + EnumPosition.getPosition(gameState[1])) + gameState[2] + I18n.translateToLocal("gui.text.hand")
                + (gameState[3] > 0 ? " " + gameState[3] + I18n.translateToLocal("gui.text.extra") : "")
                + (gameState[5] > 0 ? " " + I18n.translateToLocal("gui.text.riichibou") + "*" + gameState[5] : "");
        drawString(fontRenderer, text, left, top + line * lineWidth, 0x0088FF);
        line++;
        for (int i = 0; i < gameState[0]; i++) {
            text = I18n.translateToLocal("gui.position." + EnumPosition.getPosition(i)) + " " + names[i] + " " + scores[i];
            boolean isCur = curPos == i;
            drawString(fontRenderer, text, left, top + line * lineWidth, isCur ? 0xFF9900 : 0xFFFFFF);
            line++;
        }
        if (isFuriten) {
            drawString(fontRenderer, I18n.translateToLocal("gui.text.furiten"), left, top + line * lineWidth, 0xFFFF00);
            line++;
        }
        if (options.size() > 0) {
            drawString(fontRenderer, I18n.translateToLocal("gui.option.has"), left, top + line * lineWidth, 0xFF0000);
            line++;
            for (Integer option : options.keySet()) {
                text = I18n.translateToLocal("gui.option." + Player.Option.getOption(option).name().toLowerCase());
                if (options.get(option).length > 0) {
                    for (int indexTile : options.get(option)) {
                        if (indexTile > 0) text += " " + I18n.translateToLocal("gui.tile." + EnumTile.getTile(indexTile).getName());
                    }
                }
                drawString(fontRenderer, text, left, top + line * lineWidth, 0xFF0000);
                line++;
            }
        }
        /*
        GlStateManager.enableAlpha();
        drawRect(left, top, scaled.getScaledWidth(), top + line * lineWidth, -1873784752);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();*/

        drawGradientRect(left - 2, top - 2, scaled.getScaledWidth(), top + line * lineWidth + 2, 0x04000000, 0x04000000);

    }
/*
    private static void drawRect(int left, int top, int right, int bottom, float r, float g, float b, float a)
    {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)left, (double)bottom, 0.0D).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)right, (double)bottom, 0.0D).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)right, (double)top, 0.0D).color(r, g, b, a).endVertex();
        vertexbuffer.pos((double)left, (double)top, 0.0D).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }*/
}
