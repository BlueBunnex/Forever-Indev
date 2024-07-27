package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFarmland extends Block {
	protected BlockFarmland(int blockID) {
		super(60, Material.ground);
		this.blockIndexInTexture = 87;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return new AxisAlignedBB((float)xCoord, (float)yCoord, (float)zCoord, (float)(xCoord + 1), (float)(yCoord + 1), (float)(zCoord + 1));
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i1 == 1 && i2 > 0 ? this.blockIndexInTexture - 1 : (i1 == 1 ? this.blockIndexInTexture : 2);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(random.nextInt(5) == 0) {
			int i8 = zCoord;
			int i7 = yCoord;
			int i6 = xCoord;
			World world12 = world;
			int i9 = xCoord - 4;

			int i10;
			int i11;
			boolean z10000;
			label69:
			while(true) {
				if(i9 > i6 + 4) {
					z10000 = false;
					break;
				}

				for(i10 = i7; i10 <= i7 + 1; ++i10) {
					for(i11 = i8 - 4; i11 <= i8 + 4; ++i11) {
						if(world12.getBlockMaterial(i9, i10, i11) == Material.water) {
							z10000 = true;
							break label69;
						}
					}
				}

				++i9;
			}

			if(z10000) {
				world.setBlockMetadata(xCoord, yCoord, zCoord, 7);
				return;
			}

			byte b13;
			if((b13 = world.getBlockMetadata(xCoord, yCoord, zCoord)) > 0) {
				world.setBlockMetadata(xCoord, yCoord, zCoord, b13 - 1);
				return;
			}

			i8 = zCoord;
			i7 = yCoord;
			i6 = xCoord;
			world12 = world;
			i10 = xCoord;

			label49:
			while(true) {
				if(i10 > i6) {
					z10000 = false;
					break;
				}

				for(i11 = i8; i11 <= i8; ++i11) {
					if(world12.getBlockId(i10, i7 + 1, i11) == Block.crops.blockID) {
						z10000 = true;
						break label49;
					}
				}

				++i10;
			}

			if(!z10000) {
				world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.dirt.blockID);
			}
		}

	}

	public final void onEntityWalking(World world, int i2, int i3, int i4) {
		if(world.random.nextInt(4) == 0) {
			world.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
		}

	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		super.onNeighborBlockChange(world, xCoord, yCoord, zCoord, blockID);
		if(world.getBlockMaterial(xCoord, yCoord + 1, zCoord).isSolid()) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.dirt.blockID);
		}

	}

	public final int idDropped(int i1, Random random) {
		return Block.dirt.idDropped(0, random);
	}
}