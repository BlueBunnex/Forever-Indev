package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.level.World;

public class EntityMonster extends EntityCreature {
	
	protected int attackStrength = 2;

	public EntityMonster(World world) {
		super(world);
		this.health = 20;
	}

	public void onLivingUpdate() {
		float var1 = this.getEntityBrightness(1.0F);
		if(var1 > 0.5F) {
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
		float var1 = this.worldObj.playerEntity.getDistanceSqToEntity(this);
		return var1 < 256.0F ? this.worldObj.playerEntity : null;
	}

	public final boolean attackThisEntity(Entity attacker, int damage) {
		
		if (super.attackThisEntity(attacker, damage)) {
			
			// target entity that attacked this entity
			if (attacker != this)
				this.playerToAttack = attacker;

			return true;
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity toAttack, float damage) {
		
		if (
				(double) damage < 2.5D
				&& toAttack.boundingBox.maxY > this.boundingBox.minY
				&& toAttack.boundingBox.minY < this.boundingBox.maxY
			) {
			this.attackTime = 20;
			toAttack.attackThisEntity(this, this.attackStrength);
		}
	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return 0.5F - this.worldObj.getLightBrightness(var1, var2, var3);
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected String getEntityString() {
		return "Monster";
	}

	public final boolean getCanSpawnHere(float var1, float var2, float var3) {
		byte var4 = this.worldObj.getBlockLightValue((int)var1, (int)var2, (int)var3);
		return var4 <= this.rand.nextInt(8) && super.getCanSpawnHere(var1, var2, var3);
	}
	
	protected int getDroppedRupeeCount() {
		return (int) (Math.random() * 3) + 1;
	}
}
