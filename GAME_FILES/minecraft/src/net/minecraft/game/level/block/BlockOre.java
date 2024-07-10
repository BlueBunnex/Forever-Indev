package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.material.Material;

public final class BlockOre extends Block {
	
	public BlockOre(String name, int blockID, int blockIndexInTexture) {
		super(name, blockID, blockIndexInTexture, Material.rock);
	}

	public final int idDropped(int var1, Random random) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : this.blockID);
	}

	public final int quantityDropped(Random random) {
		return 1;
	}
}
