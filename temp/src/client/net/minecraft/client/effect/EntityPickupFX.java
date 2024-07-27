package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

import org.lwjgl.opengl.GL11;

public final class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private EntityLiving entityPickingUp;
	private int age = 0;
	private int maxAge = 0;
	private float yOffs;

	public EntityPickupFX(World world, Entity entity2, EntityLiving entityLiving3, float f4) {
		super(world, entity2.posX, entity2.posY, entity2.posZ, entity2.motionX, entity2.motionY, entity2.motionZ);
		this.entityToPickUp = entity2;
		this.entityPickingUp = entityLiving3;
		this.maxAge = 3;
		this.yOffs = -0.5F;
	}

	public final void renderParticle(Tessellator tessellator, float f2, float f3, float f4, float f5, float f6, float f7) {
		float tessellator1 = (tessellator1 = ((float)this.age + f2) / (float)this.maxAge) * tessellator1;
		f3 = this.entityToPickUp.posX;
		f4 = this.entityToPickUp.posY;
		f5 = this.entityToPickUp.posZ;
		f6 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * f2;
		f7 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * f2 + this.yOffs;
		float f8 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * f2;
		f3 += (f6 - f3) * tessellator1;
		f4 += (f7 - f4) * tessellator1;
		tessellator1 = f5 + (f8 - f5) * tessellator1;
		GL11.glColor4f(f5 = this.worldObj.getLightBrightness((int)f3, (int)f4, (int)tessellator1), f5, f5, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, f3, f4, tessellator1, this.entityToPickUp.rotationYaw, f2);
	}

	public final void onEntityUpdate() {
		++this.age;
		if(this.age == this.maxAge) {
			this.setEntityDead();
		}

	}

	public final int getFXLayer() {
		return 2;
	}
}