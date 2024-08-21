package net.minecraft.client.gui;

public final class GuiNewLevel extends GuiScreen {
    
    private GuiScreen prevGui;
    String[] worldType = new String[]{"Inland", "Island", "Floating", "Flat"};
    private String[] worldShape = new String[]{"Square", "Long", "Deep"};
    String[] worldSize = new String[]{"Small", "Normal", "Huge"};
    String[] worldTheme = new String[]{"Normal", "Hell", "Paradise", "Woods"};
    String[] worldDepth = new String[]{"Normal", "Deep", "Infinite"};
    private String[] gameModes = new String[]{"Survival", "Creative"};
    int selectedWorldType = 1;
    private int selectedWorldShape = 0;
    int selectedWorldSize = 1;
    int selectedWorldTheme = 0;
    int selectedWorldDepth = 0;
    private int selectedGameMode = 0;

    public GuiNewLevel(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public final void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButtonText(2, this.width / 2 - 100, this.height / 4, 98, 20, "Size: "));
        this.controlList.add(new GuiButtonText(1, this.width / 2 + 2, this.height / 4, 98, 20, "Depth:"));
        this.controlList.add(new GuiButtonText(0, this.width / 2 - 100, this.height / 4 + 48, "Type: "));
        this.controlList.add(new GuiButtonText(3, this.width / 2 - 100, this.height / 4 + 72, "Theme: "));
        this.controlList.add(new GuiButtonText(4, this.width / 2 - 100, this.height / 4 + 96, "Game Mode:"));
        this.controlList.add(new GuiButtonText(6, this.width / 2 - 100, this.height / 4 + 120, 98, 20, "Create"));
        this.controlList.add(new GuiButtonText(5, this.width / 2 + 2, this.height / 4 + 120, 98, 20, "Cancel"));
        
        this.refreshWorldOptionsDisplay();
    }

    private void refreshWorldOptionsDisplay() {
        ((GuiButtonText) this.controlList.get(0)).displayString = "Size: " + this.worldSize[this.selectedWorldSize];
        ((GuiButtonText) this.controlList.get(1)).displayString = "Depth: " + this.worldDepth[this.selectedWorldDepth];
        ((GuiButtonText) this.controlList.get(2)).displayString = "Type: " + this.worldType[this.selectedWorldType];
        ((GuiButtonText) this.controlList.get(3)).displayString = "Theme: " + this.worldTheme[this.selectedWorldTheme];
        ((GuiButtonText) this.controlList.get(4)).displayString = "Game Mode: " + this.gameModes[this.selectedGameMode];
    }
            
    protected final void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.selectedWorldType = (this.selectedWorldType + 1) % this.worldType.length;
                break;
            case 1:
                this.selectedWorldDepth = (this.selectedWorldDepth + 1) % this.worldDepth.length;
                break;
            case 2:
                this.selectedWorldSize = (this.selectedWorldSize + 1) % this.worldSize.length;
                break;
            case 3:
                this.selectedWorldTheme = (this.selectedWorldTheme + 1) % this.worldTheme.length;
                break;
            case 4:
                this.selectedGameMode = (this.selectedGameMode + 1) % this.gameModes.length;
                break;
            case 6:
                this.createWorld();
                break;
            case 5:
                this.mc.displayGuiScreen(this.prevGui);
                break;
        }

        this.refreshWorldOptionsDisplay();
    }

    private void createWorld() {
        this.mc.generateLevel(this.selectedWorldSize, this.selectedWorldShape, this.selectedWorldType, this.selectedWorldTheme, this.selectedWorldDepth);
        this.setGameMode(this.selectedGameMode);
        this.mc.displayGuiScreen(null);
    }

    private void setGameMode(int gameMode) {
        this.mc.thePlayer.isCreativeMode = gameMode == 1;
    }

    public final void drawScreen(int mouseX, int mouseY) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRenderer, "Generate new level", this.width / 2, 40, 16777215);

        // show level dimensions
        short[] dim = this.mc.getLevelDimensions(this.selectedWorldSize, this.selectedWorldType);
        drawCenteredString(this.fontRenderer, dim[0] + "x" + dim[2], this.width / 2, this.height / 4 + 24, 8421504);

        // show depth information
        String depthInfo = "";
        switch (this.selectedWorldDepth) {
            case 0:
                depthInfo = "64 blocks";
                break;
            case 1:
                depthInfo = "256 blocks";
                break;
            case 2:
                depthInfo = "Infinite";
                break;
        }
        drawCenteredString(this.fontRenderer, "Depth: " + depthInfo, this.width / 2, this.height / 4 + 36, 8421504);

        super.drawScreen(mouseX, mouseY);
    }
}
