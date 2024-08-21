package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;

public final class GuiSoundSettings extends GuiScreen {
    
    private GuiScreen parentScreen;
    private String screenTitle = "Sound Settings";
    private GameSettings gameSettings;

    public GuiSoundSettings(GuiScreen parentScreen, GameSettings gameSettings) {
        this.parentScreen = parentScreen;
        this.gameSettings = gameSettings;
    }

    @Override
    public final void initGui() {
        // Clear any existing controls
        this.controlList.clear();
        
        // Add buttons for Music and Sound toggles
        this.controlList.add(new GuiButtonText(1, this.width / 2 - 100, this.height / 6 + 24, 200, 20, "Music: " + (gameSettings.music ? "ON" : "OFF")));
        this.controlList.add(new GuiButtonText(2, this.width / 2 - 100, this.height / 6 + 48, 200, 20, "Sound: " + (gameSettings.sound ? "ON" : "OFF")));
        this.controlList.add(new GuiButtonText(0, this.width / 2 - 100, this.height / 6 + 72, 200, 20, "Back"));
    }

    @Override
    protected final void actionPerformed(GuiButton button) {
        if (!button.enabled)
            return;
        
        switch (button.id) {
            case 0: // Back button
            	this.mc.displayGuiScreen(this.parentScreen); // Go back to the previous screen
                break;
            case 1: // Music toggle button
                // Toggle Music setting
                gameSettings.music = !gameSettings.music;
                ((GuiButtonText) button).displayString = "Music: " + (gameSettings.music ? "ON" : "OFF");
                gameSettings.saveOptions(); // Save the changed setting
                break;
            case 2: // Sound toggle button
                // Toggle Sound setting
                gameSettings.sound = !gameSettings.sound;
                ((GuiButtonText) button).displayString = "Sound: " + (gameSettings.sound ? "ON" : "OFF");
                gameSettings.saveOptions(); // Save the changed setting
                break;
            default:
                break;
        }
    }

	public final void drawScreen(int mouseX, int mouseY) {
		this.drawDefaultBackground();
		drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		
		super.drawScreen(mouseX, mouseY);
	}
}