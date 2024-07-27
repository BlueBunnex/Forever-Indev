package net.minecraft.client.render.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep;
import net.minecraft.client.model.ModelSheepFur;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiantZombie;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.level.World;

import org.lwjgl.opengl.GL11;

public final class RenderManager {
	private Map entityRenderMap = new HashMap();
	public static RenderManager instance = new RenderManager();
	public RenderEngine renderEngine;
	public World worldObj;
	public float playerViewY;
	private float viewerPosX;
	private float viewerPosY;
	private float viewerPosZ;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderLiving(new ModelPig(), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep(), new ModelSheepFur(), 0.7F));
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderLiving(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombie.class, new RenderLiving(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		Iterator iterator1 = this.entityRenderMap.values().iterator();

		while(iterator1.hasNext()) {
			((Render)iterator1.next()).setRenderManager(this);
		}

	}

	public final Render getEntityRenderObject(Entity entity) {
		Class class2 = entity.getClass();
		Render render3;
		if((render3 = (Render)this.entityRenderMap.get(class2)) == null && class2 != Entity.class) {
			render3 = (Render)this.entityRenderMap.get(class2.getSuperclass());
			this.entityRenderMap.put(class2, render3);
		}

		return render3;
	}

	public final void cacheActiveRenderInfo(World world, RenderEngine renderEngine, EntityPlayer entityPlayer, float f4) {
		this.worldObj = world;
		this.renderEngine = renderEngine;
		this.playerViewY = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * f4;
		this.viewerPosX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * f4;
		this.viewerPosY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * f4;
		this.viewerPosZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * f4;
	}

	public final void renderEntity(Entity entity, float f2) {
		float f3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * f2;
		float f4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * f2;
		float f5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * f2;
		float f6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f2;
		float f7;
		GL11.glColor3f(f7 = this.worldObj.getLightBrightness((int)f3, (int)(f4 + entity.getShadowSize()), (int)f5), f7, f7);
		this.renderEntityWithPosYaw(entity, f3, f4, f5, f6, f2);
	}

	public final void renderEntityWithPosYaw(Entity entity, float f2, float f3, float f4, float f5, float f6) {
		Render render7;
		if((render7 = this.getEntityRenderObject(entity)) != null) {
			render7.doRender(entity, f2, f3, f4, f5, f6);
			render7.renderShadow(entity, f2, f3, f4, f6);
		}

	}

	public final void set(World world) {
		this.worldObj = world;
	}

	public final float getDistanceToCamera(float f1, float f2, float f3) {
		f1 -= this.viewerPosX;
		f2 -= this.viewerPosY;
		f3 -= this.viewerPosZ;
		return f1 * f1 + f2 * f2 + f3 * f3;
	}
}