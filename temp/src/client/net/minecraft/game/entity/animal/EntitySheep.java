package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class EntitySheep extends EntityAnimal {
	public boolean sheared = false;

	public EntitySheep(World world1) {
		super(world1);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	public final boolean attackEntityFrom(Entity entity, int i2) {
		if(!this.sheared && entity instanceof EntityLiving) {
			this.sheared = true;
			int i3 = 1 + this.rand.nextInt(3);

			for(int i4 = 0; i4 < i3; ++i4) {
				EntityItem entityItem5;
				EntityItem entityItem10000 = entityItem5 = this.entityDropItem(Block.clothGray.blockID, 1, 1.0F);
				entityItem10000.motionY += this.rand.nextFloat() * 0.05F;
				entityItem5.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
				entityItem5.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
			}
		}

		return super.attackEntityFrom(entity, i2);
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
		nbtTagCompound.setBoolean("Sheared", this.sheared);
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
		this.sheared = nbtTagCompound.getBoolean("Sheared");
	}

	protected final String getEntityString() {
		return "Sheep";
	}

	protected final String getLivingSound() {
		return "mob.sheep";
	}

	protected final String getHurtSound() {
		return "mob.sheep";
	}

	protected final String getDeathSound() {
		return "mob.sheep";
	}
}