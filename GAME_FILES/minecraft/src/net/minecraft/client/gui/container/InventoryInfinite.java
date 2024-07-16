package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class InventoryInfinite implements IInventory {
	
	private Item[] itemList;
	
	public InventoryInfinite(Item[] itemList) {
		this.itemList = itemList;
	}

	public int getSizeInventory() {
		return itemList.length;
	}

	public ItemStack getStackInSlot(int i) {
		if (i >= getSizeInventory() || i < 0)
			return null;
			
		return new ItemStack(itemList[i]);
	}

	public ItemStack decrStackSize(int i, int amount) {
		return new ItemStack(itemList[i]);
	}

	public void setInventorySlotContents(int i, ItemStack stack) {}

	public String getInvName() {
		return "INFINITE";
	}

	public int getInventoryStackLimit() {
		return 1;
	}

}
