package net.minecraft.game.level.generator;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.BlockFlower;
import net.minecraft.game.level.generator.noise.NoiseGeneratorDistort;
import net.minecraft.game.level.generator.noise.NoiseGeneratorOctaves;

import util.IProgressUpdate;
import util.MathHelper;

public final class LevelGenerator {
	private IProgressUpdate guiLoading;
	private int width;
	private int depth;
	private int height;
	private Random rand = new Random();
	private byte[] blocksByteArray;
	private int waterLevel;
	private int groundLevel;
	public boolean islandGen = false;
	public boolean floatingGen = false;
	public boolean flatGen = false;
	public int levelType;
	private int phaseBar;
	private int phases;
	private float phaseBareLength = 0.0F;
	private int[] floodFillBlocks = new int[1048576];

	public LevelGenerator(IProgressUpdate guiLoading) {
		this.guiLoading = guiLoading;
	}

	public final World generate(String authorName, int width, int depth, int height) {
		int i5 = 1;
		if(this.floatingGen) {
			i5 = (height - 64) / 48 + 1;
		}

		this.phases = 13 + i5 * 4;
		this.guiLoading.displayProgressMessage("Generating level");
		World world6;
		(world6 = new World()).waterLevel = this.waterLevel;
		world6.groundLevel = this.groundLevel;
		this.width = width;
		this.depth = depth;
		this.height = height;
		this.blocksByteArray = new byte[width * depth * height];

		int i7;
		LevelGenerator levelGenerator9;
		int i21;
		int i25;
		int i31;
		int i45;
		int i51;
		int i52;
		int i53;
		int i56;
		for(i7 = 0; i7 < i5; ++i7) {
			this.waterLevel = height - 32 - i7 * 48;
			this.groundLevel = this.waterLevel - 2;
			int[] i8;
			NoiseGeneratorOctaves noiseGeneratorOctaves13;
			int i22;
			int[] i46;
			if(this.flatGen) {
				i8 = new int[width * depth];

				for(i45 = 0; i45 < i8.length; ++i45) {
					i8[i45] = 0;
				}

				this.loadingBar();
				this.loadingBar();
			} else {
				this.guiLoading.displayLoadingString("Raising..");
				this.loadingBar();
				levelGenerator9 = this;
				NoiseGeneratorDistort noiseGeneratorDistort10 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorDistort noiseGeneratorDistort11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
				NoiseGeneratorOctaves noiseGeneratorOctaves12 = new NoiseGeneratorOctaves(this.rand, 6);
				noiseGeneratorOctaves13 = new NoiseGeneratorOctaves(this.rand, 2);
				int[] i14 = new int[this.width * this.depth];
				i22 = 0;

				label349:
				while(true) {
					if(i22 >= levelGenerator9.width) {
						i8 = i14;
						this.guiLoading.displayLoadingString("Eroding..");
						this.loadingBar();
						i46 = i14;
						levelGenerator9 = this;
						noiseGeneratorDistort11 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						NoiseGeneratorDistort noiseGeneratorDistort50 = new NoiseGeneratorDistort(new NoiseGeneratorOctaves(this.rand, 8), new NoiseGeneratorOctaves(this.rand, 8));
						i52 = 0;

						while(true) {
							if(i52 >= levelGenerator9.width) {
								break label349;
							}

							levelGenerator9.setNextPhase((float)i52 * 100.0F / (float)(levelGenerator9.width - 1));

							for(i53 = 0; i53 < levelGenerator9.depth; ++i53) {
								double d20 = noiseGeneratorDistort11.generateNoise((double)(i52 << 1), (double)(i53 << 1)) / 8.0D;
								i22 = noiseGeneratorDistort50.generateNoise((double)(i52 << 1), (double)(i53 << 1)) > 0.0D ? 1 : 0;
								if(d20 > 2.0D) {
									int i58 = ((i46[i52 + i53 * levelGenerator9.width] - i22) / 2 << 1) + i22;
									i46[i52 + i53 * levelGenerator9.width] = i58;
								}
							}

							++i52;
						}
					}

					double d23 = Math.abs(((double)i22 / ((double)levelGenerator9.width - 1.0D) - 0.5D) * 2.0D);
					levelGenerator9.setNextPhase((float)i22 * 100.0F / (float)(levelGenerator9.width - 1));

					for(i25 = 0; i25 < levelGenerator9.depth; ++i25) {
						double d26 = Math.abs(((double)i25 / ((double)levelGenerator9.depth - 1.0D) - 0.5D) * 2.0D);
						double d28 = noiseGeneratorDistort10.generateNoise((double)((float)i22 * 1.3F), (double)((float)i25 * 1.3F)) / 6.0D + -4.0D;
						double d30 = noiseGeneratorDistort11.generateNoise((double)((float)i22 * 1.3F), (double)((float)i25 * 1.3F)) / 5.0D + 10.0D + -4.0D;
						if(noiseGeneratorOctaves12.generateNoise((double)i22, (double)i25) / 8.0D > 0.0D) {
							d30 = d28;
						}

						double d34 = Math.max(d28, d30) / 2.0D;
						if(levelGenerator9.islandGen) {
							double d36 = Math.sqrt(d23 * d23 + d26 * d26) * 1.2000000476837158D;
							double d39 = noiseGeneratorOctaves13.generateNoise((double)((float)i22 * 0.05F), (double)((float)i25 * 0.05F)) / 4.0D + 1.0D;
							if((d36 = Math.max(Math.min(d36, d39), Math.max(d23, d26))) > 1.0D) {
								d36 = 1.0D;
							}

							if(d36 < 0.0D) {
								d36 = 0.0D;
							}

							d36 *= d36;
							if((d34 = d34 * (1.0D - d36) - d36 * 10.0D + 5.0D) < 0.0D) {
								d34 -= d34 * d34 * (double)0.2F;
							}
						} else if(d34 < 0.0D) {
							d34 *= 0.8D;
						}

						i14[i22 + i25 * levelGenerator9.width] = (int)d34;
					}

					++i22;
				}
			}

			this.guiLoading.displayLoadingString("Soiling..");
			this.loadingBar();
			i46 = i8;
			levelGenerator9 = this;
			int i49 = this.width;
			i51 = this.depth;
			i52 = this.height;
			NoiseGeneratorOctaves noiseGeneratorOctaves54 = new NoiseGeneratorOctaves(this.rand, 8);
			NoiseGeneratorOctaves noiseGeneratorOctaves55 = new NoiseGeneratorOctaves(this.rand, 8);

			for(i21 = 0; i21 < i49; ++i21) {
				double d57 = Math.abs(((double)i21 / ((double)i49 - 1.0D) - 0.5D) * 2.0D);
				levelGenerator9.setNextPhase((float)i21 * 100.0F / (float)(i49 - 1));

				for(int i24 = 0; i24 < i51; ++i24) {
					double d64 = Math.abs(((double)i24 / ((double)i51 - 1.0D) - 0.5D) * 2.0D);
					double d27 = (d27 = Math.max(d57, d64)) * d27 * d27;
					int i29 = (int)(noiseGeneratorOctaves54.generateNoise((double)i21, (double)i24) / 24.0D) - 4;
					int i72;
					i31 = (i72 = i46[i21 + i24 * i49] + levelGenerator9.waterLevel) + i29;
					i46[i21 + i24 * i49] = Math.max(i72, i31);
					if(i46[i21 + i24 * i49] > i52 - 2) {
						i46[i21 + i24 * i49] = i52 - 2;
					}

					if(i46[i21 + i24 * i49] <= 0) {
						i46[i21 + i24 * i49] = 1;
					}

					double d32;
					int i76;
					if((i76 = (int)((double)((int)(Math.sqrt(Math.abs(d32 = noiseGeneratorOctaves55.generateNoise((double)i21 * 2.3D, (double)i24 * 2.3D) / 24.0D)) * Math.signum(d32) * 20.0D) + levelGenerator9.waterLevel) * (1.0D - d27) + d27 * (double)levelGenerator9.height)) > levelGenerator9.waterLevel) {
						i76 = levelGenerator9.height;
					}

					for(int i35 = 0; i35 < i52; ++i35) {
						int i78 = (i35 * levelGenerator9.depth + i24) * levelGenerator9.width + i21;
						int i37 = 0;
						if(i35 <= i72) {
							i37 = Block.dirt.blockID;
						}

						if(i35 <= i31) {
							i37 = Block.stone.blockID;
						}

						if(levelGenerator9.floatingGen && i35 < i76) {
							i37 = 0;
						}

						if(levelGenerator9.blocksByteArray[i78] == 0) {
							levelGenerator9.blocksByteArray[i78] = (byte)i37;
						}
					}
				}
			}

			this.guiLoading.displayLoadingString("Growing..");
			this.loadingBar();
			i46 = i8;
			levelGenerator9 = this;
			i49 = this.width;
			i51 = this.depth;
			noiseGeneratorOctaves13 = new NoiseGeneratorOctaves(this.rand, 8);
			noiseGeneratorOctaves54 = new NoiseGeneratorOctaves(this.rand, 8);
			i56 = this.waterLevel - 1;
			if(this.levelType == 2) {
				i56 += 2;
			}

			for(i21 = 0; i21 < i49; ++i21) {
				levelGenerator9.setNextPhase((float)i21 * 100.0F / (float)(i49 - 1));

				for(i22 = 0; i22 < i51; ++i22) {
					boolean z59 = noiseGeneratorOctaves13.generateNoise((double)i21, (double)i22) > 8.0D;
					if(levelGenerator9.islandGen) {
						z59 = noiseGeneratorOctaves13.generateNoise((double)i21, (double)i22) > -8.0D;
					}

					if(levelGenerator9.levelType == 2) {
						z59 = noiseGeneratorOctaves13.generateNoise((double)i21, (double)i22) > -32.0D;
					}

					boolean z61 = noiseGeneratorOctaves54.generateNoise((double)i21, (double)i22) > 12.0D;
					if(levelGenerator9.levelType == 1 || levelGenerator9.levelType == 3) {
						z59 = noiseGeneratorOctaves13.generateNoise((double)i21, (double)i22) > -8.0D;
					}

					int i65 = ((i25 = i46[i21 + i22 * i49]) * levelGenerator9.depth + i22) * levelGenerator9.width + i21;
					int i67;
					if(((i67 = levelGenerator9.blocksByteArray[((i25 + 1) * levelGenerator9.depth + i22) * levelGenerator9.width + i21] & 255) == Block.waterMoving.blockID || i67 == Block.waterStill.blockID || i67 == 0) && i25 <= levelGenerator9.waterLevel - 1 && z61) {
						levelGenerator9.blocksByteArray[i65] = (byte)Block.gravel.blockID;
					}

					if(i67 == 0) {
						int i69 = -1;
						if(i25 <= i56 && z59) {
							i69 = Block.sand.blockID;
							if(levelGenerator9.levelType == 1) {
								i69 = Block.grass.blockID;
							}
						}

						if(levelGenerator9.blocksByteArray[i65] != 0 && i69 > 0) {
							levelGenerator9.blocksByteArray[i65] = (byte)i69;
						}
					}
				}
			}
		}

		this.guiLoading.displayLoadingString("Carving..");
		this.loadingBar();
		levelGenerator9 = this;
		i51 = this.width;
		i52 = this.depth;
		i53 = this.height;
		i56 = i51 * i52 * i53 / 256 / 64 << 1;

		for(i21 = 0; i21 < i56; ++i21) {
			levelGenerator9.setNextPhase((float)i21 * 100.0F / (float)(i56 - 1));
			float f60 = levelGenerator9.rand.nextFloat() * (float)i51;
			float f63 = levelGenerator9.rand.nextFloat() * (float)i53;
			float f62 = levelGenerator9.rand.nextFloat() * (float)i52;
			i25 = (int)((levelGenerator9.rand.nextFloat() + levelGenerator9.rand.nextFloat()) * 200.0F);
			float f66 = levelGenerator9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float f68 = 0.0F;
			float f71 = levelGenerator9.rand.nextFloat() * (float)Math.PI * 2.0F;
			float f70 = 0.0F;
			float f73 = levelGenerator9.rand.nextFloat() * levelGenerator9.rand.nextFloat();

			for(i31 = 0; i31 < i25; ++i31) {
				f60 += MathHelper.sin(f66) * MathHelper.cos(f71);
				f62 += MathHelper.cos(f66) * MathHelper.cos(f71);
				f63 += MathHelper.sin(f71);
				f66 += f68 * 0.2F;
				f68 = (f68 *= 0.9F) + (levelGenerator9.rand.nextFloat() - levelGenerator9.rand.nextFloat());
				f71 = (f71 + f70 * 0.5F) * 0.5F;
				f70 = (f70 *= 0.75F) + (levelGenerator9.rand.nextFloat() - levelGenerator9.rand.nextFloat());
				if(levelGenerator9.rand.nextFloat() >= 0.25F) {
					float f74 = f60 + (levelGenerator9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f33 = f63 + (levelGenerator9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f77 = f62 + (levelGenerator9.rand.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f75 = ((float)levelGenerator9.height - f33) / (float)levelGenerator9.height;
					float f79 = 1.2F + (f75 * 3.5F + 1.0F) * f73;
					float f80 = MathHelper.sin((float)i31 * (float)Math.PI / (float)i25) * f79;

					for(i5 = (int)(f74 - f80); i5 <= (int)(f74 + f80); ++i5) {
						for(int i81 = (int)(f33 - f80); i81 <= (int)(f33 + f80); ++i81) {
							for(int i40 = (int)(f77 - f80); i40 <= (int)(f77 + f80); ++i40) {
								float f41 = (float)i5 - f74;
								float f42 = (float)i81 - f33;
								float f48 = (float)i40 - f77;
								if(f41 * f41 + f42 * f42 * 2.0F + f48 * f48 < f80 * f80 && i5 > 0 && i81 > 0 && i40 > 0 && i5 < levelGenerator9.width - 1 && i81 < levelGenerator9.height - 1 && i40 < levelGenerator9.depth - 1) {
									i7 = (i81 * levelGenerator9.depth + i40) * levelGenerator9.width + i5;
									if(levelGenerator9.blocksByteArray[i7] == Block.stone.blockID) {
										levelGenerator9.blocksByteArray[i7] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		i7 = this.populateOre(Block.oreCoal.blockID, 1000, 10, (height << 2) / 5);
		int i44 = this.populateOre(Block.oreIron.blockID, 800, 8, height * 3 / 5);
		i45 = this.populateOre(Block.oreGold.blockID, 500, 6, (height << 1) / 5);
		i5 = this.populateOre(Block.oreDiamond.blockID, 800, 2, height / 5);
		System.out.println("Coal: " + i7 + ", Iron: " + i44 + ", Gold: " + i45 + ", Diamond: " + i5);
		this.guiLoading.displayLoadingString("Melting..");
		this.loadingBar();
		this.lavaGen();
		world6.cloudHeight = height + 2;
		if(this.floatingGen) {
			this.groundLevel = -128;
			this.waterLevel = this.groundLevel + 1;
			world6.cloudHeight = -16;
		} else if(!this.islandGen) {
			this.groundLevel = this.waterLevel + 1;
			this.waterLevel = this.groundLevel - 16;
		} else {
			this.groundLevel = this.waterLevel - 9;
		}

		this.guiLoading.displayLoadingString("Watering..");
		this.loadingBar();
		this.liquidThemeSpawner();
		if(!this.floatingGen) {
			i5 = Block.waterStill.blockID;
			if(this.levelType == 1) {
				i5 = Block.lavaStill.blockID;
			}

			for(i7 = 0; i7 < width; ++i7) {
				this.floodFill(i7, this.waterLevel - 1, 0, 0, i5);
				this.floodFill(i7, this.waterLevel - 1, depth - 1, 0, i5);
			}

			for(i7 = 0; i7 < depth; ++i7) {
				this.floodFill(width - 1, this.waterLevel - 1, i7, 0, i5);
				this.floodFill(0, this.waterLevel - 1, i7, 0, i5);
			}
		}

		if(this.levelType == 0) {
			world6.skyColor = 10079487;
			world6.fogColor = 0xFFFFFF;
			world6.cloudColor = 0xFFFFFF;
		}

		if(this.levelType == 1) {
			world6.cloudColor = 2164736;
			world6.fogColor = 1049600;
			world6.skyColor = 1049600;
			world6.skylightSubtracted = world6.skyBrightness = 7;
			world6.defaultFluid = Block.lavaMoving.blockID;
			if(this.floatingGen) {
				world6.cloudHeight = height + 2;
				this.waterLevel = -16;
			}
		}

		if(this.levelType == 2) {
			world6.skyColor = 13033215;
			world6.fogColor = 13033215;
			world6.cloudColor = 15658751;
			world6.skylightSubtracted = world6.skyBrightness = 15;
			world6.skyBrightness = 16;
			world6.cloudHeight = height + 64;
		}

		if(this.levelType == 3) {
			world6.skyColor = 7699847;
			world6.fogColor = 5069403;
			world6.cloudColor = 5069403;
			world6.skylightSubtracted = world6.skyBrightness = 12;
		}

		world6.waterLevel = this.waterLevel;
		world6.groundLevel = this.groundLevel;
		this.guiLoading.displayLoadingString("Assembling..");
		this.loadingBar();
		this.setNextPhase(0.0F);
		world6.generate(width, height, depth, this.blocksByteArray, (byte[])null);
		this.guiLoading.displayLoadingString("Building..");
		this.loadingBar();
		this.setNextPhase(0.0F);
		world6.findSpawn();
		generateHouse(world6);
		this.guiLoading.displayLoadingString("Planting..");
		this.loadingBar();
		if(this.levelType != 1) {
			this.growGrassOnDirt(world6);
		}

		this.loadingBar();
		this.growTrees(world6);
		if(this.levelType == 3) {
			for(i5 = 0; i5 < 50; ++i5) {
				this.growTrees(world6);
			}
		}

		short s43 = 100;
		if(this.levelType == 2) {
			s43 = 1000;
		}

		this.loadingBar();
		this.populateFlowersAndMushrooms(world6, Block.plantYellow, s43);
		this.loadingBar();
		this.populateFlowersAndMushrooms(world6, Block.plantRed, s43);
		this.loadingBar();
		this.populateFlowersAndMushrooms(world6, Block.mushroomBrown, 50);
		this.loadingBar();
		this.populateFlowersAndMushrooms(world6, Block.mushroomRed, 50);
		this.guiLoading.displayLoadingString("Lighting..");
		this.loadingBar();

		for(i7 = 0; i7 < 10000; ++i7) {
			this.setNextPhase((float)(i7 * 100 / 10000));
			world6.updateLighting();
		}

		this.guiLoading.displayLoadingString("Spawning..");
		this.loadingBar();
		MobSpawner mobSpawner47 = new MobSpawner(world6);

		for(width = 0; width < 1000; ++width) {
			this.setNextPhase((float)width * 100.0F / 999.0F);
			mobSpawner47.performSpawning();
		}

		world6.createTime = System.currentTimeMillis();
		world6.authorName = authorName;
		world6.name = "A Nice World";
		if(this.phaseBar != this.phases) {
			throw new IllegalStateException("Wrong number of phases! Wanted " + this.phases + ", got " + this.phaseBar);
		} else {
			return world6;
		}
	}

	private static void generateHouse(World world) {
		int i1 = world.xSpawn;
		int i2 = world.ySpawn;
		int i3 = world.zSpawn;

		for(int i4 = i1 - 3; i4 <= i1 + 3; ++i4) {
			for(int i5 = i2 - 2; i5 <= i2 + 2; ++i5) {
				for(int i6 = i3 - 3; i6 <= i3 + 3; ++i6) {
					int i7 = i5 < i2 - 1 ? Block.obsidian.blockID : 0;
					if(i4 == i1 - 3 || i6 == i3 - 3 || i4 == i1 + 3 || i6 == i3 + 3 || i5 == i2 - 2 || i5 == i2 + 2) {
						i7 = Block.stone.blockID;
						if(i5 >= i2 - 1) {
							i7 = Block.planks.blockID;
						}
					}

					if(i6 == i3 - 3 && i4 == i1 && i5 >= i2 - 1 && i5 <= i2) {
						i7 = 0;
					}

					world.setBlockWithNotify(i4, i5, i6, i7);
				}
			}
		}

		world.setBlockWithNotify(i1 - 3 + 1, i2, i3, Block.torch.blockID);
		world.setBlockWithNotify(i1 + 3 - 1, i2, i3, Block.torch.blockID);
	}

	private void growGrassOnDirt(World world) {
		for(int i2 = 0; i2 < this.width; ++i2) {
			this.setNextPhase((float)i2 * 100.0F / (float)(this.width - 1));

			for(int i3 = 0; i3 < this.height; ++i3) {
				for(int i4 = 0; i4 < this.depth; ++i4) {
					if(world.getBlockId(i2, i3, i4) == Block.dirt.blockID && world.getBlockLightValue(i2, i3 + 1, i4) >= 4 && !world.getBlockMaterial(i2, i3 + 1, i4).getCanBlockGrass()) {
						world.setBlock(i2, i3, i4, Block.grass.blockID);
					}
				}
			}
		}

	}

	private void growTrees(World world) {
		int i2 = this.width * this.depth * this.height / 80000;

		for(int i3 = 0; i3 < i2; ++i3) {
			if(i3 % 100 == 0) {
				this.setNextPhase((float)i3 * 100.0F / (float)(i2 - 1));
			}

			int i4 = this.rand.nextInt(this.width);
			int i5 = this.rand.nextInt(this.height);
			int i6 = this.rand.nextInt(this.depth);

			for(int i7 = 0; i7 < 25; ++i7) {
				int i8 = i4;
				int i9 = i5;
				int i10 = i6;

				for(int i11 = 0; i11 < 20; ++i11) {
					i8 += this.rand.nextInt(12) - this.rand.nextInt(12);
					i9 += this.rand.nextInt(3) - this.rand.nextInt(6);
					i10 += this.rand.nextInt(12) - this.rand.nextInt(12);
					if(i8 >= 0 && i9 >= 0 && i10 >= 0 && i8 < this.width && i9 < this.height && i10 < this.depth) {
						world.growTrees(i8, i9, i10);
					}
				}
			}
		}

	}

	private void populateFlowersAndMushrooms(World world, BlockFlower flowers, int i3) {
		i3 = (int)((long)this.width * (long)this.depth * (long)this.height * (long)i3 / 1600000L);

		for(int i4 = 0; i4 < i3; ++i4) {
			if(i4 % 100 == 0) {
				this.setNextPhase((float)i4 * 100.0F / (float)(i3 - 1));
			}

			int i5 = this.rand.nextInt(this.width);
			int i6 = this.rand.nextInt(this.height);
			int i7 = this.rand.nextInt(this.depth);

			for(int i8 = 0; i8 < 10; ++i8) {
				int i9 = i5;
				int i10 = i6;
				int i11 = i7;

				for(int i12 = 0; i12 < 10; ++i12) {
					i9 += this.rand.nextInt(4) - this.rand.nextInt(4);
					i10 += this.rand.nextInt(2) - this.rand.nextInt(2);
					i11 += this.rand.nextInt(4) - this.rand.nextInt(4);
					if(i9 >= 0 && i11 >= 0 && i10 > 0 && i9 < this.width && i11 < this.depth && i10 < this.height && world.getBlockId(i9, i10, i11) == 0 && flowers.canBlockStay(world, i9, i10, i11)) {
						world.setBlockWithNotify(i9, i10, i11, flowers.blockID);
					}
				}
			}
		}

	}

	private int populateOre(int i1, int i2, int i3, int i4) {
		int i5 = 0;
		byte b26 = (byte)i1;
		int i6 = this.width;
		int i7 = this.depth;
		int i8 = this.height;
		i2 = i6 * i7 * i8 / 256 / 64 * i2 / 100;

		for(int i9 = 0; i9 < i2; ++i9) {
			this.setNextPhase((float)i9 * 100.0F / (float)(i2 - 1));
			float f10 = this.rand.nextFloat() * (float)i6;
			float f11 = this.rand.nextFloat() * (float)i8;
			float f12 = this.rand.nextFloat() * (float)i7;
			if(f11 <= (float)i4) {
				int i13 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)i3 / 100.0F);
				float f14 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f15 = 0.0F;
				float f16 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f17 = 0.0F;

				for(int i18 = 0; i18 < i13; ++i18) {
					f10 += MathHelper.sin(f14) * MathHelper.cos(f16);
					f12 += MathHelper.cos(f14) * MathHelper.cos(f16);
					f11 += MathHelper.sin(f16);
					f14 += f15 * 0.2F;
					f15 = (f15 *= 0.9F) + (this.rand.nextFloat() - this.rand.nextFloat());
					f16 = (f16 + f17 * 0.5F) * 0.5F;
					f17 = (f17 *= 0.9F) + (this.rand.nextFloat() - this.rand.nextFloat());
					float f19 = MathHelper.sin((float)i18 * (float)Math.PI / (float)i13) * (float)i3 / 100.0F + 1.0F;

					for(int i20 = (int)(f10 - f19); i20 <= (int)(f10 + f19); ++i20) {
						for(int i21 = (int)(f11 - f19); i21 <= (int)(f11 + f19); ++i21) {
							for(int i22 = (int)(f12 - f19); i22 <= (int)(f12 + f19); ++i22) {
								float f23 = (float)i20 - f10;
								float f24 = (float)i21 - f11;
								float f25 = (float)i22 - f12;
								if(f23 * f23 + f24 * f24 * 2.0F + f25 * f25 < f19 * f19 && i20 > 0 && i21 > 0 && i22 > 0 && i20 < this.width - 1 && i21 < this.height - 1 && i22 < this.depth - 1) {
									int i27 = (i21 * this.depth + i22) * this.width + i20;
									if(this.blocksByteArray[i27] == Block.stone.blockID) {
										this.blocksByteArray[i27] = b26;
										++i5;
									}
								}
							}
						}
					}
				}
			}
		}

		return i5;
	}

	private void liquidThemeSpawner() {
		int i1 = Block.waterStill.blockID;
		if(this.levelType == 1) {
			i1 = Block.lavaStill.blockID;
		}

		int i2 = this.width * this.depth * this.height / 1000;

		for(int i3 = 0; i3 < i2; ++i3) {
			if(i3 % 100 == 0) {
				this.setNextPhase((float)i3 * 100.0F / (float)(i2 - 1));
			}

			int i4 = this.rand.nextInt(this.width);
			int i5 = this.rand.nextInt(this.height);
			int i6 = this.rand.nextInt(this.depth);
			if(this.blocksByteArray[(i5 * this.depth + i6) * this.width + i4] == 0) {
				long j7;
				if((j7 = this.floodFill(i4, i5, i6, 0, 255)) > 0L && j7 < 640L) {
					this.floodFill(i4, i5, i6, 255, i1);
				} else {
					this.floodFill(i4, i5, i6, 255, 0);
				}
			}
		}

		this.setNextPhase(100.0F);
	}

	private void loadingBar() {
		++this.phaseBar;
		this.phaseBareLength = 0.0F;
		this.setNextPhase(0.0F);
	}

	private void setNextPhase(float f1) {
		if(f1 < 0.0F) {
			throw new IllegalStateException("Failed to set next phase!");
		} else {
			int i2 = (int)(((float)(this.phaseBar - 1) + f1 / 100.0F) * 100.0F / (float)this.phases);
			this.guiLoading.setLoadingProgress(i2);
		}
	}

	private void lavaGen() {
		int i1 = this.width * this.depth * this.height / 2000;
		int i2 = this.groundLevel;

		for(int i3 = 0; i3 < i1; ++i3) {
			if(i3 % 100 == 0) {
				this.setNextPhase((float)i3 * 100.0F / (float)(i1 - 1));
			}

			int i4 = this.rand.nextInt(this.width);
			int i5 = Math.min(Math.min(this.rand.nextInt(i2), this.rand.nextInt(i2)), Math.min(this.rand.nextInt(i2), this.rand.nextInt(i2)));
			int i6 = this.rand.nextInt(this.depth);
			if(this.blocksByteArray[(i5 * this.depth + i6) * this.width + i4] == 0) {
				long j7;
				if((j7 = this.floodFill(i4, i5, i6, 0, 255)) > 0L && j7 < 640L) {
					this.floodFill(i4, i5, i6, 255, Block.lavaStill.blockID);
				} else {
					this.floodFill(i4, i5, i6, 255, 0);
				}
			}
		}

		this.setNextPhase(100.0F);
	}

	private long floodFill(int i1, int i2, int i3, int i4, int i5) {
		byte b6 = (byte)i5;
		byte b22 = (byte)i4;
		ArrayList arrayList7 = new ArrayList();
		byte b8 = 0;
		int i9 = 1;

		int i10;
		for(i10 = 1; 1 << i9 < this.width; ++i9) {
		}

		while(1 << i10 < this.depth) {
			++i10;
		}

		int i11 = this.depth - 1;
		int i12 = this.width - 1;
		int i23 = b8 + 1;
		this.floodFillBlocks[0] = ((i2 << i10) + i3 << i9) + i1;
		long j14 = 0L;
		i1 = this.width * this.depth;

		while(i23 > 0) {
			--i23;
			i2 = this.floodFillBlocks[i23];
			if(i23 == 0 && arrayList7.size() > 0) {
				this.floodFillBlocks = (int[])arrayList7.remove(arrayList7.size() - 1);
				i23 = this.floodFillBlocks.length;
			}

			i3 = i2 >> i9 & i11;
			int i13 = i2 >> i9 + i10;

			int i16;
			int i17;
			for(i17 = i16 = i2 & i12; i16 > 0 && this.blocksByteArray[i2 - 1] == b22; --i2) {
				--i16;
			}

			while(i17 < this.width && this.blocksByteArray[i2 + i17 - i16] == b22) {
				++i17;
			}

			int i18 = i2 >> i9 & i11;
			int i19 = i2 >> i9 + i10;
			if(i5 == 255 && (i16 == 0 || i17 == this.width - 1 || i13 == 0 || i13 == this.height - 1 || i3 == 0 || i3 == this.depth - 1)) {
				return -1L;
			}

			if(i18 != i3 || i19 != i13) {
				System.out.println("Diagonal flood!?");
			}

			boolean z24 = false;
			boolean z25 = false;
			boolean z20 = false;
			j14 += (long)(i17 - i16);

			for(i16 = i16; i16 < i17; ++i16) {
				this.blocksByteArray[i2] = b6;
				boolean z21;
				if(i3 > 0) {
					if((z21 = this.blocksByteArray[i2 - this.width] == b22) && !z24) {
						if(i23 == this.floodFillBlocks.length) {
							arrayList7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i23 = 0;
						}

						this.floodFillBlocks[i23++] = i2 - this.width;
					}

					z24 = z21;
				}

				if(i3 < this.depth - 1) {
					if((z21 = this.blocksByteArray[i2 + this.width] == b22) && !z25) {
						if(i23 == this.floodFillBlocks.length) {
							arrayList7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i23 = 0;
						}

						this.floodFillBlocks[i23++] = i2 + this.width;
					}

					z25 = z21;
				}

				if(i13 > 0) {
					byte b26 = this.blocksByteArray[i2 - i1];
					if((b6 == Block.lavaMoving.blockID || b6 == Block.lavaStill.blockID) && (b26 == Block.waterMoving.blockID || b26 == Block.waterStill.blockID)) {
						this.blocksByteArray[i2 - i1] = (byte)Block.stone.blockID;
					}

					if((z21 = b26 == b22) && !z20) {
						if(i23 == this.floodFillBlocks.length) {
							arrayList7.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i23 = 0;
						}

						this.floodFillBlocks[i23++] = i2 - i1;
					}

					z20 = z21;
				}

				++i2;
			}
		}

		return j14;
	}
}