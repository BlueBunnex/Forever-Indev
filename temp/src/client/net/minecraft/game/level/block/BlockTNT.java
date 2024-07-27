package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockTNT extends Block {
	public BlockTNT(int blockID, int textureSlot) {
		super(46, 8, Material.tnt);
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 0 ? this.blockIndexInTexture + 2 : (blockSide == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(Random random) {
		return 0;
	}

	public final void onBlockDestroyedByExplosion(World world, int xCoord, int yCoord, int zCoord) {
		EntityTNTPrimed xCoord1;
		(xCoord1 = new EntityTNTPrimed(world, (float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F)).fuse = world.random.nextInt(xCoord1.fuse / 4) + xCoord1.fuse / 8;
		world.spawnEntityInWorld(xCoord1);
	}

	public final void onBlockDestroyedByPlayer(World world, int xCoord, int yCoord, int zCoord, int i5) {
		EntityTNTPrimed xCoord1 = new EntityTNTPrimed(world, (float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F);
		world.spawnEntityInWorld(xCoord1);
		world.playSoundAtEntity(xCoord1, "random.fuse", 1.0F, 1.0F);
	}
}