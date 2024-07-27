package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(2, Material.ground);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? 0 : (blockSide == 0 ? 2 : 3);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(world.getBlockLightValue(xCoord, yCoord + 1, zCoord) < 4 && world.getBlockMaterial(xCoord, yCoord + 1, zCoord).getCanBlockGrass()) {
			if(random.nextInt(4) == 0) {
				world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.dirt.blockID);
			}
		} else {
			if(world.getBlockLightValue(xCoord, yCoord + 1, zCoord) >= 9) {
				xCoord = xCoord + random.nextInt(3) - 1;
				yCoord = yCoord + random.nextInt(5) - 3;
				zCoord = zCoord + random.nextInt(3) - 1;
				if(world.getBlockId(xCoord, yCoord, zCoord) == Block.dirt.blockID && world.getBlockLightValue(xCoord, yCoord + 1, zCoord) >= 4 && !world.getBlockMaterial(xCoord, yCoord + 1, zCoord).getCanBlockGrass()) {
					world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.grass.blockID);
				}
			}

		}
	}

	public final int idDropped(int i1, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}