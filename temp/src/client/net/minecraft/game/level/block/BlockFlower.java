package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFlower extends Block {
	protected BlockFlower(int blockID, int textureSlot) {
		super(blockID, Material.plants);
		this.blockIndexInTexture = textureSlot;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
	}

	public final boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
		return this.canThisPlantGrowOnThisBlockID(world.getBlockId(xCoord, yCoord - 1, zCoord));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.grass.blockID || blockID == Block.dirt.blockID || blockID == Block.tilledField.blockID;
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		super.onNeighborBlockChange(world, xCoord, yCoord, zCoord, blockID);
		this.checkFlowerChange(world, xCoord, yCoord, zCoord);
	}

	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		this.checkFlowerChange(world, xCoord, yCoord, zCoord);
	}

	private void checkFlowerChange(World world, int xCoord, int yCoord, int zCoord) {
		if(!this.canBlockStay(world, xCoord, yCoord, zCoord)) {
			this.dropBlockAsItem(world, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord));
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
		}

	}

	public boolean canBlockStay(World world, int xCoord, int yCoord, int zCoord) {
		return (world.getBlockLightValue(xCoord, yCoord, zCoord) >= 8 || world.getBlockLightValue(xCoord, yCoord, zCoord) >= 4 && world.canBlockSeeTheSky(xCoord, yCoord, zCoord)) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(xCoord, yCoord - 1, zCoord));
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}