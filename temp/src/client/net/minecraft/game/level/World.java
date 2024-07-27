package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.BlockContainer;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.level.path.Pathfinder;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import util.MathHelper;

public final class World {
	private static float[] lightBrightnessTable = new float[16];
	public int width;
	public int length;
	public int height;
	public byte[] blocks;
	public byte[] data;
	public String name;
	public String authorName;
	public long createTime;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	public int defaultFluid = Block.waterMoving.blockID;
	List worldAccesses = new ArrayList();
	private List tickList = new LinkedList();
	public Map map = new HashMap();
	private List list = new ArrayList();
	int[] heightMap;
	public Random random = new Random();
	private Random rand = new Random();
	private int randId = this.random.nextInt();
	public EntityMap entityMap;
	public int waterLevel;
	public int groundLevel;
	public int cloudHeight;
	public int skyColor = 10079487;
	public int fogColor = 0xFFFFFF;
	public int cloudColor = 0xFFFFFF;
	private int updateLCG = 0;
	private int playTime = 0;
	public EntityLiving playerEntity;
	public boolean survivalWorld = true;
	public int skyBrightness = 15;
	public int skylightSubtracted = 15;
	public Pathfinder pathFinder = new Pathfinder(this);
	private Light lightUpdates;
	public int worldTime = 0;
	private static short floodFillCounter;
	private short[] floodFillCounters = new short[1048576];
	private int[] coords = new int[1048576];
	private int[] floodedBlocks = new int[1048576];
	public int difficultySetting = 2;

