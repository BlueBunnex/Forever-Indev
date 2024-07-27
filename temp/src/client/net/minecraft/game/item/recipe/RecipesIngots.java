package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class RecipesIngots {
	private Object[][] recipeItems = new Object[][]{{Block.blockGold, Item.ingotGold}, {Block.blockSteel, Item.ingotIron}, {Block.blockDiamond, Item.diamond}};

	public final void addRecipes(CraftingManager craftingManager) {
		for(int i2 = 0; i2 < this.recipeItems.length; ++i2) {
			Block block3 = (Block)this.recipeItems[i2][0];
			Item item4 = (Item)this.recipeItems[i2][1];
			craftingManager.addRecipe(new ItemStack(block3), new Object[]{"###", "###", "###", '#', item4});
			craftingManager.addRecipe(new ItemStack(item4, 9), new Object[]{"#", '#', block3});
		}

	}
}