package net.minecraft.client;

import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.LevelLoader;
import net.minecraft.game.level.World;

import util.IProgressUpdate;

public final class PlayerLoader extends LevelLoader {
	private Minecraft mc;

	public PlayerLoader(Minecraft minecraft, IProgressUpdate guiLoading) {
		super(guiLoading);
		this.mc = minecraft;
	}

	protected final Entity loadEntity(World world, String playerName) {
		return (Entity)(playerName.equals("LocalPlayer") ? new EntityPlayerSP(this.mc, world, this.mc.session) : super.loadEntity(world, playerName));
	}
}