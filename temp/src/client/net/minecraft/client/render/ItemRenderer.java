package net.minecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.client.render.entity.RenderPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private int swingProgress = 0;
	private boolean itemSwingState = false;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();

	public ItemRenderer(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public final void renderItemInFirstPerson(float f1) {
		float f2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * f1;
		EntityPlayerSP entityPlayerSP3 = this.mc.thePlayer;
		GL11.glPushMatrix();
		GL11.glRotatef(entityPlayerSP3.prevRotationPitch + (entityPlayerSP3.rotationPitch - entityPlayerSP3.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(entityPlayerSP3.prevRotationYaw + (entityPlayerSP3.rotationYaw - entityPlayerSP3.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float f9;
		GL11.glColor4f(f9 = this.mc.theWorld.getLightBrightness((int)entityPlayerSP3.posX, (int)entityPlayerSP3.posY, (int)entityPlayerSP3.posZ), f9, f9, 1.0F);
		float f4;
		if(this.itemToRender != null) {
			GL11.glPushMatrix();
			if(this.itemSwingState) {
				f4 = MathHelper.sin((f9 = ((float)this.swingProgress + f1) / 8.0F) * (float)Math.PI);
				GL11.glTranslatef(-MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI) * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.2F, -f4 * 0.2F);
			}

			GL11.glTranslatef(0.56F, -0.52F - (1.0F - f2) * 0.6F, -0.71999997F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			float f5;
			if(this.itemSwingState) {
				f4 = MathHelper.sin((f9 = ((float)this.swingProgress + f1) / 8.0F) * f9 * (float)Math.PI);
				f5 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
				GL11.glRotatef(-f4 * 20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-f5 * 20.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-f5 * 80.0F, 1.0F, 0.0F, 0.0F);
			}

			GL11.glScalef(0.4F, 0.4F, 0.4F);
			if(this.itemToRender.itemID < 256 && Block.blocksList[this.itemToRender.itemID].getRenderType() == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[this.itemToRender.itemID]);
			} else {
				if(this.itemToRender.itemID < 256) {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				} else {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
				}

				Tessellator tessellator11 = Tessellator.instance;
				ItemStack itemStack10 = this.itemToRender;
				f5 = (float)(this.itemToRender.getItem().getIconIndex() % 16 << 4) / 256.0F;
				itemStack10 = this.itemToRender;
				f1 = (float)((this.itemToRender.getItem().getIconIndex() % 16 << 4) + 16) / 256.0F;
				itemStack10 = this.itemToRender;
				f2 = (float)(this.itemToRender.getItem().getIconIndex() / 16 << 4) / 256.0F;
				itemStack10 = this.itemToRender;
				f9 = (float)((this.itemToRender.getItem().getIconIndex() / 16 << 4) + 16) / 256.0F;
				GL11.glEnable(GL11.GL_NORMALIZE);
				GL11.glTranslatef(0.0F, -0.3F, 0.0F);
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
				Tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator11.startDrawingQuads();
				tessellator11.addVertexWithUV(0.0F, 0.0F, 0.0F, f1, f9);
				tessellator11.addVertexWithUV(1.0F, 0.0F, 0.0F, f5, f9);
				tessellator11.addVertexWithUV(1.0F, 1.0F, 0.0F, f5, f2);
				tessellator11.addVertexWithUV(0.0F, 1.0F, 0.0F, f1, f2);
				tessellator11.draw();
				Tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator11.startDrawingQuads();
				tessellator11.addVertexWithUV(0.0F, 1.0F, -0.0625F, f1, f2);
				tessellator11.addVertexWithUV(1.0F, 1.0F, -0.0625F, f5, f2);
				tessellator11.addVertexWithUV(1.0F, 0.0F, -0.0625F, f5, f9);
				tessellator11.addVertexWithUV(0.0F, 0.0F, -0.0625F, f1, f9);
				tessellator11.draw();
				Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator11.startDrawingQuads();

				int i6;
				float f7;
				float f8;
				for(i6 = 0; i6 < 16; ++i6) {
					f7 = (float)i6 / 16.0F;
					f8 = f1 + (f5 - f1) * f7 - 0.001953125F;
					f7 *= 1.0F;
					tessellator11.addVertexWithUV(f7, 0.0F, -0.0625F, f8, f9);
					tessellator11.addVertexWithUV(f7, 0.0F, 0.0F, f8, f9);
					tessellator11.addVertexWithUV(f7, 1.0F, 0.0F, f8, f2);
					tessellator11.addVertexWithUV(f7, 1.0F, -0.0625F, f8, f2);
				}

				tessellator11.draw();
				Tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator11.startDrawingQuads();

				for(i6 = 0; i6 < 16; ++i6) {
					f7 = (float)i6 / 16.0F;
					f8 = f1 + (f5 - f1) * f7 - 0.001953125F;
					f7 = f7 * 1.0F + 0.0625F;
					tessellator11.addVertexWithUV(f7, 1.0F, -0.0625F, f8, f2);
					tessellator11.addVertexWithUV(f7, 1.0F, 0.0F, f8, f2);
					tessellator11.addVertexWithUV(f7, 0.0F, 0.0F, f8, f9);
					tessellator11.addVertexWithUV(f7, 0.0F, -0.0625F, f8, f9);
				}

				tessellator11.draw();
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator11.startDrawingQuads();

				for(i6 = 0; i6 < 16; ++i6) {
					f7 = (float)i6 / 16.0F;
					f8 = f9 + (f2 - f9) * f7 - 0.001953125F;
					f7 = f7 * 1.0F + 0.0625F;
					tessellator11.addVertexWithUV(0.0F, f7, 0.0F, f1, f8);
					tessellator11.addVertexWithUV(1.0F, f7, 0.0F, f5, f8);
					tessellator11.addVertexWithUV(1.0F, f7, -0.0625F, f5, f8);
					tessellator11.addVertexWithUV(0.0F, f7, -0.0625F, f1, f8);
				}

				tessellator11.draw();
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				tessellator11.startDrawingQuads();

				for(i6 = 0; i6 < 16; ++i6) {
					f7 = (float)i6 / 16.0F;
					f8 = f9 + (f2 - f9) * f7 - 0.001953125F;
					f7 *= 1.0F;
					tessellator11.addVertexWithUV(1.0F, f7, 0.0F, f5, f8);
					tessellator11.addVertexWithUV(0.0F, f7, 0.0F, f1, f8);
					tessellator11.addVertexWithUV(0.0F, f7, -0.0625F, f1, f8);
					tessellator11.addVertexWithUV(1.0F, f7, -0.0625F, f5, f8);
				}

				tessellator11.draw();
				GL11.glDisable(GL11.GL_NORMALIZE);
			}

			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			if(this.itemSwingState) {
				f4 = MathHelper.sin((f9 = ((float)this.swingProgress + f1) / 8.0F) * (float)Math.PI);
				GL11.glTranslatef(-MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI) * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.4F, -f4 * 0.4F);
			}

			GL11.glTranslatef(0.64000005F, -0.6F - (1.0F - f2) * 0.6F, -0.71999997F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_NORMALIZE);
			if(this.itemSwingState) {
				f4 = MathHelper.sin((f9 = ((float)this.swingProgress + f1) / 8.0F) * f9 * (float)Math.PI);
				GL11.glRotatef(MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI) * 70.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-f4 * 20.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
			GL11.glTranslatef(-0.2F, -0.3F, 0.1F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
			GL11.glTranslatef(6.0F, 0.0F, 0.0F);
			((RenderPlayer)RenderManager.instance.getEntityRenderObject(this.mc.thePlayer)).drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
	}

	public final void renderOverlays(float f1) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int i2;
		Tessellator tessellator3;
		float f7;
		float f9;
		if(this.mc.thePlayer.fire > 0) {
			i2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
			tessellator3 = Tessellator.instance;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(i2 = 0; i2 < 2; ++i2) {
				GL11.glPushMatrix();
				int i4;
				int i5 = ((i4 = Block.fire.blockIndexInTexture + (i2 << 4)) & 15) << 4;
				i4 &= 240;
				float f6 = (float)i5 / 256.0F;
				float f10 = ((float)i5 + 15.99F) / 256.0F;
				f7 = (float)i4 / 256.0F;
				f9 = ((float)i4 + 15.99F) / 256.0F;
				GL11.glTranslatef((float)(-((i2 << 1) - 1)) * 0.24F, -0.3F, 0.0F);
				GL11.glRotatef((float)((i2 << 1) - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
				tessellator3.startDrawingQuads();
				tessellator3.addVertexWithUV(-0.5F, -0.5F, -0.5F, f10, f9);
				tessellator3.addVertexWithUV(0.5F, -0.5F, -0.5F, f6, f9);
				tessellator3.addVertexWithUV(0.5F, 0.5F, -0.5F, f6, f7);
				tessellator3.addVertexWithUV(-0.5F, 0.5F, -0.5F, f10, f7);
				tessellator3.draw();
				GL11.glPopMatrix();
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

		if(this.mc.thePlayer.isInsideOfWater()) {
			i2 = this.mc.renderEngine.getTexture("/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
			tessellator3 = Tessellator.instance;
			float f8;
			GL11.glColor4f(f8 = this.mc.thePlayer.getEntityBrightness(f1), f8, f8, 0.5F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPushMatrix();
			f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
			f9 = this.mc.thePlayer.rotationPitch / 64.0F;
			tessellator3.startDrawingQuads();
			tessellator3.addVertexWithUV(-1.0F, -1.0F, -0.5F, f7 + 4.0F, f9 + 4.0F);
			tessellator3.addVertexWithUV(1.0F, -1.0F, -0.5F, f7 + 0.0F, f9 + 4.0F);
			tessellator3.addVertexWithUV(1.0F, 1.0F, -0.5F, f7 + 0.0F, f9 + 0.0F);
			tessellator3.addVertexWithUV(-1.0F, 1.0F, -0.5F, f7 + 4.0F, f9 + 0.0F);
			tessellator3.draw();
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	public final void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		if(this.itemSwingState) {
			++this.swingProgress;
			if(this.swingProgress == 8) {
				this.swingProgress = 0;
				this.itemSwingState = false;
			}
		}

		EntityPlayerSP entityPlayerSP1 = this.mc.thePlayer;
		float f2;
		ItemStack itemStack3;
		if((f2 = ((itemStack3 = this.mc.thePlayer.inventory.getCurrentItem()) == this.itemToRender ? 1.0F : 0.0F) - this.equippedProgress) < -0.4F) {
			f2 = -0.4F;
		}

		if(f2 > 0.4F) {
			f2 = 0.4F;
		}

		this.equippedProgress += f2;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = itemStack3;
		}

	}

	public final void equipAnimationSpeed() {
		this.equippedProgress = 0.0F;
	}

	public final void equippedItemRender() {
		this.swingProgress = -1;
		this.itemSwingState = true;
	}

	public final void resetEquippedProgress() {
		this.equippedProgress = 0.0F;
	}
}