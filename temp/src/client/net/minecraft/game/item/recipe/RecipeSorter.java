package net.minecraft.game.item.recipe;

import java.util.Comparator;

final class RecipeSorter implements Comparator {
	RecipeSorter(CraftingManager craftingManager) {
	}

	public final int compare(Object object1, Object object2) {
		CraftingRecipe craftingRecipe10000 = (CraftingRecipe)object1;
		CraftingRecipe craftingRecipe4 = (CraftingRecipe)object2;
		CraftingRecipe craftingRecipe3 = craftingRecipe10000;
		return craftingRecipe4.b() < craftingRecipe3.b() ? -1 : (craftingRecipe4.b() > craftingRecipe3.b() ? 1 : 0);
	}
}