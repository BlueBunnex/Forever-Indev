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
		
		// TODO learn what these GL commands actually do,
		//      and then clean this entire function up
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
			
			jump: {
				// render item background (armor slots)
				if (itemStack == null) {
					
					int background = slot.getBackgroundIconIndex();
					
					if (background >= 0) {
						GL11.glDisable(GL11.GL_LIGHTING);
						RenderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
						this.drawTexturedModalRect(x, y, background % 16 << 4, background / 16 << 4, 16, 16);
						GL11.glEnable(GL11.GL_LIGHTING);
						
						break jump;
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
		
		// render foreground layer
		//GL11.glDisable(GL11.GL_NORMALIZE);
		
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		this.drawGuiContainerForegroundLayer(); // I suspect this function can call enableStandardItemLighting
		
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
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
			if (itemStack != null && this.heldItem == null) {

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
		
		// render held item on top of everything
		if (this.heldItem != null) {
			
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F); // so block item light comes from above
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // so item items don't render dark
			RenderHelper.enableStandardItemLighting(); // so block items don't render dark...?
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glTranslatef(0.0F, 0.0F, 32.0F); // so blocks don't clip into other block items
			
			int x = mouseX - 8,
				y = mouseY - 8;
			
			itemRenderer.renderItemIntoGUI(this.mc.renderEngine, this.heldItem, x, y);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.heldItem, x, y);
			
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
		
		// make sure standard item lighting is disabled so the sky doesn't render weird
		RenderHelper.disableStandardItemLighting();
	}

	protected void drawGuiContainerForegroundLayer() {}

	protected abstract void drawGuiContainerBackgroundLayer();

	protected final void drawSlotInventory(int mouseX, int mouseY, int mouseClick) {
		
		// account for window offset that happens when drawing
		int cornerX = (this.width - this.xSize) / 2;
		int cornerY = (this.height - this.ySize) / 2;
		super.drawSlotInventory(mouseX - cornerX, mouseY - cornerY, mouseClick);
		
		// mouse click events
		if (mouseClick == 0 || mouseClick == 1) {
			
			int var4 = mouseX;
			int var6 = mouseY;
			GuiContainer var5 = this;
			int var7 = 0;

			// find clicked slot, if any
			Slot clickedSlot;
			while (true) {
				if(var7 >= var5.inventorySlots.size()) {
					clickedSlot = null;
					break;
				}

				Slot var8 = var5.inventorySlots.get(var7);
				if(var8.isAtCursorPos(var4, var6)) {
					clickedSlot = var8;
					break;
				}

				++var7;
			}

			// interacting with a slot
			if (clickedSlot != null) {
				
				this.heldItem = clickedSlot.onClickedWithHeldStack(this.heldItem, mouseClick);
				
			// dropping
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
