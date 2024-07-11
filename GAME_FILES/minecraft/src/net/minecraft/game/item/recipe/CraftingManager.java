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
	
	private List<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		new RecipesTools().addRecipes(this);
		new RecipesWeapons().addRecipes(this);
		new RecipesIngots().addRecipes(this);
		new RecipesArmor().addRecipes(this);
		
		// both these are unused and may be removed
//		new RecipesFood();
//		new RecipesCrafting();
		
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", Character.valueOf('#'), Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.torch, 4), new Object[]{"X", "#", Character.valueOf('X'), Item.coal, Character.valueOf('#'), Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.crate), new Object[]{"###", "# #", "###", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", Character.valueOf('#'), Block.planks});
		this.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", Character.valueOf('#'), Block.cobblestone});
		this.addRecipe(new ItemStack(Block.stairSingle, 3), new Object[]{"###", Character.valueOf('#'), Block.cobblestone});
		this.addRecipe(new ItemStack(Block.clothGray, 1), new Object[]{"###", "###", "###", Character.valueOf('#'), Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", Character.valueOf('X'), Item.gunpowder, Character.valueOf('#'), Block.sand});
		this.addRecipe(new ItemStack(Item.flintAndSteel, 1), new Object[]{"A ", " B", Character.valueOf('A'), Item.ingotIron, Character.valueOf('B'), Item.flint});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Block.planks, Character.valueOf('X'), Block.clothGray});
		this.addRecipe(new ItemStack(Block.coalLamp, 1), new Object[]{"###", "#X#", "###", Character.valueOf('#'), Block.glass, Character.valueOf('X'), Item.coal});
		
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty});
		this.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", Character.valueOf('X'), Block.mushroomRed, Character.valueOf('Y'), Block.mushroomBrown, Character.valueOf('#'), Item.bowlEmpty});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", Character.valueOf('#'), Item.wheat});
		this.addRecipe(new ItemStack(Item.sugar, 1), new Object[]{"#", Character.valueOf('#'), Item.sugarBeet});
		this.addRecipe(new ItemStack(Item.applePie, 1), new Object[]{"ABC", Character.valueOf('A'), Item.apple, Character.valueOf('B'), Item.wheat, Character.valueOf('C'), Item.sugar});
		
		Collections.sort(this.recipes, new RecipeSorter());
		System.out.println(this.recipes.size() + " recipes");
	}

	final void addRecipe(ItemStack result, Object... recipe) {
		
		String var3 = "";
		
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;
		
		if(recipe[0] instanceof String[]) {
			++var4;
			String[] var11 = (String[]) recipe[0];

			for(int var8 = 0; var8 < var11.length; ++var8) {
				String var9 = var11[var8];
				++var6;
				var5 = var9.length();
				var3 = var3 + var9;
			}
		} else {
			while(recipe[var4] instanceof String) {
				String var7 = (String) recipe[var4++];
				++var6;
				var5 = var7.length();
				var3 = var3 + var7;
			}
		}

		HashMap<Character, Integer> ingredientMap = new HashMap<Character, Integer>();
		
		for(; var4 < recipe.length; var4 += 2) {
			
			int ingredientID = 0;
			
			if (recipe[var4 + 1] instanceof Item) {
				ingredientID = ((Item) recipe[var4 + 1]).shiftedIndex;
			} else if (recipe[var4 + 1] instanceof Block) {
				ingredientID = ((Block) recipe[var4 + 1]).blockID;
			}

			ingredientMap.put((Character) recipe[var4], ingredientID);
		}

		int[] var14 = new int[var5 * var6];

		for(int i = 0; i < var5 * var6; i++) {
			
			char var10 = var3.charAt(i);
			if (ingredientMap.containsKey(Character.valueOf(var10))) {
				var14[i] = ingredientMap.get(Character.valueOf(var10)).intValue();
			} else {
				var14[i] = -1;
			}
		}

		this.recipes.add(new CraftingRecipe(var5, var6, var14, result));
	}

	public final ItemStack findMatchingRecipe(int[] var1) {
		
		for(int var2 = 0; var2 < this.recipes.size(); ++var2) {
			CraftingRecipe var3 = (CraftingRecipe)this.recipes.get(var2);
			if(var3.matchRecipe(var1)) {
				return var3.createResult();
			}
		}

		return null;
	}
}
