package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public class Gui {
	
	public static final int COLOR_WHITE = 16777215;
	public static final int COLOR_GREY = 8421504;
	
	protected float zLevel = 0.0F;

	protected static void drawRect(int x1, int y1, int x2, int y2, int color) {
		float a = (float) (color >>> 24)      / 255.0F;
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8  & 255) / 255.0F;
		float b = (float) (color & 255)       / 255.0F;
		
		Tessellator var8 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(r, g, b, a);
		
		var8.startDrawingQuads();
		var8.addVertex((float) x1, (float) y2, 0.0F);
		var8.addVertex((float) x2, (float) y2, 0.0F);
		var8.addVertex((float) x2, (float) y1, 0.0F);
		var8.addVertex((float) x1, (float) y1, 0.0F);
		var8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected static void drawGradientRect(int var0, int var1, int var2, int var3, int var4, int var5) {
		float var6 = (float)(var4 >>> 24) / 255.0F;
		float var7 = (float)(var4 >> 16 & 255) / 255.0F;
		float var8 = (float)(var4 >> 8 & 255) / 255.0F;
		float var13 = (float)(var4 & 255) / 255.0F;
		float var9 = (float)(var5 >>> 24) / 255.0F;
		float var10 = (float)(var5 >> 16 & 255) / 255.0F;
		float var11 = (float)(var5 >> 8 & 255) / 255.0F;
		float var14 = (float)(var5 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var12 = Tessellator.instance;
		var12.startDrawingQuads();
		var12.setColorRGBA_F(var7, var8, var13, var6);
		var12.addVertex((float)var2, (float)var1, 0.0F);
		var12.addVertex((float)var0, (float)var1, 0.0F);
		var12.setColorRGBA_F(var10, var11, var14, var9);
		var12.addVertex((float)var0, (float)var3, 0.0F);
		var12.addVertex((float)var2, (float)var3, 0.0F);
		var12.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawCenteredString(FontRenderer fontRend, String string, int x, int y, int color) {
		fontRend.drawStringWithShadow(string, x - fontRend.getStringWidth(string) / 2, y, color);
	}

	public static void drawString(FontRenderer fontRend, String string, int x, int y, int color) {
		fontRend.drawStringWithShadow(string, x, y, color);
	}
	
	public static void drawCenteredStringWithBackground(FontRenderer fontRend, String string, int x, int y, int color) {
		drawStringWithBackground(fontRend, string, x - fontRend.getStringWidth(string) / 2, y, color);
	}
	
	public static void drawStringWithBackground(FontRenderer fontRend, String string, int x, int y, int color) {
		drawRect(
				x - 2,
				y - 2,
				x + 1 + fontRend.getStringWidth(string),
				y + 9, -1442840576);
		
		fontRend.drawString(string, x, y, color);
	}

	public final void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
		Tessellator var7 = Tessellator.instance;
		var7.startDrawingQuads();
		var7.addVertexWithUV((float)var1, (float)(var2 + var6), this.zLevel, (float)var3 * 0.00390625F, (float)(var4 + var6) * 0.00390625F);
		var7.addVertexWithUV((float)(var1 + var5), (float)(var2 + var6), this.zLevel, (float)(var3 + var5) * 0.00390625F, (float)(var4 + var6) * 0.00390625F);
		var7.addVertexWithUV((float)(var1 + var5), (float)var2, this.zLevel, (float)(var3 + var5) * 0.00390625F, (float)var4 * 0.00390625F);
		var7.addVertexWithUV((float)var1, (float)var2, this.zLevel, (float)var3 * 0.00390625F, (float)var4 * 0.00390625F);
		var7.draw();
	}
}
