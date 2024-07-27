package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int i1) {
		super(3);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack itemStack, World world, int i3, int i4, int i5, int i6) {
		if(i6 == 0) {
			--i4;
		}

		if(i6 == 1) {
			++i4;
		}

		if(i6 == 2) {
			--i5;
		}

		if(i6 == 3) {
			++i5;
		}

		if(i6 == 4) {
			--i3;
		}

		if(i6 == 5) {
			++i3;
		}

		if(i3 > 0 && i4 > 0 && i5 > 0 && i3 < world.width - 1 && i4 < world.height - 1 && i5 < world.length - 1) {
			if(world.getBlockId(i3, i4, i5) == 0) {
				world.playSoundAtPlayer((float)i3 + 0.5F, (float)i4 + 0.5F, (float)i5 + 0.5F, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
				world.setBlockWithNotify(i3, i4, i5, Block.fire.blockID);
			}

			itemStack.damageItem(1);
			return true;
		} else {
			return false;
		}
	}
}