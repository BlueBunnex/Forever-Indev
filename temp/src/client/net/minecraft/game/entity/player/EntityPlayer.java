package net.minecraft.game.entity.player;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;
import net.minecraft.game.level.material.Material;

import util.MathHelper;

public class EntityPlayer extends EntityLiving {
	public InventoryPlayer inventory = new InventoryPlayer(this);
	public byte unusedByte = 0;
	public int getScore = 0;
	public float prevCameraYaw;
	public float cameraYaw;
	private int damageRemainder = 0;

	public EntityPlayer(World world1) {
		super(world1);
		if(world1 != null) {
			world1.playerEntity = this;
			world1.releaseEntitySkin(this);
		}

		this.setPositionAndRotation((float)world1.xSpawn, (float)world1.ySpawn, (float)world1.zSpawn, 0.0F, 0.0F);
		this.yOffset = 1.62F;
		this.health = 20;
		this.fireResistance = 20;
		this.texture = "/char.png";
	}

	public final void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		if(this.worldObj != null) {
			this.worldObj.playerEntity = this;
		}

		this.health = 20;
		this.deathTime = 0;
	}

	public void onLivingUpdate() {
		this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "calm", 0.0F);
		if(this.worldObj.difficultySetting == 0 && this.health < 20 && this.ticksExisted % 20 << 2 == 0) {
			this.heal(1);
		}

		InventoryPlayer inventoryPlayer3 = this.inventory;

		for(int i4 = 0; i4 < inventoryPlayer3.mainInventory.length; ++i4) {
			if(inventoryPlayer3.mainInventory[i4] != null && inventoryPlayer3.mainInventory[i4].animationsToGo > 0) {
				--inventoryPlayer3.mainInventory[i4].animationsToGo;
			}
		}

		this.prevCameraYaw = this.cameraYaw;
		super.onLivingUpdate();
		float f1 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f2 = (float)Math.atan((double)(-this.motionY * 0.2F)) * 15.0F;
		if(f1 > 0.1F) {
			f1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f2 - this.cameraPitch) * 0.8F;
		List list5;
		if(this.health > 0 && (list5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F))) != null) {
			for(int i6 = 0; i6 < list5.size(); ++i6) {
				Entity entity7 = (Entity)list5.get(i6);
				entity7.onCollideWithPlayer(this);
			}
		}

	}

	public final void onDeath(Entity entity) {
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = 0.1F;
		if(entity != null) {
			this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
			this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
		} else {
			this.motionX = this.motionZ = 0.0F;
		}

		this.yOffset = 0.1F;
	}

	public final void setEntityDead() {
	}

	public final void dropPlayerItem(ItemStack itemStack) {
		this.dropPlayerItemWithRandomChoice(itemStack, false);
	}

	public final void dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean z2) {
		if(itemStack != null) {
			EntityItem itemStack1;
			(itemStack1 = new EntityItem(this.worldObj, this.posX, this.posY - 0.3F, this.posZ, itemStack)).delayBeforeCanPickup = 40;
			itemStack1.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F;
			itemStack1.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F;
			itemStack1.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * 0.3F + 0.1F;
			float f3 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
			float f5 = 0.02F * this.rand.nextFloat();
			itemStack1.motionX = (float)((double)itemStack1.motionX + Math.cos((double)f3) * (double)f5);
			itemStack1.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
			itemStack1.motionZ = (float)((double)itemStack1.motionZ + Math.sin((double)f3) * (double)f5);
			this.worldObj.spawnEntityInWorld(itemStack1);
		}
	}

	public final boolean canHarvestBlock(Block block1) {
		Block block2 = block1;
		InventoryPlayer block11 = this.inventory;
		ItemStack block12;
		return block2.material != Material.rock && block2.material != Material.iron ? true : ((block12 = block11.getStackInSlot(block11.currentItem)) != null ? Item.itemsList[block12.itemID].canHarvestBlock(block2) : false);
	}

	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
	}

	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
	}

	protected String getEntityString() {
		return null;
	}

	public void displayGUIChest(IInventory iInventory) {
	}

	public void displayWorkbenchGUI() {
	}

	public void onItemPickup(Entity entity) {
	}

	protected final float getEyeHeight() {
		return 0.12F;
	}

	public final boolean attackEntityFrom(Entity entity, int i2) {
		if(!this.worldObj.survivalWorld) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
				return false;
			} else {
				if(entity instanceof EntityMob || entity instanceof EntityArrow) {
					if(this.worldObj.difficultySetting == 0) {
						i2 = 0;
					}

					if(this.worldObj.difficultySetting == 1) {
						i2 = i2 / 3 + 1;
					}

					if(this.worldObj.difficultySetting == 3) {
						i2 = i2 * 3 / 2;
					}
				}

				int i3 = 25 - this.inventory.getPlayerArmorValue();
				i3 = i2 * i3 + this.damageRemainder;
				int i4 = i2;
				InventoryPlayer inventoryPlayer6 = this.inventory;

				for(int i5 = 0; i5 < inventoryPlayer6.armorInventory.length; ++i5) {
					if(inventoryPlayer6.armorInventory[i5] != null && inventoryPlayer6.armorInventory[i5].getItem() instanceof ItemArmor) {
						inventoryPlayer6.armorInventory[i5].damageItem(i4);
						if(inventoryPlayer6.armorInventory[i5].stackSize == 0) {
							inventoryPlayer6.armorInventory[i5] = null;
						}
					}
				}

				i2 = i3 / 25;
				this.damageRemainder = i3 % 25;
				if(i2 == 0) {
					return false;
				} else {
					return super.attackEntityFrom(entity, i2);
				}
			}
		}
	}

	public void displayGUIFurnace(TileEntityFurnace furnace) {
	}
}