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

	public final boolean onItemUse(ItemStack item, World world, int x, int y, int z, int var6) {
		
		if (world.containsPos(x, y, z)) {
			
			var6 = world.getBlockId(x, y, z);
			
			Material var7 = world.getBlockMaterial(x, y + 1, z);
			
			if ((var7.isSolid() || var6 != Block.grass.blockID) && var6 != Block.dirt.blockID) {
				return false;
			} else {
				Block var12 = Block.tilledField;
				float var10001 = (float) x + 0.5F;
				float var10002 = (float) y + 0.5F;
				float var10003 = (float) z + 0.5F;
				String var10004 = var12.stepSound.stepSoundDir2();
				StepSound var8 = var12.stepSound;
				float var10005 = (var8.soundVolume + 1.0F) / 2.0F;
				var8 = var12.stepSound;
				world.playSoundAtPlayer(var10001, var10002, var10003, var10004, var10005, var8.soundPitch * 0.8F);
				world.setBlockWithNotify(x, y, z, var12.blockID);
				
				item.damageItem(1);
				
				if (var6 == Block.grass.blockID && world.random.nextInt(8) == 0) {
					
					float dx = world.random.nextFloat() * 0.7F + 0.15F;
					float dz = world.random.nextFloat() * 0.7F + 0.15F;
					
					EntityItem spawnItem;
					
					if (world.random.nextInt(2) == 0) {
						spawnItem = new EntityItem(world, (float) x + dx, (float) y + 1.2F, (float) z + dz, new ItemStack(Item.seeds));
					} else {
						spawnItem = new EntityItem(world, (float) x + dx, (float) y + 1.2F, (float) z + dz, new ItemStack(Item.sugarBeet));
					}
					
					spawnItem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(spawnItem);
				}

				return true;
			}
		} else {
			return false;
		}
	}
}
