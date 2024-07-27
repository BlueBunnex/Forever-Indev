package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage bufferedImage1) {
		this.imageWidth = 64;
		this.imageHeight = 32;
		BufferedImage bufferedImage2;
		Graphics graphics3;
		(graphics3 = (bufferedImage2 = new BufferedImage(this.imageWidth, this.imageHeight, 2)).getGraphics()).drawImage(bufferedImage1, 0, 0, (ImageObserver)null);
		graphics3.dispose();
		this.imageData = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
		this.setAreaOpaque(0, 0, 32, 16);
		this.setAreaTransparent(32, 0, 64, 32);
		this.setAreaOpaque(0, 16, 64, 32);
		return bufferedImage2;
	}

	private void setAreaTransparent(int i1, int i2, int i3, int i4) {
		byte b5 = 32;
		byte b11 = 64;
		byte b10 = 0;
		byte b9 = 32;
		ImageBufferDownload imageBufferDownload8 = this;
		i2 = b9;

		boolean z10000;
		label43:
		while(true) {
			if(i2 >= b11) {
				z10000 = false;
				break;
			}

			for(int i6 = b10; i6 < b5; ++i6) {
				if(imageBufferDownload8.imageData[i2 + i6 * imageBufferDownload8.imageWidth] >>> 24 < 128) {
					z10000 = true;
					break label43;
				}
			}

			++i2;
		}

		if(!z10000) {
			for(i1 = 32; i1 < 64; ++i1) {
				for(i2 = 0; i2 < 32; ++i2) {
					this.imageData[i1 + i2 * this.imageWidth] &= 0xFFFFFF;
				}
			}

		}
	}

	private void setAreaOpaque(int i1, int i2, int i3, int i4) {
		for(i1 = 0; i1 < i3; ++i1) {
			for(int i5 = i2; i5 < i4; ++i5) {
				this.imageData[i1 + i5 * this.imageWidth] |= 0xFF000000;
			}
		}

	}
}