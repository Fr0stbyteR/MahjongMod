package net.fr0stbyter.mahjong.gui;

import net.fr0stbyter.mahjong.Mahjong;
import net.fr0stbyter.mahjong.blocks.BlockMj;
import net.fr0stbyter.mahjong.init.NetworkHandler;
import net.fr0stbyter.mahjong.inventory.ContainerMjTable;
import net.fr0stbyter.mahjong.network.message.MessageMjTable;
import net.fr0stbyter.mahjong.tileentity.TileEntityMjTable;
import net.fr0stbyter.mahjong.util.MahjongLogic.EnumTile;
import net.fr0stbyter.mahjong.util.MahjongLogic.GameType;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;

public class GuiMjTable extends GuiContainer {

    private static final ResourceLocation guiTexture = new ResourceLocation("mahjong:textures/gui/mjTable.png");
    private final InventoryPlayer playerInventory;
    private TileEntityMjTable tableInventory;
    private GuiButton buttonStart;
    private GuiButton buttonLength;
    private int region, playerCount, length, redDoraCount;
    private int isInGame;

    public GuiMjTable(InventoryPlayer playerInventoryIn, TileEntityMjTable tableInventoryIn) {
        super(new ContainerMjTable(playerInventoryIn, tableInventoryIn));
        isInGame = 0;
        region = 0;
        playerCount = 0;
        length = 1;
        redDoraCount = 0;
        playerInventory = playerInventoryIn;
        tableInventory = tableInventoryIn;
        xSize = 194;
        ySize = 233;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(I18n.translateToLocal("tile.mahjong_table.name") + " #" + Long.toHexString(tableInventory.getPos().toLong()), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 17, this.ySize - 93, 4210752);
        if (isInGame == 2) fontRendererObj.drawString(TextFormatting.BOLD + I18n.translateToLocal("gui.text.playing"), 97, 90, 4210752);
        else if (length == 0 || playerCount == 0) fontRendererObj.drawString(TextFormatting.BOLD + I18n.translateToLocal("gui.text.tilesnotenough"), 97, 90, 4210752);
        else fontRendererObj.drawString(TextFormatting.BOLD + I18n.translateToLocal("gui.text.playerscount") + ": " + playerCount
                + " " + I18n.translateToLocal("gui.text.akadora") + ": " + redDoraCount, 97, 90, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        isInGame = Mahjong.mjPlayerHandler.getIsInGame();
        buttonStart.displayString = isInGame == 0 ? I18n.translateToLocal("gui.text.start") : isInGame == 1 ? I18n.translateToLocal("gui.text.cancelwaiting") : I18n.translateToLocal("gui.text.stopgame");
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(false);
        buttonList.clear();
        buttonStart = new GuiButton(0, (width - xSize) / 2 + 61, (height - ySize) / 2 + 110, 72, 20, I18n.translateToLocal("gui.text.start"));
        buttonLength = new GuiButton(1, (width - xSize) / 2 + 7, (height - ySize) / 2 + 89, 72, 20, I18n.translateToLocal("gui.length.east"));
        buttonStart.enabled = isInGame != 0;
        buttonLength.enabled = true;
        buttonList.add(buttonStart);
        buttonList.add(buttonLength);
        checkTiles();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (!guiButton.enabled) return;
        if (guiButton.id == 0) {
            int x = tableInventory.getPos().getX();
            int y = tableInventory.getPos().getY();
            int z = tableInventory.getPos().getZ();
            if (isInGame == 0) {
                if (length == 0 || playerCount == 0) return;
                else NetworkHandler.INSTANCE.sendToServer(new MessageMjTable(true, x, y, z, region, playerCount, length, redDoraCount));
            } else NetworkHandler.INSTANCE.sendToServer(new MessageMjTable(false));
            return;
        }
        if (guiButton.id == 1) {
            if (length == 1) {
                buttonLength.displayString = I18n.translateToLocal("gui.length.south");
                length = 2;
                return;
            }
            if (length == 2) {
                buttonLength.displayString = I18n.translateToLocal("gui.length.all");
                length = 4;
                return;
            }
            if (length == 4) {
                buttonLength.displayString = I18n.translateToLocal("gui.length.east");
                length = 1;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        checkTiles();
    }

    private void checkTiles() {
        if (isInGame == 2) {
            buttonStart.enabled = true;
            return;
        }
        HashMap<EnumTile, Integer> inventoryTiles = new HashMap<EnumTile, Integer>();
        for (int i = 0; i < tableInventory.getSizeInventory(); i++) {
            ItemStack itemStack = tableInventory.getStackInSlot(i);
            if (itemStack == null) continue;
            Item item = tableInventory.getStackInSlot(i).getItem();
            if (item == null) continue;
            Block blockFromItem = Block.getBlockFromItem(item);
            if (blockFromItem == null || blockFromItem.getClass() != BlockMj.class) continue;
            EnumTile enumTile = ((BlockMj) blockFromItem).getEnumTile();
            if (enumTile == null) continue;
            inventoryTiles.put(enumTile, tableInventory.getStackInSlot(i).stackSize);
        }
        buttonStart.enabled = false;
        if (inventoryTiles.equals(GameType.TILESJ4R)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 4;
            redDoraCount = 3;
        }
        else if (inventoryTiles.equals(GameType.TILESJ4)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 4;
            redDoraCount = 0;
        }
        else if (inventoryTiles.equals(GameType.TILESJ3R)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 3;
            redDoraCount = 2;
        }
        else if (inventoryTiles.equals(GameType.TILESJ3)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 3;
            redDoraCount = 0;
        }
    }
}
