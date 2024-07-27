package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.ItemStack;

import org.lwjgl.opengl.GL11;

public final class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	private List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	private int updateCounter = 0;

	public GuiIngame(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public final void renderGameOverlay(float f1) {
		ScaledResolution scaledResolution2;
		int i3 = (scaledResolution2 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
		int i19 = scaledResolution2.getScaledHeight();
		FontRenderer fontRenderer4 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		InventoryPlayer inventoryPlayer5 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(i3 / 2 - 91, i19 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(i3 / 2 - 91 - 1 + inventoryPlayer5.currentItem * 20, i19 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(i3 / 2 - 7, i19 / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean z20 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			z20 = false;
		}

		int i6 = this.mc.thePlayer.health;
		int i7 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int i10;
		int i12;
		if(this.mc.playerController.shouldDrawHUD()) {
			EntityPlayerSP entityPlayerSP8 = this.mc.thePlayer;
			i10 = this.mc.thePlayer.inventory.getPlayerArmorValue();

			int i11;
			int i13;
			for(i11 = 0; i11 < 10; ++i11) {
				i12 = i19 - 32;
				if(i10 > 0) {
					i13 = i3 / 2 + 91 - (i11 << 3) - 9;
					if((i11 << 1) + 1 < i10) {
						this.drawTexturedModalRect(i13, i12, 34, 9, 9, 9);
					}

					if((i11 << 1) + 1 == i10) {
						this.drawTexturedModalRect(i13, i12, 25, 9, 9, 9);
					}

					if((i11 << 1) + 1 > i10) {
						this.drawTexturedModalRect(i13, i12, 16, 9, 9, 9);
					}
				}

				byte b26 = 0;
				if(z20) {
					b26 = 1;
				}

				int i14 = i3 / 2 - 91 + (i11 << 3);
				if(i6 <= 4) {
					i12 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(i14, i12, 16 + b26 * 9, 0, 9, 9);
				if(z20) {
					if((i11 << 1) + 1 < i7) {
						this.drawTexturedModalRect(i14, i12, 70, 0, 9, 9);
					}

					if((i11 << 1) + 1 == i7) {
						this.drawTexturedModalRect(i14, i12, 79, 0, 9, 9);
					}
				}

				if((i11 << 1) + 1 < i6) {
					this.drawTexturedModalRect(i14, i12, 52, 0, 9, 9);
				}

				if((i11 << 1) + 1 == i6) {
					this.drawTexturedModalRect(i14, i12, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfWater()) {
				i11 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				i12 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - i11;

				for(i13 = 0; i13 < i11 + i12; ++i13) {
					if(i13 < i11) {
						this.drawTexturedModalRect(i3 / 2 - 91 + (i13 << 3), i19 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(i3 / 2 - 91 + (i13 << 3), i19 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		for(i10 = 0; i10 < 9; ++i10) {
			int i25 = i3 / 2 - 90 + i10 * 20 + 2;
			int i21 = i19 - 16 - 3;
			ItemStack itemStack22;
			if((itemStack22 = this.mc.thePlayer.inventory.mainInventory[i10]) != null) {
				float f9;
				if((f9 = (float)itemStack22.animationsToGo - f1) > 0.0F) {
					GL11.glPushMatrix();
					float f25 = 1.0F + f9 / 5.0F;
					GL11.glTranslatef((float)(i25 + 8), (float)(i21 + 12), 0.0F);
					GL11.glScalef(1.0F / f25, (f25 + 1.0F) / 2.0F, 1.0F);
					GL11.glTranslatef((float)(-(i25 + 8)), (float)(-(i21 + 12)), 0.0F);
				}

				itemRenderer.renderItemIntoGUI(this.mc.renderEngine, itemStack22, i25, i21);
				if(f9 > 0.0F) {
					GL11.glPopMatrix();
				}

				itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, itemStack22, i25, i21);
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);
		if(this.mc.options.showFPS) {
			fontRenderer4.drawStringWithShadow("Minecraft Indev (" + this.mc.debug + ")", 2, 2, 0xFFFFFF);
			Minecraft minecraft23 = this.mc;
			fontRenderer4.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoRenders(), 2, 12, 0xFFFFFF);
			minecraft23 = this.mc;
			fontRenderer4.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoEntities(), 2, 22, 0xFFFFFF);
			minecraft23 = this.mc;
			fontRenderer4.drawStringWithShadow("P: " + minecraft23.effectRenderer.getStatistics() + ". T: " + minecraft23.theWorld.debugSkylightUpdates(), 2, 32, 0xFFFFFF);
			long j24 = Runtime.getRuntime().maxMemory();
			long j27 = Runtime.getRuntime().totalMemory();
			long j28 = Runtime.getRuntime().freeMemory();
			long j16 = j24 - j28;
			String string18 = "Free memory: " + j16 * 100L / j24 + "% of " + j24 / 1024L / 1024L + "MB";
			drawString(fontRenderer4, string18, i3 - fontRenderer4.getStringWidth(string18) - 2, 2, 14737632);
			string18 = "Allocated memory: " + j27 * 100L / j24 + "% (" + j27 / 1024L / 1024L + "MB)";
			drawString(fontRenderer4, string18, i3 - fontRenderer4.getStringWidth(string18) - 2, 12, 14737632);
		} else {
			fontRenderer4.drawStringWithShadow("Minecraft Indev", 2, 2, 0xFFFFFF);
		}

		for(i12 = 0; i12 < this.chatMessageList.size() && i12 < 10; ++i12) {
			if(((ChatLine)this.chatMessageList.get(i12)).updateCounter < 200) {
				this.chatMessageList.get(i12);
				fontRenderer4.drawStringWithShadow((String)null, 2, i19 - 8 - i12 * 9 - 20, 0xFFFFFF);
			}
		}

	}

	public final void addChatMessage() {
		++this.updateCounter;

		for(int i1 = 0; i1 < this.chatMessageList.size(); ++i1) {
			++((ChatLine)this.chatMessageList.get(i1)).updateCounter;
		}

	}
}
