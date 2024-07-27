package net.minecraft.game.level.block;

import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockGears extends Block {
	
	protected BlockGears(int blockID) {
		super("Gear", blockID, 62, Material.circuits);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
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

	public final boolean isCollidable() {
		return false;
	}
}
