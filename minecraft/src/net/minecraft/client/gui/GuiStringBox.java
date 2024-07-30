package net.minecraft.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class GuiStringBox extends Gui {
	
	private int x, y;
	private int backgroundColor;
	private int backgroundWidth;
	private int backgroundHeight;
	
	private FontRenderer fontRend;
	
	private ArrayList<String> lines;
	private ArrayList<Integer> colors;
	
	/**
	 * Creates a GUI element that automatically wraps a box around lines. Each line can be a different color.
	 * @param fontRend
	 * @param x
	 * @param y
	 */
	public GuiStringBox(FontRenderer fontRend, int x, int y) {
		this(fontRend, x, y, -1442840576);
	}
	
	public GuiStringBox(FontRenderer fontRend, int x, int y, int backgroundColor) {
		
		this.x = x;
		this.y = y;
		this.backgroundColor  = backgroundColor;
		this.backgroundWidth  = 0;
		this.backgroundHeight = 0;
		
		this.fontRend = fontRend;
		
		lines  = new ArrayList<String>();
		colors = new ArrayList<Integer>();
	}
	
	public void addLine(String line, int color) {
		
		lines.add(line);
		colors.add(color);
		
		// update background dimensions
		int width = fontRend.getStringWidth(line);
		if (width > backgroundWidth)
			backgroundWidth = width;
		
		backgroundHeight += 9;
	}
	
	public void drawStringBox(Minecraft mc) {
		
		drawRect(
				x - 2,
				y - 2,
				x + 1 + backgroundWidth,
				y     + backgroundHeight,
				backgroundColor
			);
		
		for (int i=0; i<lines.size(); i++) {
			fontRend.drawString(lines.get(i), x, y + i * 9, colors.get(i));
		}
	}

}
