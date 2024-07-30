package net.minecraft.client.gui.container;

import java.util.ArrayList;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class SlotCreative extends Slot {

	private static final InventoryInfinite ALL_ITEMS;
	
	public SlotCreative(GuiContainer guiHandler, int slotIndex, int x, int y) {
		super(guiHandler, ALL_ITEMS, slotIndex, x, y);
	}
	
	public static double getSizeInventory() {
		return ALL_ITEMS.getSizeInventory();
	}
	
	public ItemStack onClickedWithHeldStack(ItemStack heldStack, int mouseClick) {
		
		if (heldStack == null) {
			
			ItemStack toReturn = ALL_ITEMS.getStackInSlot(this.slotIndex);
			
			if (mouseClick == 1)
				toReturn.stackSize = 64;
			
			return toReturn;
			
		// if the heldStack is the same as the one in this slot, increase its size by one
		} else if (heldStack.itemID == this.inventory.getStackInSlot(this.slotIndex).itemID) {
			
			if (heldStack.stackSize < heldStack.getItem().getItemStackLimit())
				heldStack.stackSize++;
			
			return heldStack;
			
		// otherwise clear the heldStack
		} else {
			return null;
		}
	}
	
	private static class InventoryInfinite implements IInventory {
		
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
			return getStackInSlot(i);
		}

		public void setInventorySlotContents(int i, ItemStack stack) {}

		public String getInvName() {
			return "INFINITE";
		}

		public int getInventoryStackLimit() {
			return 1;
		}

	}
	
	static {
		ArrayList<Item> allItems = new ArrayList<Item>();
		
		for (Item item : Item.itemsList) {
			
			if (item != null)
				allItems.add(item);
		}
		
		ALL_ITEMS = new InventoryInfinite(allItems.toArray(new Item[0]));
	}

}
