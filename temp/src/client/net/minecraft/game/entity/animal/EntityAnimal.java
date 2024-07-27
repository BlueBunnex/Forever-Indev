package net.minecraft.game.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public abstract class EntityAnimal extends EntityCreature {
	public EntityAnimal(World world1) {
		super(world1);
	}

	protected final float getBlockPathWeight(int i1, int i2, int i3) {
		return this.worldObj.getBlockId(i1, i2 - 1, i3) == Block.grass.blockID ? 10.0F : this.worldObj.getLightBrightness(i1, i2, i3) - 0.5F;
	}

	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
	}

	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
	}

	public final boolean getCanSpawnHere(float f1, float f2, float f3) {
		return this.worldObj.getBlockLightValue((int)f1, (int)f2, (int)f3) > 8 && super.getCanSpawnHere(f1, f2, f3);
	}
}