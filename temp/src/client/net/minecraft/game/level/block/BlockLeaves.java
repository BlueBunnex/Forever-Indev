package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
	protected BlockLeaves(int blockID, int textureSlot) {
		super(18, 52, Material.leaves, true);
		this.setTickOnLoad(true);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(!world.getBlockMaterial(xCoord, yCoord - 1, zCoord).isSolid()) {
			for(int i8 = xCoord - 2; i8 <= xCoord + 2; ++i8) {
				for(int i6 = yCoord - 1; i6 <= yCoord; ++i6) {
					for(int i7 = zCoord - 2; i7 <= zCoord + 2; ++i7) {
						if(world.getBlockId(i8, i6, i7) == Block.wood.blockID) {
							return;
						}
					}
				}
			}

			this.dropBlockAsItem(world, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord));
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
		}
	}

	public final int quantityDropped(Random random) {
		return random.nextInt(10) == 0 ? 1 : 0;
	}

	public final int idDropped(int i1, Random random) {
		return Block.sapling.blockID;
	}
}