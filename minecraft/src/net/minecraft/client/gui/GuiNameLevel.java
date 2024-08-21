package net.minecraft.client.gui;

import org.lwjgl.input.Keyboard;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import net.minecraft.client.PlayerLoader;

public final class GuiNameLevel extends GuiScreen {
    
    private GuiScreen parent;
    private String title = "Enter level name:";
    private int slot;
    private String name;
    private int counter = 0;

    public GuiNameLevel(GuiScreen parent, String name, int slot) {
        this.parent = parent;
        this.slot = slot;
        this.name = name;
        
        if (this.name.equals("-"))
            this.name = "";
    }

    public final void initGui() {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);
        this.controlList.add(new GuiButtonText(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
        this.controlList.add(new GuiButtonText(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
        ((GuiButton) this.controlList.get(0)).enabled = this.name.trim().length() > 1;
    }

    public final void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public final void updateScreen() {
        ++this.counter;
    }

    protected final void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;

        if (button.id == 0 && this.name.trim().length() > 1) {
            this.name = this.name.trim();
            this.saveWorldWithFolder();
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }

        if (button.id == 1)
            this.mc.displayGuiScreen(this.parent);
    }

    private void saveWorldWithFolder() {
        try {
            File savesDir = new File(this.mc.mcDataDir, "saves");
            File folder = new File(savesDir, "World" + this.slot);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "level.mclevel");
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(this.name); // Write the world name first
                (new PlayerLoader(this.mc, this.mc.loadingScreen)).save(this.mc.theWorld, fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final void keyTyped(char character, int keycode) {
        // backspace
        if (keycode == 14 && this.name.length() > 0)
            this.name = this.name.substring(0, this.name.length() - 1);

        // type char
        if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(character) >= 0 && this.name.length() < 64)
            this.name = this.name + character;

        // update "Save" button's enabled state based on validity of world name
        this.controlList.get(0).enabled = this.name.trim().length() > 1;
    }

    public final void drawScreen(int mouseX, int mouseY) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, this.title, this.width / 2, 40, 16777215);
        int var4 = this.width / 2 - 100;
        int var5 = this.height / 2 - 10;
        drawRect(var4 - 1, var5 - 1, var4 + 200 + 1, var5 + 20 + 1, -6250336);
        drawRect(var4, var5, var4 + 200, var5 + 20, -16777216);
        drawString(this.fontRenderer, this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), var4 + 4, var5 + 6, 14737632);

        super.drawScreen(mouseX, mouseY);
    }
}
