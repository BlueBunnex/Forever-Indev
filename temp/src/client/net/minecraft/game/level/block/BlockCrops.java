package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;

public final class BlockCrops extends BlockFlower {
	protected BlockCrops(int i1, int i2) {
		super(59, 88);
		this.blockIndexInTexture = 88;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return blockID == Block.tilledField.blockID;
	}

	public final void updateTick(World world, int xCoord, int yCoord, int zCoord, Random random) {
		super.updateTick(world, xCoord, yCoord, zCoord, random);
		byte b6;
		if(world.getBlockLightValue(xCoord, yCoord + 1, zCoord) >= 9 && (b6 = world.getBlockMetadata(xCoord, yCoord, zCoord)) < 7) {
			int i11 = zCoord;
			int i10 = yCoord;
			int i9 = xCoord;
			World world8 = world;
			float f12 = 1.0F;
			int i13 = world.getBlockId(xCoord, yCoord, zCoord - 1);
			int i14 = world.getBlockId(xCoord, yCoord, zCoord + 1);
			int i15 = world.getBlockId(xCoord - 1, yCoord, zCoord);
			int i16 = world.getBlockId(xCoord + 1, yCoord, zCoord);
			int i17 = world.getBlockId(xCoord - 1, yCoord, zCoord - 1);
			int i18 = world.getBlockId(xCoord + 1, yCoord, zCoord - 1);
			int i19 = world.getBlockId(xCoord + 1, yCoord, zCoord + 1);
			int i20 = world.getBlockId(xCoord - 1, yCoord, zCoord + 1);
			boolean z23 = i15 == this.blockID || i16 == this.blockID;
			boolean z22 = i13 == this.blockID || i14 == this.blockID;
			boolean z7 = i17 == this.blockID || i18 == this.blockID || i19 == this.blockID || i20 == this.blockID;

			for(i14 = xCoord - 1; i14 <= i9 + 1; ++i14) {
				for(i16 = i11 - 1; i16 <= i11 + 1; ++i16) {
					i17 = world8.getBlockId(i14, i10 - 1, i16);
					float f24 = 0.0F;
					if(i17 == Block.tilledField.blockID) {
						f24 = 1.0F;
						if(world8.getBlockMetadata(i14, i10 - 1, i16) > 0) {
							f24 = 3.0F;
						}
					}

					if(i14 != i9 || i16 != i11) {
						f24 /= 4.0F;
					}

					f12 += f24;
				}
			}

			if(z7 || z23 && z22) {
				f12 /= 2.0F;
			}

			if(random.nextInt((int)(100.0F / f12)) == 0) {
				int i21 = b6 + 1;
				world.setBlockMetadata(xCoord, yCoord, zCoord, i21);
			}
		}

	}

	public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i2 < 0) {
			i2 = 7;
		}

		return this.blockIndexInTexture + i2;
	}

	public final int getRenderType() {
		return 6;
	}

	public final void onBlockDestroyedByPlayer(World world, int xCoord, int yCoord, int zCoord, int i5) {
		super.onBlockDestroyedByPlayer(world, xCoord, yCoord, zCoord, i5);

		for(int i6 = 0; i6 < 3; ++i6) {
			if(world.random.nextInt(15) <= i5) {
				float f7 = world.random.nextFloat() * 0.7F + 0.15F;
				float f8 = world.random.nextFloat() * 0.7F + 0.15F;
				float f9 = world.random.nextFloat() * 0.7F + 0.15F;
				EntityItem entityItem10;
				(entityItem10 = new EntityItem(world, (float)xCoord + f7, (float)yCoord + f8, (float)zCoord + f9, new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityItem10);
			}
		}

	}

	public final int idDropped(int i1, Random random) {
		System.out.println("Get resource: " + i1);
		return i1 == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public final int quantityDropped(Random random) {
		return 1;
	}
}