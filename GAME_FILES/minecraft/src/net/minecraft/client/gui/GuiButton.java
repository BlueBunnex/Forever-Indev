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

	public GuiButton(int id, int x, int y, int width, int height, String display) {
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		
		this.enabled = true;
		this.visible = true;
		this.displayString = display;
	}

	public final void drawButton(Minecraft mc, int mouseX, int mouseY) {
		
		if (!this.visible)
			return;
			
		FontRenderer fontRend = mc.fontRenderer;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		boolean hovered = mouseX >= this.xPosition
				       && mouseY >= this.yPosition
				       && mouseX < this.xPosition + this.width
				       && mouseY < this.yPosition + this.height;
		
		int texOffset;
		
		if (!this.enabled) {
			texOffset = 46; // 46 + [0, 2, 1] * 20
		} else if (hovered) {
			texOffset = 86;
		} else {
			texOffset = 66;
		}

		this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, texOffset, this.width / 2, this.height);
		this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, texOffset, this.width / 2, this.height);
		
		if (!this.enabled) {
			drawCenteredString(fontRend, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
		} else if (hovered) {
			drawCenteredString(fontRend, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
		} else {
			drawCenteredString(fontRend, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
		}
	}

	public final boolean mousePressed(int mouseX, int mouseY) {
		return this.enabled
				&& mouseX >= this.xPosition
				&& mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
	}
}
