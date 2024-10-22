package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.game.entity.player.EntityPlayerSP;

public final class GuiGameOver extends GuiScreen {
	
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButtonText(1, this.width / 2 - 100, this.height / 4 + 72, "Generate new level..."));
		this.controlList.add(new GuiButtonText(2, this.width / 2 - 100, this.height / 4 + 96, "Load level.."));
		
		if (this.mc.session == null)
			this.controlList.get(1).enabled = false;
	}

	protected final void keyTyped(char character, int keycode) {}

	protected final void actionPerformed(GuiButton button) {
		
		switch (button.id) {
		
			case 0:
				this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
				break;
	
			case 1:
				this.mc.displayGuiScreen(new GuiNewLevel(this));
				break;
	
			case 2:
				this.mc.displayGuiScreen(new GuiLoadLevel(this));
				break;
		}
	}

	public final void drawScreen(int mouseX, int mouseY) {
		drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();
		FontRenderer var10000 = this.fontRenderer;
		StringBuilder var10001 = (new StringBuilder()).append("Score: &e");
		EntityPlayerSP var4 = this.mc.thePlayer;
		drawCenteredString(var10000, var10001.append(var4.getScore).toString(), this.width / 2, 100, 16777215);
		
		super.drawScreen(mouseX, mouseY);
	}

	public final boolean doesGuiPauseGame() {
		return false;
	}
}
