package net.minecraft.client.effect;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class EntityBubbleFX extends EntityFX {
	public EntityBubbleFX(World world1, float f2, float f3, float f4, float f5, float f6, float f7) {
		super(world1, f2, f3, f4, f5, f6, f7);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 32;
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.motionX1 = f5 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.motionY1 = f6 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.motionZ1 = f7 * 0.2F + (float)(Math.random() * 2.0D - 1.0D) * 0.02F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY1 = (float)((double)this.motionY1 + 0.002D);
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.85F;
		this.motionY1 *= 0.85F;
		this.motionZ1 *= 0.85F;
		if(this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ) != Material.water) {
			this.setEntityDead();
		}

		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

	}
}