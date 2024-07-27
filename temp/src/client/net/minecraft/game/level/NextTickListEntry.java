package net.minecraft.game.level;

public final class NextTickListEntry {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int blockID;
	public int scheduledTime;

	public NextTickListEntry(int x, int y, int z, int block) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.blockID = block;
	}
}