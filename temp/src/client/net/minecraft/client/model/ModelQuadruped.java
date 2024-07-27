package net.minecraft.client.model;

import util.MathHelper;

public class ModelQuadruped extends ModelBase {
	public ModelRenderer head = new ModelRenderer(0, 0);
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public ModelQuadruped(int i1, float f2) {
		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.head.setRotationPoint(0.0F, (float)(18 - i1), -6.0F);
		this.body = new ModelRenderer(28, 8);
		this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, 0.0F);
		this.body.setRotationPoint(0.0F, (float)(17 - i1), 2.0F);
		this.leg1 = new ModelRenderer(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, i1, 4, 0.0F);
		this.leg1.setRotationPoint(-3.0F, (float)(24 - i1), 7.0F);
		this.leg2 = new ModelRenderer(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, i1, 4, 0.0F);
		this.leg2.setRotationPoint(3.0F, (float)(24 - i1), 7.0F);
		this.leg3 = new ModelRenderer(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, i1, 4, 0.0F);
		this.leg3.setRotationPoint(-3.0F, (float)(24 - i1), -5.0F);
		this.leg4 = new ModelRenderer(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, i1, 4, 0.0F);
		this.leg4.setRotationPoint(3.0F, (float)(24 - i1), -5.0F);
	}

	public final void render(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.setRotationAngles(limbAngle, animationSpeed, f3, headYaw, headPitch, 1.0F);
		this.head.render(1.0F);
		this.body.render(1.0F);
		this.leg1.render(1.0F);
		this.leg2.render(1.0F);
		this.leg3.render(1.0F);
		this.leg4.render(1.0F);
	}

	public final void setRotationAngles(float limbAngle, float animationSpeed, float f3, float headYaw, float headPitch, float f6) {
		this.head.rotateAngleY = headYaw / 57.295776F;
		this.head.rotateAngleX = headPitch / 57.295776F;
		this.body.rotateAngleX = (float)Math.PI / 2F;
		this.leg1.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * animationSpeed;
		this.leg2.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
		this.leg3.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
		this.leg4.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * animationSpeed;
	}
}