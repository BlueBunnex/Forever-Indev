package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityLavaFX extends EntityFX {
	private float lavaParticleScale;

	public EntityLavaFX(World world, float f2, float f3, float f4) {
		super(world, f2, f3, f4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.8F;
		this.motionY1 *= 0.8F;
		this.motionZ1 *= 0.8F;
		this.motionY1 = this.rand.nextFloat() * 0.4F + 0.05F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
		this.lavaParticleScale = this.particleScale;
		this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
		this.noClip = false;
		this.particleTextureIndex = 49;
	}

	public final float getEntityBrightness(float f1) {
		return 1.0F;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = ((float)this.particleAge + f2) / (float)this.particleMaxAge;
		this.particleScale = this.lavaParticleScale * (1.0F - f8 * f8);
		super.renderParticle(tessellator, f2, f3, f4, f5, f6, f7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		float f1 = (float)this.particleAge / (float)this.particleMaxAge;
		if(this.rand.nextFloat() > f1) {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, this.motionX1, this.motionY1, this.motionZ1);
		}

		this.motionY1 = (float)((double)this.motionY1 - 0.03D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.999F;
		this.motionY1 *= 0.999F;
		this.motionZ1 *= 0.999F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}