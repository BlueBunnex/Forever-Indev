package net.minecraft.client.gui;

import java.io.File;
import java.io.FilenameFilter;

final class FilenameFilterLevel implements FilenameFilter {
	FilenameFilterLevel(GuiLevelDialog guiLevelDialog1) {
	}

	public final boolean accept(File file1, String worldName) {
		return worldName.toLowerCase().endsWith(".mclevel");
	}
}