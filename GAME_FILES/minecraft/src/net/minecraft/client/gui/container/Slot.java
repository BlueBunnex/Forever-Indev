package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	
	public final int slotIndex;
	public final int xPos;
	public final int yPos;
	public final IInventory inventory;
	private final GuiContainer guiHandler;

	public Slot(GuiContainer guiHandler, IInventory inventory, int slotIndex, int x, int y) {
		this.guiHandler = guiHandler;
		this.inventory = inventory;
		this.slotIndex = slotIndex;
		this.xPos = x;
		this.yPos = y;
	}

	public final boolean isAtCursorPos(int x, int y) {
		int cornerX = (this.guiHandler.width - this.guiHandler.xSize) / 2;
		int cornerY = (this.guiHandler.height - this.guiHandler.ySize) / 2;
		x -= cornerX;
		y -= cornerY;
		return
				x >= this.xPos - 1
			 && x < this.xPos + 16 + 1
			 && y >= this.yPos - 1
			 && y < this.yPos + 16 + 1;
	}

	public void onPickupFromSlot() {}

	public boolean isItemValid(ItemStack var1) {
		return true;
	}

	public final void putStack(ItemStack stack) {
		this.inventory.setInventorySlotContents(this.slotIndex, stack);
	}

	public int getBackgroundIconIndex() {
		return -1;
	}
}
