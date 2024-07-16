package net.minecraft.game.item;

import java.util.Random;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class Item {
	
	protected static Random rand = new Random();
	public static Item[] itemsList = new Item[1024];
	
	public static Item swordWood;
	public static Item pickaxeWood;
	public static Item axeWood;
	public static Item shovelWood;
	public static Item hoeWood;
	
	public static Item swordStone;
	public static Item pickaxeStone;
	public static Item axeStone;
	public static Item shovelStone;
	public static Item hoeStone;
	
	public static Item swordIron;
	public static Item pickaxeIron;
	public static Item axeIron;
	public static Item shovelIron;
	public static Item hoeIron;
	
	public static Item swordGold;
	public static Item pickaxeGold;
	public static Item axeGold;
	public static Item shovelGold;
	public static Item hoeGold;
	
	public static Item swordDiamond;
	public static Item pickaxeDiamond;
	public static Item axeDiamond;
	public static Item shovelDiamond;
	public static Item hoeDiamond;
	
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
	
	public static Item helmetGold;
	public static Item chestplateGold;
	public static Item leggingsGold;
	public static Item bootsGold;
	
	public static Item helmetDiamond;
	public static Item chestplateDiamond;
	public static Item leggingsDiamond;
	public static Item bootsDiamond;
	
	public static Item coal;
	public static Item ingotIron;
	public static Item ingotGold;
	public static Item diamond;
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
		// swords and tools
		swordWood = new ItemSword("Wood Sword", 0, 0);
		swordWood.iconIndex = 64;
		
		pickaxeWood = new ItemPickaxe("Wood Pickaxe", 1, 0);
		pickaxeWood.iconIndex = 96;
		
		axeWood = new ItemAxe("Wood Axe", 2, 0);
		axeWood.iconIndex = 112;
		
		shovelWood = new ItemSpade("Wood Shovel", 3, 0);
		shovelWood.iconIndex = 80;
		
		hoeWood = new ItemHoe("Wood Hoe", 4, 0);
		hoeWood.iconIndex = 128;
		
		swordStone = new ItemSword("Stone Sword", 5, 1);
		swordStone.iconIndex = 65;
		
		pickaxeStone = new ItemPickaxe("Stone Pickaxe", 6, 1);
		pickaxeStone.iconIndex = 97;
		
		axeStone = new ItemAxe("Stone Axe", 7, 1);
		axeStone.iconIndex = 113;
		
		shovelStone = new ItemSpade("Stone Shovel", 8, 1);
		shovelStone.iconIndex = 81;
		
		hoeStone = new ItemHoe("Stone Hoe", 9, 1);
		hoeStone.iconIndex = 129;
		
		swordIron = new ItemSword("Iron Sword", 10, 2);
		swordIron.iconIndex = 66;
		
		pickaxeIron = new ItemPickaxe("Iron Pickaxe", 11, 2);
		pickaxeIron.iconIndex = 98;
		
		axeIron = new ItemAxe("Iron Axe", 12, 2);
		axeIron.iconIndex = 114;
		
		shovelIron = new ItemSpade("Iron Shovel", 13, 2);
		shovelIron.iconIndex = 82;
		
		hoeIron = new ItemHoe("Iron Hoe", 14, 2);
		hoeIron.iconIndex = 130;
		
		swordGold = new ItemSword("Gold Sword", 15, 0);
		swordGold.iconIndex = 68;
		
		pickaxeGold = new ItemPickaxe("Gold Pickaxe", 16, 0);
		pickaxeGold.iconIndex = 100;
		
		axeGold = new ItemAxe("Gold Axe", 17, 0);
		axeGold.iconIndex = 116;
		
		shovelGold = new ItemSpade("Gold Shovel", 18, 0);
		shovelGold.iconIndex = 84;
		
		hoeGold = new ItemHoe("Gold Hoe", 19, 4);
		hoeGold.iconIndex = 132;
		
		swordDiamond = new ItemSword("Diamond Sword", 20, 3);
		swordDiamond.iconIndex = 67;
		swordDiamond.rarity = Rarity.RARE;
		
		pickaxeDiamond = new ItemPickaxe("Diamond Pickaxe", 21, 3);
		pickaxeDiamond.iconIndex = 99;
		pickaxeDiamond.rarity = Rarity.RARE;
		
		axeDiamond = new ItemAxe("Diamond Axe", 22, 3);
		axeDiamond.iconIndex = 115;
		axeDiamond.rarity = Rarity.RARE;
		
		shovelDiamond = new ItemSpade("Diamond Shovel", 23, 3);
		shovelDiamond.iconIndex = 83;
		shovelDiamond.rarity = Rarity.RARE;
		
		hoeDiamond = new ItemHoe("Diamond Hoe", 24, 3);
		hoeDiamond.iconIndex = 131;
		hoeDiamond.rarity = Rarity.RARE;
		
		// armor
		helmetLeather = new ItemArmor("Leather Helmet", 25, 0, 0, 0);
		helmetLeather.iconIndex = 0;
		
		chestplateLeather = new ItemArmor("Leather Chestplate", 26, 0, 0, 1);
		chestplateLeather.iconIndex = 16;
		
		leggingsLeather = new ItemArmor("Leather Leggings", 27, 0, 0, 2);
		leggingsLeather.iconIndex = 32;
		
		bootsLeather = new ItemArmor("Leather Boots", 28, 0, 0, 3);
		bootsLeather.iconIndex = 48;
		
		helmetChain = new ItemArmor("Chain Helmet", 29, 1, 1, 0);
		helmetChain.iconIndex = 1;
		
		chestplateChain = new ItemArmor("Chain Chestplate", 30, 1, 1, 1);
		chestplateChain.iconIndex = 17;
		
		leggingsChain = new ItemArmor("Chain Leggings", 31, 1, 1, 2);
		leggingsChain.iconIndex = 33;
		
		bootsChain = new ItemArmor("Chain Boots", 32, 1, 1, 3);
		bootsChain.iconIndex = 49;
		
		helmetIron = new ItemArmor("Iron Helmet", 33, 2, 2, 0);
		helmetIron.iconIndex = 2;
		
		chestplateIron = new ItemArmor("Iron Chestplate", 34, 2, 2, 1);
		chestplateIron.iconIndex = 18;
		
		leggingsIron = new ItemArmor("Iron Leggings", 35, 2, 2, 2);
		leggingsIron.iconIndex = 34;
		
		bootsIron = new ItemArmor("Iron Boots", 36, 2, 2, 3);
		bootsIron.iconIndex = 50;
		
		helmetGold = new ItemArmor("Gold Helmet", 37, 1, 4, 0);
		helmetGold.iconIndex = 4;
		
		chestplateGold = new ItemArmor("Gold Chestplate", 38, 1, 4, 1);
		chestplateGold.iconIndex = 20;
		
		leggingsGold = new ItemArmor("Gold Leggings", 39, 1, 4, 2);
		leggingsGold.iconIndex = 36;
		
		bootsGold = new ItemArmor("Gold Boots", 40, 1, 4, 3);
		bootsGold.iconIndex = 52;
		
		helmetDiamond = new ItemArmor("Diamond Helmet", 41, 3, 3, 0);
		helmetDiamond.iconIndex = 3;
		helmetDiamond.rarity = Rarity.RARE;
		
		chestplateDiamond = new ItemArmor("Diamond Chestplate", 42, 3, 3, 1);
		chestplateDiamond.iconIndex = 19;
		chestplateDiamond.rarity = Rarity.RARE;
		
		leggingsDiamond = new ItemArmor("Diamond Leggings", 43, 3, 3, 2);
		leggingsDiamond.iconIndex = 35;
		leggingsDiamond.rarity = Rarity.RARE;
		
		bootsDiamond = new ItemArmor("Diamond Boots", 44, 3, 3, 3);
		bootsDiamond.iconIndex = 51;
		bootsDiamond.rarity = Rarity.RARE;
		
		// other, tools?
		flintAndSteel = new ItemFlintAndSteel("Flint and Steel", 45);
		flintAndSteel.iconIndex = 5;
		
		bow = new ItemBow("Bow", 46);
		bow.iconIndex = 21;
		
		arrow = new Item("Arrow", 47);
		arrow.iconIndex = 37;
		
		// food and crops and stuff idk
		apple = new ItemFood("Apple", 48, 4);
		apple.iconIndex = 10;
		
		applePie = new ItemFood("Apple Pie", 49, 8);
		applePie.iconIndex = 11;
		
		bowlEmpty = new Item("Bowl", 50);
		bowlEmpty.iconIndex = 71;
		
		bowlSoup = new ItemSoup("Mushroom Stew", 51, 10);
		bowlSoup.iconIndex = 72;
		
		seeds = new ItemSeeds("Wheat Seeds", 52, Block.crops.blockID);
		seeds.iconIndex = 9;
		
		wheat = new Item("Wheat", 53);
		wheat.iconIndex = 25;
		
		bread = new ItemFood("Bread", 54, 5);
		bread.iconIndex = 41;
		
		porkRaw = new ItemFood("Raw Porkchop", 55, 3);
		porkRaw.iconIndex = 87;
		
		porkCooked = new ItemFood("Cooked Porkchop", 56, 8);
		porkCooked.iconIndex = 88;
		
		sugarBeet = new Item("Sugar Beet", 57);
		sugarBeet.iconIndex = 103;
		
		sugar = new Item("Sugar", 58);
		sugar.iconIndex = 104;
		
		// misc I guess
		coal = new Item("Coal", 59);
		coal.iconIndex = 7;
		
		ingotIron = new Item("Iron Ingot", 60);
		ingotIron.iconIndex = 23;
		
		ingotGold = new Item("Gold Ingot", 61);
		ingotGold.iconIndex = 39;
		
		diamond = new Item("Diamond", 62);
		diamond.iconIndex = 55;
		diamond.rarity = Rarity.RARE;
		
		stick = new Item("Stick", 63);
		stick.iconIndex = 53;
		
		silk = new Item("Silk", 64);
		silk.iconIndex = 8;
		
		feather = new Item("Feather", 65);
		feather.iconIndex = 24;
		
		gunpowder = new Item("Sulphur", 66);
		gunpowder.iconIndex = 40;
		
		flint = new Item("Flint", 67);
		flint.iconIndex = 6;
		
		painting = new ItemPainting("Painting", 68);
		painting.iconIndex = 26;
	}
}
