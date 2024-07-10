package net.minecraft.game.level.block;

import net.minecraft.game.level.material.Material;

public final class BlockOreBlock extends Block {
	
	public BlockOreBlock(String name, int blockID, int blockIndexInTexture) {
		super(name, blockID, blockIndexInTexture, Material.iron);
	}

	public final int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? this.blockIndexInTexture - 16 : (var1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}
