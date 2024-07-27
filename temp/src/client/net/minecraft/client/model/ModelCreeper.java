package net.minecraft.client.model;

import util.MathHelper;

public final class ModelCreeper extends ModelBase {
	private ModelRenderer head = new ModelRenderer(0, 0);
	private ModelRenderer headwear;
	private ModelRenderer body;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer leg4;

	public ModelCreeper() {
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.headwear = new ModelRenderer(32, 0);
		this.headwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
		this.headwear.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.body = new ModelRenderer(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.body.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.leg1 = new ModelRenderer(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
		this.leg2 = new ModelRenderer(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
		this.leg3 = new ModelRenderer(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
		this.leg4 = new ModelRenderer(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
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
		this.leg1.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * animationSpeed;
		this.leg2.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
		this.leg3.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 1.4F * animationSpeed;
		this.leg4.rotateAngleX = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * animationSpeed;
	}
}