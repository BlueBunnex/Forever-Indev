package net.minecraft.game.level;

public class Chunk {
    public static final int CHUNK_WIDTH = 16;
    public static final int CHUNK_HEIGHT = 64;
    public static final int CHUNK_LENGTH = 16;

    private byte[] blocks;
    private byte[] data;

    public Chunk() {
        this.blocks = new byte[CHUNK_WIDTH * CHUNK_HEIGHT * CHUNK_LENGTH];
        this.data = new byte[CHUNK_WIDTH * CHUNK_HEIGHT * CHUNK_LENGTH];
    }

    public int getBlockId(int x, int y, int z) {
        if (x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_LENGTH) {
            return 0; // return air for out-of-bound access
        }
        return blocks[(y * CHUNK_LENGTH + z) * CHUNK_WIDTH + x] & 255;
    }

    public void setBlockId(int x, int y, int z, int blockId) {
        if (x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_LENGTH) {
            return; // ignore out-of-bound access
        }
        blocks[(y * CHUNK_LENGTH + z) * CHUNK_WIDTH + x] = (byte) blockId;
    }

    public int getBlockMetadata(int x, int y, int z) {
        if (x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_LENGTH) {
            return 0; // return 0 for out-of-bound access
        }
        return data[(y * CHUNK_LENGTH + z) * CHUNK_WIDTH + x] & 15;
    }

    public void setBlockMetadata(int x, int y, int z, int metadata) {
        if (x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_LENGTH) {
            return; // ignore out-of-bound access
        }
        data[(y * CHUNK_LENGTH + z) * CHUNK_WIDTH + x] = (byte) ((data[(y * CHUNK_LENGTH + z) * CHUNK_WIDTH + x] & 240) | (metadata & 15));
    }
}
