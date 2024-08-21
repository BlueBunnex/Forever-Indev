package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.World;

public final class EntityFluffyCloudFX extends EntityFX {
    private float fluffyCloudScale;

    // New fields for color
    private float particleRed;
    private float particleGreen;
    private float particleBlue;

    public EntityFluffyCloudFX(World world, float x, float y, float z) {
        this(world, x, y, z, 1.0F);
    }

    public EntityFluffyCloudFX(World world, float x, float y, float z, float scale) {
        super(world, x, y, z, 0.0F, 0.0F, 0.0F);
        this.motionX1 *= 0.1F;
        this.motionY1 *= 0.1F;
        this.motionZ1 *= 0.1F;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.3F);
        this.particleScale *= 12.0F / 16.0F;
        this.particleScale *= scale;
        this.fluffyCloudScale = this.particleScale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
        this.noClip = false;
    }

    // New method to set color
    public void setColor(float red, float green, float blue) {
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
    }

    public final void renderParticle(Tessellator tessellator, float partialTicks, float x, float y, float z, float r, float g, float b) {
        float ageFactor = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
        if (ageFactor < 0.0F) {
            ageFactor = 0.0F;
        }
        if (ageFactor > 1.0F) {
            ageFactor = 1.0F;
        }
        this.particleScale = this.fluffyCloudScale * ageFactor;
        super.renderParticle(tessellator, partialTicks, x, y, z, r, g);
    }

    @Override
    public final void onEntityUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }
        this.particleTextureIndex = 7 - (this.particleAge << 3) / this.particleMaxAge;
        this.motionY1 = (float) ((double) this.motionY1 + 0.004D);
        this.moveEntity(this.motionX1, this.motionY1, this.motionZ1);
        if (this.posY == this.prevPosY) {
            this.motionX1 = (float) ((double) this.motionX1 * 1.1D);
            this.motionZ1 = (float) ((double) this.motionZ1 * 1.1D);
        }
        this.motionX1 *= 0.96F;
        this.motionY1 *= 0.96F;
        this.motionZ1 *= 0.96F;
        if (this.onGround) {
            this.motionX1 *= 0.7F;
            this.motionZ1 *= 0.7F;
        }
    }
}
