package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.material.Material;

public final class BlockGlass extends BlockBreakable {
	public BlockGlass(int i1, int i2, Material material3, boolean z4) {
		super(20, 49, material3, false);
	}

	public final int quantityDropped(Random random) {
		return 0;
	}
}