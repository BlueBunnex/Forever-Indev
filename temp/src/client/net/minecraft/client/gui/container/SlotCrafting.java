package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

final class SlotCrafting extends Slot {
	private final IInventory craftMatrix;

	public SlotCrafting(GuiContainer guiContainer1, IInventory craftMatrix, IInventory iInventory3, int i4, int i5, int i6) {
		super(guiContainer1, iInventory3, 0, i5, i6);
		this.craftMatrix = craftMatrix;
	}

	public final boolean isItemValid(ItemStack itemStack1) {
		return false;
	}

	public final void onPickupFromSlot() {
		for(int i1 = 0; i1 < this.craftMatrix.getSizeInventory(); ++i1) {
			if(this.craftMatrix.getStackInSlot(i1) != null) {
				this.craftMatrix.decrStackSize(i1, 1);
			}
		}

	}
}