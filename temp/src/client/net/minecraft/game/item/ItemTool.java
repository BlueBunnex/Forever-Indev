package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;

	public ItemTool(int itemID, int baseDamage, int damage, Block[] blocksEffected) {
		super(itemID);
		this.blocksEffectiveAgainst = blocksEffected;
		this.maxStackSize = 1;
		this.maxDamage = 32 << damage;
		this.efficiencyOnProperMaterial = (float)(damage + 1 << 1);
		this.damageVsEntity = baseDamage + damage;
	}

	public final float getStrVsBlock(Block block) {
		for(int i2 = 0; i2 < this.blocksEffectiveAgainst.length; ++i2) {
			if(this.blocksEffectiveAgainst[i2] == block) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public final void hitEntity(ItemStack itemStack) {
		itemStack.damageItem(2);
	}

	public final void onBlockDestroyed(ItemStack itemStack) {
		itemStack.damageItem(1);
	}

	public final int getDamageVsEntity() {
		return this.damageVsEntity;
	}
}