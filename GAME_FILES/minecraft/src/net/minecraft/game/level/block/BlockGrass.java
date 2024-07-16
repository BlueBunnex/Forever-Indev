package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockGrass extends Block {
	
	protected BlockGrass(int blockID) {
		super("Grass", blockID, Material.ground);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTextureFromSide(int side) {
		return side == 1 ? 0 : (side == 0 ? 2 : 3);
	}

	public final void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		
		if (var1.getBlockLightValue(var2, var3 + 1, var4) < 4 && var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
			
			if(var5.nextInt(4) == 0)
				var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
			
		} else {
			
			if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9) {
				var2 = var2 + var5.nextInt(3) - 1;
				var3 = var3 + var5.nextInt(5) - 3;
				var4 = var4 + var5.nextInt(3) - 1;
				
				if (var1.getBlockId(var2, var3, var4) == Block.dirt.blockID && var1.getBlockLightValue(var2, var3 + 1, var4) >= 4 && !var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass())
					var1.setBlockWithNotify(var2, var3, var4, Block.grass.blockID);
			}

		}
	}

	public final int idDropped(int var1, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}
