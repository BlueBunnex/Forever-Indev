package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;

import org.lwjgl.opengl.GL11;

public final class RenderGiantZombie extends RenderLiving {
	private float scale = 6.0F;

	public RenderGiantZombie(ModelBase modelBase, float f2, float f3) {
		super(modelBase, 3.0F);
	}

	protected final void preRenderCallback(EntityLiving entityLiving, float f2) {
		GL11.glScalef(this.scale, this.scale, this.scale);
	}
}