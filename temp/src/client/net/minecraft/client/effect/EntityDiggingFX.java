package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class EntityDiggingFX extends EntityFX {
	public EntityDiggingFX(World world, float f2, float f3, float f4, float f5, float f6, float f7, Block block8) {
		super(world, f2, f3, f4, f5, f6, f7);
		this.particleTextureIndex = block8.blockIndexInTexture;
		this.particleGravity = block8.blockParticleGravity;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
	}

	public final int getFXLayer() {
		return 1;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8;
		float f9 = (f8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F) + 0.015609375F;
		float f10;
		float f11 = (f10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F) + 0.015609375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = this.prevPosX + (this.posX - this.prevPosX) * f2;
		float f14 = this.prevPosY + (this.posY - this.prevPosY) * f2;
		float f15 = this.prevPosZ + (this.posZ - this.prevPosZ) * f2;
		f2 = this.getEntityBrightness(f2);
		tessellator.setColorOpaque_F(f2 * this.particleRed, f2 * this.particleGreen, f2 * this.particleBlue);
		tessellator.addVertexWithUV(f13 - f3 * f12 - f6 * f12, f14 - f4 * f12, f15 - f5 * f12 - f7 * f12, f8, f11);
		tessellator.addVertexWithUV(f13 - f3 * f12 + f6 * f12, f14 + f4 * f12, f15 - f5 * f12 + f7 * f12, f8, f10);
		tessellator.addVertexWithUV(f13 + f3 * f12 + f6 * f12, f14 + f4 * f12, f15 + f5 * f12 + f7 * f12, f9, f10);
		tessellator.addVertexWithUV(f13 + f3 * f12 - f6 * f12, f14 - f4 * f12, f15 + f5 * f12 - f7 * f12, f9, f11);
	}
}