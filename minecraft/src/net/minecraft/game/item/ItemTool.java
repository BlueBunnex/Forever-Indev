package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;

	public ItemTool(String name, int index, int var2, int var3, Block[] blocksEffectiveAgainst) {
		super(name, index);
		
		this.blocksEffectiveAgainst = blocksEffectiveAgainst;
		
		this.maxStackSize = 1;
		this.maxDamage = 32 << var3;
		this.efficiencyOnProperMaterial = (float) (var3 + 1 << 1);
		this.damageVsEntity = var2 + var3;
	}

	public float getStrVsBlock(Block block) {
		
		for (int i = 0; i < this.blocksEffectiveAgainst.length; i++) {
			
			if (this.blocksEffectiveAgainst[i] == block) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public final void hitEntity(ItemStack item) {
		item.damageItem(2);
	}

	public void onBlockDestroyed(ItemStack item) {
		item.damageItem(1);
	}

	public int getDamageVsEntity() {
		return this.damageVsEntity;
	}
}
