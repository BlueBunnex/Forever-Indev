package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.GL11;

public class Gui {
	protected float zLevel = 0.0F;

	protected static void drawRect(int xCoordLeft, int yCoordUp, int xCoordRight, int yCoordDown, int color) {
		float f5 = (float)(color >>> 24) / 255.0F;
		float f6 = (float)(color >> 16 & 255) / 255.0F;
		float f7 = (float)(color >> 8 & 255) / 255.0F;
		float color1 = (float)(color & 255) / 255.0F;
		Tessellator tessellator8 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f6, f7, color1, f5);
		tessellator8.startDrawingQuads();
		tessellator8.addVertex((float)xCoordLeft, (float)yCoordDown, 0.0F);
		tessellator8.addVertex((float)xCoordRight, (float)yCoordDown, 0.0F);
		tessellator8.addVertex((float)xCoordRight, (float)yCoordUp, 0.0F);
		tessellator8.addVertex((float)xCoordLeft, (float)yCoordUp, 0.0F);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected static void drawGradientRect(int xCoordLeft, int yCoordUp, int xCoordRight, int yCoordDown, int upperGradient, int lowerGradient) {
		float f6 = (float)(upperGradient >>> 24) / 255.0F;
		float f7 = (float)(upperGradient >> 16 & 255) / 255.0F;
		float f8 = (float)(upperGradient >> 8 & 255) / 255.0F;
		float upperGradient1 = (float)(upperGradient & 255) / 255.0F;
		float f9 = (float)(lowerGradient >>> 24) / 255.0F;
		float f10 = (float)(lowerGradient >> 16 & 255) / 255.0F;
		float f11 = (float)(lowerGradient >> 8 & 255) / 255.0F;
		float lowerGradient1 = (float)(lowerGradient & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator12 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator12.setColorRGBA_F(f7, f8, upperGradient1, f6);
		tessellator12.addVertex((float)xCoordRight, (float)yCoordUp, 0.0F);
		tessellator12.addVertex((float)xCoordLeft, (float)yCoordUp, 0.0F);
		tessellator12.setColorRGBA_F(f10, f11, lowerGradient1, f9);
		tessellator12.addVertex((float)xCoordLeft, (float)yCoordDown, 0.0F);
		tessellator12.addVertex((float)xCoordRight, (float)yCoordDown, 0.0F);
		tessellator12.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawCenteredString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2 - fontRenderer0.getStringWidth(string1) / 2, i3, i4);
	}

	public static void drawString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2, i3, i4);
	}

	public final void drawTexturedModalRect(int i1, int i2, int i3, int i4, int i5, int i6) {
		Tessellator tessellator7 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator7.addVertexWithUV((float)i1, (float)(i2 + i6), this.zLevel, (float)i3 * 0.00390625F, (float)(i4 + i6) * 0.00390625F);
		tessellator7.addVertexWithUV((float)(i1 + i5), (float)(i2 + i6), this.zLevel, (float)(i3 + i5) * 0.00390625F, (float)(i4 + i6) * 0.00390625F);
		tessellator7.addVertexWithUV((float)(i1 + i5), (float)i2, this.zLevel, (float)(i3 + i5) * 0.00390625F, (float)i4 * 0.00390625F);
		tessellator7.addVertexWithUV((float)i1, (float)i2, this.zLevel, (float)i3 * 0.00390625F, (float)i4 * 0.00390625F);
		tessellator7.draw();
	}
}