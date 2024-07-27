package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(6, 15);
		this.setBlockBounds(0.099999994F, 0.0F, 0.099999994F, 0.9F, 0.8F, 0.9F);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		super.updateTick(world, xCoord, yCoord, zCoord, random);
		if(world.getBlockLightValue(xCoord, yCoord + 1, zCoord) >= 9 && random.nextInt(5) == 0) {
			byte random1;
			if((random1 = world.getBlockMetadata(xCoord, yCoord, zCoord)) < 15) {
				world.setBlockMetadata(xCoord, yCoord, zCoord, random1 + 1);
				return;
			}

			world.setTileNoUpdate(xCoord, yCoord, zCoord, 0);
			if(!world.growTrees(xCoord, yCoord, zCoord)) {
				world.setTileNoUpdate(xCoord, yCoord, zCoord, this.blockID);
			}
		}

	}
}