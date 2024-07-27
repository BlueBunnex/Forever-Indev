package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntityCreeper;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderCreeper extends RenderLiving {
	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
	}

	protected final void preRenderCallback(EntityLiving entityLiving, float f2) {
		float entityLiving1 = ((EntityCreeper)entityLiving).c(f2);
		f2 = 1.0F + MathHelper.sin(entityLiving1 * 100.0F) * entityLiving1 * 0.01F;
		if(entityLiving1 < 0.0F) {
			entityLiving1 = 0.0F;
		}

		if(entityLiving1 > 1.0F) {
			entityLiving1 = 1.0F;
		}

		entityLiving1 = (entityLiving1 *= entityLiving1) * entityLiving1;
		float f3 = (1.0F + entityLiving1 * 0.4F) * f2;
		entityLiving1 = (1.0F + entityLiving1 * 0.1F) / f2;
		GL11.glScalef(f3, entityLiving1, f3);
	}

	protected final int getColorMultiplier(EntityLiving entityLiving, float f2, float f3) {
		float entityLiving1;
		if((int)((entityLiving1 = ((EntityCreeper)entityLiving).c(f3)) * 10.0F) % 2 == 0) {
			return 0;
		} else {
			int entityLiving2;
			if((entityLiving2 = (int)(entityLiving1 * 0.2F * 255.0F)) < 0) {
				entityLiving2 = 0;
			}

			if(entityLiving2 > 255) {
				entityLiving2 = 255;
			}

			return entityLiving2 << 24 | 16711680 | 65280 | 255;
		}
	}
}