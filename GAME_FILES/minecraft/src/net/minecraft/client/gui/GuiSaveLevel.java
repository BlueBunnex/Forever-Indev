package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import net.minecraft.client.PlayerLoader;

public final class GuiSaveLevel extends GuiLoadLevel {
	
	public GuiSaveLevel(GuiScreen var1) {
		super(var1);
		this.title = "Save level";
	}

	protected final FileDialog saveFileDialog() {
		return new FileDialog((Dialog)null, "Save level", 1);
	}

	public final void initGui() {
		super.initGui();
		((GuiButtonText) this.controlList.get(5)).displayString = "Save file...";
	}

	protected final void setLevels(String[] var1) {
		
		for(int i = 0; i < 5; i++) {
			
			GuiButtonText button = (GuiButtonText) this.controlList.get(i);
			
			button.displayString = var1[i];
			button.visible = true;
		}

		this.controlList.get(5).visible = true;
	}

	protected final void openLevel(File var1) {
		try {
			FileOutputStream var3 = new FileOutputStream(var1);
			(new PlayerLoader(this.mc, this.mc.loadingScreen)).save(this.mc.theWorld, var3);
			var3.close();
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	protected final void openLevel(int i) {
		this.mc.displayGuiScreen(new GuiNameLevel(this, ((GuiButtonText) this.controlList.get(i)).displayString, i));
	}
}
