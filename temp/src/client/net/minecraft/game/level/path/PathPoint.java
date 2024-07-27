package net.minecraft.game.level.path;

import util.MathHelper;

public final class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	public final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst = false;

	public PathPoint(int xCoord, int yCoord, int zCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.hash = xCoord | yCoord << 10 | zCoord << 20;
	}

	public final float distanceTo(PathPoint pathPoint) {
		float f2 = (float)(pathPoint.xCoord - this.xCoord);
		float f3 = (float)(pathPoint.yCoord - this.yCoord);
		float pathPoint1 = (float)(pathPoint.zCoord - this.zCoord);
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + pathPoint1 * pathPoint1);
	}

	public final boolean equals(Object object1) {
		return ((PathPoint)object1).hash == this.hash;
	}

	public final int hashCode() {
		return this.hash;
	}

	public final boolean isAssigned() {
		return this.index >= 0;
	}

	public final String toString() {
		return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
	}
}