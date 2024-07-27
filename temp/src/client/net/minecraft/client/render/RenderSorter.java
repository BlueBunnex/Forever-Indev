package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public final class RenderSorter implements Comparator {
	private EntityPlayer baseEntity;

	public RenderSorter(EntityPlayer entityPlayer) {
		this.baseEntity = entityPlayer;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer3 = (WorldRenderer)object2;
		WorldRenderer worldRenderer6 = worldRenderer10001;
		boolean z4 = worldRenderer6.isInFrustrum;
		boolean z5 = worldRenderer3.isInFrustrum;
		return z4 && !z5 ? 1 : ((!z5 || z4) && worldRenderer6.distanceToEntitySquared(this.baseEntity) < worldRenderer3.distanceToEntitySquared(this.baseEntity) ? 1 : -1);
	}
}