package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public class PlayerController {
	
	protected final Minecraft mc;

	public PlayerController(Minecraft mc) {
		this.mc = mc;
	}

	public void onWorldChange(World world) {}

	public void clickBlock(int x, int y, int z) {
		this.sendBlockRemoved(x, y, z);
	}

	public boolean sendBlockRemoved(int x, int y, int z) {
		
		this.mc.effectRenderer.addBlockDestroyEffects(x, y, z);
		
		World world = this.mc.theWorld;
		Block removedBlock = Block.blocksList[world.getBlockId(x, y, z)];
		byte var6 = world.getBlockMetadata(x, y, z);
		boolean didRemove = world.setBlockWithNotify(x, y, z, 0);
		
		if (removedBlock != null && didRemove) {
			SoundManager var10000 = this.mc.sndManager;
			String var10001 = removedBlock.stepSound.stepSoundDir();
			float var10002 = (float) x + 0.5F;
			float var10003 = (float) y + 0.5F;
			float var10004 = (float) z + 0.5F;
			StepSound var8 = removedBlock.stepSound;
			float var10005 = (var8.soundVolume + 1.0F) / 2.0F;
			var8 = removedBlock.stepSound;
			var10000.playSound(var10001, var10002, var10003, var10004, var10005, var8.soundPitch * 0.8F);
			removedBlock.onBlockDestroyedByPlayer(world, x, y, z, var6);
		}

		return didRemove;
	}

	public void sendBlockRemoving(int x, int y, int z, int side) {}

	public void resetBlockRemoving() {}

	public void setPartialTime(float var1) {}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void onUpdate() {}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer player) {}
	
}