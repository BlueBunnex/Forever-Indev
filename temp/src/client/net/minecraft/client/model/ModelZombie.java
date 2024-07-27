package net.minecraft.client.model;

import util.MathHelper;

public class ModelZombie extends ModelBiped {
	public final void setRotationAngles(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		super.setRotationAngles(limbAngle, animationSpeed, f3, headYaw, headPitch, f6);
		limbAngle = MathHelper.sin(0.0F);
		animationSpeed = MathHelper.sin(0.0F);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - limbAngle * 0.6F);
		this.bipedLeftArm.rotateAngleY = 0.1F - limbAngle * 0.6F;
		this.bipedRightArm.rotateAngleX = -1.5707964F;
		this.bipedLeftArm.rotateAngleX = -1.5707964F;
		this.bipedRightArm.rotateAngleX -= limbAngle * 1.2F - animationSpeed * 0.4F;
		this.bipedLeftArm.rotateAngleX -= limbAngle * 1.2F - animationSpeed * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067F) * 0.05F;
	}
}