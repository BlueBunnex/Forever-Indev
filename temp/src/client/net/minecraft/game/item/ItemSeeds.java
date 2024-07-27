package net.minecraft.game.item;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int itemID, int blockID) {
		super(39);
		this.blockType = blockID;
	}

	public final boolean onItemUse(ItemStack itemStack, World world, int i3, int i4, int i5, int i6) {
		if(i6 != 1) {
			return false;
		} else if(i3 > 0 && i4 > 0 && i5 > 0 && i3 < world.width - 1 && i4 < world.height - 1 && i5 < world.length - 1) {
			if(world.getBlockId(i3, i4, i5) == Block.tilledField.blockID) {
				world.setBlockWithNotify(i3, i4 + 1, i5, this.blockType);
				--itemStack.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}