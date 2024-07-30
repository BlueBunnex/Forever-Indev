package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class GuiButtonTexture extends GuiButton {
	
	private int texX, texY;

	public GuiButtonTexture(int id, int x, int y, int width, int height, int texX, int texY) {
		super(id, x, y, width, height);
		
		this.texX = texX;
		this.texY = texY;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		
		if (!this.visible)
			return;
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		boolean hovered = mouseX >= this.xPosition
				       && mouseY >= this.yPosition
				       && mouseX < this.xPosition + this.width
				       && mouseY < this.yPosition + this.height;
		
		int texOffset;
		
		if (!this.enabled) {
			texOffset = 0;
		} else if (hovered) {
			texOffset = this.height * 2;
		} else {
			texOffset = this.height;
		}

		this.drawTexturedModalRect(this.xPosition, this.yPosition, texX, texY + texOffset, this.width, this.height);
	}

}
