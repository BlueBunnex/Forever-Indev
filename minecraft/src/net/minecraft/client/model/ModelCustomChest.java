package net.minecraft.client.model;

public class ModelCustomChest extends ModelBase {
    public ModelRenderer chestLeft;
    public ModelRenderer chestRight;

    public ModelCustomChest() {
        this(0.0F);
    }

    public ModelCustomChest(float scale) {
        this.chestLeft = new ModelRenderer(16, 32); // Define texture position
        this.chestLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 2, scale);
        this.chestLeft.setRotationPoint(-1.2F, 2.0F, -1.0F); // Position it appropriately
        this.chestLeft.rotateAngleX = -0.4F;
        this.chestLeft.rotateAngleY = 0.2F;
        this.chestLeft.rotateAngleZ = 0.05F;

        this.chestRight = new ModelRenderer(16, 32);
        this.chestRight.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 2, scale);
        this.chestRight.setRotationPoint(1.2F, 2.0F, -1.0F);
        this.chestRight.rotateAngleX = -0.4F;
        this.chestRight.rotateAngleY = -0.2F;
        this.chestRight.rotateAngleZ = -0.05F;
    }

    public void render(float scale) {
        this.chestLeft.render(scale);
        this.chestRight.render(scale);
    }
}
