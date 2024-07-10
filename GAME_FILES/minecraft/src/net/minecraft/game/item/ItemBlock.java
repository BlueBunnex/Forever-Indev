package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.physics.AxisAlignedBB;

public final class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(String name, int index) {
		super(name, index);
		this.blockID = index + 256;
		this.setIconIndex(Block.blocksList[index + 256].getBlockTextureFromSide(2));
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

		if (item.stackSize == 0) {
			return false;
			
		} else if (var3 > 0 && var4 > 0 && var5 > 0 && var3 < world.width - 1 && var4 < world.height - 1 && var5 < world.length - 1) {
			
			int var7 = world.getBlockId(var3, var4, var5);
			Block var10 = Block.blocksList[var7];
			
			if (this.blockID > 0 && var10 == null || var10 == Block.waterMoving || var10 == Block.waterStill || var10 == Block.lavaMoving || var10 == Block.lavaStill || var10 == Block.fire) {
				var10 = Block.blocksList[this.blockID];
				AxisAlignedBB var8 = var10.getCollisionBoundingBoxFromPool(var3, var4, var5);
				if(world.checkIfAABBIsClear(var8) && var10.canPlaceBlockAt(world, var3, var4, var5) && world.setBlockWithNotify(var3, var4, var5, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world, var3, var4, var5, var6);
					float var10001 = (float)var3 + 0.5F;
					float var10002 = (float)var4 + 0.5F;
					float var10003 = (float)var5 + 0.5F;
					String var10004 = var10.stepSound.stepSoundDir2();
					StepSound var9 = var10.stepSound;
					float var10005 = (var9.soundVolume + 1.0F) / 2.0F;
					var9 = var10.stepSound;
					world.playSoundAtPlayer(var10001, var10002, var10003, var10004, var10005, var9.soundPitch * 0.8F);
					--item.stackSize;
				}
			}
			return true;
			
		} else {
			return false;
		}
	}
}
