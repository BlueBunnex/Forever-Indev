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

	public RenderEngine(GameSettings var1) {
		this.options = var1;
	}

	public final int getTexture(String var1) {
		Integer var2 = (Integer)this.textureMap.get(var1);
		if(var2 != null) {
			return var2.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				if(var1.startsWith("##")) {
					this.setupTexture(unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(var1.substring(2)))), var4);
				} else if(var1.startsWith("%%")) {
					this.clampTexture = true;
					this.setupTexture(ImageIO.read(RenderEngine.class.getResourceAsStream(var1.substring(2))), var4);
					this.clampTexture = false;
				} else {
					this.setupTexture(ImageIO.read(RenderEngine.class.getResourceAsStream(var1)), var4);
				}

				this.textureMap.put(var1, Integer.valueOf(var4));
				return var4;
			} catch (IOException var3) {
				throw new RuntimeException("!!");
			}
		}
	}

	private static BufferedImage unwrapImageByColumns(BufferedImage var0) {
		int var1 = var0.getWidth() / 16;
		BufferedImage var2 = new BufferedImage(16, var0.getHeight() * var1, 2);
		Graphics var3 = var2.getGraphics();

		for(int var4 = 0; var4 < var1; ++var4) {
			var3.drawImage(var0, -var4 << 4, var4 * var0.getHeight(), (ImageObserver)null);
		}

		var3.dispose();
		return var2;
	}

private void setupTexture(BufferedImage var1, int var2) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    if (this.clampTexture) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
    } else {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    int width = var1.getWidth();
    int height = var1.getHeight();
    int[] pixels = new int[width * height];
    byte[] imageDataArray = new byte[width * height * 4]; // RGBA

    var1.getRGB(0, 0, width, height, pixels, 0, width);

    for (int i = 0; i < pixels.length; i++) {
        int alpha = (pixels[i] >>> 24) & 0xFF;
        int red = (pixels[i] >> 16) & 0xFF;
        int green = (pixels[i] >> 8) & 0xFF;
        int blue = pixels[i] & 0xFF;

        imageDataArray[i * 4] = (byte) red;
        imageDataArray[i * 4 + 1] = (byte) green;
        imageDataArray[i * 4 + 2] = (byte) blue;
        imageDataArray[i * 4 + 3] = (byte) alpha;
    }

    // Ensure buffer is correctly sized and initialized
    ByteBuffer buffer = BufferUtils.createByteBuffer(imageDataArray.length);
    buffer.put(imageDataArray);
    buffer.flip(); // Set position to 0 and limit to the end

    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
}

	public final int getTextureForDownloadableImage(String var1, String var2) {
		ThreadDownloadImageData var6 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
		if(var6 != null && var6.image != null && !var6.textureSetupComplete) {
			if(var6.textureName < 0) {
				BufferedImage var4 = var6.image;
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int var5 = this.singleIntBuffer.get(0);
				this.setupTexture(var4, var5);
				this.textureContentsMap.put(Integer.valueOf(var5), var4);
				var6.textureName = var5;
			} else {
				this.setupTexture(var6.image, var6.textureName);
			}

			var6.textureSetupComplete = true;
		}

		return var6 != null && var6.textureName >= 0 ? var6.textureName : this.getTexture(var2);
	}

	public final ThreadDownloadImageData obtainImageData(String var1, ImageBufferDownload var2) {
		ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
		if(var3 == null) {
			this.urlToImageDataMap.put(var1, new ThreadDownloadImageData(var1, var2));
		} else {
			++var3.referenceCount;
		}

		return var3;
	}

	public final void releaseImageData(String var1) {
		ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
		if(var2 != null) {
			--var2.referenceCount;
			if(var2.referenceCount == 0) {
				if(var2.textureName >= 0) {
					int var3 = var2.textureName;
					this.textureContentsMap.remove(Integer.valueOf(var3));
					this.singleIntBuffer.clear();
					this.singleIntBuffer.put(var3);
					this.singleIntBuffer.flip();
					GL11.glDeleteTextures(this.singleIntBuffer);
				}

				this.urlToImageDataMap.remove(var1);
			}
		}

	}

	public final void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}

	public final void updateDynamicTextures() {
		int var1;
		TextureFX var2;
		for(var1 = 0; var1 < this.textureList.size(); ++var1) {
			var2 = (TextureFX)this.textureList.get(var1);
			var2.anaglyphEnabled = this.options.anaglyph;
			var2.onTick();
			this.imageData.clear();
			this.imageData.put(var2.imageData);
			this.imageData.position(0).limit(var2.imageData.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var2.iconIndex % 16 << 4, var2.iconIndex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
		}

		for(var1 = 0; var1 < this.textureList.size(); ++var1) {
			var2 = (TextureFX)this.textureList.get(var1);
			if(var2.textureId > 0) {
				this.imageData.clear();
				this.imageData.put(var2.imageData);
				this.imageData.position(0).limit(var2.imageData.length);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2.textureId);
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
			}
		}

	}

	public final void refreshTextures() {
		Iterator var1 = this.textureContentsMap.keySet().iterator();

		int var2;
		BufferedImage var3;
		while(var1.hasNext()) {
			var2 = ((Integer)var1.next()).intValue();
			var3 = (BufferedImage)this.textureContentsMap.get(Integer.valueOf(var2));
			this.setupTexture(var3, var2);
		}

		ThreadDownloadImageData var5;
		for(var1 = this.urlToImageDataMap.values().iterator(); var1.hasNext(); var5.textureSetupComplete = false) {
			var5 = (ThreadDownloadImageData)var1.next();
		}

		var1 = this.textureMap.keySet().iterator();

		while(var1.hasNext()) {
			String var6 = (String)var1.next();

			try {
				if(var6.startsWith("##")) {
					var3 = unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(var6.substring(2))));
				} else if(var6.startsWith("%%")) {
					this.clampTexture = true;
					var3 = ImageIO.read(RenderEngine.class.getResourceAsStream(var6.substring(2)));
					this.clampTexture = false;
				} else {
					var3 = ImageIO.read(RenderEngine.class.getResourceAsStream(var6));
				}

				var2 = ((Integer)this.textureMap.get(var6)).intValue();
				this.setupTexture(var3, var2);
			} catch (IOException var4) {
				var4.printStackTrace();
			}
		}

	}

	public static void bindTexture(int var0) {
		if(var0 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var0);
		}
	}
}