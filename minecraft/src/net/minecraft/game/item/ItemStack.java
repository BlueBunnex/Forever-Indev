package net.minecraft.game.item;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.item.enchant.Enchant;
import net.minecraft.game.item.enchant.EnchantType;
import net.minecraft.game.level.block.Block;

public final class ItemStack implements Serializable {
    private static final long serialVersionUID = 1L; // Ensure version compatibility

    public int stackSize;
    public int animationsToGo;
    public int itemID;
    public int itemDamage;

    private transient ArrayList<Enchant> enchants; // Mark as transient if non-serializable

    // New field for a custom icon index (-1 means no custom icon is set)
    private int customIconIndex = -1;

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
        this.enchants = new ArrayList<Enchant>(); // Initialize to avoid null references
        // Additional deserialization for enchants could be added here
    }

    public String getName() {
        return Item.itemsList[itemID].name;
    }

    /**
     * Gets the enchant level of the specified enchant type, or 0 if not enchanted with that enchant.
     * @param type the type of enchant to check for
     */
    public int enchantLevelOf(EnchantType type) {
        for (Enchant enchant : enchants) {
            if (enchant.getType() == type) {
                return enchant.getLevel();
            }
        }
        return 0;
    }

    public Enchant addEnchant(EnchantType type, int level) {
        return this.addEnchant(new Enchant(type, level));
    }

    public Enchant addEnchant(Enchant enchant) {
        if (enchant.getLevel() > 0) {
            enchants.add(enchant);
        }
        return enchant;
    }

    public Collection<Enchant> getEnchants() {
        return enchants;
    }

    /**
     * Splits off the amount from this stack, and returns a new stack with that amount, or null if amount is <= 0.
     * Note: You need to check if this stack's size is == 0 and, if so, nullify it. Size 0 stacks should not exist.
     * @param amount for the new stack to be
     * @return the new stack
     */
    public final ItemStack splitStack(int amount) {
        if (amount <= 0) {
            return null;
        }
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

        NBTTagList enchantList = new NBTTagList();
        for (Enchant enchant : enchants) {
            NBTTagCompound enchantTag = new NBTTagCompound();
            enchantTag.setByte("id", (byte) enchant.getType().ordinal());
            enchantTag.setByte("lvl", (byte) enchant.getLevel());
            enchantList.setTag(enchantTag);
        }
        nbt.setTag("Enchantments", enchantList);

        return nbt;
    }

    public final int isItemStackDamageable() {
        return Item.itemsList[this.itemID].getMaxDamage();
    }

    /**
     * @param damage negative values deal damage, positive repair
     */
    public final void damageItem(int damage) {
        this.itemDamage += damage;

        if (this.itemDamage > this.isItemStackDamageable()) {
            --this.stackSize;
            if (this.stackSize < 0) {
                this.stackSize = 0;
            }
            this.itemDamage = 0;
        }
    }

    // Custom serialization methods
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Write default serialization
        out.writeInt(customIconIndex); // Serialize the custom icon index
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read default serialization
        customIconIndex = in.readInt(); // Deserialize the custom icon index
    }

    // New methods for handling custom icon index
    public void setCustomIconIndex(int index) {
        this.customIconIndex = index;
    }

    public int getCustomIconIndex() {
        return this.customIconIndex;
    }

    public boolean hasCustomIcon() {
        return this.customIconIndex != -1;
    }
}
