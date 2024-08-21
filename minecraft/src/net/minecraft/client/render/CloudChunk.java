package net.minecraft.client.render;

import org.lwjgl.opengl.GL11;
import java.util.Random;

public class CloudChunk {
    private final int displayListID;
    private final Random random = new Random();

    public CloudChunk() {
        displayListID = GL11.glGenLists(1);
    }

    public void render(double playerX, double playerZ, float cloudHeight) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) playerX, cloudHeight, (float) playerZ);
        GL11.glCallList(displayListID);
        GL11.glPopMatrix();
    }

    public void generateFluffyClouds() {
        GL11.glNewList(displayListID, GL11.GL_COMPILE);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                float offsetX = (random.nextFloat() - 0.5F) * 5.0F;
                float offsetY = (random.nextFloat() - 0.5F) * 2.5F;
                float offsetZ = (random.nextFloat() - 0.5F) * 5.0F;

                tessellator.addVertex(x + offsetX, 0 + offsetY, z + offsetZ);
                tessellator.addVertex(x + offsetX + 1, 0 + offsetY, z + offsetZ);
                tessellator.addVertex(x + offsetX + 1, 1 + offsetY, z + offsetZ);
                tessellator.addVertex(x + offsetX, 1 + offsetY, z + offsetZ);
            }
        }

        tessellator.draw();
        GL11.glEndList();
    }
}
