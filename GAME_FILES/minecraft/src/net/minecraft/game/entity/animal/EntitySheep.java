package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class EntitySheep extends EntityAnimal {
	
	public boolean sheared = false;

	public EntitySheep(World world) {
		super(world);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	public final boolean attackEntityFrom(Entity attacker, int damage) {
		
		if (!this.sheared && attacker instanceof EntityLiving) {
			this.sheared = true;
			int var3 = 1 + this.rand.nextInt(3);

			for (int i = 0; i < var3; i++) {
				EntityItem var5 = this.entityDropItem(Block.clothWhite.blockID, 1, 1.0F);
				var5.motionY += this.rand.nextFloat() * 0.05F;
				var5.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
				var5.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
			}
			
			return false;
		}

		return super.attackEntityFrom(attacker, damage);
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Sheared", this.sheared);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.sheared = var1.getBoolean("Sheared");
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
