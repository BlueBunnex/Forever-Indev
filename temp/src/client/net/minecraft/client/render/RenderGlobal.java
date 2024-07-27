package net.minecraft.client.render;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EntityBubbleFX;
import net.minecraft.client.effect.EntityExplodeFX;
import net.minecraft.client.effect.EntityFlameFX;
import net.minecraft.client.effect.EntityLavaFX;
import net.minecraft.client.effect.EntitySmokeFX;
import net.minecraft.client.effect.EntitySplashFX;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.EntityMap;
import net.minecraft.game.level.IWorldAccess;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import util.MathHelper;
import org.lwjgl.opengl.GL15;

public final class RenderGlobal implements IWorldAccess {
	private World worldObj;
	private RenderEngine renderEngine;
	private int glGenList;
	private IntBuffer renderIntBuffer = BufferUtils.createIntBuffer(65536);
	private List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	private Minecraft mc;
	private RenderBlocks globalRenderBlocks;
	private IntBuffer glOcclusionQueryBase;
	private boolean occlusionEnabled = false;
	private int cloudOffsetX = 0;
	private int glSkyList;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	private int[] dummyBuf50k = new int[50000];
	private IntBuffer occlusionResult = BufferUtils.createIntBuffer(64);
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private float prevSortX = -9999.0F;
	private float prevSortY = -9999.0F;
	private float prevSortZ = -9999.0F;
	public float damagePartialTime;

