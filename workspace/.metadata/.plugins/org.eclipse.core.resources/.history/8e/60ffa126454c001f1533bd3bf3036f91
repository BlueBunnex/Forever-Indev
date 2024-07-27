package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;

public final class GuiControls extends GuiScreen {
	private GuiScreen parentScreen;
	private String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;

	public GuiControls(GuiScreen guiScreen1, GameSettings gameSettings2) {
		this.parentScreen = guiScreen1;
		this.options = gameSettings2;
	}

	public final void initGui() {
		for(int i1 = 0; i1 < this.options.keyBindings.length; ++i1) {
			this.controlList.add(new GuiSmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.setKeyBindingString(i1)));
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		for(int i2 = 0; i2 < this.options.keyBindings.length; ++i2) {
			((GuiButton)this.controlList.get(i2)).displayString = this.options.setKeyBindingString(i2);
		}

		if(guiButton1.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = guiButton1.id;
			guiButton1.displayString = "> " + this.options.setKeyBindingString(guiButton1.id) + " <";
		}
	}

	protected final void keyTyped(char c1, int i2) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, i2);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.setKeyBindingString(this.buttonId);
			this.buttonId = -1;
		} else {
			super.keyTyped(c1, i2);
		}
	}

	public final void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(xSize_lo, ySize_lo, f3);
	}
}