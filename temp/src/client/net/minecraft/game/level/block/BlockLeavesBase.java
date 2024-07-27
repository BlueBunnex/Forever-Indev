package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class BlockLeavesBase extends Block {
	private boolean graphicsLevel = true;

	protected BlockLeavesBase(int blockID, int textureSlot, Material material, boolean z4) {
		super(blockID, textureSlot, material);
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		int i6 = world.getBlockId(xCoord, yCoord, zCoord);
		return !this.graphicsLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(world, xCoord, yCoord, zCoord, i5);
	}
}