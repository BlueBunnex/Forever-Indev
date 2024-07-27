package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.level.block.Block;

public final class Light {
	private int lightingUpdateCounter = 0;
	private List lightingUpdateList = new ArrayList();
	private World worldObj;
	private int worldWidth;
	private int worldLength;
	private int worldHeight;
	private byte[] blocks;
	private byte[] data;
	private int[] heightMap;
	private List skyLightList = new ArrayList();
	private List blockLightList = new ArrayList();
	private List lightingUpdateList1 = new ArrayList();
	private List lightingUpdateList2 = new ArrayList();
	private int[] lightingUpdateList3 = this.getLightingUpdates();
	private byte[] chunks;
	private MetadataChunkBlock metadataChunkBlock = null;
	private int lightValue = 0;
	private int skylightSubtracted;

	public Light(World world) {
		this.worldObj = world;
		this.worldWidth = world.width;
		this.worldLength = world.length;
		this.worldHeight = world.height;
		this.chunks = new byte[world.blocks.length / 8];
		this.blocks = world.blocks;
		this.data = world.data;
		this.heightMap = world.heightMap;
	}

	private int[] getLightingUpdates() {
		return this.lightingUpdateList2.size() > 0 ? (int[])this.lightingUpdateList2.remove(this.lightingUpdateList2.size() - 1) : new int[32768];
	}

	public final void updateSkylight(int i1, int i2, int i3, int i4) {
		this.lightingUpdateList1.add(new MetadataChunkBlock(this, i1, i2, 0, i3, i4, 1));
	}

