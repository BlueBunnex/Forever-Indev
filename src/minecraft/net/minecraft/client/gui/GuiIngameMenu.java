package net.minecraft.client.gui;

public final class GuiIngameMenu extends GuiScreen {
	
	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButtonText(0, this.width / 2 - 100, this.height / 4 + 24, "Options..."));
		this.controlList.add(new GuiButtonText(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
		this.controlList.add(new GuiButtonText(2, this.width / 2 - 100, this.height / 4 + 72, 98, 20, "Save level.."));
		this.controlList.add(new GuiButtonText(3, this.width / 2 + 2,   this.height / 4 + 72, 98, 20, "Load level.."));
		this.controlList.add(new GuiButtonText(4, this.width / 2 - 100, this.height / 4, "Back to game"));
		this.controlList.add(new GuiButtonText(5, this.width / 2 - 100, this.height / 4 + 120, "Exit to main menu (SAVE FIRST!)"));
		
		if (this.mc.session == null) {
			this.controlList.get(2).enabled = false;
			this.controlList.get(3).enabled = false;
		}

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
				if(this.mc.session != null)
					this.mc.displayGuiScreen(new GuiSaveLevel(this));
				break;
				
			case 3:
				if(this.mc.session != null)
					this.mc.displayGuiScreen(new GuiLoadLevel(this));
				break;
				
			case 4:
				this.mc.displayGuiScreen(null);
				this.mc.setIngameFocus();
				break;
				
			case 5:
				this.mc.theWorld = null;
				this.mc.displayGuiScreen(null);
				break;
		}

	}

	public final void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
		super.drawScreen(mouseX, mouseY);
	}
}