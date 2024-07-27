package net.minecraft.client.effect;

import net.minecraft.game.level.World;

public final class EntitySplashFX extends EntityRainFX {
	public EntitySplashFX(World world1, float f2, float f3, float f4) {
		super(world1, f2, f3, f4);
		this.particleGravity = 0.04F;
		++this.particleTextureIndex;
	}
}