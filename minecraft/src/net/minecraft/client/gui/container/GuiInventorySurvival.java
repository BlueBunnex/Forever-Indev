package net.minecraft.client.gui.container;

import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;
import org.lwjgl.opengl.GL11;

public final class GuiInventorySurvival extends GuiContainer {

    private InventoryCrafting inventoryCrafting = new InventoryCrafting(this, 2, 2);
    private IInventory craftResult = new InventoryCraftResult();

    private float xSize_lo;
    private float ySize_lo;

    public GuiInventorySurvival(IInventory inventory) {

        this.allowUserInput = true;

        int x;
        int y;

        // crafting
        for (y = 0; y < 2; y++) {
            for (x = 0; x < 2; x++) {
                this.inventorySlots.add(new Slot(this, this.inventoryCrafting, x + (y << 1), 98 + x * 18, 26 + y * 18));
            }
        }
        this.inventorySlots.add(new SlotCrafting(this, this.inventoryCrafting, this.craftResult, 0, 154, 36));

        // armor
        for (int armorType = 0; armorType < 4; armorType++) {
            this.inventorySlots.add(new SlotArmor(this, inventory, inventory.getSizeInventory() - 1 - armorType, 24, 8 + armorType * 18, armorType));
        }
        
     // Quiver and arrow slots:
        this.inventorySlots.add(new SlotAccessory(this, inventory, 36, 6, 26, SlotAccessory.ACCESSORY_QUIVER));
        this.inventorySlots.add(new SlotAccessory(this, inventory, 37, 6, 44, SlotAccessory.ACCESSORY_ARROW));

        // inventory
        for (y = 0; y < 3; y++) {
            for (x = 0; x < 9; x++) {
                this.inventorySlots.add(new Slot(this, inventory, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // hotbar
        for (x = 0; x < 9; x++) {
            this.inventorySlots.add(new Slot(this, inventory, x, 8 + x * 18, 142));
        }
    }

    public final void onGuiClosed() {
        super.onGuiClosed();

        for (int var1 = 0; var1 < this.inventoryCrafting.getSizeInventory(); ++var1) {
            ItemStack var2 = this.inventoryCrafting.getStackInSlot(var1);
            if (var2 != null) {
                this.mc.thePlayer.dropPlayerItem(var2);
            }
        }
    }

    public final void guiCraftingItemsCheck() {
        int[] var1 = new int[9];

        for (int var2 = 0; var2 < 3; ++var2) {
            for (int var3 = 0; var3 < 3; ++var3) {
                int var4 = -1;
                if (var2 < 2 && var3 < 2) {
                    ItemStack var5 = this.inventoryCrafting.getStackInSlot(var2 + (var3 << 1));
                    if (var5 != null) {
                        var4 = var5.itemID;
                    }
                }

                var1[var2 + var3 * 3] = var4;
            }
        }

        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(var1));
    }

    protected final void drawGuiContainerForegroundLayer() {
    	this.fontRenderer.drawString("Crafting", 96, 16, 4210752);
    }

    public final void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        this.xSize_lo = (float) mouseX;
        this.ySize_lo = (float) mouseY;
    }

    protected final void drawGuiContainerBackgroundLayer() {
        int var1 = this.mc.renderEngine.getTexture("/gui/inventorySurvival.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderEngine.bindTexture(var1);
        var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (var1 + 67), (float) (var2 + 75), 50.0F); // Moved the character model 16 pixels to the right (51 + 16 = 67)
        GL11.glScalef(-30.0F, 30.0F, 30.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var3 = this.mc.thePlayer.renderYawOffset;
        float var4 = this.mc.thePlayer.rotationYaw;
        float var5 = this.mc.thePlayer.rotationPitch;
        float var6 = (float) (var1 + 67) - this.xSize_lo;
        float var7 = (float) (var2 + 75 - 50) - this.ySize_lo;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan((double) (var7 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        this.mc.thePlayer.renderYawOffset = (float) Math.atan((double) (var6 / 40.0F)) * 20.0F;
        this.mc.thePlayer.rotationYaw = (float) Math.atan((double) (var6 / 40.0F)) * 40.0F;
        this.mc.thePlayer.rotationPitch = -((float) Math.atan((double) (var7 / 40.0F))) * 20.0F;
        GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
        RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
        this.mc.thePlayer.renderYawOffset = var3;
        this.mc.thePlayer.rotationYaw = var4;
        this.mc.thePlayer.rotationPitch = var5;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_NORMALIZE);
    }
}
