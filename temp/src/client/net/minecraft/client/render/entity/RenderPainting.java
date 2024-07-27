package net.minecraft.client.render.entity;

import java.util.Random;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.EnumArt;

import org.lwjgl.opengl.GL11;

public final class RenderPainting extends Render {
	private Random rand = new Random();

	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		EntityPainting entityPainting22 = (EntityPainting)entity;
		this.rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef(f2, f3, f4);
		GL11.glRotatef(f5, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);
		this.loadTexture("/art/kz.png");
		EnumArt enumArt23 = entityPainting22.art;
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		int i27 = enumArt23.offsetY;
		int i26 = enumArt23.offsetX;
		int i25 = enumArt23.sizeY;
		int i24 = enumArt23.sizeX;
		entityPainting22 = entityPainting22;
		RenderPainting renderPainting21 = this;
		float f7 = (float)(-i24) / 2.0F;
		float f8 = (float)(-i25) / 2.0F;

		for(int i9 = 0; i9 < i24 / 16; ++i9) {
			for(int i10 = 0; i10 < i25 / 16; ++i10) {
				float f11 = f7 + (float)(i9 + 1 << 4);
				float f12 = f7 + (float)(i9 << 4);
				float f13 = f8 + (float)(i10 + 1 << 4);
				float f14 = f8 + (float)(i10 << 4);
				float f10002 = (f11 + f12) / 2.0F;
				float f18 = (f13 + f14) / 2.0F;
				float f17 = f10002;
				int i19 = (int)entityPainting22.posX;
				int i28 = (int)(entityPainting22.posY + f18 / 16.0F);
				int i20 = (int)entityPainting22.posZ;
				if(entityPainting22.direction == 0) {
					i19 = (int)(entityPainting22.posX + f17 / 16.0F);
				}

				if(entityPainting22.direction == 1) {
					i20 = (int)(entityPainting22.posZ - f17 / 16.0F);
				}

				if(entityPainting22.direction == 2) {
					i19 = (int)(entityPainting22.posX - f17 / 16.0F);
				}

				if(entityPainting22.direction == 3) {
					i20 = (int)(entityPainting22.posZ + f17 / 16.0F);
				}

				float f15;
				GL11.glColor3f(f15 = renderPainting21.renderManager.worldObj.getLightBrightness(i19, i28, i20), f15, f15);
				f15 = (float)(i26 + i24 - (i9 << 4)) / 256.0F;
				float f16 = (float)(i26 + i24 - (i9 + 1 << 4)) / 256.0F;
				f17 = (float)(i27 + i25 - (i10 << 4)) / 256.0F;
				f18 = (float)(i27 + i25 - (i10 + 1 << 4)) / 256.0F;
				Tessellator tessellator29 = Tessellator.instance;
				Tessellator.instance.startDrawingQuads();
				Tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator29.addVertexWithUV(f11, f14, -0.5F, f16, f17);
				tessellator29.addVertexWithUV(f12, f14, -0.5F, f15, f17);
				tessellator29.addVertexWithUV(f12, f13, -0.5F, f15, f18);
				tessellator29.addVertexWithUV(f11, f13, -0.5F, f16, f18);
				Tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator29.addVertexWithUV(f11, f13, 0.5F, 0.75F, 0.0F);
				tessellator29.addVertexWithUV(f12, f13, 0.5F, 0.8125F, 0.0F);
				tessellator29.addVertexWithUV(f12, f14, 0.5F, 0.8125F, 0.0625F);
				tessellator29.addVertexWithUV(f11, f14, 0.5F, 0.75F, 0.0625F);
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				tessellator29.addVertexWithUV(f11, f13, -0.5F, 0.75F, 0.001953125F);
				tessellator29.addVertexWithUV(f12, f13, -0.5F, 0.8125F, 0.001953125F);
				tessellator29.addVertexWithUV(f12, f13, 0.5F, 0.8125F, 0.001953125F);
				tessellator29.addVertexWithUV(f11, f13, 0.5F, 0.75F, 0.001953125F);
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator29.addVertexWithUV(f11, f14, 0.5F, 0.75F, 0.001953125F);
				tessellator29.addVertexWithUV(f12, f14, 0.5F, 0.8125F, 0.001953125F);
				tessellator29.addVertexWithUV(f12, f14, -0.5F, 0.8125F, 0.001953125F);
				tessellator29.addVertexWithUV(f11, f14, -0.5F, 0.75F, 0.001953125F);
				Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator29.addVertexWithUV(f11, f13, 0.5F, 0.7519531F, 0.0F);
				tessellator29.addVertexWithUV(f11, f14, 0.5F, 0.7519531F, 0.0625F);
				tessellator29.addVertexWithUV(f11, f14, -0.5F, 0.7519531F, 0.0625F);
				tessellator29.addVertexWithUV(f11, f13, -0.5F, 0.7519531F, 0.0F);
				Tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator29.addVertexWithUV(f12, f13, -0.5F, 0.7519531F, 0.0F);
				tessellator29.addVertexWithUV(f12, f14, -0.5F, 0.7519531F, 0.0625F);
				tessellator29.addVertexWithUV(f12, f14, 0.5F, 0.7519531F, 0.0625F);
				tessellator29.addVertexWithUV(f12, f13, 0.5F, 0.7519531F, 0.0F);
				tessellator29.draw();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}