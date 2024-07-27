package net.minecraft.game.level.block;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockWorkbench extends Block {
	protected BlockWorkbench(int blockID) {
		super(58, Material.wood);
		this.blockIndexInTexture = 59;
	}

	public final int getBlockTextureFromSide(int blockSide) {
		return blockSide == 1 ? this.blockIndexInTexture - 16 : (blockSide == 0 ? Block.planks.getBlockTextureFromSide(0) : (blockSide != 2 && blockSide != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 1));
	}

	public final boolean blockActivated(World world, int xCoord, int yCoord, int zCoord, EntityPlayer player) {
		player.displayWorkbenchGUI();
		return true;
	}
}