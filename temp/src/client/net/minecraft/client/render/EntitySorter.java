package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.game.entity.player.EntityPlayer;

public final class EntitySorter implements Comparator {
	private EntityPlayer player;

	public EntitySorter(EntityPlayer entityPlayer) {
		this.player = entityPlayer;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer3 = (WorldRenderer)object2;
		WorldRenderer worldRenderer4 = worldRenderer10001;
		return worldRenderer4.distanceToEntitySquared(this.player) < worldRenderer3.distanceToEntitySquared(this.player) ? -1 : 1;
	}
}