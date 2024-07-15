package net.minecraft.client.gui.container;

import net.minecraft.client.RenderHelper;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public final class GuiInventoryCreative extends GuiContainer {
	
	private static IInventory infinite;
	
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventoryCreative(IInventory inventory) {
		
		this.xSize = 231;
		this.ySize = 86;
		
		this.allowUserInput = true;

		int x;
		int y;

		// inventory
		for (y = 0; y < 3; y++) {
			for (x = 0; x < 9; x++) {
				this.inventorySlots.add(new Slot(this, inventory, x + (y + 1) * 9, 65 + x * 18, 6 + y * 18));
			}
		}

		// hotbar
		for (x = 0; x < 9; x++) {
			this.inventorySlots.add(new Slot(this, inventory, x, 65 + x * 18, 64));
		}
		
		// creative inventory
		for (int i=0; i<infinite.getSizeInventory(); i++) {
			this.inventorySlots.add(new Slot(this, infinite, i, 65 + i % 9 * 18, 100 + i / 9 * 18));
		}
	}

	protected final void drawGuiContainerForegroundLayer() {
		//this.fontRenderer.drawString("All Items", 86, 16, 4210752);
	}

	public final void drawScreen(int mouseX, int mouseY) {
		super.drawScreen(mouseX, mouseY);
		
		this.xSize_lo = (float) mouseX;
		this.ySize_lo = (float) mouseY;
	}

	protected final void drawGuiContainerBackgroundLayer() {
		
		int var1 = this.mc.renderEngine.getTexture("/gui/inventoryCreative.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.ySize);
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float)(var1 + 34), (float)(var2 + 75), 50.0F);
		GL11.glScalef(-30.0F, 30.0F, 30.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var3 = this.mc.thePlayer.renderYawOffset;
		float var4 = this.mc.thePlayer.rotationYaw;
		float var5 = this.mc.thePlayer.rotationPitch;
		
		// this is used to calculate the point where the player model should look
		float var6 = (float)(var1 + 34) - this.xSize_lo;
		float var7 = (float)(var2 + 75 - 50) - this.ySize_lo;
		
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(var7 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(var6 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(var6 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(var7 / 40.0F))) * 20.0F;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
		
		this.mc.thePlayer.renderYawOffset = var3;
		this.mc.thePlayer.rotationYaw = var4;
		this.mc.thePlayer.rotationPitch = var5;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		
		GL11.glDisable(GL11.GL_NORMALIZE);
	}
	
	static {
		ArrayList<Item> allItems = new ArrayList<Item>();
		
		for (Item item : Item.itemsList) {
			
			if (item != null)
				allItems.add(item);
		}
		
		infinite = new InventoryInfinite(allItems.toArray(new Item[0]));
	}
}
