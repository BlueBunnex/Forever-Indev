package net.minecraft.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.game.item.ItemStack;

public final class InventorySorter {
	
	public static final void sort(IInventory inventory) {
		
		sort(inventory, new Comparator<ItemStack>() {

			public int compare(ItemStack o1, ItemStack o2) {
				return o1.itemID - o2.itemID;
			}
			
		});
	}

	public static final void sort(IInventory inventory, Comparator<ItemStack> compare) {
		
		List<ItemStack> itemstacks = new ArrayList<ItemStack>();
		
		for (int i=0; i<inventory.getSizeInventory(); i++) {
			
			ItemStack slotContent = inventory.getStackInSlot(i);
			
			if (slotContent != null) {
				itemstacks.add(slotContent);
				inventory.setInventorySlotContents(i, null);
			}
		}
		
		Collections.sort(itemstacks, compare);
		
		for (int i=0; i<itemstacks.size(); i++) {
			
			inventory.setInventorySlotContents(i, itemstacks.get(i));
		}
	}
	
}
