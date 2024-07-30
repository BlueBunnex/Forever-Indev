package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockGlass extends Block {
	
	public BlockGlass(String name, int blockID, int blockIndexInTexture, Material material) {
		super(name, blockID, blockIndexInTexture, material);
	}

//	public final int quantityDropped(Random var1) {
//		return 0;
//	}
	
	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		int var6 = var1.getBlockId(var2, var3, var4);
		return var6 == this.blockID ? false : super.shouldSideBeRendered(var1, var2, var3, var4, var5);
	}
}
