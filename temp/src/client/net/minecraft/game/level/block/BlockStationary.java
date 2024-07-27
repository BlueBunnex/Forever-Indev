package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.movingId = i1 - 1;
		this.stillId = i1;
		this.setTickOnLoad(false);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		boolean z6 = false;
		if(this.canFlow(world, xCoord, yCoord - 1, zCoord)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, xCoord - 1, yCoord, zCoord)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, xCoord + 1, yCoord, zCoord)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, xCoord, yCoord, zCoord - 1)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world, xCoord, yCoord, zCoord + 1)) {
			z6 = true;
		}

		if(blockID != 0) {
			Material material7 = Block.blocksList[blockID].material;
			if(this.material == Material.water && material7 == Material.lava || material7 == Material.water && this.material == Material.lava) {
				world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.stone.blockID);
				return;
			}
		}

		if(Block.fire.getChanceOfNeighborsEncouragingFire(blockID)) {
			z6 = true;
		}

		if(z6) {
			world.setTileNoUpdate(xCoord, yCoord, zCoord, this.movingId);
			world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.movingId);
		}

	}
}