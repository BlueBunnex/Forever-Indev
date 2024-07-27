package net.minecraft.client.gui.container;

import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonText;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.IInventory;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public final class GuiInventoryCreative extends GuiContainer {
	
	private float mouseX;
	private float mouseY;
	private List<Slot> creativeSlots = new ArrayList<Slot>();
	
	private int page;
	private int numPages;
	private GuiButton decPageButton, incPageButton;

	public GuiInventoryCreative(IInventory inventory) {
		
		this.page = 1;
		this.numPages = (int) Math.ceil(SlotCreative.getSizeInventory() / 36.0);
		
		this.xSize = 247;
		this.ySize = 144;
		
		this.allowUserInput = true;

		int x, y;

		// inventory
		for (y = 0; y < 3; y++) {
			for (x = 0; x < 9; x++) {
				this.inventorySlots.add(new Slot(this, inventory, x + (y + 1) * 9, 81 + x * 18, 64 + y * 18));
			}
		}
		
		// armor
		for (int armorType = 0; armorType < 4; armorType++) {
			this.inventorySlots.add(new SlotArmor(this, inventory, inventory.getSizeInventory() - 1 - armorType, 9, 66 + armorType * 18, armorType));
		}

		// hotbar
		for (x = 0; x < 9; x++) {
			this.inventorySlots.add(new Slot(this, inventory, x, 81 + x * 18, 122));
		}
		
		// creative inventory
		for (int i=0; i<36; i++) {
			
			Slot slot = new SlotCreative(this, i, 9 + i % 12 * 18, 6 + i / 12 * 18);
			this.inventorySlots.add(slot);
			this.creativeSlots.add(slot);
		}
		
		this.controlList.clear();
		this.controlList.add(decPageButton = new GuiButtonText(0, 226, 5, 16, 20, "^"));
		this.controlList.add(incPageButton = new GuiButtonText(1, 226, 27, 16, 20, "v"));
		
		decPageButton.enabled = false;
	}
	
	protected final void actionPerformed(GuiButton button) {
		
		switch (button.id) {
			case 0:
				for (Slot slot : this.creativeSlots) {
					slot.slotIndex -= 36;
				}
				this.page--;
				break;
			case 1:
				for (Slot slot : this.creativeSlots) {
					slot.slotIndex += 36;
				}
				this.page++;
				break;
		}
		
		decPageButton.enabled = !(this.page == 1);
		incPageButton.enabled = !(this.page == this.numPages);
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.page + "/" + this.numPages, 225, 52, 4210752);
	}

	public final void drawScreen(int mouseX, int mouseY) {
		super.drawScreen(mouseX, mouseY);
		
		this.mouseX = (float) mouseX;
		this.mouseY = (float) mouseY;
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
		
		int playerX = var1 + 52;
		int playerY = var2 + 132;
		GL11.glTranslatef((float) playerX, (float) playerY, 50.0F);
		GL11.glScalef(-30.0F, 30.0F, 30.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var3 = this.mc.thePlayer.renderYawOffset;
		float var4 = this.mc.thePlayer.rotationYaw;
		float var5 = this.mc.thePlayer.rotationPitch;
		
		// this is used to calculate the point where the player model should look
		float var6 = (float) playerX - this.mouseX;
		float var7 = (float) playerY - 50 - this.mouseY;
		
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
}