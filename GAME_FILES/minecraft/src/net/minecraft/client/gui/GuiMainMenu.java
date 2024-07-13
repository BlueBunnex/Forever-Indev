package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import util.MathHelper;

public final class GuiMainMenu extends GuiScreen {
	private float updateCounter = 0.0F;
	
	private static final String[] SPLASHES = new String[] { "Not-quite indev!", "Chests on the glass door!", "NP is not in P!", "The blue-twintailed girl is right!", "In development hell, did I development sin?", "Bubbles from the gum machine!", "[INSERT SPLASH HERE]", "Full-stop!" };
	private static final String CURRENT_SPLASH = SPLASHES[(int)(Math.random() * (double) SPLASHES.length)];

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	protected final void keyTyped(char var1, int var2) {}

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

	public final void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
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
		String var16 = "Copyright Mojang Specifications. Do not distribute.";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, this.height - 10, 16777215);
		long var7 = Runtime.getRuntime().maxMemory();
		long var9 = Runtime.getRuntime().totalMemory();
		long var11 = Runtime.getRuntime().freeMemory();
		long var13 = var7 - var11;
		var16 = "Free memory: " + var13 * 100L / var7 + "% of " + var7 / 1024L / 1024L + "MB";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, 2, 8421504);
		var16 = "Allocated memory: " + var9 * 100L / var7 + "% (" + var9 / 1024L / 1024L + "MB)";
		drawString(this.fontRenderer, var16, this.width - this.fontRenderer.getStringWidth(var16) - 2, 12, 8421504);
		super.drawScreen(var1, var2, var3);
	}
}
