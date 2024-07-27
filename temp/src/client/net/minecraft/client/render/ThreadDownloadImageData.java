package net.minecraft.client.render;

import java.awt.image.BufferedImage;

public final class ThreadDownloadImageData {
	public BufferedImage image;
	public int referenceCount = 1;
	public int textureName = -1;
	public boolean textureSetupComplete = false;

	public ThreadDownloadImageData(String string1, ImageBufferDownload imageBufferDownload) {
		(new ThreadDownloadImage(this, string1, imageBufferDownload)).start();
	}
}