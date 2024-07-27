package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.level.World;

public class EntityMob extends EntityCreature {
	protected int attackStrength = 2;

	public EntityMob(World world1) {
		super(world1);
		this.health = 20;
	}

	public void onLivingUpdate() {
		if(this.getEntityBrightness(1.0F) > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		return this.worldObj.playerEntity.getDistanceSqToEntity(this) < 256.0F ? this.worldObj.playerEntity : null;
	}

	public final boolean attackEntityFrom(Entity entity, int i2) {
		if(super.attackEntityFrom(entity, i2)) {
			if(entity != this) {
				this.playerToAttack = entity;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity entity, float f2) {
		if((double)f2 < 2.5D && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.5F - this.worldObj.getLightBrightness(i1, i2, i3);
	}

	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
	}

	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
	}

	protected String getEntityString() {
		return "Monster";
	}

	public final boolean getCanSpawnHere(float f1, float f2, float f3) {
		return this.worldObj.getBlockLightValue((int)f1, (int)f2, (int)f3) <= this.rand.nextInt(8) && super.getCanSpawnHere(f1, f2, f3);
	}
}