package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.animal.EntitySheep;

public final class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase modelBase, ModelBase modelBase2, float f3) {
		super(modelBase, 0.7F);
		this.setRenderPassModel(modelBase2);
	}

	protected final boolean shouldRenderPass(EntityLiving entityLiving, int i2) {
		EntitySheep entitySheep10001 = (EntitySheep)entityLiving;
		int i3 = i2;
		EntitySheep entitySheep4 = entitySheep10001;
		this.loadTexture("/mob/sheep_fur.png");
		return i3 == 0 && !entitySheep4.sheared;
	}
}