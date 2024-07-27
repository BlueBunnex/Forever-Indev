package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.material.Material;

public final class BlockStone extends Block {
	public BlockStone(int blockID, int textureSlot) {
		super(blockID, textureSlot, Material.rock);
	}

	public final int idDropped(int i1, Random random) {
		return Block.cobblestone.blockID;
	}
}