package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.texture.TextureFX;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderEngine {
	private HashMap textureMap = new HashMap();
	private HashMap textureContentsMap = new HashMap();
	private IntBuffer singleIntBuffer = BufferUtils.createIntBuffer(1);
	private ByteBuffer imageData = BufferUtils.createByteBuffer(262144);
	private List textureList = new ArrayList();
	private Map urlToImageDataMap = new HashMap();
	private GameSettings options;
	private boolean clampTexture = false;

	public RenderEngine(GameSettings gameSettings) {
		this.options = gameSettings;
	}

	public final int getTexture(String string1) {
		Integer integer2;
		if((integer2 = (Integer)this.textureMap.get(string1)) != null) {
			return integer2.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int i4 = this.singleIntBuffer.get(0);
				if(string1.startsWith("##")) {
					this.setupTexture(unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(string1.substring(2)))), i4);
				} else if(string1.startsWith("%%")) {
					this.clampTexture = true;
					this.setupTexture(ImageIO.read(RenderEngine.class.getResourceAsStream(string1.substring(2))), i4);
					this.clampTexture = false;
				} else {
					this.setupTexture(ImageIO.read(RenderEngine.class.getResourceAsStream(string1)), i4);
				}

				this.textureMap.put(string1, i4);
				return i4;
			} catch (IOException iOException3) {
				throw new RuntimeException("!!");
			}
		}
	}

	private static BufferedImage unwrapImageByColumns(BufferedImage bufferedImage0) {
		int i1 = bufferedImage0.getWidth() / 16;
		BufferedImage bufferedImage2;
		Graphics graphics3 = (bufferedImage2 = new BufferedImage(16, bufferedImage0.getHeight() * i1, 2)).getGraphics();

		for(int i4 = 0; i4 < i1; ++i4) {
			graphics3.drawImage(bufferedImage0, -i4 << 4, i4 * bufferedImage0.getHeight(), (ImageObserver)null);
		}

		graphics3.dispose();
		return bufferedImage2;
	}

	private void setupTexture(BufferedImage bufferedImage1, int i2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		i2 = bufferedImage1.getWidth();
		int i3 = bufferedImage1.getHeight();
		int[] i4 = new int[i2 * i3];
		byte[] b5 = new byte[i2 * i3 << 2];
		bufferedImage1.getRGB(0, 0, i2, i3, i4, 0, i2);

		for(int i11 = 0; i11 < i4.length; ++i11) {
			int i6 = i4[i11] >>> 24;
			int i7 = i4[i11] >> 16 & 255;
			int i8 = i4[i11] >> 8 & 255;
			int i9 = i4[i11] & 255;
			if(this.options != null && this.options.anaglyph) {
				int i10 = (i7 * 30 + i8 * 59 + i9 * 11) / 100;
				i8 = (i7 * 30 + i8 * 70) / 100;
				i9 = (i7 * 30 + i9 * 70) / 100;
				i7 = i10;
				i8 = i8;
				i9 = i9;
			}

			b5[i11 << 2] = (byte)i7;
			b5[(i11 << 2) + 1] = (byte)i8;
			b5[(i11 << 2) + 2] = (byte)i9;
			b5[(i11 << 2) + 3] = (byte)i6;
		}

		this.imageData.clear();
		this.imageData.put(b5);
		this.imageData.position(0).limit(b5.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i2, i3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
	}

	public final int getTextureForDownloadableImage(String string1, String string2) {
		ThreadDownloadImageData threadDownloadImageData6;
		if((threadDownloadImageData6 = (ThreadDownloadImageData)this.urlToImageDataMap.get(string1)) != null && threadDownloadImageData6.image != null && !threadDownloadImageData6.textureSetupComplete) {
			if(threadDownloadImageData6.textureName < 0) {
				BufferedImage bufferedImage4 = threadDownloadImageData6.image;
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int i5 = this.singleIntBuffer.get(0);
				this.setupTexture(bufferedImage4, i5);
				this.textureContentsMap.put(i5, bufferedImage4);
				threadDownloadImageData6.textureName = i5;
			} else {
				this.setupTexture(threadDownloadImageData6.image, threadDownloadImageData6.textureName);
			}

			threadDownloadImageData6.textureSetupComplete = true;
		}

		return threadDownloadImageData6 != null && threadDownloadImageData6.textureName >= 0 ? threadDownloadImageData6.textureName : this.getTexture(string2);
	}

	public final ThreadDownloadImageData obtainImageData(String string1, ImageBufferDownload imageBufferDownload) {
		ThreadDownloadImageData threadDownloadImageData3;
		if((threadDownloadImageData3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(string1)) == null) {
			this.urlToImageDataMap.put(string1, new ThreadDownloadImageData(string1, imageBufferDownload));
		} else {
			++threadDownloadImageData3.referenceCount;
		}

		return threadDownloadImageData3;
	}

	public final void releaseImageData(String string1) {
		ThreadDownloadImageData threadDownloadImageData2;
		if((threadDownloadImageData2 = (ThreadDownloadImageData)this.urlToImageDataMap.get(string1)) != null) {
			--threadDownloadImageData2.referenceCount;
			if(threadDownloadImageData2.referenceCount == 0) {
				if(threadDownloadImageData2.textureName >= 0) {
					int i3 = threadDownloadImageData2.textureName;
					this.textureContentsMap.remove(i3);
					this.singleIntBuffer.clear();
					this.singleIntBuffer.put(i3);
					this.singleIntBuffer.flip();
					GL11.glDeleteTextures(this.singleIntBuffer);
				}

				this.urlToImageDataMap.remove(string1);
			}
		}

	}

	public final void registerTextureFX(TextureFX textureFX) {
		this.textureList.add(textureFX);
		textureFX.onTick();
	}

	public final void updateDynamicTextures() {
		int i1;
		TextureFX textureFX2;
		for(i1 = 0; i1 < this.textureList.size(); ++i1) {
			(textureFX2 = (TextureFX)this.textureList.get(i1)).anaglyphEnabled = this.options.anaglyph;
			textureFX2.onTick();
			this.imageData.clear();
			this.imageData.put(textureFX2.imageData);
			this.imageData.position(0).limit(textureFX2.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, textureFX2.iconIndex % 16 << 4, textureFX2.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
		}

		for(i1 = 0; i1 < this.textureList.size(); ++i1) {
			if((textureFX2 = (TextureFX)this.textureList.get(i1)).textureId > 0) {
				this.imageData.clear();
				this.imageData.put(textureFX2.imageData);
				this.imageData.position(0).limit(textureFX2.imageData.length);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureFX2.textureId);
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
			}
		}

	}

	public final void refreshTextures() {
		Iterator iterator1 = this.textureContentsMap.keySet().iterator();

		int i2;
		BufferedImage bufferedImage3;
		while(iterator1.hasNext()) {
			i2 = ((Integer)iterator1.next()).intValue();
			bufferedImage3 = (BufferedImage)this.textureContentsMap.get(i2);
			this.setupTexture(bufferedImage3, i2);
		}

		for(iterator1 = this.urlToImageDataMap.values().iterator(); iterator1.hasNext(); ((ThreadDownloadImageData)iterator1.next()).textureSetupComplete = false) {
		}

		iterator1 = this.textureMap.keySet().iterator();

		while(iterator1.hasNext()) {
			String string5 = (String)iterator1.next();

			try {
				if(string5.startsWith("##")) {
					bufferedImage3 = unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(string5.substring(2))));
				} else if(string5.startsWith("%%")) {
					this.clampTexture = true;
					bufferedImage3 = ImageIO.read(RenderEngine.class.getResourceAsStream(string5.substring(2)));
					this.clampTexture = false;
				} else {
					bufferedImage3 = ImageIO.read(RenderEngine.class.getResourceAsStream(string5));
				}

				i2 = ((Integer)this.textureMap.get(string5)).intValue();
				this.setupTexture(bufferedImage3, i2);
			} catch (IOException iOException4) {
				iOException4.printStackTrace();
			}
		}

	}

	public static void bindTexture(int i0) {
		if(i0 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i0);
		}
	}
}