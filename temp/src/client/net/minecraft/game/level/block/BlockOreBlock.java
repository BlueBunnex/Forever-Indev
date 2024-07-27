package net.minecraft.game.level.block;

import net.minecraft.game.level.material.Material;

public final class BlockOreBlock extends Block {
	public BlockOreBlock(int blockID, int oreSideTexture) {
		super(blockID, Material.iron);
		this.blockIndexInTexture = oreSideTexture;
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? this.blockIndexInTexture - 16 : (blockSide == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}