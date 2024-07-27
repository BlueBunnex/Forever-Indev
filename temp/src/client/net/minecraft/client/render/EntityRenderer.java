package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.ClippingHelperImplementation;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.camera.IsomCamera;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import util.MathHelper;

public final class EntityRenderer {
	private Minecraft mc;
	private boolean anaglyphEnable = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private int entityRendererInt1;
	private int entityRendererInt2;
	private DecimalFormat entityDecimalFormat = new DecimalFormat("0000");
	private ByteBuffer entityByteBuffer;
	private FloatBuffer entityFloatBuffer = BufferUtils.createFloatBuffer(16);
	private Random random = new Random();
	private volatile int unusedInt0 = 0;
	private volatile int unusedInt1 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float prevFogColor;
	private float fogColor;

	public EntityRenderer(Minecraft minecraft) {
		this.mc = minecraft;
		this.itemRenderer = new ItemRenderer(minecraft);
	}

	public final void updateRenderer() {
		this.prevFogColor = this.fogColor;
		float f1 = this.mc.theWorld.getLightBrightness((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ);
		float f2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
		f1 = f1 * (1.0F - f2) + f2;
		this.fogColor += (f1 - this.fogColor) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
	}

	private Vec3D orientCamera(float f1) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		float f3 = this.mc.thePlayer.prevPosX + (entityPlayerSP2.posX - entityPlayerSP2.prevPosX) * f1;
		float f4 = entityPlayerSP2.prevPosY + (entityPlayerSP2.posY - entityPlayerSP2.prevPosY) * f1;
		f1 = entityPlayerSP2.prevPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.prevPosZ) * f1;
		return new Vec3D(f3, f4, f1);
	}

	private void hurtCameraEffect(float f1) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		float f3 = (float)this.mc.thePlayer.hurtTime - f1;
		if(entityPlayerSP2.health <= 0) {
			f1 += (float)entityPlayerSP2.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f3 >= 0.0F) {
			f3 = MathHelper.sin((f3 /= (float)entityPlayerSP2.maxHurtTime) * f3 * f3 * f3 * (float)Math.PI);
			f1 = entityPlayerSP2.attackedAtYaw;
			GL11.glRotatef(-entityPlayerSP2.attackedAtYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float f1) {
		if(!this.mc.options.thirdPersonView) {
			EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
			float f3 = this.mc.thePlayer.distanceWalkedModified - entityPlayerSP2.prevDistanceWalkedModified;
			f3 = entityPlayerSP2.distanceWalkedModified + f3 * f1;
			float f4 = entityPlayerSP2.prevCameraYaw + (entityPlayerSP2.cameraYaw - entityPlayerSP2.prevCameraYaw) * f1;
			f1 = entityPlayerSP2.prevCameraPitch + (entityPlayerSP2.cameraPitch - entityPlayerSP2.prevCameraPitch) * f1;
			GL11.glTranslatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5F, -Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0F);
			GL11.glRotatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f3 * (float)Math.PI + 0.2F) * f4) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
		}
	}

	public final void updateCameraAndRender(float f1) {
		if(this.anaglyphEnable && !Display.isActive()) {
			this.mc.displayInGameMenu();
		}

		this.anaglyphEnable = Display.isActive();
		int i5;
		int i6;
		if(this.mc.inventoryScreen) {
			Mouse.getDX();
			byte b2 = 0;
			Mouse.getDY();
			byte b3 = 0;
			this.mc.mouseHelper.ungrabMouseCursor();
			byte b4 = 1;
			if(this.mc.options.invertMouse) {
				b4 = -1;
			}

			i5 = b2 + this.mc.mouseHelper.deltaX;
			i6 = b3 - this.mc.mouseHelper.deltaY;
			if(b2 != 0 || this.entityRendererInt1 != 0) {
				System.out.println("xxo: " + b2 + ", " + this.entityRendererInt1 + ": " + this.entityRendererInt1 + ", xo: " + i5);
			}

			if(this.entityRendererInt1 != 0) {
				this.entityRendererInt1 = 0;
			}

			if(this.entityRendererInt2 != 0) {
				this.entityRendererInt2 = 0;
			}

			if(b2 != 0) {
				this.entityRendererInt1 = b2;
			}

			if(b3 != 0) {
				this.entityRendererInt2 = b3;
			}

			float f10001 = (float)i5;
			float f11 = (float)(i6 * b4);
			float f9 = f10001;
			EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
			float f13 = this.mc.thePlayer.rotationPitch;
			float f14 = entityPlayerSP7.rotationYaw;
			entityPlayerSP7.rotationYaw = (float)((double)entityPlayerSP7.rotationYaw + (double)f9 * 0.15D);
			entityPlayerSP7.rotationPitch = (float)((double)entityPlayerSP7.rotationPitch - (double)f11 * 0.15D);
			if(entityPlayerSP7.rotationPitch < -90.0F) {
				entityPlayerSP7.rotationPitch = -90.0F;
			}

			if(entityPlayerSP7.rotationPitch > 90.0F) {
				entityPlayerSP7.rotationPitch = 90.0F;
			}

			entityPlayerSP7.prevRotationPitch += entityPlayerSP7.rotationPitch - f13;
			entityPlayerSP7.prevRotationYaw += entityPlayerSP7.rotationYaw - f14;
		}

		ScaledResolution scaledResolution8;
		int i10 = (scaledResolution8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
		int i12 = scaledResolution8.getScaledHeight();
		i5 = Mouse.getX() * i10 / this.mc.displayWidth;
		i6 = i12 - Mouse.getY() * i12 / this.mc.displayHeight - 1;
		if(this.mc.theWorld != null) {
			this.getMouseOver(f1);
			this.mc.ingameGUI.renderGameOverlay(f1);
		} else {
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			this.setupOverlayRendering();
		}

		if(this.mc.currentScreen != null) {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			this.mc.currentScreen.drawScreen(i5, i6, f1);
		}

		Thread.yield();
		Display.update();
	}

	public final void grabLargeScreenshot() {
		this.mc.loadingScreen.displayProgressMessage("Grabbing large screenshot");
		File file1 = new File(System.getProperty("user.home", "."));

		int i2;
		File file3;
		for(i2 = 0; (file3 = new File(file1, "mc_map_" + this.entityDecimalFormat.format((long)i2) + ".png")).exists(); ++i2) {
		}

		file3 = file3.getAbsoluteFile();
		this.mc.loadingScreen.displayLoadingString("Rendering");
		this.mc.loadingScreen.setLoadingProgress(0);

		try {
			int i19 = (this.mc.theWorld.width << 4) + (this.mc.theWorld.length << 4);
			i2 = (this.mc.theWorld.height << 4) + i19 / 2;
			BufferedImage bufferedImage4;
			Graphics graphics5 = (bufferedImage4 = new BufferedImage(i19, i2, 1)).getGraphics();
			int i6 = this.mc.displayWidth;
			int i7 = this.mc.displayHeight;
			int i8 = (i19 / i6 + 1) * (i2 / i7 + 1);
			int i9 = 0;

			for(int i10 = 0; i10 < i19; i10 += i6) {
				for(int i11 = 0; i11 < i2; i11 += i7) {
					++i9;
					this.mc.loadingScreen.setLoadingProgress(i9 * 100 / i8);
					int i10001 = i10 - i19 / 2;
					int i10002 = i11 - i2 / 2;
					float f12 = 0.0F;
					int i14 = i10002;
					int i13 = i10001;
					if(this.entityByteBuffer == null) {
						this.entityByteBuffer = BufferUtils.createByteBuffer(this.mc.displayWidth * this.mc.displayHeight << 2);
					}

					EntityPlayerSP entityPlayerSP15 = this.mc.thePlayer;
					World world16 = this.mc.theWorld;
					RenderGlobal renderGlobal17 = this.mc.renderGlobal;
					GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
					this.updateFogColor(0.0F);
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					GL11.glEnable(GL11.GL_CULL_FACE);
					this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glLoadIdentity();
					GL11.glOrtho(0.0D, (double)this.mc.displayWidth, 0.0D, (double)this.mc.displayHeight, 10.0D, 10000.0D);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glLoadIdentity();
					GL11.glTranslatef((float)(-i13), (float)(-i14), -5000.0F);
					GL11.glScalef(16.0F, -16.0F, -16.0F);
					float f22 = 1.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = 0.5F;
					f22 = 1.0F;
					f22 = 0.0F;
					f22 = -1.0F;
					f22 = 1.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = 0.0F;
					f22 = -0.5F;
					f22 = 1.0F;
					this.entityFloatBuffer.clear();
					this.entityFloatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
					this.entityFloatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
					this.entityFloatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
					this.entityFloatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
					this.entityFloatBuffer.flip();
					GL11.glMultMatrix(this.entityFloatBuffer);
					GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef((float)(-world16.width) / 2.0F, (float)(-world16.height) / 2.0F, (float)(-world16.length) / 2.0F);
					IsomCamera isomCamera24 = new IsomCamera();
					this.mc.renderGlobal.clipRenderersByFrustrum(isomCamera24);
					this.mc.renderGlobal.updateRenderers(entityPlayerSP15);
					this.setupFog();
					GL11.glEnable(GL11.GL_FOG);
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
					float f23 = (float)world16.height * 8.0F;
					GL11.glFogf(GL11.GL_FOG_START, 5000.0F - f23);
					GL11.glFogf(GL11.GL_FOG_END, 5000.0F + f23 * 8.0F);
					RenderHelper.enableStandardItemLighting();
					renderGlobal17.renderEntities(this.orientCamera(0.0F), isomCamera24, 0.0F);
					RenderHelper.disableStandardItemLighting();
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
					renderGlobal17.sortAndRender(entityPlayerSP15, 0);
					renderGlobal17.oobGroundRenderer();
					if(world16.cloudHeight < world16.height) {
						renderGlobal17.renderSky(0.0F);
					}

					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColorMask(false, false, false, false);
					i13 = renderGlobal17.sortAndRender(entityPlayerSP15, 1);
					GL11.glColorMask(true, true, true, true);
					if(i13 > 0) {
						renderGlobal17.renderAllRenderLists();
					}

					if(world16.getGroundLevel() >= 0) {
						renderGlobal17.oobWaterRenderer();
					}

					GL11.glDepthMask(true);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_FOG);
					this.entityByteBuffer.clear();
					GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
					GL11.glReadPixels(0, 0, this.mc.displayWidth, this.mc.displayHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, this.entityByteBuffer);
					BufferedImage bufferedImage21 = screenshotBuffer(this.entityByteBuffer, i6, i7);
					graphics5.drawImage(bufferedImage21, i10, i11, (ImageObserver)null);
				}
			}

			graphics5.dispose();
			this.mc.loadingScreen.displayLoadingString("Saving as " + file3.toString());
			this.mc.loadingScreen.setLoadingProgress(100);
			FileOutputStream fileOutputStream20 = new FileOutputStream(file3);
			ImageIO.write(bufferedImage4, "png", fileOutputStream20);
			fileOutputStream20.close();
		} catch (Throwable throwable18) {
			throwable18.printStackTrace();
		}
	}

	private static BufferedImage screenshotBuffer(ByteBuffer byteBuffer0, int i1, int i2) {
		byteBuffer0.position(0).limit(i1 * i2 << 2);
		BufferedImage bufferedImage3;
		int[] i4 = ((DataBufferInt)(bufferedImage3 = new BufferedImage(i1, i2, 1)).getRaster().getDataBuffer()).getData();

		for(int i5 = 0; i5 < i1 * i2; ++i5) {
			int i6 = byteBuffer0.get(i5 * 3) & 255;
			int i7 = byteBuffer0.get(i5 * 3 + 1) & 255;
			int i8 = byteBuffer0.get(i5 * 3 + 2) & 255;
			i4[i5] = i6 << 16 | i7 << 8 | i8;
		}

		return bufferedImage3;
	}

	private void getMouseOver(float f1) {
		EntityRenderer entityRenderer7 = this;
		EntityPlayerSP entityPlayerSP9;
		float f2 = (entityPlayerSP9 = this.mc.thePlayer).prevRotationPitch + (entityPlayerSP9.rotationPitch - entityPlayerSP9.prevRotationPitch) * f1;
		float f10 = entityPlayerSP9.prevRotationYaw + (entityPlayerSP9.rotationYaw - entityPlayerSP9.prevRotationYaw) * f1;
		Vec3D vec3D11 = this.orientCamera(f1);
		float f12 = MathHelper.cos(-f10 * 0.017453292F - (float)Math.PI);
		float f13 = MathHelper.sin(-f10 * 0.017453292F - (float)Math.PI);
		float f14 = -MathHelper.cos(-f2 * 0.017453292F);
		float f15 = MathHelper.sin(-f2 * 0.017453292F);
		float f16 = f13 * f14;
		float f17 = f12 * f14;
		float f18 = this.mc.playerController.getBlockReachDistance();
		Vec3D vec3D19 = vec3D11.addVector(f16 * f18, f15 * f18, f17 * f18);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(vec3D11, vec3D19);
		float f20 = f18;
		vec3D11 = this.orientCamera(f1);
		if(this.mc.objectMouseOver != null) {
			f20 = this.mc.objectMouseOver.hitVec.distance(vec3D11);
		}

		if(this.mc.playerController instanceof PlayerControllerCreative) {
			f18 = 32.0F;
		} else {
			if(f20 > 3.0F) {
				f20 = 3.0F;
			}

			f18 = f20;
		}

		vec3D19 = vec3D11.addVector(f16 * f18, f15 * f18, f17 * f18);
		this.pointedEntity = null;
		List list8 = this.mc.theWorld.entityMap.getEntitiesWithinAABB(entityPlayerSP9, entityPlayerSP9.boundingBox.addCoord(f16 * f18, f15 * f18, f17 * f18));
		float f27 = 0.0F;

		for(int i40 = 0; i40 < list8.size(); ++i40) {
			Entity entity21;
			MovingObjectPosition movingObjectPosition22;
			if((entity21 = (Entity)list8.get(i40)).canBeCollidedWith() && (movingObjectPosition22 = entity21.boundingBox.expand(0.1F, 0.1F, 0.1F).calculateIntercept(vec3D11, vec3D19)) != null && ((f2 = vec3D11.distance(movingObjectPosition22.hitVec)) < f27 || f27 == 0.0F)) {
				entityRenderer7.pointedEntity = entity21;
				f27 = f2;
			}
		}

		if(entityRenderer7.pointedEntity != null && !(entityRenderer7.mc.playerController instanceof PlayerControllerCreative)) {
			entityRenderer7.mc.objectMouseOver = new MovingObjectPosition(entityRenderer7.pointedEntity);
		}

		for(int i23 = 0; i23 < 2; ++i23) {
			if(this.mc.options.anaglyph) {
				if(i23 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			EntityPlayerSP entityPlayerSP3 = this.mc.thePlayer;
			World world4 = this.mc.theWorld;
			RenderGlobal renderGlobal5 = this.mc.renderGlobal;
			EffectRenderer effectRenderer6 = this.mc.effectRenderer;
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(f1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			float f26 = f1;
			this.farPlaneDistance = (float)(512 >> (this.mc.options.renderDistance << 1));
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)(-((i23 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
			}

			EntityPlayerSP entityPlayerSP33 = this.mc.thePlayer;
			f13 = 70.0F;
			if(entityPlayerSP33.isInsideOfWater()) {
				f13 = 60.0F;
			}

			if(entityPlayerSP33.health <= 0) {
				f14 = (float)entityPlayerSP33.deathTime + f1;
				f13 /= (1.0F - 500.0F / (f14 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(f13, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((i23 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(f1);
			if(this.mc.options.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			EntityRenderer entityRenderer29 = this;
			float f10001 = entityPlayerSP33.posX - entityPlayerSP33.prevPosX;
			f13 = (entityPlayerSP33 = this.mc.thePlayer).prevPosX + f10001 * f1;
			f14 = entityPlayerSP33.prevPosY + (entityPlayerSP33.posY - entityPlayerSP33.prevPosY) * f1;
			f15 = entityPlayerSP33.prevPosZ + (entityPlayerSP33.posZ - entityPlayerSP33.prevPosZ) * f1;
			if(!this.mc.options.thirdPersonView) {
				GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			} else {
				f16 = 4.0F;
				float f24 = -MathHelper.sin(entityPlayerSP33.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP33.rotationPitch / 180.0F * (float)Math.PI) * 4.0F;
				f17 = MathHelper.cos(entityPlayerSP33.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP33.rotationPitch / 180.0F * (float)Math.PI) * 4.0F;
				f18 = -MathHelper.sin(entityPlayerSP33.rotationPitch / 180.0F * (float)Math.PI) * 4.0F;

				for(int i38 = 0; i38 < 8; ++i38) {
					f20 = (float)(((i38 & 1) << 1) - 1);
					f26 = (float)(((i38 >> 1 & 1) << 1) - 1);
					f27 = (float)(((i38 >> 2 & 1) << 1) - 1);
					f20 *= 0.1F;
					f26 *= 0.1F;
					f27 *= 0.1F;
					float f39;
					MovingObjectPosition movingObjectPosition41;
					if((movingObjectPosition41 = entityRenderer29.mc.theWorld.rayTraceBlocks(new Vec3D(f13 + f20, f14 + f26, f15 + f27), new Vec3D(f13 - f24 + f20 + f27, f14 - f18 + f26, f15 - f17 + f27))) != null && (f39 = movingObjectPosition41.hitVec.distance(new Vec3D(f13, f14, f15))) < f16) {
						f16 = f39;
					}
				}

				GL11.glTranslatef(0.0F, 0.0F, -f16);
			}

			GL11.glRotatef(entityPlayerSP33.prevRotationPitch + (entityPlayerSP33.rotationPitch - entityPlayerSP33.prevRotationPitch) * f26, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityPlayerSP33.prevRotationYaw + (entityPlayerSP33.rotationYaw - entityPlayerSP33.prevRotationYaw) * f26 + 180.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-f13, -f14, -f15);
			ClippingHelperImplementation.init();
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			renderGlobal5.renderSky(f1);
			this.setupFog();
			Frustrum frustrum25 = new Frustrum();
			this.mc.renderGlobal.clipRenderersByFrustrum(frustrum25);
			this.mc.renderGlobal.updateRenderers(entityPlayerSP3);
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			renderGlobal5.sortAndRender(entityPlayerSP3, 0);
			int i28;
			if(world4.isSolid(entityPlayerSP3.posX, entityPlayerSP3.posY, entityPlayerSP3.posZ, 0.1F)) {
				i28 = (int)entityPlayerSP3.posX;
				int i30 = (int)entityPlayerSP3.posY;
				int i31 = (int)entityPlayerSP3.posZ;
				RenderBlocks renderBlocks32 = new RenderBlocks(world4);

				for(int i34 = i28 - 1; i34 <= i28 + 1; ++i34) {
					for(int i36 = i30 - 1; i36 <= i30 + 1; ++i36) {
						for(int i35 = i31 - 1; i35 <= i31 + 1; ++i35) {
							int i37;
							if((i37 = world4.getBlockId(i34, i36, i35)) > 0) {
								renderBlocks32.renderBlockAllFaces(Block.blocksList[i37], i34, i36, i35);
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			renderGlobal5.renderEntities(this.orientCamera(f1), frustrum25, f1);
			effectRenderer6.renderLitParticles(f1);
			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			effectRenderer6.renderParticles(entityPlayerSP3, f1);
			renderGlobal5.oobGroundRenderer();
			if(this.mc.objectMouseOver != null && entityPlayerSP3.isInsideOfWater()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderGlobal5.drawBlockBreaking(this.mc.objectMouseOver, 0, entityPlayerSP3.inventory.getCurrentItem());
				renderGlobal5.drawSelectionBox(this.mc.objectMouseOver, 0);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			renderGlobal5.oobWaterRenderer();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColorMask(false, false, false, false);
			i28 = renderGlobal5.sortAndRender(entityPlayerSP3, 1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.options.anaglyph) {
				if(i23 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(i28 > 0) {
				renderGlobal5.renderAllRenderLists();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.mc.objectMouseOver != null && !entityPlayerSP3.isInsideOfWater()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderGlobal5.drawBlockBreaking(this.mc.objectMouseOver, 0, entityPlayerSP3.inventory.getCurrentItem());
				renderGlobal5.drawSelectionBox(this.mc.objectMouseOver, 0);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL11.GL_FOG);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.options.anaglyph) {
				GL11.glTranslatef((float)((i23 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(f1);
			if(this.mc.options.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			if(!this.mc.options.thirdPersonView) {
				this.itemRenderer.renderItemInFirstPerson(f1);
			}

			GL11.glPopMatrix();
			if(!this.mc.options.thirdPersonView) {
				this.itemRenderer.renderOverlays(f1);
				this.hurtCameraEffect(f1);
			}

			if(this.mc.options.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			if(!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void setupOverlayRendering() {
		ScaledResolution scaledResolution1;
		int i2 = (scaledResolution1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
		int i3 = scaledResolution1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)i2, (double)i3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float f1) {
		World world2 = this.mc.theWorld;
		EntityPlayerSP entityPlayerSP3 = this.mc.thePlayer;
		float f4 = 1.0F / (float)(4 - this.mc.options.renderDistance);
		f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
		Vec3D vec3D5;
		float f6 = (vec3D5 = world2.getSkyColor(f1)).xCoord;
		float f7 = vec3D5.yCoord;
		float f13 = vec3D5.zCoord;
		Vec3D vec3D8 = world2.getFogColor(f1);
		this.fogColorRed = vec3D8.xCoord;
		this.fogColorGreen = vec3D8.yCoord;
		this.fogColorBlue = vec3D8.zCoord;
		this.fogColorRed += (f6 - this.fogColorRed) * f4;
		this.fogColorGreen += (f7 - this.fogColorGreen) * f4;
		this.fogColorBlue += (f13 - this.fogColorBlue) * f4;
		Block block9;
		if((block9 = Block.blocksList[world2.getBlockId((int)entityPlayerSP3.posX, (int)(entityPlayerSP3.posY + 0.12F), (int)entityPlayerSP3.posZ)]) != null && block9.material != Material.air) {
			Material material10;
			if((material10 = block9.material) == Material.water) {
				this.fogColorRed = 0.02F;
				this.fogColorGreen = 0.02F;
				this.fogColorBlue = 0.2F;
			} else if(material10 == Material.lava) {
				this.fogColorRed = 0.6F;
				this.fogColorGreen = 0.1F;
				this.fogColorBlue = 0.0F;
			}
		}

		float f11 = this.prevFogColor + (this.fogColor - this.prevFogColor) * f1;
		this.fogColorRed *= f11;
		this.fogColorGreen *= f11;
		this.fogColorBlue *= f11;
		if(this.mc.options.anaglyph) {
			f1 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			f11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f12 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f1;
			this.fogColorGreen = f11;
			this.fogColorBlue = f12;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog() {
		World world1 = this.mc.theWorld;
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		float f3 = 1.0F;
		float f6 = this.fogColorBlue;
		float f5 = this.fogColorGreen;
		float f4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(f4).put(f5).put(f6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(GL11.GL_FOG_COLOR, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block block7;
		if((block7 = Block.blocksList[world1.getBlockId((int)entityPlayerSP2.posX, (int)(entityPlayerSP2.posY + 0.12F), (int)entityPlayerSP2.posZ)]) != null && block7.material.getIsLiquid()) {
			Material material8 = block7.material;
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(material8 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if(material8 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance / 4.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}