package net.minecraft.client.render.entity;

import net.minecraft.game.entity.Entity;

import org.lwjgl.opengl.GL11;

public final class RenderEntity extends Render {
	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		GL11.glPushMatrix();
		GL11.glTranslatef(f2 - entity.lastTickPosX, f3 - entity.lastTickPosY, f4 - entity.lastTickPosZ);
		renderOffsetAABB(entity.boundingBox);
		GL11.glPopMatrix();
	}
}