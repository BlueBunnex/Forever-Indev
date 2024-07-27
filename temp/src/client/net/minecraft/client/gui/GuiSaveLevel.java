package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.client.PlayerLoader;

public final class GuiSaveLevel extends GuiLoadLevel {
	public GuiSaveLevel(GuiScreen guiScreen1) {
		super(guiScreen1);
		this.title = "Save level";
	}

	protected final FileDialog saveFileDialog() {
		return new FileDialog((Dialog)null, "Save level", 1);
	}

	public final void initGui() {
		super.initGui();
		((GuiButton)this.controlList.get(5)).displayString = "Save file...";
	}

	protected final void setLevels(String[] string1) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((GuiButton)this.controlList.get(i2)).displayString = string1[i2];
			((GuiButton)this.controlList.get(i2)).visible = true;
		}

		((GuiButton)this.controlList.get(5)).visible = true;
	}

	protected final void openLevel(File file1) {
		try {
			FileOutputStream fileOutputStream3 = new FileOutputStream(file1);
			(new PlayerLoader(this.mc, this.mc.loadingScreen)).save(this.mc.theWorld, fileOutputStream3);
			fileOutputStream3.close();
		} catch (IOException iOException2) {
			iOException2.printStackTrace();
		}
	}

	protected final void openLevel(int i1) {
		this.mc.displayGuiScreen(new GuiNameLevel(this, ((GuiButton)this.controlList.get(i1)).displayString, i1));
	}
}