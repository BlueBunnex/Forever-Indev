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
	
	public static Item apple;
	public static Item sugarBeet;
	public static Item sugar;
	public static Item applePie;
	
	public final int shiftedIndex;
	protected int maxStackSize = 64;
	protected int maxDamage = 32;
	protected int iconIndex;
	protected String name;
	protected Rarity rarity;
	
	protected Item(String name, Rarity rarity, int index) {
		this.name = name;
		this.rarity = rarity;
		
		this.shiftedIndex = index + 256;
		
		if(itemsList[index + 256] != null)
			System.out.println("ITEM CONFLICT @ " + index);

		itemsList[index + 256] = this;
	}

	protected Item(String name, int index) {
		this(name, Rarity.COMMON, index);
	}

	public final Item setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
		return this;
	}

	public final int getIconIndex() {
		return this.iconIndex;
	}
	
	public final String getName() {
		return name;
	}
	
	public final Rarity getRarity() {
		return rarity;
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
		shovelIron = new ItemSpade("Iron Shovel", 0, 2);
		shovelIron.iconIndex = 82;
		
		pickaxeIron = new ItemPickaxe("Iron Pickaxe", 1, 2);
		pickaxeIron.iconIndex = 98;
		
		axeIron = new ItemAxe("Iron Axe", 2, 2);
		axeIron.iconIndex = 114;
		
		flintAndSteel = new ItemFlintAndSteel("Flint and Steel", 3);
		flintAndSteel.iconIndex = 5;
		
		apple = new ItemFood("Apple", 4, 4);
		apple.iconIndex = 10;
		
		bow = new ItemBow("Bow", 5);
		bow.iconIndex = 21;
		
		arrow = new Item("Arrow", 6);
		arrow.iconIndex = 37;
		
		coal = new Item("Coal", 7);
		coal.iconIndex = 7;
		
		diamond = new Item("Diamond", 8);
		diamond.iconIndex = 55;
		diamond.rarity = Rarity.RARE;
		
		ingotIron = new Item("Iron Ingot", 9);
		ingotIron.iconIndex = 23;
		
		ingotGold = new Item("Gold Ingot", 10);
		ingotGold.iconIndex = 39;
		
		swordIron = new ItemSword("Iron Sword", 11, 2);
		swordIron.iconIndex = 66;
		
		swordWood = new ItemSword("Wood Sword", 12, 0);
		swordWood.iconIndex = 64;
		
		shovelWood = new ItemSpade("Wood Shovel", 13, 0);
		shovelWood.iconIndex = 80;
		
		pickaxeWood = new ItemPickaxe("Wood Pickaxe", 14, 0);
		pickaxeWood.iconIndex = 96;
		
		axeWood = new ItemAxe("Wood Axe", 15, 0);
		axeWood.iconIndex = 112;
		
		swordStone = new ItemSword("Stone Sword", 16, 1);
		swordStone.iconIndex = 65;
		
		shovelStone = new ItemSpade("Stone Shovel", 17, 1);
		shovelStone.iconIndex = 81;
		
		pickaxeStone = new ItemPickaxe("Stone Pickaxe", 18, 1);
		pickaxeStone.iconIndex = 97;
		
		axeStone = new ItemAxe("Stone Axe", 19, 1);
		axeStone.iconIndex = 113;
		
		swordDiamond = new ItemSword("Diamond Sword", 20, 3);
		swordDiamond.iconIndex = 67;
		swordDiamond.rarity = Rarity.RARE;
		
		shovelDiamond = new ItemSpade("Diamond Shovel", 21, 3);
		shovelDiamond.iconIndex = 83;
		shovelDiamond.rarity = Rarity.RARE;
		
		pickaxeDiamond = new ItemPickaxe("Diamond Pickaxe", 22, 3);
		pickaxeDiamond.iconIndex = 99;
		pickaxeDiamond.rarity = Rarity.RARE;
		
		axeDiamond = new ItemAxe("Diamond Axe", 23, 3);
		axeDiamond.iconIndex = 115;
		axeDiamond.rarity = Rarity.RARE;
		
		hoeDiamond = new ItemHoe("Diamond Hoe", 37, 3);
		hoeDiamond.iconIndex = 131;
		hoeDiamond.rarity = Rarity.RARE;
		
		stick = new Item("Stick", 24);
		stick.iconIndex = 53;
		
		bowlEmpty = new Item("Empty Bowl", 25);
		bowlEmpty.iconIndex = 71;
		
		bowlSoup = new ItemSoup("Mushroom Stew", 26, 10);
		bowlSoup.iconIndex = 72;
		
		swordGold = new ItemSword("Gold Sword", 27, 0);
		swordGold.iconIndex = 68;
		
		shovelGold = new ItemSpade("Gold Shovel", 28, 0);
		shovelGold.iconIndex = 84;
		
		pickaxeGold = new ItemPickaxe("Gold Pickaxe", 29, 0);
		pickaxeGold.iconIndex = 100;
		
		axeGold = new ItemAxe("Gold Axe", 30, 0);
		axeGold.iconIndex = 116;
		
		silk = new Item("Silk", 31);
		silk.iconIndex = 8;
		
		feather = new Item("Feather", 32);
		feather.iconIndex = 24;
		
		gunpowder = new Item("Sulphur", 33);
		gunpowder.iconIndex = 40;
		
		hoeWood = new ItemHoe("Wood Hoe", 34, 0);
		hoeWood.iconIndex = 128;
		
		hoeStone = new ItemHoe("Stone Hoe", 35, 1);
		hoeStone.iconIndex = 129;
		
		hoeIron = new ItemHoe("Iron Hoe", 36, 2);
		hoeIron.iconIndex = 130;
		
		hoeGold = new ItemHoe("Gold Hoe", 38, 4);
		hoeGold.iconIndex = 132;
		
		seeds = new ItemSeeds("Wheat Seeds", 39, Block.crops.blockID);
		seeds.iconIndex = 9;
		
		wheat = new Item("Wheat", 40);
		wheat.iconIndex = 25;
		
		bread = new ItemFood("Bread", 41, 5);
		bread.iconIndex = 41;
		
		helmetLeather = new ItemArmor("Leather Helmet", 42, 0, 0, 0);
		helmetLeather.iconIndex = 0;
		
		chestplateLeather = new ItemArmor("Leather Chestplate", 43, 0, 0, 1);
		chestplateLeather.iconIndex = 16;
		
		leggingsLeather = new ItemArmor("Leather Leggings", 44, 0, 0, 2);
		leggingsLeather.iconIndex = 32;
		
		bootsLeather = new ItemArmor("Leather Boots", 45, 0, 0, 3);
		bootsLeather.iconIndex = 48;
		
		helmetChain = new ItemArmor("Chain Helmet", 46, 1, 1, 0);
		helmetChain.iconIndex = 1;
		
		chestplateChain = new ItemArmor("Chain Chestplate", 47, 1, 1, 1);
		chestplateChain.iconIndex = 17;
		
		leggingsChain = new ItemArmor("Chain Leggings", 48, 1, 1, 2);
		leggingsChain.iconIndex = 33;
		
		bootsChain = new ItemArmor("Chain Boots", 49, 1, 1, 3);
		bootsChain.iconIndex = 49;
		
		helmetIron = new ItemArmor("Iron Helmet", 50, 2, 2, 0);
		helmetIron.iconIndex = 2;
		
		chestplateIron = new ItemArmor("Iron Chestplate", 51, 2, 2, 1);
		chestplateIron.iconIndex = 18;
		
		leggingsIron = new ItemArmor("Iron Leggings", 52, 2, 2, 2);
		leggingsIron.iconIndex = 34;
		
		bootsIron = new ItemArmor("Iron Boots", 53, 2, 2, 3);
		bootsIron.iconIndex = 50;
		
		helmetDiamond = new ItemArmor("Diamond Helmet", 54, 3, 3, 0);
		helmetDiamond.iconIndex = 3;
		helmetDiamond.rarity = Rarity.RARE;
		
		chestplateDiamond = new ItemArmor("Diamond Chestplate", 55, 3, 3, 1);
		chestplateDiamond.iconIndex = 19;
		chestplateDiamond.rarity = Rarity.RARE;
		
		leggingsDiamond = new ItemArmor("Diamond Leggings", 56, 3, 3, 2);
		leggingsDiamond.iconIndex = 35;
		leggingsDiamond.rarity = Rarity.RARE;
		
		bootsDiamond = new ItemArmor("Diamond Boots", 57, 3, 3, 3);
		bootsDiamond.iconIndex = 51;
		bootsDiamond.rarity = Rarity.RARE;
		
		helmetGold = new ItemArmor("Gold Helmet", 58, 1, 4, 0);
		helmetGold.iconIndex = 4;
		
		chestplateGold = new ItemArmor("Gold Chestplate", 59, 1, 4, 1);
		chestplateGold.iconIndex = 20;
		
		leggingsGold = new ItemArmor("Gold Leggings", 60, 1, 4, 2);
		leggingsGold.iconIndex = 36;
		
		bootsGold = new ItemArmor("Gold Boots", 61, 1, 4, 3);
		bootsGold.iconIndex = 52;
		
		flint = new Item("Flint", 62);
		flint.iconIndex = 6;
		
		porkRaw = new ItemFood("Raw Porkchop", 63, 3);
		porkRaw.iconIndex = 87;
		
		porkCooked = new ItemFood("Cooked Porkchop", 64, 8);
		porkCooked.iconIndex = 88;
		
		painting = new ItemPainting("Painting", 65);
		painting.iconIndex = 26;
		
		sugarBeet = new Item("Sugar Beet", 66);
		sugarBeet.iconIndex = 103;
		
		sugar = new Item("Sugar", 67);
		sugar.iconIndex = 104;
		
		applePie = new ItemFood("Apple Pie", 68, 8);
		applePie.iconIndex = 11;
	}
}
