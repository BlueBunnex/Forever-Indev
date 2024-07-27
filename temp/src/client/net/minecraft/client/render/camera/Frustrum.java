package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public final class Frustrum implements ICamera {
	private ClippingHelper clippingHelper = ClippingHelperImplementation.init();

	public final boolean isBoundingBoxInFrustrum(AxisAlignedBB aabb) {
		ClippingHelper clippingHelper10001 = this.clippingHelper;
		float f7 = aabb.maxZ;
		float f6 = aabb.maxY;
		float f5 = aabb.maxX;
		float f4 = aabb.minZ;
		float f3 = aabb.minY;
		float f2 = aabb.minX;
		ClippingHelper clippingHelper9 = this.clippingHelper;

		for(int i8 = 0; i8 < 6; ++i8) {
			if(clippingHelper9.frustrum[i8][0] * f2 + clippingHelper9.frustrum[i8][1] * f3 + clippingHelper9.frustrum[i8][2] * f4 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f5 + clippingHelper9.frustrum[i8][1] * f3 + clippingHelper9.frustrum[i8][2] * f4 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f2 + clippingHelper9.frustrum[i8][1] * f6 + clippingHelper9.frustrum[i8][2] * f4 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f5 + clippingHelper9.frustrum[i8][1] * f6 + clippingHelper9.frustrum[i8][2] * f4 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f2 + clippingHelper9.frustrum[i8][1] * f3 + clippingHelper9.frustrum[i8][2] * f7 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f5 + clippingHelper9.frustrum[i8][1] * f3 + clippingHelper9.frustrum[i8][2] * f7 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f2 + clippingHelper9.frustrum[i8][1] * f6 + clippingHelper9.frustrum[i8][2] * f7 + clippingHelper9.frustrum[i8][3] <= 0.0F && clippingHelper9.frustrum[i8][0] * f5 + clippingHelper9.frustrum[i8][1] * f6 + clippingHelper9.frustrum[i8][2] * f7 + clippingHelper9.frustrum[i8][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
}