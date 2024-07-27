package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class BlockSand extends Block {
	public BlockSand(int blockID, int textureSlot) {
		super(blockID, textureSlot, Material.sand);
		new Random();
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		this.tryToFall(world, xCoord, yCoord, zCoord);
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		this.tryToFall(world, xCoord, yCoord, zCoord);
	}

	private void tryToFall(World world, int i2, int i3, int i4) {
		int i5 = i3;

		while(true) {
			int i8 = i5 - 1;
			int i6;
			Material material10;
			if(!((i6 = world.getBlockId(i2, i8, i4)) == 0 ? true : (i6 == Block.fire.blockID ? true : ((material10 = Block.blocksList[i6].material) == Material.water ? true : material10 == Material.lava))) || i5 < 0) {
				if(i5 < 0) {
					world.setTileNoUpdate(i2, i3, i4, 0);
				}

				if(i5 != i3) {
					if((i6 = world.getBlockId(i2, i5, i4)) > 0 && Block.blocksList[i6].material != Material.air) {
						world.setTileNoUpdate(i2, i5, i4, 0);
					}

					world.swap(i2, i3, i4, i2, i5, i4);
				}

				return;
			}

			--i5;
			if(world.getBlockId(i2, i5, i4) == Block.fire.blockID) {
				world.setBlock(i2, i5, i4, 0);
			}
		}
	}
}