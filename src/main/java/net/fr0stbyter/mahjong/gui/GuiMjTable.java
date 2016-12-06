package net.fr0stbyter.mahjong.gui;

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
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;

public class GuiMjTable extends GuiContainer {

    private static final ResourceLocation guiTexture = new ResourceLocation("mahjong:textures/gui/mjTable.png");
    private final InventoryPlayer playerInventory;
    private TileEntityMjTable tableInventory;
    private GuiButton buttonStart;
    private int region, playerCount, length, redDoraCount;

    public GuiMjTable(InventoryPlayer playerInventoryIn, TileEntityMjTable tableInventoryIn) {
        super(new ContainerMjTable(playerInventoryIn, tableInventoryIn));
        region = 0;
        playerCount = 0;
        length = 0;
        redDoraCount = 0;
        playerInventory = playerInventoryIn;
        tableInventory = tableInventoryIn;
        xSize = 194;
        ySize = 232;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(tableInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 17, this.ySize - 96 + 2, 4210752);
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
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(false);
        buttonList.clear();
        buttonStart = new GuiButton(0, (width - xSize) / 2 + 148, (height - ySize) / 2 + 92, 40, 20, "Start");
        buttonStart.enabled = false;
        buttonList.add(buttonStart);
        //checkTiles();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (!guiButton.enabled) return;
        if (guiButton.id == 0) {
            if (length == 0 || playerCount == 0) return;
            int x = tableInventory.getPos().getX();
            int y = tableInventory.getPos().getY();
            int z = tableInventory.getPos().getZ();
            NetworkHandler.INSTANCE.sendToServer(new MessageMjTable(x, y, z, region, playerCount, length, redDoraCount));

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
            length = 1;
            redDoraCount = 3;
        }
        else if (inventoryTiles.equals(GameType.TILESJ4)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 4;
            length = 1;
            redDoraCount = 0;
        }
        else if (inventoryTiles.equals(GameType.TILESJ3R)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 3;
            length = 2;
            redDoraCount = 2;
        }
        else if (inventoryTiles.equals(GameType.TILESJ3)) {
            buttonStart.enabled = true;
            region = 0;
            playerCount = 3;
            length = 2;
            redDoraCount = 0;
        }
    }
}
