package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;

import util.MathHelper;

public class EntitySpider extends EntityMob {
	public EntitySpider(World world1) {
		super(world1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
	}

	protected final Entity findPlayerToAttack() {
		return this.getEntityBrightness(1.0F) < 0.5F && this.worldObj.playerEntity.getDistanceSqToEntity(this) < 256.0F ? this.worldObj.playerEntity : null;
	}

	protected final void attackEntity(Entity entity, float f2) {
		if(this.getEntityBrightness(1.0F) > 0.5F && this.rand.nextInt(100) == 0) {
			this.playerToAttack = null;
		} else {
			if(f2 > 2.0F && f2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					f2 = entity.posX - this.posX;
					float entity1 = entity.posZ - this.posZ;
					float f3 = MathHelper.sqrt_float(f2 * f2 + entity1 * entity1);
					this.motionX = f2 / f3 * 0.5F * 0.8F + this.motionX * 0.2F;
					this.motionZ = entity1 / f3 * 0.5F * 0.8F + this.motionZ * 0.2F;
					this.motionY = 0.4F;
					return;
				}
			} else {
				super.attackEntity(entity, f2);
			}

		}
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
	}

	protected final String getEntityString() {
		return "Spider";
	}

	protected final int scoreValue() {
		return Item.silk.shiftedIndex;
	}
}