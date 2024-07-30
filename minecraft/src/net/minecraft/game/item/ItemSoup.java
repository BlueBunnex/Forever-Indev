package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public final class ItemSoup extends ItemFood {
	
	public ItemSoup(String name, int index, int healAmount) {
		super(name, index, healAmount);
	}

	public final ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		
		super.onItemRightClick(item, world, player);
		
		return new ItemStack(Item.bowlEmpty);
	}
}
