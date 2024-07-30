package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
	
	private final String title;
	private final String text;

	public GuiErrorScreen(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public final void initGui() {}

	public final void drawScreen(int mouseX, int mouseY) {
		drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
		drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 16777215);
		
		super.drawScreen(mouseX, mouseY);
	}

	protected final void keyTyped(char character, int keycode) {}
}
