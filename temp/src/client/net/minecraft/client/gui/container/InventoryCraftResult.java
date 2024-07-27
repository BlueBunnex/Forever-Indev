package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class InventoryCraftResult implements IInventory {
	private ItemStack[] stackResult = new ItemStack[1];

	public final int getSizeInventory() {
		return 1;
	}

	public final ItemStack getStackInSlot(int i1) {
		return this.stackResult[i1];
	}

	public final String getInvName() {
		return "Result";
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		if(this.stackResult[i1] != null) {
			ItemStack itemStack3 = this.stackResult[i1];
			this.stackResult[i1] = null;
			return itemStack3;
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack) {
		this.stackResult[i1] = itemStack;
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}