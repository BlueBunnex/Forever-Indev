package net.minecraft.game.level.generator.noise;

import java.util.Random;

import util.MathHelper;

public final class NoiseGeneratorPerlin extends NoiseGenerator {
	private int[] permutations;

	public NoiseGeneratorPerlin() {
		this(new Random());
	}

	public NoiseGeneratorPerlin(Random random1) {
		this.permutations = new int[512];

		int i2;
		for(i2 = 0; i2 < 256; this.permutations[i2] = i2++) {
		}

		for(i2 = 0; i2 < 256; ++i2) {
			int i3 = random1.nextInt(256 - i2) + i2;
			int i4 = this.permutations[i2];
			this.permutations[i2] = this.permutations[i3];
			this.permutations[i3] = i4;
			this.permutations[i2 + 256] = this.permutations[i2];
		}

	}

	private static double generateNoise(double d0) {
		return d0 * d0 * d0 * (d0 * (d0 * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double d0, double d2, double d4) {
		return d2 + d0 * (d4 - d2);
	}

	private static double grad(int i0, double d1, double d3, double d5) {
		double d8 = (i0 &= 15) < 8 ? d1 : d3;
		double d10 = i0 < 4 ? d3 : (i0 != 12 && i0 != 14 ? d5 : d1);
		return ((i0 & 1) == 0 ? d8 : -d8) + ((i0 & 2) == 0 ? d10 : -d10);
	}

	public final double generateNoise(double d1, double d3) {
		double d10 = 0.0D;
		double d8 = d3;
		int i2 = MathHelper.floor_double(d1) & 255;
		int i21 = MathHelper.floor_double(d3) & 255;
		int i4 = MathHelper.floor_double(0.0D) & 255;
		double d6 = d1 - (double)MathHelper.floor_double(d1);
		d8 -= (double)MathHelper.floor_double(d8);
		d10 = 0.0D - (double)MathHelper.floor_double(0.0D);
		double d15 = generateNoise(d6);
		double d17 = generateNoise(d8);
		double d19 = generateNoise(d10);
		int i5 = this.permutations[i2] + i21;
		int i12 = this.permutations[i5] + i4;
		i5 = this.permutations[i5 + 1] + i4;
		i2 = this.permutations[i2 + 1] + i21;
		i21 = this.permutations[i2] + i4;
		i2 = this.permutations[i2 + 1] + i4;
		return lerp(d19, lerp(d17, lerp(d15, grad(this.permutations[i12], d6, d8, d10), grad(this.permutations[i21], d6 - 1.0D, d8, d10)), lerp(d15, grad(this.permutations[i5], d6, d8 - 1.0D, d10), grad(this.permutations[i2], d6 - 1.0D, d8 - 1.0D, d10))), lerp(d17, lerp(d15, grad(this.permutations[i12 + 1], d6, d8, d10 - 1.0D), grad(this.permutations[i21 + 1], d6 - 1.0D, d8, d10 - 1.0D)), lerp(d15, grad(this.permutations[i5 + 1], d6, d8 - 1.0D, d10 - 1.0D), grad(this.permutations[i2 + 1], d6 - 1.0D, d8 - 1.0D, d10 - 1.0D))));
	}
}