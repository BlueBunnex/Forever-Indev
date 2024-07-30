package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(String name, int index, int var2) {
		super(name, index);
		
		this.maxStackSize = 1;
		this.maxDamage = 32 << var2;
		this.weaponDamage = 4 + (var2 << 1);
	}

	public final float getStrVsBlock(Block block) {
		return 1.5F;
	}

	public final void hitEntity(ItemStack item) {
		item.damageItem(1);
	}

	public final void onBlockDestroyed(ItemStack item) {
		item.damageItem(2);
	}

	public final int getDamageVsEntity() {
		return this.weaponDamage;
	}
}
