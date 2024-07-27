package net.minecraft.client.gui.container;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

final class SlotArmor extends Slot {
	private int armorType;

	SlotArmor(GuiInventory guiInventory1, GuiContainer guiContainer2, IInventory iInventory3, int i4, int i5, int i6, int armorType) {
		super(guiContainer2, iInventory3, i4, 8, i6);
		this.armorType = armorType;
	}

	public final boolean isItemValid(ItemStack itemStack1) {
		return itemStack1.getItem() instanceof ItemArmor ? ((ItemArmor)itemStack1.getItem()).armorType == this.armorType : false;
	}

	public final int getBackgroundIconIndex() {
		return 15 + (this.armorType << 4);
	}
}