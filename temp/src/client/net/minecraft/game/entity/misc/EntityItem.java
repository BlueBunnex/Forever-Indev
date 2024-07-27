package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;

public class EntityItem extends Entity {
	public ItemStack item;
	private int unknownEntityItemInt;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World world, float f2, float f3, float f4, ItemStack item) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(f2, f3, f4);
		this.item = item;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.motionY = 0.2F;
		this.motionZ = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.canTriggerWalking = false;
	}

	public EntityItem(World world1) {
		super(world1);
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.04F;
		if(this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY, (int)this.posZ) == Material.lava) {
			this.motionY = 0.2F;
			this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		float f4 = this.posZ;
		float f3 = this.posY;
		float f2 = this.posX;
		int i5 = (int)f2;
		int i6 = (int)f3;
		int i7 = (int)f4;
		f2 -= (float)i5;
		f3 -= (float)i6;
		f4 -= (float)i7;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(i5, i6, i7)]) {
			boolean z8 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5 - 1, i6, i7)];
			boolean z9 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5 + 1, i6, i7)];
			boolean z10 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5, i6 - 1, i7)];
			boolean z11 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5, i6 + 1, i7)];
			boolean z12 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5, i6, i7 - 1)];
			boolean z13 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(i5, i6, i7 + 1)];
			byte b14 = -1;
			float f15 = 9999.0F;
			if(z8 && f2 < 9999.0F) {
				f15 = f2;
				b14 = 0;
			}

			if(z9 && 1.0F - f2 < f15) {
				f15 = 1.0F - f2;
				b14 = 1;
			}

			if(z10 && f3 < f15) {
				f15 = f3;
				b14 = 2;
			}

			if(z11 && 1.0F - f3 < f15) {
				f15 = 1.0F - f3;
				b14 = 3;
			}

			if(z12 && f4 < f15) {
				f15 = f4;
				b14 = 4;
			}

			if(z13 && 1.0F - f4 < f15) {
				b14 = 5;
			}

			f2 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(b14 == 0) {
				this.motionX = -f2;
			}

			if(b14 == 1) {
				this.motionX = f2;
			}

			if(b14 == 2) {
				this.motionY = -f2;
			}

			if(b14 == 3) {
				this.motionY = f2;
			}

			if(b14 == 4) {
				this.motionZ = -f2;
			}

			if(b14 == 5) {
				this.motionZ = f2;
			}
		}

		boolean z10000 = false;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98F;
		this.motionY *= 0.98F;
		this.motionZ *= 0.98F;
		if(this.onGround) {
			this.motionX *= 0.7F;
			this.motionZ *= 0.7F;
			this.motionY *= -0.5F;
		}

		++this.unknownEntityItemInt;
		++this.age;
		if(this.age >= 6000) {
			this.setEntityDead();
		}

	}

	protected final void dealFireDamage(int i1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	public final boolean attackEntityFrom(Entity entity, int i2) {
		this.health -= i2;
		if(this.health <= 0) {
			this.setEntityDead();
		}

		return false;
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setShort("Health", (byte)this.health);
		nbtTagCompound.setShort("Age", (short)this.age);
		nbtTagCompound.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		this.health = nbtTagCompound.getShort("Health") & 255;
		this.age = nbtTagCompound.getShort("Age");
		nbtTagCompound = nbtTagCompound.getCompoundTag("Item");
		this.item = new ItemStack(nbtTagCompound);
	}

	protected final String getEntityString() {
		return "Item";
	}

	public final void onCollideWithPlayer(EntityPlayer player) {
		if(this.delayBeforeCanPickup == 0 && player.inventory.storePartialItemStack(this.item)) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this);
			this.setEntityDead();
		}

	}
}