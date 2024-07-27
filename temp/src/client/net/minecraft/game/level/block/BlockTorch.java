package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class BlockTorch extends Block {
	protected BlockTorch(int blockID, int textureSlot) {
		super(50, 80, Material.circuits);
		this.setTickOnLoad(true);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 2;
	}

	public final boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
		return world.isBlockNormalCube(xCoord - 1, yCoord, zCoord) ? true : (world.isBlockNormalCube(xCoord + 1, yCoord, zCoord) ? true : (world.isBlockNormalCube(xCoord, yCoord, zCoord - 1) ? true : (world.isBlockNormalCube(xCoord, yCoord, zCoord + 1) ? true : world.isBlockNormalCube(xCoord, yCoord - 1, zCoord))));
	}

	public final void onBlockPlaced(World world, int i2, int i3, int i4, int i5) {
		byte b6 = world.getBlockMetadata(i2, i3, i4);
		if(i5 == 1 && world.isBlockNormalCube(i2, i3 - 1, i4)) {
			b6 = 5;
		}

		if(i5 == 2 && world.isBlockNormalCube(i2, i3, i4 + 1)) {
			b6 = 4;
		}

		if(i5 == 3 && world.isBlockNormalCube(i2, i3, i4 - 1)) {
			b6 = 3;
		}

		if(i5 == 4 && world.isBlockNormalCube(i2 + 1, i3, i4)) {
			b6 = 2;
		}

		if(i5 == 5 && world.isBlockNormalCube(i2 - 1, i3, i4)) {
			b6 = 1;
		}

		world.setBlockMetadata(i2, i3, i4, b6);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		super.updateTick(world, xCoord, yCoord, zCoord, random);
		if(world.getBlockMetadata(xCoord, yCoord, zCoord) == 0) {
			this.onBlockAdded(world, xCoord, yCoord, zCoord);
		}

	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		if(world.isBlockNormalCube(xCoord - 1, yCoord, zCoord)) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, 1);
		} else if(world.isBlockNormalCube(xCoord + 1, yCoord, zCoord)) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, 2);
		} else if(world.isBlockNormalCube(xCoord, yCoord, zCoord - 1)) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, 3);
		} else if(world.isBlockNormalCube(xCoord, yCoord, zCoord + 1)) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, 4);
		} else if(world.isBlockNormalCube(xCoord, yCoord - 1, zCoord)) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, 5);
		}

		this.dropTorchIfCantStay(world, xCoord, yCoord, zCoord);
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		if(this.dropTorchIfCantStay(world, xCoord, yCoord, zCoord)) {
			byte blockID1 = world.getBlockMetadata(xCoord, yCoord, zCoord);
			boolean z6 = false;
			if(!world.isBlockNormalCube(xCoord - 1, yCoord, zCoord) && blockID1 == 1) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(xCoord + 1, yCoord, zCoord) && blockID1 == 2) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(xCoord, yCoord, zCoord - 1) && blockID1 == 3) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(xCoord, yCoord, zCoord + 1) && blockID1 == 4) {
				z6 = true;
			}

			if(!world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) && blockID1 == 5) {
				z6 = true;
			}

			if(z6) {
				this.dropBlockAsItem(world, xCoord, yCoord, zCoord, world.getBlockMetadata(xCoord, yCoord, zCoord));
				world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
			}
		}

	}

	private boolean dropTorchIfCantStay(World world1, int i2, int i3, int i4) {
		if(!this.canPlaceBlockAt(world1, i2, i3, i4)) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
			return false;
		} else {
			return true;
		}
	}

	public final MovingObjectPosition collisionRayTrace(World world, int i2, int i3, int i4, Vec3D vec3D, Vec3D vec3D2) {
		byte b7;
		if((b7 = world.getBlockMetadata(i2, i3, i4)) == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.35F, 0.3F, 0.8F, 0.65F);
		} else if(b7 == 2) {
			this.setBlockBounds(0.7F, 0.2F, 0.35F, 1.0F, 0.8F, 0.65F);
		} else if(b7 == 3) {
			this.setBlockBounds(0.35F, 0.2F, 0.0F, 0.65F, 0.8F, 0.3F);
		} else if(b7 == 4) {
			this.setBlockBounds(0.35F, 0.2F, 0.7F, 0.65F, 0.8F, 1.0F);
		} else {
			this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.6F, 0.6F);
		}

		return super.collisionRayTrace(world, i2, i3, i4, vec3D, vec3D2);
	}

	public final void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		byte random1 = world.getBlockMetadata(xCoord, yCoord, zCoord);
		float xCoord1 = (float)xCoord + 0.5F;
		float yCoord1 = (float)yCoord + 0.7F;
		float zCoord1 = (float)zCoord + 0.5F;
		if(random1 == 1) {
			world.spawnParticle("smoke", xCoord1 - 0.27F, yCoord1 + 0.22F, zCoord1, 0.0F, 0.0F, 0.0F);
			world.spawnParticle("flame", xCoord1 - 0.27F, yCoord1 + 0.22F, zCoord1, 0.0F, 0.0F, 0.0F);
		} else if(random1 == 2) {
			world.spawnParticle("smoke", xCoord1 + 0.27F, yCoord1 + 0.22F, zCoord1, 0.0F, 0.0F, 0.0F);
			world.spawnParticle("flame", xCoord1 + 0.27F, yCoord1 + 0.22F, zCoord1, 0.0F, 0.0F, 0.0F);
		} else if(random1 == 3) {
			world.spawnParticle("smoke", xCoord1, yCoord1 + 0.22F, zCoord1 - 0.27F, 0.0F, 0.0F, 0.0F);
			world.spawnParticle("flame", xCoord1, yCoord1 + 0.22F, zCoord1 - 0.27F, 0.0F, 0.0F, 0.0F);
		} else if(random1 == 4) {
			world.spawnParticle("smoke", xCoord1, yCoord1 + 0.22F, zCoord1 + 0.27F, 0.0F, 0.0F, 0.0F);
			world.spawnParticle("flame", xCoord1, yCoord1 + 0.22F, zCoord1 + 0.27F, 0.0F, 0.0F, 0.0F);
		} else {
			world.spawnParticle("smoke", xCoord1, yCoord1, zCoord1, 0.0F, 0.0F, 0.0F);
			world.spawnParticle("flame", xCoord1, yCoord1, zCoord1, 0.0F, 0.0F, 0.0F);
		}
	}
}