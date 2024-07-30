package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(String name, int index) {
		super(name, index);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack item, World world, int var3, int var4, int var5, int var6) {
		if(var6 == 0) {
			--var4;
		}

		if(var6 == 1) {
			++var4;
		}

		if(var6 == 2) {
			--var5;
		}

		if(var6 == 3) {
			++var5;
		}

		if(var6 == 4) {
			--var3;
		}

		if(var6 == 5) {
			++var3;
		}

		if (var3 > 0 && var4 > 0 && var5 > 0 && var3 < world.width - 1 && var4 < world.height - 1 && var5 < world.length - 1) {
			
			var6 = world.getBlockId(var3, var4, var5);
			if (var6 == 0) {
				world.playSoundAtPlayer((float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5 + 0.5F, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
				world.setBlockWithNotify(var3, var4, var5, Block.fire.blockID);
			}

			item.damageItem(1);
			return true;
		} else {
			return false;
		}
	}
}
