package net.minecraft.game.physics;

import net.minecraft.game.entity.Entity;

public final class MovingObjectPosition {
	public int typeOfHit;
	public int blockX;
	public int blockY;
	public int blockZ;
	public int sideHit;
	public Vec3D hitVec;
	public Entity entityHit;

	public MovingObjectPosition(int blockX, int blockY, int blockZ, int sideHit, Vec3D vec3D) {
		this.typeOfHit = 0;
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
		this.sideHit = sideHit;
		this.hitVec = new Vec3D(vec3D.xCoord, vec3D.yCoord, vec3D.zCoord);
	}

	public MovingObjectPosition(Entity entityHit) {
		this.typeOfHit = 1;
		this.entityHit = entityHit;
		this.hitVec = new Vec3D(entityHit.posX, entityHit.posY, entityHit.posZ);
	}
}