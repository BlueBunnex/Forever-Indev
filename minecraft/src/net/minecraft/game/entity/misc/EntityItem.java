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
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float hoverStart = (float) (Math.random() * Math.PI * 2.0D);

	public EntityItem(World world, float x, float y, float z, ItemStack item) {
		super(world);
		
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		
		this.yOffset = this.height / 2.0F;
		this.rotationYaw = (float) (Math.random() * 360.0D);
		
		this.motionX = (float) (Math.random() * 0.2 - 0.1);
		this.motionY = 0.2F;
		this.motionZ = (float) (Math.random() * 0.2 - 0.1);
		
		this.canTriggerWalking = false;
		
		this.item = item;
	}

	public EntityItem(World world) {
		super(world);
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

		float var4 = this.posZ;
		float var3 = this.posY;
		float var2 = this.posX;
		int var5 = (int)var2;
		int var6 = (int)var3;
		int var7 = (int)var4;
		var2 -= (float)var5;
		var3 -= (float)var6;
		var4 -= (float)var7;
		if(Block.opaqueCubeLookup[this.worldObj.getBlockId(var5, var6, var7)]) {
			boolean var8 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5 - 1, var6, var7)];
			boolean var9 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5 + 1, var6, var7)];
			boolean var10 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5, var6 - 1, var7)];
			boolean var11 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5, var6 + 1, var7)];
			boolean var12 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5, var6, var7 - 1)];
			boolean var13 = !Block.opaqueCubeLookup[this.worldObj.getBlockId(var5, var6, var7 + 1)];
			byte var14 = -1;
			float var15 = 9999.0F;
			if(var8 && var2 < 9999.0F) {
				var15 = var2;
				var14 = 0;
			}

			if(var9 && 1.0F - var2 < var15) {
				var15 = 1.0F - var2;
				var14 = 1;
			}

			if(var10 && var3 < var15) {
				var15 = var3;
				var14 = 2;
			}

			if(var11 && 1.0F - var3 < var15) {
				var15 = 1.0F - var3;
				var14 = 3;
			}

			if(var12 && var4 < var15) {
				var15 = var4;
				var14 = 4;
			}

			if(var13 && 1.0F - var4 < var15) {
				var14 = 5;
			}

			var2 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(var14 == 0) {
				this.motionX = -var2;
			}

			if(var14 == 1) {
				this.motionX = var2;
			}

			if(var14 == 2) {
				this.motionY = -var2;
			}

			if(var14 == 3) {
				this.motionY = var2;
			}

			if(var14 == 4) {
				this.motionZ = -var2;
			}

			if(var14 == 5) {
				this.motionZ = var2;
			}
		}

		boolean var10000 = false;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98F;
		this.motionY *= 0.98F;
		this.motionZ *= 0.98F;
		if(this.onGround) {
			this.motionX *= 0.7F;
			this.motionZ *= 0.7F;
			this.motionY *= -0.5F;
		}

		this.age++;
		
		if(this.age >= 6000)
			this.setEntityDead();

	}

	protected final void dealFireDamage(int var1) {
		this.attackEntityFrom(null, 1);
	}

	public final boolean attackEntityFrom(Entity attacker, int damage) {
		this.health -= damage;
		
		if(this.health <= 0)
			this.setEntityDead();

		return false;
	}

	protected final void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (byte)this.health);
		var1.setShort("Age", (short)this.age);
		var1.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	protected final void readEntityFromNBT(NBTTagCompound var1) {
		this.health = var1.getShort("Health") & 255;
		this.age = var1.getShort("Age");
		var1 = var1.getCompoundTag("Item");
		this.item = new ItemStack(var1);
	}

	protected final String getEntityString() {
		return "Item";
	}

	public final void onCollideWithPlayer(EntityPlayer player) {
		
		if (this.delayBeforeCanPickup == 0 && player.inventory.storePartialItemStack(this.item)) {
			
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(this);
			this.setEntityDead();
		}
	}
}
