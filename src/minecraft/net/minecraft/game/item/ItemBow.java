package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

public final class ItemBow extends Item {
	
	public ItemBow(String name, int index) {
		super(name, index);
		this.maxStackSize = 1;
	}

	public final ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		
		if (player.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			
			world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			world.spawnEntityInWorld(new EntityArrow(world, player));
		}

		return item;
	}
	
}
