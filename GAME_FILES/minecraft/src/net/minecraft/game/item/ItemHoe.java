package net.minecraft.game.item;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.level.material.Material;

public final class ItemHoe extends Item {
	public ItemHoe(String name, int index, int var2) {
		super(name, index);
		
		this.maxStackSize = 1;
		this.maxDamage = 32 << var2;
	}

	public final boolean onItemUse(ItemStack item, World world, int var3, int var4, int var5, int var6) {
		
		if (var3 > 0 && var4 > 0 && var5 > 0 && var3 < world.width - 1 && var4 < world.height - 1 && var5 < world.length - 1) {
			var6 = world.getBlockId(var3, var4, var5);
			Material var7 = world.getBlockMaterial(var3, var4 + 1, var5);
			
			if ((var7.isSolid() || var6 != Block.grass.blockID) && var6 != Block.dirt.blockID) {
				return false;
			} else {
				Block var12 = Block.tilledField;
				float var10001 = (float)var3 + 0.5F;
				float var10002 = (float)var4 + 0.5F;
				float var10003 = (float)var5 + 0.5F;
				String var10004 = var12.stepSound.stepSoundDir2();
				StepSound var8 = var12.stepSound;
				float var10005 = (var8.soundVolume + 1.0F) / 2.0F;
				var8 = var12.stepSound;
				world.playSoundAtPlayer(var10001, var10002, var10003, var10004, var10005, var8.soundPitch * 0.8F);
				world.setBlockWithNotify(var3, var4, var5, var12.blockID);
				
				item.damageItem(1);
				
				if (world.random.nextInt(8) == 0 && var6 == Block.grass.blockID) {
					for (int i = 0; i <= 0; i++) {
						float var10 = world.random.nextFloat() * 0.7F + 0.15F;
						float var13 = world.random.nextFloat() * 0.7F + 0.15F;
						EntityItem var11 = new EntityItem(world, (float) var3 + var10, (float) var4 + 1.2F, (float) var5 + var13, new ItemStack(Item.seeds));
						var11.delayBeforeCanPickup = 10;
						world.spawnEntityInWorld(var11);
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}
}
