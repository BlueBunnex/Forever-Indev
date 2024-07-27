package net.minecraft.client.gui.container;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.IInventory;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.recipe.CraftingManager;

import org.lwjgl.opengl.GL11;

public final class GuiCrafting extends GuiContainer {
	private InventoryCrafting inventoryCrafting = new InventoryCrafting(this, 3, 3);
	private IInventory iInventory = new InventoryCraftResult();

	public GuiCrafting(InventoryPlayer inventoryPlayer1) {
		this.inventorySlots.add(new SlotCrafting(this, this.inventoryCrafting, this.iInventory, 0, 124, 35));

		int i2;
		int i3;
		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 3; ++i3) {
				this.inventorySlots.add(new Slot(this, this.inventoryCrafting, i3 + i2 * 3, 30 + i3 * 18, 17 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 3; ++i2) {
			for(i3 = 0; i3 < 9; ++i3) {
				this.inventorySlots.add(new Slot(this, inventoryPlayer1, i3 + (i2 + 1) * 9, 8 + i3 * 18, 84 + i2 * 18));
			}
		}

		for(i2 = 0; i2 < 9; ++i2) {
			this.inventorySlots.add(new Slot(this, inventoryPlayer1, i2, 8 + i2 * 18, 142));
		}

	}

	public final void onGuiClosed() {
		super.onGuiClosed();

		for(int i1 = 0; i1 < 9; ++i1) {
			ItemStack itemStack2;
			if((itemStack2 = this.inventoryCrafting.getStackInSlot(i1)) != null) {
				this.mc.thePlayer.dropPlayerItem(itemStack2);
			}
		}

	}

	public final void guiCraftingItemsCheck() {
		int[] i1 = new int[9];

		for(int i2 = 0; i2 < 3; ++i2) {
			for(int i3 = 0; i3 < 3; ++i3) {
				int i4 = i2 + i3 * 3;
				ItemStack itemStack5;
				if((itemStack5 = this.inventoryCrafting.getStackInSlot(i4)) == null) {
					i1[i4] = -1;
				} else {
					i1[i4] = itemStack5.itemID;
				}
			}
		}

		this.iInventory.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(i1));
	}

	protected final void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Crafting", 28, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	protected final void drawGuiContainerBackgroundLayer() {
		int i1 = this.mc.renderEngine.getTexture("/gui/crafting.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderEngine.bindTexture(i1);
		i1 = (this.width - this.xSize) / 2;
		int i2 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i1, i2, 0, 0, this.xSize, this.ySize);
	}
}