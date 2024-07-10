package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemAxe extends ItemTool {
	
	private static Block[] blocksEffectiveAgainst = new Block[] {
			Block.planks,
			Block.bookShelf,
			Block.wood,
			Block.crate
	};

	public ItemAxe(String name, int index, int var2) {
		super(name, index, 3, var2, blocksEffectiveAgainst);
	}
}
