package net.minecraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.lwjgl.input.Keyboard;

public final class GameSettings {
    private static final String[] RENDER_DISTANCES = { "Far", "Normal", "Short", "Tiny" };
    private static final String[] DIFFICULTIES = { "Peaceful", "Easy", "Normal", "Hard" };

    private Minecraft mc;
    private File optionsFile;

    public int numberOfOptions = 10; // Updated number of options
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
    public boolean showExitConfirmation = true; // New setting for confirmation screen

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

    public KeyBinding[] keyBindings = new KeyBinding[]{keyBindForward, keyBindLeft, keyBindBack, keyBindRight, keyBindJump, keyBindDrop, keyBindInventory, keyBindChat, keyBindToggleFog, keyBindSave, keyBindLoad};

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
                this.music = !this.music;
                this.mc.sndManager.onSoundOptionsChanged();
                break;

            case 1:
                this.sound = !this.sound;
                this.mc.sndManager.onSoundOptionsChanged();
                break;

            case 2:
                this.invertMouse = !this.invertMouse;
                break;

            case 3:
                this.showFPS = !this.showFPS;
                break;

            case 4:
                this.renderDistance = (this.renderDistance + value) & 3;
                break;

            case 5:
                this.fancyGraphics = !this.fancyGraphics;
                break;

            case 6:
                this.anaglyph = !this.anaglyph;
                this.mc.renderEngine.refreshTextures();
                break;

            case 7:
                this.limitFramerate = !this.limitFramerate;
                break;

            case 8:
                this.difficulty = (this.difficulty + value) & 3;
                break;

            case 9: // New option ID for confirmation setting
                this.showExitConfirmation = !this.showExitConfirmation;
                break;
        }

        this.saveOptions();
    }

    public final String setOptionString(int optionID) {
        switch (optionID) {
            case 0: return "Music: " + (this.music ? "ON" : "OFF");
            case 1: return "Sound: " + (this.sound ? "ON" : "OFF");
            case 2: return "Invert mouse: " + (this.invertMouse ? "ON" : "OFF");
            case 3: return "Show debug: " + (this.showFPS ? "ON" : "OFF");
            case 4: return "Render distance: " + RENDER_DISTANCES[this.renderDistance];
            case 5: return "View bobbing: " + (this.fancyGraphics ? "ON" : "OFF");
            case 6: return "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF");
            case 7: return "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF");
            case 8: return "Difficulty: " + DIFFICULTIES[this.difficulty];
            case 9: return "Show exit confirmation: " + (this.showExitConfirmation ? "ON" : "OFF");
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
                    case "music":
                        this.music = parts[1].equals("true");
                        break;
                    case "sound":
                        this.sound = parts[1].equals("true");
                        break;
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
            writer.println("music:" + this.music);
            writer.println("sound:" + this.sound);
            writer.println("invertYMouse:" + this.invertMouse);
            writer.println("showFrameRate:" + this.showFPS);
            writer.println("viewDistance:" + this.renderDistance);
            writer.println("bobView:" + this.fancyGraphics);
            writer.println("anaglyph3d:" + this.anaglyph);
            writer.println("limitFramerate:" + this.limitFramerate);
            writer.println("difficulty:" + this.difficulty);
            writer.println("showExitConfirmation:" + this.showExitConfirmation);

            for (KeyBinding keyBinding : this.keyBindings) {
                writer.println("key_" + keyBinding.keyDescription + ":" + keyBinding.keyCode);
            }
        } catch (Exception e) {
            System.err.println("Failed to save options: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
