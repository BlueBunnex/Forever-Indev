package net.minecraft.client.model;

import util.MathHelper;

public class ModelBiped extends ModelBase {
	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;

	public ModelBiped() {
		this(0.0F);
	}

	public ModelBiped(float scale) {
		this(scale, 0.0F);
	}

	private ModelBiped(float scale, float f2) {
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHeadwear = new ModelRenderer(32, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody = new ModelRenderer(16, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedRightArm = new ModelRenderer(40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightLeg = new ModelRenderer(0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

	public final void render(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.setRotationAngles(limbAngle, animationSpeed, f3, headYaw, headPitch, 1.0F);
		this.bipedHead.render(1.0F);
		this.bipedBody.render(1.0F);
		this.bipedRightArm.render(1.0F);
		this.bipedLeftArm.render(1.0F);
		this.bipedRightLeg.render(1.0F);
		this.bipedLeftLeg.render(1.0F);
		this.bipedHeadwear.render(1.0F);
	}

	public void setRotationAngles(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.bipedHead.rotateAngleY = headYaw / 57.295776F;
		this.bipedHead.rotateAngleX = headPitch / 57.295776F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 2.0F * animationSpeed;
		this.bipedRightArm.rotateAngleZ = (MathHelper.cos(limbAngle * 0.2312F) + 1.0F) * animationSpeed;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 2.0F * animationSpeed;
		this.bipedLeftArm.rotateAngleZ = (MathHelper.cos(limbAngle * 0.2812F) - 1.0F) * animationSpeed;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * animationSpeed;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067F) * 0.05F;
	}
}