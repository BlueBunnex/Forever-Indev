package net.minecraft.game.level.generator.noise;

import java.util.Random;

public final class NoiseGeneratorOctaves extends NoiseGenerator {
	private NoiseGeneratorPerlin[] generatorCollection;
	private int octaves;

	public NoiseGeneratorOctaves(Random random1, int octaves) {
		this.octaves = octaves;
		this.generatorCollection = new NoiseGeneratorPerlin[octaves];

		for(int i3 = 0; i3 < octaves; ++i3) {
			this.generatorCollection[i3] = new NoiseGeneratorPerlin(random1);
		}

	}

	public final double generateNoise(double d1, double d3) {
		double d5 = 0.0D;
		double d7 = 1.0D;

		for(int i9 = 0; i9 < this.octaves; ++i9) {
			d5 += this.generatorCollection[i9].generateNoise(d1 / d7, d3 / d7) * d7;
			d7 *= 2.0D;
		}

		return d5;
	}
}