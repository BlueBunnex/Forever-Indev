package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSpade extends ItemTool {
	
	private static Block[] blocksEffectiveAgainst = new Block[] {
			Block.grass,
			Block.dirt,
			Block.sand,
			Block.gravel
	};

	public ItemSpade(String name, int index, int var2) {
		super(name, index, 1, var2, blocksEffectiveAgainst);
	}
}
