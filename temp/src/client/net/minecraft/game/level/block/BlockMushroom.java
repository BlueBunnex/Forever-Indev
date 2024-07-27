package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int i1, int i2) {
		super(i1, i2);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return Block.opaqueCubeLookup[blockID];
	}

	public final boolean canBlockStay(World world, int xCoord, int yCoord, int zCoord) {
		if(world.getBlockLightValue(xCoord, yCoord, zCoord) <= 13) {
			xCoord = world.getBlockId(xCoord, yCoord - 1, zCoord);
			if(Block.opaqueCubeLookup[xCoord]) {
				return true;
			}
		}

		return false;
	}
}