package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public class ItemFood extends Item {
	private int healAmount;

	public ItemFood(int itemID, int healAmount) {
		super(itemID);
		this.healAmount = healAmount;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		--itemStack.stackSize;
		entityPlayer.heal(this.healAmount);
		return itemStack;
	}
}