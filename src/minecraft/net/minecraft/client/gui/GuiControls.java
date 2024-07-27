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

		this.controlList.add(new GuiButtonText(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton button) {
		
		// done was pressed
		if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
			return;
		}
		
		GuiButtonText textButton = (GuiButtonText) button;
		
		for (int i = 0; i < this.options.keyBindings.length; i++) {
			((GuiButtonText) this.controlList.get(i)).displayString = this.options.setKeyBindingString(i);
		}

		this.buttonId = textButton.id;
		textButton.displayString = "> " + this.options.setKeyBindingString(textButton.id) + " <";
	}

	protected final void keyTyped(char character, int keycode) {
		
		if(this.buttonId >= 0) {
			// set bind
			this.options.setKeyBinding(this.buttonId, keycode);
			((GuiButtonText) this.controlList.get(this.buttonId)).displayString = this.options.setKeyBindingString(this.buttonId);
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
