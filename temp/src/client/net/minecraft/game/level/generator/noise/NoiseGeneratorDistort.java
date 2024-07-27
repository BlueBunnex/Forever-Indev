package net.minecraft.game.level.generator.noise;

public final class NoiseGeneratorDistort extends NoiseGenerator {
	private NoiseGenerator source;
	private NoiseGenerator distort;

	public NoiseGeneratorDistort(NoiseGenerator noiseGenerator1, NoiseGenerator noiseGenerator2) {
		this.source = noiseGenerator1;
		this.distort = noiseGenerator2;
	}

	public final double generateNoise(double d1, double d3) {
		return this.source.generateNoise(d1 + this.distort.generateNoise(d1, d3), d3);
	}
}