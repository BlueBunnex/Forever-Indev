package net.minecraft.client.gui.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {
	
	private static RenderItem itemRenderer = new RenderItem();
	private ItemStack heldItem = null;
	protected int xSize = 176;
	protected int ySize = 166;
	protected List<Slot> inventorySlots = new ArrayList<Slot>();

	public void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		
		int cornerX = (this.width - this.xSize) / 2;
		int cornerY = (this.height - this.ySize) / 2;
		
		this.drawGuiContainerBackgroundLayer();
		
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) cornerX, (float) cornerY, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);

		Slot hovered = null;
		
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			
			Slot slot = this.inventorySlots.get(i);
			
			int x = slot.xPos;
			int y = slot.yPos;
			
			ItemStack itemStack = slot.inventory.getStackInSlot(slot.slotIndex);
			
			label24: {
				// render item background (armor slots)
				if (itemStack == null) {
					
					int background = slot.getBackgroundIconIndex();
					
					if (background >= 0) {
						GL11.glDisable(GL11.GL_LIGHTING);
						RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
						this.drawTexturedModalRect(x, y, background % 16 << 4, background / 16 << 4, 16, 16);
						GL11.glEnable(GL11.GL_LIGHTING);
						break label24;
					}
				}

				// render item
				itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack, x, y);
				itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, itemStack, x, y);
			}

			if (slot.isAtCursorPos(mouseX, mouseY))
				hovered = slot;
		}
		
		// draw buttons
		RenderHelper.disableStandardItemLighting();
		super.drawScreen(mouseX - cornerX, mouseY - cornerY);
		
		// render hovered slot glow and tooltip
		if (hovered != null) {
			
			int x = hovered.xPos;
			int y = hovered.yPos;
			
			ItemStack itemStack = hovered.inventory.getStackInSlot(hovered.slotIndex);
			
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			// highlight hovered slot
			drawRect(x, y, x + 16, y + 16, -2130706433);
			
			// show tooltip for hovered item
			if (itemStack != null) {

				//GL11.glTranslatef(0.0F, 0.0F, 16.0F);
				drawStringWithBackground(
						this.fontRenderer,
						itemStack.getName() + " (#" + itemStack.getItem().shiftedIndex + ")",
						mouseX - cornerX + 6,
						mouseY - cornerY - 6,
						itemStack.getItem().getRarity().color
				);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		// render held item
		if (this.heldItem != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			
			int x = mouseX - cornerX - 8,
				y = mouseY - cornerY - 8;
			
			itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.heldItem, x, y);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.heldItem, x, y);
		}

		// not sure what this does
		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {}

	protected abstract void drawGuiContainerBackgroundLayer();

	protected final void drawSlotInventory(int mouseX, int mouseY, int mouseClick) {
		
		// account for window offset that happens when drawing
		int cornerX = (this.width - this.xSize) / 2;
		int cornerY = (this.height - this.ySize) / 2;
		super.drawSlotInventory(mouseX - cornerX, mouseY - cornerY, mouseClick);
		
		// item slot stuff
		if (mouseClick == 0 || mouseClick == 1) {
			int var4 = mouseX;
			int var6 = mouseY;
			GuiContainer var5 = this;
			int var7 = 0;

			// find clicked slot, if any
			Slot clicked;
			while (true) {
				if(var7 >= var5.inventorySlots.size()) {
					clicked = null;
					break;
				}

				Slot var8 = var5.inventorySlots.get(var7);
				if(var8.isAtCursorPos(var4, var6)) {
					clicked = var8;
					break;
				}

				++var7;
			}

			Slot var11 = clicked;
			if (var11 != null) {
				ItemStack var12 = var11.inventory.getStackInSlot(var11.slotIndex);
				if(var12 == null && this.heldItem == null) {
					return;
				}

				if(var12 != null && this.heldItem == null) {
					var6 = mouseClick == 0 ? var12.stackSize : (var12.stackSize + 1) / 2;
					this.heldItem = var11.inventory.decrStackSize(var11.slotIndex, var6);
					if(var12.stackSize == 0) {
						var11.putStack((ItemStack)null);
					}

					var11.onPickupFromSlot();
				} else if(var12 == null && this.heldItem != null && var11.isItemValid(this.heldItem)) {
					var6 = mouseClick == 0 ? this.heldItem.stackSize : 1;
					if(var6 > var11.inventory.getInventoryStackLimit()) {
						var6 = var11.inventory.getInventoryStackLimit();
					}

					var11.putStack(this.heldItem.splitStack(var6));
					if(this.heldItem.stackSize == 0) {
						this.heldItem = null;
					}
				} else {
					if(var12 == null || this.heldItem == null) {
						return;
					}

					ItemStack var9;
					if(!var11.isItemValid(this.heldItem)) {
						if(var12.itemID == this.heldItem.itemID) {
							var9 = this.heldItem;
							if(var9.getItem().getItemStackLimit() > 1) {
								var6 = var12.stackSize;
								if(var6 > 0) {
									int var14 = var6 + this.heldItem.stackSize;
									var9 = this.heldItem;
									if(var14 <= var9.getItem().getItemStackLimit()) {
										this.heldItem.stackSize += var6;
										var12.splitStack(var6);
										if(var12.stackSize == 0) {
											var11.putStack((ItemStack)null);
										}

										var11.onPickupFromSlot();
										return;
									}
								}

								return;
							}
						}

						return;
					}

					if(var12.itemID != this.heldItem.itemID) {
						if(this.heldItem.stackSize > var11.inventory.getInventoryStackLimit()) {
							return;
						}

						var11.putStack(this.heldItem);
						this.heldItem = var12;
					} else {
						if(var12.itemID != this.heldItem.itemID) {
							return;
						}

						if (mouseClick == 0) {
							var6 = this.heldItem.stackSize;
							if(var6 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.heldItem;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.heldItem;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.heldItem.splitStack(var6);
							if(this.heldItem.stackSize == 0) {
								this.heldItem = null;
							}

							var12.stackSize += var6;
						} else {
							if (mouseClick != 1) {
								return;
							}

							var6 = 1;
							if(1 > var11.inventory.getInventoryStackLimit() - var12.stackSize) {
								var6 = var11.inventory.getInventoryStackLimit() - var12.stackSize;
							}

							var9 = this.heldItem;
							if(var6 > var9.getItem().getItemStackLimit() - var12.stackSize) {
								var9 = this.heldItem;
								var6 = var9.getItem().getItemStackLimit() - var12.stackSize;
							}

							this.heldItem.splitStack(var6);
							if(this.heldItem.stackSize == 0) {
								this.heldItem = null;
							}

							var12.stackSize += var6;
						}
					}
				}
				
			} else if (this.heldItem != null) {
				int var13 = (this.width - this.xSize) / 2;
				var6 = (this.height - this.ySize) / 2;
				if(mouseX < var13 || mouseY < var6 || mouseX >= var13 + this.xSize || mouseY >= var6 + this.xSize) {
					EntityPlayerSP var10 = this.mc.thePlayer;
					if (mouseClick == 0) {
						var10.dropPlayerItem(this.heldItem);
						this.heldItem = null;
					}

					if (mouseClick == 1) {
						var10.dropPlayerItem(this.heldItem.splitStack(1));
						if(this.heldItem.stackSize == 0) {
							this.heldItem = null;
						}
					}
				}
			}
		}

	}

	protected final void keyTyped(char var1, int var2) {
		
		if(var2 == 1 || var2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen(null);
		}
	}

	public void onGuiClosed() {
		
		if (this.heldItem != null)
			this.mc.thePlayer.dropPlayerItem(this.heldItem);
	}

	public void guiCraftingItemsCheck() {
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}
}
