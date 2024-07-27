package net.minecraft.client.render;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

final class ThreadDownloadImage extends Thread {
	private String location;
	private ImageBufferDownload buffer;
	private ThreadDownloadImageData imageData;

	ThreadDownloadImage(ThreadDownloadImageData threadDownloadImageData, String string2, ImageBufferDownload imageBufferDownload) {
		this.imageData = threadDownloadImageData;
		this.location = string2;
		this.buffer = imageBufferDownload;
	}

	public final void run() {
		HttpURLConnection httpURLConnection1 = null;

		try {
			(httpURLConnection1 = (HttpURLConnection)(new URL(this.location)).openConnection()).setDoInput(true);
			httpURLConnection1.setDoOutput(false);
			httpURLConnection1.connect();
			if(httpURLConnection1.getResponseCode() != 404) {
				if(this.buffer == null) {
					this.imageData.image = ImageIO.read(httpURLConnection1.getInputStream());
				} else {
					this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(httpURLConnection1.getInputStream()));
				}

				return;
			}
		} catch (Exception exception5) {
			exception5.printStackTrace();
			return;
		} finally {
			httpURLConnection1.disconnect();
		}

	}
}