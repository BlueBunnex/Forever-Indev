package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	public final int slotIndex;
	public final int xPos;
	public final int yPos;
	public final IInventory inventory;
	private final GuiContainer guiHandler;

	public Slot(GuiContainer guiContainer1, IInventory iInventory2, int slotIndex, int xPos, int yPos) {
		this.guiHandler = guiContainer1;
		this.inventory = iInventory2;
		this.slotIndex = slotIndex;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public final boolean isAtCursorPos(int i1, int i2) {
		int i3 = (this.guiHandler.width - this.guiHandler.xSize) / 2;
		int i4 = (this.guiHandler.height - this.guiHandler.ySize) / 2;
		i1 -= i3;
		i2 -= i4;
		return i1 >= this.xPos - 1 && i1 < this.xPos + 16 + 1 && i2 >= this.yPos - 1 && i2 < this.yPos + 16 + 1;
	}

	public void onPickupFromSlot() {
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return true;
	}

	public final void putStack(ItemStack itemStack1) {
		this.inventory.setInventorySlotContents(this.slotIndex, itemStack1);
	}

	public int getBackgroundIconIndex() {
		return -1;
	}
}