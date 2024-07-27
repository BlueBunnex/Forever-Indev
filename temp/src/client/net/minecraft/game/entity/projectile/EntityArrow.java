package net.minecraft.game.entity.projectile;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import util.MathHelper;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inGround = false;
	public int arrowShake = 0;
	private EntityLiving owner;
	private int ticksInGround;
	private int ticksInAir = 0;

	public EntityArrow(World world, EntityLiving entityLiving) {
		super(world);
		this.owner = entityLiving;
		this.setSize(0.5F, 0.5F);
		this.setPositionAndRotation(entityLiving.posX, entityLiving.posY, entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.posY -= 0.1F;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI);
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public final void setArrowHeading(float f1, float f2, float f3, float f4, float f5) {
		float f6 = MathHelper.sqrt_float(f1 * f1 + f2 * f2 + f3 * f3);
		f1 /= f6;
		f2 /= f6;
		f3 /= f6;
		f1 = (float)((double)f1 + this.rand.nextGaussian() * (double)0.0075F * (double)f5);
		f2 = (float)((double)f2 + this.rand.nextGaussian() * (double)0.0075F * (double)f5);
		f3 = (float)((double)f3 + this.rand.nextGaussian() * (double)0.0075F * (double)f5);
		f1 *= f4;
		f2 *= f4;
		f3 *= f4;
		this.motionX = f1;
		this.motionY = f2;
		this.motionZ = f3;
		f4 = MathHelper.sqrt_float(f1 * f1 + f3 * f3);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2((double)f1, (double)f3) * 180.0D / (double)(float)Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2((double)f2, (double)f4) * 180.0D / (double)(float)Math.PI);
		this.ticksInGround = 0;
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			if(this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile) == this.inTile) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					this.setEntityDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= this.rand.nextFloat() * 0.2F;
			this.motionY *= this.rand.nextFloat() * 0.2F;
			this.motionZ *= this.rand.nextFloat() * 0.2F;
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3D vec3D1 = new Vec3D(this.posX, this.posY, this.posZ);
		Vec3D vec3D2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingObjectPosition3 = this.worldObj.rayTraceBlocks(vec3D1, vec3D2);
		vec3D1 = new Vec3D(this.posX, this.posY, this.posZ);
		vec3D2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(movingObjectPosition3 != null) {
			vec3D2 = new Vec3D(movingObjectPosition3.hitVec.xCoord, movingObjectPosition3.hitVec.yCoord, movingObjectPosition3.hitVec.zCoord);
		}

		Entity entity4 = null;
		List list5 = this.worldObj.entityMap.getEntitiesWithinAABB(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0F, 1.0F, 1.0F));
		float f6 = 0.0F;

		for(int i7 = 0; i7 < list5.size(); ++i7) {
			Entity entity8;
			MovingObjectPosition movingObjectPosition9;
			float f12;
			if((entity8 = (Entity)list5.get(i7)).canBeCollidedWith() && (entity8 != this.owner || this.ticksInAir >= 5) && (movingObjectPosition9 = entity8.boundingBox.expand(0.3F, 0.3F, 0.3F).calculateIntercept(vec3D1, vec3D2)) != null && ((f12 = vec3D1.distance(movingObjectPosition9.hitVec)) < f6 || f6 == 0.0F)) {
				entity4 = entity8;
				f6 = f12;
			}
		}

		if(entity4 != null) {
			movingObjectPosition3 = new MovingObjectPosition(entity4);
		}

		float f10;
		if(movingObjectPosition3 != null) {
			if(movingObjectPosition3.entityHit != null) {
				if(movingObjectPosition3.entityHit.attackEntityFrom(this, 4)) {
					this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.setEntityDead();
				} else {
					this.motionX *= -0.1F;
					this.motionY *= -0.1F;
					this.motionZ *= -0.1F;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}
			} else {
				this.xTile = movingObjectPosition3.blockX;
				this.yTile = movingObjectPosition3.blockY;
				this.zTile = movingObjectPosition3.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.motionX = movingObjectPosition3.hitVec.xCoord - this.posX;
				this.motionY = movingObjectPosition3.hitVec.yCoord - this.posY;
				this.motionZ = movingObjectPosition3.hitVec.zCoord - this.posZ;
				f10 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / f10 * 0.05F;
				this.posY -= this.motionY / f10 * 0.05F;
				this.posZ -= this.motionZ / f10 * 0.05F;
				this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.inGround = true;
				this.arrowShake = 7;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		f10 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2((double)this.motionX, (double)this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2((double)this.motionY, (double)f10) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f11 = 0.99F;
		if(this.handleWaterMovement()) {
			for(int i13 = 0; i13 < 4; ++i13) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25F, this.posY - this.motionY * 0.25F, this.posZ - this.motionZ * 0.25F, this.motionX, this.motionY, this.motionZ);
			}

			f11 = 0.8F;
		}

		this.motionX *= f11;
		this.motionY *= f11;
		this.motionZ *= f11;
		this.motionY -= 0.03F;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setShort("xTile", (short)this.xTile);
		nbtTagCompound.setShort("yTile", (short)this.yTile);
		nbtTagCompound.setShort("zTile", (short)this.zTile);
		nbtTagCompound.setByte("inTile", (byte)this.inTile);
		nbtTagCompound.setByte("shake", (byte)this.arrowShake);
		nbtTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		this.xTile = nbtTagCompound.getShort("xTile");
		this.yTile = nbtTagCompound.getShort("yTile");
		this.zTile = nbtTagCompound.getShort("zTile");
		this.inTile = nbtTagCompound.getByte("inTile") & 255;
		this.arrowShake = nbtTagCompound.getByte("shake") & 255;
		this.inGround = nbtTagCompound.getByte("inGround") == 1;
	}

	protected final String getEntityString() {
		return "Arrow";
	}

	public final void onCollideWithPlayer(EntityPlayer player) {
		if(this.inGround && this.owner == player && this.arrowShake <= 0 && player.inventory.storePartialItemStack(new ItemStack(Item.arrow.shiftedIndex, 1))) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this);
			this.setEntityDead();
		}

	}

	public final float getShadowSize() {
		return 0.0F;
	}
}