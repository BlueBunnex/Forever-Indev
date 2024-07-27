package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockGears extends Block {
	protected BlockGears(int blockID, int textureSlot) {
		super(55, 62, Material.circuits);
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

	public final int getRenderType() {
		return 5;
	}

	public final int quantityDropped(Random random) {
		return 1;
	}

	public final boolean isCollidable() {
		return false;
	}
}