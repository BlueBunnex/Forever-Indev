package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
	private String title;
	private String text;

	public GuiErrorScreen(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public final void initGui() {
	}

	public final void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 0xFFFFFF);
		super.drawScreen(xSize_lo, ySize_lo, f3);
	}

	protected final void keyTyped(char c1, int i2) {
	}
}