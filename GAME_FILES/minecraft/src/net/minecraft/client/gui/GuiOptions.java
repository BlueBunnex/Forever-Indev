package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;

public final class GuiOptions extends GuiScreen {
	
	private GuiScreen parentScreen;
	private String screenTitle = "Options";
	private GameSettings options;

	public GuiOptions(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public final void initGui() {
		for(int i = 0; i < this.options.numberOfOptions; i++) {
			this.controlList.add(new GuiButtonText(i, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, this.options.setOptionString(i)));
		}

		this.controlList.add(new GuiButtonText(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
		this.controlList.add(new GuiButtonText(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void actionPerformed(GuiButton button) {
		
		if (!button.enabled)
			return;
			
		if (button.id < 100) {
			this.options.setOptionValue(button.id, 1);
			((GuiButtonText) button).displayString = this.options.setOptionString(button.id);
		}

		else if (button.id == 100) {
			this.mc.displayGuiScreen(new GuiControls(this, this.options));
		}

		else if (button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}

	public final void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		
		super.drawScreen(mouseX, mouseY);
	}
}
