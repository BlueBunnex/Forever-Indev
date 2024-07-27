package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;

import util.MathHelper;

public class EntitySkeleton extends EntityMob {
	public EntitySkeleton(World world1) {
		super(world1);
		this.texture = "/mob/skeleton.png";
	}

	public final void onLivingUpdate() {
		float f1;
		if(this.worldObj.skylightSubtracted > 7 && (f1 = this.getEntityBrightness(1.0F)) > 0.5F && this.worldObj.canBlockSeeTheSky((int)this.posX, (int)this.posY, (int)this.posZ) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
			this.fire = 300;
		}

		super.onLivingUpdate();
	}

	protected final void attackEntity(Entity entity, float f2) {
		if(f2 < 10.0F) {
			f2 = entity.posX - this.posX;
			float f3 = entity.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow entityArrow4;
				++(entityArrow4 = new EntityArrow(this.worldObj, this)).posY;
				float entity1 = entity.posY - 0.2F - entityArrow4.posY;
				float f5 = MathHelper.sqrt_float(f2 * f2 + f3 * f3) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entityArrow4);
				entityArrow4.setArrowHeading(f2, entity1 + f5, f3, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2((double)f3, (double)f2) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
	}

	protected final String getEntityString() {
		return "Skeleton";
	}

	protected final int scoreValue() {
		return Item.arrow.shiftedIndex;
	}
}