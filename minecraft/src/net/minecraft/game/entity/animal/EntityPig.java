package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.item.Item;
import net.minecraft.game.level.World;

public class EntityPig extends EntityAnimal {
	
	public EntityPig(World world) {
		super(world);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return "Pig";
	}

	protected final String getLivingSound() {
		return "mob.pig";
	}

	protected final String getHurtSound() {
		return "mob.pig";
	}

	protected final String getDeathSound() {
		return "mob.pigdeath";
	}

	protected final int getDroppedItemID() {
		return this.fire > 0 ? Item.porkCooked.shiftedIndex : Item.porkRaw.shiftedIndex;
	}
	
	protected final int getDroppedItemCount() {
		return this.rand.nextInt(2) + 1;
	}
}
