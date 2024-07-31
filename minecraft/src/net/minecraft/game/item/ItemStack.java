package net.minecraft.game.item;

import java.util.ArrayList;
import java.util.Iterator;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.item.enchant.Enchant;
import net.minecraft.game.item.enchant.EnchantType;
import net.minecraft.game.level.block.Block;

public final class ItemStack {
	
	/*
	 * Note: Only non-stackable items (item.getItemStackLimit() == 1) should
	 * be able to be enchanted or have durability
	 */
	
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDamage;
	
	private ArrayList<Enchant> enchants;

	public ItemStack(Block block) {
		this(block, 1);
	}

	public ItemStack(Block block, int stackSize) {
		this(block.blockID, stackSize);
	}

	public ItemStack(Item item) {
		this(item, 1);
	}

	public ItemStack(Item item, int stackSize) {
		this(item.shiftedIndex, stackSize);
	}

	public ItemStack(int itemID) {
		this(itemID, 1);
	}

	public ItemStack(int itemID, int stackSize) {
		this.itemID = itemID;
		this.stackSize = stackSize;
		
		this.enchants = new ArrayList<Enchant>();
	}

	public ItemStack(int itemID, int stackSize, int itemDamage) {
		this.itemID = itemID;
		this.stackSize = stackSize;
		this.itemDamage = itemDamage;
		
		this.enchants = new ArrayList<Enchant>();
	}

	public ItemStack(NBTTagCompound nbt) {
		this.stackSize = 0;
		this.itemID = nbt.getShort("id");
		this.stackSize = nbt.getByte("Count");
		this.itemDamage = nbt.getShort("Damage");
	}
	
	public String getName() {
		return Item.itemsList[itemID].name;
	}
	
	/**
	 * [NOT YET IMPLEMENTED] Gets the enchant level of the specified enchant type, or 0 if not enchanted with that enchant.
	 * @param type the type of enchant to check for
	 */
	public int enchantLevelOf(EnchantType type) {
		// TODO implement
		return 0;
	}
	
	public Enchant addEnchant(EnchantType type, int level) {
		return this.addEnchant(new Enchant(type, level));
	}
	
	public Enchant addEnchant(Enchant enchant) {
		
		if (enchant.getLevel() > 0)
			enchants.add(enchant);
		
		return enchant;
	}
	
	public Iterable<Enchant> getEnchants() {
		
		return new Iterable<Enchant>() {

			public Iterator<Enchant> iterator() {
				return enchants.iterator();
			}
			
		};
	}
	
	/**
	 * Splits off the amount from this stack, and returns a new stack with that amount, or null if amount is <= 0.
	 * Note: You need to check if this stack's size is == 0 and, if so, nullify it. Size 0 stacks should not exist.
	 * @param amount for the new stack to be
	 * @return the new stack
	 */
	public final ItemStack splitStack(int amount) {
		
		if (amount <= 0)
			return null;
		
		this.stackSize -= amount;
		return new ItemStack(this.itemID, amount, this.itemDamage);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("id", (short) this.itemID);
		nbt.setByte("Count", (byte) this.stackSize);
		nbt.setShort("Damage", (short) this.itemDamage);
		
		return nbt;
	}

	public final int isItemStackDamageable() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	/**
	 * 
	 * @param damage negative values deal damage, positive repair
	 */
	public final void damageItem(int damage) {
		this.itemDamage += damage;
		
		if (this.itemDamage > this.isItemStackDamageable()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
}
