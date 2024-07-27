package net.minecraft.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLoadLevel;
import net.minecraft.client.gui.GuiNewLevel;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class GuiMainMenu extends GuiScreen {
	private float updateCounter = 0.0F;
	private String[] splashes = new String[]{"Pre-beta!", "As seen on TV!", "Awesome!", "100% pure!", "May contain nuts!", "Better than Prey!", "More polygons!", "Sexy!", "Limited edition!", "Flashing letters!", "Made by Notch!", "Coming soon!", "Best in class!", "When it\'s finished!", "Absolutely dragon free!", "Excitement!", "More than 5000 sold!", "One of a kind!", "700+ hits on YouTube!", "Indev!", "Spiders everywhere!", "Check it out!", "Holy cow, man!", "It\'s a game!", "Made in Sweden!", "Uses LWJGL!", "Reticulating splines!", "Minecraft!", "Yaaay!", "Alpha version!", "Singleplayer!", "Keyboard compatible!", "Undocumented!", "Ingots!", "Exploding creepers!", "That\'s not a moon!", "l33t!", "Create!", "Survive!", "Dungeon!", "Exclusive!", "The bee\'s knees!", "Down with O.P.P.!", "Closed source!", "Classy!", "Wow!", "Not on steam!", "9.95 euro!", "Half price!", "Oh man!", "Check it out!", "Awesome community!", "Pixels!", "Teetsuuuuoooo!", "Kaaneeeedaaaa!", "Now with difficulty!", "Enhanced!", "90% bug free!", "Pretty!", "12 herbs and spices!", "Fat free!", "Absolutely no memes!", "Free dental!", "Ask your doctor!", "Minors welcome!", "Cloud computing!", "Legal in Finland!", "Hard to label!", "Technically good!", "Bringing home the bacon!", "Indie!", "GOTY!", "Ceci n\'est pas une title screen!", "Euclidian!", "Now in 3D!", "Inspirational!", "Herregud!", "Complex cellular automata!", "Yes, sir!", "Played by cowboys!", "OpenGL 1.1!", "Thousands of colors!", "Try it!", "Age of Wonders is better!", "Try the mushroom stew!", "Sensational!", "Hot tamale, hot hot tamale!", "Play him off, keyboard cat!", "Guaranteed!", "Macroscopic!", "Bring it on!", "Random splash!", "Call your mother!", "Monster infighting!", "Loved by millions!", "Ultimate edition!", "Freaky!", "You\'ve got a brand new key!", "Water proof!", "Uninflammable!", "Whoa, dude!", "All inclusive!", "Tell your friends!", "NP is not in P!", "Notch <3 Ez!", "Music by C418!"};
	private String currentSplash = this.splashes[(int)(Math.random() * (double)this.splashes.length)];

	public final void updateScreen() {
		this.updateCounter += 0.01F;
	}

	protected final void keyTyped(char c1, int i2) {
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, "Load level.."));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96, "Play tutorial level"));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Options..."));
		((GuiButton)this.controlList.get(2)).enabled = false;
		if(this.mc.session == null) {
			((GuiButton)this.controlList.get(1)).enabled = false;
		}

	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
		}

		if(guiButton1.id == 1) {
			this.mc.displayGuiScreen(new GuiNewLevel(this));
		}

		if(this.mc.session != null && guiButton1.id == 2) {
			this.mc.displayGuiScreen(new GuiLoadLevel(this));
		}

	}

	public final void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		this.drawDefaultBackground();
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator4.setColorOpaque_I(0xFFFFFF);
		this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 49);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f15;
		GL11.glScalef(f15 = (1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F)) * 100.0F / (float)(this.fontRenderer.getStringWidth(this.currentSplash) + 32), f15, f15);
		drawCenteredString(this.fontRenderer, this.currentSplash, 0, -8, 16776960);
		GL11.glPopMatrix();
		String string16 = "Copyright Mojang Specifications. Do not distribute.";
		drawString(this.fontRenderer, string16, this.width - this.fontRenderer.getStringWidth(string16) - 2, this.height - 10, 0xFFFFFF);
		long j7 = Runtime.getRuntime().maxMemory();
		long j9 = Runtime.getRuntime().totalMemory();
		long j11 = Runtime.getRuntime().freeMemory();
		long j13 = j7 - j11;
		string16 = "Free memory: " + j13 * 100L / j7 + "% of " + j7 / 1024L / 1024L + "MB";
		drawString(this.fontRenderer, string16, this.width - this.fontRenderer.getStringWidth(string16) - 2, 2, 8421504);
		string16 = "Allocated memory: " + j9 * 100L / j7 + "% (" + j9 / 1024L / 1024L + "MB)";
		drawString(this.fontRenderer, string16, this.width - this.fontRenderer.getStringWidth(string16) - 2, 12, 8421504);
		super.drawScreen(xSize_lo, ySize_lo, f3);
	}
}