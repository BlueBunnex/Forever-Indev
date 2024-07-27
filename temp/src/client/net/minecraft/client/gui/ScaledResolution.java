package net.minecraft.client.gui;

public final class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;

	public ScaledResolution(int scaledWidth, int scaledHeight) {
		this.scaledWidth = scaledWidth;
		this.scaledHeight = scaledHeight;

		for(scaledWidth = 1; this.scaledWidth / (scaledWidth + 1) >= 320 && this.scaledHeight / (scaledWidth + 1) >= 240; ++scaledWidth) {
		}

		this.scaledWidth /= scaledWidth;
		this.scaledHeight /= scaledWidth;
	}

	public final int getScaledWidth() {
		return this.scaledWidth;
	}

	public final int getScaledHeight() {
		return this.scaledHeight;
	}
}