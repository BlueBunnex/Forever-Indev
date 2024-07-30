package net.minecraft.game.item.recipe;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public final class RecipesArmor {
	
	private String[][] recipePatterns = new String[][] {
		{"XXX", "X X"},
		{"X X", "XXX", "XXX"},
		{"XXX", "X X", "X X"},
		{"X X", "X X"}
	};
	
	private Object[][] recipeItems = new Object[][] {
		{ Item.ingotIron,      Item.ingotGold,      Item.diamond },
		{ Item.helmetIron,     Item.helmetGold,     Item.helmetDiamond },
		{ Item.chestplateIron, Item.chestplateGold, Item.chestplateDiamond },
		{ Item.leggingsIron,   Item.leggingsGold,   Item.leggingsDiamond },
		{ Item.bootsIron,      Item.bootsGold,      Item.bootsDiamond }
	};

	public final void addRecipes(CraftingManager crafting) {
		
		for (int resType = 0; resType < this.recipeItems[0].length; resType++) {
			
			Object resource = this.recipeItems[0][resType];

			for (int armorType = 0; armorType < this.recipePatterns.length; armorType++) {
				
				crafting.addRecipe(
						// result
						new ItemStack((Item) this.recipeItems[armorType + 1][resType]),
						// recipe descriptor
						new Object[] { this.recipePatterns[armorType], Character.valueOf('X'), resource }
				);
			}
		}
	}
}
