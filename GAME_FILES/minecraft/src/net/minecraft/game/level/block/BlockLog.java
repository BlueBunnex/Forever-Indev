package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.material.Material;

public final class BlockLog extends Block {
	protected BlockLog() {
		super("Log", 17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(Random random) {
		return 1;
	}

	public final int idDropped(int var1, Random random) {
		return Block.wood.blockID;
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? 21 : (side == 0 ? 21 : 20);
	}
}
