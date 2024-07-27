package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.level.block.Block;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDamage;

	public ItemStack(Block block) {
		this((Block)block, 1);
	}

	public ItemStack(Block block, int i2) {
		this(block.blockID, i2);
	}

	public ItemStack(Item item) {
		this((Item)item, 1);
	}

	public ItemStack(Item item, int i2) {
		this(item.shiftedIndex, i2);
	}

	public ItemStack(int i1) {
		this(i1, 1);
	}

	public ItemStack(int i1, int i2) {
		this.stackSize = 0;
		this.itemID = i1;
		this.stackSize = i2;
	}

	public ItemStack(int i1, int i2, int i3) {
		this.stackSize = 0;
		this.itemID = i1;
		this.stackSize = i2;
		this.itemDamage = i3;
	}

	public ItemStack(NBTTagCompound nbtTagCompound) {
		this.stackSize = 0;
		this.itemID = nbtTagCompound.getShort("id");
		this.stackSize = nbtTagCompound.getByte("Count");
		this.itemDamage = nbtTagCompound.getShort("Damage");
	}

	public final ItemStack splitStack(int i1) {
		this.stackSize -= i1;
		return new ItemStack(this.itemID, i1, this.itemDamage);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setShort("id", (short)this.itemID);
		nbtTagCompound.setByte("Count", (byte)this.stackSize);
		nbtTagCompound.setShort("Damage", (short)this.itemDamage);
		return nbtTagCompound;
	}

	public final int isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public final void damageItem(int i1) {
		this.itemDamage += i1;
		if(this.itemDamage > this.isItemStackDamageable()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
}