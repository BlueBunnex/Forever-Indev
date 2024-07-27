package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import util.IProgressUpdate;

public class LoadingScreenRenderer implements IProgressUpdate {
	private String text;
	private Minecraft mc;
	private String title;
	private long start;

	public LoadingScreenRenderer(Minecraft minecraft1) {
		this.text = "";
		this.title = "";
		this.start = System.currentTimeMillis();
		this.mc = minecraft1;
	}

	public final void displayProgressMessage(String title) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.title = title;
			ScaledResolution title1;
			int i2 = (title1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
			int title2 = title1.getScaledHeight();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)i2, (double)title2, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void displayLoadingString(String text) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			this.start = 0L;
			this.text = text;
			this.setLoadingProgress(-1);
			this.start = 0L;
		}
	}

	public final void setLoadingProgress(int progress) {
		if(!this.mc.running) {
			throw new MinecraftError();
		} else {
			long j2;
			if((j2 = System.currentTimeMillis()) - this.start >= 20L) {
				this.start = j2;
				ScaledResolution scaledResolution8;
				int i3 = (scaledResolution8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
				int i9 = scaledResolution8.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, (double)i3, (double)i9, 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				Tessellator tessellator4 = Tessellator.instance;
				int i5 = this.mc.renderEngine.getTexture("/dirt.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
				tessellator4.startDrawingQuads();
				tessellator4.setColorOpaque_I(4210752);
				tessellator4.addVertexWithUV(0.0F, (float)i9, 0.0F, 0.0F, (float)i9 / 32.0F);
				tessellator4.addVertexWithUV((float)i3, (float)i9, 0.0F, (float)i3 / 32.0F, (float)i9 / 32.0F);
				tessellator4.addVertexWithUV((float)i3, 0.0F, 0.0F, (float)i3 / 32.0F, 0.0F);
				tessellator4.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				tessellator4.draw();
				if(progress >= 0) {
					i5 = i3 / 2 - 50;
					int i6 = i9 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator4.startDrawingQuads();
					tessellator4.setColorOpaque_I(8421504);
					tessellator4.addVertex((float)i5, (float)i6, 0.0F);
					tessellator4.addVertex((float)i5, (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + 100), (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + 100), (float)i6, 0.0F);
					tessellator4.setColorOpaque_I(8454016);
					tessellator4.addVertex((float)i5, (float)i6, 0.0F);
					tessellator4.addVertex((float)i5, (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + progress), (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + progress), (float)i6, 0.0F);
					tessellator4.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.title, (i3 - this.mc.fontRenderer.getStringWidth(this.title)) / 2, i9 / 2 - 4 - 16, 0xFFFFFF);
				this.mc.fontRenderer.drawStringWithShadow(this.text, (i3 - this.mc.fontRenderer.getStringWidth(this.text)) / 2, i9 / 2 - 4 + 8, 0xFFFFFF);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception exception7) {
				}
			}
		}
	}

	public LoadingScreenRenderer() {
	}

	public static NBTTagCompound writeLevelTags(InputStream inputstream) throws IOException {
		DataInputStream inputstream1 = new DataInputStream(new GZIPInputStream(inputstream));

		NBTTagCompound nBTTagCompound5;
		try {
			NBTBase nBTBase1;
			if(!((nBTBase1 = NBTBase.readTag(inputstream1)) instanceof NBTTagCompound)) {
				throw new IOException("Root tag must be a named compound tag");
			}

			nBTTagCompound5 = (NBTTagCompound)nBTBase1;
		} finally {
			inputstream1.close();
		}

		return nBTTagCompound5;
	}
}