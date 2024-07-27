package net.minecraft.client.gui;

import org.lwjgl.input.Keyboard;

public final class GuiNameLevel extends GuiScreen {
	private GuiScreen parent;
	private String title = "Enter level name:";
	private int id;
	private String name;
	private int counter = 0;

	public GuiNameLevel(GuiScreen guiScreen1, String name, int id) {
		this.parent = guiScreen1;
		this.id = id;
		this.name = name;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void updateScreen() {
		++this.counter;
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 0 && this.name.trim().length() > 1) {
				this.name.trim();
				this.mc.displayGuiScreen((GuiScreen)null);
				this.mc.setIngameFocus();
			}

			if(guiButton1.id == 1) {
				this.mc.displayGuiScreen(this.parent);
			}

		}
	}

	protected final void keyTyped(char c1, int i2) {
		if(i2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(c1) >= 0 && this.name.length() < 64) {
			this.name = this.name + c1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
		int i4 = this.width / 2 - 100;
		int i5 = this.height / 2 - 10;
		drawRect(i4 - 1, i5 - 1, i4 + 200 + 1, i5 + 20 + 1, -6250336);
		drawRect(i4, i5, i4 + 200, i5 + 20, 0xFF000000);
		drawString(this.fontRenderer, this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), i4 + 4, i5 + 6, 14737632);
		super.drawScreen(xSize_lo, ySize_lo, f3);
	}
}