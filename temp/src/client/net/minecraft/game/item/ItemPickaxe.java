package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

public final class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond};
	private int harvestLevel;

	public ItemPickaxe(int itemID, int harvestLevel) {
		super(itemID, 2, harvestLevel, blocksEffectiveAgainst);
		this.harvestLevel = harvestLevel;
	}

	public final boolean canHarvestBlock(Block block) {
		return block == Block.obsidian ? this.harvestLevel == 3 : (block != Block.blockDiamond && block != Block.oreDiamond ? (block != Block.blockGold && block != Block.oreGold ? (block != Block.blockSteel && block != Block.oreIron ? (block.material == Material.rock ? true : block.material == Material.iron) : this.harvestLevel > 0) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}