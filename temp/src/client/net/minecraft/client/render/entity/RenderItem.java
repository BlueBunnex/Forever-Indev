package net.minecraft.client.render.entity;

import java.util.Random;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class RenderItem extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();
	private Random random = new Random();

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	public final void renderItemIntoGUI(RenderEngine renderEngineClass, ItemStack itemStackClass, int i3, int i4) {
		if(itemStackClass != null) {
			int itemStackClass1;
			if(itemStackClass.itemID < 256 && Block.blocksList[itemStackClass.itemID].getRenderType() == 0) {
				itemStackClass1 = itemStackClass.itemID;
				RenderEngine.bindTexture(renderEngineClass.getTexture("/terrain.png"));
				Block renderEngineClass3 = Block.blocksList[itemStackClass1];
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(i3 - 2), (float)(i4 + 3), 0.0F);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 8.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.renderBlocks.renderBlockOnInventory(renderEngineClass3);
				GL11.glPopMatrix();
			} else {
				if(itemStackClass.getItem().getIconIndex() >= 0) {
					GL11.glDisable(GL11.GL_LIGHTING);
					if(itemStackClass.itemID < 256) {
						RenderEngine.bindTexture(renderEngineClass.getTexture("/terrain.png"));
					} else {
						RenderEngine.bindTexture(renderEngineClass.getTexture("/gui/items.png"));
					}

					int i10000 = i3;
					int i10001 = i4;
					int i10002 = itemStackClass.getItem().getIconIndex() % 16 << 4;
					int i10003 = itemStackClass.getItem().getIconIndex() / 16 << 4;
					boolean renderEngineClass1 = true;
					renderEngineClass1 = true;
					i4 = i10003;
					i3 = i10002;
					itemStackClass1 = i10001;
					int renderEngineClass2 = i10000;
					Tessellator tessellator5 = Tessellator.instance;
					Tessellator.instance.startDrawingQuads();
					tessellator5.addVertexWithUV((float)renderEngineClass2, (float)(itemStackClass1 + 16), 0.0F, (float)i3 * 0.00390625F, (float)(i4 + 16) * 0.00390625F);
					tessellator5.addVertexWithUV((float)(renderEngineClass2 + 16), (float)(itemStackClass1 + 16), 0.0F, (float)(i3 + 16) * 0.00390625F, (float)(i4 + 16) * 0.00390625F);
					tessellator5.addVertexWithUV((float)(renderEngineClass2 + 16), (float)itemStackClass1, 0.0F, (float)(i3 + 16) * 0.00390625F, (float)i4 * 0.00390625F);
					tessellator5.addVertexWithUV((float)renderEngineClass2, (float)itemStackClass1, 0.0F, (float)i3 * 0.00390625F, (float)i4 * 0.00390625F);
					tessellator5.draw();
					GL11.glEnable(GL11.GL_LIGHTING);
				}

			}
		}
	}

	public final void renderItemOverlayIntoGUI(FontRenderer fontRenderer1, ItemStack itemStack2, int i3, int i4) {
		if(itemStack2 != null) {
			if(itemStack2.stackSize > 1) {
				String string5 = "" + itemStack2.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				fontRenderer1.drawStringWithShadow(string5, i3 + 19 - 2 - fontRenderer1.getStringWidth(string5), i4 + 6 + 3, 0xFFFFFF);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			if(itemStack2.itemDamage > 0) {
				int i9 = 13 - itemStack2.itemDamage * 13 / itemStack2.isItemStackDamageable();
				int i7 = 255 - itemStack2.itemDamage * 255 / itemStack2.isItemStackDamageable();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator8 = Tessellator.instance;
				int i6 = 255 - i7 << 16 | i7 << 8;
				i7 = (255 - i7) / 4 << 16 | 16128;
				renderQuad(tessellator8, i3 + 2, i4 + 13, 13, 2, 0);
				renderQuad(tessellator8, i3 + 2, i4 + 13, 12, 1, i7);
				renderQuad(tessellator8, i3 + 2, i4 + 13, i9, 1, i6);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		}
	}

	private static void renderQuad(Tessellator tessellator0, int i1, int i2, int i3, int i4, int i5) {
		tessellator0.startDrawingQuads();
		tessellator0.setColorOpaque_I(i5);
		tessellator0.addVertex((float)i1, (float)i2, 0.0F);
		tessellator0.addVertex((float)i1, (float)(i2 + i4), 0.0F);
		tessellator0.addVertex((float)(i1 + i3), (float)(i2 + i4), 0.0F);
		tessellator0.addVertex((float)(i1 + i3), (float)i2, 0.0F);
		tessellator0.draw();
	}

	public final void doRender(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		EntityItem entityItem13 = (EntityItem)entity;
		RenderItem renderItem12 = this;
		this.random.setSeed(187L);
		ItemStack itemStack7 = entityItem13.item;
		GL11.glPushMatrix();
		float f8 = MathHelper.sin(((float)entityItem13.age + f6) / 10.0F + entityItem13.hoverStart) * 0.1F + 0.1F;
		f6 = (((float)entityItem13.age + f6) / 20.0F + entityItem13.hoverStart) * 57.295776F;
		byte b9 = 1;
		if(entityItem13.item.stackSize > 1) {
			b9 = 2;
		}

		if(entityItem13.item.stackSize > 5) {
			b9 = 3;
		}

		if(entityItem13.item.stackSize > 20) {
			b9 = 4;
		}

		GL11.glTranslatef(f2, f3 + f8, f4);
		GL11.glEnable(GL11.GL_NORMALIZE);
		if(itemStack7.itemID < 256 && Block.blocksList[itemStack7.itemID].getRenderType() == 0) {
			GL11.glRotatef(f6, 0.0F, 1.0F, 0.0F);
			this.loadTexture("/terrain.png");
			f2 = 0.25F;
			if(!Block.blocksList[itemStack7.itemID].renderAsNormalBlock() && itemStack7.itemID != Block.stairSingle.blockID) {
				f2 = 0.5F;
			}

			GL11.glScalef(f2, f2, f2);

			for(int i16 = 0; i16 < b9; ++i16) {
				GL11.glPushMatrix();
				if(i16 > 0) {
					f4 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f2;
					f5 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f2;
					f6 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.2F / f2;
					GL11.glTranslatef(f4, f5, f6);
				}

				renderItem12.renderBlocks.renderBlockOnInventory(Block.blocksList[itemStack7.itemID]);
				GL11.glPopMatrix();
			}
		} else {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			int i14 = itemStack7.getItem().getIconIndex();
			if(itemStack7.itemID < 256) {
				this.loadTexture("/terrain.png");
			} else {
				this.loadTexture("/gui/items.png");
			}

			Tessellator tessellator15 = Tessellator.instance;
			f4 = (float)(i14 % 16 << 4) / 256.0F;
			f5 = (float)((i14 % 16 << 4) + 16) / 256.0F;
			f6 = (float)(i14 / 16 << 4) / 256.0F;
			f2 = (float)((i14 / 16 << 4) + 16) / 256.0F;

			for(int i17 = 0; i17 < b9; ++i17) {
				GL11.glPushMatrix();
				if(i17 > 0) {
					f8 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f10 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float f11 = (renderItem12.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(f8, f10, f11);
				}

				GL11.glRotatef(180.0F - renderItem12.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				tessellator15.startDrawingQuads();
				Tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator15.addVertexWithUV(-0.5F, -0.25F, 0.0F, f4, f2);
				tessellator15.addVertexWithUV(0.5F, -0.25F, 0.0F, f5, f2);
				tessellator15.addVertexWithUV(0.5F, 0.75F, 0.0F, f5, f6);
				tessellator15.addVertexWithUV(-0.5F, 0.75F, 0.0F, f4, f6);
				tessellator15.draw();
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}
}