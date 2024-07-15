package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

final class SlotArmor extends Slot {
	
	private int armorType;

	SlotArmor(GuiContainer guiHandler, IInventory inventory, int slotIndex, int x, int y, int armorType) {
		super(guiHandler, inventory, slotIndex, x, y);
		this.armorType = armorType;
	}

	public final boolean isItemValid(ItemStack stack) {
		
		if (!(stack.getItem() instanceof ItemArmor))
			return false;
		
		return ((ItemArmor) stack.getItem()).armorType == this.armorType;
	}

	public final int getBackgroundIconIndex() {
		return 15 + (this.armorType << 4);
	}
}
