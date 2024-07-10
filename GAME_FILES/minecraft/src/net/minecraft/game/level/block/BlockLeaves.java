package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
	
	protected BlockLeaves() {
		super("Leaves", 18, 52, Material.leaves);
		this.setTickOnLoad(true);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		
		if (!var1.getBlockMaterial(var2, var3 - 1, var4).isSolid()) {
			
			for (int var8 = var2 - 2; var8 <= var2 + 2; ++var8) {
				for (int var6 = var3 - 1; var6 <= var3; ++var6) {
					for (int var7 = var4 - 2; var7 <= var4 + 2; ++var7) {
						if(var1.getBlockId(var8, var6, var7) == Block.wood.blockID) {
							return;
						}
					}
				}
			}

			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
	}

	public final int quantityDropped(Random random) {
		return random.nextInt(8) == 0 ? 1 : 0;
	}

	public final int idDropped(int var1, Random random) {
		return random.nextInt(2) == 0 ? Block.sapling.blockID : Item.apple.shiftedIndex;
	}
}
