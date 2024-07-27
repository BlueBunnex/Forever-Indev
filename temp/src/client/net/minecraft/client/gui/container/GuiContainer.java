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
	private ItemStack itemStack = null;
	protected int xSize = 176;
	protected int ySize = 166;
	protected List inventorySlots = new ArrayList();

	public void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		this.drawDefaultBackground();
		int i13 = (this.width - this.xSize) / 2;
		int i4 = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)i13, (float)i4, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_NORMALIZE);

		for(int i5 = 0; i5 < this.inventorySlots.size(); ++i5) {
			Slot slot6 = (Slot)this.inventorySlots.get(i5);
			IInventory iInventory9 = slot6.inventory;
			int i10 = slot6.slotIndex;
			int i11 = slot6.xPos;
			int i12 = slot6.yPos;
			int i8;
			ItemStack itemStack15;
			if((itemStack15 = iInventory9.getStackInSlot(i10)) == null && (i8 = slot6.getBackgroundIconIndex()) >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
				this.drawTexturedModalRect(i11, i12, i8 % 16 << 4, i8 / 16 << 4, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
			} else {
				itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack15, i11, i12);
				itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, itemStack15, i11, i12);
			}

			if(slot6.isAtCursorPos(xSize_lo, ySize_lo)) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int i7 = slot6.xPos;
				int i14 = slot6.yPos;
				drawGradientRect(i7, i14, i7 + 16, i14 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		if(this.itemStack != null) {
			GL11.glTranslatef(0.0F, 0.0F, 32.0F);
			itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.itemStack, xSize_lo - i13 - 8, ySize_lo - i4 - 8);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.itemStack, xSize_lo - i13 - 8, ySize_lo - i4 - 8);
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		this.drawGuiContainerForegroundLayer();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	protected abstract void drawGuiContainerBackgroundLayer();

	protected final void drawSlotInventory(int i1, int i2, int i3) {
		if(i3 == 0 || i3 == 1) {
			int i6 = i2;
			int i4 = i1;
			GuiContainer guiContainer5 = this;
			int i7 = 0;

			Slot slot10000;
			while(true) {
				if(i7 >= guiContainer5.inventorySlots.size()) {
					slot10000 = null;
					break;
				}

				Slot slot8;
				if((slot8 = (Slot)guiContainer5.inventorySlots.get(i7)).isAtCursorPos(i4, i6)) {
					slot10000 = slot8;
					break;
				}

				++i7;
			}

			Slot slot11 = slot10000;
			if(slot10000 == null) {
				if(this.itemStack != null) {
					int i13 = (this.width - this.xSize) / 2;
					i6 = (this.height - this.ySize) / 2;
					if(i1 < i13 || i2 < i6 || i1 >= i13 + this.xSize || i2 >= i6 + this.xSize) {
						EntityPlayerSP entityPlayerSP10 = this.mc.thePlayer;
						if(i3 == 0) {
							entityPlayerSP10.dropPlayerItem(this.itemStack);
							this.itemStack = null;
						}

						if(i3 == 1) {
							entityPlayerSP10.dropPlayerItem(this.itemStack.splitStack(1));
							if(this.itemStack.stackSize == 0) {
								this.itemStack = null;
							}
						}
					}
				}
			} else {
				ItemStack itemStack12;
				if((itemStack12 = slot11.inventory.getStackInSlot(slot11.slotIndex)) != null || this.itemStack != null) {
					if(itemStack12 != null && this.itemStack == null) {
						i6 = i3 == 0 ? itemStack12.stackSize : (itemStack12.stackSize + 1) / 2;
						this.itemStack = slot11.inventory.decrStackSize(slot11.slotIndex, i6);
						if(itemStack12.stackSize == 0) {
							slot11.putStack((ItemStack)null);
						}

						slot11.onPickupFromSlot();
						return;
					}

					if(itemStack12 == null && this.itemStack != null && slot11.isItemValid(this.itemStack)) {
						if((i6 = i3 == 0 ? this.itemStack.stackSize : 1) > slot11.inventory.getInventoryStackLimit()) {
							i6 = slot11.inventory.getInventoryStackLimit();
						}

						slot11.putStack(this.itemStack.splitStack(i6));
						if(this.itemStack.stackSize == 0) {
							this.itemStack = null;
						}

						return;
					}

					if(itemStack12 != null && this.itemStack != null) {
						ItemStack itemStack9;
						if(slot11.isItemValid(this.itemStack)) {
							if(itemStack12.itemID != this.itemStack.itemID) {
								if(this.itemStack.stackSize <= slot11.inventory.getInventoryStackLimit()) {
									slot11.putStack(this.itemStack);
									this.itemStack = itemStack12;
									return;
								}
							} else if(itemStack12.itemID == this.itemStack.itemID) {
								if(i3 == 0) {
									if((i6 = this.itemStack.stackSize) > slot11.inventory.getInventoryStackLimit() - itemStack12.stackSize) {
										i6 = slot11.inventory.getInventoryStackLimit() - itemStack12.stackSize;
									}

									itemStack9 = this.itemStack;
									if(i6 > this.itemStack.getItem().getItemStackLimit() - itemStack12.stackSize) {
										itemStack9 = this.itemStack;
										i6 = this.itemStack.getItem().getItemStackLimit() - itemStack12.stackSize;
									}

									this.itemStack.splitStack(i6);
									if(this.itemStack.stackSize == 0) {
										this.itemStack = null;
									}

									itemStack12.stackSize += i6;
									return;
								}

								if(i3 == 1) {
									i6 = 1;
									if(1 > slot11.inventory.getInventoryStackLimit() - itemStack12.stackSize) {
										i6 = slot11.inventory.getInventoryStackLimit() - itemStack12.stackSize;
									}

									itemStack9 = this.itemStack;
									if(i6 > this.itemStack.getItem().getItemStackLimit() - itemStack12.stackSize) {
										itemStack9 = this.itemStack;
										i6 = this.itemStack.getItem().getItemStackLimit() - itemStack12.stackSize;
									}

									this.itemStack.splitStack(i6);
									if(this.itemStack.stackSize == 0) {
										this.itemStack = null;
									}

									itemStack12.stackSize += i6;
									return;
								}
							}
						} else if(itemStack12.itemID == this.itemStack.itemID) {
							itemStack9 = this.itemStack;
							if(this.itemStack.getItem().getItemStackLimit() > 1) {
								i6 = itemStack12.stackSize;
								if(itemStack12.stackSize > 0) {
									int i14 = i6 + this.itemStack.stackSize;
									itemStack9 = this.itemStack;
									if(i14 <= this.itemStack.getItem().getItemStackLimit()) {
										this.itemStack.stackSize += i6;
										itemStack12.splitStack(i6);
										if(itemStack12.stackSize == 0) {
											slot11.putStack((ItemStack)null);
										}

										slot11.onPickupFromSlot();
									}
								}
							}
						}
					}
				}

				return;
			}
		}

	}

	protected final void keyTyped(char c1, int i2) {
		if(i2 == 1 || i2 == this.mc.options.keyBindInventory.keyCode) {
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void onGuiClosed() {
		if(this.itemStack != null) {
			this.mc.thePlayer.dropPlayerItem(this.itemStack);
		}

	}

	public void guiCraftingItemsCheck() {
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}
}