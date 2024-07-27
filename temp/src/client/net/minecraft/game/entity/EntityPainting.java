package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class EntityPainting extends Entity {
	private int tickCounter1;
	public int direction;
	private int xPosition;
	private int yPosition;
	private int zPosition;
	public EnumArt art;

	public EntityPainting(World world1) {
		super(world1);
		this.tickCounter1 = 0;
		this.direction = 0;
		this.yOffset = 0.0F;
		this.setSize(0.5F, 0.5F);
	}

	public EntityPainting(World world, int xPosition, int yPosition, int zPosition, int i5) {
		this(world);
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
		ArrayList arrayList7 = new ArrayList();
		EnumArt[] enumArt8;
		yPosition = (enumArt8 = EnumArt.values()).length;

		for(zPosition = 0; zPosition < yPosition; ++zPosition) {
			EnumArt enumArt6 = enumArt8[zPosition];
			this.art = enumArt6;
			this.setDirection(i5);
			if(this.onValidSurface()) {
				arrayList7.add(enumArt6);
			}
		}

		if(arrayList7.size() > 0) {
			this.art = (EnumArt)arrayList7.get(this.rand.nextInt(arrayList7.size()));
		}

		this.setDirection(i5);
	}

	private void setDirection(int direction) {
		this.direction = direction;
		this.prevRotationYaw = this.rotationYaw = (float)(direction * 90);
		float f2 = (float)this.art.sizeX;
		float f3 = (float)this.art.sizeY;
		float f4 = (float)this.art.sizeX;
		if(direction != 0 && direction != 2) {
			f2 = 0.5F;
		} else {
			f4 = 0.5F;
		}

		f2 /= 32.0F;
		f3 /= 32.0F;
		f4 /= 32.0F;
		float f5 = (float)this.xPosition + 0.5F;
		float f6 = (float)this.yPosition + 0.5F;
		float f7 = (float)this.zPosition + 0.5F;
		if(direction == 0) {
			f7 -= 0.5625F;
		}

		if(direction == 1) {
			f5 -= 0.5625F;
		}

		if(direction == 2) {
			f7 += 0.5625F;
		}

		if(direction == 3) {
			f5 += 0.5625F;
		}

		if(direction == 0) {
			f5 -= getArtSize(this.art.sizeX);
		}

		if(direction == 1) {
			f7 += getArtSize(this.art.sizeX);
		}

		if(direction == 2) {
			f5 += getArtSize(this.art.sizeX);
		}

		if(direction == 3) {
			f7 -= getArtSize(this.art.sizeX);
		}

		f6 += getArtSize(this.art.sizeY);
		this.setPosition(f5, f6, f7);
		this.boundingBox = new AxisAlignedBB(f5 - f2, f6 - f3, f7 - f4, f5 + f2, f6 + f3, f7 + f4);
		float direction1 = 0.00625F;
		direction1 = 0.00625F;
		direction1 = 0.00625F;
		AxisAlignedBB direction2 = this.boundingBox;
		f2 = this.boundingBox.minX;
		f3 = direction2.minY;
		f4 = direction2.minZ;
		f5 = direction2.maxX;
		f6 = direction2.maxY;
		direction1 = direction2.maxZ;
		f5 -= 0.00625F;
		f6 -= 0.00625F;
		direction1 -= 0.00625F;
		this.boundingBox = new AxisAlignedBB(f2, f3, f4, f5, f6, direction1);
	}

	private static float getArtSize(int i0) {
		return i0 == 32 ? 0.5F : (i0 == 64 ? 0.5F : 0.0F);
	}

	public final void onEntityUpdate() {
		if(this.tickCounter1++ == 100 && !this.onValidSurface()) {
			this.tickCounter1 = 0;
			this.setEntityDead();
			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		}

	}

	public final boolean onValidSurface() {
		if(this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() > 0) {
			return false;
		} else {
			int i1 = this.art.sizeX / 16;
			int i2 = this.art.sizeY / 16;
			int i3 = this.xPosition;
			int i5 = this.zPosition;
			if(this.direction == 0) {
				i3 = (int)(this.posX - (float)this.art.sizeX / 32.0F);
			}

			if(this.direction == 1) {
				i5 = (int)(this.posZ - (float)this.art.sizeX / 32.0F);
			}

			if(this.direction == 2) {
				i3 = (int)(this.posX - (float)this.art.sizeX / 32.0F);
			}

			if(this.direction == 3) {
				i5 = (int)(this.posZ - (float)this.art.sizeX / 32.0F);
			}

			int i4 = (int)(this.posY - (float)this.art.sizeY / 32.0F);

			int i7;
			for(int i6 = 0; i6 < i1; ++i6) {
				for(i7 = 0; i7 < i2; ++i7) {
					Material material8;
					if(this.direction != 0 && this.direction != 2) {
						material8 = this.worldObj.getBlockMaterial(this.xPosition, i4 + i7, i5 + i6);
					} else {
						material8 = this.worldObj.getBlockMaterial(i3 + i6, i4 + i7, this.zPosition);
					}

					if(!material8.isSolid()) {
						return false;
					}
				}
			}

			List list9 = this.worldObj.entityMap.getEntitiesWithinAABB(this, this.boundingBox);

			for(i7 = 0; i7 < list9.size(); ++i7) {
				if(list9.get(i7) instanceof EntityPainting) {
					return false;
				}
			}

			return true;
		}
	}

	public final boolean canBeCollidedWith() {
		return true;
	}

	public final boolean attackEntityFrom(Entity entity, int i2) {
		this.setEntityDead();
		this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.painting)));
		return true;
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setByte("Dir", (byte)this.direction);
		nbtTagCompound.setString("Motive", this.art.title);
		nbtTagCompound.setInteger("TileX", this.xPosition);
		nbtTagCompound.setInteger("TileY", this.yPosition);
		nbtTagCompound.setInteger("TileZ", this.zPosition);
	}

	protected final String getEntityString() {
		return "Painting";
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		this.direction = nbtTagCompound.getByte("Dir");
		this.xPosition = nbtTagCompound.getInteger("TileX");
		this.yPosition = nbtTagCompound.getInteger("TileY");
		this.zPosition = nbtTagCompound.getInteger("TileZ");
		String string6 = nbtTagCompound.getString("Motive");
		EnumArt[] enumArt2;
		int i3 = (enumArt2 = EnumArt.values()).length;

		for(int i4 = 0; i4 < i3; ++i4) {
			EnumArt enumArt5;
			if((enumArt5 = enumArt2[i4]).title.equals(string6)) {
				this.art = enumArt5;
			}
		}

		if(this.art == null) {
			this.art = EnumArt.Kebab;
		}

		this.setDirection(this.direction);
	}
}