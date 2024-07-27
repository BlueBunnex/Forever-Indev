package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;

public final class ItemSoup extends ItemFood {
	public ItemSoup(int i1, int i2) {
		super(26, 10);
	}

	public final ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onItemRightClick(itemStack, world, entityPlayer);
		return new ItemStack(Item.bowlEmpty);
	}
}