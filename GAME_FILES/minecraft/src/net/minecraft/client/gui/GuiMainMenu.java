package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class GuiMainMenu extends GuiScreen {
	
	private static final String[] SPLASHES = new String[] { "Not-quite indev!", "Chests on the glass door!", "NP is not in P!", "The blue-twintailed girl is right!", "In development hell, did I development sin?", "Bubbles from the gum machine!", "[INSERT SPLASH HERE]", "Full-stop!", "[EXTREMELY LOUD INCORRECT BUZZER]" };
	private static final String CURRENT_SPLASH = SPLASHES[(int) (Math.random() * (double) SPLASHES.length)];

	public final void updateScreen() {}

	protected final void keyTyped(char character, int keycode) {}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Load level.."));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Options..."));
		
		// disable load level for some reason
		if (this.mc.session == null)
			((GuiButton)this.controlList.get(1)).enabled = false;

	}

	protected final void actionPerformed(GuiButton button) {
		
		switch (button.id) {
			case 0:
				this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
				break;
			case 1:
				this.mc.displayGuiScreen(new GuiNewLevel(this));
				break;
			case 2:
				if (this.mc.session != null)
					this.mc.displayGuiScreen(new GuiLoadLevel(this));
				break;
		}

	}

	public final void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		
		// this code scares me
		Tessellator var4 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.setColorOpaque_I(16777215);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 64);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (this.width / 2 + 110), 85.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var15 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		var15 = var15 * 100.0F / (float)(this.fontRenderer.getStringWidth(CURRENT_SPLASH) + 32);
		GL11.glScalef(var15, var15, var15);
		drawCenteredString(this.fontRenderer, CURRENT_SPLASH, 0, -8, 16776960);
		GL11.glPopMatrix();
		
		// yeah fuck Mojang
		String text = "Made by Blue. Distribute!";
		drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, this.height - 10, 16777215);
		
		// no idea how memory calculations work, this was here
		// before I got here I just changed the variable names
		// don't hurt me
		long maxMem   = Runtime.getRuntime().maxMemory();
		long totalMem = Runtime.getRuntime().totalMemory();
		long freeMem  = Runtime.getRuntime().freeMemory();
		long what     = maxMem - freeMem;
		
		text = "Free memory: " + what * 100L / maxMem + "% of " + maxMem / 1024L / 1024L + "MB";
		drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, 2, 8421504);
		
		text = "Allocated memory: " + totalMem * 100L / maxMem + "% (" + totalMem / 1024L / 1024L + "MB)";
		drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, 12, 8421504);
		
		super.drawScreen(mouseX, mouseY);
	}
}
