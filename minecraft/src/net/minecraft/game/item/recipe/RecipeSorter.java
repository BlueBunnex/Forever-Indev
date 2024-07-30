package net.minecraft.game.item.recipe;

import java.util.Comparator;

final class RecipeSorter implements Comparator<CraftingRecipe> {

	public final int compare(CraftingRecipe recipe1, CraftingRecipe recipe2) {
		return recipe2.b() < recipe1.b() ? -1 : (recipe2.b() > recipe1.b() ? 1 : 0);
	}
}
