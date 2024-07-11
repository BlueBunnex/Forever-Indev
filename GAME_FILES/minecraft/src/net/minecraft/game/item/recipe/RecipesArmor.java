package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class RecipesArmor {
	private String[][] recipePatterns = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
	
	private Object[][] recipeItems = new Object[][]{
		{Block.clothWhite, Block.fire, Item.ingotIron, Item.diamond, Item.ingotGold},
		{Item.helmetLeather, Item.helmetChain, Item.helmetIron, Item.helmetDiamond, Item.helmetGold},
		{Item.chestplateLeather, Item.chestplateChain, Item.chestplateIron, Item.chestplateDiamond, Item.chestplateGold},
		{Item.leggingsLeather, Item.leggingsChain, Item.leggingsIron, Item.leggingsDiamond, Item.leggingsGold},
		{Item.bootsLeather, Item.bootsChain, Item.bootsIron, Item.bootsDiamond, Item.bootsGold}
	};

	public final void addRecipes(CraftingManager var1) {
		
		for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
			Object var3 = this.recipeItems[0][var2];

			for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
				Item var5 = (Item) this.recipeItems[var4 + 1][var2];
				var1.addRecipe(new ItemStack(var5), new Object[]{this.recipePatterns[var4], Character.valueOf('X'), var3});
			}
		}
	}
}
