package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;

	public void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		for(int i5 = 0; i5 < this.controlList.size(); ++i5) {
			((GuiButton)this.controlList.get(i5)).drawButton(this.mc, xSize_lo, ySize_lo);
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	protected void drawSlotInventory(int i1, int i2, int i3) {
		if(i3 == 0) {
			for(i3 = 0; i3 < this.controlList.size(); ++i3) {
				GuiButton guiButton4;
				if((guiButton4 = (GuiButton)this.controlList.get(i3)).mousePressed(i1, i2)) {
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(guiButton4);
				}
			}
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
	}

	public final void setWorldAndResolution(Minecraft minecraft1, int width, int height) {
		this.mc = minecraft1;
		this.fontRenderer = minecraft1.fontRenderer;
		this.width = width;
		this.height = height;
		this.initGui();
	}

	public void initGui() {
	}

	public final void handleMouseInput() {
		if(Mouse.getEventButtonState()) {
			int i1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int i2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.drawSlotInventory(i1, i2, Mouse.getEventButton());
		} else {
			Mouse.getEventX();
			Mouse.getEventY();
			Mouse.getEventButton();
		}
	}

	public final void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
			if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
				this.mc.toggleFullscreen();
				return;
			}

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

	public void onGuiClosed() {
	}

	public final void drawDefaultBackground() {
		boolean z1 = false;
		if(this.mc.theWorld != null) {
			drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			Tessellator tessellator2 = Tessellator.instance;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tessellator2.startDrawingQuads();
			tessellator2.setColorOpaque_I(4210752);
			tessellator2.addVertexWithUV(0.0F, (float)this.height, 0.0F, 0.0F, (float)this.height / 32.0F);
			tessellator2.addVertexWithUV((float)this.width, (float)this.height, 0.0F, (float)this.width / 32.0F, (float)this.height / 32.0F);
			tessellator2.addVertexWithUV((float)this.width, 0.0F, 0.0F, (float)this.width / 32.0F, 0.0F);
			tessellator2.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			tessellator2.draw();
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}