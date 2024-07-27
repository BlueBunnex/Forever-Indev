package net.minecraft.client.render;

import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
	public boolean isInFrustrum = false;
	private boolean[] skipRenderPass = new boolean[2];
	private int posXPlus;
	private int posYPlus;
	private int posZPlus;
	public boolean needsUpdate;
	private AxisAlignedBB rendererBoundingBox;
	private RenderBlocks renderBlocks;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;

	public WorldRenderer(World world, int xCoord, int yCoord, int zCoord, int i5, int i6) {
		this.renderBlocks = new RenderBlocks(world);
		this.worldObj = world;
		this.posX = xCoord;
		this.posY = yCoord;
		this.posZ = zCoord;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		this.posXPlus = xCoord + this.sizeWidth / 2;
		this.posYPlus = yCoord + this.sizeHeight / 2;
		this.posZPlus = zCoord + this.sizeDepth / 2;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.rendererBoundingBox = (new AxisAlignedBB((float)xCoord, (float)yCoord, (float)zCoord, (float)(xCoord + this.sizeWidth), (float)(yCoord + this.sizeHeight), (float)(zCoord + this.sizeDepth))).expand(2.0F, 2.0F, 2.0F);
		this.glRenderList = i6;
		this.setDontDraw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glNewList(i6 + 2, GL11.GL_COMPILE);
		RenderItem.renderOffsetAABB(this.rendererBoundingBox);
		GL11.glEndList();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

			int i7;
			for(i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			for(i7 = 0; i7 < 2; ++i7) {
				boolean z8 = false;
				boolean z9 = false;
				tessellator.startDrawingQuads();
				GL11.glNewList(this.glRenderList + i7, GL11.GL_COMPILE);

				for(int i10 = i2; i10 < i5; ++i10) {
					for(int i11 = i3; i11 < i6; ++i11) {
						int i12 = (i10 * this.worldObj.length + i11) * this.worldObj.width + i1;

						for(int i13 = i1; i13 < i4; ++i13) {
							int i14;
							if((i14 = this.worldObj.blocks[i12++] & 255) > 0) {
								Block block15;
								if((block15 = Block.blocksList[i14]).getRenderBlockPass() != i7) {
									z8 = true;
								} else {
									z9 |= this.renderBlocks.renderBlockByRenderType(block15, i13, i10, i11);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glEndList();
				if(z9) {
					this.skipRenderPass[i7] = false;
				}

				if(!z8) {
					break;
				}
			}

		}
	}

	public final float distanceToEntitySquared(EntityLiving entityLiving1) {
		float f2 = entityLiving1.posX - (float)this.posXPlus;
		float f3 = entityLiving1.posY - (float)this.posYPlus;
		float f4 = entityLiving1.posZ - (float)this.posZPlus;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int[] i1, int i2, int i3) {
		if(!this.isInFrustrum) {
			return i2;
		} else {
			if(!this.skipRenderPass[i3]) {
				i1[i2++] = this.glRenderList + i3;
			}

			return i2;
		}
	}

	public final void updateInFrustrum(ICamera iCamera1) {
		this.isInFrustrum = iCamera1.isBoundingBoxInFrustrum(this.rendererBoundingBox);
	}

	public final void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}
}