package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class InventoryInfinite implements IInventory {
	
	private Item[] itemList;
	
	public InventoryInfinite() {
		// testing, will add parameters later
		
		itemList = new Item[1];
		itemList[0] = Item.applePie;
	}

	public int getSizeInventory() {
		return itemList.length;
	}

	public ItemStack getStackInSlot(int i) {
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
