package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntitySmokeFX extends EntityFX {
	private float smokeParticleScale;

	public EntitySmokeFX(World world, float f2, float f3, float f4) {
		this(world, f2, f3, f4, 1.0F);
	}

	public EntitySmokeFX(World world, float f2, float f3, float f4, float f5) {
		super(world, f2, f3, f4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.1F;
		this.motionY1 *= 0.1F;
		this.motionZ1 *= 0.1F;
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * (double)0.3F);
		this.particleScale *= 0.75F;
		this.particleScale *= f5;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * f5);
		this.noClip = false;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8;
		if((f8 = ((float)this.particleAge + f2) / (float)this.particleMaxAge * 32.0F) < 0.0F) {
			f8 = 0.0F;
		}

		if(f8 > 1.0F) {
			f8 = 1.0F;
		}

		this.particleScale = this.smokeParticleScale * f8;
		super.renderParticle(tessellator, f2, f3, f4, f5, f6, f7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.particleTextureIndex = 7 - (this.particleAge << 3) / this.particleMaxAge;
		this.motionY1 = (float)((double)this.motionY1 + 0.004D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		if(this.posY == this.prevPosY) {
			this.motionX1 = (float)((double)this.motionX1 * 1.1D);
			this.motionZ1 = (float)((double)this.motionZ1 * 1.1D);
		}

		this.motionX1 *= 0.96F;
		this.motionY1 *= 0.96F;
		this.motionZ1 *= 0.96F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}