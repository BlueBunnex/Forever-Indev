package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft minecraft) {
		this.mc = minecraft;
	}

	public void onWorldChange(World world) {
	}

	public void clickBlock(int i1, int i2, int i3) {
		this.sendBlockRemoved(i1, i2, i3);
	}

	public boolean sendBlockRemoved(int i1, int i2, int i3) {
		this.mc.effectRenderer.addBlockDestroyEffects(i1, i2, i3);
		World world4 = this.mc.theWorld;
		Block block5 = Block.blocksList[world4.getBlockId(i1, i2, i3)];
		byte b6 = world4.getBlockMetadata(i1, i2, i3);
		boolean z7 = world4.setBlockWithNotify(i1, i2, i3, 0);
		if(block5 != null && z7) {
			SoundManager soundManager10000 = this.mc.sndManager;
			String string10001 = block5.stepSound.stepSoundDir();
			float f10002 = (float)i1 + 0.5F;
			float f10003 = (float)i2 + 0.5F;
			float f10004 = (float)i3 + 0.5F;
			StepSound stepSound8 = block5.stepSound;
			float f10005 = (block5.stepSound.soundVolume + 1.0F) / 2.0F;
			stepSound8 = block5.stepSound;
			soundManager10000.playSound(string10001, f10002, f10003, f10004, f10005, block5.stepSound.soundPitch * 0.8F);
			block5.onBlockDestroyedByPlayer(world4, i1, i2, i3, b6);
		}

		return z7;
	}

	public void sendBlockRemoving(int i1, int i2, int i3, int i4) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float f1) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void onRespawn(EntityPlayer entityPlayer) {
	}
}