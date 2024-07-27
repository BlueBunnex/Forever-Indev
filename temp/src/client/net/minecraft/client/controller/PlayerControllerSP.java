package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.block.StepSound;

public final class PlayerControllerSP extends PlayerController {
	private int curBlockX = -1;
	private int curBlockY = -1;
	private int curBlockZ = -1;
	private float curBlockDamage = 0.0F;
	private float prevBlockDamage = 0.0F;
	private float blockDestroySoundCounter = 0.0F;
	private int blockHitWait = 0;
	private MobSpawner mobSpawner;

	public PlayerControllerSP(Minecraft minecraft1) {
		super(minecraft1);
	}

	public final boolean sendBlockRemoved(int i1, int i2, int i3) {
		int i4 = this.mc.theWorld.getBlockId(i1, i2, i3);
		byte b5 = this.mc.theWorld.getBlockMetadata(i1, i2, i3);
		boolean z6 = super.sendBlockRemoved(i1, i2, i3);
		EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
		ItemStack itemStack9;
		if((itemStack9 = this.mc.thePlayer.inventory.getCurrentItem()) != null) {
			Item.itemsList[itemStack9.itemID].onBlockDestroyed(itemStack9);
			if(itemStack9.stackSize == 0) {
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(z6 && this.mc.thePlayer.canHarvestBlock(Block.blocksList[i4])) {
			Block.blocksList[i4].dropBlockAsItem(this.mc.theWorld, i1, i2, i3, b5);
		}

		return z6;
	}

	public final void clickBlock(int i1, int i2, int i3) {
		int i4;
		if((i4 = this.mc.theWorld.getBlockId(i1, i2, i3)) > 0 && Block.blocksList[i4].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(i1, i2, i3);
		}

	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0.0F;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int i1, int i2, int i3, int i4) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else {
			super.sendBlockRemoving(i1, i2, i3, i4);
			if(i1 == this.curBlockX && i2 == this.curBlockY && i3 == this.curBlockZ) {
				if((i4 = this.mc.theWorld.getBlockId(i1, i2, i3)) != 0) {
					Block block6 = Block.blocksList[i4];
					this.curBlockDamage += block6.blockStrength(this.mc.thePlayer);
					if(this.blockDestroySoundCounter % 4.0F == 0.0F && block6 != null) {
						SoundManager soundManager10000 = this.mc.sndManager;
						String string10001 = block6.stepSound.stepSoundDir2();
						float f10002 = (float)i1 + 0.5F;
						float f10003 = (float)i2 + 0.5F;
						float f10004 = (float)i3 + 0.5F;
						StepSound stepSound5 = block6.stepSound;
						float f10005 = (block6.stepSound.soundVolume + 1.0F) / 8.0F;
						stepSound5 = block6.stepSound;
						soundManager10000.playSound(string10001, f10002, f10003, f10004, f10005, block6.stepSound.soundPitch * 0.5F);
					}

					++this.blockDestroySoundCounter;
					if(this.curBlockDamage >= 1.0F) {
						this.sendBlockRemoved(i1, i2, i3);
						this.curBlockDamage = 0.0F;
						this.prevBlockDamage = 0.0F;
						this.blockDestroySoundCounter = 0.0F;
						this.blockHitWait = 5;
					}

				}
			} else {
				this.curBlockDamage = 0.0F;
				this.prevBlockDamage = 0.0F;
				this.blockDestroySoundCounter = 0.0F;
				this.curBlockX = i1;
				this.curBlockY = i2;
				this.curBlockZ = i3;
			}
		}
	}

	public final void setPartialTime(float f1) {
		if(this.curBlockDamage <= 0.0F) {
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			f1 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * f1;
			this.mc.renderGlobal.damagePartialTime = f1;
		}
	}

	public final float getBlockReachDistance() {
		return 4.0F;
	}

	public final void onWorldChange(World world) {
		super.onWorldChange(world);
		this.mobSpawner = new MobSpawner(world);
	}

	public final void onUpdate() {
		this.prevBlockDamage = this.curBlockDamage;
		this.mobSpawner.performSpawning();
	}
}