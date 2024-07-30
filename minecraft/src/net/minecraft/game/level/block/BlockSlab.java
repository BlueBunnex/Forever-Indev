package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSlab extends Block {
	
	private boolean isFull;

	public BlockSlab(String name, int blockID, boolean isFull) {
		super(name, blockID, Material.rock);
		
		this.isFull = isFull;
		
		if (!isFull) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public final int getBlockTextureFromSide(int side) {
		return side <= 1 ? 96 : 97;
	}

	public final boolean isOpaqueCube() {
		return this.isFull;
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
//		if(this == Block.stairSingle) {
//		}
	}

	public final void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(this != Block.slabHalf) {
			super.onBlockAdded(var1, var2, var3, var4);
		}

		int var5 = var1.getBlockId(var2, var3 - 1, var4);
		if(var5 == Block.slabHalf.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
			var1.setBlockWithNotify(var2, var3 - 1, var4, Block.slabFull.blockID);
		}

	}

	public final int idDropped(int var1, Random random) {
		return Block.slabHalf.blockID;
	}

	public final boolean renderAsNormalBlock() {
		return this.isFull;
	}

	public final boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		return var5 == 1 ? true : (!super.shouldSideBeRendered(var1, var2, var3, var4, var5) ? false : (var5 == 0 ? true : var1.getBlockId(var2, var3, var4) != this.blockID));
	}
}
