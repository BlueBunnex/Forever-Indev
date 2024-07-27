package net.minecraft.game.level;

final class MetadataChunkBlock {
	public int x;
	public int y;
	public int z;
	public int maxX;
	public int maxY;
	public int maxZ;

	public MetadataChunkBlock(Light skyBlock, int x, int y, int z, int x2, int y2, int z2) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxX = x2;
		this.maxY = y2;
		this.maxZ = z2;
	}
}