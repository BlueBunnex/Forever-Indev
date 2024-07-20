package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public abstract class GuiButton extends Gui {
	
	protected int width;
	protected int height;
	protected int xPosition;
	protected int yPosition;
	
	public int id;
	public boolean enabled;
	public boolean visible;

	public GuiButton(int id, int x, int y, int width, int height) {
		this.id = id;
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		
		this.enabled = true;
		this.visible = true;
	}

	public abstract void drawButton(Minecraft mc, int mouseX, int mouseY);

	public final boolean mousePressed(int mouseX, int mouseY) {
		return this.enabled
				&& mouseX >= this.xPosition
				&& mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
	}
}