	public RenderGlobal(Minecraft minecraft, RenderEngine renderEngine) {
		this.mc = minecraft;
		this.renderEngine = renderEngine;
		this.glGenList = GL11.glGenLists(2);
		this.glRenderListBase = GL11.glGenLists(786432);
		this.occlusionEnabled = GLContext.getCapabilities().GL_ARB_occlusion_query;
		if(this.occlusionEnabled) {
			this.occlusionResult.clear();
			GL11.glGetInteger(GL15.GL_QUERY_COUNTER_BITS, this.occlusionResult);
			if(this.occlusionResult.get(0) == 0) {
				this.occlusionEnabled = false;
			} else {
				this.glOcclusionQueryBase = BufferUtils.createIntBuffer(262144);
				this.glOcclusionQueryBase.clear();
				this.glOcclusionQueryBase.position(0);
				this.glOcclusionQueryBase.limit(262144);
				ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
			}
		}

		this.glSkyList = GL11.glGenLists(1);
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		Random random5 = new Random(10842L);

		for(int i6 = 0; i6 < 500; ++i6) {
			GL11.glRotatef(random5.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(random5.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(random5.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			Tessellator tessellator3 = Tessellator.instance;
			float f4 = 0.25F + random5.nextFloat() * 0.25F;
			tessellator3.startDrawingQuads();
			tessellator3.addVertexWithUV(-f4, -100.0F, f4, 1.0F, 1.0F);
			tessellator3.addVertexWithUV(f4, -100.0F, f4, 0.0F, 1.0F);
			tessellator3.addVertexWithUV(f4, -100.0F, -f4, 0.0F, 0.0F);
			tessellator3.addVertexWithUV(-f4, -100.0F, -f4, 1.0F, 0.0F);
			tessellator3.draw();
		}

		GL11.glEndList();
	}

	public final void changeWorld(World world) {
		if(this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0F;
		this.prevSortY = -9999.0F;
		this.prevSortZ = -9999.0F;
		RenderManager.instance.set(world);
		this.worldObj = world;
		this.globalRenderBlocks = new RenderBlocks(world);
		if(world != null) {
			world.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public final void loadRenderers() {
		int i1;
		if(this.worldRenderers != null) {
			for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
				this.worldRenderers[i1].stopRendering();
			}
		}

		this.renderChunksWide = this.worldObj.width / 16;
		this.renderChunksTall = this.worldObj.height / 16;
		this.renderChunksDeep = this.worldObj.length / 16;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		i1 = 0;
		int i2 = 0;

		int i3;
		for(i3 = 0; i3 < this.renderChunksWide; ++i3) {
			for(int i4 = 0; i4 < this.renderChunksTall; ++i4) {
				for(int i5 = 0; i5 < this.renderChunksDeep; ++i5) {
					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = new WorldRenderer(this.worldObj, i3 << 4, i4 << 4, i5 << 4, 16, this.glRenderListBase + i1);
					if(this.occlusionEnabled) {
						this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].glOcclusionQuery = this.glOcclusionQueryBase.get(i2);
					}

					++i2;
					this.sortedWorldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3];
					i1 += 3;
				}
			}
		}

		for(i3 = 0; i3 < this.worldRenderersToUpdate.size(); ++i3) {
			((WorldRenderer)this.worldRenderersToUpdate.get(i3)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		this.oobGroundRenderHeight();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		this.oobWaterRenderHeight();
		GL11.glEndList();
		this.markBlocksForUpdate(0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
	}

	public final void renderEntities(Vec3D vec3D, ICamera iCamera, float f3) {
		EntityMap entityMap4 = this.worldObj.entityMap;
		RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.thePlayer, f3);
		this.countEntitiesTotal = 0;
		this.countEntitiesRendered = 0;
		this.countEntitiesHidden = 0;

		for(int i5 = 0; i5 < entityMap4.width; ++i5) {
			for(int i6 = 0; i6 < entityMap4.depth; ++i6) {
				for(int i7 = 0; i7 < entityMap4.height; ++i7) {
					List list8;
					if((list8 = entityMap4.entityGrid[(i7 * entityMap4.depth + i6) * entityMap4.width + i5]).size() != 0) {
						int i9 = (i5 << 3) + 4;
						int i10 = (i6 << 3) + 4;
						int i11 = (i7 << 3) + 4;
						this.countEntitiesTotal += list8.size();
						float f10001 = (float)i9;
						float f10002 = (float)i10;
						float f14 = (float)i11;
						float f13 = f10002;
						float f12 = f10001;
						boolean z10000;
						if(f12 >= 0.0F && f13 >= 0.0F && f14 >= 0.0F && f12 < (float)this.worldObj.width && f13 < (float)this.worldObj.height && f14 < (float)this.worldObj.length) {
							int i18 = (int)(f12 / 16.0F);
							int i20 = (int)(f13 / 16.0F);
							int i21 = (int)(f14 / 16.0F);
							z10000 = this.worldRenderers[(i21 * this.renderChunksTall + i20) * this.renderChunksWide + i18].isInFrustrum && this.worldRenderers[(i21 * this.renderChunksTall + i20) * this.renderChunksWide + i18].isVisible;
						} else {
							z10000 = true;
						}

						if(!z10000) {
							this.countEntitiesHidden += list8.size();
						} else {
							for(i9 = 0; i9 < list8.size(); ++i9) {
								Entity entity15;
								Entity entity16;
								f13 = (entity16 = entity15 = (Entity)list8.get(i9)).posX - vec3D.xCoord;
								f14 = entity16.posY - vec3D.yCoord;
								f12 = entity16.posZ - vec3D.zCoord;
								f13 = f13 * f13 + f14 * f14 + f12 * f12;
								float f17 = f13;
								AxisAlignedBB axisAlignedBB19;
								f13 = (axisAlignedBB19 = entity16.boundingBox).maxX - axisAlignedBB19.minX;
								f14 = axisAlignedBB19.maxY - axisAlignedBB19.minY;
								f12 = axisAlignedBB19.maxZ - axisAlignedBB19.minZ;
								f12 = (f13 + f14 + f12) / 3.0F * 64.0F;
								if(f17 < f12 * f12 && iCamera.isBoundingBoxInFrustrum(entity15.boundingBox) && (entity15 != this.worldObj.playerEntity || this.mc.options.thirdPersonView)) {
									++this.countEntitiesRendered;
									RenderManager.instance.renderEntity(entity15, f3);
								}
							}
						}
					}
				}
			}
		}

	}

	public final String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded;
	}

	public final String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	public final int sortAndRender(EntityPlayer entityPlayer, int i2) {
		if(i2 == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
		}

		float f3 = entityPlayer.posX - this.prevSortX;
		float f4 = entityPlayer.posY - this.prevSortY;
		float f5 = entityPlayer.posZ - this.prevSortZ;
		if(f3 * f3 + f4 * f4 + f5 * f5 > 16.0F) {
			this.prevSortX = entityPlayer.posX;
			this.prevSortY = entityPlayer.posY;
			this.prevSortZ = entityPlayer.posZ;
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayer));
		}

		int i8;
		if(this.occlusionEnabled && i2 == 0) {
			int i10 = 8;
			this.checkOcclusionQueryResult(0, 8);

			int i6;
			for(i6 = 0; i6 < 8; ++i6) {
				this.sortedWorldRenderers[i6].isVisible = true;
			}

			i8 = 0 + this.renderSortedRenderers(0, 8, i2);

			do {
				int i9 = i10;
				if((i10 <<= 1) > this.sortedWorldRenderers.length) {
					i10 = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.checkOcclusionQueryResult(i9, i10);

				for(i6 = i9; i6 < i10; ++i6) {
					if(!this.sortedWorldRenderers[i6].isInFrustrum) {
						this.sortedWorldRenderers[i6].isVisible = true;
					}

					if(this.sortedWorldRenderers[i6].isInFrustrum && !this.sortedWorldRenderers[i6].isWaitingOnOcclusionQuery) {
						float f7 = MathHelper.sqrt_float(this.sortedWorldRenderers[i6].distanceToEntitySquared(entityPlayer));
						int i11 = (int)(1.0F + f7 / 64.0F);
						if(this.cloudOffsetX % i11 == i6 % i11) {
							ARBOcclusionQuery.glBeginQueryARB(GL15.GL_SAMPLES_PASSED, this.sortedWorldRenderers[i6].glOcclusionQuery);
							this.sortedWorldRenderers[i6].callOcclusionQueryList();
							ARBOcclusionQuery.glEndQueryARB(GL15.GL_SAMPLES_PASSED);
							this.sortedWorldRenderers[i6].isWaitingOnOcclusionQuery = true;
						}
					}
				}

				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				i8 += this.renderSortedRenderers(i9, i10, i2);
			} while(i10 < this.sortedWorldRenderers.length);
		} else {
			i8 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i2);
		}

		return i8;
	}

	private void checkOcclusionQueryResult(int i1, int i2) {
		for(i1 = i1; i1 < i2; ++i1) {
			if(this.sortedWorldRenderers[i1].isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i1].glOcclusionQuery, GL15.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
				if(this.occlusionResult.get(0) != 0) {
					this.sortedWorldRenderers[i1].isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i1].glOcclusionQuery, GL15.GL_QUERY_RESULT, this.occlusionResult);
					this.sortedWorldRenderers[i1].isVisible = this.occlusionResult.get(0) != 0;
				}
			}
		}

	}

	private int renderSortedRenderers(int i1, int i2, int i3) {
		int i4 = 0;

		for(i1 = i1; i1 < i2; ++i1) {
			if(i3 == 0) {
				++this.renderersLoaded;
				if(!this.sortedWorldRenderers[i1].isInFrustrum) {
					++this.renderersBeingClipped;
				}

				if(this.sortedWorldRenderers[i1].isInFrustrum && !this.sortedWorldRenderers[i1].isVisible) {
					++this.renderersBeingOccluded;
				}

				if(this.sortedWorldRenderers[i1].isInFrustrum && this.sortedWorldRenderers[i1].isVisible) {
					++this.renderersBeingRendered;
				}
			}

			if(this.sortedWorldRenderers[i1].isInFrustrum && this.sortedWorldRenderers[i1].isVisible) {
				i4 = this.sortedWorldRenderers[i1].getGLCallListForPass(this.dummyBuf50k, i4, i3);
			}
		}

		this.renderIntBuffer.clear();
		this.renderIntBuffer.put(this.dummyBuf50k, 0, i4);
		this.renderIntBuffer.flip();
		if(this.renderIntBuffer.remaining() > 0) {
			GL11.glCallLists(this.renderIntBuffer);
		}

		return this.renderIntBuffer.remaining();
	}

	public final void renderAllRenderLists() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		GL11.glCallLists(this.renderIntBuffer);
	}

	public final void updateClouds() {
		++this.cloudOffsetX;
	}

	public final void renderSky(float f1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3D vec3D2;
		float f3 = (vec3D2 = this.worldObj.getSkyColor(f1)).xCoord;
		float f4 = vec3D2.yCoord;
		float f9 = vec3D2.zCoord;
		if(this.mc.options.anaglyph) {
			float f5 = (f3 * 30.0F + f4 * 59.0F + f9 * 11.0F) / 100.0F;
			f4 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
			f9 = (f3 * 30.0F + f9 * 70.0F) / 100.0F;
			f3 = f5;
			f4 = f4;
			f9 = f9;
		}

		GL11.glDepthMask(false);
		Tessellator tessellator12 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator12.setColorOpaque_F(f3, f4, f9);
		f9 = (float)(this.worldObj.height + 10);

		int i10;
		for(i10 = -2048; i10 < this.worldObj.width + 2048; i10 += 512) {
			for(int i13 = -2048; i13 < this.worldObj.length + 2048; i13 += 512) {
				tessellator12.addVertex((float)i10, f9, (float)i13);
				tessellator12.addVertex((float)(i10 + 512), f9, (float)i13);
				tessellator12.addVertex((float)(i10 + 512), f9, (float)(i13 + 512));
				tessellator12.addVertex((float)i10, f9, (float)(i13 + 512));
			}
		}

		tessellator12.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glPushMatrix();
		f3 = this.worldObj.playerEntity.lastTickPosX + (this.worldObj.playerEntity.posX - this.worldObj.playerEntity.lastTickPosX) * f1;
		f4 = this.worldObj.playerEntity.lastTickPosY + (this.worldObj.playerEntity.posY - this.worldObj.playerEntity.lastTickPosY) * f1;
		float f6 = this.worldObj.playerEntity.lastTickPosZ + (this.worldObj.playerEntity.posZ - this.worldObj.playerEntity.lastTickPosZ) * f1;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(f3, f4, f6);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.worldObj.getCelestialAngle(f1) * 360.0F, 1.0F, 0.0F, 0.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
		tessellator12.startDrawingQuads();
		tessellator12.addVertexWithUV(-30.0F, 100.0F, -30.0F, 0.0F, 0.0F);
		tessellator12.addVertexWithUV(30.0F, 100.0F, -30.0F, 1.0F, 0.0F);
		tessellator12.addVertexWithUV(30.0F, 100.0F, 30.0F, 1.0F, 1.0F);
		tessellator12.addVertexWithUV(-30.0F, 100.0F, 30.0F, 0.0F, 1.0F);
		tessellator12.draw();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
		tessellator12.startDrawingQuads();
		tessellator12.addVertexWithUV(-20.0F, -100.0F, 20.0F, 1.0F, 1.0F);
		tessellator12.addVertexWithUV(20.0F, -100.0F, 20.0F, 0.0F, 1.0F);
		tessellator12.addVertexWithUV(20.0F, -100.0F, -20.0F, 0.0F, 0.0F);
		tessellator12.addVertexWithUV(-20.0F, -100.0F, -20.0F, 1.0F, 0.0F);
		tessellator12.draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f9 = this.worldObj.getStarBrightness(f1), f9, f9, f9);
		GL11.glCallList(this.glSkyList);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Vec3D vec3D11;
		f4 = (vec3D11 = this.worldObj.getCloudColor(f1)).xCoord;
		f6 = vec3D11.yCoord;
		f3 = vec3D11.zCoord;
		float f7;
		if(this.mc.options.anaglyph) {
			f9 = (f4 * 30.0F + f6 * 59.0F + f3 * 11.0F) / 100.0F;
			f7 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
			f3 = (f4 * 30.0F + f3 * 70.0F) / 100.0F;
			f4 = f9;
			f6 = f7;
			f3 = f3;
		}

		f9 = (float)this.worldObj.cloudHeight;
		f7 = ((float)this.cloudOffsetX + f1) * 4.8828125E-4F * 0.03F;
		tessellator12.startDrawingQuads();
		tessellator12.setColorOpaque_F(f4, f6, f3);

		for(int i8 = -2048; i8 < this.worldObj.width + 2048; i8 += 512) {
			for(i10 = -2048; i10 < this.worldObj.length + 2048; i10 += 512) {
				tessellator12.addVertexWithUV((float)i8, f9, (float)(i10 + 512), (float)i8 * 4.8828125E-4F + f7, (float)(i10 + 512) * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)(i8 + 512), f9, (float)(i10 + 512), (float)(i8 + 512) * 4.8828125E-4F + f7, (float)(i10 + 512) * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)(i8 + 512), f9, (float)i10, (float)(i8 + 512) * 4.8828125E-4F + f7, (float)i10 * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)i8, f9, (float)i10, (float)i8 * 4.8828125E-4F + f7, (float)i10 * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)i8, f9, (float)i10, (float)i8 * 4.8828125E-4F + f7, (float)i10 * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)(i8 + 512), f9, (float)i10, (float)(i8 + 512) * 4.8828125E-4F + f7, (float)i10 * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)(i8 + 512), f9, (float)(i10 + 512), (float)(i8 + 512) * 4.8828125E-4F + f7, (float)(i10 + 512) * 4.8828125E-4F);
				tessellator12.addVertexWithUV((float)i8, f9, (float)(i10 + 512), (float)i8 * 4.8828125E-4F + f7, (float)(i10 + 512) * 4.8828125E-4F);
			}
		}

		tessellator12.draw();
	}

	public final void oobGroundRenderer() {
		float f1 = this.worldObj.getLightBrightness(0, this.worldObj.getGroundLevel(), 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/dirt.png"));
		if(this.worldObj.getGroundLevel() > this.worldObj.getWaterLevel() && this.worldObj.defaultFluid == Block.waterMoving.blockID) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/grass.png"));
		}

		GL11.glColor4f(f1, f1, f1, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glCallList(this.glGenList);
	}

	private void oobGroundRenderHeight() {
		Tessellator tessellator1 = Tessellator.instance;
		float f2 = (float)this.worldObj.getGroundLevel();
		int i3 = 128;
		if(128 > this.worldObj.width) {
			i3 = this.worldObj.width;
		}

		if(i3 > this.worldObj.length) {
			i3 = this.worldObj.length;
		}

		int i4 = 2048 / i3;
		tessellator1.startDrawingQuads();

		for(int i5 = -i3 * i4; i5 < this.worldObj.width + i3 * i4; i5 += i3) {
			for(int i6 = -i3 * i4; i6 < this.worldObj.length + i3 * i4; i6 += i3) {
				if(f2 < 0.0F || i5 < 0 || i6 < 0 || i5 >= this.worldObj.width || i6 >= this.worldObj.length) {
					tessellator1.addVertexWithUV((float)i5, f2, (float)(i6 + i3), 0.0F, (float)i3);
					tessellator1.addVertexWithUV((float)(i5 + i3), f2, (float)(i6 + i3), (float)i3, (float)i3);
					tessellator1.addVertexWithUV((float)(i5 + i3), f2, (float)i6, (float)i3, 0.0F);
					tessellator1.addVertexWithUV((float)i5, f2, (float)i6, 0.0F, 0.0F);
				}
			}
		}

		tessellator1.draw();
	}

	public final void oobWaterRenderer() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/water.png"));
		float f1;
		GL11.glColor4f(f1 = this.worldObj.getLightBrightness(0, this.worldObj.getWaterLevel(), 0), f1, f1, 1.0F);
		GL11.glCallList(this.glGenList + 1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void oobWaterRenderHeight() {
		float f1 = (float)this.worldObj.getWaterLevel();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator2 = Tessellator.instance;
		int i3 = 128;
		if(128 > this.worldObj.width) {
			i3 = this.worldObj.width;
		}

		if(i3 > this.worldObj.length) {
			i3 = this.worldObj.length;
		}

		int i4 = 2048 / i3;
		tessellator2.startDrawingQuads();
		float f5 = Block.waterMoving.minX;
		float f6 = Block.waterMoving.minZ;

		for(int i7 = -i3 * i4; i7 < this.worldObj.width + i3 * i4; i7 += i3) {
			for(int i8 = -i3 * i4; i8 < this.worldObj.length + i3 * i4; i8 += i3) {
				float f9 = f1 + Block.waterMoving.minY;
				if(f1 < 0.0F || i7 < 0 || i8 < 0 || i7 >= this.worldObj.width || i8 >= this.worldObj.length) {
					tessellator2.addVertexWithUV((float)i7 + f5, f9, (float)(i8 + i3) + f6, 0.0F, (float)i3);
					tessellator2.addVertexWithUV((float)(i7 + i3) + f5, f9, (float)(i8 + i3) + f6, (float)i3, (float)i3);
					tessellator2.addVertexWithUV((float)(i7 + i3) + f5, f9, (float)i8 + f6, (float)i3, 0.0F);
					tessellator2.addVertexWithUV((float)i7 + f5, f9, (float)i8 + f6, 0.0F, 0.0F);
					tessellator2.addVertexWithUV((float)i7 + f5, f9, (float)i8 + f6, 0.0F, 0.0F);
					tessellator2.addVertexWithUV((float)(i7 + i3) + f5, f9, (float)i8 + f6, (float)i3, 0.0F);
					tessellator2.addVertexWithUV((float)(i7 + i3) + f5, f9, (float)(i8 + i3) + f6, (float)i3, (float)i3);
					tessellator2.addVertexWithUV((float)i7 + f5, f9, (float)(i8 + i3) + f6, 0.0F, (float)i3);
				}
			}
		}

		tessellator2.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public final void updateRenderers(EntityPlayer entityPlayer) {
		Collections.sort(this.worldRenderersToUpdate, new RenderSorter(entityPlayer));
		int i2 = this.worldRenderersToUpdate.size() - 1;
		int i3 = this.worldRenderersToUpdate.size();

		for(int i4 = 0; i4 < i3; ++i4) {
			WorldRenderer worldRenderer5;
			if((worldRenderer5 = (WorldRenderer)this.worldRenderersToUpdate.get(i2 - i4)).distanceToEntitySquared(entityPlayer) > 2500.0F && i4 > 4) {
				return;
			}

			this.worldRenderersToUpdate.remove(worldRenderer5);
			worldRenderer5.updateRenderer();
			worldRenderer5.needsUpdate = false;
		}

	}

	public final void drawBlockBreaking(MovingObjectPosition movingObjectPosition, int i2, ItemStack itemStack) {
		Tessellator tessellator4 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		if(this.damagePartialTime > 0.0F) {
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
			int itemStack1 = this.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, itemStack1);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			Block itemStack2 = (itemStack1 = this.worldObj.getBlockId(movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ)) > 0 ? Block.blocksList[itemStack1] : null;
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			tessellator4.startDrawingQuads();
			tessellator4.disableColor();
			if(itemStack2 == null) {
				itemStack2 = Block.stone;
			}

			this.globalRenderBlocks.renderBlockUsingTexture(itemStack2, movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
			tessellator4.draw();
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public final void drawSelectionBox(MovingObjectPosition movingObjectPosition, int i2) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		if((i2 = this.worldObj.getBlockId(movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ)) > 0) {
			AxisAlignedBB movingObjectPosition1 = Block.blocksList[i2].getSelectedBoundingBoxFromPool(movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ).expand(0.002F, 0.002F, 0.002F);
			Tessellator tessellator4 = Tessellator.instance;
			Tessellator.instance.startDrawing(3);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.minY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.minY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.minY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.minY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.minY, movingObjectPosition1.minZ);
			tessellator4.draw();
			tessellator4.startDrawing(3);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.maxY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.maxY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.maxY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.maxY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.maxY, movingObjectPosition1.minZ);
			tessellator4.draw();
			tessellator4.startDrawing(1);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.minY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.maxY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.minY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.maxY, movingObjectPosition1.minZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.minY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.maxX, movingObjectPosition1.maxY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.minY, movingObjectPosition1.maxZ);
			tessellator4.addVertex(movingObjectPosition1.minX, movingObjectPosition1.maxY, movingObjectPosition1.maxZ);
			tessellator4.draw();
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void markBlocksForUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		i1 /= 16;
		i2 /= 16;
		i3 /= 16;
		i4 /= 16;
		i5 /= 16;
		i6 /= 16;
		if(i1 < 0) {
			i1 = 0;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i4 > this.renderChunksWide - 1) {
			i4 = this.renderChunksWide - 1;
		}

		if(i5 > this.renderChunksTall - 1) {
			i5 = this.renderChunksTall - 1;
		}

		if(i6 > this.renderChunksDeep - 1) {
			i6 = this.renderChunksDeep - 1;
		}

		for(i1 = i1; i1 <= i4; ++i1) {
			for(int i7 = i2; i7 <= i5; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					WorldRenderer worldRenderer9;
					if(!(worldRenderer9 = this.worldRenderers[(i8 * this.renderChunksTall + i7) * this.renderChunksWide + i1]).needsUpdate) {
						worldRenderer9.needsUpdate = true;
						this.worldRenderersToUpdate.add(this.worldRenderers[(i8 * this.renderChunksTall + i7) * this.renderChunksWide + i1]);
					}
				}
			}
		}

	}

	public final void markBlockAndNeighborsNeedsUpdate(int i1, int i2, int i3) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
	}

	public final void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i4 + 1, i5 + 1, i6 + 1);
	}

	public final void clipRenderersByFrustrum(ICamera iCamera) {
		for(int i2 = 0; i2 < this.worldRenderers.length; ++i2) {
			this.worldRenderers[i2].updateInFrustrum(iCamera);
		}

	}

	public final void playSound(String string1, float f2, float f3, float f4, float f5, float f6) {
		this.mc.sndManager.playSound(string1, f2, f3, f4, f5, f6);
	}

	public final void spawnParticle(String string1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = this.worldObj.playerEntity.posX - f2;
		float f9 = this.worldObj.playerEntity.posY - f3;
		float f10 = this.worldObj.playerEntity.posZ - f4;
		if(f8 * f8 + f9 * f9 + f10 * f10 <= 256.0F) {
			if(string1 == "bubble") {
				this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, f2, f3, f4, f5, f6, f7));
			} else if(string1 == "smoke") {
				this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, f2, f3, f4));
			} else if(string1 == "explode") {
				this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, f2, f3, f4, f5, f6, f7));
			} else if(string1 == "flame") {
				this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, f2, f3, f4));
			} else if(string1 == "lava") {
				this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, f2, f3, f4));
			} else if(string1 == "splash") {
				this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, f2, f3, f4));
			} else {
				if(string1 == "largesmoke") {
					this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, f2, f3, f4, 2.5F));
				}

			}
		}
	}

	public final void playMusic(String string1, float f2, float f3, float f4, float f5) {
		this.mc.sndManager.playRandomMusicIfReady(f2, f3, f4);
	}

	public final void obtainEntitySkin(Entity player) {
		if(player.skinUrl != null) {
			this.renderEngine.obtainImageData(player.skinUrl, new ImageBufferDownload());
		}

	}

	public final void releaseEntitySkin(Entity player) {
		if(player.skinUrl != null) {
			this.renderEngine.releaseImageData(player.skinUrl);
		}

	}

	public final void updateAllRenderers() {
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		this.oobGroundRenderHeight();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		this.oobWaterRenderHeight();
		GL11.glEndList();
	}
}