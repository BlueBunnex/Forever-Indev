package net.minecraft.client.gui;

import java.awt.FileDialog;
import java.io.File;

final class GuiLevelDialog extends Thread {
	private GuiLoadLevel screen;

	GuiLevelDialog(GuiLoadLevel guiLoadLevel1) {
		this.screen = guiLoadLevel1;
	}

	public final void run() {
		try {
			FileDialog fileDialog1 = this.screen.saveFileDialog();
			File file2;
			(file2 = new File(this.screen.mc.mcDataDir, "saves")).mkdir();
			String string5;
			if(!(string5 = file2.toString()).endsWith(File.separator)) {
				string5 = string5 + File.separator;
			}

			fileDialog1.setDirectory(string5);
			FilenameFilterLevel filenameFilterLevel6 = new FilenameFilterLevel(this);
			fileDialog1.setFilenameFilter(filenameFilterLevel6);
			fileDialog1.setLocationRelativeTo(this.screen.mc.mcCanvas);
			fileDialog1.setVisible(true);
			if(fileDialog1.getFile() != null) {
				if(!(string5 = fileDialog1.getDirectory()).endsWith(File.separator)) {
					string5 = string5 + File.separator;
				}

				GuiLoadLevel.a(this.screen, new File(string5 + fileDialog1.getFile()));
			}
		} finally {
			GuiLoadLevel.unknown(this.screen, false);
		}

	}
}