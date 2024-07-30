package net.minecraft.game.item.enchant;

public class Enchant {
	
	private final EnchantType type;
	private int level;
	
	public Enchant(EnchantType type, int level) {
		this.type = type;
		this.level = level;
	}
	
	public EnchantType getType() {
		return type;
	}
	
	public int getLevel() {
		return level;
	}

}
