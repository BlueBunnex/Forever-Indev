package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public final class PositionTextureVertex {
	public Vec3D vector3D;
	public float texturePositionX;
	public float texturePositionY;

	public PositionTextureVertex(float f1, float f2, float f3, float f4, float f5) {
		this(new Vec3D(f1, f2, f3), f4, f5);
	}

	public final PositionTextureVertex setTexturePosition(float f1, float f2) {
		return new PositionTextureVertex(this, f1, f2);
	}

	private PositionTextureVertex(PositionTextureVertex positionTextureVertex, float f2, float f3) {
		this.vector3D = positionTextureVertex.vector3D;
		this.texturePositionX = f2;
		this.texturePositionY = f3;
	}

	private PositionTextureVertex(Vec3D vec3D, float f2, float f3) {
		this.vector3D = vec3D;
		this.texturePositionX = f2;
		this.texturePositionY = f3;
	}
}