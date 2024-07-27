package net.minecraft.client.render.entity;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderArrow extends Render {
	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		EntityArrow entityArrow8 = (EntityArrow)entity;
		this.loadTexture("/item/arrows.png");
		GL11.glPushMatrix();
		GL11.glTranslatef(f2, f3, f4);
		GL11.glRotatef(entityArrow8.prevRotationYaw + (entityArrow8.rotationYaw - entityArrow8.prevRotationYaw) * f6 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityArrow8.prevRotationPitch + (entityArrow8.rotationPitch - entityArrow8.prevRotationPitch) * f6, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator7 = Tessellator.instance;
		GL11.glEnable(GL11.GL_NORMALIZE);
		if((f2 = (float)entityArrow8.arrowShake - f6) > 0.0F) {
			GL11.glRotatef(-MathHelper.sin(f2 * 3.0F) * f2, 0.0F, 0.0F, 1.0F);
		}

		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(0.05625F, 0.05625F, 0.05625F);
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(0.05625F, 0.0F, 0.0F);
		tessellator7.startDrawingQuads();
		tessellator7.addVertexWithUV(-7.0F, -2.0F, -2.0F, 0.0F, 0.15625F);
		tessellator7.addVertexWithUV(-7.0F, -2.0F, 2.0F, 0.15625F, 0.15625F);
		tessellator7.addVertexWithUV(-7.0F, 2.0F, 2.0F, 0.15625F, 0.3125F);
		tessellator7.addVertexWithUV(-7.0F, 2.0F, -2.0F, 0.0F, 0.3125F);
		tessellator7.draw();
		GL11.glNormal3f(-0.05625F, 0.0F, 0.0F);
		tessellator7.startDrawingQuads();
		tessellator7.addVertexWithUV(-7.0F, 2.0F, -2.0F, 0.0F, 0.15625F);
		tessellator7.addVertexWithUV(-7.0F, 2.0F, 2.0F, 0.15625F, 0.15625F);
		tessellator7.addVertexWithUV(-7.0F, -2.0F, 2.0F, 0.15625F, 0.3125F);
		tessellator7.addVertexWithUV(-7.0F, -2.0F, -2.0F, 0.0F, 0.3125F);
		tessellator7.draw();

		for(int i9 = 0; i9 < 4; ++i9) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, 0.05625F);
			tessellator7.startDrawingQuads();
			tessellator7.addVertexWithUV(-8.0F, -2.0F, 0.0F, 0.0F, 0.0F);
			tessellator7.addVertexWithUV(8.0F, -2.0F, 0.0F, 0.5F, 0.0F);
			tessellator7.addVertexWithUV(8.0F, 2.0F, 0.0F, 0.5F, 0.15625F);
			tessellator7.addVertexWithUV(-8.0F, 2.0F, 0.0F, 0.0F, 0.15625F);
			tessellator7.draw();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}