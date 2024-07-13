package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;

public final class GuiControls extends GuiScreen {
	
	private GuiScreen parentScreen;
	private String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;

	public GuiControls(GuiScreen parentScreen, GameSettings options) {
		this.parentScreen = parentScreen;
		this.options = options;
	}

	public final void initGui() {
		for(int var1 = 0; var1 < this.options.keyBindings.length; ++var1) {
			this.controlList.add(new GuiSmallButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.options.setKeyBindingString(var1)));
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton var1) {
		
		for (int i = 0; i < this.options.keyBindings.length; i++) {
			this.controlList.get(i).displayString = this.options.setKeyBindingString(i);
		}

		if (var1.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = var1.id;
			var1.displayString = "> " + this.options.setKeyBindingString(var1.id) + " <";
		}
	}

	protected final void keyTyped(char character, int keycode) {
		
		if(this.buttonId >= 0) {
			// set bind
			this.options.setKeyBinding(this.buttonId, keycode);
			this.controlList.get(this.buttonId).displayString = this.options.setKeyBindingString(this.buttonId);
			this.buttonId = -1;
		} else {
			super.keyTyped(character, keycode);
		}
	}

	public final void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		
		super.drawScreen(mouseX, mouseY);
	}
}
