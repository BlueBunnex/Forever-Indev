package net.minecraft.client.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;

public final class GuiControls extends GuiScreen {

    private GuiScreen parentScreen;
    private String screenTitle = "Controls";
    private GameSettings options;
    private int buttonId = -1;

    public GuiControls(GuiScreen parentScreen, GameSettings options) {
        this.parentScreen = parentScreen;
        this.options = options;
    }

    @Override
    public final void initGui() {
        this.controlList.clear();  // Clear existing controls to avoid duplicates

        // Add key binding buttons
        for (int var1 = 0; var1 < this.options.keyBindings.length; ++var1) {
            this.controlList.add(new GuiButtonText(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), 150, 20, this.options.setKeyBindingString(var1)));
        }

        // Add Done button
        this.controlList.add(new GuiButtonText(200, this.width / 2 - 100, this.height / 6 + 168, 200, 20, "Done"));
    }

    @Override
    protected final void actionPerformed(GuiButton button) {
        if (button.id == 200) {
            // Done button was pressed
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }

        GuiButtonText textButton = (GuiButtonText) button;

        // Update all key binding buttons with their current binding
        for (int i = 0; i < this.options.keyBindings.length; i++) {
            ((GuiButtonText) this.controlList.get(i)).displayString = this.options.setKeyBindingString(i);
        }

        // Highlight the clicked button
        this.buttonId = textButton.id;
        textButton.displayString = "> " + this.options.setKeyBindingString(textButton.id) + " <";
    }

    @Override
    protected final void keyTyped(char character, int keycode) {
        if (this.buttonId >= 0) {
            // Set the key binding for the button
            this.options.setKeyBinding(this.buttonId, keycode);
            ((GuiButtonText) this.controlList.get(this.buttonId)).displayString = this.options.setKeyBindingString(this.buttonId);
            this.buttonId = -1;
        } else {
            super.keyTyped(character, keycode);
        }
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY) {
        // Draw the default background
        this.drawDefaultBackground();
        
        // Draw the title of the screen
        drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);

        // Draw all the buttons and other controls
        super.drawScreen(mouseX, mouseY);
    }
}
