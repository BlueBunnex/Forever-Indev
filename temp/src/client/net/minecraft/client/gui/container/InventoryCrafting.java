package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public final class InventoryCrafting implements IInventory {
	private ItemStack[] stackList;
	private int inventoryWidth;
	private GuiContainer eventHandler;

	public InventoryCrafting(GuiContainer guiContainer, int i2, int i3) {
		this.inventoryWidth = i2 * i3;
		this.stackList = new ItemStack[this.inventoryWidth];
		this.eventHandler = guiContainer;
	}

	public final int getSizeInventory() {
		return this.inventoryWidth;
	}

	public final ItemStack getStackInSlot(int i1) {
		return this.stackList[i1];
	}

	public final String getInvName() {
		return "Crafting";
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		if(this.stackList[i1] != null) {
			ItemStack itemStack3;
			if(this.stackList[i1].stackSize <= i2) {
				itemStack3 = this.stackList[i1];
				this.stackList[i1] = null;
				this.eventHandler.guiCraftingItemsCheck();
				return itemStack3;
			} else {
				itemStack3 = this.stackList[i1].splitStack(i2);
				if(this.stackList[i1].stackSize == 0) {
					this.stackList[i1] = null;
				}

				this.eventHandler.guiCraftingItemsCheck();
				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack) {
		this.stackList[i1] = itemStack;
		this.eventHandler.guiCraftingItemsCheck();
	}

	public final int getInventoryStackLimit() {
		return 64;
	}
}