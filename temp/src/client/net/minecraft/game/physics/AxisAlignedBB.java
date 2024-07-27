package net.minecraft.game.physics;

public final class AxisAlignedBB {
	private float epsilon = 0.0F;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;

	public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public final AxisAlignedBB addCoord(float f1, float f2, float f3) {
		float f4 = this.minX;
		float f5 = this.minY;
		float f6 = this.minZ;
		float f7 = this.maxX;
		float f8 = this.maxY;
		float f9 = this.maxZ;
		if(f1 < 0.0F) {
			f4 += f1;
		}

		if(f1 > 0.0F) {
			f7 += f1;
		}

		if(f2 < 0.0F) {
			f5 += f2;
		}

		if(f2 > 0.0F) {
			f8 += f2;
		}

		if(f3 < 0.0F) {
			f6 += f3;
		}

		if(f3 > 0.0F) {
			f9 += f3;
		}

		return new AxisAlignedBB(f4, f5, f6, f7, f8, f9);
	}

	public final AxisAlignedBB expand(float f1, float f2, float f3) {
		if(this.minY > this.maxY) {
			throw new IllegalArgumentException("NOOOOOO!");
		} else {
			float f4 = this.minX - f1;
			float f5 = this.minY - f2;
			float f6 = this.minZ - f3;
			f1 += this.maxX;
			f2 += this.maxY;
			f3 += this.maxZ;
			return new AxisAlignedBB(f4, f5, f6, f1, f2, f3);
		}
	}

	public final float calculateXOffset(AxisAlignedBB aabb, float f2) {
		if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
			if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
				float f3;
				if(f2 > 0.0F && aabb.maxX <= this.minX && (f3 = this.minX - aabb.maxX) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && aabb.minX >= this.maxX && (f3 = this.maxX - aabb.minX) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final float calculateYOffset(AxisAlignedBB aabb, float f2) {
		if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
			if(aabb.maxZ > this.minZ && aabb.minZ < this.maxZ) {
				float f3;
				if(f2 > 0.0F && aabb.maxY <= this.minY && (f3 = this.minY - aabb.maxY) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && aabb.minY >= this.maxY && (f3 = this.maxY - aabb.minY) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final float calculateZOffset(AxisAlignedBB aabb, float f2) {
		if(aabb.maxX > this.minX && aabb.minX < this.maxX) {
			if(aabb.maxY > this.minY && aabb.minY < this.maxY) {
				float f3;
				if(f2 > 0.0F && aabb.maxZ <= this.minZ && (f3 = this.minZ - aabb.maxZ) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && aabb.minZ >= this.maxZ && (f3 = this.maxZ - aabb.minZ) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final boolean intersectsWith(AxisAlignedBB aabb) {
		return aabb.maxX >= this.minX && aabb.minX <= this.maxX ? (aabb.maxY >= this.minY && aabb.minY <= this.maxY ? aabb.maxZ >= this.minZ && aabb.minZ <= this.maxZ : false) : false;
	}

	public final void offset(float f1, float f2, float f3) {
		this.minX += f1;
		this.minY += f2;
		this.minZ += f3;
		this.maxX += f1;
		this.maxY += f2;
		this.maxZ += f3;
	}

	public final AxisAlignedBB copy() {
		return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public final MovingObjectPosition calculateIntercept(Vec3D vec3D, Vec3D vec3D2) {
		Vec3D vec3D3 = vec3D.getIntermediateWithXValue(vec3D2, this.minX);
		Vec3D vec3D4 = vec3D.getIntermediateWithXValue(vec3D2, this.maxX);
		Vec3D vec3D5 = vec3D.getIntermediateWithYValue(vec3D2, this.minY);
		Vec3D vec3D6 = vec3D.getIntermediateWithYValue(vec3D2, this.maxY);
		Vec3D vec3D7 = vec3D.getIntermediateWithZValue(vec3D2, this.minZ);
		vec3D2 = vec3D.getIntermediateWithZValue(vec3D2, this.maxZ);
		if(!this.isVecInYZ(vec3D3)) {
			vec3D3 = null;
		}

		if(!this.isVecInYZ(vec3D4)) {
			vec3D4 = null;
		}

		if(!this.isVecInXZ(vec3D5)) {
			vec3D5 = null;
		}

		if(!this.isVecInXZ(vec3D6)) {
			vec3D6 = null;
		}

		if(!this.isVecInXY(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInXY(vec3D2)) {
			vec3D2 = null;
		}

		Vec3D vec3D8 = null;
		if(vec3D3 != null) {
			vec3D8 = vec3D3;
		}

		if(vec3D4 != null && (vec3D8 == null || vec3D.squareDistanceTo(vec3D4) < vec3D.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D4;
		}

		if(vec3D5 != null && (vec3D8 == null || vec3D.squareDistanceTo(vec3D5) < vec3D.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D5;
		}

		if(vec3D6 != null && (vec3D8 == null || vec3D.squareDistanceTo(vec3D6) < vec3D.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D6;
		}

		if(vec3D7 != null && (vec3D8 == null || vec3D.squareDistanceTo(vec3D7) < vec3D.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D7;
		}

		if(vec3D2 != null && (vec3D8 == null || vec3D.squareDistanceTo(vec3D2) < vec3D.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D2;
		}

		if(vec3D8 == null) {
			return null;
		} else {
			byte vec3D1 = -1;
			if(vec3D8 == vec3D3) {
				vec3D1 = 4;
			}

			if(vec3D8 == vec3D4) {
				vec3D1 = 5;
			}

			if(vec3D8 == vec3D5) {
				vec3D1 = 0;
			}

			if(vec3D8 == vec3D6) {
				vec3D1 = 1;
			}

			if(vec3D8 == vec3D7) {
				vec3D1 = 2;
			}

			if(vec3D8 == vec3D2) {
				vec3D1 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, vec3D1, vec3D8);
		}
	}

	private boolean isVecInYZ(Vec3D vev3D) {
		return vev3D == null ? false : vev3D.yCoord >= this.minY && vev3D.yCoord <= this.maxY && vev3D.zCoord >= this.minZ && vev3D.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3D vec3D) {
		return vec3D == null ? false : vec3D.xCoord >= this.minX && vec3D.xCoord <= this.maxX && vec3D.zCoord >= this.minZ && vec3D.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3D vec3D) {
		return vec3D == null ? false : vec3D.xCoord >= this.minX && vec3D.xCoord <= this.maxX && vec3D.yCoord >= this.minY && vec3D.yCoord <= this.maxY;
	}
}