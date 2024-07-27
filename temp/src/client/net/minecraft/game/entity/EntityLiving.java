package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

import util.MathHelper;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
	protected String texture = "/char.png";
	private int scoreValue = 0;
	public int health;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	public float prevLimbYaw;
	public float limbYaw;
	public float limbSwing;
	protected int entityAge;
	protected float moveStrafing;
	protected float moveForward;
	private float randomYawVelocity;
	protected boolean isJumping;
	private float defaultPitch;
	protected float moveSpeed;

	public EntityLiving(World world1) {
		super(world1);
		Math.random();
		this.entityAge = 0;
		this.isJumping = false;
		this.defaultPitch = 0.0F;
		this.moveSpeed = 0.7F;
		this.health = 10;
		this.preventEntitySpawning = true;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
		this.rotationYaw = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.stepHeight = 0.5F;
	}

	public final String getTexture() {
		return this.texture;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final boolean canBePushed() {
		return !this.isDead;
	}

	protected float getEyeHeight() {
		return this.height * 0.85F;
	}

	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -80;
			String string1;
			if((string1 = this.getLivingSound()) != null) {
				this.worldObj.playSoundAtEntity(this, string1, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

		float f2;
		float f3;
		float f4;
		if(this.isInsideOfWater()) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(int i7 = 0; i7 < 8; ++i7) {
					f2 = this.rand.nextFloat() - this.rand.nextFloat();
					f3 = this.rand.nextFloat() - this.rand.nextFloat();
					f4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + f2, this.posY + f3, this.posZ + f4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				this.setEntityDead();
			}
		}

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.onLivingUpdate();
		float f8 = this.posX - this.prevPosX;
		f2 = this.posZ - this.prevPosZ;
		f3 = MathHelper.sqrt_float(f8 * f8 + f2 * f2);
		f4 = this.renderYawOffset;
		float f5 = 0.0F;
		float f6 = 0.0F;
		if(f3 > 0.05F) {
			f6 = 1.0F;
			f5 = f3 * 3.0F;
			f4 = (float)Math.atan2((double)f2, (double)f8) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			f6 = 0.0F;
		}

		this.rotationYawHead += (f6 - this.rotationYawHead) * 0.3F;

		for(f8 = f4 - this.renderYawOffset; f8 < -180.0F; f8 += 360.0F) {
		}

		while(f8 >= 180.0F) {
			f8 -= 360.0F;
		}

		this.renderYawOffset += f8 * 0.1F;

		for(f8 = this.rotationYaw - this.renderYawOffset; f8 < -180.0F; f8 += 360.0F) {
		}

		while(f8 >= 180.0F) {
			f8 -= 360.0F;
		}

		boolean z9 = f8 < -90.0F || f8 >= 90.0F;
		if(f8 < -75.0F) {
			f8 = -75.0F;
		}

		if(f8 >= 75.0F) {
			f8 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - f8;
		this.renderYawOffset += f8 * 0.1F;
		if(z9) {
			f5 = -f5;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.prevRotationYawHead += f5;
	}

	protected final void setSize(float width, float height) {
		super.setSize(width, height);
	}

	public final void heal(int i1) {
		if(this.health > 0) {
			this.health += i1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(Entity entity, int i2) {
		if(!this.worldObj.survivalWorld) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else {
				this.limbYaw = 1.5F;
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(this.prevHealth - i2 >= this.health) {
						return false;
					}

					this.health = this.prevHealth - i2;
				} else {
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.health -= i2;
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				if(entity != null) {
					float f6 = entity.posX - this.posX;
					float f3 = entity.posZ - this.posZ;
					this.attackedAtYaw = (float)(Math.atan2((double)f3, (double)f6) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
					float f5 = MathHelper.sqrt_float(f6 * f6 + f3 * f3);
					this.motionX /= 2.0F;
					this.motionY /= 2.0F;
					this.motionZ /= 2.0F;
					this.motionX -= f6 / f5 * 0.4F;
					this.motionY += 0.4F;
					this.motionZ -= f3 / f5 * 0.4F;
					if(this.motionY > 0.4F) {
						this.motionY = 0.4F;
					}
				} else {
					this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
				}

				if(this.health <= 0) {
					this.worldObj.playSoundAtEntity(this, this.getDeathSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					this.onDeath(entity);
				} else {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void onDeath(Entity entity) {
		int i4;
		if((i4 = this.scoreValue()) > 0) {
			int i2 = this.rand.nextInt(3);

			for(int i3 = 0; i3 < i2; ++i3) {
				this.dropItemWithOffset(i4, 1);
			}
		}

	}

	protected int scoreValue() {
		return 0;
	}

	protected final void fall(float f1) {
		int i3;
		if((i3 = (int)Math.ceil((double)(f1 - 3.0F))) > 0) {
			this.attackEntityFrom((Entity)null, i3);
			if((i3 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ)) > 0) {
				StepSound stepSound4 = Block.blocksList[i3].stepSound;
				this.worldObj.playSoundAtEntity(this, stepSound4.stepSoundDir2(), stepSound4.soundVolume * 0.5F, stepSound4.soundPitch * 0.75F);
			}
		}

	}

	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setShort("Health", (short)this.health);
		nbtTagCompound.setShort("HurtTime", (short)this.hurtTime);
		nbtTagCompound.setShort("DeathTime", (short)this.deathTime);
		nbtTagCompound.setShort("AttackTime", (short)this.attackTime);
	}

	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		this.health = nbtTagCompound.getShort("Health");
		if(!nbtTagCompound.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = nbtTagCompound.getShort("HurtTime");
		this.deathTime = nbtTagCompound.getShort("DeathTime");
		this.attackTime = nbtTagCompound.getShort("AttackTime");
	}

	protected String getEntityString() {
		return "Mob";
	}

	public final boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		++this.entityAge;
		Entity entity1;
		float f2;
		float f3;
		if(this.entityAge > 600 && this.rand.nextInt(800) == 0 && (entity1 = this.worldObj.getPlayerEntity()) != null) {
			f2 = entity1.posX - this.posX;
			f3 = entity1.posY - this.posY;
			float f5 = entity1.posZ - this.posZ;
			if(f2 * f2 + f3 * f3 + f5 * f5 < 1024.0F) {
				this.entityAge = 0;
			} else {
				this.setEntityDead();
			}
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updatePlayerActionState();
		}

		boolean z6 = this.handleWaterMovement();
		boolean z8 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z6) {
				this.motionY += 0.04F;
			} else if(z8) {
				this.motionY += 0.04F;
			} else if(this.onGround) {
				this.motionY = 0.42F;
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		f3 = this.moveForward;
		f2 = this.moveStrafing;
		float f4;
		if(this.handleWaterMovement()) {
			f4 = this.posY;
			this.moveFlying(f2, f3, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.8F;
			this.motionY *= 0.8F;
			this.motionZ *= 0.8F;
			this.motionY = (float)((double)this.motionY - 0.02D);
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + f4, this.motionZ)) {
				this.motionY = 0.3F;
			}
		} else if(this.handleLavaMovement()) {
			f4 = this.posY;
			this.moveFlying(f2, f3, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5F;
			this.motionY *= 0.5F;
			this.motionZ *= 0.5F;
			this.motionY = (float)((double)this.motionY - 0.02D);
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + f4, this.motionZ)) {
				this.motionY = 0.3F;
			}
		} else {
			this.moveFlying(f2, f3, this.onGround ? 0.1F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.91F;
			this.motionY *= 0.98F;
			this.motionZ *= 0.91F;
			this.motionY = (float)((double)this.motionY - 0.08D);
			if(this.onGround) {
				this.motionX *= 0.6F;
				this.motionZ *= 0.6F;
			}
		}

		this.prevLimbYaw = this.limbYaw;
		f4 = this.posX - this.prevPosX;
		f2 = this.posZ - this.prevPosZ;
		if((f2 = MathHelper.sqrt_float(f4 * f4 + f2 * f2) * 4.0F) > 1.0F) {
			f2 = 1.0F;
		}

		this.limbYaw += (f2 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
		List list9;
		if((list9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2F, 0.0F, 0.2F))) != null && list9.size() > 0) {
			for(int i7 = 0; i7 < list9.size(); ++i7) {
				Entity entity10;
				if((entity10 = (Entity)list9.get(i7)).canBePushed()) {
					entity10.applyEntityCollision(this);
				}
			}
		}

	}

	protected void updatePlayerActionState() {
		if(this.rand.nextFloat() < 0.07F) {
			this.moveStrafing = (this.rand.nextFloat() - 0.5F) * this.moveSpeed;
			this.moveForward = this.rand.nextFloat() * this.moveSpeed;
		}

		this.isJumping = this.rand.nextFloat() < 0.01F;
		if(this.rand.nextFloat() < 0.04F) {
			this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 60.0F;
		}

		this.rotationYaw += this.randomYawVelocity;
		this.rotationPitch = 0.0F;
		boolean z1 = this.handleWaterMovement();
		boolean z2 = this.handleLavaMovement();
		if(z1 || z2) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public boolean getCanSpawnHere(float f1, float f2, float f3) {
		this.setPosition(f1, f2 + this.height / 2.0F, f3);
		return this.worldObj.checkIfAABBIsClear1(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
}