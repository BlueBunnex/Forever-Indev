package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.material.Material;

public abstract class BlockContainer extends Block {
	protected BlockContainer(int i1, Material material2) {
		super(i1, material2);
	}

	public void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		super.onBlockAdded(world, xCoord, yCoord, zCoord);
		world.setBlockTileEntity(xCoord, yCoord, zCoord, this.getBlockEntity());
	}

	public void onBlockRemoval(World world, int xCoord, int yCoord, int zCoord) {
		super.onBlockRemoval(world, xCoord, yCoord, zCoord);
		world.removeBlockTileEntity(xCoord, yCoord, zCoord);
	}

	protected abstract TileEntity getBlockEntity();
}