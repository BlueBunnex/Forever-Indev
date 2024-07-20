package net.minecraft.client.gui.container;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonText;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.IInventory;
import net.minecraft.game.InventorySorter;

import org.lwjgl.opengl.GL11;

public final class GuiChest extends GuiContainer {
	
	private IInventory playerInventory;
	private IInventory chestInventory;
	private int inventoryRows = 0;

	public GuiChest(IInventory playerInventory, IInventory chestInventory) {
		
		this.playerInventory = playerInventory;
		this.chestInventory = chestInventory;
		
		this.allowUserInput = false;
		this.inventoryRows = chestInventory.getSizeInventory() / 9;
		this.ySize = 114 + this.inventoryRows * 18;
		int var3 = (this.inventoryRows - 4) * 18;

		// add rendered inventory slots
		int x, y;
		for (y = 0; y < this.inventoryRows; y++) {
			for (x = 0; x < 9; x++) {
				this.inventorySlots.add(new Slot(this, chestInventory, x + y * 9, 8 + x * 18, 18 + y * 18));
			}
		}

		for (y = 0; y < 3; y++) {
			for (x = 0; x < 9; x++) {
				this.inventorySlots.add(new Slot(this, playerInventory, x + (y + 1) * 9, 8 + x * 18, 103 + y * 18 + var3));
			}
		}

		for (x = 0; x < 9; x++) {
			this.inventorySlots.add(new Slot(this, playerInventory, x, 8 + x * 18, var3 + 161));
		}

		// add sort button
		this.controlList.clear();
		this.controlList.add(new GuiButtonText(0, 180, 5, 50, 20, "sort"));
	}
	
	protected final void actionPerformed(GuiButton button) {
		
		// sort
		InventorySorter.sort(chestInventory);
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.chestInventory.getInvName(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getInvName(), 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int var1 = this.mc.renderEngine.getTexture("/gui/container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(var1);
		var1 = (this.width - this.xSize) / 2;
		int var2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var1, var2, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(var1, var2 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}
