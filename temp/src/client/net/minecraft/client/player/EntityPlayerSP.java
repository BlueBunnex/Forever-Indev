package net.minecraft.client.player;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.effect.EntityPickupFX;
import net.minecraft.client.gui.container.GuiChest;
import net.minecraft.client.gui.container.GuiCrafting;
import net.minecraft.client.gui.container.GuiFurnace;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.tileentity.TileEntityFurnace;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;

	public EntityPlayerSP(Minecraft minecraft, World world, Session sessionData) {
		super(world);
		this.mc = minecraft;
		if(sessionData != null) {
			this.skinUrl = "http://www.minecraft.net/skin/" + sessionData.username + ".png";
		}

	}

	public final void updatePlayerActionState() {
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public final void onLivingUpdate() {
		this.movementInput.updatePlayerMoveState();
		super.onLivingUpdate();
	}

	protected final void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("Score", this.getScore);
		InventoryPlayer inventoryPlayer10002 = this.inventory;
		NBTTagList nBTTagList2 = new NBTTagList();
		InventoryPlayer inventoryPlayer5 = inventoryPlayer10002;

		int i3;
		NBTTagCompound nBTTagCompound4;
		for(i3 = 0; i3 < inventoryPlayer5.mainInventory.length; ++i3) {
			if(inventoryPlayer5.mainInventory[i3] != null) {
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				inventoryPlayer5.mainInventory[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		for(i3 = 0; i3 < inventoryPlayer5.armorInventory.length; ++i3) {
			if(inventoryPlayer5.armorInventory[i3] != null) {
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)(i3 + 100));
				inventoryPlayer5.armorInventory[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nbtTagCompound.setTag("Inventory", nBTTagList2);
	}

	protected final void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
		this.getScore = nbtTagCompound.getInteger("Score");
		NBTTagList nBTTagList6 = nbtTagCompound.getTagList("Inventory");
		NBTTagList nBTTagList2 = nBTTagList6;
		InventoryPlayer inventoryPlayer7 = this.inventory;
		this.inventory.mainInventory = new ItemStack[36];
		inventoryPlayer7.armorInventory = new ItemStack[4];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4;
			int i5;
			if((i5 = (nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3)).getByte("Slot") & 255) >= 0 && i5 < inventoryPlayer7.mainInventory.length) {
				inventoryPlayer7.mainInventory[i5] = new ItemStack(nBTTagCompound4);
			}

			if(i5 >= 100 && i5 < inventoryPlayer7.armorInventory.length + 100) {
				inventoryPlayer7.armorInventory[i5 - 100] = new ItemStack(nBTTagCompound4);
			}
		}

	}

	protected final String getEntityString() {
		return "LocalPlayer";
	}

	public final void displayGUIChest(IInventory iInventory) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, iInventory));
	}

	public final void displayWorkbenchGUI() {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
	}

	public final void displayGUIFurnace(TileEntityFurnace furnace) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, furnace));
	}

	public final void destroyCurrentEquippedItem() {
		this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
	}

	public final void onItemPickup(Entity entity) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
	}
}