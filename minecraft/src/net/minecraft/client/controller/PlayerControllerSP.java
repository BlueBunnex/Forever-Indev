package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.player.EntityPlayerSP;
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

	public PlayerControllerSP(Minecraft mc) {
		super(mc);
	}

	public final boolean sendBlockRemoved(int x, int y, int z) {
		
		ItemStack heldItem = this.mc.thePlayer.inventory.getCurrentItem();
		
		if(heldItem != null) {
			
			Item.itemsList[heldItem.itemID].onBlockDestroyed(heldItem);
			
			if(heldItem.stackSize == 0)
				this.mc.thePlayer.destroyCurrentEquippedItem();
		}
		
		byte blockMeta = this.mc.theWorld.getBlockMetadata(x, y, z);
		int blockID = this.mc.theWorld.getBlockId(x, y, z);
		
		boolean didRemove = super.sendBlockRemoved(x, y, z);

		// drop block, unless can't harvest or in creative
		if(
				didRemove
				&& this.mc.thePlayer.canHarvestBlock(Block.blocksList[blockID])
				&& !this.mc.thePlayer.isCreativeMode
			) {
			Block.blocksList[blockID].dropBlockAsItem(this.mc.theWorld, x, y, z, blockMeta);
		}

		return didRemove;
	}

	public final void clickBlock(int x, int y, int z) {
		
		int blockID = this.mc.theWorld.getBlockId(x, y, z);
		
		if (blockID > 0 && Block.blocksList[blockID].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(x, y, z);
		}
	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0.0F;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int x, int y, int z, int side) {
		
		if(this.blockHitWait > 0) {
			
			--this.blockHitWait;
			
		} else if (this.mc.thePlayer.isCreativeMode) {
			
			this.sendBlockRemoved(x, y, z);
			this.blockHitWait = 5;
			
		} else {
			
			super.sendBlockRemoving(x, y, z, side);
			
			if (x == this.curBlockX && y == this.curBlockY && z == this.curBlockZ) {
				
				int blockID = this.mc.theWorld.getBlockId(x, y, z);
				
				if(blockID != 0) {
					
					Block var6 = Block.blocksList[blockID];
					this.curBlockDamage += var6.blockStrength(this.mc.thePlayer);
					if(this.blockDestroySoundCounter % 4.0F == 0.0F && var6 != null) {
						SoundManager var10000 = this.mc.sndManager;
						String var10001 = var6.stepSound.stepSoundDir2();
						float var10002 = (float) x + 0.5F;
						float var10003 = (float) y + 0.5F;
						float var10004 = (float) z + 0.5F;
						StepSound var5 = var6.stepSound;
						float var10005 = (var5.soundVolume + 1.0F) / 8.0F;
						var5 = var6.stepSound;
						var10000.playSound(var10001, var10002, var10003, var10004, var10005, var5.soundPitch * 0.5F);
					}

					++this.blockDestroySoundCounter;
					if(this.curBlockDamage >= 1.0F) {
						this.sendBlockRemoved(x, y, z);
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
				this.curBlockX = x;
				this.curBlockY = y;
				this.curBlockZ = z;
			}
		}
	}

	public final void setPartialTime(float var1) {
		if(this.curBlockDamage <= 0.0F) {
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			var1 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * var1;
			this.mc.renderGlobal.damagePartialTime = var1;
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
