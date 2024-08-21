package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public class ItemFood extends Item {
	
	private int healAmount;

	public ItemFood(String name, int index, int healAmount) {
		super(name, index);
		
		this.healAmount = healAmount;
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {	
		item.stackSize--;
		player.heal(this.healAmount);
		
		return item;
	}
}
