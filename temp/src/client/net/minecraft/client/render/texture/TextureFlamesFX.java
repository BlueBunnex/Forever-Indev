package net.minecraft.client.render.texture;

import net.minecraft.game.level.block.Block;

public final class TextureFlamesFX extends TextureFX {
	private float[] currentFireFrame = new float[320];
	private float[] lastFireFrame = new float[320];

	public TextureFlamesFX(int i1) {
		super(Block.fire.blockIndexInTexture + (i1 << 4));
	}

	public final void onTick() {
		int i1;
		int i2;
		int i3;
		int i5;
		int i6;
		for(i1 = 0; i1 < 16; ++i1) {
			for(i2 = 0; i2 < 20; ++i2) {
				i3 = 18;
				float f4 = this.currentFireFrame[i1 + ((i2 + 1) % 20 << 4)] * 18.0F;

				for(i5 = i1 - 1; i5 <= i1 + 1; ++i5) {
					for(i6 = i2; i6 <= i2 + 1; ++i6) {
						if(i5 >= 0 && i6 >= 0 && i5 < 16 && i6 < 20) {
							f4 += this.currentFireFrame[i5 + (i6 << 4)];
						}

						++i3;
					}
				}

				this.lastFireFrame[i1 + (i2 << 4)] = f4 / ((float)i3 * 1.06F);
				if(i2 >= 19) {
					this.lastFireFrame[i1 + (i2 << 4)] = (float)(Math.random() * Math.random() * Math.random() * 4.0D + Math.random() * (double)0.1F + (double)0.2F);
				}
			}
		}

		float[] f9 = this.lastFireFrame;
		this.lastFireFrame = this.currentFireFrame;
		this.currentFireFrame = f9;

		for(i2 = 0; i2 < 256; ++i2) {
			float f10;
			if((f10 = this.currentFireFrame[i2] * 1.8F) > 1.0F) {
				f10 = 1.0F;
			}

			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			i5 = (int)(f10 * 155.0F + 100.0F);
			i6 = (int)(f10 * f10 * 255.0F);
			int i7 = (int)(f10 * f10 * f10 * f10 * f10 * f10 * f10 * f10 * f10 * f10 * 255.0F);
			short s8 = 255;
			if(f10 < 0.5F) {
				s8 = 0;
			}

			if(this.anaglyphEnabled) {
				i1 = (i5 * 30 + i6 * 59 + i7 * 11) / 100;
				i3 = (i5 * 30 + i6 * 70) / 100;
				int i11 = (i5 * 30 + i7 * 70) / 100;
				i5 = i1;
				i6 = i3;
				i7 = i11;
			}

			this.imageData[i2 << 2] = (byte)i5;
			this.imageData[(i2 << 2) + 1] = (byte)i6;
			this.imageData[(i2 << 2) + 2] = (byte)i7;
			this.imageData[(i2 << 2) + 3] = (byte)s8;
		}

	}
}