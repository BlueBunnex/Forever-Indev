package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemBlock;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class Block {
	private static StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
	private static StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
	private static StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
	private static StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
	private static StepSound soundGlassFootstep = new StepSoundGlass("stone", 1.0F, 1.0F);
	private static StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
	private static StepSound soundSandFootstep = new StepSoundSand("sand", 1.0F, 1.0F);
	public static final Block[] blocksList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	public static final boolean[] opaqueCubeLookup = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	private static boolean[] canBlockGrass = new boolean[256];
	public static final boolean[] isBlockFluid = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone;
	public static final BlockGrass grass;
	public static final Block dirt;
	public static final Block cobblestone;
	public static final Block planks;
	public static final Block sapling;
	public static final Block bedrock;
	public static final Block waterMoving;
	public static final Block waterStill;
	public static final Block lavaMoving;
	public static final Block lavaStill;
	public static final Block sand;
	public static final Block gravel;
	public static final Block oreGold;
	public static final Block oreIron;
	public static final Block oreCoal;
	public static final Block wood;
	public static final Block leaves;
	public static final Block sponge;
	public static final Block glass;
	public static final Block clothRed;
	public static final Block clothOrange;
	public static final Block clothYellow;
	public static final Block clothChartreuse;
	public static final Block clothGreen;
	public static final Block clothSpringGreen;
	public static final Block clothCyan;
	public static final Block clothCapri;
	public static final Block clothUltramarine;
	public static final Block clothViolet;
	public static final Block clothPurple;
	public static final Block clothMagenta;
	public static final Block clothRose;
	public static final Block clothDarkGray;
	public static final Block clothGray;
	public static final Block clothWhite;
	public static final BlockFlower plantYellow;
	public static final BlockFlower plantRed;
	public static final BlockFlower mushroomBrown;
	public static final BlockFlower mushroomRed;
	public static final Block blockGold;
	public static final Block blockSteel;
	public static final Block stairDouble;
	public static final Block stairSingle;
	public static final Block brick;
	public static final Block tnt;
	public static final Block bookShelf;
	public static final Block cobblestoneMossy;
	public static final Block obsidian;
	public static final Block torch;
	public static final BlockFire fire;
	public static final Block waterSource;
	public static final Block lavaSource;
	public static final Block crate;
	public static final Block cog;
	public static final Block oreDiamond;
	public static final Block blockDiamond;
	public static final Block workbench;
	public static final Block crops;
	public static final Block tilledField;
	public static final Block stoneOvenIdle;
	public static final Block stoneOvenActive;
	public int blockIndexInTexture;
	public final int blockID;
	private float hardness;
	private float resistance;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	public StepSound stepSound;
	public float blockParticleGravity;
	public final Material material;

	protected Block(int blockID, Material material) {
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		if(blocksList[blockID] != null) {
			throw new IllegalArgumentException("Slot " + blockID + " is already occupied by " + blocksList[blockID] + " when adding " + this);
		} else {
			this.material = material;
			blocksList[blockID] = this;
			this.blockID = blockID;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			opaqueCubeLookup[blockID] = this.isOpaqueCube();
			lightOpacity[blockID] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[blockID] = this.renderAsNormalBlock();
			isBlockFluid[blockID] = false;
		}
	}

	protected Block(int blockID, int blockIndexInTexture, Material material) {
		this(blockID, material);
		this.blockIndexInTexture = blockIndexInTexture;
	}

	protected final Block setLightOpacity(int i1) {
		lightOpacity[this.blockID] = i1;
		return this;
	}

	private Block setLightValue(float f1) {
		lightValue[this.blockID] = (int)(15.0F * f1);
		return this;
	}

	protected final Block setResistance(float f1) {
		this.resistance = f1 * 3.0F;
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float f1) {
		this.hardness = f1;
		if(this.resistance < f1 * 5.0F) {
			this.resistance = f1 * 5.0F;
		}

		return this;
	}

	protected final void setTickOnLoad(boolean z1) {
		tickOnLoad[this.blockID] = z1;
	}

	protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public float getBlockBrightness(World world, int i2, int i3, int i4) {
		return world.getLightBrightness(i2, i3, i4);
	}

	public boolean shouldSideBeRendered(World world, int xCoord, int yCoord, int zCoord, int i5) {
		return !world.isBlockNormalCube(xCoord, yCoord, zCoord);
	}

	public int getBlockTexture(World world, int xCoord, int yCoord, int zCoord, int blockSide) {
		return this.getBlockTextureFromSideAndMetadata(blockSide, world.getBlockMetadata(xCoord, yCoord, zCoord));
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.getBlockTextureFromSide(i1);
	}

	public int getBlockTextureFromSide(int blockSide) {
		return this.blockIndexInTexture;
	}

	public final AxisAlignedBB getSelectedBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return new AxisAlignedBB((float)xCoord + this.minX, (float)yCoord + this.minY, (float)zCoord + this.minZ, (float)xCoord + this.maxX, (float)yCoord + this.maxY, (float)zCoord + this.maxZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int xCoord, int yCoord, int zCoord) {
		return new AxisAlignedBB((float)xCoord + this.minX, (float)yCoord + this.minY, (float)zCoord + this.minZ, (float)xCoord + this.maxX, (float)yCoord + this.maxY, (float)zCoord + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public boolean isCollidable() {
		return true;
	}

	public void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
	}

	public void randomDisplayTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
	}

	public void onBlockDestroyedByPlayer(World world, int xCoord, int yCoord, int zCoord, int i5) {
	}

	public void onNeighborBlockChange(World world, int xCoord, int yCoord, int zCoord, int blockID) {
	}

	public int tickRate() {
		return 5;
	}

	public void onBlockAdded(World world, int xCoord, int yCoord, int zCoord) {
	}

	public void onBlockRemoval(World world, int xCoord, int yCoord, int zCoord) {
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int i1, Random random) {
		return this.blockID;
	}

	public final float blockStrength(EntityPlayer player) {
		if(this.hardness < 0.0F) {
			return 0.0F;
		} else if(!player.canHarvestBlock(this)) {
			return 1.0F / this.hardness / 100.0F;
		} else {
			InventoryPlayer inventoryPlayer2 = (player = player).inventory;
			float f4 = 1.0F;
			if(inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem] != null) {
				f4 = 1.0F * inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem].getItem().getStrVsBlock(this);
			}

			float f5 = f4;
			if(player.isInsideOfWater()) {
				f5 = f4 / 5.0F;
			}

			if(!player.onGround) {
				f5 /= 5.0F;
			}

			return f5 / this.hardness / 30.0F;
		}
	}

	public final void dropBlockAsItem(World world, int i2, int i3, int i4, int i5) {
		this.dropBlockAsItemWithChance(world, i2, i3, i4, i5, 1.0F);
	}

	public final void dropBlockAsItemWithChance(World world, int i2, int i3, int i4, int i5, float f6) {
		int i7 = this.quantityDropped(world.random);

		for(int i8 = 0; i8 < i7; ++i8) {
			int i9;
			if(world.random.nextFloat() <= f6 && (i9 = this.idDropped(i5, world.random)) > 0) {
				float f10 = world.random.nextFloat() * 0.7F + 0.15F;
				float f11 = world.random.nextFloat() * 0.7F + 0.15F;
				float f12 = world.random.nextFloat() * 0.7F + 0.15F;
				EntityItem entityItem13;
				(entityItem13 = new EntityItem(world, (float)i2 + f10, (float)i3 + f11, (float)i4 + f12, new ItemStack(i9))).delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityItem13);
			}
		}

	}

	public final float getExplosionResistance() {
		return this.resistance / 5.0F;
	}

	public MovingObjectPosition collisionRayTrace(World world, int i2, int i3, int i4, Vec3D vec3D, Vec3D vec3D2) {
		vec3D = vec3D.addVector((float)(-i2), (float)(-i3), (float)(-i4));
		vec3D2 = vec3D2.addVector((float)(-i2), (float)(-i3), (float)(-i4));
		Vec3D world1 = vec3D.getIntermediateWithXValue(vec3D2, this.minX);
		Vec3D vec3D7 = vec3D.getIntermediateWithXValue(vec3D2, this.maxX);
		Vec3D vec3D8 = vec3D.getIntermediateWithYValue(vec3D2, this.minY);
		Vec3D vec3D9 = vec3D.getIntermediateWithYValue(vec3D2, this.maxY);
		Vec3D vec3D10 = vec3D.getIntermediateWithZValue(vec3D2, this.minZ);
		vec3D2 = vec3D.getIntermediateWithZValue(vec3D2, this.maxZ);
		if(!this.isVecInsideYZBounds(world1)) {
			world1 = null;
		}

		if(!this.isVecInsideYZBounds(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D8)) {
			vec3D8 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D9)) {
			vec3D9 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D10)) {
			vec3D10 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D2)) {
			vec3D2 = null;
		}

		Vec3D vec3D11 = null;
		if(world1 != null) {
			vec3D11 = world1;
		}

		if(vec3D7 != null && (vec3D11 == null || vec3D.distance(vec3D7) < vec3D.distance(vec3D11))) {
			vec3D11 = vec3D7;
		}

		if(vec3D8 != null && (vec3D11 == null || vec3D.distance(vec3D8) < vec3D.distance(vec3D11))) {
			vec3D11 = vec3D8;
		}

		if(vec3D9 != null && (vec3D11 == null || vec3D.distance(vec3D9) < vec3D.distance(vec3D11))) {
			vec3D11 = vec3D9;
		}

		if(vec3D10 != null && (vec3D11 == null || vec3D.distance(vec3D10) < vec3D.distance(vec3D11))) {
			vec3D11 = vec3D10;
		}

		if(vec3D2 != null && (vec3D11 == null || vec3D.distance(vec3D2) < vec3D.distance(vec3D11))) {
			vec3D11 = vec3D2;
		}

		if(vec3D11 == null) {
			return null;
		} else {
			byte vec3D1 = -1;
			if(vec3D11 == world1) {
				vec3D1 = 4;
			}

			if(vec3D11 == vec3D7) {
				vec3D1 = 5;
			}

			if(vec3D11 == vec3D8) {
				vec3D1 = 0;
			}

			if(vec3D11 == vec3D9) {
				vec3D1 = 1;
			}

			if(vec3D11 == vec3D10) {
				vec3D1 = 2;
			}

			if(vec3D11 == vec3D2) {
				vec3D1 = 3;
			}

			return new MovingObjectPosition(i2, i3, i4, vec3D1, vec3D11.addVector((float)i2, (float)i3, (float)i4));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D vec3D) {
		return vec3D == null ? false : vec3D.yCoord >= this.minY && vec3D.yCoord <= this.maxY && vec3D.zCoord >= this.minZ && vec3D.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D vec3D) {
		return vec3D == null ? false : vec3D.xCoord >= this.minX && vec3D.xCoord <= this.maxX && vec3D.zCoord >= this.minZ && vec3D.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D vec3D) {
		return vec3D == null ? false : vec3D.xCoord >= this.minX && vec3D.xCoord <= this.maxX && vec3D.yCoord >= this.minY && vec3D.yCoord <= this.maxY;
	}

	public void onBlockDestroyedByExplosion(World world, int xCoord, int yCoord, int zCoord) {
	}

	public int getRenderBlockPass() {
		return 0;
	}

	public boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
		return true;
	}

	public boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer player) {
		return false;
	}

	public void onEntityWalking(World world, int i2, int i3, int i4) {
	}

	public void onBlockPlaced(World world, int i2, int i3, int i4, int i5) {
	}

	static {
		Block block10000 = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F);
		StepSound stepSound1 = soundStoneFootstep;
		Block block0 = block10000;
		block10000.stepSound = stepSound1;
		stone = block0;
		block10000 = (new BlockGrass(2)).setHardness(0.6F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		grass = (BlockGrass)block0;
		block10000 = (new BlockDirt(3, 2)).setHardness(0.5F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		dirt = block0;
		block10000 = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cobblestone = block0;
		block10000 = (new Block(5, 4, Material.wood)).setHardness(2.0F).setResistance(5.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		planks = block0;
		block10000 = (new BlockSapling(6, 15)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sapling = block0;
		block10000 = (new Block(7, 17, Material.rock)).setHardness(-1.0F).setResistance(6000000.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		bedrock = block0;
		waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3);
		waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3);
		lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
		lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
		block10000 = (new BlockSand(12, 18)).setHardness(0.5F);
		stepSound1 = soundSandFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sand = block0;
		block10000 = (new BlockGravel(13, 19)).setHardness(0.6F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		gravel = block0;
		block10000 = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreGold = block0;
		block10000 = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreIron = block0;
		block10000 = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreCoal = block0;
		block10000 = (new BlockLog(17)).setHardness(2.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		wood = block0;
		block10000 = (new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		leaves = block0;
		block10000 = (new BlockSponge(19)).setHardness(0.6F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sponge = block0;
		block10000 = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F);
		stepSound1 = soundGlassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		glass = block0;
		block10000 = (new Block(21, 64, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothRed = block0;
		block10000 = (new Block(22, 65, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothOrange = block0;
		block10000 = (new Block(23, 66, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothYellow = block0;
		block10000 = (new Block(24, 67, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothChartreuse = block0;
		block10000 = (new Block(25, 68, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothGreen = block0;
		block10000 = (new Block(26, 69, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothSpringGreen = block0;
		block10000 = (new Block(27, 70, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothCyan = block0;
		block10000 = (new Block(28, 71, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothCapri = block0;
		block10000 = (new Block(29, 72, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothUltramarine = block0;
		block10000 = (new Block(30, 73, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothViolet = block0;
		block10000 = (new Block(31, 74, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothPurple = block0;
		block10000 = (new Block(32, 75, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothMagenta = block0;
		block10000 = (new Block(33, 76, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothRose = block0;
		block10000 = (new Block(34, 77, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothDarkGray = block0;
		block10000 = (new Block(35, 78, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothGray = block0;
		block10000 = (new Block(36, 79, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothWhite = block0;
		block10000 = (new BlockFlower(37, 13)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		plantYellow = (BlockFlower)block0;
		block10000 = (new BlockFlower(38, 12)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		plantRed = (BlockFlower)block0;
		block10000 = (new BlockMushroom(39, 29)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		mushroomBrown = (BlockFlower)block0.setLightValue(0.125F);
		block10000 = (new BlockMushroom(40, 28)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		mushroomRed = (BlockFlower)block0;
		block10000 = (new BlockOreBlock(41, 39)).setHardness(3.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockGold = block0;
		block10000 = (new BlockOreBlock(42, 38)).setHardness(5.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockSteel = block0;
		block10000 = (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stairDouble = block0;
		block10000 = (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stairSingle = block0;
		block10000 = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		brick = block0;
		block10000 = (new BlockTNT(46, 8)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		tnt = block0;
		block10000 = (new BlockBookshelf(47, 35)).setHardness(1.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		bookShelf = block0;
		block10000 = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cobblestoneMossy = block0;
		block10000 = (new BlockStone(49, 37)).setHardness(10.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		obsidian = block0;
		block10000 = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(0.875F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		torch = block0;
		block10000 = (new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		fire = (BlockFire)block0;
		block10000 = (new BlockSource(52, waterMoving.blockID)).setHardness(0.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		waterSource = block0;
		block10000 = (new BlockSource(53, lavaMoving.blockID)).setHardness(0.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		lavaSource = block0;
		block10000 = (new BlockChest(54)).setHardness(2.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		crate = block0;
		block10000 = (new BlockGears(55, 62)).setHardness(0.5F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cog = block0;
		block10000 = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreDiamond = block0;
		block10000 = (new BlockOreBlock(57, 40)).setHardness(5.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockDiamond = block0;
		block10000 = (new BlockWorkbench(58)).setHardness(2.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		workbench = block0;
		block10000 = (new BlockCrops(59, 88)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		crops = block0;
		block10000 = (new BlockFarmland(60)).setHardness(0.6F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		tilledField = block0;
		block10000 = (new BlockFurnace(61, false)).setHardness(3.5F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stoneOvenIdle = block0;
		block10000 = (new BlockFurnace(62, true)).setHardness(3.5F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stoneOvenActive = block0.setLightValue(0.875F);

		for(int i2 = 0; i2 < 256; ++i2) {
			if(blocksList[i2] != null) {
				Item.itemsList[i2] = new ItemBlock(i2 - 256);
			}
		}

	}
}