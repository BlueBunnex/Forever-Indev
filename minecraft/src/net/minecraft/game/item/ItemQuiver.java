package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;

public class ItemQuiver extends Item {

    public ItemQuiver(String name, int index) {
        super(name, index);
    }

    // Ensures the item can only stack one at a time
    public int getItemStackLimit() {
        return 1;
    }
}