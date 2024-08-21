package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class RenderPlayer extends RenderLiving {
    private final ModelBiped modelBipedMain = (ModelBiped) this.mainModel;
    private final ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
    private final ModelBiped modelArmor = new ModelBiped(0.5F);
    private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

    public RenderPlayer() {
        super(new ModelBiped(0.0F), 0.5F);
    }

    private void renderPlayer(EntityPlayer player, float var2, float var3, float var4, float var5, float var6) {
        super.a(player, var2, var3 - player.yOffset, var4, var5, var6);
    }

    public final void drawFirstPersonHand() {
        this.modelBipedMain.bipedRightArm.render(1.0F);
    }

    protected final boolean shouldRenderPass(EntityLiving var1, int var2) {
        EntityPlayer player = (EntityPlayer) var1;
        int armorSlot = var2;
        int slotIndex = 3 - armorSlot;
        InventoryPlayer inventory = player.inventory;
        ItemStack armorItem = inventory.armorInventory[slotIndex];
        if (armorItem != null) {
            Item item = armorItem.getItem();
            if (item instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) item;
                this.loadTexture("/armor/" + armorFilenamePrefix[armor.renderIndex] + "_" + (armorSlot == 2 ? 2 : 1) + ".png");
                ModelBiped model = armorSlot == 2 ? this.modelArmor : this.modelArmorChestplate;
                model.bipedHead.showModel = armorSlot == 0;
                model.bipedHeadwear.showModel = armorSlot == 0;
                model.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
                model.bipedRightArm.showModel = armorSlot == 1;
                model.bipedLeftArm.showModel = armorSlot == 1;
                model.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
                model.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
                this.setRenderPassModel(model);
                return true;
            }
        }
        return false;
    }

    public final void a(EntityLiving var1, float var2, float var3, float var4, float var5, float var6) {
        this.renderPlayer((EntityPlayer) var1, var2, var3, var4, var5, var6);
    }

    public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
        this.renderPlayer((EntityPlayer) var1, var2, var3, var4, var5, var6);
    }
}