	public final void load() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.worldAccesses = new ArrayList();
			this.heightMap = new int[this.width * this.length];
			Arrays.fill(this.heightMap, this.height);
			this.lightUpdates.updateSkylight(0, 0, this.width, this.length);
			this.random = new Random();
			this.randId = this.random.nextInt();
			this.tickList = new ArrayList();
			if(this.entityMap == null) {
				this.entityMap = new EntityMap(this.width, this.height, this.length);
			}

		}
	}

	public final void generate(int i1, int i2, int i3, byte[] b4, byte[] b5) {
		if(b5 != null && b5.length == 0) {
			b5 = null;
		}

		this.width = i1;
		this.length = i3;
		this.height = i2;
		this.blocks = b4;

		int i6;
		int i7;
		for(i2 = 0; i2 < this.width; ++i2) {
			for(i6 = 0; i6 < this.length; ++i6) {
				for(i7 = 0; i7 < this.height; ++i7) {
					int i8 = 0;
					if(i7 <= 1 && i7 < this.groundLevel - 1 && b4[((i7 + 1) * this.length + i6) * this.width + i2] == 0) {
						i8 = Block.lavaStill.blockID;
					} else if(i7 < this.groundLevel - 1) {
						i8 = Block.bedrock.blockID;
					} else if(i7 < this.groundLevel) {
						if(this.groundLevel > this.waterLevel && this.defaultFluid == Block.waterMoving.blockID) {
							i8 = Block.grass.blockID;
						} else {
							i8 = Block.dirt.blockID;
						}
					} else if(i7 < this.waterLevel) {
						i8 = this.defaultFluid;
					}

					b4[(i7 * this.length + i6) * this.width + i2] = (byte)i8;
					if(i7 == 1 && i2 != 0 && i6 != 0 && i2 != this.width - 1 && i6 != this.length - 1) {
						i7 = this.height - 2;
					}
				}
			}
		}

		this.heightMap = new int[i1 * i3];
		Arrays.fill(this.heightMap, this.height);
		if(b5 == null) {
			this.data = new byte[b4.length];
			this.lightUpdates = new Light(this);
			boolean z10 = true;
			World world11 = this;
			i2 = this.skylightSubtracted;

			for(i3 = 0; i3 < world11.width; ++i3) {
				for(int i12 = 0; i12 < world11.length; ++i12) {
					int i13;
					for(i13 = world11.height - 1; i13 > 0 && Block.lightOpacity[world11.getBlockId(i3, i13, i12)] == 0; --i13) {
					}

					world11.heightMap[i3 + i12 * world11.width] = i13 + 1;

					for(i13 = 0; i13 < world11.height; ++i13) {
						i6 = (i13 * world11.length + i12) * world11.width + i3;
						i7 = world11.heightMap[i3 + i12 * world11.width];
						i7 = i13 >= i7 ? i2 : 0;
						byte b14 = world11.blocks[i6];
						if(i7 < Block.lightValue[b14]) {
							i7 = Block.lightValue[b14];
						}

						world11.data[i6] = (byte)((world11.data[i6] & 240) + i7);
					}
				}
			}

			world11.lightUpdates.updateBlockLight(0, 0, 0, world11.width, world11.height, world11.length);
		} else {
			this.data = b5;
			this.lightUpdates = new Light(this);
		}

		for(i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).loadRenderers();
		}

		this.tickList.clear();
		this.findSpawn();
		this.load();
		System.gc();
	}

	public final void findSpawn() {
		Random random1 = new Random();
		int i2 = 0;

		while(true) {
			label58:
			while(true) {
				int i3;
				int i4;
				int i5;
				do {
					do {
						++i2;
						i3 = random1.nextInt(this.width / 2) + this.width / 4;
						i4 = random1.nextInt(this.length / 2) + this.length / 4;
						i5 = this.getFirstUncoveredBlock(i3, i4) + 1;
						if(i2 == 1000000) {
							this.xSpawn = i3;
							this.ySpawn = this.height + 100;
							this.zSpawn = i4;
							this.rotSpawn = 180.0F;
							return;
						}
					} while(i5 < 4);
				} while(i5 <= this.waterLevel);

				int i6;
				int i7;
				int i8;
				for(i6 = i3 - 3; i6 <= i3 + 3; ++i6) {
					for(i7 = i5 - 1; i7 <= i5 + 2; ++i7) {
						for(i8 = i4 - 3 - 2; i8 <= i4 + 3; ++i8) {
							if(this.getBlockMaterial(i6, i7, i8).isSolid()) {
								continue label58;
							}
						}
					}
				}

				i6 = i5 - 2;

				for(i7 = i3 - 3; i7 <= i3 + 3; ++i7) {
					for(i8 = i4 - 3 - 2; i8 <= i4 + 3; ++i8) {
						if(!Block.opaqueCubeLookup[this.getBlockId(i7, i6, i8)]) {
							continue label58;
						}
					}
				}

				this.xSpawn = i3;
				this.ySpawn = i5;
				this.zSpawn = i4;
				this.rotSpawn = 180.0F;
				return;
			}
		}
	}

	public final void addWorldAccess(IWorldAccess iWorldAccess) {
		for(int i2 = 0; i2 < this.entityMap.entities.size(); ++i2) {
			iWorldAccess.obtainEntitySkin((Entity)this.entityMap.entities.get(i2));
		}

		this.worldAccesses.add(iWorldAccess);
	}

	public final void finalize() {
	}

	public final void removeWorldAccess(IWorldAccess iWorldAccess) {
		this.worldAccesses.remove(iWorldAccess);
	}

	public final ArrayList getCollidingBoundingBoxes(AxisAlignedBB aabb) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = (int)aabb.minX;
		int i4 = (int)aabb.maxX + 1;
		int i5 = (int)aabb.minY;
		int i6 = (int)aabb.maxY + 1;
		int i7 = (int)aabb.minZ;
		int i8 = (int)aabb.maxZ + 1;
		if(aabb.minX < 0.0F) {
			--i3;
		}

		if(aabb.minY < 0.0F) {
			--i5;
		}

		if(aabb.minZ < 0.0F) {
			--i7;
		}

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i9 = i5; i9 < i6; ++i9) {
				for(int i10 = i7; i10 < i8; ++i10) {
					Block block11;
					AxisAlignedBB axisAlignedBB12;
					if((block11 = Block.blocksList[this.getBlockId(i3, i9, i10)]) != null) {
						if((axisAlignedBB12 = block11.getCollisionBoundingBoxFromPool(i3, i9, i10)) != null && aabb.intersectsWith(axisAlignedBB12)) {
							arrayList2.add(axisAlignedBB12);
						}
					} else if(this.groundLevel < 0 && (i9 < this.groundLevel || i9 < this.waterLevel) && (axisAlignedBB12 = Block.bedrock.getCollisionBoundingBoxFromPool(i3, i9, i10)) != null && aabb.intersectsWith(axisAlignedBB12)) {
						arrayList2.add(axisAlignedBB12);
					}
				}
			}
		}

		return arrayList2;
	}

	public final void swap(int i1, int i2, int i3, int i4, int i5, int i6) {
		int i7 = this.getBlockId(i1, i2, i3);
		int i8 = this.getBlockId(i4, i5, i6);
		this.setBlock(i1, i2, i3, i8);
		this.setBlock(i4, i5, i6, i7);
		this.notifyBlocksOfNeighborChange(i1, i2, i3, i8);
		this.notifyBlocksOfNeighborChange(i4, i5, i6, i7);
	}

	public final boolean setBlock(int i1, int i2, int i3, int i4) {
		if(i1 > 0 && i2 > 0 && i3 > 0 && i1 < this.width - 1 && i2 < this.height - 1 && i3 < this.length - 1) {
			if(i4 == this.blocks[(i2 * this.length + i3) * this.width + i1]) {
				return false;
			} else {
				if(i4 == 0 && (i1 == 0 || i3 == 0 || i1 == this.width - 1 || i3 == this.length - 1) && i2 >= this.groundLevel && i2 < this.waterLevel) {
					i4 = Block.waterMoving.blockID;
				}

				byte b5 = this.blocks[(i2 * this.length + i3) * this.width + i1];
				this.blocks[(i2 * this.length + i3) * this.width + i1] = (byte)i4;
				this.setBlockMetadata(i1, i2, i3, 0);
				if(b5 != 0) {
					Block.blocksList[b5].onBlockRemoval(this, i1, i2, i3);
				}

				if(i4 != 0) {
					Block.blocksList[i4].onBlockAdded(this, i1, i2, i3);
				}

				if(Block.lightOpacity[b5] != Block.lightOpacity[i4] || Block.lightValue[b5] != 0 || Block.lightValue[i4] != 0) {
					this.lightUpdates.updateSkylight(i1, i3, 1, 1);
					this.lightUpdates.updateBlockLight(i1, i2, i3, i1 + 1, i2 + 1, i3 + 1);
				}

				for(i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
					((IWorldAccess)this.worldAccesses.get(i4)).markBlockAndNeighborsNeedsUpdate(i1, i2, i3);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean setBlockWithNotify(int i1, int i2, int i3, int i4) {
		if(this.setBlock(i1, i2, i3, i4)) {
			this.notifyBlocksOfNeighborChange(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
	}

	public final void notifyBlocksOfNeighborChange(int i1, int i2, int i3, int i4) {
		this.notifyBlockOfNeighborChange(i1 - 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1 + 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 - 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 + 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 - 1, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 + 1, i4);
	}

	public final boolean setTileNoUpdate(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			if(i4 == this.blocks[(i2 * this.length + i3) * this.width + i1]) {
				return false;
			} else {
				this.blocks[(i2 * this.length + i3) * this.width + i1] = (byte)i4;
				this.lightUpdates.updateBlockLight(i1, i2, i3, i1 + 1, i2 + 1, i3 + 1);
				return true;
			}
		} else {
			return false;
		}
	}

	private void notifyBlockOfNeighborChange(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			Block block5;
			if((block5 = Block.blocksList[this.blocks[(i2 * this.length + i3) * this.width + i1]]) != null) {
				block5.onNeighborBlockChange(this, i1, i2, i3, i4);
			}

		}
	}

	public final int getBlockId(int i1, int i2, int i3) {
		if(i1 < 0) {
			i1 = 0;
		} else if(i1 >= this.width) {
			i1 = this.width - 1;
		}

		if(i2 < 0) {
			i2 = 0;
		} else if(i2 >= this.height) {
			i2 = this.height - 1;
		}

		if(i3 < 0) {
			i3 = 0;
		} else if(i3 >= this.length) {
			i3 = this.length - 1;
		}

		return this.blocks[(i2 * this.length + i3) * this.width + i1] & 255;
	}

	public final boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4;
		return (block4 = Block.blocksList[this.getBlockId(i1, i2, i3)]) == null ? false : block4.isOpaqueCube();
	}

	public final void updateEntities() {
		this.entityMap.updateEntities();

		for(int i1 = 0; i1 < this.list.size(); ++i1) {
			((TileEntity)this.list.get(i1)).updateEntity();
		}

	}

	public final void updateLighting() {
		this.lightUpdates.updateLight();
	}

	public final float getStarBrightness(float f1) {
		f1 = this.getCelestialAngle(f1);
		if((f1 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.75F)) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		return f1 * f1 * 0.5F;
	}

	public final Vec3D getSkyColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.skyColor >> 16 & 255) / 255.0F;
		float f3 = (float)(this.skyColor >> 8 & 255) / 255.0F;
		float f4 = (float)(this.skyColor & 255) / 255.0F;
		f2 *= f1;
		f3 *= f1;
		f4 *= f1;
		return new Vec3D(f2, f3, f4);
	}

	public final float getCelestialAngle(float f1) {
		return this.skyBrightness > 15 ? 0.0F : ((float)this.worldTime + f1) / 24000.0F - 0.15F;
	}

	public final Vec3D getFogColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.fogColor >> 16 & 255) / 255.0F;
		float f3 = (float)(this.fogColor >> 8 & 255) / 255.0F;
		float f4 = (float)(this.fogColor & 255) / 255.0F;
		f2 *= f1 * 0.94F + 0.06F;
		f3 *= f1 * 0.94F + 0.06F;
		f4 *= f1 * 0.91F + 0.09F;
		return new Vec3D(f2, f3, f4);
	}

	public final Vec3D getCloudColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.cloudColor >> 16 & 255) / 255.0F;
		float f3 = (float)(this.cloudColor >> 8 & 255) / 255.0F;
		float f4 = (float)(this.cloudColor & 255) / 255.0F;
		f2 *= f1 * 0.9F + 0.1F;
		f3 *= f1 * 0.9F + 0.1F;
		f4 *= f1 * 0.85F + 0.15F;
		return new Vec3D(f2, f3, f4);
	}

	public final int getSkyBrightness() {
		float f1;
		if((f1 = MathHelper.cos(this.getCelestialAngle(1.0F) * (float)Math.PI * 2.0F) * 1.5F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i2;
		if((i2 = (int)(f1 * ((float)(15 * this.skyBrightness) / 15.0F - 4.0F) + 4.0F)) > 15) {
			i2 = 15;
		}

		if(i2 < 4) {
			i2 = 4;
		}

		return i2;
	}

	public final void tick() {
		++this.worldTime;
		if(this.worldTime == 24000) {
			this.worldTime = 0;
		}

		int i1 = this.getSkyBrightness();
		if(this.skylightSubtracted > i1) {
			this.updateChunkLight(this.skylightSubtracted - 1);
		}

		if(this.skylightSubtracted < i1) {
			this.updateChunkLight(this.skylightSubtracted + 1);
		}

		++this.playTime;
		i1 = 1;

		int i2;
		for(i2 = 1; 1 << i1 < this.width; ++i1) {
		}

		while(1 << i2 < this.length) {
			++i2;
		}

		int i3 = this.length - 1;
		int i4 = this.width - 1;
		int i5 = this.height - 1;
		int i6;
		if((i6 = this.tickList.size()) > 200) {
			i6 = 200;
		}

		int i7;
		int i10;
		for(i7 = 0; i7 < i6; ++i7) {
			NextTickListEntry nextTickListEntry8;
			if((nextTickListEntry8 = (NextTickListEntry)this.tickList.remove(0)).scheduledTime > 0) {
				--nextTickListEntry8.scheduledTime;
				this.tickList.add(nextTickListEntry8);
			} else {
				int i12 = nextTickListEntry8.zCoord;
				int i11 = nextTickListEntry8.yCoord;
				i10 = nextTickListEntry8.xCoord;
				byte b9;
				if(i10 >= 0 && i11 >= 0 && i12 >= 0 && i10 < this.width && i11 < this.height && i12 < this.length && (b9 = this.blocks[(nextTickListEntry8.yCoord * this.length + nextTickListEntry8.zCoord) * this.width + nextTickListEntry8.xCoord]) == nextTickListEntry8.blockID && b9 > 0) {
					Block.blocksList[b9].updateTick(this, nextTickListEntry8.xCoord, nextTickListEntry8.yCoord, nextTickListEntry8.zCoord, this.random);
				}
			}
		}

		this.updateLCG += this.width * this.length * this.height;
		i6 = this.updateLCG / 200;
		this.updateLCG -= i6 * 200;

		for(i7 = 0; i7 < i6; ++i7) {
			this.randId = this.randId * 3 + 1013904223;
			int i13;
			int i14 = (i13 = this.randId >> 2) & i4;
			i10 = i13 >> i1 & i3;
			i13 = i13 >> i1 + i2 & i5;
			byte b15 = this.blocks[(i13 * this.length + i10) * this.width + i14];
			if(Block.tickOnLoad[b15]) {
				Block.blocksList[b15].updateTick(this, i14, i13, i10, this.random);
			}
		}

	}

	public final int entitiesInLevelList(Class class1) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.entityMap.entities.size(); ++i3) {
			Entity entity4 = (Entity)this.entityMap.entities.get(i3);
			if(class1.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public final int getGroundLevel() {
		return this.groundLevel;
	}

	public final int getWaterLevel() {
		return this.waterLevel;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB aabb) {
		int i2 = (int)aabb.minX;
		int i3 = (int)aabb.maxX + 1;
		int i4 = (int)aabb.minY;
		int i5 = (int)aabb.maxY + 1;
		int i6 = (int)aabb.minZ;
		int i7 = (int)aabb.maxZ + 1;
		if(aabb.minX < 0.0F) {
			--i2;
		}

		if(aabb.minY < 0.0F) {
			--i4;
		}

		if(aabb.minZ < 0.0F) {
			--i6;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i4 < 0) {
			i4 = 0;
		}

		if(i6 < 0) {
			i6 = 0;
		}

		if(i3 > this.width) {
			i3 = this.width;
		}

		if(i5 > this.height) {
			i5 = this.height;
		}

		if(i7 > this.length) {
			i7 = this.length;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Block block9;
					if((block9 = Block.blocksList[this.getBlockId(i10, i2, i8)]) != null && block9.material.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isBoundingBoxBurning(AxisAlignedBB aabb) {
		int i2 = (int)aabb.minX;
		int i3 = (int)aabb.maxX + 1;
		int i4 = (int)aabb.minY;
		int i5 = (int)aabb.maxY + 1;
		int i6 = (int)aabb.minZ;
		int i10 = (int)aabb.maxZ + 1;

		for(i2 = i2; i2 < i3; ++i2) {
			for(int i7 = i4; i7 < i5; ++i7) {
				for(int i8 = i6; i8 < i10; ++i8) {
					int i9;
					if((i9 = this.getBlockId(i2, i7, i8)) == Block.fire.blockID || i9 == Block.lavaMoving.blockID || i9 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean handleMaterialAcceleration(AxisAlignedBB aabb, Material material) {
		int i3 = (int)aabb.minX;
		int i4 = (int)aabb.maxX + 1;
		int i5 = (int)aabb.minY;
		int i6 = (int)aabb.maxY + 1;
		int i7 = (int)aabb.minZ;
		int i11 = (int)aabb.maxZ + 1;

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i8 = i5; i8 < i6; ++i8) {
				for(int i9 = i7; i9 < i11; ++i9) {
					Block block10;
					if((block10 = Block.blocksList[this.getBlockId(i3, i8, i9)]) != null && block10.material == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void scheduleBlockUpdate(int i1, int i2, int i3, int i4) {
		NextTickListEntry nextTickListEntry5 = new NextTickListEntry(i1, i2, i3, i4);
		if(i4 > 0) {
			i3 = Block.blocksList[i4].tickRate();
			nextTickListEntry5.scheduledTime = i3;
		}

		this.tickList.add(nextTickListEntry5);
	}

	public final boolean checkIfAABBIsClear1(AxisAlignedBB aabb) {
		return this.entityMap.getEntitiesWithinAABB((Entity)null, aabb).size() == 0;
	}

	public final boolean checkIfAABBIsClear(AxisAlignedBB aabb) {
		List list4 = this.entityMap.getEntitiesWithinAABB((Entity)null, aabb);

		for(int i2 = 0; i2 < list4.size(); ++i2) {
			if(((Entity)list4.get(i2)).preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB aabb) {
		return this.entityMap.getEntitiesWithinAABB(entity, aabb);
	}

	public final boolean isSolid(float f1, float f2, float f3, float f4) {
		return this.isSolid(f1 - 0.1F, f2 - 0.1F, f3 - 0.1F) ? true : (this.isSolid(f1 - 0.1F, f2 - 0.1F, f3 + 0.1F) ? true : (this.isSolid(f1 - 0.1F, f2 + 0.1F, f3 - 0.1F) ? true : (this.isSolid(f1 - 0.1F, f2 + 0.1F, f3 + 0.1F) ? true : (this.isSolid(f1 + 0.1F, f2 - 0.1F, f3 - 0.1F) ? true : (this.isSolid(f1 + 0.1F, f2 - 0.1F, f3 + 0.1F) ? true : (this.isSolid(f1 + 0.1F, f2 + 0.1F, f3 - 0.1F) ? true : this.isSolid(f1 + 0.1F, f2 + 0.1F, f3 + 0.1F)))))));
	}

	private boolean isSolid(float f1, float f2, float f3) {
		int i4;
		return (i4 = this.getBlockId((int)f1, (int)f2, (int)f3)) > 0 && Block.blocksList[i4].isOpaqueCube();
	}

	private int getFirstUncoveredBlock(int i1, int i2) {
		int i3;
		for(i3 = this.height; (this.getBlockId(i1, i3 - 1, i2) == 0 || Block.blocksList[this.getBlockId(i1, i3 - 1, i2)].material == Material.air) && i3 > 0; --i3) {
		}

		return i3;
	}

	public final void setSpawnLocation(int i1, int i2, int i3, float f4) {
		this.xSpawn = i1;
		this.ySpawn = i2;
		this.zSpawn = i3;
		this.rotSpawn = f4;
	}

	public final float getLightBrightness(int i1, int i2, int i3) {
		return lightBrightnessTable[this.getBlockLightValue(i1, i2, i3)];
	}

	public final byte getBlockLightValue(int i1, int i2, int i3) {
		if(i1 < 0) {
			i1 = 0;
		} else if(i1 >= this.width) {
			i1 = this.width - 1;
		}

		if(i2 < 0) {
			i2 = 0;
		} else if(i2 >= this.height) {
			i2 = this.height - 1;
		}

		if(i3 < 0) {
			i3 = 0;
		} else if(i3 >= this.length) {
			i3 = this.length - 1;
		}

		return this.blocks[(i2 * this.length + i3) * this.width + i1] == Block.stairSingle.blockID ? (i2 < this.height - 1 ? (byte)(this.data[((i2 + 1) * this.length + i3) * this.width + i1] & 15) : 15) : (byte)(this.data[(i2 * this.length + i3) * this.width + i1] & 15);
	}

	public final byte getBlockMetadata(int i1, int i2, int i3) {
		if(i1 < 0) {
			i1 = 0;
		} else if(i1 >= this.width) {
			i1 = this.width - 1;
		}

		if(i2 < 0) {
			i2 = 0;
		} else if(i2 >= this.height) {
			i2 = this.height - 1;
		}

		if(i3 < 0) {
			i3 = 0;
		} else if(i3 >= this.length) {
			i3 = this.length - 1;
		}

		return (byte)(this.data[(i2 * this.length + i3) * this.width + i1] >>> 4 & 15);
	}

	public final void setBlockMetadata(int i1, int i2, int i3, int i4) {
		if(i1 < 0) {
			i1 = 0;
		} else if(i1 >= this.width) {
			i1 = this.width - 1;
		}

		if(i2 < 0) {
			i2 = 0;
		} else if(i2 >= this.height) {
			i2 = this.height - 1;
		}

		if(i3 < 0) {
			i3 = 0;
		} else if(i3 >= this.length) {
			i3 = this.length - 1;
		}

		this.data[(i2 * this.length + i3) * this.width + i1] = (byte)((this.data[(i2 * this.length + i3) * this.width + i1] & 15) + (i4 << 4));

		for(i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
			((IWorldAccess)this.worldAccesses.get(i4)).markBlockAndNeighborsNeedsUpdate(i1, i2, i3);
		}

	}

	public final Material getBlockMaterial(int i1, int i2, int i3) {
		return (i1 = this.getBlockId(i1, i2, i3)) == 0 ? Material.air : Block.blocksList[i1].material;
	}

	public final boolean isWater(int i1, int i2, int i3) {
		return (i1 = this.getBlockId(i1, i2, i3)) > 0 && Block.blocksList[i1].material == Material.water;
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D vec3D, Vec3D vec3D2) {
		if(!Float.isNaN(vec3D.xCoord) && !Float.isNaN(vec3D.yCoord) && !Float.isNaN(vec3D.zCoord)) {
			if(!Float.isNaN(vec3D2.xCoord) && !Float.isNaN(vec3D2.yCoord) && !Float.isNaN(vec3D2.zCoord)) {
				int i3 = MathHelper.floor_float(vec3D2.xCoord);
				int i4 = MathHelper.floor_float(vec3D2.yCoord);
				int i5 = MathHelper.floor_float(vec3D2.zCoord);
				int i6 = MathHelper.floor_float(vec3D.xCoord);
				int i7 = MathHelper.floor_float(vec3D.yCoord);
				int i8 = MathHelper.floor_float(vec3D.zCoord);
				int i9 = 20;

				int i21;
				MovingObjectPosition movingObjectPosition22;
				Block block23;
				do {
					if(i9-- < 0) {
						return null;
					}

					if(Float.isNaN(vec3D.xCoord) || Float.isNaN(vec3D.yCoord) || Float.isNaN(vec3D.zCoord)) {
						return null;
					}

					if(i6 == i3 && i7 == i4 && i8 == i5) {
						return null;
					}

					float f10 = 999.0F;
					float f11 = 999.0F;
					float f12 = 999.0F;
					if(i3 > i6) {
						f10 = (float)i6 + 1.0F;
					}

					if(i3 < i6) {
						f10 = (float)i6;
					}

					if(i4 > i7) {
						f11 = (float)i7 + 1.0F;
					}

					if(i4 < i7) {
						f11 = (float)i7;
					}

					if(i5 > i8) {
						f12 = (float)i8 + 1.0F;
					}

					if(i5 < i8) {
						f12 = (float)i8;
					}

					float f13 = 999.0F;
					float f14 = 999.0F;
					float f15 = 999.0F;
					float f16 = vec3D2.xCoord - vec3D.xCoord;
					float f17 = vec3D2.yCoord - vec3D.yCoord;
					float f18 = vec3D2.zCoord - vec3D.zCoord;
					if(f10 != 999.0F) {
						f13 = (f10 - vec3D.xCoord) / f16;
					}

					if(f11 != 999.0F) {
						f14 = (f11 - vec3D.yCoord) / f17;
					}

					if(f12 != 999.0F) {
						f15 = (f12 - vec3D.zCoord) / f18;
					}

					byte b19;
					if(f13 < f14 && f13 < f15) {
						if(i3 > i6) {
							b19 = 4;
						} else {
							b19 = 5;
						}

						vec3D.xCoord = f10;
						vec3D.yCoord += f17 * f13;
						vec3D.zCoord += f18 * f13;
					} else if(f14 < f15) {
						if(i4 > i7) {
							b19 = 0;
						} else {
							b19 = 1;
						}

						vec3D.xCoord += f16 * f14;
						vec3D.yCoord = f11;
						vec3D.zCoord += f18 * f14;
					} else {
						if(i5 > i8) {
							b19 = 2;
						} else {
							b19 = 3;
						}

						vec3D.xCoord += f16 * f15;
						vec3D.yCoord += f17 * f15;
						vec3D.zCoord = f12;
					}

					Vec3D vec3D20;
					i6 = (int)((vec3D20 = new Vec3D(vec3D.xCoord, vec3D.yCoord, vec3D.zCoord)).xCoord = (float)MathHelper.floor_float(vec3D.xCoord));
					if(b19 == 5) {
						--i6;
						++vec3D20.xCoord;
					}

					i7 = (int)(vec3D20.yCoord = (float)MathHelper.floor_float(vec3D.yCoord));
					if(b19 == 1) {
						--i7;
						++vec3D20.yCoord;
					}

					i8 = (int)(vec3D20.zCoord = (float)MathHelper.floor_float(vec3D.zCoord));
					if(b19 == 3) {
						--i8;
						++vec3D20.zCoord;
					}

					i21 = this.getBlockId(i6, i7, i8);
					block23 = Block.blocksList[i21];
				} while(i21 <= 0 || !block23.isCollidable() || (movingObjectPosition22 = block23.collisionRayTrace(this, i6, i7, i8, vec3D, vec3D2)) == null);

				return movingObjectPosition22;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final boolean growTrees(int i1, int i2, int i3) {
		int i4 = this.random.nextInt(3) + 4;
		boolean z5 = true;
		if(i2 > 0 && i2 + i4 + 1 <= this.height) {
			int i6;
			int i8;
			int i9;
			for(i6 = i2; i6 <= i2 + 1 + i4; ++i6) {
				byte b7 = 1;
				if(i6 == i2) {
					b7 = 0;
				}

				if(i6 >= i2 + 1 + i4 - 2) {
					b7 = 2;
				}

				for(i8 = i1 - b7; i8 <= i1 + b7 && z5; ++i8) {
					for(i9 = i3 - b7; i9 <= i3 + b7 && z5; ++i9) {
						if(i8 >= 0 && i6 >= 0 && i9 >= 0 && i8 < this.width && i6 < this.height && i9 < this.length) {
							if((this.blocks[(i6 * this.length + i9) * this.width + i8] & 255) != 0) {
								z5 = false;
							}
						} else {
							z5 = false;
						}
					}
				}
			}

			if(!z5) {
				return false;
			} else if(((i6 = this.blocks[((i2 - 1) * this.length + i3) * this.width + i1] & 255) == Block.grass.blockID || i6 == Block.dirt.blockID) && i2 < this.height - i4 - 1) {
				this.setBlockWithNotify(i1, i2 - 1, i3, Block.dirt.blockID);

				int i13;
				for(i13 = i2 - 3 + i4; i13 <= i2 + i4; ++i13) {
					i8 = i13 - (i2 + i4);
					i9 = 1 - i8 / 2;

					for(int i10 = i1 - i9; i10 <= i1 + i9; ++i10) {
						int i12 = i10 - i1;

						for(i6 = i3 - i9; i6 <= i3 + i9; ++i6) {
							int i11 = i6 - i3;
							if((Math.abs(i12) != i9 || Math.abs(i11) != i9 || this.random.nextInt(2) != 0 && i8 != 0) && !Block.opaqueCubeLookup[this.getBlockId(i10, i13, i6)]) {
								this.setBlockWithNotify(i10, i13, i6, Block.leaves.blockID);
							}
						}
					}
				}

				for(i13 = 0; i13 < i4; ++i13) {
					if(!Block.opaqueCubeLookup[this.getBlockId(i1, i2 + i13, i3)]) {
						this.setBlockWithNotify(i1, i2 + i13, i3, Block.wood.blockID);
					}
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final void spawnEntityInWorld(Entity entity) {
		this.entityMap.insert(entity);
		entity.setWorld(this);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).obtainEntitySkin(entity);
		}

	}

	public final void releaseEntitySkin(Entity entity) {
		this.entityMap.remove(entity);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			((IWorldAccess)this.worldAccesses.get(i2)).releaseEntitySkin(entity);
		}

	}

	public final void createExplosion(Entity entity, float f2, float f3, float f4, float f5) {
		this.playSoundAtPlayer(f2, f3, f4, "random.explode", 4.0F, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
		TreeSet treeSet6 = new TreeSet();
		float f7 = f5;

		int i8;
		int i9;
		int i10;
		float f11;
		float f18;
		int i19;
		int i20;
		int i21;
		int i22;
		for(i8 = 0; i8 < 16; ++i8) {
			for(i9 = 0; i9 < 16; ++i9) {
				for(i10 = 0; i10 < 16; ++i10) {
					if(i8 == 0 || i8 == 15 || i9 == 0 || i9 == 15 || i10 == 0 || i10 == 15) {
						f11 = (float)i8 / 15.0F * 2.0F - 1.0F;
						float f12 = (float)i9 / 15.0F * 2.0F - 1.0F;
						float f13 = (float)i10 / 15.0F * 2.0F - 1.0F;
						float f14 = (float)Math.sqrt((double)(f11 * f11 + f12 * f12 + f13 * f13));
						f11 /= f14;
						f12 /= f14;
						f13 /= f14;
						float f15 = f5 * (0.7F + this.random.nextFloat() * 0.6F);
						float f16 = f2;
						float f17 = f3;

						for(f18 = f4; f15 > 0.0F; f15 -= 0.22500001F) {
							i19 = (int)f16;
							i20 = (int)f17;
							i21 = (int)f18;
							if((i22 = this.getBlockId(i19, i20, i21)) > 0) {
								f15 -= (Block.blocksList[i22].getExplosionResistance() + 0.3F) * 0.3F;
							}

							if(f15 > 0.0F) {
								int i23 = i19 + (i20 << 10) + (i21 << 10 << 10);
								treeSet6.add(i23);
							}

							f16 += f11 * 0.3F;
							f17 += f12 * 0.3F;
							f18 += f13 * 0.3F;
						}
					}
				}
			}
		}

		f5 *= 2.0F;
		i8 = (int)(f2 - f5 - 1.0F);
		i9 = (int)(f2 + f5 + 1.0F);
		i10 = (int)(f3 - f5 - 1.0F);
		int i30 = (int)(f3 + f5 + 1.0F);
		int i31 = (int)(f4 - f5 - 1.0F);
		int i32 = (int)(f4 + f5 + 1.0F);
		List list33 = this.entityMap.getEntities(entity, (float)i8, (float)i10, (float)i31, (float)i9, (float)i30, (float)i32);
		Vec3D vec3D34 = new Vec3D(f2, f3, f4);

		float f28;
		float f29;
		float f44;
		for(int i35 = 0; i35 < list33.size(); ++i35) {
			Entity entity26;
			Entity entity37;
			f28 = (entity26 = entity37 = (Entity)list33.get(i35)).posX - f2;
			f29 = entity26.posY - f3;
			float f27 = entity26.posZ - f4;
			if((f18 = MathHelper.sqrt_float(f28 * f28 + f29 * f29 + f27 * f27) / f5) <= 1.0F) {
				f27 = entity37.posX - f2;
				float f40 = entity37.posY - f3;
				float f41 = entity37.posZ - f4;
				float f42 = MathHelper.sqrt_float(f27 * f27 + f40 * f40 + f41 * f41);
				f27 /= f42;
				f40 /= f42;
				f41 /= f42;
				float f43 = this.getBlockDensity(vec3D34, entity37.boundingBox);
				f44 = (1.0F - f18) * f43;
				entity37.attackEntityFrom(entity, (int)((f44 * f44 + f44) / 2.0F * 8.0F * f5 + 1.0F));
				entity37.motionX += f27 * f44;
				entity37.motionY += f40 * f44;
				entity37.motionZ += f41 * f44;
			}
		}

		f5 = f7;
		ArrayList arrayList36;
		(arrayList36 = new ArrayList()).addAll(treeSet6);

		for(int i38 = arrayList36.size() - 1; i38 >= 0; --i38) {
			int i39;
			i8 = (i39 = ((Integer)arrayList36.get(i38)).intValue()) & 1023;
			i19 = i39 >> 10 & 1023;
			i20 = i39 >> 20 & 1023;
			if(i8 >= 0 && i19 >= 0 && i20 >= 0 && i8 < this.width && i19 < this.height && i20 < this.length) {
				i21 = this.getBlockId(i8, i19, i20);

				for(i22 = 0; i22 <= 0; ++i22) {
					f44 = (float)i8 + this.random.nextFloat();
					f28 = (float)i19 + this.random.nextFloat();
					float f24 = (float)i20 + this.random.nextFloat();
					float f25 = f44 - f2;
					f7 = f28 - f3;
					f29 = f24 - f4;
					f11 = MathHelper.sqrt_float(f25 * f25 + f7 * f7 + f29 * f29);
					f25 /= f11;
					f7 /= f11;
					f29 /= f11;
					f11 = (f11 = 0.5F / (f11 / f5 + 0.1F)) * (this.random.nextFloat() * this.random.nextFloat() + 0.3F);
					f25 *= f11;
					f7 *= f11;
					f29 *= f11;
					this.spawnParticle("explode", (f44 + f2) / 2.0F, (f28 + f3) / 2.0F, (f24 + f4) / 2.0F, f25, f7, f29);
					this.spawnParticle("smoke", f44, f28, f24, f25, f7, f29);
				}

				if(i21 > 0) {
					Block.blocksList[i21].dropBlockAsItemWithChance(this, i8, i19, i20, this.getBlockMetadata(i8, i19, i20), 0.3F);
					this.setBlockWithNotify(i8, i19, i20, 0);
					Block.blocksList[i21].onBlockDestroyedByExplosion(this, i8, i19, i20);
				}
			}
		}

	}

	private float getBlockDensity(Vec3D vec3D, AxisAlignedBB aabb) {
		float f3 = 1.0F / ((aabb.maxX - aabb.minX) * 2.0F + 1.0F);
		float f4 = 1.0F / ((aabb.maxY - aabb.minY) * 2.0F + 1.0F);
		float f5 = 1.0F / ((aabb.maxZ - aabb.minZ) * 2.0F + 1.0F);
		int i6 = 0;
		int i7 = 0;

		for(float f8 = 0.0F; f8 <= 1.0F; f8 += f3) {
			for(float f9 = 0.0F; f9 <= 1.0F; f9 += f4) {
				for(float f10 = 0.0F; f10 <= 1.0F; f10 += f5) {
					float f11 = aabb.minX + (aabb.maxX - aabb.minX) * f8;
					float f12 = aabb.minY + (aabb.maxY - aabb.minY) * f9;
					float f13 = aabb.minZ + (aabb.maxZ - aabb.minZ) * f10;
					if(this.rayTraceBlocks(new Vec3D(f11, f12, f13), vec3D) == null) {
						++i6;
					}

					++i7;
				}
			}
		}

		return (float)i6 / (float)i7;
	}

	public final Entity findSubclassOf(Class class1) {
		for(int i2 = 0; i2 < this.entityMap.entities.size(); ++i2) {
			Entity entity3 = (Entity)this.entityMap.entities.get(i2);
			if(class1.isAssignableFrom(entity3.getClass())) {
				return entity3;
			}
		}

		return null;
	}

	public final int fluidFlowCheck(int i1, int i2, int i3, int i4, int i5) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			int i6 = i1;
			int i7 = i3;
			int i8 = ((i2 << 10) + i3 << 10) + i1;
			byte b9 = 0;
			int i20 = b9 + 1;
			this.coords[0] = i1 + (i3 << 10);
			int i11 = -9999;
			if(i4 == Block.waterStill.blockID || i4 == Block.waterMoving.blockID) {
				i11 = Block.waterSource.blockID;
			}

			if(i4 == Block.lavaStill.blockID || i4 == Block.lavaMoving.blockID) {
				i11 = Block.lavaSource.blockID;
			}

			int i10;
			boolean z12;
			label170:
			do {
				z12 = false;
				int i13 = -1;
				i10 = 0;
				if(++floodFillCounter == 30000) {
					Arrays.fill(this.floodFillCounters, (short)0);
					floodFillCounter = 1;
				}

				while(true) {
					int i14;
					do {
						if(i20 <= 0) {
							++i2;
							int[] i21 = this.floodedBlocks;
							this.floodedBlocks = this.coords;
							this.coords = i21;
							i20 = i10;
							continue label170;
						}

						--i20;
						i14 = this.coords[i20];
					} while(this.floodFillCounters[i14] == floodFillCounter);

					i1 = i14 % 1024;

					int i15;
					for(i15 = (i15 = (i3 = i14 / 1024) - i7) * i15; i1 > 0 && this.floodFillCounters[i14 - 1] != floodFillCounter && (this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == i4 || this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == i5); --i14) {
						--i1;
					}

					if(i1 > 0 && this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == i11) {
						z12 = true;
					}

					boolean z16 = false;
					boolean z17 = false;

					for(boolean z18 = false; i1 < this.width && this.floodFillCounters[i14] != floodFillCounter && (this.blocks[(i2 * this.length + i3) * this.width + i1] == i4 || this.blocks[(i2 * this.length + i3) * this.width + i1] == i5); ++i1) {
						byte b19;
						boolean z22;
						if(i3 > 0) {
							if((b19 = this.blocks[(i2 * this.length + i3 - 1) * this.width + i1]) == i11) {
								z12 = true;
							}

							if((z22 = this.floodFillCounters[i14 - 1024] != floodFillCounter && (b19 == i4 || b19 == i5)) && !z16) {
								this.coords[i20++] = i14 - 1024;
							}

							z16 = z22;
						}

						if(i3 < this.length - 1) {
							if((b19 = this.blocks[(i2 * this.length + i3 + 1) * this.width + i1]) == i11) {
								z12 = true;
							}

							if((z22 = this.floodFillCounters[i14 + 1024] != floodFillCounter && (b19 == i4 || b19 == i5)) && !z17) {
								this.coords[i20++] = i14 + 1024;
							}

							z17 = z22;
						}

						if(i2 < this.height - 1) {
							if((z22 = (b19 = this.blocks[((i2 + 1) * this.length + i3) * this.width + i1]) == i4 || b19 == i5) && !z18) {
								this.floodedBlocks[i10++] = i14;
							}

							z18 = z22;
						}

						int i23;
						if((i23 = (i23 = i1 - i6) * i23 + i15) > i13) {
							i13 = i23;
							i8 = ((i2 << 10) + i3 << 10) + i1;
						}

						this.floodFillCounters[i14++] = floodFillCounter;
					}

					if(i1 < this.width && this.blocks[(i2 * this.length + i3) * this.width + i1] == i11) {
						z12 = true;
					}
				}
			} while(i10 > 0);

			if(z12) {
				return -9999;
			} else {
				return i8;
			}
		} else {
			return -1;
		}
	}

	public final int floodFill(int i1, int i2, int i3, int i4, int i5) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			if(++floodFillCounter == 30000) {
				Arrays.fill(this.floodFillCounters, (short)0);
				floodFillCounter = 1;
			}

			byte b6 = 0;
			int i11 = b6 + 1;
			this.coords[0] = i1 + (i3 << 10);

			do {
				int i7;
				do {
					if(i11 <= 0) {
						return 1;
					}

					--i11;
					i7 = this.coords[i11];
				} while(this.floodFillCounters[i7] == floodFillCounter);

				i1 = i7 % 1024;
				i3 = i7 / 1024;
				if(i1 == 0 || i1 == this.width - 1 || i2 == 0 || i2 == this.height - 1 || i3 == 0 || i3 == this.length - 1) {
					return 2;
				}

				while(i1 > 0 && this.floodFillCounters[i7 - 1] != floodFillCounter && (this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == i4 || this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == i5)) {
					--i1;
					--i7;
				}

				if(i1 > 0 && this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == 0) {
					return 0;
				}

				boolean z8 = false;

				for(boolean z9 = false; i1 < this.width && this.floodFillCounters[i7] != floodFillCounter && (this.blocks[(i2 * this.length + i3) * this.width + i1] == i4 || this.blocks[(i2 * this.length + i3) * this.width + i1] == i5); ++i1) {
					if(i1 == 0 || i1 == this.width - 1) {
						return 2;
					}

					byte b10;
					boolean z12;
					if(i3 > 0) {
						if((b10 = this.blocks[(i2 * this.length + i3 - 1) * this.width + i1]) == 0) {
							return 0;
						}

						if((z12 = this.floodFillCounters[i7 - 1024] != floodFillCounter && (b10 == i4 || b10 == i5)) && !z8) {
							this.coords[i11++] = i7 - 1024;
						}

						z8 = z12;
					}

					if(i3 < this.length - 1) {
						if((b10 = this.blocks[(i2 * this.length + i3 + 1) * this.width + i1]) == 0) {
							return 0;
						}

						if((z12 = this.floodFillCounters[i7 + 1024] != floodFillCounter && (b10 == i4 || b10 == i5)) && !z9) {
							this.coords[i11++] = i7 + 1024;
						}

						z9 = z12;
					}

					this.floodFillCounters[i7] = floodFillCounter;
					++i7;
				}
			} while(i1 >= this.width || this.blocks[(i2 * this.length + i3) * this.width + i1] != 0);

			return 0;
		} else {
			return 0;
		}
	}

	public final void playSoundAtEntity(Entity entity, String string2, float f3, float f4) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			float f6 = 16.0F;
			if(f3 > 1.0F) {
				f6 = 16.0F * f3;
			}

			if(this.playerEntity.getDistanceSqToEntity(entity) < f6 * f6) {
				((IWorldAccess)this.worldAccesses.get(i5)).playSound(string2, entity.posX, entity.posY - entity.yOffset, entity.posZ, f3, f4);
			}
		}

	}

	public final void playSoundEffect(float f1, float f2, float f3, String string4, float f5) {
		try {
			for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
				((IWorldAccess)this.worldAccesses.get(i7)).playMusic(string4, f1, f2, f3, 0.0F);
			}

		} catch (Exception exception6) {
			exception6.printStackTrace();
		}
	}

	public final void playSoundAtPlayer(float f1, float f2, float f3, String string4, float f5, float f6) {
		try {
			for(int i7 = 0; i7 < this.worldAccesses.size(); ++i7) {
				float f8 = 16.0F;
				if(f5 > 1.0F) {
					f8 = 16.0F * f5;
				}

				float f9 = f1 - this.playerEntity.posX;
				float f10 = f2 - this.playerEntity.posY;
				float f11 = f3 - this.playerEntity.posZ;
				if(f9 * f9 + f10 * f10 + f11 * f11 < f8 * f8) {
					((IWorldAccess)this.worldAccesses.get(i7)).playSound(string4, f1, f2, f3, f5, f6);
				}
			}

		} catch (Exception exception12) {
			exception12.printStackTrace();
		}
	}

	public final void extinguishFire(int i1, int i2, int i3, int i4) {
		if(i4 == 0) {
			--i2;
		}

		if(i4 == 1) {
			++i2;
		}

		if(i4 == 2) {
			--i3;
		}

		if(i4 == 3) {
			++i3;
		}

		if(i4 == 4) {
			--i1;
		}

		if(i4 == 5) {
			++i1;
		}

		if(this.getBlockId(i1, i2, i3) == Block.fire.blockID) {
			this.playSoundAtPlayer((float)i1 + 0.5F, (float)i2 + 0.5F, (float)i3 + 0.5F, "random.fizz", 0.5F, 2.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.8F);
			this.setBlockWithNotify(i1, i2, i3, 0);
		}

	}

	public final void setBlockTileEntity(int i1, int i2, int i3, TileEntity tileEntity) {
		tileEntity.worldObj = this;
		tileEntity.xCoord = i1;
		tileEntity.yCoord = i2;
		tileEntity.zCoord = i3;
		this.map.put(i1 + (i2 << 10) + (i3 << 10 << 10), tileEntity);
		this.list.add(tileEntity);
	}

	public final void removeBlockTileEntity(int i1, int i2, int i3) {
		this.list.remove(this.map.remove(i1 + (i2 << 10) + (i3 << 10 << 10)));
	}

	public final TileEntity getBlockTileEntity(int i1, int i2, int i3) {
		int i4 = i1 + (i2 << 10) + (i3 << 10 << 10);
		TileEntity tileEntity5;
		if((tileEntity5 = (TileEntity)this.map.get(i4)) == null) {
			int i6 = this.getBlockId(i1, i2, i3);
			((BlockContainer)Block.blocksList[i6]).onBlockAdded(this, i1, i2, i3);
			tileEntity5 = (TileEntity)this.map.get(i4);
		}

		return tileEntity5;
	}

	public final void spawnParticle(String string1, float f2, float f3, float f4, float f5, float f6, float f7) {
		for(int i8 = 0; i8 < this.worldAccesses.size(); ++i8) {
			((IWorldAccess)this.worldAccesses.get(i8)).spawnParticle(string1, f2, f3, f4, f5, f6, f7);
		}

	}

	public final void randomDisplayUpdates(int i1, int i2, int i3) {
		for(int i4 = 0; i4 < 1000; ++i4) {
			int i5 = i1 + this.random.nextInt(16) - this.random.nextInt(16);
			int i6 = i2 + this.random.nextInt(16) - this.random.nextInt(16);
			int i7 = i3 + this.random.nextInt(16) - this.random.nextInt(16);
			int i8;
			if((i8 = this.getBlockId(i5, i6, i7)) > 0) {
				Block.blocksList[i8].randomDisplayTick(this, i5, i6, i7, this.rand);
			}
		}

	}

	public final String debugSkylightUpdates() {
		return "" + this.tickList.size() + ". L: " + this.lightUpdates.debugLightUpdates();
	}

	public final void setLevel() {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			IWorldAccess iWorldAccess2 = (IWorldAccess)this.worldAccesses.get(i1);

			for(int i3 = 0; i3 < this.entityMap.entities.size(); ++i3) {
				iWorldAccess2.releaseEntitySkin((Entity)this.entityMap.entities.get(i1));
			}
		}

	}

	private void updateChunkLight(int i1) {
		this.lightUpdates.updateDaylightCycle(i1);
	}

	public final boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		if(this.heightMap[i1 + i3 * this.width] <= i2) {
			return true;
		} else {
			while(i2 < this.height) {
				if(Block.opaqueCubeLookup[this.getBlockId(i1, i2, i3)]) {
					return false;
				}

				++i2;
			}

			return true;
		}
	}

	static {
		for(int i0 = 0; i0 <= 15; ++i0) {
			float f1 = 1.0F - (float)i0 / 15.0F;
			lightBrightnessTable[i0] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

		floodFillCounter = 0;
	}
}