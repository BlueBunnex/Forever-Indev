package net.minecraft.game.level;

import net.minecraft.game.entity.Entity;

public interface IWorldAccess {
	void markBlockAndNeighborsNeedsUpdate(int i1, int i2, int i3);

	void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6);

	void loadRenderers();

	void playSound(String string1, float f2, float f3, float f4, float f5, float f6);

	void spawnParticle(String string1, float f2, float f3, float f4, float f5, float f6, float f7);

	void playMusic(String string1, float f2, float f3, float f4, float f5);

	void obtainEntitySkin(Entity entity1);

	void releaseEntitySkin(Entity entity1);

	void updateAllRenderers();
}