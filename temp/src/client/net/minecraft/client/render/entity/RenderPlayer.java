package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

	private void renderPlayer(EntityPlayer player, float f2, float f3, float f4, float f5, float f6) {
		super.a(player, f2, f3 - player.yOffset, f4, f5, f6);
	}

	public final void drawFirstPersonHand() {
		this.modelBipedMain.bipedRightArm.render(1.0F);
	}

	protected final boolean shouldRenderPass(EntityLiving entityLiving, int i2) {
		EntityPlayer entityPlayer10001 = (EntityPlayer)entityLiving;
		int i3 = i2;
		EntityPlayer entityPlayer5 = entityPlayer10001;
		int i4 = 3 - i3;
		InventoryPlayer inventoryPlayer10 = entityPlayer5.inventory;
		ItemStack itemStack6;
		Item item7;
		if((itemStack6 = entityPlayer5.inventory.armorInventory[i4]) != null && (item7 = itemStack6.getItem()) instanceof ItemArmor) {
			ItemArmor itemArmor8 = (ItemArmor)item7;
			this.loadTexture("/armor/" + armorFilenamePrefix[itemArmor8.renderIndex] + "_" + (i3 == 2 ? 2 : 1) + ".png");
			ModelBiped modelBiped9 = i3 == 2 ? this.modelArmor : this.modelArmorChestplate;
			modelBiped9.bipedHead.showModel = i3 == 0;
			modelBiped9.bipedHeadwear.showModel = i3 == 0;
			modelBiped9.bipedBody.showModel = i3 == 1 || i3 == 2;
			modelBiped9.bipedRightArm.showModel = i3 == 1;
			modelBiped9.bipedLeftArm.showModel = i3 == 1;
			modelBiped9.bipedRightLeg.showModel = i3 == 2 || i3 == 3;
			modelBiped9.bipedLeftLeg.showModel = i3 == 2 || i3 == 3;
			this.setRenderPassModel(modelBiped9);
			return true;
		} else {
			return false;
		}
	}

	public final void a(EntityLiving entityLiving1, float f2, float f3, float f4, float f5, float f6) {
		this.renderPlayer((EntityPlayer)entityLiving1, f2, f3, f4, f5, f6);
	}

	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		this.renderPlayer((EntityPlayer)entity, f2, f3, f4, f5, f6);
	}
}
