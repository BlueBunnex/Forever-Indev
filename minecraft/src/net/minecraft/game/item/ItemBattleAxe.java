package net.minecraft.game.item;

import net.minecraft.game.level.block.Block;
import net.minecraft.game.entity.Entity;

public final class ItemBattleAxe extends ItemTool {
	
	private static Block[] blocksEffectiveAgainst = new Block[] {
			Block.planks,
			Block.bookShelf,
			Block.wood,
			Block.crate
	};

	private float criticalHitChance = 0.2f; // 20% chance for critical hit
	private int bonusDamage = 2; // Additional damage against specific blocks

	public ItemBattleAxe(String name, int index, int var2) {
		super(name, index, 3, var2, blocksEffectiveAgainst);
	}

	@Override
	public final float getStrVsBlock(Block block) {
		for (Block effectiveBlock : blocksEffectiveAgainst) {
			if (block == effectiveBlock) {
				return super.getStrVsBlock(block) + bonusDamage; // Increased damage for effective blocks
			}
		}
		return super.getStrVsBlock(block);
	}

	public final void hitEntity(ItemStack item, Entity entity) {
		boolean isCriticalHit = Math.random() < criticalHitChance; // Critical hit logic
		int damage = isCriticalHit ? getDamageVsEntity() * 2 : getDamageVsEntity();
		// This is where the damage is applied to the entity
		entity.attackThisEntity(null, damage); // Apply damage to the entity like the sword does
		item.damageItem(1); // Decrease the item's durability
	}

	@Override
	public final int getDamageVsEntity() {
		return 7; // Base damage for the battle axe
	}

	@Override
	public final void onBlockDestroyed(ItemStack item) {
		item.damageItem(3); // Custom durability logic for block breaking
	}
}
