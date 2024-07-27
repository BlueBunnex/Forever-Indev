package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.monster.EntitySpider;

import org.lwjgl.opengl.GL11;

public final class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected final float getDeathMaxRotation(EntityLiving entityLiving) {
		return 180.0F;
	}

	protected final boolean shouldRenderPass(EntityLiving entityLiving, int i2) {
		EntitySpider entitySpider10001 = (EntitySpider)entityLiving;
		int i3 = i2;
		EntitySpider entitySpider5 = entitySpider10001;
		if(i3 != 0) {
			return false;
		} else if(i3 != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float entityLiving1 = (1.0F - entitySpider5.getEntityBrightness(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, entityLiving1);
			return true;
		}
	}
}