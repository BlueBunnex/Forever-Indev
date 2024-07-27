package net.minecraft.client.render.texture;

import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.game.level.block.Block;

import util.MathHelper;

public class TextureGearsFX extends TextureFX {
	private int gearRotation = 0;
	private int[] gearColor = new int[1024];
	private int[] gearMiddleColor = new int[1024];
	private int gearRotationDir;

	public TextureGearsFX(int i1) {
		super(Block.cog.blockIndexInTexture + i1);
		this.gearRotationDir = (i1 << 1) - 1;
		this.gearRotation = 2;

		try {
			ImageIO.read(TextureGearsFX.class.getResource("/misc/gear.png")).getRGB(0, 0, 32, 32, this.gearColor, 0, 32);
			ImageIO.read(TextureGearsFX.class.getResource("/misc/gearmiddle.png")).getRGB(0, 0, 16, 16, this.gearMiddleColor, 0, 16);
		} catch (IOException iOException2) {
			iOException2.printStackTrace();
		}
	}

	public final void onTick() {
		this.gearRotation = this.gearRotation + this.gearRotationDir & 63;
		float f1 = MathHelper.sin((float)this.gearRotation / 64.0F * (float)Math.PI * 2.0F);
		float f2 = MathHelper.cos((float)this.gearRotation / 64.0F * (float)Math.PI * 2.0F);

		for(int i3 = 0; i3 < 16; ++i3) {
			for(int i4 = 0; i4 < 16; ++i4) {
				float f5 = ((float)i3 / 15.0F - 0.5F) * 31.0F;
				float f6 = ((float)i4 / 15.0F - 0.5F) * 31.0F;
				float f7 = f2 * f5 - f1 * f6;
				f5 = f2 * f6 + f1 * f5;
				int i11 = (int)(f7 + 16.0F);
				int i10 = (int)(f5 + 16.0F);
				int i12 = 0;
				if(i11 >= 0 && i10 >= 0 && i11 < 32 && i10 < 32) {
					i12 = this.gearColor[i11 + (i10 << 5)];
					if((i10 = this.gearMiddleColor[i3 + (i4 << 4)]) >>> 24 > 128) {
						i12 = i10;
					}
				}

				i10 = i12 >> 16 & 255;
				i11 = i12 >> 8 & 255;
				int i8 = i12 & 255;
				i12 = i12 >>> 24 > 128 ? 255 : 0;
				int i9 = i3 + (i4 << 4);
				this.imageData[i9 << 2] = (byte)i10;
				this.imageData[(i9 << 2) + 1] = (byte)i11;
				this.imageData[(i9 << 2) + 2] = (byte)i8;
				this.imageData[(i9 << 2) + 3] = (byte)i12;
			}
		}

	}
}