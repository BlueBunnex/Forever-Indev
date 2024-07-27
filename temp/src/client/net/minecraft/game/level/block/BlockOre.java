package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.item.Item;
import net.minecraft.game.level.material.Material;

public final class BlockOre extends Block {
	public BlockOre(int blockID, int textureSlot) {
		super(blockID, textureSlot, Material.rock);
	}

	public final int idDropped(int i1, Random random) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public final int quantityDropped(Random random) {
		return 1;
	}
}