package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.IInventory;
import net.minecraft.game.InventoryLargeChest;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.tileentity.TileEntity;
import net.minecraft.game.level.block.tileentity.TileEntityChest;
import net.minecraft.game.level.material.Material;

public final class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int blockID) {
		super(54, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public final int getBlockTexture(World world, int xCoord, int yCoord, int zCoord, int blockSide) {
		if(blockSide == 1) {
			return this.blockIndexInTexture - 1;
		} else if(blockSide == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int i6 = world.getBlockId(xCoord, yCoord, zCoord - 1);
			int i7 = world.getBlockId(xCoord, yCoord, zCoord + 1);
			int i8 = world.getBlockId(xCoord - 1, yCoord, zCoord);
			int i9 = world.getBlockId(xCoord + 1, yCoord, zCoord);
			int i10;
			int i11;
			int world1;
			byte xCoord1;
			if(i6 != this.blockID && i7 != this.blockID) {
				if(i8 != this.blockID && i9 != this.blockID) {
					byte b14 = 3;
					if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
						b14 = 3;
					}

					if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
						b14 = 2;
					}

					if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i9]) {
						b14 = 5;
					}

					if(Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i8]) {
						b14 = 4;
					}

					return blockSide == b14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(blockSide != 4 && blockSide != 5) {
					i10 = 0;
					if(i8 == this.blockID) {
						i10 = -1;
					}

					i11 = world.getBlockId(i8 == this.blockID ? xCoord - 1 : xCoord + 1, yCoord, zCoord - 1);
					world1 = world.getBlockId(i8 == this.blockID ? xCoord - 1 : xCoord + 1, yCoord, zCoord + 1);
					if(blockSide == 3) {
						i10 = -1 - i10;
					}

					xCoord1 = 3;
					if((Block.opaqueCubeLookup[i6] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[world1]) {
						xCoord1 = 3;
					}

					if((Block.opaqueCubeLookup[i7] || Block.opaqueCubeLookup[world1]) && !Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i11]) {
						xCoord1 = 2;
					}

					return (blockSide == xCoord1 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(blockSide != 2 && blockSide != 3) {
				i10 = 0;
				if(i6 == this.blockID) {
					i10 = -1;
				}

				i11 = world.getBlockId(xCoord - 1, yCoord, i6 == this.blockID ? zCoord - 1 : zCoord + 1);
				world1 = world.getBlockId(xCoord + 1, yCoord, i6 == this.blockID ? zCoord - 1 : zCoord + 1);
				if(blockSide == 4) {
					i10 = -1 - i10;
				}

				xCoord1 = 5;
				if((Block.opaqueCubeLookup[i8] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[world1]) {
					xCoord1 = 5;
				}

				if((Block.opaqueCubeLookup[i9] || Block.opaqueCubeLookup[world1]) && !Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i11]) {
					xCoord1 = 4;
				}

				return (blockSide == xCoord1 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? this.blockIndexInTexture - 1 : (blockSide == 0 ? this.blockIndexInTexture - 1 : (blockSide == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public final boolean canPlaceBlockAt(World world, int xCoord, int yCoord, int zCoord) {
		int i5 = 0;
		if(world.getBlockId(xCoord - 1, yCoord, zCoord) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(xCoord + 1, yCoord, zCoord) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(xCoord, yCoord, zCoord - 1) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(xCoord, yCoord, zCoord + 1) == this.blockID) {
			++i5;
		}

		return i5 > 1 ? false : (this.isThereANeighborChest(world, xCoord - 1, yCoord, zCoord) ? false : (this.isThereANeighborChest(world, xCoord + 1, yCoord, zCoord) ? false : (this.isThereANeighborChest(world, xCoord, yCoord, zCoord - 1) ? false : !this.isThereANeighborChest(world, xCoord, yCoord, zCoord + 1))));
	}

	private boolean isThereANeighborChest(World world, int xCoord, int yCoord, int zCoord) {
		return world.getBlockId(xCoord, yCoord, zCoord) != this.blockID ? false : (world.getBlockId(xCoord - 1, yCoord, zCoord) == this.blockID ? true : (world.getBlockId(xCoord + 1, yCoord, zCoord) == this.blockID ? true : (world.getBlockId(xCoord, yCoord, zCoord - 1) == this.blockID ? true : world.getBlockId(xCoord, yCoord, zCoord + 1) == this.blockID)));
	}

	public final void onBlockRemoval(World world, int xCoord, int yCoord, int zCoord) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)world.getBlockTileEntity(xCoord, yCoord, zCoord);

		for(int i6 = 0; i6 < tileEntityChest5.getSizeInventory(); ++i6) {
			ItemStack itemStack7;
			if((itemStack7 = tileEntityChest5.getStackInSlot(i6)) != null) {
				float f8 = this.random.nextFloat() * 0.8F + 0.1F;
				float f9 = this.random.nextFloat() * 0.8F + 0.1F;
				float f10 = this.random.nextFloat() * 0.8F + 0.1F;

				while(itemStack7.stackSize > 0) {
					int i11;
					if((i11 = this.random.nextInt(21) + 10) > itemStack7.stackSize) {
						i11 = itemStack7.stackSize;
					}

					itemStack7.stackSize -= i11;
					EntityItem entityItem12;
					(entityItem12 = new EntityItem(world, (float)xCoord + f8, (float)yCoord + f9, (float)zCoord + f10, new ItemStack(itemStack7.itemID, i11, itemStack7.itemDamage))).motionX = (float)this.random.nextGaussian() * 0.05F;
					entityItem12.motionY = (float)this.random.nextGaussian() * 0.05F + 0.2F;
					entityItem12.motionZ = (float)this.random.nextGaussian() * 0.05F;
					world.spawnEntityInWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(world, xCoord, yCoord, zCoord);
	}

	public final boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer player) {
		Object object6 = (TileEntityChest)world.getBlockTileEntity(xCoord, yCoord, zCoord);
		if(world.isBlockNormalCube(xCoord, yCoord + 1, zCoord)) {
			return true;
		} else if(world.getBlockId(xCoord - 1, yCoord, zCoord) == this.blockID && world.isBlockNormalCube(xCoord - 1, yCoord + 1, zCoord)) {
			return true;
		} else if(world.getBlockId(xCoord + 1, yCoord, zCoord) == this.blockID && world.isBlockNormalCube(xCoord + 1, yCoord + 1, zCoord)) {
			return true;
		} else if(world.getBlockId(xCoord, yCoord, zCoord - 1) == this.blockID && world.isBlockNormalCube(xCoord, yCoord + 1, zCoord - 1)) {
			return true;
		} else if(world.getBlockId(xCoord, yCoord, zCoord + 1) == this.blockID && world.isBlockNormalCube(xCoord, yCoord + 1, zCoord + 1)) {
			return true;
		} else {
			if(world.getBlockId(xCoord - 1, yCoord, zCoord) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(xCoord - 1, yCoord, zCoord), (IInventory)object6);
			}

			if(world.getBlockId(xCoord + 1, yCoord, zCoord) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(xCoord + 1, yCoord, zCoord));
			}

			if(world.getBlockId(xCoord, yCoord, zCoord - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(xCoord, yCoord, zCoord - 1), (IInventory)object6);
			}

			if(world.getBlockId(xCoord, yCoord, zCoord + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(xCoord, yCoord, zCoord + 1));
			}

			player.displayGUIChest((IInventory)object6);
			return true;
		}
	}

	protected final TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}