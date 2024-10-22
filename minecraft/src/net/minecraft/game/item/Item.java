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
    public static Item battleAxeWood;

    public static Item swordStone;
    public static Item pickaxeStone;
    public static Item axeStone;
    public static Item shovelStone;
    public static Item hoeStone;
    public static Item battleAxeStone;

    public static Item swordIron;
    public static Item pickaxeIron;
    public static Item axeIron;
    public static Item shovelIron;
    public static Item hoeIron;
    public static Item battleAxeIron;

    public static Item swordGold;
    public static Item pickaxeGold;
    public static Item axeGold;
    public static Item shovelGold;
    public static Item hoeGold;
    public static Item battleAxeGold;

    public static Item swordDiamond;
    public static Item pickaxeDiamond;
    public static Item axeDiamond;
    public static Item shovelDiamond;
    public static Item hoeDiamond;
    public static Item battleAxeDiamond;

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
    public static Item sweetBread;
    public static Item flint;
    public static Item porkRaw;
    public static Item porkCooked;
    public static Item painting;
    public static Item apple;
    public static Item sugarBeet;
    public static Item sugar;
    public static Item applePie;
    public static Item rupeePlain;
    public static Item rupeeDark;
    public static ItemQuiver quiver;

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

    public int getIconIndex() {
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

    public int getItemStackLimit() {
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
        swordWood.setIconIndex(64);

        pickaxeWood = new ItemPickaxe("Wood Pickaxe", 1, 0);
        pickaxeWood.setIconIndex(96);

        axeWood = new ItemAxe("Wood Axe", 2, 0);
        axeWood.setIconIndex(112);

        shovelWood = new ItemSpade("Wood Shovel", 3, 0);
        shovelWood.setIconIndex(80);

        hoeWood = new ItemHoe("Wood Hoe", 4, 0);
        hoeWood.setIconIndex(128);

        battleAxeWood = new ItemAxe("Wood Battle Axe", 5, 0);
        battleAxeWood.setIconIndex(117);

        swordStone = new ItemSword("Stone Sword", 6, 1);
        swordStone.setIconIndex(65);

        pickaxeStone = new ItemPickaxe("Stone Pickaxe", 7, 1);
        pickaxeStone.setIconIndex(97);

        axeStone = new ItemAxe("Stone Axe", 8, 1);
        axeStone.setIconIndex(113);

        shovelStone = new ItemSpade("Stone Shovel", 9, 1);
        shovelStone.setIconIndex(81);

        hoeStone = new ItemHoe("Stone Hoe", 10, 1);
        hoeStone.setIconIndex(129);

        battleAxeStone = new ItemAxe("Stone Battle Axe", 11, 1);
        battleAxeStone.setIconIndex(118);

        swordIron = new ItemSword("Iron Sword", 12, 2);
        swordIron.setIconIndex(66);

        pickaxeIron = new ItemPickaxe("Iron Pickaxe", 13, 2);
        pickaxeIron.setIconIndex(98);

        axeIron = new ItemAxe("Iron Axe", 14, 2);
        axeIron.setIconIndex(114);

        shovelIron = new ItemSpade("Iron Shovel", 15, 2);
        shovelIron.setIconIndex(82);

        hoeIron = new ItemHoe("Iron Hoe", 16, 2);
        hoeIron.setIconIndex(130);

        battleAxeIron = new ItemAxe("Iron Battle Axe", 17, 2);
        battleAxeIron.setIconIndex(119);

        swordGold = new ItemSword("Gold Sword", 18, 0);
        swordGold.setIconIndex(68);

        pickaxeGold = new ItemPickaxe("Gold Pickaxe", 19, 0);
        pickaxeGold.setIconIndex(100);

        axeGold = new ItemAxe("Gold Axe", 20, 0);
        axeGold.setIconIndex(116);

        shovelGold = new ItemSpade("Gold Shovel", 21, 0);
        shovelGold.setIconIndex(84);

        hoeGold = new ItemHoe("Gold Hoe", 22, 4);
        hoeGold.setIconIndex(132);

        battleAxeGold = new ItemAxe("Gold Battle Axe", 23, 0);
        battleAxeGold.setIconIndex(121);

        swordDiamond = new ItemSword("Diamond Sword", 24, 3);
        swordDiamond.setIconIndex(67);
        swordDiamond.rarity = Rarity.RARE;

        pickaxeDiamond = new ItemPickaxe("Diamond Pickaxe", 25, 3);
        pickaxeDiamond.setIconIndex(99);
        pickaxeDiamond.rarity = Rarity.RARE;

        axeDiamond = new ItemAxe("Diamond Axe", 26, 3);
        axeDiamond.setIconIndex(115);
        axeDiamond.rarity = Rarity.RARE;

        shovelDiamond = new ItemSpade("Diamond Shovel", 27, 3);
        shovelDiamond.setIconIndex(83);
        shovelDiamond.rarity = Rarity.RARE;

        hoeDiamond = new ItemHoe("Diamond Hoe", 28, 3);
        hoeDiamond.setIconIndex(131);
        hoeDiamond.rarity = Rarity.RARE;

        battleAxeDiamond = new ItemAxe("Diamond Battle Axe", 29, 3);
        battleAxeDiamond.setIconIndex(120);
        battleAxeDiamond.rarity = Rarity.RARE;

        // armor
        helmetIron = new ItemArmor("Iron Helmet", 33, 2, 2, 0);
        helmetIron.setIconIndex(2);

        chestplateIron = new ItemArmor("Iron Chestplate", 34, 2, 2, 1);
        chestplateIron.setIconIndex(18);

        leggingsIron = new ItemArmor("Iron Leggings", 35, 2, 2, 2);
        leggingsIron.setIconIndex(34);

        bootsIron = new ItemArmor("Iron Boots", 36, 2, 2, 3);
        bootsIron.setIconIndex(50);

        helmetGold = new ItemArmor("Gold Helmet", 37, 1, 4, 0);
        helmetGold.setIconIndex(4);

        chestplateGold = new ItemArmor("Gold Chestplate", 38, 1, 4, 1);
        chestplateGold.setIconIndex(20);

        leggingsGold = new ItemArmor("Gold Leggings", 39, 1, 4, 2);
        leggingsGold.setIconIndex(36);

        bootsGold = new ItemArmor("Gold Boots", 40, 1, 4, 3);
        bootsGold.setIconIndex(52);

        helmetDiamond = new ItemArmor("Diamond Helmet", 41, 3, 3, 0);
        helmetDiamond.setIconIndex(3);
        helmetDiamond.rarity = Rarity.RARE;

        chestplateDiamond = new ItemArmor("Diamond Chestplate", 42, 3, 3, 1);
        chestplateDiamond.setIconIndex(19);
        chestplateDiamond.rarity = Rarity.RARE;

        leggingsDiamond = new ItemArmor("Diamond Leggings", 43, 3, 3, 2);
        leggingsDiamond.setIconIndex(35);
        leggingsDiamond.rarity = Rarity.RARE;

        bootsDiamond = new ItemArmor("Diamond Boots", 44, 3, 3, 3);
        bootsDiamond.setIconIndex(51);
        bootsDiamond.rarity = Rarity.RARE;
        
        // other, tools?
        flintAndSteel = new ItemFlintAndSteel("Flint and Steel", 45);
        flintAndSteel.setIconIndex(5);

        bow = new ItemBow("Bow", 46);
        bow.setIconIndex(21);

        arrow = new Item("Arrow", 47);
        arrow.setIconIndex(37);

        // food and crops and stuff idk
        apple = new ItemFood("Apple", 48, 4);
        apple.setIconIndex(10);

        applePie = new ItemFood("Apple Pie", 49, 8);
        applePie.setIconIndex(11);

        bowlEmpty = new Item("Bowl", 50);
        bowlEmpty.setIconIndex(71);

        bowlSoup = new ItemSoup("Mushroom Stew", 51, 10);
        bowlSoup.setIconIndex(72);

        seeds = new ItemSeeds("Wheat Seeds", 52, Block.crops.blockID);
        seeds.setIconIndex(9);

        wheat = new Item("Wheat", 53);
        wheat.setIconIndex(25);

        bread = new ItemFood("Bread", 54, 5);
        bread.setIconIndex(41);

        sweetBread = new ItemFood("Sweet Bread", 55, 7);
        sweetBread.setIconIndex(57);

        porkRaw = new ItemFood("Raw Porkchop", 56, 3);
        porkRaw.setIconIndex(87);

        porkCooked = new ItemFood("Cooked Porkchop", 57, 8);
        porkCooked.setIconIndex(88);

        sugarBeet = new Item("Sugar Beet", 58);
        sugarBeet.setIconIndex(103);

        sugar = new Item("Sugar", 59);
        sugar.setIconIndex(104);

        // misc I guess
        coal = new Item("Coal", 60);
        coal.setIconIndex(7);

        ingotIron = new Item("Iron Ingot", 61);
        ingotIron.setIconIndex(23);

        ingotGold = new Item("Gold Ingot", 62);
        ingotGold.setIconIndex(39);

        diamond = new Item("Diamond", 63);
        diamond.setIconIndex(55);
        diamond.rarity = Rarity.RARE;

        stick = new Item("Stick", 64);
        stick.setIconIndex(53);

        silk = new Item("Silk", 65);
        silk.setIconIndex(8);

        feather = new Item("Feather", 66);
        feather.setIconIndex(24);

        gunpowder = new Item("Sulphur", 67);
        gunpowder.setIconIndex(40);

        flint = new Item("Flint", 68);
        flint.setIconIndex(6);

        painting = new ItemPainting("Painting", 69);
        painting.setIconIndex(26);

        // rupees
        rupeePlain = new Item("Rupee", 70);
        rupeePlain.setIconIndex(144);

        rupeeDark = new Item("Dark Rupee", 71);
        rupeeDark.setIconIndex(146);

     // Create the quiver item
        quiver = new ItemQuiver("Quiver", 72); // Assign a unique index for the item
        quiver.setIconIndex(54);
        	 }
}