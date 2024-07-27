package net.minecraft.client;

import java.nio.FloatBuffer;

import net.minecraft.game.physics.Vec3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class RenderHelper {
	private static FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(16);

	public static void disableStandardItemLighting() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
	}

	public static void enableStandardItemLighting() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		Vec3D vec3D0 = new Vec3D(0.3F, 1.0F, -0.7F);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(vec3D0.xCoord, vec3D0.yCoord, vec3D0.zCoord, 0.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(0.5F, 0.5F, 0.5F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		vec3D0 = new Vec3D(-0.7F, 1.0F, 0.2F);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(vec3D0.xCoord, vec3D0.yCoord, vec3D0.zCoord, 0.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(0.5F, 0.5F, 0.5F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(0.5F, 0.5F, 0.5F, 1.0F));
	}

	private static FloatBuffer setColorBuffer(float f0, float f1, float f2, float f3) {
		colorBuffer.clear();
		colorBuffer.put(f0).put(f1).put(f2).put(f3);
		colorBuffer.flip();
		return colorBuffer;
	}
}