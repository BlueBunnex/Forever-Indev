package net.minecraft.game.item;

public enum Rarity {
	
	COMMON(16777215),
	RARE(-16711681),
	LEGENDARY(-22016);
	
	public final int color;
	
	Rarity(int color) {
		this.color = color;
	}

}
