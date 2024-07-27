package net.minecraft.client.render;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

import org.lwjgl.opengl.GL11;

public final class RenderBlocks {
	private World blockAccess;
	private int overrideBlockTexture = -1;
	private boolean renderAllFaces = false;

	public RenderBlocks(World world) {
		this.blockAccess = world;
	}

	public RenderBlocks() {
	}

	public final void renderBlockUsingTexture(Block block, int i2, int i3, int i4, int i5) {
		this.overrideBlockTexture = i5;
		this.renderBlockByRenderType(block, i2, i3, i4);
		this.overrideBlockTexture = -1;
	}

	public final void renderBlockAllFaces(Block block, int i2, int i3, int i4) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(block, i2, i3, i4);
		this.renderAllFaces = false;
	}

	public final boolean renderBlockByRenderType(Block block, int i2, int i3, int i4) {
		int i5;
		Tessellator tessellator6;
		float f10;
		boolean z26;
		if((i5 = block.getRenderType()) == 0) {
			tessellator6 = Tessellator.instance;
			z26 = false;
			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2, i3 - 1, i4);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.5F * f10, 0.5F * f10, 0.5F * f10);
				this.renderBlockBottom(block, (float)i2, (float)i3, (float)i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
				z26 = true;
			}

			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(f10 * 1.0F, f10 * 1.0F, f10 * 1.0F);
				this.renderBlockTop(block, (float)i2, (float)i3, (float)i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
				z26 = true;
			}

			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2, i3, i4 - 1);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.8F * f10, 0.8F * f10, 0.8F * f10);
				this.renderBlockNorth(block, i2, i3, i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 2));
				z26 = true;
			}

			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2, i3, i4 + 1);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.8F * f10, 0.8F * f10, 0.8F * f10);
				this.renderBlockSouth(block, i2, i3, i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 3));
				z26 = true;
			}

			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2 - 1, i3, i4);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.6F * f10, 0.6F * f10, 0.6F * f10);
				this.renderBlockWest(block, i2, i3, i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 4));
				z26 = true;
			}

			if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
				f10 = block.getBlockBrightness(this.blockAccess, i2 + 1, i3, i4);
				if(Block.lightValue[block.blockID] > 0) {
					f10 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.6F * f10, 0.6F * f10, 0.6F * f10);
				this.renderBlockEast(block, i2, i3, i4, block.getBlockTexture(this.blockAccess, i2, i3, i4, 5));
				z26 = true;
			}

			return z26;
		} else {
			float f11;
			float f22;
			if(i5 == 4) {
				tessellator6 = Tessellator.instance;
				z26 = false;
				f10 = block.minY;
				f11 = block.maxY;
				block.maxY = f11 - this.materialNotWater(i2, i3, i4);
				if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2, i3 - 1, i4);
					tessellator6.setColorOpaque_F(0.5F * f22, 0.5F * f22, 0.5F * f22);
					this.renderBlockBottom(block, (float)i2, (float)i3, (float)i4, block.getBlockTextureFromSide(0));
					z26 = true;
				}

				if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
					tessellator6.setColorOpaque_F(f22 * 1.0F, f22 * 1.0F, f22 * 1.0F);
					this.renderBlockTop(block, (float)i2, (float)i3, (float)i4, block.getBlockTextureFromSide(1));
					z26 = true;
				}

				block.minY = f11 - this.materialNotWater(i2, i3, i4 - 1);
				if(this.renderAllFaces || block.maxY > block.minY || block.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2, i3, i4 - 1);
					tessellator6.setColorOpaque_F(0.8F * f22, 0.8F * f22, 0.8F * f22);
					this.renderBlockNorth(block, i2, i3, i4, block.getBlockTextureFromSide(2));
					z26 = true;
				}

				block.minY = f11 - this.materialNotWater(i2, i3, i4 + 1);
				if(this.renderAllFaces || block.maxY > block.minY || block.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2, i3, i4 + 1);
					tessellator6.setColorOpaque_F(0.8F * f22, 0.8F * f22, 0.8F * f22);
					this.renderBlockSouth(block, i2, i3, i4, block.getBlockTextureFromSide(3));
					z26 = true;
				}

				block.minY = f11 - this.materialNotWater(i2 - 1, i3, i4);
				if(this.renderAllFaces || block.maxY > block.minY || block.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2 - 1, i3, i4);
					tessellator6.setColorOpaque_F(0.6F * f22, 0.6F * f22, 0.6F * f22);
					this.renderBlockWest(block, i2, i3, i4, block.getBlockTextureFromSide(4));
					z26 = true;
				}

				block.minY = f11 - this.materialNotWater(i2 + 1, i3, i4);
				if(this.renderAllFaces || block.maxY > block.minY || block.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
					f22 = block.getBlockBrightness(this.blockAccess, i2 + 1, i3, i4);
					tessellator6.setColorOpaque_F(0.6F * f22, 0.6F * f22, 0.6F * f22);
					this.renderBlockEast(block, i2, i3, i4, block.getBlockTextureFromSide(5));
					z26 = true;
				}

				block.minY = f10;
				block.maxY = f11;
				return z26;
			} else if(i5 == 1) {
				tessellator6 = Tessellator.instance;
				f22 = block.getBlockBrightness(this.blockAccess, i2, i3, i4);
				tessellator6.setColorOpaque_F(f22, f22, f22);
				this.renderBlockPlant(block, this.blockAccess.getBlockMetadata(i2, i3, i4), (float)i2, (float)i3, (float)i4);
				return true;
			} else if(i5 == 6) {
				tessellator6 = Tessellator.instance;
				f22 = block.getBlockBrightness(this.blockAccess, i2, i3, i4);
				tessellator6.setColorOpaque_F(f22, f22, f22);
				this.renderBlockCrops(block, this.blockAccess.getBlockMetadata(i2, i3, i4), (float)i2, (float)i3 - 0.0625F, (float)i4);
				return true;
			} else {
				float f8;
				if(i5 == 2) {
					byte b21 = this.blockAccess.getBlockMetadata(i2, i3, i4);
					Tessellator tessellator25 = Tessellator.instance;
					f8 = block.getBlockBrightness(this.blockAccess, i2, i3, i4);
					if(Block.lightValue[block.blockID] > 0) {
						f8 = 1.0F;
					}

					tessellator25.setColorOpaque_F(f8, f8, f8);
					if(b21 == 1) {
						this.renderBlockTorch(block, (float)i2 - 0.099999994F, (float)i3 + 0.2F, (float)i4, -0.4F, 0.0F);
					} else if(b21 == 2) {
						this.renderBlockTorch(block, (float)i2 + 0.099999994F, (float)i3 + 0.2F, (float)i4, 0.4F, 0.0F);
					} else if(b21 == 3) {
						this.renderBlockTorch(block, (float)i2, (float)i3 + 0.2F, (float)i4 - 0.099999994F, 0.0F, -0.4F);
					} else if(b21 == 4) {
						this.renderBlockTorch(block, (float)i2, (float)i3 + 0.2F, (float)i4 + 0.099999994F, 0.0F, 0.4F);
					} else {
						this.renderBlockTorch(block, (float)i2, (float)i3, (float)i4, 0.0F, 0.0F);
					}

					return true;
				} else {
					int i7;
					float f12;
					float f13;
					float f14;
					float f15;
					int i23;
					if(i5 == 3) {
						i5 = i4;
						i4 = i3;
						i3 = i2;
						tessellator6 = Tessellator.instance;
						i7 = block.getBlockTextureFromSide(0);
						if(this.overrideBlockTexture >= 0) {
							i7 = this.overrideBlockTexture;
						}

						f8 = block.getBlockBrightness(this.blockAccess, i2, i4, i5);
						tessellator6.setColorOpaque_F(f8, f8, f8);
						i2 = (i7 & 15) << 4;
						i23 = i7 & 240;
						float f24 = (float)i2 / 256.0F;
						f10 = ((float)i2 + 15.99F) / 256.0F;
						f11 = (float)i23 / 256.0F;
						f22 = ((float)i23 + 15.99F) / 256.0F;
						float f16;
						float f19;
						if(!this.blockAccess.isBlockNormalCube(i3, i4 - 1, i5) && !Block.fire.canBlockCatchFire(this.blockAccess, i3, i4 - 1, i5)) {
							if((i3 + i4 + i5 & 1) == 1) {
								f24 = (float)i2 / 256.0F;
								f10 = ((float)i2 + 15.99F) / 256.0F;
								f11 = (float)(i23 + 16) / 256.0F;
								f22 = ((float)i23 + 15.99F + 16.0F) / 256.0F;
							}

							if((i3 / 2 + i4 / 2 + i5 / 2 & 1) == 1) {
								f14 = f10;
								f10 = f24;
								f24 = f14;
							}

							if(Block.fire.canBlockCatchFire(this.blockAccess, i3 - 1, i4, i5)) {
								tessellator6.addVertexWithUV((float)i3 + 0.2F, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1), f10, f11);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)i3 + 0.2F, (float)i4 + 1.4F + 0.0625F, (float)i5, f24, f11);
								tessellator6.addVertexWithUV((float)i3 + 0.2F, (float)i4 + 1.4F + 0.0625F, (float)i5, f24, f11);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)i3 + 0.2F, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1), f10, f11);
							}

							if(Block.fire.canBlockCatchFire(this.blockAccess, i3 + 1, i4, i5)) {
								tessellator6.addVertexWithUV((float)(i3 + 1) - 0.2F, (float)i4 + 1.4F + 0.0625F, (float)i5, f24, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1) - 0.2F, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1), f10, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1) - 0.2F, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1), f10, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1) - 0.2F, (float)i4 + 1.4F + 0.0625F, (float)i5, f24, f11);
							}

							if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4, i5 - 1)) {
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F + 0.0625F, (float)i5 + 0.2F, f10, f11);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)i5, f10, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F + 0.0625F, (float)i5 + 0.2F, f24, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F + 0.0625F, (float)i5 + 0.2F, f24, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)i5, f24, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)i5, f10, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F + 0.0625F, (float)i5 + 0.2F, f10, f11);
							}

							if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4, i5 + 1)) {
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1) - 0.2F, f24, f11);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)(i5 + 1), f24, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1) - 0.2F, f10, f11);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1) - 0.2F, f10, f11);
								tessellator6.addVertexWithUV((float)i3, (float)i4 + 0.0625F, (float)(i5 + 1), f10, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 0.0625F, (float)(i5 + 1), f24, f22);
								tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F + 0.0625F, (float)(i5 + 1) - 0.2F, f24, f11);
							}

							if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4 + 1, i5)) {
								f14 = (float)i3 + 0.5F + 0.5F;
								f15 = (float)i3 + 0.5F - 0.5F;
								f19 = (float)i5 + 0.5F + 0.5F;
								f16 = (float)i5 + 0.5F - 0.5F;
								f24 = (float)i2 / 256.0F;
								f10 = ((float)i2 + 15.99F) / 256.0F;
								f11 = (float)i23 / 256.0F;
								f22 = ((float)i23 + 15.99F) / 256.0F;
								++i4;
								if((i3 + i4 + i5 & 1) == 0) {
									tessellator6.addVertexWithUV(f15, (float)i4 + -0.2F, (float)i5, f10, f11);
									tessellator6.addVertexWithUV(f14, (float)i4, (float)i5, f10, f22);
									tessellator6.addVertexWithUV(f14, (float)i4, (float)(i5 + 1), f24, f22);
									tessellator6.addVertexWithUV(f15, (float)i4 + -0.2F, (float)(i5 + 1), f24, f11);
									f24 = (float)i2 / 256.0F;
									f10 = ((float)i2 + 15.99F) / 256.0F;
									f11 = (float)(i23 + 16) / 256.0F;
									f22 = ((float)i23 + 15.99F + 16.0F) / 256.0F;
									tessellator6.addVertexWithUV(f14, (float)i4 + -0.2F, (float)(i5 + 1), f10, f11);
									tessellator6.addVertexWithUV(f15, (float)i4, (float)(i5 + 1), f10, f22);
									tessellator6.addVertexWithUV(f15, (float)i4, (float)i5, f24, f22);
									tessellator6.addVertexWithUV(f14, (float)i4 + -0.2F, (float)i5, f24, f11);
								} else {
									tessellator6.addVertexWithUV((float)i3, (float)i4 + -0.2F, f19, f10, f11);
									tessellator6.addVertexWithUV((float)i3, (float)i4, f16, f10, f22);
									tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f16, f24, f22);
									tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + -0.2F, f19, f24, f11);
									f24 = (float)i2 / 256.0F;
									f10 = ((float)i2 + 15.99F) / 256.0F;
									f11 = (float)(i23 + 16) / 256.0F;
									f22 = ((float)i23 + 15.99F + 16.0F) / 256.0F;
									tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + -0.2F, f16, f10, f11);
									tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f19, f10, f22);
									tessellator6.addVertexWithUV((float)i3, (float)i4, f19, f24, f22);
									tessellator6.addVertexWithUV((float)i3, (float)i4 + -0.2F, f16, f24, f11);
								}
							}
						} else {
							f12 = (float)i3 + 0.5F + 0.2F;
							f13 = (float)i3 + 0.5F - 0.2F;
							f14 = (float)i5 + 0.5F + 0.2F;
							f15 = (float)i5 + 0.5F - 0.2F;
							f19 = (float)i3 + 0.5F - 0.3F;
							f16 = (float)i3 + 0.5F + 0.3F;
							float f17 = (float)i5 + 0.5F - 0.3F;
							float f18 = (float)i5 + 0.5F + 0.3F;
							tessellator6.addVertexWithUV(f19, (float)i4 + 1.4F, (float)(i5 + 1), f10, f11);
							tessellator6.addVertexWithUV(f12, (float)i4, (float)(i5 + 1), f10, f22);
							tessellator6.addVertexWithUV(f12, (float)i4, (float)i5, f24, f22);
							tessellator6.addVertexWithUV(f19, (float)i4 + 1.4F, (float)i5, f24, f11);
							tessellator6.addVertexWithUV(f16, (float)i4 + 1.4F, (float)i5, f10, f11);
							tessellator6.addVertexWithUV(f13, (float)i4, (float)i5, f10, f22);
							tessellator6.addVertexWithUV(f13, (float)i4, (float)(i5 + 1), f24, f22);
							tessellator6.addVertexWithUV(f16, (float)i4 + 1.4F, (float)(i5 + 1), f24, f11);
							f24 = (float)i2 / 256.0F;
							f10 = ((float)i2 + 15.99F) / 256.0F;
							f11 = (float)(i23 + 16) / 256.0F;
							f22 = ((float)i23 + 15.99F + 16.0F) / 256.0F;
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F, f18, f10, f11);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f15, f10, f22);
							tessellator6.addVertexWithUV((float)i3, (float)i4, f15, f24, f22);
							tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F, f18, f24, f11);
							tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F, f17, f10, f11);
							tessellator6.addVertexWithUV((float)i3, (float)i4, f14, f10, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f14, f24, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F, f17, f24, f11);
							f12 = (float)i3 + 0.5F - 0.5F;
							f13 = (float)i3 + 0.5F + 0.5F;
							f14 = (float)i5 + 0.5F - 0.5F;
							f15 = (float)i5 + 0.5F + 0.5F;
							f19 = (float)i3 + 0.5F - 0.4F;
							f16 = (float)i3 + 0.5F + 0.4F;
							f17 = (float)i5 + 0.5F - 0.4F;
							f18 = (float)i5 + 0.5F + 0.4F;
							tessellator6.addVertexWithUV(f19, (float)i4 + 1.4F, (float)i5, f24, f11);
							tessellator6.addVertexWithUV(f12, (float)i4, (float)i5, f24, f22);
							tessellator6.addVertexWithUV(f12, (float)i4, (float)(i5 + 1), f10, f22);
							tessellator6.addVertexWithUV(f19, (float)i4 + 1.4F, (float)(i5 + 1), f10, f11);
							tessellator6.addVertexWithUV(f16, (float)i4 + 1.4F, (float)(i5 + 1), f24, f11);
							tessellator6.addVertexWithUV(f13, (float)i4, (float)(i5 + 1), f24, f22);
							tessellator6.addVertexWithUV(f13, (float)i4, (float)i5, f10, f22);
							tessellator6.addVertexWithUV(f16, (float)i4 + 1.4F, (float)i5, f10, f11);
							f24 = (float)i2 / 256.0F;
							f10 = ((float)i2 + 15.99F) / 256.0F;
							f11 = (float)i23 / 256.0F;
							f22 = ((float)i23 + 15.99F) / 256.0F;
							tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F, f18, f24, f11);
							tessellator6.addVertexWithUV((float)i3, (float)i4, f15, f24, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f15, f10, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F, f18, f10, f11);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4 + 1.4F, f17, f24, f11);
							tessellator6.addVertexWithUV((float)(i3 + 1), (float)i4, f14, f24, f22);
							tessellator6.addVertexWithUV((float)i3, (float)i4, f14, f10, f22);
							tessellator6.addVertexWithUV((float)i3, (float)i4 + 1.4F, f17, f10, f11);
						}

						return true;
					} else if(i5 == 5) {
						i5 = i4;
						i4 = i3;
						i3 = i2;
						tessellator6 = Tessellator.instance;
						i7 = block.getBlockTextureFromSide(0);
						if(this.overrideBlockTexture >= 0) {
							i7 = this.overrideBlockTexture;
						}

						f8 = block.getBlockBrightness(this.blockAccess, i2, i4, i5);
						tessellator6.setColorOpaque_F(f8, f8, f8);
						i2 = ((i7 & 15) << 4) + 16;
						i23 = (i7 & 15) << 4;
						int i9 = i7 & 240;
						if((i3 + i4 + i5 & 1) == 1) {
							i2 = (i7 & 15) << 4;
							i23 = ((i7 & 15) << 4) + 16;
						}

						f10 = (float)i2 / 256.0F;
						f11 = ((float)i2 + 15.99F) / 256.0F;
						f22 = (float)i9 / 256.0F;
						float f20 = ((float)i9 + 15.99F) / 256.0F;
						f12 = (float)i23 / 256.0F;
						f13 = ((float)i23 + 15.99F) / 256.0F;
						f14 = (float)i9 / 256.0F;
						f15 = ((float)i9 + 15.99F) / 256.0F;
						if(this.blockAccess.isBlockNormalCube(i3 - 1, i4, i5)) {
							tessellator6.addVertexWithUV((float)i3 + 0.05F, (float)(i4 + 1) + 0.125F, (float)(i5 + 1) + 0.125F, f10, f22);
							tessellator6.addVertexWithUV((float)i3 + 0.05F, (float)i4 - 0.125F, (float)(i5 + 1) + 0.125F, f10, f20);
							tessellator6.addVertexWithUV((float)i3 + 0.05F, (float)i4 - 0.125F, (float)i5 - 0.125F, f11, f20);
							tessellator6.addVertexWithUV((float)i3 + 0.05F, (float)(i4 + 1) + 0.125F, (float)i5 - 0.125F, f11, f22);
						}

						if(this.blockAccess.isBlockNormalCube(i3 + 1, i4, i5)) {
							tessellator6.addVertexWithUV((float)(i3 + 1) - 0.05F, (float)i4 - 0.125F, (float)(i5 + 1) + 0.125F, f11, f20);
							tessellator6.addVertexWithUV((float)(i3 + 1) - 0.05F, (float)(i4 + 1) + 0.125F, (float)(i5 + 1) + 0.125F, f11, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1) - 0.05F, (float)(i4 + 1) + 0.125F, (float)i5 - 0.125F, f10, f22);
							tessellator6.addVertexWithUV((float)(i3 + 1) - 0.05F, (float)i4 - 0.125F, (float)i5 - 0.125F, f10, f20);
						}

						if(this.blockAccess.isBlockNormalCube(i3, i4, i5 - 1)) {
							tessellator6.addVertexWithUV((float)(i3 + 1) + 0.125F, (float)i4 - 0.125F, (float)i5 + 0.05F, f13, f15);
							tessellator6.addVertexWithUV((float)(i3 + 1) + 0.125F, (float)(i4 + 1) + 0.125F, (float)i5 + 0.05F, f13, f14);
							tessellator6.addVertexWithUV((float)i3 - 0.125F, (float)(i4 + 1) + 0.125F, (float)i5 + 0.05F, f12, f14);
							tessellator6.addVertexWithUV((float)i3 - 0.125F, (float)i4 - 0.125F, (float)i5 + 0.05F, f12, f15);
						}

						if(this.blockAccess.isBlockNormalCube(i3, i4, i5 + 1)) {
							tessellator6.addVertexWithUV((float)(i3 + 1) + 0.125F, (float)(i4 + 1) + 0.125F, (float)(i5 + 1) - 0.05F, f12, f14);
							tessellator6.addVertexWithUV((float)(i3 + 1) + 0.125F, (float)i4 - 0.125F, (float)(i5 + 1) - 0.05F, f12, f15);
							tessellator6.addVertexWithUV((float)i3 - 0.125F, (float)i4 - 0.125F, (float)(i5 + 1) - 0.05F, f13, f15);
							tessellator6.addVertexWithUV((float)i3 - 0.125F, (float)(i4 + 1) + 0.125F, (float)(i5 + 1) - 0.05F, f13, f14);
						}

						return true;
					} else {
						return false;
					}
				}
			}
		}
	}

	private void renderBlockTorch(Block block, float f2, float f3, float f4, float f5, float f6) {
		Tessellator tessellator7 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		int i8 = (block1 & 15) << 4;
		block1 &= 240;
		float f9 = (float)i8 / 256.0F;
		float f21 = ((float)i8 + 15.99F) / 256.0F;
		float f10 = (float)block1 / 256.0F;
		float block2 = ((float)block1 + 15.99F) / 256.0F;
		float f11 = f9 + 0.02734375F;
		float f12 = f10 + 0.0234375F;
		float f13 = f9 + 0.03515625F;
		float f14 = f10 + 0.03125F;
		f2 += 0.5F;
		f4 += 0.5F;
		float f15 = f2 - 0.5F;
		float f16 = f2 + 0.5F;
		float f17 = f4 - 0.5F;
		float f18 = f4 + 0.5F;
		tessellator7.addVertexWithUV(f2 + f5 * 0.375F - 0.0625F, f3 + 0.625F, f4 + f6 * 0.375F - 0.0625F, f11, f12);
		tessellator7.addVertexWithUV(f2 + f5 * 0.375F - 0.0625F, f3 + 0.625F, f4 + f6 * 0.375F + 0.0625F, f11, f14);
		tessellator7.addVertexWithUV(f2 + f5 * 0.375F + 0.0625F, f3 + 0.625F, f4 + f6 * 0.375F + 0.0625F, f13, f14);
		tessellator7.addVertexWithUV(f2 + f5 * 0.375F + 0.0625F, f3 + 0.625F, f4 + f6 * 0.375F - 0.0625F, f13, f12);
		tessellator7.addVertexWithUV(f2 - 0.0625F, f3 + 1.0F, f17, f9, f10);
		tessellator7.addVertexWithUV(f2 - 0.0625F + f5, f3, f17 + f6, f9, block2);
		tessellator7.addVertexWithUV(f2 - 0.0625F + f5, f3, f18 + f6, f21, block2);
		tessellator7.addVertexWithUV(f2 - 0.0625F, f3 + 1.0F, f18, f21, f10);
		tessellator7.addVertexWithUV(f2 + 0.0625F, f3 + 1.0F, f18, f9, f10);
		tessellator7.addVertexWithUV(f2 + f5 + 0.0625F, f3, f18 + f6, f9, block2);
		tessellator7.addVertexWithUV(f2 + f5 + 0.0625F, f3, f17 + f6, f21, block2);
		tessellator7.addVertexWithUV(f2 + 0.0625F, f3 + 1.0F, f17, f21, f10);
		tessellator7.addVertexWithUV(f15, f3 + 1.0F, f4 + 0.0625F, f9, f10);
		tessellator7.addVertexWithUV(f15 + f5, f3, f4 + 0.0625F + f6, f9, block2);
		tessellator7.addVertexWithUV(f16 + f5, f3, f4 + 0.0625F + f6, f21, block2);
		tessellator7.addVertexWithUV(f16, f3 + 1.0F, f4 + 0.0625F, f21, f10);
		tessellator7.addVertexWithUV(f16, f3 + 1.0F, f4 - 0.0625F, f9, f10);
		tessellator7.addVertexWithUV(f16 + f5, f3, f4 - 0.0625F + f6, f9, block2);
		tessellator7.addVertexWithUV(f15 + f5, f3, f4 - 0.0625F + f6, f21, block2);
		tessellator7.addVertexWithUV(f15, f3 + 1.0F, f4 - 0.0625F, f21, f10);
	}

	private void renderBlockPlant(Block block, int i2, float f3, float f4, float f5) {
		Tessellator tessellator6 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		i2 = (block1 & 15) << 4;
		block1 &= 240;
		float f7 = (float)i2 / 256.0F;
		float f12 = ((float)i2 + 15.99F) / 256.0F;
		float f8 = (float)block1 / 256.0F;
		float block2 = ((float)block1 + 15.99F) / 256.0F;
		float f9 = f3 + 0.5F - 0.45F;
		f3 = f3 + 0.5F + 0.45F;
		float f10 = f5 + 0.5F - 0.45F;
		f5 = f5 + 0.5F + 0.45F;
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f10, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f10, f7, block2);
		tessellator6.addVertexWithUV(f3, f4, f5, f12, block2);
		tessellator6.addVertexWithUV(f3, f4 + 1.0F, f5, f12, f8);
		tessellator6.addVertexWithUV(f3, f4 + 1.0F, f5, f7, f8);
		tessellator6.addVertexWithUV(f3, f4, f5, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f10, f12, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f10, f12, f8);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f5, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f5, f7, block2);
		tessellator6.addVertexWithUV(f3, f4, f10, f12, block2);
		tessellator6.addVertexWithUV(f3, f4 + 1.0F, f10, f12, f8);
		tessellator6.addVertexWithUV(f3, f4 + 1.0F, f10, f7, f8);
		tessellator6.addVertexWithUV(f3, f4, f10, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f5, f12, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f5, f12, f8);
	}

	private void renderBlockCrops(Block block, int i2, float f3, float f4, float f5) {
		Tessellator tessellator6 = Tessellator.instance;
		int block1 = block.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			block1 = this.overrideBlockTexture;
		}

		i2 = (block1 & 15) << 4;
		block1 &= 240;
		float f7 = (float)i2 / 256.0F;
		float f14 = ((float)i2 + 15.99F) / 256.0F;
		float f8 = (float)block1 / 256.0F;
		float block2 = ((float)block1 + 15.99F) / 256.0F;
		float f9 = f3 + 0.5F - 0.25F;
		float f10 = f3 + 0.5F + 0.25F;
		float f11 = f5 + 0.5F - 0.5F;
		float f12 = f5 + 0.5F + 0.5F;
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f11, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f11, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f12, f14, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f12, f14, f8);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f12, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f12, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f11, f14, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f11, f14, f8);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f12, f7, f8);
		tessellator6.addVertexWithUV(f10, f4, f12, f7, block2);
		tessellator6.addVertexWithUV(f10, f4, f11, f14, block2);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f11, f14, f8);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f11, f7, f8);
		tessellator6.addVertexWithUV(f10, f4, f11, f7, block2);
		tessellator6.addVertexWithUV(f10, f4, f12, f14, block2);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f12, f14, f8);
		f9 = f3 + 0.5F - 0.5F;
		f10 = f3 + 0.5F + 0.5F;
		f11 = f5 + 0.5F - 0.25F;
		f12 = f5 + 0.5F + 0.25F;
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f11, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f11, f7, block2);
		tessellator6.addVertexWithUV(f10, f4, f11, f14, block2);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f11, f14, f8);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f11, f7, f8);
		tessellator6.addVertexWithUV(f10, f4, f11, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f11, f14, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f11, f14, f8);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f12, f7, f8);
		tessellator6.addVertexWithUV(f10, f4, f12, f7, block2);
		tessellator6.addVertexWithUV(f9, f4, f12, f14, block2);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f12, f14, f8);
		tessellator6.addVertexWithUV(f9, f4 + 1.0F, f12, f7, f8);
		tessellator6.addVertexWithUV(f9, f4, f12, f7, block2);
		tessellator6.addVertexWithUV(f10, f4, f12, f14, block2);
		tessellator6.addVertexWithUV(f10, f4 + 1.0F, f12, f14, f8);
	}

	private float materialNotWater(int i1, int i2, int i3) {
		return this.blockAccess.getBlockMaterial(i1, i2, i3) != Material.water ? 1.0F : (float)this.blockAccess.getBlockMetadata(i1, i2, i3) / 9.0F;
	}

	private void renderBlockBottom(Block block, float f2, float f3, float f4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f14 = ((float)i7 + 15.99F) / 256.0F;
		float f9 = (float)i5 / 256.0F;
		float f13 = ((float)i5 + 15.99F) / 256.0F;
		float f10 = f2 + block.minX;
		f2 += block.maxX;
		f3 += block.minY;
		float f11 = f4 + block.minZ;
		float block1 = f4 + block.maxZ;
		tessellator6.addVertexWithUV(f10, f3, block1, f8, f13);
		tessellator6.addVertexWithUV(f10, f3, f11, f8, f9);
		tessellator6.addVertexWithUV(f2, f3, f11, f14, f9);
		tessellator6.addVertexWithUV(f2, f3, block1, f14, f13);
	}

	private void renderBlockTop(Block block, float f2, float f3, float f4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f14 = ((float)i7 + 15.99F) / 256.0F;
		float f9 = (float)i5 / 256.0F;
		float f13 = ((float)i5 + 15.99F) / 256.0F;
		float f10 = f2 + block.minX;
		f2 += block.maxX;
		f3 += block.maxY;
		float f11 = f4 + block.minZ;
		float block1 = f4 + block.maxZ;
		tessellator6.addVertexWithUV(f2, f3, block1, f14, f13);
		tessellator6.addVertexWithUV(f2, f3, f11, f14, f9);
		tessellator6.addVertexWithUV(f10, f3, f11, f8, f9);
		tessellator6.addVertexWithUV(f10, f3, block1, f8, f13);
	}

	private void renderBlockNorth(Block block, int i2, int i3, int i4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f16 = ((float)i7 + 15.99F) / 256.0F;
		float f9;
		float f15;
		if(block.minY >= 0.0F && block.maxY <= 1.0F) {
			f9 = ((float)i5 + block.minY * 15.99F) / 256.0F;
			f15 = ((float)i5 + block.maxY * 15.99F) / 256.0F;
		} else {
			f9 = (float)i5 / 256.0F;
			f15 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f10 = (float)i2 + block.minX;
		float f13 = (float)i2 + block.maxX;
		float f11 = (float)i3 + block.minY;
		float f14 = (float)i3 + block.maxY;
		float block1 = (float)i4 + block.minZ;
		tessellator6.addVertexWithUV(f10, f14, block1, f16, f9);
		tessellator6.addVertexWithUV(f13, f14, block1, f8, f9);
		tessellator6.addVertexWithUV(f13, f11, block1, f8, f15);
		tessellator6.addVertexWithUV(f10, f11, block1, f16, f15);
	}

	private void renderBlockSouth(Block block, int i2, int i3, int i4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f16 = ((float)i7 + 15.99F) / 256.0F;
		float f9;
		float f15;
		if(block.minY >= 0.0F && block.maxY <= 1.0F) {
			f9 = ((float)i5 + block.minY * 15.99F) / 256.0F;
			f15 = ((float)i5 + block.maxY * 15.99F) / 256.0F;
		} else {
			f9 = (float)i5 / 256.0F;
			f15 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f10 = (float)i2 + block.minX;
		float f13 = (float)i2 + block.maxX;
		float f11 = (float)i3 + block.minY;
		float f14 = (float)i3 + block.maxY;
		float block1 = (float)i4 + block.maxZ;
		tessellator6.addVertexWithUV(f10, f14, block1, f8, f9);
		tessellator6.addVertexWithUV(f10, f11, block1, f8, f15);
		tessellator6.addVertexWithUV(f13, f11, block1, f16, f15);
		tessellator6.addVertexWithUV(f13, f14, block1, f16, f9);
	}

	private void renderBlockWest(Block block, int i2, int i3, int i4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f16 = ((float)i7 + 15.99F) / 256.0F;
		float f9;
		float f15;
		if(block.minY >= 0.0F && block.maxY <= 1.0F) {
			f9 = ((float)i5 + block.minY * 15.99F) / 256.0F;
			f15 = ((float)i5 + block.maxY * 15.99F) / 256.0F;
		} else {
			f9 = (float)i5 / 256.0F;
			f15 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f13 = (float)i2 + block.minX;
		float f10 = (float)i3 + block.minY;
		float f14 = (float)i3 + block.maxY;
		float f11 = (float)i4 + block.minZ;
		float block1 = (float)i4 + block.maxZ;
		tessellator6.addVertexWithUV(f13, f14, block1, f16, f9);
		tessellator6.addVertexWithUV(f13, f14, f11, f8, f9);
		tessellator6.addVertexWithUV(f13, f10, f11, f8, f15);
		tessellator6.addVertexWithUV(f13, f10, block1, f16, f15);
	}

	private void renderBlockEast(Block block, int i2, int i3, int i4, int i5) {
		Tessellator tessellator6 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i7 = (i5 & 15) << 4;
		i5 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f16 = ((float)i7 + 15.99F) / 256.0F;
		float f9;
		float f15;
		if(block.minY >= 0.0F && block.maxY <= 1.0F) {
			f9 = ((float)i5 + block.minY * 15.99F) / 256.0F;
			f15 = ((float)i5 + block.maxY * 15.99F) / 256.0F;
		} else {
			f9 = (float)i5 / 256.0F;
			f15 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f13 = (float)i2 + block.maxX;
		float f10 = (float)i3 + block.minY;
		float f14 = (float)i3 + block.maxY;
		float f11 = (float)i4 + block.minZ;
		float block1 = (float)i4 + block.maxZ;
		tessellator6.addVertexWithUV(f13, f10, block1, f8, f15);
		tessellator6.addVertexWithUV(f13, f10, f11, f16, f15);
		tessellator6.addVertexWithUV(f13, f14, f11, f16, f9);
		tessellator6.addVertexWithUV(f13, f14, block1, f8, f9);
	}

	public final void renderBlockOnInventory(Block block) {
		Tessellator tessellator2 = Tessellator.instance;
		int i3;
		if((i3 = block.getRenderType()) == 0) {
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockBottom(block, 0.0F, 0.0F, 0.0F, block.getBlockTextureFromSide(0));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderBlockTop(block, 0.0F, 0.0F, 0.0F, block.getBlockTextureFromSide(1));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderBlockNorth(block, 0, 0, 0, block.getBlockTextureFromSide(2));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderBlockSouth(block, 0, 0, 0, block.getBlockTextureFromSide(3));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderBlockWest(block, 0, 0, 0, block.getBlockTextureFromSide(4));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(1.0F, 0.0F, 0.0F);
			this.renderBlockEast(block, 0, 0, 0, block.getBlockTextureFromSide(5));
			tessellator2.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(i3 == 1) {
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockPlant(block, -1, -0.5F, -0.5F, -0.5F);
			tessellator2.draw();
		} else if(i3 == 6) {
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockCrops(block, -1, -0.5F, -0.5F, -0.5F);
			tessellator2.draw();
		} else {
			if(i3 == 2) {
				tessellator2.startDrawingQuads();
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockTorch(block, -0.5F, -0.5F, -0.5F, 0.0F, 0.0F);
				tessellator2.draw();
			}

		}
	}
}