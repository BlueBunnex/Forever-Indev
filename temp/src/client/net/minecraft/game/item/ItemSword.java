package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public final class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int itemID, int damage) {
		super(itemID);
		this.maxStackSize = 1;
		this.maxDamage = 32 << damage;
		this.weaponDamage = 4 + (damage << 1);
	}

	public final float getStrVsBlock(Block block) {
		return 1.5F;
	}

	public final void hitEntity(ItemStack itemStack) {
		itemStack.damageItem(1);
	}

	public final void onBlockDestroyed(ItemStack itemStack) {
		itemStack.damageItem(2);
	}

	public final int getDamageVsEntity() {
		return this.weaponDamage;
	}
}