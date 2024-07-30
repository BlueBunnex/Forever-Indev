package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public final class PositionTextureVertex {
	public Vec3D vector3D;
	public float texturePositionX;
	public float texturePositionY;

	public PositionTextureVertex(float var1, float var2, float var3, float var4, float var5) {
		this(new Vec3D(var1, var2, var3), var4, var5);
	}

	public final PositionTextureVertex setTexturePosition(float var1, float var2) {
		return new PositionTextureVertex(this, var1, var2);
	}

	private PositionTextureVertex(PositionTextureVertex var1, float var2, float var3) {
		this.vector3D = var1.vector3D;
		this.texturePositionX = var2;
		this.texturePositionY = var3;
	}

	private PositionTextureVertex(Vec3D var1, float var2, float var3) {
		this.vector3D = var1;
		this.texturePositionX = var2;
		this.texturePositionY = var3;
	}
}
