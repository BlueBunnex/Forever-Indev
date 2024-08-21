package net.minecraft.client.gui.container;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiStringBox;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.enchant.Enchant;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {

    protected static RenderItem itemRenderer = new RenderItem();
    private ItemStack heldItem = null;
    protected int xSize = 176;
    protected int ySize = 166;
    protected List<Slot> inventorySlots = new ArrayList<Slot>();

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        this.drawDefaultBackground();

        int cornerX = (this.width - this.xSize) / 2;
        int cornerY = (this.height - this.ySize) / 2;

        this.drawGuiContainerBackgroundLayer();

        // GL setup
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) cornerX, (float) cornerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);

        Slot hovered = null;

        for (int i = 0; i < this.inventorySlots.size(); i++) {
            Slot slot = this.inventorySlots.get(i);

            int x = slot.xPos;
            int y = slot.yPos;

            ItemStack itemStack = slot.inventory.getStackInSlot(slot.slotIndex);

            jump: {
                if (itemStack == null) {
                    int background = slot.getBackgroundIconIndex();

                    if (background >= 0) {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                        this.drawTexturedModalRect(x, y, background % 16 << 4, background / 16 << 4, 16, 16);
                        GL11.glEnable(GL11.GL_LIGHTING);

                        break jump;
                    }
                }

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack, x, y);
                GL11.glDisable(GL11.GL_BLEND);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, itemStack, x, y);
                GL11.glDisable(GL11.GL_BLEND);
            }

            if (slot.slotIndex == 37 && slot instanceof SlotAccessory) { // Check if slot is index 37 and is an AccessorySlot
                // Get the ItemStack in slot 36
                ItemStack itemStackInSlot36 = slot.inventory.getStackInSlot(36); 

                // Check if slot 36 is empty or if the item in slot 36 is not a quiver
                if (itemStackInSlot36 == null || !itemStackInSlot36.getItem().equals(Item.quiver)) {
                    // Semi-transparent black overlay (ARGB format)
                    int overlayColor = 0x500D0D0D;
                    // Semi-transparent red (ARGB format)
                    int outlineColor = 0x80FF0000; // 50% transparency + Red color

                    // Draw the darkened overlay
                    drawRect(x, y, x + 16, y + 16, overlayColor);

                    // Draw the red outline by drawing four rectangles to form a border
                    drawRect(x - 1, y - 1, x + 17, y, outlineColor); // Top border
                    drawRect(x - 1, y + 16, x + 17, y + 17, outlineColor); // Bottom border
                    drawRect(x - 1, y, x, y + 16, outlineColor); // Left border
                    drawRect(x + 16, y, x + 17, y + 16, outlineColor); // Right border

                    // Resetting the color to white (standard reset)
                    drawRect(0, 0, 0, 0, 0xFFFFFFFF); // A no-op draw with white color (standard reset in many GUIs)
                }
            }

            if (slot.isAtCursorPos(mouseX, mouseY))
                hovered = slot;
        }

        RenderHelper.disableStandardItemLighting();
        super.drawScreen(mouseX - cornerX, mouseY - cornerY);

        // Render foreground layer
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.drawGuiContainerForegroundLayer();

        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        if (hovered != null) {
            int x = hovered.xPos;
            int y = hovered.yPos;

            ItemStack itemStack = hovered.inventory.getStackInSlot(hovered.slotIndex);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            drawRect(x, y, x + 16, y + 16, -2130706433);

            if (itemStack != null && this.heldItem == null) {
                GuiStringBox tooltip = new GuiStringBox(mc.fontRenderer, mouseX - cornerX + 6, mouseY - cornerY - 6);

                tooltip.addLine(
                        itemStack.getName() + " (#" + itemStack.getItem().shiftedIndex + ")",
                        itemStack.getItem().getRarity().color
                );

                // Rainbow effect for enchantments with fixed brightness
                long currentTime = System.currentTimeMillis();
                float hue = (currentTime % 10000L) / 10000.0F;
                float brightness = 0.7F; // Fixed brightness

                // Ensure 'itemStack.getEnchants()' returns a valid collection
                Collection<Enchant> enchantCollection = itemStack.getEnchants();
                if (enchantCollection == null) {
                    enchantCollection = Collections.emptyList(); // Provide a default empty collection if null
                }
                List<Enchant> enchantments = new ArrayList<>(enchantCollection);

                int enchantCount = enchantments.size();

                if (enchantCount > 0) {
                    for (int index = 0; index < enchantCount; index++) {
                        Enchant enchant = enchantments.get(index);

                        // Calculate the color with fixed brightness
                        float offsetHue = (hue + (index / (float) enchantCount)) % 1.0F;
                        int color = Color.HSBtoRGB(offsetHue, 0.7F, brightness);

                        // Add the line to the tooltip with calculated color
                        tooltip.addLine(enchant.toString(), color);
                    }
                }

                // Draw the tooltip
                tooltip.drawStringBox(mc);

                // Reset color to default (ensure it's done after drawing)
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glPopMatrix();

        if (this.heldItem != null) {
            GL11.glPushMatrix();
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);

            int x = mouseX - 8;
            int y = mouseY - 8;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.heldItem, x, y);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.heldItem, x, y);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }

        RenderHelper.disableStandardItemLighting();
    }


    protected void drawGuiContainerForegroundLayer() {}

    protected abstract void drawGuiContainerBackgroundLayer();

    protected final void drawSlotInventory(int mouseX, int mouseY, int mouseClick) {
        int cornerX = (this.width - this.xSize) / 2;
        int cornerY = (this.height - this.ySize) / 2;
        super.drawSlotInventory(mouseX - cornerX, mouseY - cornerY, mouseClick);

        if (mouseClick == 0 || mouseClick == 1) {
            int var4 = mouseX;
            int var6 = mouseY;
            GuiContainer var5 = this;
            int var7 = 0;

            Slot clickedSlot;
            while (true) {
                if (var7 >= var5.inventorySlots.size()) {
                    clickedSlot = null;
                    break;
                }

                Slot var8 = var5.inventorySlots.get(var7);
                if (var8.isAtCursorPos(var4, var6)) {
                    clickedSlot = var8;
                    break;
                }

                ++var7;
            }

            if (clickedSlot != null) {
                this.heldItem = clickedSlot.onClickedWithHeldStack(this.heldItem, mouseClick);
            } else if (this.heldItem != null) {
                int var13 = (this.width - this.xSize) / 2;
                var6 = (this.height - this.ySize) / 2;
                if (mouseX < var13 || mouseY < var6 || mouseX >= var13 + this.xSize || mouseY >= var6 + this.xSize) {
                    net.minecraft.game.entity.player.EntityPlayerSP var10 = this.mc.thePlayer;
                    if (mouseClick == 0) {
                        var10.dropPlayerItem(this.heldItem);
                        this.heldItem = null;
                    }

                    if (mouseClick == 1) {
                        var10.dropPlayerItem(this.heldItem.splitStack(1));
                        if (this.heldItem.stackSize == 0) {
                            this.heldItem = null;
                        }
                    }
                }
            }
        }
    }

    protected final void keyTyped(char var1, int var2) {
        if (var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
            this.mc.displayGuiScreen(null);
        }
    }

    public void onGuiClosed() {
        if (this.heldItem != null)
            this.mc.thePlayer.dropPlayerItem(this.heldItem);
    }

    public void guiCraftingItemsCheck() {}

    public final boolean doesGuiPauseGame() {
        return false;
    }
}
