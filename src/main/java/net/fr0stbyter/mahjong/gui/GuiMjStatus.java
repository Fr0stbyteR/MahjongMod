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
        this.options = Mahjong.mjPlayerHandler.getOptions();
        draw();
    }

    public void draw() {
        if (isInGame == 0) return;
        int top = 60;
        int lineWidth = 10;
        int line = 0;
        String text;
        ScaledResolution scaled = new ScaledResolution(this.mc);
        int x = scaled.getScaledWidth() - 100;

        if (isInGame == 1) {
            text = I18n.translateToLocal("gui.text.waiting") + "#" + gameId;
            drawString(fontRenderer, text, x, top, Integer.parseInt("0088FF", 16));
            line++;
            //drawRect(x, top, scaled.getScaledWidth(), top + line * lineWidth, Integer.MIN_VALUE);
            return;
        }
        text = I18n.translateToLocal("gui.text.playing") + "#" + gameId;
        drawString(fontRenderer, text, x, top, Integer.parseInt("0088FF", 16));
        line++;
        text = (gameState[0] == 3 ? I18n.translateToLocal("gui.text.sanma") : I18n.translateToLocal("gui.text.fourp"))
                + (gameState[4] == 1 ? I18n.translateToLocal("gui.length.east") : gameState[4] == 2 ? I18n.translateToLocal("gui.length.south") : I18n.translateToLocal("gui.length.all"));
        drawString(fontRenderer, text, x, top + line * lineWidth, Integer.parseInt("0088FF", 16));
        line++;
        text = TextFormatting.BOLD + I18n.translateToLocal("gui.position." + EnumPosition.getPosition(gameState[1])) + gameState[2] + I18n.translateToLocal("gui.text.hand")
                + " " + gameState[3] + I18n.translateToLocal("gui.text.extra")
                + " " + I18n.translateToLocal("gui.text.riichibou") + "*" +  gameState[5];
        drawString(fontRenderer, text, x, top + line * lineWidth, Integer.parseInt("0088FF", 16));
        line++;
        for (int i = 0; i < gameState[0]; i++) {
            text = I18n.translateToLocal("gui.position." + EnumPosition.getPosition(i)) + " " + names[i] + " " + scores[i];
            boolean isCur = curPos == i;
            drawString(fontRenderer, text, x, top + line * lineWidth, Integer.parseInt(isCur ? "FF9900" : "FFFFFF", 16));
            line++;
        }
        if (isFuriten) {
            drawString(fontRenderer, I18n.translateToLocal("gui.text.furiten"), x, top + line * lineWidth, Integer.parseInt("FFFF00", 16));
            line++;
        }
        if (options.size() > 0) {
            drawString(fontRenderer, I18n.translateToLocal("gui.option.has"), x, top + line * lineWidth, Integer.parseInt("FF0000", 16));
            line++;
            for (Integer option : options.keySet()) {
                text = I18n.translateToLocal("gui.option." + Player.Option.getOption(option).name().toLowerCase());
                if (options.get(option).length > 0) {
                    for (int indexTile : options.get(option)) {
                        if (indexTile > 0) text += " " + I18n.translateToLocal("gui.tile." + EnumTile.getTile(indexTile).getName());
                    }
                }
                drawString(fontRenderer, text, x, top + line * lineWidth, Integer.parseInt("FF0000", 16));
                line++;
            }
        }
        /*
        GlStateManager.enableAlpha();
        drawRect(x, top, scaled.getScaledWidth(), top + line * lineWidth, -1873784752);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();*/
    }
}
