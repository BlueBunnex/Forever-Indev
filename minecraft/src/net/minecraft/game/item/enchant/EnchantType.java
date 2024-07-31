package net.minecraft.game.item.enchant;

public enum EnchantType {
	
	// Enchants are just enums since they don't have any internal
	// logic (basically just flags for other functions to use)
	
	fiery("Fiery");
	
	public final String name;

	EnchantType(String name) {
		this.name = name;
	}
	
}
