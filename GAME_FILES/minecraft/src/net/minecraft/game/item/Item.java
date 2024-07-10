package net.minecraft.game.item;

import java.util.Random;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class Item {
	protected static Random rand = new Random();
	public static Item[] itemsList = new Item[1024];
	
	public static Item coal;
	public static Item ingotIron;
	public static Item ingotGold;
	public static Item diamond;
	
	public static Item shovelWood;
	public static Item shovelStone;
	public static Item shovelIron;
	public static Item shovelGold;
	public static Item shovelDiamond;
	
	public static Item pickaxeWood;
	public static Item pickaxeStone;
	public static Item pickaxeIron;
	public static Item pickaxeGold;
	public static Item pickaxeDiamond;
	
	public static Item axeWood;
	public static Item axeStone;
	public static Item axeIron;
	public static Item axeGold;
	public static Item axeDiamond;
	
	public static Item swordWood;
	public static Item swordStone;
	public static Item swordIron;
	public static Item swordGold;
	public static Item swordDiamond;
	
	public static Item hoeWood;
	public static Item hoeStone;
	public static Item hoeIron;
	public static Item hoeDiamond;
	public static Item hoeGold;
	
	public static Item helmetLeather;
	public static Item chestplateLeather;
	public static Item leggingsLeather;
	public static Item bootsLeather;
	
	public static Item helmetChain;
	public static Item chestplateChain;
	public static Item leggingsChain;
	public static Item bootsChain;
	
	public static Item helmetIron;
	public static Item chestplateIron;
	public static Item leggingsIron;
	public static Item bootsIron;
	
	public static Item helmetDiamond;
	public static Item chestplateDiamond;
	public static Item leggingsDiamond;
	public static Item bootsDiamond;
	
	public static Item helmetGold;
	public static Item chestplateGold;
	public static Item leggingsGold;
	public static Item bootsGold;
	
	public static Item flintAndSteel;
	public static Item bow;
	public static Item arrow;
	public static Item stick;
	public static Item bowlEmpty;
	public static Item bowlSoup;
	public static Item silk;
	public static Item feather;
	public static Item gunpowder;
	public static Item seeds;
	public static Item wheat;
	public static Item bread;
	public static Item flint;
	public static Item porkRaw;
	public static Item porkCooked;
	public static Item painting;
	
	public final int shiftedIndex;
	protected int maxStackSize = 64;
	protected int maxDamage = 32;
	protected int iconIndex;
	protected String name;

	protected Item(String name, int index) {
		this.name = name;
		
		this.shiftedIndex = index + 256;
		
		if(itemsList[index + 256] != null)
			System.out.println("ITEM CONFLICT @ " + index);

		itemsList[index + 256] = this;
	}

	public final Item setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
		return this;
	}

	public final int getIconIndex() {
		return this.iconIndex;
	}

	public boolean onItemUse(ItemStack item, World world, int var3, int var4, int var5, int var6) {
		return false;
	}

	public float getStrVsBlock(Block block) {
		return 1.0F;
	}

	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		return item;
	}

	public final int getItemStackLimit() {
		return this.maxStackSize;
	}

	public final int getMaxDamage() {
		return this.maxDamage;
	}

	public void hitEntity(ItemStack item) {}

	public void onBlockDestroyed(ItemStack item) {}

	public int getDamageVsEntity() {
		return 1;
	}

	public boolean canHarvestBlock(Block block) {
		return false;
	}

	static {
		ItemSpade var10000 = new ItemSpade("Iron Shovel", 0, 2);
		byte var1 = 82;
		ItemSpade var0 = var10000;
		var0.iconIndex = var1;
		shovelIron = var0;
		
		ItemPickaxe var15 = new ItemPickaxe("Iron Pickaxe", 1, 2);
		var1 = 98;
		ItemPickaxe var2 = var15;
		var2.iconIndex = var1;
		pickaxeIron = var2;
		
		ItemAxe var16 = new ItemAxe("Iron Axe", 2, 2);
		var1 = 114;
		ItemAxe var3 = var16;
		var3.iconIndex = var1;
		axeIron = var3;
		
		ItemFlintAndSteel var17 = new ItemFlintAndSteel("Flint and Steel", 3);
		var1 = 5;
		ItemFlintAndSteel var4 = var17;
		var4.iconIndex = var1;
		flintAndSteel = var4;
		
		ItemFood var18 = new ItemFood("IDK", 4, 4);
		var1 = 4;
		ItemFood var5 = var18;
		var5.iconIndex = var1;
		
		ItemBow var19 = new ItemBow("Bow", 5);
		var1 = 21;
		ItemBow var6 = var19;
		var6.iconIndex = var1;
		bow = var6;
		
		Item var20 = new Item("Arrow", 6);
		var1 = 37;
		Item var7 = var20;
		var7.iconIndex = var1;
		arrow = var7;
		
		var20 = new Item("Coal", 7);
		var1 = 7;
		var7 = var20;
		var7.iconIndex = var1;
		coal = var7;
		
		var20 = new Item("Diamond", 8);
		var1 = 55;
		var7 = var20;
		var7.iconIndex = var1;
		diamond = var7;
		
		var20 = new Item("Iron Ingot", 9);
		var1 = 23;
		var7 = var20;
		var7.iconIndex = var1;
		ingotIron = var7;
		
		var20 = new Item("Gold Ingot", 10);
		var1 = 39;
		var7 = var20;
		var7.iconIndex = var1;
		ingotGold = var7;
		
		ItemSword var21 = new ItemSword("Iron Sword", 11, 2);
		var1 = 66;
		ItemSword var8 = var21;
		var8.iconIndex = var1;
		swordIron = var8;
		
		var21 = new ItemSword("Wood Sword", 12, 0);
		var1 = 64;
		var8 = var21;
		var8.iconIndex = var1;
		swordWood = var8;
		
		var10000 = new ItemSpade("Wood Shovel", 13, 0);
		var1 = 80;
		var0 = var10000;
		var0.iconIndex = var1;
		shovelWood = var0;
		
		var15 = new ItemPickaxe("Wood Pickaxe", 14, 0);
		var1 = 96;
		var2 = var15;
		var2.iconIndex = var1;
		pickaxeWood = var2;
		
		var16 = new ItemAxe("Wood Axe", 15, 0);
		var1 = 112;
		var3 = var16;
		var3.iconIndex = var1;
		axeWood = var3;
		
		var21 = new ItemSword("Stone Sword", 16, 1);
		var1 = 65;
		var8 = var21;
		var8.iconIndex = var1;
		swordStone = var8;
		
		var10000 = new ItemSpade("Stone Shovel", 17, 1);
		var1 = 81;
		var0 = var10000;
		var0.iconIndex = var1;
		shovelStone = var0;
		
		var15 = new ItemPickaxe("Stone Pickaxe", 18, 1);
		var1 = 97;
		var2 = var15;
		var2.iconIndex = var1;
		pickaxeStone = var2;
		
		var16 = new ItemAxe("Stone Axe", 19, 1);
		var1 = 113;
		var3 = var16;
		var3.iconIndex = var1;
		axeStone = var3;
		
		var21 = new ItemSword("Diamond Sword", 20, 3);
		var1 = 67;
		var8 = var21;
		var8.iconIndex = var1;
		swordDiamond = var8;
		
		var10000 = new ItemSpade("Diamond Shovel", 21, 3);
		var1 = 83;
		var0 = var10000;
		var0.iconIndex = var1;
		shovelDiamond = var0;
		
		var15 = new ItemPickaxe("Diamond Pickaxe", 22, 3);
		var1 = 99;
		var2 = var15;
		var2.iconIndex = var1;
		pickaxeDiamond = var2;
		
		var16 = new ItemAxe("Diamond Axe", 23, 3);
		var1 = 115;
		var3 = var16;
		var3.iconIndex = var1;
		axeDiamond = var3;
		
		var20 = new Item("Stick", 24);
		var1 = 53;
		var7 = var20;
		var7.iconIndex = var1;
		stick = var7;
		
		var20 = new Item("Empty Bowl", 25);
		var1 = 71;
		var7 = var20;
		var7.iconIndex = var1;
		bowlEmpty = var7;
		
		ItemSoup var22 = new ItemSoup("Mushroom Stew", 26, 10);
		var1 = 72;
		ItemSoup var9 = var22;
		var9.iconIndex = var1;
		bowlSoup = var9;
		
		var21 = new ItemSword("Gold Sword", 27, 0);
		var1 = 68;
		var8 = var21;
		var8.iconIndex = var1;
		swordGold = var8;
		
		var10000 = new ItemSpade("Gold Shovel", 28, 0);
		var1 = 84;
		var0 = var10000;
		var0.iconIndex = var1;
		shovelGold = var0;
		
		var15 = new ItemPickaxe("Gold Pickaxe", 29, 0);
		var1 = 100;
		var2 = var15;
		var2.iconIndex = var1;
		pickaxeGold = var2;
		
		var16 = new ItemAxe("Gold Axe", 30, 0);
		var1 = 116;
		var3 = var16;
		var3.iconIndex = var1;
		axeGold = var3;
		
		var20 = new Item("Silk", 31);
		var1 = 8;
		var7 = var20;
		var7.iconIndex = var1;
		silk = var7;
		
		var20 = new Item("Feather", 32);
		var1 = 24;
		var7 = var20;
		var7.iconIndex = var1;
		feather = var7;
		
		var20 = new Item("Sulphur", 33);
		var1 = 40;
		var7 = var20;
		var7.iconIndex = var1;
		gunpowder = var7;
		
		ItemHoe var23 = new ItemHoe("Wood Hoe", 34, 0);
		short var11 = 128;
		ItemHoe var10 = var23;
		var10.iconIndex = var11;
		hoeWood = var10;
		
		var23 = new ItemHoe("Stone Hoe", 35, 1);
		var11 = 129;
		var10 = var23;
		var10.iconIndex = var11;
		hoeStone = var10;
		
		var23 = new ItemHoe("Iron Hoe", 36, 2);
		var11 = 130;
		var10 = var23;
		var10.iconIndex = var11;
		hoeIron = var10;
		
		var23 = new ItemHoe("Diamond Hoe", 37, 3);
		var11 = 131;
		var10 = var23;
		var10.iconIndex = var11;
		hoeDiamond = var10;
		
		var23 = new ItemHoe("Gold Hoe", 38, 4);
		var11 = 132;
		var10 = var23;
		var10.iconIndex = var11;
		hoeGold = var10;
		
		ItemSeeds var24 = new ItemSeeds("Wheat Seeds", 39, Block.crops.blockID);
		var1 = 9;
		ItemSeeds var12 = var24;
		var12.iconIndex = var1;
		seeds = var12;
		
		var20 = new Item("Wheat", 40);
		var1 = 25;
		var7 = var20;
		var7.iconIndex = var1;
		wheat = var7;
		
		var18 = new ItemFood("Bread", 41, 5);
		var1 = 41;
		var5 = var18;
		var5.iconIndex = var1;
		bread = var5;
		
		ItemArmor var25 = new ItemArmor("Leather Helmet", 42, 0, 0, 0);
		var1 = 0;
		ItemArmor var13 = var25;
		var13.iconIndex = var1;
		helmetLeather = var13;
		
		var25 = new ItemArmor("Leather Chestplate", 43, 0, 0, 1);
		var1 = 16;
		var13 = var25;
		var13.iconIndex = var1;
		chestplateLeather = var13;
		
		var25 = new ItemArmor("Leather Leggings", 44, 0, 0, 2);
		var1 = 32;
		var13 = var25;
		var13.iconIndex = var1;
		leggingsLeather = var13;
		
		var25 = new ItemArmor("Leather Boots", 45, 0, 0, 3);
		var1 = 48;
		var13 = var25;
		var13.iconIndex = var1;
		bootsLeather = var13;
		
		var25 = new ItemArmor("Chain Helmet", 46, 1, 1, 0);
		var1 = 1;
		var13 = var25;
		var13.iconIndex = var1;
		helmetChain = var13;
		
		var25 = new ItemArmor("Chain Chestplate", 47, 1, 1, 1);
		var1 = 17;
		var13 = var25;
		var13.iconIndex = var1;
		chestplateChain = var13;
		
		var25 = new ItemArmor("Chain Leggings", 48, 1, 1, 2);
		var1 = 33;
		var13 = var25;
		var13.iconIndex = var1;
		leggingsChain = var13;
		
		var25 = new ItemArmor("Chain Boots", 49, 1, 1, 3);
		var1 = 49;
		var13 = var25;
		var13.iconIndex = var1;
		bootsChain = var13;
		
		var25 = new ItemArmor("Iron Helmet", 50, 2, 2, 0);
		var1 = 2;
		var13 = var25;
		var13.iconIndex = var1;
		helmetIron = var13;
		
		var25 = new ItemArmor("Iron Chestplate", 51, 2, 2, 1);
		var1 = 18;
		var13 = var25;
		var13.iconIndex = var1;
		chestplateIron = var13;
		
		var25 = new ItemArmor("Iron Leggings", 52, 2, 2, 2);
		var1 = 34;
		var13 = var25;
		var13.iconIndex = var1;
		leggingsIron = var13;
		
		var25 = new ItemArmor("Iron Boots", 53, 2, 2, 3);
		var1 = 50;
		var13 = var25;
		var13.iconIndex = var1;
		bootsIron = var13;
		
		var25 = new ItemArmor("Diamond Helmet", 54, 3, 3, 0);
		var1 = 3;
		var13 = var25;
		var13.iconIndex = var1;
		helmetDiamond = var13;
		
		var25 = new ItemArmor("Diamond Chestplate", 55, 3, 3, 1);
		var1 = 19;
		var13 = var25;
		var13.iconIndex = var1;
		chestplateDiamond = var13;
		
		var25 = new ItemArmor("Diamond Leggings", 56, 3, 3, 2);
		var1 = 35;
		var13 = var25;
		var13.iconIndex = var1;
		leggingsDiamond = var13;
		
		var25 = new ItemArmor("Diamond Boots", 57, 3, 3, 3);
		var1 = 51;
		var13 = var25;
		var13.iconIndex = var1;
		bootsDiamond = var13;
		
		var25 = new ItemArmor("Gold Helmet", 58, 1, 4, 0);
		var1 = 4;
		var13 = var25;
		var13.iconIndex = var1;
		helmetGold = var13;
		
		var25 = new ItemArmor("Gold Chestplate", 59, 1, 4, 1);
		var1 = 20;
		var13 = var25;
		var13.iconIndex = var1;
		chestplateGold = var13;
		
		var25 = new ItemArmor("Gold Leggings", 60, 1, 4, 2);
		var1 = 36;
		var13 = var25;
		var13.iconIndex = var1;
		leggingsGold = var13;
		
		var25 = new ItemArmor("Gold Boots", 61, 1, 4, 3);
		var1 = 52;
		var13 = var25;
		var13.iconIndex = var1;
		bootsGold = var13;
		
		var20 = new Item("Flint", 62);
		var1 = 6;
		var7 = var20;
		var7.iconIndex = var1;
		flint = var7;
		
		var18 = new ItemFood("Raw Porkchop", 63, 3);
		var1 = 87;
		var5 = var18;
		var5.iconIndex = var1;
		porkRaw = var5;
		
		var18 = new ItemFood("Cooked Porkchop", 64, 8);
		var1 = 88;
		var5 = var18;
		var5.iconIndex = var1;
		porkCooked = var5;
		
		ItemPainting var26 = new ItemPainting("Painting", 65);
		var1 = 26;
		ItemPainting var14 = var26;
		var14.iconIndex = var1;
		painting = var14;
	}
}
