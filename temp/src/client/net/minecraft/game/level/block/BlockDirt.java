package net.minecraft.game.level.block;

import net.minecraft.game.level.material.Material;

public final class BlockDirt extends Block {
	protected BlockDirt(int blockID, int textureSlot) {
		super(3, 2, Material.ground);
	}
}