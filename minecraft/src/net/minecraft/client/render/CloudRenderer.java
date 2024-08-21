package net.minecraft.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.player.EntityPlayer;

import java.util.Random;

public class CloudRenderer {
    private static final int CLOUD_SIZE = 512;
    private static final int CHUNK_RADIUS = 2048;
    private final CloudChunk[] chunks;
    private final CloudTexture cloudTexture;

    public CloudRenderer(RenderEngine renderEngine) {
        chunks = new CloudChunk[calculateChunkCapacity()];
        cloudTexture = new CloudTexture(renderEngine);
        initializeChunks();
    }

    private int calculateChunkCapacity() {
        int radius = CHUNK_RADIUS / CLOUD_SIZE;
        return (radius * 2 + 1) * (radius * 2 + 1);
    }

    private void initializeChunks() {
        for (int i = 0; i < chunks.length; i++) {
            chunks[i] = new CloudChunk();
        }
    }

    public void render(float delta, Minecraft mc) {
        EntityPlayer player = mc.thePlayer;
        double playerX = player.posX;
        double playerZ = player.posZ;
        float cloudHeight = (float) mc.theWorld.cloudHeight;

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        cloudTexture.bindAndUpdate(mc.theWorld.getSkyColor(delta));

        for (CloudChunk chunk : chunks) {
            chunk.render(playerX, playerZ, cloudHeight);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
    }
}
