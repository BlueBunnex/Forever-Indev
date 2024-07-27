package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFluid extends Block {
	protected int stillId;
	protected int movingId;

	protected BlockFluid(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockFluid[i1] = true;
		this.movingId = i1;
		this.stillId = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
		this.setResistance(2.0F);
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return this.material == Material.lava ? this.blockIndexInTexture : (blockSide == 1 ? this.blockIndexInTexture : (blockSide == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
		world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.movingId);
	}

	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		this.update(world, xCoord, yCoord, zCoord, 0);
	}

	public boolean update(World world, int xCoord, int yCoord, int zCoord, int i5) {
		boolean z7 = false;

		boolean z6;
		do {
			--yCoord;
			if(!this.canFlow(world, xCoord, yCoord, zCoord)) {
				break;
			}

			if(z6 = world.setBlockWithNotify(xCoord, yCoord, zCoord, this.movingId)) {
				z7 = true;
			}
		} while(z6 && this.material != Material.lava);

		++yCoord;
		if(this.material == Material.water || !z7) {
			z7 = z7 | this.flow(world, xCoord - 1, yCoord, zCoord) | this.flow(world, xCoord + 1, yCoord, zCoord) | this.flow(world, xCoord, yCoord, zCoord - 1) | this.flow(world, xCoord, yCoord, zCoord + 1);
		}

		if(this.material == Material.lava) {
			z7 = z7 | extinguishFireLava(world, xCoord - 1, yCoord, zCoord) | extinguishFireLava(world, xCoord + 1, yCoord, zCoord) | extinguishFireLava(world, xCoord, yCoord, zCoord - 1) | extinguishFireLava(world, xCoord, yCoord, zCoord + 1);
		}

		if(!z7) {
			world.setTileNoUpdate(xCoord, yCoord, zCoord, this.stillId);
		} else {
			world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.movingId);
		}

		return z7;
	}

	protected final boolean canFlow(World world1, int i2, int i3, int i4) {
		if(!world1.getBlockMaterial(i2, i3, i4).liquidSolidCheck()) {
			return false;
		} else {
			if(this.material == Material.water) {
				for(int i5 = i2 - 2; i5 <= i2 + 2; ++i5) {
					for(int i6 = i3 - 2; i6 <= i3 + 2; ++i6) {
						for(int i7 = i4 - 2; i7 <= i4 + 2; ++i7) {
							if(world1.getBlockId(i5, i6, i7) == Block.sponge.blockID) {
								return false;
							}
						}
					}
				}
			}

			return true;
		}
	}

	private static boolean extinguishFireLava(World world, int i1, int i2, int i3) {
		if(Block.fire.getChanceOfNeighborsEncouragingFire(world.getBlockId(i1, i2, i3))) {
			Block.fire.fireSpread(world, i1, i2, i3);
			return true;
		} else {
			return false;
		}
	}

	private boolean flow(World world, int i2, int i3, int i4) {
		if(!this.canFlow(world, i2, i3, i4)) {
			return false;
		} else {
			if(world.setBlockWithNotify(i2, i3, i4, this.movingId)) {
				world.scheduleBlockUpdate(i2, i3, i4, this.movingId);
			}

			return false;
		}
	}

	public final float getBlockBrightness(World world, int i2, int i3, int i4) {
		return this.material == Material.lava ? 100.0F : world.getLightBrightness(i2, i3, i4);
	}

	public boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		int i6;
		return xCoord >= 0 && yCoord >= 0 && zCoord >= 0 && xCoord < world.width && zCoord < world.length ? ((i6 = world.getBlockId(xCoord, yCoord, zCoord)) != this.movingId && i6 != this.stillId ? (i5 == 1 && (world.getBlockId(xCoord - 1, yCoord, zCoord) == 0 || world.getBlockId(xCoord + 1, yCoord, zCoord) == 0 || world.getBlockId(xCoord, yCoord, zCoord - 1) == 0 || world.getBlockId(xCoord, yCoord, zCoord + 1) == 0) ? true : super.shouldSideBeRendered(world, xCoord, yCoord, zCoord, i5)) : false) : false;
	}

	public boolean isCollidable() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
		if(blockID != 0) {
			Material blockID1 = Block.blocksList[blockID].material;
			if(this.material == Material.water && blockID1 == Material.lava || blockID1 == Material.water && this.material == Material.lava) {
				world.setBlockWithNotify(xCoord, yCoord, zCoord, Block.stone.blockID);
			}
		}

		world.scheduleBlockUpdate(xCoord, yCoord, zCoord, this.blockID);
	}

	public int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}

	public final void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		if(random.nextInt(128) == -1 && world.getBlockMaterial(xCoord, yCoord + 1, zCoord).getIsSolid()) {
			if(this.material == Material.lava) {
				world.playSoundAtPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "liquid.lava", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() * 0.5F + 0.3F);
			}

			if(this.material == Material.water) {
				world.playSoundAtPlayer((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "liquid.water", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F);
			}
		}

		if(this.material == Material.lava && world.getBlockMaterial(xCoord, yCoord + 1, zCoord) == Material.air && !world.isBlockNormalCube(xCoord, yCoord + 1, zCoord) && random.nextInt(100) == 0) {
			float f6 = (float)xCoord + random.nextFloat();
			float f7 = (float)yCoord + this.maxY;
			float f8 = (float)zCoord + random.nextFloat();
			world.spawnParticle("lava", f6, f7, f8, 0.0F, 0.0F, 0.0F);
		}

		if(this.material == Material.water) {
			int i9;
			if(liquidAirCheck(world, xCoord + 1, yCoord, zCoord)) {
				for(i9 = 0; i9 < 4; ++i9) {
					world.spawnParticle("splash", (float)(xCoord + 1) + 0.125F, (float)yCoord, (float)zCoord + random.nextFloat(), 0.0F, 0.0F, 0.0F);
				}
			}

			if(liquidAirCheck(world, xCoord - 1, yCoord, zCoord)) {
				for(i9 = 0; i9 < 4; ++i9) {
					world.spawnParticle("splash", (float)xCoord - 0.125F, (float)yCoord, (float)zCoord + random.nextFloat(), 0.0F, 0.0F, 0.0F);
				}
			}

			if(liquidAirCheck(world, xCoord, yCoord, zCoord + 1)) {
				for(i9 = 0; i9 < 4; ++i9) {
					world.spawnParticle("splash", (float)xCoord + random.nextFloat(), (float)yCoord, (float)(zCoord + 1) + 0.125F, 0.0F, 0.0F, 0.0F);
				}
			}

			if(liquidAirCheck(world, xCoord, yCoord, zCoord - 1)) {
				for(i9 = 0; i9 < 4; ++i9) {
					world.spawnParticle("splash", (float)xCoord + random.nextFloat(), (float)yCoord, (float)zCoord - 0.125F, 0.0F, 0.0F, 0.0F);
				}
			}
		}

	}

	private static boolean liquidAirCheck(World world, int i1, int i2, int i3) {
		Material material4 = world.getBlockMaterial(i1, i2, i3);
		Material world1 = world.getBlockMaterial(i1, i2 - 1, i3);
		return !material4.getIsSolid() && !material4.getIsLiquid() ? world1.getIsSolid() || world1.getIsLiquid() : false;
	}
}