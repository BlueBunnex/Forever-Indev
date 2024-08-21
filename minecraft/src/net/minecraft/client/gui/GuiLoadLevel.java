package net.minecraft.client.gui;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import net.minecraft.client.PlayerLoader;
import net.minecraft.game.level.World;

public class GuiLoadLevel extends GuiScreen implements Runnable {

    private GuiScreen parent;
    private boolean finished = false;
    private boolean loaded = false;
    private String[] levels = new String[5];
    private String status = "";
    protected String title = "Load level";
    private boolean frozen = false;
    private File selectedFile;

    public GuiLoadLevel(GuiScreen parent) {
        this.parent = parent;
    }

    public final void updateScreen() {
        if (this.selectedFile != null) {
            if (!this.selectedFile.getName().endsWith(".mclevel")) {
                this.selectedFile = new File(this.selectedFile.getAbsolutePath() + ".mclevel");
            }

            this.openLevel(this.selectedFile);
            this.selectedFile = null;
            this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    public void run() {
        try {
            this.status = "Getting level list..";
            loadLocalLevels();
            this.loaded = true;
        } catch (Exception var2) {
            var2.printStackTrace();
            this.status = "Failed to load levels";
            this.finished = true;
        }
    }

    private void loadLocalLevels() {
        File savesDir = new File(this.mc.mcDataDir, "saves");
        for (int i = 0; i < 5; i++) {
            File folder = new File(savesDir, "World" + i);
            if (folder.exists() && folder.isDirectory()) {
                File file = new File(folder, "level.mclevel");
                if (file.exists()) {
                    levels[i] = readWorldName(file);
                } else {
                    levels[i] = "-";
                }
            } else {
                levels[i] = "-";
            }
        }
        setLevels(levels);
    }

    private String readWorldName(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "-";
    }

    protected void setLevels(String[] var1) {
        for (int i = 0; i < 5; i++) {
            GuiButtonText button = (GuiButtonText) this.controlList.get(i);
            button.enabled = !var1[i].equals("-");
            button.displayString = var1[i];
            button.visible = true;
        }
        this.controlList.get(5).visible = true;
    }

    public void initGui() {
        new Thread(this).start();
        for (int i = 0; i < 5; i++) {
            this.controlList.add(new GuiButtonText(i, this.width / 2 - 100, this.height / 6 + i * 24, "---"));
            this.controlList.get(i).visible = false;
        }
        this.controlList.add(new GuiButtonText(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
        this.controlList.add(new GuiButtonText(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
        this.controlList.get(5).visible = false;
    }

    protected final void actionPerformed(GuiButton button) {
        if (this.frozen || !button.enabled)
            return;
        if (this.loaded && button.id < 5) {
            this.openLevel(button.id);
        }
        if (this.finished || this.loaded && button.id == 5) {
            this.frozen = true;
            GuiLevelDialog var2 = new GuiLevelDialog(this);
            var2.setDaemon(true);
            var2.start();
        }
        if (this.finished || this.loaded && button.id == 6) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    protected FileDialog saveFileDialog() {
        return new FileDialog((Dialog) null, "Load level", 0);
    }

    protected void openLevel(int slot) {
        File savesDir = new File(this.mc.mcDataDir, "saves");
        File folder = new File(savesDir, "World" + slot);
        if (folder.exists() && folder.isDirectory()) {
            File file = new File(folder, "level.mclevel");
            if (file.exists()) {
                openLevel(file);
            }
        }
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();
    }

    public final void drawScreen(int mouseX, int mouseY) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        if (!this.loaded) {
            drawCenteredString(this.fontRenderer, this.status, this.width / 2, this.height / 2 - 4, 16777215);
        }
        super.drawScreen(mouseX, mouseY);
    }

    protected void openLevel(File file) {
        try (FileInputStream in = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(in)) {
            String worldName = (String) ois.readObject();
            World world = (new PlayerLoader(this.mc, this.mc.loadingScreen)).load(in);
            this.mc.setLevel(world);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static File a(GuiLoadLevel var0, File var1) {
        return var0.selectedFile = var1;
    }

    static boolean unknown(GuiLoadLevel var0, boolean var1) {
        return var0.frozen = false;
    }
}
