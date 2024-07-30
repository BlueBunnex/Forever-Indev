package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class Slot {
	
	public int slotIndex;
	
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

	public boolean isItemValid(ItemStack stack) {
		return true;
	}

	/**
	 * 
	 * @param heldStack
	 * @return stack that should become the next heldStack, or null
	 */
	public ItemStack onClickedWithHeldStack(ItemStack heldStack, int mouseClick) {
		
		// if heldStack cannot be put into this slot, don't do anything
		if (heldStack != null && !this.isItemValid(heldStack))
			return heldStack;
		
		ItemStack currContained = this.inventory.getStackInSlot(slotIndex);
		
		// don't do anything if nothing is in either position
		if (currContained == null && heldStack == null)
			return null;
		
		if (mouseClick == 0) {
			
			// merging stacks
			if (currContained != null && heldStack != null && currContained.itemID == heldStack.itemID) {
				
				int stackLimit = currContained.getItem().getItemStackLimit();
				int stackSplit = Math.min(stackLimit - currContained.stackSize, heldStack.stackSize);
				
				heldStack.stackSize     -= stackSplit;
				currContained.stackSize += stackSplit;
				
				return heldStack.stackSize == 0 ? null : heldStack;
			}
			
			// picking up, placing in, or swapping
			this.inventory.setInventorySlotContents(this.slotIndex, heldStack);
			
			this.onPickupFromSlot();
			return currContained;
			
		} else {
			
			// split stack
			if (heldStack == null && currContained != null) {
				
				if (currContained.stackSize == 1) {
					this.inventory.setInventorySlotContents(slotIndex, null);
					
					this.onPickupFromSlot();
					return currContained;
				
				} else {
					this.onPickupFromSlot();
					return currContained.splitStack((currContained.stackSize + 1) / 2);
				}
			
			// depositing one
			} else {
				
				if (currContained == null) {
					
					this.inventory.setInventorySlotContents(slotIndex, heldStack.splitStack(1));
					
				} else if (currContained.itemID == heldStack.itemID && currContained.stackSize < currContained.getItem().getItemStackLimit()) {
					
					heldStack.stackSize--;
					currContained.stackSize++;
				}
				
				return heldStack.stackSize == 0 ? null : heldStack;
			}
		}
	}

	public int getBackgroundIconIndex() {
		return -1;
	}
}
