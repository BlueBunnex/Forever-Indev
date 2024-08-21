package net.minecraft.game.item.enchant;

public enum EnchantType {
    
    fiery("Fiery"),
    detonation("Detonation"),
    quickshot("Quickshot"); // Changed to Quickshot for coherence

    public final String name;

    EnchantType(String name) {
        this.name = name;
    }
}
