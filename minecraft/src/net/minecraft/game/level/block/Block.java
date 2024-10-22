package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemBlock;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.Rarity;
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
	public static final boolean[] isBlockFluid = new boolean[256];
	public static final int[] lightValue = new int[256];
	
	private static boolean[] canBlockGrass = new boolean[256];
	
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
	public static final Block blockIron;
	
	public static final Block slabFull;
	public static final Block slabHalf;
	public static final Block polishedBlock;
	public static final Block polishedTiles;
	
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
	public static final Block coalLamp;
	
	public static final Block mudBrick;
	public static final Block cobbledBrick;
	
	// New Blocks (steve2024)
	public static final Block cobbledStone;
	public static final Block packedGravel;
	public static final Block compressedDirt;
	public static final Block clayBricks;
	public static final Block tintedGlass;
	public static final Block mossyCobbledStone;
	public static final Block mossyStone;
	public static final Block woodenPaver;
	
	protected String name;
	protected Rarity rarity; 
	public final int blockID;
	public final Material material;
	
	public int blockIndexInTexture;
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

	protected Block(String name, int blockID, Material material) {
		rarity = Rarity.COMMON;
		
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		if(blocksList[blockID] != null) {
			throw new IllegalArgumentException("blockID " + blockID + " is already taken by " + blocksList[blockID] + " when adding " + this);
		} else {
			this.name = name;
			this.blockID = blockID;
			this.material = material;
			
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			
			blocksList[blockID] = this;
			opaqueCubeLookup[blockID] = this.isOpaqueCube();
			lightOpacity[blockID] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[blockID] = this.renderAsNormalBlock();
			isBlockFluid[blockID] = false;
		}
	}

	protected Block(String name, int blockID, int blockIndexInTexture, Material material) {
		this(name, blockID, material);
		this.blockIndexInTexture = blockIndexInTexture;
	}

	protected final Block setLightOpacity(int var1) {
		lightOpacity[this.blockID] = var1;
		return this;
	}

	private Block setLightValue(float var1) {
		lightValue[this.blockID] = (int)(15.0F * var1);
		return this;
	}

	protected final Block setResistance(float var1) {
		this.resistance = var1 * 3.0F;
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float var1) {
		this.hardness = var1;
		if(this.resistance < var1 * 5.0F) {
			this.resistance = var1 * 5.0F;
		}

		return this;
	}

	protected final void setTickOnLoad(boolean var1) {
		tickOnLoad[this.blockID] = var1;
	}

	protected final void setBlockBounds(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.minX = var1;
		this.minY = var2;
		this.minZ = var3;
		this.maxX = var4;
		this.maxY = var5;
		this.maxZ = var6;
	}

	public float getBlockBrightness(World var1, int var2, int var3, int var4) {
		return var1.getLightBrightness(var2, var3, var4);
	}

	public boolean shouldSideBeRendered(World var1, int var2, int var3, int var4, int var5) {
		return !var1.isBlockNormalCube(var2, var3, var4);
	}

	public int getBlockTexture(World var1, int var2, int var3, int var4, int var5) {
		return this.getBlockTextureFromSideAndMetadata(var5, var1.getBlockMetadata(var2, var3, var4));
	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		return this.getBlockTextureFromSide(var1);
	}

	public int getBlockTextureFromSide(int var1) {
		return this.blockIndexInTexture;
	}

	public final AxisAlignedBB getSelectedBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return new AxisAlignedBB((float)var1 + this.minX, (float)var2 + this.minY, (float)var3 + this.minZ, (float)var1 + this.maxX, (float)var2 + this.maxY, (float)var3 + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public boolean isCollidable() {
		return true;
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
	}

	public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
	}

	public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
	}

	public int tickRate() {
		return 5;
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int var1, Random var2) {
		return this.blockID;
	}

	public final float blockStrength(EntityPlayer var1) {
		if(this.hardness < 0.0F) {
			return 0.0F;
		} else if(!var1.canHarvestBlock(this)) {
			return 1.0F / this.hardness / 100.0F;
		} else {
			InventoryPlayer var2 = var1.inventory;
			float var4 = 1.0F;
			if(var2.mainInventory[var2.currentItem] != null) {
				ItemStack var5 = var2.mainInventory[var2.currentItem];
				var4 = 1.0F * var5.getItem().getStrVsBlock(this);
			}

			float var6 = var4;
			if(var1.isInsideOfWater()) {
				var6 = var4 / 5.0F;
			}

			if(!var1.onGround) {
				var6 /= 5.0F;
			}

			return var6 / this.hardness / 30.0F;
		}
	}

	public final void dropBlockAsItem(World var1, int var2, int var3, int var4, int var5) {
		this.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, 1.0F);
	}

	public final void dropBlockAsItemWithChance(World world, int x, int y, int z, int var5, float var6) {
		int var7 = this.quantityDropped(world.random);

		for(int i = 0; i < var7; i++) {
			
			if (world.random.nextFloat() <= var6) {
				int var9 = this.idDropped(var5, world.random);
				if(var9 > 0) {
					float dx = world.random.nextFloat() * 0.7F + 0.15F;
					float dy = world.random.nextFloat() * 0.7F + 0.15F;
					float dz = world.random.nextFloat() * 0.7F + 0.15F;
					EntityItem var13 = new EntityItem(world, (float) x + dx, (float) y + dy, (float) z + dz, new ItemStack(var9));
					var13.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(var13);
				}
			}
		}

	}

	public final float getExplosionResistance() {
		return this.resistance / 5.0F;
	}

	public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6) {
		var5 = var5.addVector((float)(-var2), (float)(-var3), (float)(-var4));
		var6 = var6.addVector((float)(-var2), (float)(-var3), (float)(-var4));
		Vec3D var12 = var5.getIntermediateWithXValue(var6, this.minX);
		Vec3D var7 = var5.getIntermediateWithXValue(var6, this.maxX);
		Vec3D var8 = var5.getIntermediateWithYValue(var6, this.minY);
		Vec3D var9 = var5.getIntermediateWithYValue(var6, this.maxY);
		Vec3D var10 = var5.getIntermediateWithZValue(var6, this.minZ);
		var6 = var5.getIntermediateWithZValue(var6, this.maxZ);
		if(!this.isVecInsideYZBounds(var12)) {
			var12 = null;
		}

		if(!this.isVecInsideYZBounds(var7)) {
			var7 = null;
		}

		if(!this.isVecInsideXZBounds(var8)) {
			var8 = null;
		}

		if(!this.isVecInsideXZBounds(var9)) {
			var9 = null;
		}

		if(!this.isVecInsideXYBounds(var10)) {
			var10 = null;
		}

		if(!this.isVecInsideXYBounds(var6)) {
			var6 = null;
		}

		Vec3D var11 = null;
		if(var12 != null) {
			var11 = var12;
		}

		if(var7 != null && (var11 == null || var5.distance(var7) < var5.distance(var11))) {
			var11 = var7;
		}

		if(var8 != null && (var11 == null || var5.distance(var8) < var5.distance(var11))) {
			var11 = var8;
		}

		if(var9 != null && (var11 == null || var5.distance(var9) < var5.distance(var11))) {
			var11 = var9;
		}

		if(var10 != null && (var11 == null || var5.distance(var10) < var5.distance(var11))) {
			var11 = var10;
		}

		if(var6 != null && (var11 == null || var5.distance(var6) < var5.distance(var11))) {
			var11 = var6;
		}

		if(var11 == null) {
			return null;
		} else {
			byte var13 = -1;
			if(var11 == var12) {
				var13 = 4;
			}

			if(var11 == var7) {
				var13 = 5;
			}

			if(var11 == var8) {
				var13 = 0;
			}

			if(var11 == var9) {
				var13 = 1;
			}

			if(var11 == var10) {
				var13 = 2;
			}

			if(var11 == var6) {
				var13 = 3;
			}

			return new MovingObjectPosition(var2, var3, var4, var13, var11.addVector((float)var2, (float)var3, (float)var4));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D var1) {
		return var1 == null ? false : var1.yCoord >= this.minY && var1.yCoord <= this.maxY && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.zCoord >= this.minZ && var1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D var1) {
		return var1 == null ? false : var1.xCoord >= this.minX && var1.xCoord <= this.maxX && var1.yCoord >= this.minY && var1.yCoord <= this.maxY;
	}

	public void onBlockDestroyedByExplosion(World var1, int var2, int var3, int var4) {
	}

	public int getRenderBlockPass() {
		return 0;
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return true;
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		return false;
	}

	public void onEntityWalking(World var1, int var2, int var3, int var4) {
	}

	public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
	}

	static {
		// grass
		grass = (BlockGrass) new BlockGrass(1).setHardness(0.6F);
		grass.stepSound = soundGrassFootstep;
		
		// dirt
		dirt = new Block("Dirt", 2, 2, Material.ground).setHardness(0.5F);
		dirt.stepSound = soundGravelFootstep;
		
		// farmland
		tilledField = new BlockFarmland(3).setHardness(0.6F);
		tilledField.stepSound = soundGravelFootstep;
		
		// stone
		stone = new Block("Stone", 4, 1, Material.rock) {
			
			public final int idDropped(int var1, Random random) {
				return Block.cobblestone.blockID;
			}
			
		}.setHardness(1.5F).setResistance(10.0F);
		stone.stepSound = soundStoneFootstep;
		
		// cobblestone
		cobblestone = new Block("Cobblestone", 5, 16, Material.rock).setHardness(2.0F).setResistance(10.0F);
		cobblestone.stepSound = soundStoneFootstep;
		
		// mossy cobblestone
		cobblestoneMossy = new Block("Mossy Cobblestone", 6, 36, Material.rock).setHardness(2.0F).setResistance(10.0F);
		cobblestoneMossy.stepSound = soundStoneFootstep;
		
		// sand
		sand = new BlockGravity("Sand", 7, 18).setHardness(0.5F);
		sand.stepSound = soundSandFootstep;
		
		// gravel
		gravel = new BlockGravity("Gravel", 8, 19) {
			
			public final int idDropped(int var1, Random random) {
				return random.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
			}
			
		}.setHardness(0.6F);
		gravel.stepSound = soundGravelFootstep;
		
		// obsidian
		obsidian = new Block("Obsidian", 9, 37, Material.rock).setHardness(10.0F).setResistance(10.0F);
		obsidian.stepSound = soundStoneFootstep;
		obsidian.rarity = Rarity.RARE;
		
		// ALL the ores
		oreCoal = new BlockOre("Coal Ore", 10, 34).setHardness(3.0F).setResistance(5.0F);
		oreCoal.stepSound = soundStoneFootstep;
		
		oreIron = new BlockOre("Iron Ore", 11, 33).setHardness(3.0F).setResistance(5.0F);
		oreIron.stepSound = soundStoneFootstep;
		
		oreGold = new BlockOre("Gold Ore", 12, 32).setHardness(3.0F).setResistance(5.0F);
		oreGold.stepSound = soundStoneFootstep;
		
		oreDiamond = new BlockOre("Diamond Ore", 13, 50).setHardness(3.0F).setResistance(5.0F);
		oreDiamond.stepSound = soundStoneFootstep;
		oreDiamond.rarity = Rarity.RARE;
		
		// ALL ore blocks
		blockIron = new BlockOreBlock("Iron Block", 14, 38).setHardness(5.0F).setResistance(10.0F);
		blockIron.stepSound = soundMetalFootstep;
		
		blockGold = new BlockOreBlock("Gold Block", 15, 39).setHardness(3.0F).setResistance(10.0F);
		blockGold.stepSound = soundMetalFootstep;
		
		blockDiamond = new BlockOreBlock("Diamond Block", 16, 40).setHardness(5.0F).setResistance(10.0F);
		blockDiamond.stepSound = soundMetalFootstep;
		blockDiamond.rarity = Rarity.RARE;
		
		// sapling
		sapling = new BlockSapling("Sapling", 17, 15).setHardness(0.0F);
		sapling.stepSound = soundGrassFootstep;
		
		// (wood) log
		wood = new BlockLog(18).setHardness(2.0F);
		wood.stepSound = soundWoodFootstep;
		
		// planks
		planks = new Block("Planks", 19, 4, Material.wood).setHardness(2.0F).setResistance(5.0F);
		planks.stepSound = soundWoodFootstep;
		
		// leaves
		leaves = new BlockLeaves(20).setHardness(0.2F).setLightOpacity(1);
		leaves.stepSound = soundGrassFootstep;
		
		// ALL the cloth
		clothRed = new Block("Red Cloth", 21, 64, Material.cloth).setHardness(0.8F);
		clothRed.stepSound = soundClothFootstep;
		
		clothOrange = new Block("Orange Cloth", 22, 65, Material.cloth).setHardness(0.8F);
		clothOrange.stepSound = soundClothFootstep;
		
		clothYellow = new Block("Yellow Cloth", 23, 66, Material.cloth).setHardness(0.8F);
		clothYellow.stepSound = soundClothFootstep;
		
		clothChartreuse = new Block("Chartreuse Cloth", 24, 67, Material.cloth).setHardness(0.8F);
		clothChartreuse.stepSound = soundClothFootstep;
		
		clothGreen = new Block("Green Cloth", 25, 68, Material.cloth).setHardness(0.8F);
		clothGreen.stepSound = soundClothFootstep;
		
		clothSpringGreen = new Block("Spring Green Cloth", 26, 69, Material.cloth).setHardness(0.8F);
		clothSpringGreen.stepSound = soundClothFootstep;
		
		clothCyan = new Block("Cyan Cloth", 27, 70, Material.cloth).setHardness(0.8F);
		clothCyan.stepSound = soundClothFootstep;
		
		clothCapri = new Block("Capri Cloth", 28, 71, Material.cloth).setHardness(0.8F);
		clothCapri.stepSound = soundClothFootstep;
		
		clothUltramarine = new Block("Ultramarine Cloth", 29, 72, Material.cloth).setHardness(0.8F);
		clothUltramarine.stepSound = soundClothFootstep;
		
		clothViolet = new Block("Violet Cloth", 30, 73, Material.cloth).setHardness(0.8F);
		clothViolet.stepSound = soundClothFootstep;
		
		clothPurple = new Block("Purple Cloth", 31, 74, Material.cloth).setHardness(0.8F);
		clothPurple.stepSound = soundClothFootstep;
		
		clothMagenta = new Block("Magenta Cloth", 32, 75, Material.cloth).setHardness(0.8F);
		clothMagenta.stepSound = soundClothFootstep;
		
		clothRose = new Block("Rose Cloth", 33, 76, Material.cloth).setHardness(0.8F);
		clothRose.stepSound = soundClothFootstep;
		
		clothDarkGray = new Block("Dark Gray Cloth", 34, 77, Material.cloth).setHardness(0.8F);
		clothDarkGray.stepSound = soundClothFootstep;
		
		clothGray = new Block("Gray Cloth", 35, 78, Material.cloth).setHardness(0.8F);
		clothGray.stepSound = soundClothFootstep;
		
		clothWhite = new Block("White Cloth", 36, 79, Material.cloth).setHardness(0.8F);
		clothWhite.stepSound = soundClothFootstep;
		
		// ALL plants
		plantYellow = (BlockFlower) new BlockFlower("Dandelion", 37, 13).setHardness(0.0F);
		plantYellow.stepSound = soundGrassFootstep;
		
		plantRed = (BlockFlower) new BlockFlower("Rose", 38, 12).setHardness(0.0F);
		plantRed.stepSound = soundGrassFootstep;
		
		mushroomBrown = (BlockFlower) new BlockMushroom("Brown Mushroom", 39, 29).setHardness(0.0F).setLightValue(2.0F / 16.0F);
		mushroomBrown.stepSound = soundGrassFootstep;
		
		mushroomRed = (BlockFlower) new BlockMushroom("Red Mushroom", 40, 28).setHardness(0.0F);
		mushroomRed.stepSound = soundGrassFootstep;
		
		crops = new BlockCrops("Crops", 41, 88).setHardness(0.0F);
		crops.stepSound = soundGrassFootstep;
		
		// polished
		polishedBlock = new Block("Polished Block", 42, 96, Material.rock).setHardness(2.0F).setResistance(10.0F);
		polishedBlock.stepSound = soundStoneFootstep;
		
		polishedTiles = new Block("Polished Tiles", 43, 98, Material.rock).setHardness(2.0F).setResistance(10.0F);
		polishedTiles.stepSound = soundStoneFootstep;
		
		slabHalf = new BlockSlab("Polished Slab", 44, false).setHardness(2.0F).setResistance(10.0F);
		slabHalf.stepSound = soundStoneFootstep;
		
		slabFull = new BlockSlab("Polished Full Slab", 45, true).setHardness(2.0F).setResistance(10.0F);
		slabFull.stepSound = soundStoneFootstep;
		
		// brick
		brick = new Block("Fired Brick", 46, 7, Material.rock).setHardness(2.0F).setResistance(10.0F);
		brick.stepSound = soundStoneFootstep;
		
		// TNT
		tnt = new BlockTNT(47).setHardness(0.0F);
		tnt.stepSound = soundGrassFootstep;
		
		// bookshelf
		bookShelf = new Block("Bookshelf", 48, 35, Material.wood) {
			
			public final int getBlockTextureFromSide(int side) {
				return side <= 1 ? 4 : this.blockIndexInTexture;
			}

			public final int quantityDropped(Random random) {
				return 0;
			}
			
		}.setHardness(1.5F);
		bookShelf.stepSound = soundWoodFootstep;
		
		// glass
		glass = new BlockGlass("Glass", 49, 49, Material.glass).setHardness(0.3F);
		glass.stepSound = soundGlassFootstep;
		
		// sponge
		sponge = new BlockSponge(50).setHardness(0.6F);
		sponge.stepSound = soundGrassFootstep;
		
		// torch
		torch = new BlockTorch(51).setHardness(0.0F).setLightValue(14.0F / 16.0F);
		torch.stepSound = soundWoodFootstep;
		
		// chest
		crate = new BlockChest(52).setHardness(2.5F);
		crate.stepSound = soundWoodFootstep;
		
		// workbench / crafting table
		workbench = new BlockWorkbench(53).setHardness(2.5F);
		workbench.stepSound = soundWoodFootstep;
		
		// furnace (idle and active)
		stoneOvenIdle = new BlockFurnace("Furnace", 54, false).setHardness(3.5F);
		stoneOvenIdle.stepSound = soundStoneFootstep;
		
		stoneOvenActive = new BlockFurnace("Active Furnace", 55, true).setHardness(3.5F);
		stoneOvenActive.stepSound = soundStoneFootstep;
		stoneOvenActive.setLightValue(14.0F / 16.0F);
		
		// coal lamp
		coalLamp = new Block("Coal Lamp", 56, 81, Material.rock).setHardness(2.0F).setResistance(10.0F).setLightValue(16.0F / 16.0F);
		coalLamp.stepSound = soundStoneFootstep;
		
		// fluids (scary)
		waterMoving = new BlockFlowing("Water", 57, 14, Material.water).setHardness(100.0F).setLightOpacity(3);
		waterStill  = new BlockStationary("Water", 58, 14, Material.water).setHardness(100.0F).setLightOpacity(3);
		lavaMoving  = new BlockFlowing("Lava", 59, 30, Material.lava).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
		lavaStill   = new BlockStationary("Lava", 60, 30, Material.lava).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
		
		waterSource = new BlockSource(61, waterMoving.blockID).setHardness(0.0F);
		waterSource.stepSound = soundWoodFootstep; // idk either
		
		lavaSource = new BlockSource(62, lavaMoving.blockID).setHardness(0.0F);
		lavaSource.stepSound = soundWoodFootstep;
		
		// bedrock
		bedrock = new Block("Bedrock", 63, 17, Material.rock).setHardness(-1.0F).setResistance(6000000.0F);
		bedrock.stepSound = soundStoneFootstep;
		bedrock.rarity = Rarity.LEGENDARY;
		
		// cog (idk man)
		cog = new BlockGears(64).setHardness(0.5F);
		cog.stepSound = soundMetalFootstep;
		cog.rarity = Rarity.LEGENDARY;
		
		// fire
		fire = (BlockFire) new BlockFire(65).setHardness(0.0F).setLightValue(1.0F);
		fire.stepSound = soundWoodFootstep; // what
		fire.rarity = Rarity.LEGENDARY;
		
		// various brick types
		mudBrick = new Block("Mud Brick", 66, 99, Material.ground).setHardness(1.0F).setResistance(10.0F);
		mudBrick.stepSound = soundStoneFootstep;
		
		cobbledBrick = new Block("Cobbled Brick", 67, 100, Material.rock).setHardness(2.0F).setResistance(10.0F);
		cobbledBrick.stepSound = soundStoneFootstep;

		// New blocks (steve2024)	
		
		cobbledStone = new Block("Cobbled Stone", 68, 101, Material.rock).setHardness(2.0F).setResistance(10.0F);
		cobbledStone.stepSound = soundStoneFootstep;

		packedGravel = new Block("Packed Gravel", 69, 102, Material.ground).setHardness(0.6F).setResistance(10.0F);
		packedGravel.stepSound = soundGravelFootstep;

		compressedDirt = new Block("Compressed Dirt", 70, 103, Material.ground).setHardness(0.5F).setResistance(5.0F);
		compressedDirt.stepSound = soundGravelFootstep;

		clayBricks = new Block("Clay Bricks", 71, 104, Material.rock).setHardness(2.0F).setResistance(10.0F);
		clayBricks.stepSound = soundStoneFootstep;

		tintedGlass = new BlockGlass("Tinted Glass", 72, 105, Material.glass).setHardness(0.5F);
		tintedGlass.stepSound = soundGlassFootstep;

		mossyCobbledStone = new Block("Mossy Cobbled Stone", 73, 106, Material.rock).setHardness(2.0F).setResistance(10.0F);
		mossyCobbledStone.stepSound = soundStoneFootstep;

		mossyStone = new Block("Mossy Stone", 74, 107, Material.rock).setHardness(1.5F).setResistance(10.0F);
		mossyStone.stepSound = soundStoneFootstep;
		
		woodenPaver = new Block("Wooden Paver", 75, 108, Material.wood).setHardness(1.5F).setResistance(5.0F);
		woodenPaver.stepSound = soundWoodFootstep;
		
		// make an item for every block
		for(int i = 0; i < 256; i++) {
			
			if (blocksList[i] != null) {
				Item.itemsList[i] = new ItemBlock(blocksList[i].name, blocksList[i].rarity, i);
			}
		}

	}

	public void distortTextureCoordinates(float uOffset, float vOffset) {
	    // If the block uses a texture index, we might want to adjust its appearance by modifying its texture coordinates.
	    // We can apply the offset to the block's texture index to distort its appearance slightly.
	    
	    // The blockIndexInTexture can be thought of as a reference to where in the texture atlas the block's texture starts.
	    // We can simulate the effect of "distorting" the texture by modifying this index.
	    this.blockIndexInTexture += (int) (uOffset + vOffset);
	    
	    // Ensure the block index remains within valid bounds (0 to 255 in a 256-texture atlas).
	    if (this.blockIndexInTexture < 0) {
	        this.blockIndexInTexture = 0;
	    } else if (this.blockIndexInTexture >= 256) {
	        this.blockIndexInTexture = 255;
	    }
	}
}