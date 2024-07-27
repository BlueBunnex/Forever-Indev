package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerCreative extends PlayerController {
	public PlayerControllerCreative(Minecraft minecraft) {
		super(minecraft);
	}

	private MobSpawner mobSpawner;

	public final void onRespawn(EntityPlayer entityPlayer) {
		for(int i2 = 0; i2 < 9; ++i2) {
			if(entityPlayer.inventory.mainInventory[i2] == null) {
				this.mc.thePlayer.inventory.mainInventory[i2] = new ItemStack(((Block)Session.registeredBlocksList.get(i2)).blockID);
			} else {
				this.mc.thePlayer.inventory.mainInventory[i2].stackSize = 1;
			}
		}

	}

	public final boolean shouldDrawHUD() {
		return false;
	}

	public final void onWorldChange(World world) {
		super.onWorldChange(world);
		world.survivalWorld = false;
		this.mobSpawner = new MobSpawner(world);
	}

	public final void onUpdate() {
		this.mobSpawner.performSpawning();
	}
}
