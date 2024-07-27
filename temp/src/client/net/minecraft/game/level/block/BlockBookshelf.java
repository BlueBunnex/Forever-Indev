package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.material.Material;

public final class BlockBookshelf extends Block {
	public BlockBookshelf(int blockID, int textureSlot) {
		super(47, 35, Material.wood);
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(Random random) {
		return 0;
	}
}