	public final void updateDaylightCycle(int i1) {
		if(i1 > 15) {
			i1 = 15;
		}

		if(i1 < 0) {
			i1 = 0;
		}

		this.skylightSubtracted = i1 - this.worldObj.skylightSubtracted;
		if(this.skylightSubtracted != 0) {
			this.lightValue = this.worldObj.skylightSubtracted;
			this.worldObj.skylightSubtracted = i1;

			while(this.metadataChunkBlock != null) {
				this.updateLight(64);
			}

			this.metadataChunkBlock = new MetadataChunkBlock(this, 0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
		}
	}

	private void updateLight(int i1) {
		int i2 = this.metadataChunkBlock.x;
		int i3 = this.metadataChunkBlock.maxX;
		int i4 = this.metadataChunkBlock.z;
		int i5 = this.metadataChunkBlock.maxZ;

		int i6;
		int i7;
		for(i2 = i2; i2 < i3; ++i2) {
			if(i1-- <= 0 && i2 != i3 - 1) {
				this.metadataChunkBlock.x = i2;
				return;
			}

			for(i6 = i4; i6 < i5; ++i6) {
				for(i7 = this.heightMap[i2 + i6 * this.worldWidth] - 1; i7 > 0 && Block.lightOpacity[this.blocks[(i7 * this.worldLength + i6) * this.worldWidth + i2]] < 100; --i7) {
				}

				++i7;

				for(; i7 < this.worldHeight; ++i7) {
					int i8 = (i7 * this.worldLength + i6) * this.worldWidth + i2;
					int i9;
					if(Block.lightValue[this.blocks[i8]] == 0 && (i9 = this.data[i8] & 15) <= this.lightValue) {
						if(this.skylightSubtracted < 0 && i9 > 0) {
							--this.data[i8];
						} else if(this.skylightSubtracted > 0 && i9 < 15) {
							++this.data[i8];
						}
					}
				}
			}
		}

		for(i6 = 0; i6 < this.worldWidth; i6 += 32) {
			for(i7 = 0; i7 < this.worldLength; i7 += 32) {
				this.blockLightList.add(new MetadataChunkBlock(this, i6, 0, i7, i6 + 32, this.worldHeight, i7 + 32));
				this.skyLightList.add(new MetadataChunkBlock(this, i6, 0, i7, i6 + 32, this.worldHeight, i7 + 32));
			}
		}

		for(i6 = 0; i6 < this.worldObj.worldAccesses.size(); ++i6) {
			((IWorldAccess)this.worldObj.worldAccesses.get(i6)).updateAllRenderers();
		}

		this.metadataChunkBlock = null;
	}

	public final void updateBlockLight(int i1, int i2, int i3, int i4, int i5, int i6) {
		this.blockLightList.add(new MetadataChunkBlock(this, i1, i2, i3, i4, i5, i6));
	}

	private void updateLists(int i1, int i2, int i3, int i4, int i5, int i6) {
		for(i2 = i2; i2 < i5; ++i2) {
			for(int i7 = i3; i7 < i6; ++i7) {
				for(int i8 = i1; i8 < i4; ++i8) {
					int i9 = i8 + i2 * this.worldWidth + i7 * this.worldWidth * this.worldHeight;
					if((this.chunks[i9 >> 3] & 1 << (i9 & 7)) == 0) {
						this.chunks[i9 >> 3] = (byte)(this.chunks[i9 >> 3] | 1 << (i9 & 7));
						this.lightingUpdateList3[this.lightingUpdateCounter++] = i9;
						if((this.chunks[i9 >> 3] & 1 << (i9 & 7)) == 0) {
							System.out.println("OMG ERROR!");
						}

						if(this.lightingUpdateCounter > this.lightingUpdateList3.length - 32) {
							i9 = this.lightingUpdateList3[--this.lightingUpdateCounter];
							this.lightingUpdateList3[this.lightingUpdateList3.length - 1] = this.lightingUpdateCounter;
							this.lightingUpdateList.add(this.lightingUpdateList3);
							this.lightingUpdateList3 = this.getLightingUpdates();
							this.lightingUpdateCounter = 1;
							this.lightingUpdateList3[0] = i9;
						}
					}
				}
			}
		}

	}

	public final void updateLight() {
		if(this.lightingUpdateList2.size() > 0) {
			this.lightingUpdateList2.remove(this.lightingUpdateList2.size() - 1);
		}

		int i1 = 5;

		while(this.skyLightList.size() > 0 && i1-- > 0) {
			MetadataChunkBlock metadataChunkBlock2 = (MetadataChunkBlock)this.skyLightList.remove(0);

			for(int i3 = 0; i3 < this.worldObj.worldAccesses.size(); ++i3) {
				((IWorldAccess)this.worldObj.worldAccesses.get(i3)).markBlockRangeNeedsUpdate(metadataChunkBlock2.x, metadataChunkBlock2.y, metadataChunkBlock2.z, metadataChunkBlock2.maxX, metadataChunkBlock2.maxY, metadataChunkBlock2.maxZ);
			}
		}

		if(this.metadataChunkBlock != null) {
			this.updateLight(8);
		} else {
			for(int i19 = 0; i19 < 16; ++i19) {
				boolean z20 = false;
				MetadataChunkBlock metadataChunkBlock21;
				if(this.blockLightList.size() > 0) {
					z20 = true;
					metadataChunkBlock21 = (MetadataChunkBlock)this.blockLightList.remove(0);
					this.updateLists(metadataChunkBlock21.x, metadataChunkBlock21.y, metadataChunkBlock21.z, metadataChunkBlock21.maxX, metadataChunkBlock21.maxY, metadataChunkBlock21.maxZ);
				}

				int i4;
				int i5;
				int i6;
				int i7;
				int i8;
				int i9;
				int i10;
				int i11;
				int i12;
				int i13;
				Light light22;
				if(this.lightingUpdateList1.size() > 0) {
					z20 = true;
					metadataChunkBlock21 = (MetadataChunkBlock)this.lightingUpdateList1.remove(0);
					i7 = metadataChunkBlock21.maxY;
					i6 = metadataChunkBlock21.maxX;
					i5 = metadataChunkBlock21.y;
					i4 = metadataChunkBlock21.x;
					light22 = this;

					for(i8 = i4; i8 < i4 + i6; ++i8) {
						for(i9 = i5; i9 < i5 + i7; ++i9) {
							i10 = light22.heightMap[i8 + i9 * light22.worldWidth];

							for(i11 = light22.worldHeight - 1; i11 > 0 && Block.lightOpacity[light22.blocks[(i11 * light22.worldLength + i9) * light22.worldWidth + i8]] == 0; --i11) {
							}

							light22.heightMap[i8 + i9 * light22.worldWidth] = i11 + 1;
							if(i10 != i11) {
								i12 = i10 < i11 ? i10 : i11;
								i13 = i10 > i11 ? i10 : i11;
								light22.updateLists(i8, i12, i9, i8 + 1, i13, i9 + 1);
							}
						}
					}
				}

				light22 = this;
				i4 = this.worldObj.skylightSubtracted;
				i5 = -999;
				i6 = -999;
				i7 = -999;
				i8 = -999;
				i9 = -999;
				i10 = -999;
				i11 = 1024;
				i12 = 0;

				while(i11-- > 0 && (light22.lightingUpdateCounter > 0 || light22.lightingUpdateList.size() > 0)) {
					++i12;
					if(light22.lightingUpdateCounter == 0) {
						if(light22.lightingUpdateList3 != null) {
							int[] i23 = light22.lightingUpdateList3;
							light22.lightingUpdateList2.add(i23);
						}

						light22.lightingUpdateList3 = (int[])light22.lightingUpdateList.remove(light22.lightingUpdateList.size() - 1);
						light22.lightingUpdateCounter = light22.lightingUpdateList3[light22.lightingUpdateList3.length - 1];
					}

					if(light22.lightingUpdateCounter > light22.lightingUpdateList3.length - 32) {
						i13 = light22.lightingUpdateList3[--light22.lightingUpdateCounter];
						light22.lightingUpdateList3[light22.lightingUpdateList3.length - 1] = light22.lightingUpdateCounter;
						light22.lightingUpdateList.add(light22.lightingUpdateList3);
						light22.lightingUpdateList3 = light22.getLightingUpdates();
						light22.lightingUpdateCounter = 1;
						light22.lightingUpdateList3[0] = i13;
					} else {
						i1 = (i13 = light22.lightingUpdateList3[--light22.lightingUpdateCounter]) % light22.worldWidth;
						int i14 = i13 / light22.worldWidth % light22.worldHeight;
						int i15 = i13 / light22.worldWidth / light22.worldHeight % light22.worldLength;
						light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] ^ 1 << (i13 & 7));
						i13 = light22.heightMap[i1 + i15 * light22.worldWidth];
						int i16 = i14 >= i13 ? i4 : 0;
						byte b24 = light22.blocks[(i14 * light22.worldLength + i15) * light22.worldWidth + i1];
						int i17;
						if((i17 = Block.lightOpacity[b24]) > 100) {
							i16 = 0;
						} else if(i16 < 15) {
							if((i17 = i17) == 0) {
								i17 = 1;
							}

							int i18;
							if(i1 > 0 && (i18 = (light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + (i1 - 1)] & 15) - i17) > i16) {
								i16 = i18;
							}

							if(i1 < light22.worldWidth - 1 && (i18 = (light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + i1 + 1] & 15) - i17) > i16) {
								i16 = i18;
							}

							if(i14 > 0 && (i18 = (light22.data[((i14 - 1) * light22.worldLength + i15) * light22.worldWidth + i1] & 15) - i17) > i16) {
								i16 = i18;
							}

							if(i14 < light22.worldHeight - 1 && (i18 = (light22.data[((i14 + 1) * light22.worldLength + i15) * light22.worldWidth + i1] & 15) - i17) > i16) {
								i16 = i18;
							}

							if(i15 > 0 && (i18 = (light22.data[(i14 * light22.worldLength + (i15 - 1)) * light22.worldWidth + i1] & 15) - i17) > i16) {
								i16 = i18;
							}

							if(i15 < light22.worldLength - 1 && (i18 = (light22.data[(i14 * light22.worldLength + i15 + 1) * light22.worldWidth + i1] & 15) - i17) > i16) {
								i16 = i18;
							}
						}

						if(i16 < Block.lightValue[b24]) {
							i16 = Block.lightValue[b24];
						}

						if((light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + i1] & 15) != i16) {
							light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + i1] = (byte)((light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + i1] & 240) + i16);
							if(i1 > 0 && (light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + (i1 - 1)] & 15) != i16 - 1) {
								i13 = i1 - 1 + i14 * light22.worldWidth + i15 * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i1 < light22.worldWidth - 1 && (light22.data[(i14 * light22.worldLength + i15) * light22.worldWidth + i1 + 1] & 15) != i16 - 1) {
								i13 = i1 + 1 + i14 * light22.worldWidth + i15 * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i14 > 0 && (light22.data[((i14 - 1) * light22.worldLength + i15) * light22.worldWidth + i1] & 15) != i16 - 1) {
								i13 = i1 + (i14 - 1) * light22.worldWidth + i15 * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i14 < light22.worldHeight - 1 && (light22.data[((i14 + 1) * light22.worldLength + i15) * light22.worldWidth + i1] & 15) != i16 - 1) {
								i13 = i1 + (i14 + 1) * light22.worldWidth + i15 * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i15 > 0 && (light22.data[(i14 * light22.worldLength + (i15 - 1)) * light22.worldWidth + i1] & 15) != i16 - 1) {
								i13 = i1 + i14 * light22.worldWidth + (i15 - 1) * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i15 < light22.worldLength - 1 && (light22.data[(i14 * light22.worldLength + i15 + 1) * light22.worldWidth + i1] & 15) != i16 - 1) {
								i13 = i1 + i14 * light22.worldWidth + (i15 + 1) * light22.worldWidth * light22.worldHeight;
								if((light22.chunks[i13 >> 3] & 1 << (i13 & 7)) == 0) {
									light22.chunks[i13 >> 3] = (byte)(light22.chunks[i13 >> 3] | 1 << (i13 & 7));
									light22.lightingUpdateList3[light22.lightingUpdateCounter++] = i13;
								}
							}

							if(i5 == -999) {
								i5 = i1;
								i6 = i1;
								i7 = i14;
								i8 = i14;
								i9 = i15;
								i10 = i15;
							}

							if(i1 < i5) {
								i5 = i1;
							} else if(i1 > i6) {
								i6 = i1;
							}

							if(i14 > i8) {
								i8 = i14;
							} else if(i14 < i7) {
								i7 = i14;
							}

							if(i15 < i9) {
								i9 = i15;
							} else if(i15 > i10) {
								i10 = i15;
							}
						}
					}
				}

				if(i5 > -999) {
					light22.skyLightList.add(new MetadataChunkBlock(light22, i5, i7, i9, i6, i8, i10));
				}

				if(i12 > 0) {
					z20 = true;
				}
			}

		}
	}

	public final String debugLightUpdates() {
		return "" + (this.blockLightList.size() + this.skyLightList.size());
	}
}