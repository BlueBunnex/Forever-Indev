package net.minecraft.game.item.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.block.Block;

public final class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private List recipes = new ArrayList();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		new RecipesFood();
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", 'X', Block.mushroomBrown, 'Y', Block.mushroomRed, '#', Item.bowlEmpty});
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", 'X', Block.mushroomRed, 'Y', Block.mushroomBrown, '#', Item.bowlEmpty});
		new RecipesCrafting();
		this.addRecipe(new ItemStack(Block.crate), new Object[]{"###", "# #", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", '#', Block.planks});
		(new RecipesArmor()).addRecipes(this);
		this.addRecipe(new ItemStack(Block.clothGray, 1), new Object[]{"###", "###", "###", '#', Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 3), new Object[]{"###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", '#', Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.torch, 4), new Object[]{"X", "#", 'X', Item.coal, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.striker, 1), new Object[]{"A ", " B", 'A', Item.ingotIron, 'B', Item.flint});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", '#', Item.wheat});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", '#', Block.planks, 'X', Block.clothGray});
		Collections.sort(this.recipes, new RecipeSorter(this));
		System.out.println(this.recipes.size() + " recipes");
	}

	final void addRecipe(ItemStack itemStack, Object... object2) {
		String string3 = "";
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;
		if(object2[0] instanceof String[]) {
			++i4;
			String[] string11 = (String[])object2[0];

			for(int i8 = 0; i8 < string11.length; ++i8) {
				String string9 = string11[i8];
				++i6;
				i5 = string9.length();
				string3 = string3 + string9;
			}
		} else {
			while(object2[i4] instanceof String) {
				String string7 = (String)object2[i4++];
				++i6;
				i5 = string7.length();
				string3 = string3 + string7;
			}
		}

		HashMap hashMap12;
		int i15;
		for(hashMap12 = new HashMap(); i4 < object2.length; i4 += 2) {
			Character character13 = (Character)object2[i4];
			i15 = 0;
			if(object2[i4 + 1] instanceof Item) {
				i15 = ((Item)object2[i4 + 1]).shiftedIndex;
			} else if(object2[i4 + 1] instanceof Block) {
				i15 = ((Block)object2[i4 + 1]).blockID;
			}

			hashMap12.put(character13, i15);
		}

		int[] i14 = new int[i5 * i6];

		for(i15 = 0; i15 < i5 * i6; ++i15) {
			char c10 = string3.charAt(i15);
			if(hashMap12.containsKey(c10)) {
				i14[i15] = ((Integer)hashMap12.get(c10)).intValue();
			} else {
				i14[i15] = -1;
			}
		}

		this.recipes.add(new CraftingRecipe(i5, i6, i14, itemStack));
	}

	public final ItemStack findMatchingRecipe(int[] i1) {
		for(int i2 = 0; i2 < this.recipes.size(); ++i2) {
			CraftingRecipe craftingRecipe3;
			if((craftingRecipe3 = (CraftingRecipe)this.recipes.get(i2)).matchRecipe(i1)) {
				return craftingRecipe3.createResult();
			}
		}

		return null;
	}
}