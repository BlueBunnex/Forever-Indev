package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagFloat;
import com.mojang.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

import util.MathHelper;

public abstract class Entity {
	public boolean preventEntitySpawning = false;
	protected World worldObj;
	public float prevPosX;
	public float prevPosY;
	public float prevPosZ;
	public float posX;
	public float posY;
	public float posZ;
	public float motionX;
	public float motionY;
	public float motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox;
	public boolean onGround = false;
	public boolean isCollidedHorizontally = false;
	private boolean surfaceCollision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected boolean canTriggerWalking = true;
	private float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public float lastTickPosX;
	public float lastTickPosY;
	public float lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	private float entityCollisionReduction = 0.0F;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	private boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean isFirstUpdate = true;
	public String skinUrl;

	public Entity(World world) {
		this.worldObj = world;
		this.setPosition(0.0F, 0.0F, 0.0F);
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			float f1 = (float)this.worldObj.xSpawn + 0.5F;
			float f2 = (float)this.worldObj.ySpawn;

			for(float f3 = (float)this.worldObj.zSpawn + 0.5F; f2 > 0.0F; ++f2) {
				this.setPosition(f1, f2, f3);
				if(this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0) {
					break;
				}
			}

			this.motionX = this.motionY = this.motionZ = 0.0F;
			this.rotationYaw = this.worldObj.rotSpawn;
			this.rotationPitch = 0.0F;
		}
	}

	public void setEntityDead() {
		this.isDead = true;
	}

	protected void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	protected final void setPosition(float posX, float posY, float posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		float f4 = this.width / 2.0F;
		float f5 = this.height / 2.0F;
		this.boundingBox = new AxisAlignedBB(posX - f4, posY - f5, posZ - f4, posX + f4, posY + f5, posZ + f4);
	}

	public void onEntityUpdate() {
		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.isFirstUpdate) {
				float f1;
				if((f1 = MathHelper.sqrt_float(this.motionX * this.motionX * 0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.2F) * 0.2F) > 1.0F) {
					f1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				f1 = (float)((int)this.boundingBox.minY);

				int i2;
				float f3;
				float f4;
				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + f3, f1 + 1.0F, this.posZ + f4, this.motionX, this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ);
				}

				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + f3, f1 + 1.0F, this.posZ + f4, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.fire > 0) {
			if(this.fire % 20 == 0) {
				this.attackEntityFrom((Entity)null, 1);
			}

			--this.fire;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
			this.fire = 600;
		}

		float f5;
		if(this.posX < -8.0F) {
			f5 = -(this.posX + 8.0F);
			this.motionX += f5 * 0.001F;
		}

		if(this.posZ < -8.0F) {
			f5 = -(this.posZ + 8.0F);
			this.motionZ += f5 * 0.001F;
		}

		if(this.posX > (float)this.worldObj.width + 8.0F) {
			f5 = this.posX - (float)this.worldObj.width + 8.0F;
			this.motionX -= f5 * 0.001F;
		}

		if(this.posZ > (float)this.worldObj.length + 8.0F) {
			f5 = this.posZ - (float)this.worldObj.length + 8.0F;
			this.motionZ -= f5 * 0.001F;
		}

		this.isFirstUpdate = false;
	}

	public final boolean isOffsetPositionInLiquid(float f1, float f2, float f3) {
		float f4 = f3;
		f3 = f2;
		f2 = f1;
		AxisAlignedBB axisAlignedBB5 = this.boundingBox;
		axisAlignedBB5 = new AxisAlignedBB(axisAlignedBB5.minX + f4, axisAlignedBB5.minY + f3, axisAlignedBB5.minZ + f4, axisAlignedBB5.maxX + f2, axisAlignedBB5.maxY + f3, axisAlignedBB5.maxZ + f4);
		return this.worldObj.getCollidingBoundingBoxes(axisAlignedBB5).size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB5);
	}

	public final void moveEntity(float f1, float f2, float f3) {
		if(this.noClip) {
			this.boundingBox.offset(f1, f2, f3);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
			this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
		} else {
			float f4 = this.posX;
			float f5 = this.posZ;
			float f6 = f1;
			float f7 = f2;
			float f8 = f3;
			AxisAlignedBB axisAlignedBB9 = this.boundingBox.copy();
			ArrayList arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(f1, f2, f3));

			for(int i11 = 0; i11 < arrayList10.size(); ++i11) {
				f2 = ((AxisAlignedBB)arrayList10.get(i11)).calculateYOffset(this.boundingBox, f2);
			}

			this.boundingBox.offset(0.0F, f2, 0.0F);
			if(!this.surfaceCollision && f7 != f2) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			boolean z18 = this.onGround || f7 != f2 && f7 < 0.0F;

			int i12;
			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				f1 = ((AxisAlignedBB)arrayList10.get(i12)).calculateXOffset(this.boundingBox, f1);
			}

			this.boundingBox.offset(f1, 0.0F, 0.0F);
			if(!this.surfaceCollision && f6 != f1) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				f3 = ((AxisAlignedBB)arrayList10.get(i12)).calculateZOffset(this.boundingBox, f3);
			}

			this.boundingBox.offset(0.0F, 0.0F, f3);
			if(!this.surfaceCollision && f8 != f3) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			int i17;
			float f19;
			float f20;
			if(this.stepHeight > 0.0F && z18 && this.ySize < 0.05F && (f6 != f1 || f8 != f3)) {
				f20 = f1;
				f19 = f2;
				float f13 = f3;
				f1 = f6;
				f2 = this.stepHeight;
				f3 = f8;
				AxisAlignedBB axisAlignedBB14 = this.boundingBox.copy();
				this.boundingBox = axisAlignedBB9.copy();
				arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(f6, f2, f8));

				for(i17 = 0; i17 < arrayList10.size(); ++i17) {
					f2 = ((AxisAlignedBB)arrayList10.get(i17)).calculateYOffset(this.boundingBox, f2);
				}

				this.boundingBox.offset(0.0F, f2, 0.0F);
				if(!this.surfaceCollision && f7 != f2) {
					f3 = 0.0F;
					f2 = 0.0F;
					f1 = 0.0F;
				}

				for(i17 = 0; i17 < arrayList10.size(); ++i17) {
					f1 = ((AxisAlignedBB)arrayList10.get(i17)).calculateXOffset(this.boundingBox, f1);
				}

				this.boundingBox.offset(f1, 0.0F, 0.0F);
				if(!this.surfaceCollision && f6 != f1) {
					f3 = 0.0F;
					f2 = 0.0F;
					f1 = 0.0F;
				}

				for(i17 = 0; i17 < arrayList10.size(); ++i17) {
					f3 = ((AxisAlignedBB)arrayList10.get(i17)).calculateZOffset(this.boundingBox, f3);
				}

				this.boundingBox.offset(0.0F, 0.0F, f3);
				if(!this.surfaceCollision && f8 != f3) {
					f3 = 0.0F;
					f2 = 0.0F;
					f1 = 0.0F;
				}

				if(f20 * f20 + f13 * f13 >= f1 * f1 + f3 * f3) {
					f1 = f20;
					f2 = f19;
					f3 = f13;
					this.boundingBox = axisAlignedBB14.copy();
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0F;
			this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0F;
			this.isCollidedHorizontally = f6 != f1 || f8 != f3;
			this.onGround = f7 != f2 && f7 < 0.0F;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(f2 < 0.0F) {
				this.fallDistance -= f2;
			}

			if(f6 != f1) {
				this.motionX = 0.0F;
			}

			if(f7 != f2) {
				this.motionY = 0.0F;
			}

			if(f8 != f3) {
				this.motionZ = 0.0F;
			}

			f20 = this.posX - f4;
			f19 = this.posZ - f5;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_float(f20 * f20 + f19 * f19) * 0.6D);
			if(this.canTriggerWalking) {
				int i21 = (int)this.posX;
				int i23 = (int)(this.posY - 0.2F - this.yOffset);
				i17 = (int)this.posZ;
				int i16 = this.worldObj.getBlockId(i21, i23, i17);
				if(this.distanceWalkedModified > (float)this.nextStepDistance && i16 > 0) {
					++this.nextStepDistance;
					StepSound stepSound15 = Block.blocksList[i16].stepSound;
					if(!Block.blocksList[i16].material.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound15.stepSoundDir2(), stepSound15.soundVolume * 0.15F, stepSound15.soundPitch);
					}

					Block.blocksList[i16].onEntityWalking(this.worldObj, i21, i23, i17);
				}
			}

			this.ySize *= 0.4F;
			boolean z22 = this.handleWaterMovement();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
				this.dealFireDamage(1);
				if(!z22) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(z22 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	protected void dealFireDamage(int i1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	protected void fall(float f1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.water);
	}

	public final boolean isInsideOfWater() {
		int i1;
		return (i1 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY + this.getEyeHeight()), (int)this.posZ)) != 0 ? Block.blocksList[i1].material == Material.water : false;
	}

	protected float getEyeHeight() {
		return 0.0F;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.lava);
	}

	public final void moveFlying(float f1, float f2, float f3) {
		float f4;
		if((f4 = MathHelper.sqrt_float(f1 * f1 + f2 * f2)) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			f3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			f4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += f1 * f4 - f2 * f3;
			this.motionZ += f2 * f4 + f1 * f3;
		}
	}

	public float getEntityBrightness(float f1) {
		int i4 = (int)this.posX;
		int i2 = (int)(this.posY + this.yOffset / 2.0F);
		int i3 = (int)this.posZ;
		return this.worldObj.getLightBrightness(i4, i2, i3);
	}

	public final void setWorld(World world) {
		this.worldObj = world;
	}

	public final void setPositionAndRotation(float posX, float f2, float posZ, float rotationYaw, float rotationPitch) {
		this.prevPosX = this.posX = posX;
		this.prevPosY = this.posY = f2 + this.yOffset;
		this.prevPosZ = this.posZ = posZ;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final float getDistanceSqToEntity(Entity entity) {
		float f2 = this.posX - entity.posX;
		float f3 = this.posY - entity.posY;
		float entity1 = this.posZ - entity.posZ;
		return f2 * f2 + f3 * f3 + entity1 * entity1;
	}

	public void onCollideWithPlayer(EntityPlayer player) {
	}

	public final void applyEntityCollision(Entity entity) {
		float f2 = entity.posX - this.posX;
		float f3 = entity.posZ - this.posZ;
		float f4;
		if((f4 = f2 * f2 + f3 * f3) >= 0.01F) {
			f4 = MathHelper.sqrt_float(f4);
			f2 /= f4;
			f3 /= f4;
			f2 /= f4;
			f3 /= f4;
			f2 *= 0.05F;
			f3 *= 0.05F;
			this.addVelocity(-f2, 0.0F, -f3);
			entity.addVelocity(f2, 0.0F, f3);
		}

	}

	private void addVelocity(float motionX, float f2, float motionZ) {
		this.motionX += motionX;
		this.motionY = this.motionY;
		this.motionZ += motionZ;
	}

	public boolean attackEntityFrom(Entity entity, int i2) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public String getTexture() {
		return null;
	}

	public final void writeToNBT(NBTTagCompound nbtTagCompound) {
		String string2 = this.getEntityString();
		if(!this.isDead && string2 != null) {
			nbtTagCompound.setString("id", string2);
			nbtTagCompound.setTag("Pos", newDoubleNBTList(new float[]{this.posX, this.posY, this.posZ}));
			nbtTagCompound.setTag("Motion", newDoubleNBTList(new float[]{this.motionX, this.motionY, this.motionZ}));
			nbtTagCompound.setTag("Rotation", newDoubleNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
			nbtTagCompound.setFloat("FallDistance", this.fallDistance);
			nbtTagCompound.setShort("Fire", (short)this.fire);
			nbtTagCompound.setShort("Air", (short)this.air);
			this.writeEntityToNBT(nbtTagCompound);
		}
	}

	public final void readFromNBT(NBTTagCompound nbtTagCompound) {
		NBTTagList nBTTagList2 = nbtTagCompound.getTagList("Pos");
		NBTTagList nBTTagList3 = nbtTagCompound.getTagList("Motion");
		NBTTagList nBTTagList4 = nbtTagCompound.getTagList("Rotation");
		this.posX = ((NBTTagFloat)nBTTagList2.tagAt(0)).floatValue;
		this.posY = ((NBTTagFloat)nBTTagList2.tagAt(1)).floatValue;
		this.posZ = ((NBTTagFloat)nBTTagList2.tagAt(2)).floatValue;
		this.motionX = ((NBTTagFloat)nBTTagList3.tagAt(0)).floatValue;
		this.motionY = ((NBTTagFloat)nBTTagList3.tagAt(1)).floatValue;
		this.motionZ = ((NBTTagFloat)nBTTagList3.tagAt(2)).floatValue;
		this.rotationYaw = ((NBTTagFloat)nBTTagList4.tagAt(0)).floatValue;
		this.rotationPitch = ((NBTTagFloat)nBTTagList4.tagAt(1)).floatValue;
		this.fallDistance = nbtTagCompound.getFloat("FallDistance");
		this.fire = nbtTagCompound.getShort("Fire");
		this.air = nbtTagCompound.getShort("Air");
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		this.readEntityFromNBT(nbtTagCompound);
	}

	protected abstract String getEntityString();

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	private static NBTTagList newDoubleNBTList(float... f0) {
		NBTTagList nBTTagList1 = new NBTTagList();
		int i2 = (f0 = f0).length;

		for(int i3 = 0; i3 < i2; ++i3) {
			float f4 = f0[i3];
			nBTTagList1.setTag(new NBTTagFloat(f4));
		}

		return nBTTagList1;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public final EntityItem dropItemWithOffset(int i1, int i2) {
		return this.entityDropItem(i1, 1, 0.0F);
	}

	public final EntityItem entityDropItem(int i1, int i2, float f3) {
		EntityItem entityItem4;
		(entityItem4 = new EntityItem(this.worldObj, this.posX, this.posY + f3, this.posZ, new ItemStack(i1, i2))).delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(entityItem4);
		return entityItem4;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}
}