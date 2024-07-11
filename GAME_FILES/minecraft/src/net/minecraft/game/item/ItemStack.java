package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.game.level.block.Block;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDamage;

	public ItemStack(Block block) {
		this(block, 1);
	}

	public ItemStack(Block block, int stackSize) {
		this(block.blockID, stackSize);
	}

	public ItemStack(Item item) {
		this(item, 1);
	}

	public ItemStack(Item item, int stackSize) {
		this(item.shiftedIndex, stackSize);
	}

	public ItemStack(int itemID) {
		this(itemID, 1);
	}

	public ItemStack(int itemID, int stackSize) {
		this.itemID = itemID;
		this.stackSize = stackSize;
	}

	public ItemStack(int itemID, int stackSize, int itemDamage) {
		this.itemID = itemID;
		this.stackSize = stackSize;
		this.itemDamage = itemDamage;
	}

	public ItemStack(NBTTagCompound nbt) {
		this.stackSize = 0;
		this.itemID = nbt.getShort("id");
		this.stackSize = nbt.getByte("Count");
		this.itemDamage = nbt.getShort("Damage");
	}
	
	public String getName() {
		return Item.itemsList[itemID].name;
	}

	public final ItemStack splitStack(int amount) {
		this.stackSize -= amount;
		return new ItemStack(this.itemID, amount, this.itemDamage);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("id", (short) this.itemID);
		nbt.setByte("Count", (byte) this.stackSize);
		nbt.setShort("Damage", (short) this.itemDamage);
		
		return nbt;
	}

	public final int isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	/**
	 * 
	 * @param damage negative values deal damage, positive repair
	 */
	public final void damageItem(int damage) {
		this.itemDamage += damage;
		
		if (this.itemDamage > this.isItemStackDamageable()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
}
