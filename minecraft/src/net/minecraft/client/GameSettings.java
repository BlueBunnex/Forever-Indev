package net.minecraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiSoundSettings;

public final class GameSettings {
    private static final String[] RENDER_DISTANCES = { "Far", "Normal", "Short", "Tiny" };
    private static final String[] DIFFICULTIES = { "Peaceful", "Easy", "Normal", "Hard" };

    private Minecraft mc;
    private File optionsFile;

    public int numberOfOptions = 9;
    public int difficulty = 2;
    public boolean thirdPersonView = false;
    public boolean music = true;
    public boolean sound = true;
    public boolean invertMouse = false;
    public boolean showFPS = false;
    public int renderDistance = 0;
    public boolean fancyGraphics = true;
    public boolean anaglyph = false;
    public boolean limitFramerate = false;
    public boolean showExitConfirmation = true;
    public float fovSetting = 70.0F; // Default FOV
    
    public KeyBinding keyBindForward = new KeyBinding("Forward", 17);
    public KeyBinding keyBindLeft = new KeyBinding("Left", 30);
    public KeyBinding keyBindBack = new KeyBinding("Back", 31);
    public KeyBinding keyBindRight = new KeyBinding("Right", 32);
    public KeyBinding keyBindJump = new KeyBinding("Jump", 57);
    public KeyBinding keyBindInventory = new KeyBinding("Inventory", 18);
    public KeyBinding keyBindDrop = new KeyBinding("Drop", 16);
    public KeyBinding keyBindChat = new KeyBinding("Chat", 20);
    public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", 33);
    public KeyBinding keyBindSave = new KeyBinding("Save location", 28);
    public KeyBinding keyBindLoad = new KeyBinding("Load location", 19);
    public KeyBinding keyBindSprint = new KeyBinding("Sprint", 29); // Left Control key

    public KeyBinding[] keyBindings = new KeyBinding[]{
        keyBindForward, keyBindLeft, keyBindBack, keyBindRight, keyBindJump, keyBindDrop,
        keyBindInventory, keyBindChat, keyBindToggleFog, keyBindSave, keyBindLoad, keyBindSprint
    };

    public GameSettings(Minecraft mc, File fileRoot) {
        this.mc = mc;
        this.optionsFile = new File(fileRoot, "options.txt");
        this.loadOptions();
    }

    public final String setKeyBindingString(int keyBindID) {
        return this.keyBindings[keyBindID].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[keyBindID].keyCode);
    }

    public final void setKeyBinding(int keyBindID, int keyCode) {
        this.keyBindings[keyBindID].keyCode = keyCode;
        this.saveOptions();
    }

    public final void setOptionValue(int optionID, int value) {
        switch (optionID) {
            case 0:
                this.openSoundSettings(); // Open sound settings GUI
                break;

            case 1:
                this.invertMouse = !this.invertMouse;
                break;

            case 2:
                this.showFPS = !this.showFPS;
                break;

            case 3:
                this.renderDistance = (this.renderDistance + value) & 3;
                break;

            case 4:
                this.fancyGraphics = !this.fancyGraphics;
                break;

            case 5:
                this.anaglyph = !this.anaglyph;
                this.mc.renderEngine.refreshTextures();
                break;

            case 6:
                this.limitFramerate = !this.limitFramerate;
                break;

            case 7:
                this.difficulty = (this.difficulty + value) & 3;
                break;

            case 8:
                this.showExitConfirmation = !this.showExitConfirmation;
                break;
        }

        this.saveOptions();
    }

    private void openSoundSettings() {
        this.mc.displayGuiScreen(new GuiSoundSettings(this.mc.currentScreen, this));
    }

    public final String setOptionString(int optionID) {
        switch (optionID) {
            case 0: return "Sound settings...";
            case 1: return "Invert mouse: " + (this.invertMouse ? "ON" : "OFF");
            case 2: return "Show debug: " + (this.showFPS ? "ON" : "OFF");
            case 3: return "Render distance: " + RENDER_DISTANCES[this.renderDistance];
            case 4: return "View bobbing: " + (this.fancyGraphics ? "ON" : "OFF");
            case 5: return "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF");
            case 6: return "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF");
            case 7: return "Difficulty: " + DIFFICULTIES[this.difficulty];
            case 8: return "Show exit confirmation: " + (this.showExitConfirmation ? "ON" : "OFF");
            default: return "";
        }
    }

    private void loadOptions() {
        if (this.optionsFile == null || !this.optionsFile.exists()) {
            System.err.println("Options file is missing or not initialized.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(this.optionsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;

                switch (parts[0]) {
                    case "invertYMouse":
                        this.invertMouse = parts[1].equals("true");
                        break;
                    case "showFrameRate":
                        this.showFPS = parts[1].equals("true");
                        break;
                    case "viewDistance":
                        this.renderDistance = Integer.parseInt(parts[1]);
                        break;
                    case "bobView":
                        this.fancyGraphics = parts[1].equals("true");
                        break;
                    case "anaglyph3d":
                        this.anaglyph = parts[1].equals("true");
                        break;
                    case "limitFramerate":
                        this.limitFramerate = parts[1].equals("true");
                        break;
                    case "difficulty":
                        this.difficulty = Integer.parseInt(parts[1]);
                        break;
                    case "showExitConfirmation":
                        this.showExitConfirmation = parts[1].equals("true");
                        break;
                    case "music":
                        this.music = parts[1].equals("true");
                        if (!this.music) {
                            this.mc.sndManager.stopBackgroundMusic(); // Stop background music if music is disabled
                        }
                        break;
                    case "sound":
                        this.sound = parts[1].equals("true");
                        break;
                    default:
                        for (KeyBinding keyBinding : this.keyBindings) {
                            if (parts[0].equals("key_" + keyBinding.keyDescription)) {
                                keyBinding.keyCode = Integer.parseInt(parts[1]);
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load options: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public final void saveOptions() {
        if (this.optionsFile == null) {
            System.err.println("Options file is not initialized.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(this.optionsFile))) {
            writer.println("invertYMouse:" + this.invertMouse);
            writer.println("showFrameRate:" + this.showFPS);
            writer.println("viewDistance:" + this.renderDistance);
            writer.println("bobView:" + this.fancyGraphics);
            writer.println("anaglyph3d:" + this.anaglyph);
            writer.println("limitFramerate:" + this.limitFramerate);
            writer.println("difficulty:" + this.difficulty);
            writer.println("showExitConfirmation:" + this.showExitConfirmation);
            writer.println("music:" + this.music);
            writer.println("sound:" + this.sound);

            for (KeyBinding keyBinding : this.keyBindings) {
                writer.println("key_" + keyBinding.keyDescription + ":" + keyBinding.keyCode);
            }
        } catch (Exception e) {
            System.err.println("Failed to save options: " + e.getMessage());
            e.printStackTrace();
        }
    }
}	
