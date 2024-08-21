package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;
import net.minecraft.game.item.enchant.EnchantType;

public final class ItemBow extends Item {

    public ItemBow(String name, int index) {
        super(name, index);
        this.maxStackSize = 1;
    }

    public final ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {

        // Check if the player has a quiver in slot 36
        ItemStack quiverSlot = player.inventory.getStackInSlot(36);

        if (quiverSlot != null && quiverSlot.getItem() == Item.quiver) {
            // If a quiver is present, check if slot 37 contains arrows
            ItemStack arrowSlot = player.inventory.getStackInSlot(37);

            if (arrowSlot != null && arrowSlot.getItem() == Item.arrow && arrowSlot.stackSize > 0) {
                // Consume an arrow from slot 37
                arrowSlot.stackSize--;
                if (arrowSlot.stackSize == 0) {
                    player.inventory.setInventorySlotContents(37, null); // Remove the empty stack
                }

                // Retrieve enchantment levels from the bow
                int detonationLevel = item.enchantLevelOf(EnchantType.detonation);
                int fieryLevel = item.enchantLevelOf(EnchantType.fiery);

                // Shoot the arrow with properties based on enchantments
                world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

                EntityArrow arrow = new EntityArrow(world, player);
                
                // Apply detonation level if present
                if (detonationLevel > 0) {
                    arrow.setDetonationLevel(detonationLevel);
                    arrow.setDetonatesOnImpact(true);
                }

                // Apply fiery effect if present
                if (fieryLevel > 0) {
                    arrow.setFiery(true);
                }

                world.spawnEntityInWorld(arrow);

                return item;
            } else {
                // If no arrows are available in the accessory slot, do nothing
                return item; // Return the item unchanged (no arrows to shoot)
            }
        }

        // If there's no quiver, do not shoot any arrows (skip inventory check)
        return item; // Return the item unchanged (since we rely solely on the accessory slots)
    }
}
