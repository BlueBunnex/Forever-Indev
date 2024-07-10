package net.minecraft.game.item;

import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.level.World;

public final class ItemPainting extends Item {
	public ItemPainting(String name, int index) {
		super(name, index);
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack item, World world, int var3, int var4, int var5, int var6) {
		if (var6 == 0) {
			return false;
		} else if (var6 == 1) {
			return false;
		} else if (var3 > 0 && var4 > 0 && var5 > 0 && var3 < world.width - 1 && var4 < world.height - 1 && var5 < world.length - 1) {
			byte var7 = 0;
			if(var6 == 4) {
				var7 = 1;
			}

			if(var6 == 3) {
				var7 = 2;
			}

			if(var6 == 5) {
				var7 = 3;
			}

			EntityPainting var8 = new EntityPainting(world, var3, var4, var5, var7);
			
			if (var8.onValidSurface()) {
				world.spawnEntityInWorld(var8);
				item.stackSize--;
			}

			return true;
		} else {
			return false;
		}
	}
}
