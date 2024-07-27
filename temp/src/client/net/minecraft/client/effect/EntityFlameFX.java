package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityFlameFX extends EntityFX {
	private float flameScale;

	public EntityFlameFX(World world, float f2, float f3, float f4) {
		super(world, f2, f3, f4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.01F;
		this.motionY1 *= 0.01F;
		this.motionZ1 *= 0.01F;
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.rand.nextFloat();
		this.flameScale = this.particleScale;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.noClip = true;
		this.particleTextureIndex = 48;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = ((float)this.particleAge + f2) / (float)this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f8 * f8 * 0.5F);
		super.renderParticle(tessellator, f2, f3, f4, f5, f6, f7);
	}

	public final float getEntityBrightness(float f1) {
		float f2;
		if((f2 = ((float)this.particleAge + f1) / (float)this.particleMaxAge) < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		return super.getEntityBrightness(f1) * f2 + (1.0F - f2);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.96F;
		this.motionY1 *= 0.96F;
		this.motionZ1 *= 0.96F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}