package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSource extends Block {
	private int fluid;

	protected BlockSource(int blockID, int fluidType) {
		super(blockID, Block.blocksList[fluidType].blockIndexInTexture, Material.water);
		this.fluid = fluidType;
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		super.onBlockAdded(world, xCoord, yCoord, zCoord);
		if(world.getBlockId(xCoord - 1, yCoord, zCoord) == 0) {
			world.setBlockWithNotify(xCoord - 1, yCoord, zCoord, this.fluid);
		}

		if(world.getBlockId(xCoord + 1, yCoord, zCoord) == 0) {
			world.setBlockWithNotify(xCoord + 1, yCoord, zCoord, this.fluid);
		}

		if(world.getBlockId(xCoord, yCoord, zCoord - 1) == 0) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord - 1, this.fluid);
		}

		if(world.getBlockId(xCoord, yCoord, zCoord + 1) == 0) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord + 1, this.fluid);
		}

	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		super.updateTick(world, xCoord, yCoord, zCoord, random);
		if(world.getBlockId(xCoord - 1, yCoord, zCoord) == 0) {
			world.setBlockWithNotify(xCoord - 1, yCoord, zCoord, this.fluid);
		}

		if(world.getBlockId(xCoord + 1, yCoord, zCoord) == 0) {
			world.setBlockWithNotify(xCoord + 1, yCoord, zCoord, this.fluid);
		}

		if(world.getBlockId(xCoord, yCoord, zCoord - 1) == 0) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord - 1, this.fluid);
		}

		if(world.getBlockId(xCoord, yCoord, zCoord + 1) == 0) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord + 1, this.fluid);
		}

	}
}