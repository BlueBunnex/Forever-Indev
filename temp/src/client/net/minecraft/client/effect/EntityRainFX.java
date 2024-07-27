package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World world, float f2, float f3, float f4) {
		super(world, f2, f3, f4, 0.0F, 0.0F, 0.0F);
		this.motionX1 *= 0.3F;
		this.motionY1 = (float)Math.random() * 0.2F + 0.1F;
		this.motionZ1 *= 0.3F;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 16;
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		super.renderParticle(tessellator, f2, f3, f4, f5, f6, f7);
	}

	public final void onEntityUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY1 -= this.particleGravity;
		this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
		this.motionX1 *= 0.98F;
		this.motionY1 *= 0.98F;
		this.motionZ1 *= 0.98F;
		if(this.particleMaxAge-- <= 0) {
			this.setEntityDead();
		}

		if(this.onGround) {
			if(Math.random() < 0.5D) {
				this.setEntityDead();
			}

			this.motionX1 *= 0.7F;
			this.motionZ1 *= 0.7F;
		}

		Material material1;
		if((material1 = this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ)).getIsLiquid() || material1.isSolid()) {
			this.setEntityDead();
		}

	}
}