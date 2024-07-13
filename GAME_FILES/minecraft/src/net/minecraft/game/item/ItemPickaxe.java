package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

public final class ItemPickaxe extends ItemTool {
	
	private static Block[] blocksEffectiveAgainst = new Block[] {
			Block.cobblestone,
			Block.slabFull,
			Block.slabHalf,
			Block.stone,
			Block.cobblestoneMossy,
			Block.oreIron,
			Block.blockIron,
			Block.oreCoal,
			Block.blockGold,
			Block.oreGold,
			Block.oreDiamond,
			Block.blockDiamond
	};
	
	private int harvestLevel;

	public ItemPickaxe(String name, int index, int var2) {
		super(name, index, 2, var2, blocksEffectiveAgainst);
		this.harvestLevel = var2;
	}

	public final boolean canHarvestBlock(Block block) {
		return block == Block.obsidian ? this.harvestLevel == 3 : (block != Block.blockDiamond && block != Block.oreDiamond ? (block != Block.blockGold && block != Block.oreGold ? (block != Block.blockIron && block != Block.oreIron ? (block.material == Material.rock ? true : block.material == Material.iron) : this.harvestLevel > 0) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}
