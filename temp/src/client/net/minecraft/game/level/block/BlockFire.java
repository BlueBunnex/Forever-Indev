package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int blockID, int textureSlot) {
		super(51, 31, Material.fire);
		this.setBurnRate(Block.planks.blockID, 5, 20);
		this.setBurnRate(Block.wood.blockID, 5, 5);
		this.setBurnRate(Block.leaves.blockID, 30, 60);
		this.setBurnRate(Block.bookShelf.blockID, 30, 20);
		this.setBurnRate(Block.tnt.blockID, 15, 100);

		for(blockID = 0; blockID < 16; ++blockID) {
			this.setBurnRate(Block.clothRed.blockID + blockID, 30, 60);
		}

		this.setTickOnLoad(true);
	}

	private void setBurnRate(int i1, int i2, int i3) {
		this.chanceToEncourageFire[i1] = i2;
		this.abilityToCatchFire[i1] = i3;
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
		return 3;
	}

	public final int quantityDropped(Random random) {
		return 0;
	}

	public final int tickRate() {
		return 20;
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		byte b6;
		if((b6 = world.getBlockMetadata(xCoord, yCoord, zCoord)) < 15) {
			world.setBlockMetadata(xCoord, yCoord, zCoord, b6 + 1);
			world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockID);
		}

		if(!this.canNeighborCatchFire(world, xCoord, yCoord, zCoord)) {
			if(!world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) || b6 > 3) {
				world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
			}

		} else if(!this.canBlockCatchFire(world, xCoord, yCoord - 1, zCoord) && b6 == 15 && random.nextInt(4) == 0) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
		} else {
			if(b6 % 5 == 0 && b6 > 5) {
				this.tryToCatchBlockOnFire(world, xCoord + 1, yCoord, zCoord, 300, random);
				this.tryToCatchBlockOnFire(world, xCoord - 1, yCoord, zCoord, 300, random);
				this.tryToCatchBlockOnFire(world, xCoord, yCoord - 1, zCoord, 100, random);
				this.tryToCatchBlockOnFire(world, xCoord, yCoord + 1, zCoord, 200, random);
				this.tryToCatchBlockOnFire(world, xCoord, yCoord, zCoord - 1, 300, random);
				this.tryToCatchBlockOnFire(world, xCoord, yCoord, zCoord + 1, 300, random);

				for(int i16 = xCoord - 1; i16 <= xCoord + 1; ++i16) {
					for(int i7 = zCoord - 1; i7 <= zCoord + 1; ++i7) {
						for(int i8 = yCoord - 1; i8 <= yCoord + 4; ++i8) {
							if(i16 != xCoord || i8 != yCoord || i7 != zCoord) {
								int i9 = 100;
								if(i8 > yCoord + 1) {
									i9 = 100 + (i8 - (yCoord + 1)) * 100;
								}

								int i10000;
								if(world.getBlockId(i16, i8, i7) != 0) {
									i10000 = 0;
								} else {
									int i15 = this.getChanceToEncourageFire(world, i16 + 1, i8, i7, 0);
									i15 = this.getChanceToEncourageFire(world, i16 - 1, i8, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i16, i8 - 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i16, i8 + 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world, i16, i8, i7 - 1, i15);
									i10000 = this.getChanceToEncourageFire(world, i16, i8, i7 + 1, i15);
								}

								int i10 = i10000;
								if(i10000 > 0 && random.nextInt(i9) <= i10) {
									world.setBlockWithNotify(i16, i8, i7, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world, int i2, int i3, int i4, int i5, Random random6) {
		int i7 = this.abilityToCatchFire[world.getBlockId(i2, i3, i4)];
		if(random6.nextInt(i5) < i7) {
			boolean z8 = world.getBlockId(i2, i3, i4) == Block.tnt.blockID;
			if(random6.nextInt(2) == 0) {
				world.setBlockWithNotify(i2, i3, i4, this.blockID);
			} else {
				world.setBlockWithNotify(i2, i3, i4, 0);
			}

			if(z8) {
				Block.tnt.onBlockDestroyedByPlayer(world, i2, i3, i4, 0);
			}
		}

	}

	private boolean canNeighborCatchFire(World world, int i2, int i3, int i4) {
		return this.canBlockCatchFire(world, i2 + 1, i3, i4) ? true : (this.canBlockCatchFire(world, i2 - 1, i3, i4) ? true : (this.canBlockCatchFire(world, i2, i3 - 1, i4) ? true : (this.canBlockCatchFire(world, i2, i3 + 1, i4) ? true : (this.canBlockCatchFire(world, i2, i3, i4 - 1) ? true : this.canBlockCatchFire(world, i2, i3, i4 + 1)))));
	}

	public final boolean isCollidable() {
		return false;
	}

	public final boolean canBlockCatchFire(World world, int i2, int i3, int i4) {
		return this.chanceToEncourageFire[world.getBlockId(i2, i3, i4)] > 0;
	}

	private int getChanceToEncourageFire(World world, int i2, int i3, int i4, int i5) {
		int world1;
		return (world1 = this.chanceToEncourageFire[world.getBlockId(i2, i3, i4)]) > i5 ? world1 : i5;
	}

	public final boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
		return world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) || this.canNeighborCatchFire(world, xCoord, yCoord, zCoord);
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		if(!world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) && !this.canNeighborCatchFire(world, xCoord, yCoord, zCoord)) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
		}
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		if(!world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) && !this.canNeighborCatchFire(world, xCoord, yCoord, zCoord)) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
		} else {
			world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockID);
		}
	}

	public final boolean getChanceOfNeighborsEncouragingFire(int i1) {
		return this.chanceToEncourageFire[i1] > 0;
	}

	public final void fireSpread(World world, int xCoord, int yCoord, int zCoord) {
		boolean z5 = false;
		if(!(z5 = fireCheck(world, xCoord, yCoord + 1, zCoord))) {
			z5 = fireCheck(world, xCoord - 1, yCoord, zCoord);
		}

		if(!z5) {
			z5 = fireCheck(world, xCoord + 1, yCoord, zCoord);
		}

		if(!z5) {
			z5 = fireCheck(world, xCoord, yCoord, zCoord - 1);
		}

		if(!z5) {
			z5 = fireCheck(world, xCoord, yCoord, zCoord + 1);
		}

		if(!z5) {
			z5 = fireCheck(world, xCoord, yCoord - 1, zCoord);
		}

		if(!z5) {
			world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.fire.blockID);
		}

	}

	public final void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(random.nextInt(24) == 0) {
			world.playSoundAtPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
		}

		int i6;
		float f7;
		float f8;
		float f9;
		if(!world.isBlockNormalCube(xCoord, yCoord - 1, zCoord) && !Block.fire.canBlockCatchFire(world, xCoord, yCoord - 1, zCoord)) {
			if(Block.fire.canBlockCatchFire(world, xCoord - 1, yCoord, zCoord)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)xCoord + random.nextFloat() * 0.1F;
					f8 = (float)yCoord + random.nextFloat();
					f9 = (float)zCoord + random.nextFloat();
					world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(world, xCoord + 1, yCoord, zCoord)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)(xCoord + 1) - random.nextFloat() * 0.1F;
					f8 = (float)yCoord + random.nextFloat();
					f9 = (float)zCoord + random.nextFloat();
					world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(world, xCoord, yCoord, zCoord - 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)xCoord + random.nextFloat();
					f8 = (float)yCoord + random.nextFloat();
					f9 = (float)zCoord + random.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(world, xCoord, yCoord, zCoord + 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)xCoord + random.nextFloat();
					f8 = (float)yCoord + random.nextFloat();
					f9 = (float)(zCoord + 1) - random.nextFloat() * 0.1F;
					world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
				}
			}

			if(Block.fire.canBlockCatchFire(world, xCoord, yCoord + 1, zCoord)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)xCoord + random.nextFloat();
					f8 = (float)(yCoord + 1) - random.nextFloat() * 0.1F;
					f9 = (float)zCoord + random.nextFloat();
					world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
				}
			}

		} else {
			for(i6 = 0; i6 < 3; ++i6) {
				f7 = (float)xCoord + random.nextFloat();
				f8 = (float)yCoord + random.nextFloat() * 0.5F + 0.5F;
				f9 = (float)zCoord + random.nextFloat();
				world.spawnParticle("largesmoke", f7, f8, f9, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	private static boolean fireCheck(World world, int i1, int i2, int i3) {
		int i4;
		if((i4 = world.getBlockId(i1, i2, i3)) == Block.fire.blockID) {
			return true;
		} else if(i4 == 0) {
			world.setBlockWithNotify(i1, i2, i3, Block.fire.blockID);
			return true;
		} else {
			return false;
		}
	}
}