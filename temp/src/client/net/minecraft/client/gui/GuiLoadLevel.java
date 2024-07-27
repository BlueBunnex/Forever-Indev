package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;

public class GuiLoadLevel extends GuiScreen implements Runnable {
	private GuiScreen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";
	private boolean frozen = false;
	private File selectedFile;

	public GuiLoadLevel(GuiScreen guiScreen1) {
		this.parent = guiScreen1;
	}

	public final void updateScreen() {
		if(this.selectedFile != null) {
			if(!this.selectedFile.getName().endsWith(".mclevel")) {
				this.selectedFile = new File(this.selectedFile.getAbsolutePath() + ".mclevel");
			}

			this.openLevel(this.selectedFile);
			this.selectedFile = null;
			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}

	public void run() {
		try {
			this.status = "Getting level list..";
			URL uRL1 = new URL("http://" + this.mc.minecraftUri + "/listmaps.jsp?user=" + this.mc.session.username);
			BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(uRL1.openConnection().getInputStream()));
			this.levels = bufferedReader3.readLine().split(";");
			if(this.levels.length >= 5) {
				this.setLevels(this.levels);
				this.loaded = true;
				return;
			}

			this.status = this.levels[0];
			this.finished = true;
		} catch (Exception exception2) {
			exception2.printStackTrace();
			this.status = "Failed to load levels";
			this.finished = true;
		}

	}

	protected void setLevels(String[] string1) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((GuiButton)this.controlList.get(i2)).enabled = !string1[i2].equals("-");
			((GuiButton)this.controlList.get(i2)).displayString = string1[i2];
			((GuiButton)this.controlList.get(i2)).visible = true;
		}

		((GuiButton)this.controlList.get(5)).visible = true;
	}

	public void initGui() {
		(new Thread(this)).start();

		for(int i1 = 0; i1 < 5; ++i1) {
			this.controlList.add(new GuiButton(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, "---"));
			((GuiButton)this.controlList.get(i1)).visible = false;
		}

		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
		((GuiButton)this.controlList.get(5)).visible = false;
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(!this.frozen) {
			if(guiButton1.enabled) {
				if(this.loaded && guiButton1.id < 5) {
					this.openLevel(guiButton1.id);
				}

				if(this.finished || this.loaded && guiButton1.id == 5) {
					this.frozen = true;
					GuiLevelDialog guiLevelDialog2;
					(guiLevelDialog2 = new GuiLevelDialog(this)).setDaemon(true);
					guiLevelDialog2.start();
				}

				if(this.finished || this.loaded && guiButton1.id == 6) {
					this.mc.displayGuiScreen(this.parent);
				}

			}
		}
	}

	protected FileDialog saveFileDialog() {
		return new FileDialog((Dialog)null, "Load level", 0);
	}

	protected void openLevel(int i1) {
		this.mc.displayGuiScreen((GuiScreen)null);
		this.mc.setIngameFocus();
	}

	public final void drawScreen(int xSize_lo, int ySize_lo, float f3) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
		if(!this.loaded) {
			drawCenteredString(this.fontRenderer, this.status, this.width / 2, this.height / 2 - 4, 0xFFFFFF);
		}

		super.drawScreen(xSize_lo, ySize_lo, f3);
	}

	protected void openLevel(File file1) {
		try {
			FileInputStream fileInputStream4 = new FileInputStream(file1);
			World world2 = (new PlayerLoader(this.mc, this.mc.loadingScreen)).load(fileInputStream4);
			fileInputStream4.close();
			this.mc.setLevel(world2);
		} catch (IOException iOException3) {
			iOException3.printStackTrace();
		}
	}

	static File a(GuiLoadLevel guiLoadLevel0, File file1) {
		return guiLoadLevel0.selectedFile = file1;
	}

	static boolean unknown(GuiLoadLevel guiLoadLevel0, boolean z1) {
		return guiLoadLevel0.frozen = false;
	}
}