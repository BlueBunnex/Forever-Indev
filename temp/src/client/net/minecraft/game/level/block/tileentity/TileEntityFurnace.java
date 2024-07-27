package net.minecraft.game.level.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

public final class TileEntityFurnace extends TileEntity implements IInventory {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private int furnaceBurnTime = 0;
	private int currentItemBurnTime = 0;
	private int furnaceCookTime = 0;

	public final int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	public final ItemStack getStackInSlot(int i1) {
		return this.furnaceItemStacks[i1];
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		if(this.furnaceItemStacks[i1] != null) {
			ItemStack itemStack3;
			if(this.furnaceItemStacks[i1].stackSize <= i2) {
				itemStack3 = this.furnaceItemStacks[i1];
				this.furnaceItemStacks[i1] = null;
				return itemStack3;
			} else {
				itemStack3 = this.furnaceItemStacks[i1].splitStack(i2);
				if(this.furnaceItemStacks[i1].stackSize == 0) {
					this.furnaceItemStacks[i1] = null;
				}

				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack) {
		this.furnaceItemStacks[i1] = itemStack;
		if(itemStack != null && itemStack.stackSize > 64) {
			itemStack.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagList nBTTagList2 = nbtTagCompound.getTagList("Items");
		this.furnaceItemStacks = new ItemStack[this.furnaceItemStacks.length];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4;
			byte b5;
			if((b5 = (nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3)).getByte("Slot")) >= 0 && b5 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b5] = new ItemStack(nBTTagCompound4);
			}
		}

		this.furnaceBurnTime = nbtTagCompound.getShort("BurnTime");
		this.furnaceCookTime = nbtTagCompound.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
		System.out.println("Lit: " + this.furnaceBurnTime + "/" + this.currentItemBurnTime);
	}

	public final void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
		nbtTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
		nbtTagCompound.setString("id", "Furnace");
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.furnaceItemStacks.length; ++i3) {
			if(this.furnaceItemStacks[i3] != null) {
				NBTTagCompound nBTTagCompound4;
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				this.furnaceItemStacks[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nbtTagCompound.setTag("Items", nBTTagList2);
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final int getCookProgressScaled(int i1) {
		return this.furnaceCookTime * 24 / 200;
	}

	public final int getBurnTimeRemainingScaled(int i1) {
		return this.furnaceBurnTime * 12 / this.currentItemBurnTime;
	}

	public final boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public final void updateEntity() {
		boolean z1 = this.furnaceBurnTime > 0;
		if(this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if(this.furnaceBurnTime == 0 && this.canSmelt()) {
			this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
			if(this.furnaceBurnTime > 0 && this.furnaceItemStacks[1] != null) {
				--this.furnaceItemStacks[1].stackSize;
				if(this.furnaceItemStacks[1].stackSize == 0) {
					this.furnaceItemStacks[1] = null;
				}
			}
		}

		if(this.isBurning() && this.canSmelt()) {
			++this.furnaceCookTime;
			if(this.furnaceCookTime == 200) {
				this.furnaceCookTime = 0;
				if(this.canSmelt()) {
					int i3 = smeltItem(this.furnaceItemStacks[0].getItem().shiftedIndex);
					if(this.furnaceItemStacks[2] == null) {
						this.furnaceItemStacks[2] = new ItemStack(i3, 1);
					} else if(this.furnaceItemStacks[2].itemID == i3) {
						++this.furnaceItemStacks[2].stackSize;
					}

					--this.furnaceItemStacks[0].stackSize;
					if(this.furnaceItemStacks[0].stackSize <= 0) {
						this.furnaceItemStacks[0] = null;
					}
				}
			}
		} else {
			this.furnaceCookTime = 0;
		}

		if(z1 != this.furnaceBurnTime > 0) {
			boolean z10000 = this.furnaceBurnTime > 0;
			int i5 = this.zCoord;
			int i4 = this.yCoord;
			int i8 = this.xCoord;
			World world9 = this.worldObj;
			boolean z2 = z10000;
			byte b6 = world9.getBlockMetadata(i8, i4, i5);
			TileEntity tileEntity7 = world9.getBlockTileEntity(i8, i4, i5);
			if(z2) {
				world9.setBlockWithNotify(i8, i4, i5, Block.stoneOvenActive.blockID);
			} else {
				world9.setBlockWithNotify(i8, i4, i5, Block.stoneOvenIdle.blockID);
			}

			world9.setBlockMetadata(i8, i4, i5, b6);
			world9.setBlockTileEntity(i8, i4, i5, tileEntity7);
		}

	}

	private boolean canSmelt() {
		int i1;
		return this.furnaceItemStacks[0] == null ? false : ((i1 = smeltItem(this.furnaceItemStacks[0].getItem().shiftedIndex)) < 0 ? false : (this.furnaceItemStacks[2] == null ? true : (this.furnaceItemStacks[2].itemID != i1 ? false : (this.furnaceItemStacks[2].stackSize < 64 ? true : this.furnaceItemStacks[2].stackSize < Item.itemsList[i1].getItemStackLimit()))));
	}

	private static int smeltItem(int i0) {
		return i0 == Block.oreIron.blockID ? Item.ingotIron.shiftedIndex : (i0 == Block.oreGold.blockID ? Item.ingotGold.shiftedIndex : (i0 == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (i0 == Block.sand.blockID ? Block.glass.blockID : (i0 == Item.porkRaw.shiftedIndex ? Item.porkCooked.shiftedIndex : (i0 == Block.cobblestone.blockID ? Block.stone.blockID : -1)))));
	}

	private static int getItemBurnTime(ItemStack itemStack) {
		int itemStack1;
		return itemStack == null ? 0 : ((itemStack1 = itemStack.getItem().shiftedIndex) < 256 && Block.blocksList[itemStack1].material == Material.wood ? 300 : (itemStack1 == Item.stick.shiftedIndex ? 100 : (itemStack1 == Item.coal.shiftedIndex ? 1600 : 0)));
	}
}