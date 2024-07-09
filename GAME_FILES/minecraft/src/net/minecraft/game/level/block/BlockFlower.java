package net.minecraft.game.level.block;

import java.util.Random;
import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFlower extends Block {
	protected BlockFlower(int var1, int var2) {
		super(var1, Material.plants);
		this.blockIndexInTexture = var2;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
	}

	public final boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return this.canThisPlantGrowOnThisBlockID(var1.getBlockId(var2, var3 - 1, var4));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int var1) {
		return var1 == Block.grass.blockID || var1 == Block.dirt.blockID || var1 == Block.tilledField.blockID;
	}

	public final void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		super.onNeighborBlockChange(var1, var2, var3, var4, var5);
		this.checkFlowerChange(var1, var2, var3, var4);
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		this.checkFlowerChange(var1, var2, var3, var4);
	}

	private void checkFlowerChange(World var1, int var2, int var3, int var4) {
		if(!this.canBlockStay(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4));
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public boolean canBlockStay(World var1, int var2, int var3, int var4) {
		return (var1.getBlockLightValue(var2, var3, var4) >= 8 || var1.getBlockLightValue(var2, var3, var4) >= 4 && var1.canBlockSeeTheSky(var2, var3, var4)) && this.canThisPlantGrowOnThisBlockID(var1.getBlockId(var2, var3 - 1, var4));
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int var1, int var2, int var3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
