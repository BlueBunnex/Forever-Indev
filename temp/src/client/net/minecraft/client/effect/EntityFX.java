package net.minecraft.client.effect;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;

import util.MathHelper;

public class EntityFX extends Entity {
	protected float motionX1;
	protected float motionY1;
	protected float motionZ1;
	protected int particleTextureIndex;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge = 0;
	protected int particleMaxAge = 0;
	protected float particleScale;
	protected float particleGravity;
	protected float particleRed;
	protected float particleGreen;
	protected float particleBlue;

	public EntityFX(World world, float f2, float f3, float f4, float f5, float f6, float f7) {
		super(world);
		this.setSize(0.2F, 0.2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(f2, f3, f4);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.motionX1 = f5 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.motionY1 = f6 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.motionZ1 = f7 + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		float world1 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		f2 = MathHelper.sqrt_float(this.motionX1 * this.motionX1 + this.motionY1 * this.motionY1 + this.motionZ1 * this.motionZ1);
		this.motionX1 = this.motionX1 / f2 * world1 * 0.4F;
		this.motionY1 = this.motionY1 / f2 * world1 * 0.4F + 0.1F;
		this.motionZ1 = this.motionZ1 / f2 * world1 * 0.4F;
		this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
		this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
		this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
		this.particleAge = 0;
		this.canTriggerWalking = false;
	}

	public final EntityFX multiplyVelocity(float f1) {
		this.motionX1 *= 0.2F;
		this.motionY1 = (this.motionY1 - 0.1F) * 0.2F + 0.1F;
		this.motionZ1 *= 0.2F;
		return this;
	}

	public final EntityFX multipleParticleScaleBy(float f1) {
		this.setSize(0.120000005F, 0.120000005F);
		this.particleScale *= 0.6F;
		return this;
	}

	public void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.motionY1 = (float)((double)this.motionY1 - 0.04D * (double)this.particleGravity);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.98F;
		this.motionY1 *= 0.98F;
		this.motionZ1 *= 0.98F;
		if(this.onGround) {
			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

	}

	public void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8;
		float f9 = (f8 = (float)(this.particleTextureIndex % 16) / 16.0F) + 0.0624375F;
		float f10;
		float f11 = (f10 = (float)(this.particleTextureIndex / 16) / 16.0F) + 0.0624375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = this.prevPosX + (this.posX - this.prevPosX) * f2;
		float f14 = this.prevPosY + (this.posY - this.prevPosY) * f2;
		float f15 = this.prevPosZ + (this.posZ - this.prevPosZ) * f2;
		f2 = this.getEntityBrightness(f2);
		tessellator.setColorOpaque_F(this.particleRed * f2, this.particleGreen * f2, this.particleBlue * f2);
		tessellator.addVertexWithUV(f13 - f3 * f12 - f6 * f12, f14 - f4 * f12, f15 - f5 * f12 - f7 * f12, f8, f11);
		tessellator.addVertexWithUV(f13 - f3 * f12 + f6 * f12, f14 + f4 * f12, f15 - f5 * f12 + f7 * f12, f8, f10);
		tessellator.addVertexWithUV(f13 + f3 * f12 + f6 * f12, f14 + f4 * f12, f15 + f5 * f12 + f7 * f12, f9, f10);
		tessellator.addVertexWithUV(f13 + f3 * f12 - f6 * f12, f14 - f4 * f12, f15 + f5 * f12 - f7 * f12, f9, f11);
	}

	public int getFXLayer() {
		return 0;
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
	}

	protected final String getEntityString() {
		return null;
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
	}
}