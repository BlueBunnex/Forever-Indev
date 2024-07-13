package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
	private int width;
	private int height;
	private int xPosition;
	private int yPosition;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;

	public GuiButton(int id, int x, int y, String display) {
		this(id, x, y, 200, 20, display);
	}

	protected GuiButton(int id, int x, int y, int width, int height, String display) {
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		
		this.enabled = true;
		this.visible = true;
		this.displayString = display;
	}

	public final void drawButton(Minecraft var1, int var2, int var3) {
		if(this.visible) {
			FontRenderer var4 = var1.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			byte var5 = 1;
			boolean var6 = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
			if(!this.enabled) {
				var5 = 0;
			} else if(var6) {
				var5 = 2;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
			if(!this.enabled) {
				drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
			} else if(var6) {
				drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
			} else {
				drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
			}
		}
	}

	public final boolean mousePressed(int var1, int var2) {
		return this.enabled && var1 >= this.xPosition && var2 >= this.yPosition && var1 < this.xPosition + this.width && var2 < this.yPosition + this.height;
	}
}
