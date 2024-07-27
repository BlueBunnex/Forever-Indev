package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.material.Material;

public final class BlockLog extends Block {
	protected BlockLog(int blockID) {
		super(17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(Random random) {
		return 1;
	}

	public final int idDropped(int i1, Random random) {
		return Block.wood.blockID;
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? 21 : (blockSide == 0 ? 21 : 20);
	}
}