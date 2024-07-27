package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int blockID, int textureSlot, Material material, boolean localFlag) {
		super(blockID, textureSlot, material);
		this.localFlag = localFlag;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		int i6 = world.getBlockId(xCoord, yCoord, zCoord);
		return !this.localFlag && i6 == this.blockID ? false : super.shouldSideBeRendered(world, xCoord, yCoord, zCoord, i5);
	}
}