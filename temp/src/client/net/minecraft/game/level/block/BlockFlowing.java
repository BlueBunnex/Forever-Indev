package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockFlowing extends BlockFluid {
	private int stillId1;
	private int movingId1;
	private Random rand = new Random();
	private int[] liquidIntArray = new int[]{0, 1, 2, 3};

	protected BlockFlowing(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockFluid[i1] = true;
		this.movingId1 = i1;
		this.stillId1 = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.movingId1);
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		this.update(world, xCoord, yCoord, zCoord, 0);
	}

	public final boolean update(World world, int xCoord, int yCoord, int zCoord, int i5) {
		boolean z10 = false;
		boolean z6;
		int i7;
		int i8;
		int i9;
		if((z6 = this.canFlow(world, xCoord - 1, yCoord, zCoord) || this.canFlow(world, xCoord + 1, yCoord, zCoord) || this.canFlow(world, xCoord, yCoord, zCoord - 1) || this.canFlow(world, xCoord, yCoord, zCoord + 1)) && world.getBlockMaterial(xCoord, yCoord - 1, zCoord) == this.material && world.floodFill(xCoord, yCoord - 1, zCoord, this.movingId1, this.stillId1) == 1) {
			if((i7 = world.fluidFlowCheck(xCoord, yCoord, zCoord, this.movingId1, this.stillId1)) != -9999) {
				if(i7 < 0) {
					return false;
				} else {
					i8 = i7 % 1024;
					i9 = (i7 >>= 10) % 1024;
					xCoord = (i7 >>= 10) % 1024;
					world.setBlockWithNotify(i8, xCoord, i9, 0);
					return false;
				}
			} else {
				return false;
			}
		} else {
			z10 = this.liquidSpread2(world, xCoord, yCoord, zCoord, xCoord, yCoord - 1, zCoord);

			for(i7 = 0; i7 < 4; ++i7) {
				i8 = this.rand.nextInt(4 - i7) + i7;
				i9 = this.liquidIntArray[i7];
				this.liquidIntArray[i7] = this.liquidIntArray[i8];
				this.liquidIntArray[i8] = i9;
				if(this.liquidIntArray[i7] == 0 && !z10) {
					z10 = this.liquidSpread2(world, xCoord, yCoord, zCoord, xCoord - 1, yCoord, zCoord);
				}

				if(this.liquidIntArray[i7] == 1 && !z10) {
					z10 = this.liquidSpread2(world, xCoord, yCoord, zCoord, xCoord + 1, yCoord, zCoord);
				}

				if(this.liquidIntArray[i7] == 2 && !z10) {
					z10 = this.liquidSpread2(world, xCoord, yCoord, zCoord, xCoord, yCoord, zCoord - 1);
				}

				if(this.liquidIntArray[i7] == 3 && !z10) {
					z10 = this.liquidSpread2(world, xCoord, yCoord, zCoord, xCoord, yCoord, zCoord + 1);
				}
			}

			if(!z10 && z6) {
				if(this.rand.nextInt(3) == 0) {
					if(this.rand.nextInt(3) == 0) {
						z10 = false;

						for(i7 = 0; i7 < 4; ++i7) {
							i8 = this.rand.nextInt(4 - i7) + i7;
							i9 = this.liquidIntArray[i7];
							this.liquidIntArray[i7] = this.liquidIntArray[i8];
							this.liquidIntArray[i8] = i9;
							if(this.liquidIntArray[i7] == 0 && !z10) {
								z10 = this.liquidSpread(world, xCoord, yCoord, zCoord, xCoord - 1, yCoord, zCoord);
							}

							if(this.liquidIntArray[i7] == 1 && !z10) {
								z10 = this.liquidSpread(world, xCoord, yCoord, zCoord, xCoord + 1, yCoord, zCoord);
							}

							if(this.liquidIntArray[i7] == 2 && !z10) {
								z10 = this.liquidSpread(world, xCoord, yCoord, zCoord, xCoord, yCoord, zCoord - 1);
							}

							if(this.liquidIntArray[i7] == 3 && !z10) {
								z10 = this.liquidSpread(world, xCoord, yCoord, zCoord, xCoord, yCoord, zCoord + 1);
							}
						}
					} else if(this.material == Material.lava) {
						world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.stone.blockID);
					} else {
						world.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
					}
				}

				return false;
			} else {
				if(this.material == Material.water) {
					z10 = z10 | extinguishFireLava(world, xCoord - 1, yCoord, zCoord) | extinguishFireLava(world, xCoord + 1, yCoord, zCoord) | extinguishFireLava(world, xCoord, yCoord, zCoord - 1) | extinguishFireLava(world, xCoord, yCoord, zCoord + 1);
				}

				if(this.material == Material.lava) {
					z10 = z10 | flow(world, xCoord - 1, yCoord, zCoord) | flow(world, xCoord + 1, yCoord, zCoord) | flow(world, xCoord, yCoord, zCoord - 1) | flow(world, xCoord, yCoord, zCoord + 1);
				}

				if(!z10) {
					world.setTileNoUpdate(xCoord, yCoord, zCoord, this.stillId1);
				} else {
					world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.movingId1);
				}

				return z10;
			}
		}
	}

	private boolean liquidSpread(World world, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(this.canFlow(world, i5, i6, i7)) {
			world.setBlockWithNotify(i5, i6, i7, this.blockID);
			world.scheduleBlockUpdate(i5, i6, i7, this.blockID);
			return true;
		} else {
			return false;
		}
	}

	private boolean liquidSpread2(World world, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(!this.canFlow(world, i5, i6, i7)) {
			return false;
		} else {
			if((i2 = world.fluidFlowCheck(i2, i3, i4, this.movingId1, this.stillId1)) != -9999) {
				if(i2 < 0) {
					return false;
				}

				i3 = i2 % 1024;
				i4 = (i2 >>= 10) % 1024;
				if(((i2 = (i2 >>= 10) % 1024) > i6 || !this.canFlow(world, i5, i6 - 1, i7)) && i2 <= i6 && i3 != 0 && i3 != world.width - 1 && i4 != 0 && i4 != world.length - 1) {
					return false;
				}

				world.setBlockWithNotify(i3, i2, i4, 0);
			}

			world.setBlockWithNotify(i5, i6, i7, this.blockID);
			world.scheduleBlockUpdate(i5, i6, i7, this.blockID);
			return true;
		}
	}

	public final boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		int i6;
		return xCoord >= 0 && yCoord >= 0 && zCoord >= 0 && xCoord < world.width && zCoord < world.length ? ((i6 = world.getBlockId(xCoord, yCoord, zCoord)) != this.movingId1 && i6 != this.stillId1 ? (i5 == 1 && (world.getBlockId(xCoord - 1, yCoord, zCoord) == 0 || world.getBlockId(xCoord + 1, yCoord, zCoord) == 0 || world.getBlockId(xCoord, yCoord, zCoord - 1) == 0 || world.getBlockId(xCoord, yCoord, zCoord + 1) == 0) ? true : super.shouldSideBeRendered(world, xCoord, yCoord, zCoord, i5)) : false) : false;
	}

	public final boolean isCollidable() {
		return false;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
	}

	public final int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public final int quantityDropped(Random random) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}

	private static boolean extinguishFireLava(World world, int i1, int i2, int i3) {
		if(world.getBlockId(i1, i2, i3) == Block.fire.blockID) {
			world.setBlockWithNotify(i1, i2, i3, 0);
			return true;
		} else if(world.getBlockId(i1, i2, i3) != Block.lavaMoving.blockID && world.getBlockId(i1, i2, i3) != Block.lavaStill.blockID) {
			return false;
		} else {
			world.setBlockWithNotify(i1, i2, i3, Block.stone.blockID);
			return true;
		}
	}

	private static boolean flow(World world0, int i1, int i2, int i3) {
		if(Block.fire.getChanceOfNeighborsEncouragingFire(world0.getBlockId(i1, i2, i3))) {
			Block.fire.fireSpread(world0, i1, i2, i3);
			return true;
		} else {
			return false;
		}
	}
}