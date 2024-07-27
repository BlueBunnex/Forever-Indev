package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityExplodeFX extends EntityFX {
	public EntityExplodeFX(World world1, float f2, float f3, float f4, float f5, float f6, float f7) {
		super(world1, f2, f3, f4, f5, f6, f7);
		this.motionX1 = f5 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.motionY1 = f6 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.motionZ1 = f7 + (float)(Math.random() * 2.0D - 1.0D) * 0.05F;
		this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F + 0.7F;
		this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
		this.particleMaxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
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
		this.motionX1 *= 0.9F;
		this.motionY1 *= 0.9F;
		this.motionZ1 *= 0.9F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}
}