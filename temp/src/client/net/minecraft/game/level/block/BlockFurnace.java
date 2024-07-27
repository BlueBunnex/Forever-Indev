package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;
import net.minecraft.game.level.material.Material;

public final class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int blockID, boolean status) {
		super(blockID, Material.rock);
		this.isActive = status;
		this.blockIndexInTexture = 45;
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		super.onBlockAdded(world, xCoord, yCoord, zCoord);
		setDefaultDirection(world, xCoord, yCoord, zCoord);
	}

	private static void setDefaultDirection(World world, int xCoord, int yCoord, int zCoord) {
		int i4 = world.getBlockId(xCoord, yCoord, zCoord - 1);
		int i5 = world.getBlockId(xCoord, yCoord, zCoord + 1);
		int i6 = world.getBlockId(xCoord - 1, yCoord, zCoord);
		int i7 = world.getBlockId(xCoord + 1, yCoord, zCoord);
		byte b8 = 3;
		if(Block.opaqueCubeLookup[i4] && !Block.opaqueCubeLookup[i5]) {
			b8 = 3;
		}

		if(Block.opaqueCubeLookup[i5] && !Block.opaqueCubeLookup[i4]) {
			b8 = 2;
		}

		if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
			b8 = 5;
		}

		if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
			b8 = 4;
		}

		world.setBlockMetadata(xCoord, yCoord, zCoord, b8);
	}

	public final int getBlockTexture(World world, int xCoord, int yCoord, int zCoord, int blockSide) {
		if(blockSide == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(blockSide == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			byte b6;
			if((b6 = world.getBlockMetadata(xCoord, yCoord, zCoord)) == 0) {
				setDefaultDirection(world, xCoord, yCoord, zCoord);
				b6 = world.getBlockMetadata(xCoord, yCoord, zCoord);
			}

			return blockSide != b6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public final void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(this.isActive) {
			byte b6 = world.getBlockMetadata(xCoord, yCoord, zCoord);
			float xCoord1 = (float)xCoord + 0.5F;
			float yCoord1 = (float)yCoord + random.nextFloat() * 6.0F / 16.0F;
			float zCoord1 = (float)zCoord + 0.5F;
			float random1 = random.nextFloat() * 0.6F - 0.3F;
			if(b6 == 4) {
				world.spawnParticle("smoke", xCoord1 - 0.52F, yCoord1, zCoord1 + random1, 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", xCoord1 - 0.52F, yCoord1, zCoord1 + random1, 0.0F, 0.0F, 0.0F);
			} else if(b6 == 5) {
				world.spawnParticle("smoke", xCoord1 + 0.52F, yCoord1, zCoord1 + random1, 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", xCoord1 + 0.52F, yCoord1, zCoord1 + random1, 0.0F, 0.0F, 0.0F);
			} else if(b6 == 2) {
				world.spawnParticle("smoke", xCoord1 + random1, yCoord1, zCoord1 - 0.52F, 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", xCoord1 + random1, yCoord1, zCoord1 - 0.52F, 0.0F, 0.0F, 0.0F);
			} else {
				if(b6 == 3) {
					world.spawnParticle("smoke", xCoord1 + random1, yCoord1, zCoord1 + 0.52F, 0.0F, 0.0F, 0.0F);
					world.spawnParticle("flame", xCoord1 + random1, yCoord1, zCoord1 + 0.52F, 0.0F, 0.0F, 0.0F);
				}

			}
		}
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? Block.stone.blockID : (blockSide == 0 ? Block.stone.blockID : (blockSide == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public final boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer player) {
		TileEntityFurnace world1 = (TileEntityFurnace)world.getBlockTileEntity(xCoord, yCoord, zCoord);
		player.displayGUIFurnace(world1);
		return true;
	}

	protected final TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}