package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public abstract class Render {
	protected RenderManager renderManager;
	protected float shadowSize;
	protected float shadowOpaque;

	public Render() {
		new ModelBiped();
		new RenderBlocks();
		this.shadowSize = 0.0F;
		this.shadowOpaque = 1.0F;
	}

	public abstract void doRender(Entity entity1, float f2, float f3, float f4, float f5, float f6);

	protected final void loadTexture(String string1) {
		RenderEngine renderEngine2 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(this.renderManager.renderEngine.getTexture(string1));
	}

	protected final void loadDownloadableImageTexture(String string1, String string2) {
		RenderEngine renderEngine3 = this.renderManager.renderEngine;
		RenderEngine.bindTexture(this.renderManager.renderEngine.getTextureForDownloadableImage(string1, string2));
	}

	public static void renderOffsetAABB(AxisAlignedBB aabb) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator1 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator1.startDrawingQuads();
		Tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		Tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		Tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		tessellator1.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		tessellator1.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		Tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		tessellator1.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		tessellator1.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		tessellator1.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public final void setRenderManager(RenderManager renderManager) {
		this.renderManager = renderManager;
	}

	public final void renderShadow(Entity entity, float f2, float f3, float f4, float f5) {
		float f12;
		float f18;
		float f19;
		int i29;
		float f34;
		float f36;
		if(this.shadowSize > 0.0F) {
			f5 = this.renderManager.getDistanceToCamera(f2, f3, f4);
			if((f5 = (1.0F - f5 / 256.0F) * this.shadowOpaque) > 0.0F) {
				float f9 = f5;
				float f8 = f4;
				float f7 = f3;
				float f6 = f2;
				Render render27 = this;
				GL11.glEnable(GL11.GL_BLEND);
				RenderEngine renderEngine10 = this.renderManager.renderEngine;
				RenderEngine.bindTexture(this.renderManager.renderEngine.getTexture("%%/shadow.png"));
				World world11 = this.renderManager.worldObj;
				GL11.glDepthMask(false);
				f12 = this.shadowSize;

				for(i29 = (int)(f2 - f12); i29 <= (int)(f6 + f12); ++i29) {
					for(int i13 = (int)(f7 - 2.0F); i13 <= (int)f7; ++i13) {
						for(int i14 = (int)(f8 - f12); i14 <= (int)(f8 + f12); ++i14) {
							int i15;
							if((i15 = world11.getBlockId(i29, i13 - 1, i14)) > 0 && world11.getBlockLightValue(i29, i13, i14) > 3) {
								Block block16 = Block.blocksList[i15];
								Tessellator tessellator25 = Tessellator.instance;
								if((f34 = (f9 - (f7 - (float)i13) / 2.0F) * 0.5F * render27.renderManager.worldObj.getLightBrightness(i29, i13, i14)) >= 0.0F) {
									GL11.glColor4f(1.0F, 1.0F, 1.0F, f34);
									tessellator25.startDrawingQuads();
									f34 = (float)i29 + block16.minX;
									f18 = (float)i29 + block16.maxX;
									float f20 = (float)i13 + block16.minY;
									float f21 = (float)i14 + block16.minZ;
									f36 = (float)i14 + block16.maxZ;
									float f22 = (f6 - f34) / 2.0F / f12 + 0.5F;
									float f17 = (f6 - f18) / 2.0F / f12 + 0.5F;
									float f23 = (f8 - f21) / 2.0F / f12 + 0.5F;
									f19 = (f8 - f36) / 2.0F / f12 + 0.5F;
									tessellator25.addVertexWithUV(f34, f20, f21, f22, f23);
									tessellator25.addVertexWithUV(f34, f20, f36, f22, f19);
									tessellator25.addVertexWithUV(f18, f20, f36, f17, f19);
									tessellator25.addVertexWithUV(f18, f20, f21, f17, f23);
									tessellator25.draw();
								}
							}
						}
					}
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
			}
		}

		if(entity.fire > 0) {
			GL11.glDisable(GL11.GL_LIGHTING);
			int i26 = Block.fire.blockIndexInTexture;
			i29 = (Block.fire.blockIndexInTexture & 15) << 4;
			int i30 = i26 & 240;
			f12 = (float)i29 / 256.0F;
			float f31 = ((float)i29 + 15.99F) / 256.0F;
			float f32 = (float)i30 / 256.0F;
			float f33 = ((float)i30 + 15.99F) / 256.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(f2, f3, f4);
			GL11.glScalef(f34 = entity.width * 1.4F, f34, f34);
			this.loadTexture("/terrain.png");
			Tessellator tessellator35 = Tessellator.instance;
			f36 = 1.0F;
			f18 = 0.0F;
			f19 = entity.height / entity.width;
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, 0.4F + (float)((int)f19) * 0.02F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tessellator35.startDrawingQuads();

			while(f19 > 0.0F) {
				tessellator35.addVertexWithUV(f36 - 0.5F, 0.0F - f18, 0.0F, f31, f33);
				tessellator35.addVertexWithUV(-0.5F, 0.0F - f18, 0.0F, f12, f33);
				tessellator35.addVertexWithUV(-0.5F, 1.4F - f18, 0.0F, f12, f32);
				tessellator35.addVertexWithUV(f36 - 0.5F, 1.4F - f18, 0.0F, f31, f32);
				--f19;
				--f18;
				f36 *= 0.9F;
				GL11.glTranslatef(0.0F, 0.0F, -0.04F);
			}

			tessellator35.draw();
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}

	}
}