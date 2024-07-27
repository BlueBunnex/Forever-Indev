package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

public final class ItemBow extends Item {
	public ItemBow(int i1) {
		super(5);
		this.maxStackSize = 1;
	}

	public final ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(entityPlayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world.playSoundAtEntity(entityPlayer, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			world.spawnEntityInWorld(new EntityArrow(world, entityPlayer));
		}

		return itemStack;
	}
}