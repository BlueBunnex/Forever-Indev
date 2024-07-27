package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.physics.AxisAlignedBB;

public final class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
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

		if(itemStack.stackSize == 0) {
			return false;
		} else if(i3 > 0 && i4 > 0 && i5 > 0 && i3 < world.width - 1 && i4 < world.height - 1 && i5 < world.length - 1) {
			int i7 = world.getBlockId(i3, i4, i5);
			Block block10 = Block.blocksList[i7];
			if(this.blockID > 0 && block10 == null || block10 == Block.waterMoving || block10 == Block.waterStill || block10 == Block.lavaMoving || block10 == Block.lavaStill || block10 == Block.fire) {
				AxisAlignedBB axisAlignedBB8 = (block10 = Block.blocksList[this.blockID]).getCollisionBoundingBoxFromPool(i3, i4, i5);
				if(world.checkIfAABBIsClear(axisAlignedBB8) && block10.canPlaceBlockAt(world, i3, i4, i5) && world.setBlockWithNotify(i3, i4, i5, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(world, i3, i4, i5, i6);
					float f10001 = (float)i3 + 0.5F;
					float f10002 = (float)i4 + 0.5F;
					float f10003 = (float)i5 + 0.5F;
					String string10004 = block10.stepSound.stepSoundDir2();
					StepSound stepSound9 = block10.stepSound;
					float f10005 = (block10.stepSound.soundVolume + 1.0F) / 2.0F;
					stepSound9 = block10.stepSound;
					world.playSoundAtPlayer(f10001, f10002, f10003, string10004, f10005, block10.stepSound.soundPitch * 0.8F);
					--itemStack.stackSize;
				}
			}

			return true;
		} else {
			return false;
		}
	}
}