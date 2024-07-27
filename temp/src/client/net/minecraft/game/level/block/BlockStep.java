package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int blockID, boolean z2) {
		super(blockID, 6, Material.rock);
		this.blockType = z2;
		if(!z2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide <= 1 ? 6 : 5;
	}

	public final boolean isOpaqueCube() {
		return this.blockType;
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		if(this == Block.stairSingle) {
			;
		}
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world, xCoord, yCoord, zCoord);
		}

		if(world.getBlockId(xCoord, yCoord - 1, zCoord) == stairSingle.blockID) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
			world.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.stairDouble.blockID);
		}

	}

	public final int idDropped(int i1, Random random) {
		return Block.stairSingle.blockID;
	}

	public final boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public final boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		return i5 == 1 ? true : (!super.shouldSideBeRendered(world, xCoord, yCoord, zCoord, i5) ? false : (i5 == 0 ? true : world.getBlockId(xCoord, yCoord, zCoord) != this.blockID));
	}
}