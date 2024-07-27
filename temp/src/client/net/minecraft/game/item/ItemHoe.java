package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public final class ItemHoe extends Item {
	public ItemHoe(int itemID, int damage) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 32 << damage;
	}

	public final boolean onItemUse(ItemStack itemStack, World world, int i3, int i4, int i5, int i6) {
		if(i3 > 0 && i4 > 0 && i5 > 0 && i3 < world.width - 1 && i4 < world.height - 1 && i5 < world.length - 1) {
			i6 = world.getBlockId(i3, i4, i5);
			if((world.getBlockMaterial(i3, i4 + 1, i5).isSolid() || i6 != Block.grass.blockID) && i6 != Block.dirt.blockID) {
				return false;
			} else {
				Block block7 = Block.tilledField;
				float f10001 = (float)i3 + 0.5F;
				float f10002 = (float)i4 + 0.5F;
				float f10003 = (float)i5 + 0.5F;
				String string10004 = block7.stepSound.stepSoundDir2();
				StepSound stepSound8 = block7.stepSound;
				float f10005 = (block7.stepSound.soundVolume + 1.0F) / 2.0F;
				stepSound8 = block7.stepSound;
				world.playSoundAtPlayer(f10001, f10002, f10003, string10004, f10005, block7.stepSound.soundPitch * 0.8F);
				world.setBlockWithNotify(i3, i4, i5, block7.blockID);
				itemStack.damageItem(1);
				if(world.random.nextInt(8) == 0 && i6 == Block.grass.blockID) {
					for(int i9 = 0; i9 <= 0; ++i9) {
						float f10 = world.random.nextFloat() * 0.7F + 0.15F;
						float f12 = world.random.nextFloat() * 0.7F + 0.15F;
						EntityItem entityItem11;
						(entityItem11 = new EntityItem(world, (float)i3 + f10, (float)i4 + 1.2F, (float)i5 + f12, new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
						world.spawnEntityInWorld(entityItem11);
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}
}