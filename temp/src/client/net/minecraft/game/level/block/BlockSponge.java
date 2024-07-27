package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSponge extends Block {
	protected BlockSponge(int blockID) {
		super(19, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		for(int i5 = xCoord - 2; i5 <= xCoord + 2; ++i5) {
			for(int i6 = yCoord - 2; i6 <= yCoord + 2; ++i6) {
				for(int i7 = zCoord - 2; i7 <= zCoord + 2; ++i7) {
					if(world.isWater(i5, i6, i7)) {
						world.setBlock(i5, i6, i7, 0);
					}
				}
			}
		}

	}

	public final void onBlockRemoval(World world, int xCoord, int yCoord, int zCoord) {
		for(int i5 = xCoord - 2; i5 <= xCoord + 2; ++i5) {
			for(int i6 = yCoord - 2; i6 <= yCoord + 2; ++i6) {
				for(int i7 = zCoord - 2; i7 <= zCoord + 2; ++i7) {
					world.notifyBlocksOfNeighborChange(i5, i6, i7, world.getBlockId(i5, i6, i7));
				}
			}
		}

	}
}