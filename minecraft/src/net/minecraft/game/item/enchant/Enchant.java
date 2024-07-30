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
	
	public String toString() {
		
		String levelString;
		
		// bad code I know I know
		switch (level) {
			case 1: levelString = "I"; break;
			case 2: levelString = "II"; break;
			case 3: levelString = "III"; break;
			case 4: levelString = "IV"; break;
			case 5: levelString = "V"; break;
			case 6: levelString = "VI"; break;
			case 7: levelString = "VII"; break;
			case 8: levelString = "VIII"; break;
			case 9: levelString = "IX"; break;
			case 10: levelString = "X"; break;
			default: levelString = "" + level; break;
		}
		
		return type.name + " " + levelString;
	}

}
