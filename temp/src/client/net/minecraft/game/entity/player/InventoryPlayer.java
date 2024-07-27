package net.minecraft.game.entity.player;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer implements IInventory {
	public ItemStack[] mainInventory = new ItemStack[36];
	public ItemStack[] armorInventory = new ItemStack[4];
	public int currentItem = 0;
	private EntityPlayer player;

	public InventoryPlayer(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}

	public final ItemStack getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	private int getInventorySlotContainItem(int i1) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(this.mainInventory[i2] != null && this.mainInventory[i2].itemID == i1) {
				return i2;
			}
		}

		return -1;
	}

	private int storeItemStack() {
		for(int i1 = 0; i1 < this.mainInventory.length; ++i1) {
			if(this.mainInventory[i1] == null) {
				return i1;
			}
		}

		return -1;
	}

	public final void getFirstEmptyStack(int i1) {
		if((i1 = this.getInventorySlotContainItem(i1)) >= 0 && i1 < 9) {
			this.currentItem = i1;
		}
	}

	public final boolean consumeInventoryItem(int i1) {
		if((i1 = this.getInventorySlotContainItem(i1)) < 0) {
			return false;
		} else {
			if(--this.mainInventory[i1].stackSize <= 0) {
				this.mainInventory[i1] = null;
			}

			return true;
		}
	}

	public final boolean storePartialItemStack(ItemStack itemStack) {
		if(itemStack.itemDamage == 0) {
			int i4 = itemStack.stackSize;
			int i3 = itemStack.itemID;
			int i6 = i3;
			InventoryPlayer inventoryPlayer5 = this;
			int i7 = 0;

			int i10001;
			while(true) {
				if(i7 >= inventoryPlayer5.mainInventory.length) {
					i10001 = -1;
					break;
				}

				if(inventoryPlayer5.mainInventory[i7] != null && inventoryPlayer5.mainInventory[i7].itemID == i6 && inventoryPlayer5.mainInventory[i7].stackSize < inventoryPlayer5.mainInventory[i7].getItem().getItemStackLimit() && inventoryPlayer5.mainInventory[i7].stackSize < 64) {
					i10001 = i7;
					break;
				}

				++i7;
			}

			int i9 = i10001;
			if(i10001 < 0) {
				i9 = this.storeItemStack();
			}

			if(i9 < 0) {
				i10001 = i4;
			} else {
				if(this.mainInventory[i9] == null) {
					this.mainInventory[i9] = new ItemStack(i3, 0);
				}

				i3 = i4;
				if(i4 > this.mainInventory[i9].getItem().getItemStackLimit() - this.mainInventory[i9].stackSize) {
					i3 = this.mainInventory[i9].getItem().getItemStackLimit() - this.mainInventory[i9].stackSize;
				}

				if(i3 > 64 - this.mainInventory[i9].stackSize) {
					i3 = 64 - this.mainInventory[i9].stackSize;
				}

				if(i3 == 0) {
					i10001 = i4;
				} else {
					i4 -= i3;
					this.mainInventory[i9].stackSize += i3;
					this.mainInventory[i9].animationsToGo = 5;
					i10001 = i4;
				}
			}

			itemStack.stackSize = i10001;
			if(itemStack.stackSize == 0) {
				return true;
			}
		}

		int i2;
		if((i2 = this.storeItemStack()) >= 0) {
			this.mainInventory[i2] = itemStack;
			this.mainInventory[i2].animationsToGo = 5;
			return true;
		} else {
			return false;
		}
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(i1 >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			i1 -= this.mainInventory.length;
		}

		if(itemStack3[i1] != null) {
			ItemStack itemStack4;
			if(itemStack3[i1].stackSize <= i2) {
				itemStack4 = itemStack3[i1];
				itemStack3[i1] = null;
				return itemStack4;
			} else {
				itemStack4 = itemStack3[i1].splitStack(i2);
				if(itemStack3[i1].stackSize == 0) {
					itemStack3[i1] = null;
				}

				return itemStack4;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack) {
		ItemStack[] itemStack3 = this.mainInventory;
		if(i1 >= this.mainInventory.length) {
			itemStack3 = this.armorInventory;
			i1 -= this.mainInventory.length;
		}

		itemStack3[i1] = itemStack;
	}

	public final int getSizeInventory() {
		return this.mainInventory.length + 4;
	}

	public final ItemStack getStackInSlot(int i1) {
		ItemStack[] itemStack2 = this.mainInventory;
		if(i1 >= this.mainInventory.length) {
			itemStack2 = this.armorInventory;
			i1 -= this.mainInventory.length;
		}

		return itemStack2[i1];
	}

	public final String getInvName() {
		return "Inventory";
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final int getPlayerArmorValue() {
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;

		for(int i4 = 0; i4 < this.armorInventory.length; ++i4) {
			if(this.armorInventory[i4] != null && this.armorInventory[i4].getItem() instanceof ItemArmor) {
				int i5 = this.armorInventory[i4].isItemStackDamageable();
				int i6 = this.armorInventory[i4].itemDamage;
				i6 = i5 - i6;
				i2 += i6;
				i3 += i5;
				i5 = ((ItemArmor)this.armorInventory[i4].getItem()).damageReduceAmount;
				i1 += i5;
			}
		}

		if(i3 == 0) {
			return 0;
		} else {
			return (i1 - 1) * i2 / i3 + 1;
		}
	}
}