package net.minecraft.client.gui.container;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;

import org.lwjgl.opengl.GL11;

public final class GuiFurnace extends GuiContainer {
	private TileEntityFurnace furnaceInventory;

	public GuiFurnace(InventoryPlayer playerInventory, TileEntityFurnace furnace) {
		new InventoryCraftResult();
		this.furnaceInventory = furnace;
		this.inventorySlots.add(new Slot(this, furnace, 0, 56, 17));
		this.inventorySlots.add(new Slot(this, furnace, 1, 56, 53));
		this.inventorySlots.add(new Slot(this, furnace, 2, 116, 35));

		int i4;
		for(i4 = 0; i4 < 3; ++i4) {
			for(int i3 = 0; i3 < 9; ++i3) {
				this.inventorySlots.add(new Slot(this, playerInventory, i3 + (i4 + 1) * 9, 8 + i3 * 18, 84 + i4 * 18));
			}
		}

		for(i4 = 0; i4 < 9; ++i4) {
			this.inventorySlots.add(new Slot(this, playerInventory, i4, 8 + i4 * 18, 142));
		}

	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Furnace", 60, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int i1 = this.mc.renderEngine.getTexture("/gui/furnace.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(i1);
		i1 = (this.width - this.xSize) / 2;
		int i2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.ySize);
		int i3;
		if(this.furnaceInventory.isBurning()) {
			i3 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(i1 + 56, i2 + 36 + 12 - i3, 176, 12 - i3, 14, i3 + 2);
		}

		i3 = this.furnaceInventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(i1 + 79, i2 + 34, 176, 14, i3 + 1, 16);
	}
}