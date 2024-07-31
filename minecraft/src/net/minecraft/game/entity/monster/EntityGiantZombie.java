package net.minecraft.game.entity.monster;

import net.minecraft.game.level.World;

public class EntityGiantZombie extends EntityMonster {
	
	public EntityGiantZombie(World world) {
		super(world);
		
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= 10;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected final float getBlockPathWeight(int var1, int var2, int var3) {
		return this.worldObj.getLightBrightness(var1, var2, var3) - 0.5F;
	}

	protected final String getEntityString() {
		return "Giant";
	}
	
	protected String getLivingSound() {
		return "mob.zombie";
	}
	
	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}
}
