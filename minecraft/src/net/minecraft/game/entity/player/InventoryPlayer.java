package net.minecraft.game.entity.player;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class InventoryPlayer implements IInventory {
    
    public ItemStack[] mainInventory = new ItemStack[38]; // Increased from 36 to 38
    public ItemStack[] armorInventory = new ItemStack[4];
    public int currentItem = 0;
    private EntityPlayer player;

    public InventoryPlayer(EntityPlayer var1) {
        this.player = var1;
    }

    public final ItemStack getCurrentItem() {
        return this.mainInventory[this.currentItem];
    }

    public int getInventorySlotContainItem(int var1) {
        for(int var2 = 0; var2 < this.mainInventory.length; ++var2) {
            if(this.mainInventory[var2] != null && this.mainInventory[var2].itemID == var1) {
                return var2;
            }
        }
        return -1;
    }

    private int getAvailableSlot() {
        for (int i = 0; i < this.mainInventory.length; i++) {
            if (this.mainInventory[i] == null)
                return i;
        }
        return -1;
    }

    public final void getFirstEmptyStack(int var1) {
        var1 = this.getInventorySlotContainItem(var1);
        if(var1 >= 0 && var1 < 9) {
            this.currentItem = var1;
        }
    }

    public final boolean consumeInventoryItem(int var1) {
        var1 = this.getInventorySlotContainItem(var1);
        if(var1 < 0) {
            return false;
        } else {
            if(--this.mainInventory[var1].stackSize <= 0) {
                this.mainInventory[var1] = null;
            }
            return true;
        }
    }

    public final boolean storePartialItemStack(ItemStack itemstack) {
        if (itemstack.getItem().getItemStackLimit() == 1) {
            int i = this.getAvailableSlot();
            if(i >= 0) {
                this.mainInventory[i] = itemstack;
                this.mainInventory[i].animationsToGo = 5;
                return true;
            } else {
                return false;
            }
        } else {
            int var4 = itemstack.stackSize;
            int var3 = itemstack.itemID;
            int var6 = var3;
            InventoryPlayer var5 = this;
            int var7 = 0;

            int var10001;
            ItemStack var8;
            while(true) {
                if(var7 >= var5.mainInventory.length) {
                    var10001 = -1;
                    break;
                }
                if(var5.mainInventory[var7] != null && var5.mainInventory[var7].itemID == var6) {
                    var8 = var5.mainInventory[var7];
                    if(var5.mainInventory[var7].stackSize < var8.getItem().getItemStackLimit() && var5.mainInventory[var7].stackSize < 64) {
                        var10001 = var7;
                        break;
                    }
                }
                ++var7;
            }

            int var9 = var10001;
            if(var9 < 0) {
                var9 = this.getAvailableSlot();
            }

            if(var9 < 0) {
                var10001 = var4;
            } else {
                if(this.mainInventory[var9] == null) {
                    this.mainInventory[var9] = new ItemStack(var3, 0);
                }

                var3 = var4;
                var8 = this.mainInventory[var9];
                if(var4 > var8.getItem().getItemStackLimit() - this.mainInventory[var9].stackSize) {
                    var8 = this.mainInventory[var9];
                    var3 = var8.getItem().getItemStackLimit() - this.mainInventory[var9].stackSize;
                }

                if(var3 > 64 - this.mainInventory[var9].stackSize) {
                    var3 = 64 - this.mainInventory[var9].stackSize;
                }

                if(var3 == 0) {
                    var10001 = var4;
                } else {
                    var4 -= var3;
                    this.mainInventory[var9].stackSize += var3;
                    this.mainInventory[var9].animationsToGo = 5;
                    var10001 = var4;
                }
            }

            itemstack.stackSize = var10001;
            if(itemstack.stackSize == 0) {
                return true;
            }
        }
        return false;
    }

    public final ItemStack decrStackSize(int var1, int var2) {
        ItemStack[] var3 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var3 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }

        if(var3[var1] != null) {
            ItemStack var4;
            if(var3[var1].stackSize <= var2) {
                var4 = var3[var1];
                var3[var1] = null;
                return var4;
            } else {
                var4 = var3[var1].splitStack(var2);
                if(var3[var1].stackSize == 0) {
                    var3[var1] = null;
                }
                return var4;
            }
        } else {
            return null;
        }
    }

    public final void setInventorySlotContents(int var1, ItemStack var2) {
        ItemStack[] var3 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var3 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }
        var3[var1] = var2;
    }

    public final int getSizeInventory() {
        return this.mainInventory.length + this.armorInventory.length;
    }

    public final ItemStack getStackInSlot(int var1) {
        ItemStack[] var2 = this.mainInventory;
        if(var1 >= this.mainInventory.length) {
            var2 = this.armorInventory;
            var1 -= this.mainInventory.length;
        }
        return var2[var1];
    }

    public final String getInvName() {
        return "Inventory";
    }

    public final int getInventoryStackLimit() {
        return 64;
    }

    public final int getPlayerArmorValue(int armorType) {
        if (this.armorInventory[armorType] == null || !(this.armorInventory[armorType].getItem() instanceof ItemArmor))
            return 0;

        int maxDam = this.armorInventory[armorType].getItem().getMaxDamage();
        int damage = this.armorInventory[armorType].itemDamage;

        return (int) Math.ceil((maxDam - damage) * 4.0 / maxDam);
    }

    public final int getPlayerArmorValue() {
        int var1 = 0;
        int var2 = 0;
        int var3 = 0;

        for(int i = 0; i < this.armorInventory.length; i++) {
            if(this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
                int var5 = this.armorInventory[i].isItemStackDamageable();
                int var6 = this.armorInventory[i].itemDamage;
                var6 = var5 - var6;
                var2 += var6;
                var3 += var5;
                var5 = ((ItemArmor) this.armorInventory[i].getItem()).damageReduceAmount;
                var1 += var5;
            }
        }

        if(var3 == 0) {
            return 0;
        } else {
            return (var1 - 1) * var2 / var3 + 1;
        }
    }
}
