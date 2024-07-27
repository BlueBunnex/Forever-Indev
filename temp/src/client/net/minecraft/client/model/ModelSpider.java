package net.minecraft.client.model;

import util.MathHelper;

public final class ModelSpider extends ModelBase {
	private ModelRenderer spiderHead = new ModelRenderer(32, 4);
	private ModelRenderer spiderNeck;
	private ModelRenderer spiderBody;
	private ModelRenderer spiderLeg1;
	private ModelRenderer spiderLeg2;
	private ModelRenderer spiderLeg3;
	private ModelRenderer spiderLeg4;
	private ModelRenderer spiderLeg5;
	private ModelRenderer spiderLeg6;
	private ModelRenderer spiderLeg7;
	private ModelRenderer spiderLeg8;

	public ModelSpider() {
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.spiderHead.setRotationPoint(0.0F, 15.0F, -3.0F);
		this.spiderNeck = new ModelRenderer(0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
		this.spiderNeck.setRotationPoint(0.0F, 15.0F, 0.0F);
		this.spiderBody = new ModelRenderer(0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
		this.spiderBody.setRotationPoint(0.0F, 15.0F, 9.0F);
		this.spiderLeg1 = new ModelRenderer(18, 0);
		this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
		this.spiderLeg2 = new ModelRenderer(18, 0);
		this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
		this.spiderLeg3 = new ModelRenderer(18, 0);
		this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
		this.spiderLeg4 = new ModelRenderer(18, 0);
		this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
		this.spiderLeg5 = new ModelRenderer(18, 0);
		this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
		this.spiderLeg6 = new ModelRenderer(18, 0);
		this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
		this.spiderLeg7 = new ModelRenderer(18, 0);
		this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
		this.spiderLeg8 = new ModelRenderer(18, 0);
		this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.spiderLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
	}

	public final void render(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.setRotationAngles(limbAngle, animationSpeed, f3, headYaw, headPitch, 1.0F);
		this.spiderHead.render(1.0F);
		this.spiderNeck.render(1.0F);
		this.spiderBody.render(1.0F);
		this.spiderLeg1.render(1.0F);
		this.spiderLeg2.render(1.0F);
		this.spiderLeg3.render(1.0F);
		this.spiderLeg4.render(1.0F);
		this.spiderLeg5.render(1.0F);
		this.spiderLeg6.render(1.0F);
		this.spiderLeg7.render(1.0F);
		this.spiderLeg8.render(1.0F);
	}

	public final void setRotationAngles(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.spiderHead.rotateAngleY = headYaw / 57.295776F;
		this.spiderHead.rotateAngleX = headPitch / 57.295776F;
		this.spiderLeg1.rotateAngleZ = -0.7853982F;
		this.spiderLeg2.rotateAngleZ = 0.7853982F;
		this.spiderLeg3.rotateAngleZ = -0.58119464F;
		this.spiderLeg4.rotateAngleZ = 0.58119464F;
		this.spiderLeg5.rotateAngleZ = -0.58119464F;
		this.spiderLeg6.rotateAngleZ = 0.58119464F;
		this.spiderLeg7.rotateAngleZ = -0.7853982F;
		this.spiderLeg8.rotateAngleZ = 0.7853982F;
		this.spiderLeg1.rotateAngleY = 0.7853982F;
		this.spiderLeg2.rotateAngleY = -0.7853982F;
		this.spiderLeg3.rotateAngleY = 0.3926991F;
		this.spiderLeg4.rotateAngleY = -0.3926991F;
		this.spiderLeg5.rotateAngleY = -0.3926991F;
		this.spiderLeg6.rotateAngleY = 0.3926991F;
		this.spiderLeg7.rotateAngleY = -0.7853982F;
		this.spiderLeg8.rotateAngleY = 0.7853982F;
		f3 = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F) * 0.4F) * animationSpeed;
		headYaw = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * animationSpeed;
		headPitch = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + (float)Math.PI / 2F) * 0.4F) * animationSpeed;
		f6 = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 4.712389F) * 0.4F) * animationSpeed;
		float f7 = Math.abs(MathHelper.sin(limbAngle * 0.6662F) * 0.4F) * animationSpeed;
		float f8 = Math.abs(MathHelper.sin(limbAngle * 0.6662F + (float)Math.PI) * 0.4F) * animationSpeed;
		float f9 = Math.abs(MathHelper.sin(limbAngle * 0.6662F + (float)Math.PI / 2F) * 0.4F) * animationSpeed;
		limbAngle = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 4.712389F) * 0.4F) * animationSpeed;
		this.spiderLeg1.rotateAngleY += f3;
		this.spiderLeg2.rotateAngleY -= f3;
		this.spiderLeg3.rotateAngleY += headYaw;
		this.spiderLeg4.rotateAngleY -= headYaw;
		this.spiderLeg5.rotateAngleY += headPitch;
		this.spiderLeg6.rotateAngleY -= headPitch;
		this.spiderLeg7.rotateAngleY += f6;
		this.spiderLeg8.rotateAngleY -= f6;
		this.spiderLeg1.rotateAngleZ += f7;
		this.spiderLeg2.rotateAngleZ -= f7;
		this.spiderLeg3.rotateAngleZ += f8;
		this.spiderLeg4.rotateAngleZ -= f8;
		this.spiderLeg5.rotateAngleZ += f9;
		this.spiderLeg6.rotateAngleZ -= f9;
		this.spiderLeg7.rotateAngleZ += limbAngle;
		this.spiderLeg8.rotateAngleZ -= limbAngle;
	}
}