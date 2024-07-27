package net.minecraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.lwjgl.input.Keyboard;

public final class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	private static final String[] DIFFICULTIES = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
	public boolean music = true;
	public boolean sound = true;
	public boolean invertMouse = false;
	public boolean showFPS = false;
	public int renderDistance = 0;
	public boolean fancyGraphics = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public KeyBinding keyBindForward = new KeyBinding("Forward", Keyboard.KEY_W);
	public KeyBinding keyBindLeft = new KeyBinding("Left", Keyboard.KEY_A);
	public KeyBinding keyBindBack = new KeyBinding("Back", Keyboard.KEY_S);
	public KeyBinding keyBindRight = new KeyBinding("Right", Keyboard.KEY_D);
	public KeyBinding keyBindJump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	public KeyBinding keyBindInventory = new KeyBinding("Inventory", Keyboard.KEY_I);
	public KeyBinding keyBindDrop = new KeyBinding("Drop", Keyboard.KEY_Q);
	private KeyBinding keyBindChat = new KeyBinding("Chat", Keyboard.KEY_T);
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", Keyboard.KEY_F);
	public KeyBinding keyBindSave = new KeyBinding("Save location", Keyboard.KEY_RETURN);
	public KeyBinding keyBindLoad = new KeyBinding("Load location", Keyboard.KEY_R);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.keyBindSave, this.keyBindLoad};
	private Minecraft mc;
	private File optionsFile;
	public int numberOfOptions = 9;
	public int difficulty = 2;
	public boolean thirdPersonView = false;

	public GameSettings(Minecraft minecraft, File file) {
		this.mc = minecraft;
		this.optionsFile = new File(file, "options.txt");
		this.loadOptions();
	}

	public final String setKeyBindingString(int keyBindingNumber) {
		return this.keyBindings[keyBindingNumber].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[keyBindingNumber].keyCode);
	}

	public final void setKeyBinding(int keyBindingNumber, int keyCode) {
		this.keyBindings[keyBindingNumber].keyCode = keyCode;
		this.saveOptions();
	}

	public final void setOptionValue(int optionMenuValue, int optionSubValue) {
		if(optionMenuValue == 0) {
			this.music = !this.music;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(optionMenuValue == 1) {
			this.sound = !this.sound;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(optionMenuValue == 2) {
			this.invertMouse = !this.invertMouse;
		}

		if(optionMenuValue == 3) {
			this.showFPS = !this.showFPS;
		}

		if(optionMenuValue == 4) {
			this.renderDistance = this.renderDistance + optionSubValue & 3;
		}

		if(optionMenuValue == 5) {
			this.fancyGraphics = !this.fancyGraphics;
		}

		if(optionMenuValue == 6) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(optionMenuValue == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		if(optionMenuValue == 8) {
			this.difficulty = this.difficulty + optionSubValue & 3;
		}

		this.saveOptions();
	}

	public final String setOptionString(int optionMenuValue) {
		return optionMenuValue == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (optionMenuValue == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (optionMenuValue == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (optionMenuValue == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (optionMenuValue == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (optionMenuValue == 5 ? "View bobbing: " + (this.fancyGraphics ? "ON" : "OFF") : (optionMenuValue == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (optionMenuValue == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (optionMenuValue == 8 ? "Difficulty: " + DIFFICULTIES[this.difficulty] : ""))))))));
	}

	private void loadOptions() {
		try {
			if(this.optionsFile.exists()) {
				BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));

				String string2;
				while((string2 = bufferedReader1.readLine()) != null) {
					String[] string5;
					if((string5 = string2.split(":"))[0].equals("music")) {
						this.music = string5[1].equals("true");
					}

					if(string5[0].equals("sound")) {
						this.sound = string5[1].equals("true");
					}

					if(string5[0].equals("invertYMouse")) {
						this.invertMouse = string5[1].equals("true");
					}

					if(string5[0].equals("showFrameRate")) {
						this.showFPS = string5[1].equals("true");
					}

					if(string5[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(string5[1]);
					}

					if(string5[0].equals("bobView")) {
						this.fancyGraphics = string5[1].equals("true");
					}

					if(string5[0].equals("anaglyph3d")) {
						this.anaglyph = string5[1].equals("true");
					}

					if(string5[0].equals("limitFramerate")) {
						this.limitFramerate = string5[1].equals("true");
					}

					if(string5[0].equals("difficulty")) {
						this.difficulty = Integer.parseInt(string5[1]);
					}

					for(int i3 = 0; i3 < this.keyBindings.length; ++i3) {
						if(string5[0].equals("key_" + this.keyBindings[i3].keyDescription)) {
							this.keyBindings[i3].keyCode = Integer.parseInt(string5[1]);
						}
					}
				}

				bufferedReader1.close();
			}
		} catch (Exception exception4) {
			System.out.println("Failed to load options");
			exception4.printStackTrace();
		}
	}

	public final void saveOptions() {
		try {
			PrintWriter printWriter1;
			(printWriter1 = new PrintWriter(new FileWriter(this.optionsFile))).println("music:" + this.music);
			printWriter1.println("sound:" + this.sound);
			printWriter1.println("invertYMouse:" + this.invertMouse);
			printWriter1.println("showFrameRate:" + this.showFPS);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("bobView:" + this.fancyGraphics);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("limitFramerate:" + this.limitFramerate);
			printWriter1.println("difficulty:" + this.difficulty);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}
	}
}