package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import util.MathHelper;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public final class GuiMainMenu extends GuiScreen {
    private boolean isDragging = false;
    private int lastMouseY = 0;
    private float scrollVelocity = 0;
    private float scrollOffset = 0;
    private float scrollAcceleration = 0.1F; // Acceleration factor
    private float maxScrollSpeed = 2.0F; // Maximum scroll speed
    private static final String[] SPLASHES = new String[] {
        "Not-quite indev!",
        "Chests on the glass door!",
        "NP is not in P!",
        "The blue-twintailed girl is right!",
        "Trust the blue-twintailed girl!",
        "In development hell, did I development sin?",
        "Bubbles from the gum machine!",
        "[INSERT SPLASH HERE]",
        "Full-stop!",
        "[EXTREMELY LOUD INCORRECT BUZZER]",
        "Bricks in the wall!",
        "Catch the falling stars!",
        "Syntax error at line 42!",
        "Glitches in the matrix!",
        "Null pointer exception!",
        "Endless loops ahead!",
        "In the rabbit hole!",
        "Blue screen of life!",
        "Assembly required!",
        "Pixels and polygons!",
        "Legacy code detected!",
        "Unicorns in disguise!",
        "404: Splash not found!",
        "Press F to pay respects!",
        "Welcome to Hell!",
        "Steve? Is that you, Minecraft Steve?",
        "Hell level? More like in development hell!",
        "Creepers gonna creep!",
        "You are the chosen one!",
        "Loading... please wait!",
        "Did you bring a sword?",
        "Save early, save often!",
        "Not-quite reality!",
        "Get tf outta my fork, Herobrine!",
        "Watch out for Herobrine! He’s been known to mess with devs!",
        "Is this going to be Forever Indev or just indefinitely on hold?",
        "Under construction!",
        "Stealthy creepers!",
        "Save your progress!",
        "Less is more!",
        "Made with <3",
        "Made with the help of RetroMCP-Java",
        "Wait, who is that in the fog?",
    };

    private static final String CURRENT_SPLASH = SPLASHES[(int) (Math.random() * SPLASHES.length)];

    public GuiMainMenu() {
        // No additional initialization needed for splashes
    }

    @Override
    public final void updateScreen() {
        // Update the scroll offset to create a scrolling effect
        this.scrollOffset += 0.5F; // Adjust this value for scroll speed
        if (this.scrollOffset >= 32) { // Texture height is 32
            this.scrollOffset = 0;
        }
    }

    @Override
    public final void drawDefaultBackground() {
        boolean var1 = false;
        if (this.mc.theWorld != null) {
            drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            Tessellator var2 = Tessellator.instance;

            // Bind the dirt texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/dirt.png"));

            // Disable mipmapping and linear filtering
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            // Set the color to white (full texture colors)
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            var2.startDrawingQuads();
            var2.setColorOpaque_I(4210752);

            // Define the vertices with UV mapping and scrolling offset
            float textureY = this.scrollOffset / 32.0F; // Calculate texture Y offset
            var2.addVertexWithUV(0.0F, (float) this.height, 0.0F, 0.0F, textureY + (this.height / 32.0F));
            var2.addVertexWithUV((float) this.width, (float) this.height, 0.0F, (float) this.width / 32.0F, textureY + (this.height / 32.0F));
            var2.addVertexWithUV((float) this.width, 0.0F, 0.0F, (float) this.width / 32.0F, textureY);
            var2.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, textureY);

            var2.draw();
        }
    }


    @Override
    protected final void keyTyped(char character, int keycode) {}

    @Override
    public final void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButtonText(1, this.width / 2 - 100, this.height / 4 + 48, "Generate new level..."));
        this.controlList.add(new GuiButtonText(2, this.width / 2 - 100, this.height / 4 + 72, "Load level..."));
        this.controlList.add(new GuiButtonText(0, this.width / 2 - 100, this.height / 4 + 96, "Options..."));
        this.controlList.add(new GuiButtonText(3, this.width / 2 - 100, this.height / 4 + 132, "Exit Game"));

        // Disable load level button if no session
        if (this.mc.session == null) {
            this.controlList.get(1).enabled = false;
        }
    }

    @Override
    protected final void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.options));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiNewLevel(this));
                break;
            case 2:
                if (this.mc.session != null) {
                    this.mc.displayGuiScreen(new GuiLoadLevel(this));
                }
                break;
            case 3:
                this.mc.shutdownMinecraftApplet();
                break;
        }
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY) {
        this.drawDefaultBackground();

        // Set texture filtering to linear
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        // Draw the logo
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/logo.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator.instance.setColorOpaque_I(Gui.COLOR_WHITE);
        this.drawTexturedModalRect((this.width - 256) / 2, 30, 0, 0, 256, 64);

        // Draw "Forever Indev" text in cyan under the logo, moved up and larger
        GL11.glPushMatrix();
        GL11.glTranslatef((this.width / 2), 75.0F, 0.0F); // Move it up more
        float scaleIndev = 1.5F; // Make it larger
        GL11.glScalef(scaleIndev, scaleIndev, 1);
        String indevText = "Forever Indev";
        drawCenteredString(this.fontRenderer, indevText, 0, 0, 65535);  // 65535 is the color code for cyan
        GL11.glPopMatrix();

        // Draw other elements
        String text = "Made by Blue. Distribute!";
        drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, this.height - 10, Gui.COLOR_WHITE);

        // Memory info
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;

        text = "Free memory: " + usedMem * 100L / maxMem + "% of " + maxMem / 1024L / 1024L + "MB";
        drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, 2, Gui.COLOR_GREY);

        text = "Allocated memory: " + totalMem * 100L / maxMem + "% (" + totalMem / 1024L / 1024L + "MB)";
        drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, 12, Gui.COLOR_GREY);

        // Draw current date and time in "Month day, Year @ time" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy @ h:mm a");
        String dateTime = dateFormat.format(new Date());
        drawString(this.fontRenderer, "Current time: " + dateTime, 2, 2, Gui.COLOR_WHITE);

        // Draw buttons and splash text
        super.drawScreen(mouseX, mouseY);

        // Draw splash text
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2 + 110), 85.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);

        float scale = 1.8F + MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        scale = scale * 100.0F / (float)(this.fontRenderer.getStringWidth(CURRENT_SPLASH) + 32);
        GL11.glScalef(scale, scale, 1);

        // Estimate text height if getHeight() is not available
        int lineHeight = 9; // Default value, adjust as needed

        // Split the splash text into multiple lines based on available width
        String[] lines = splitTextIntoLines(CURRENT_SPLASH, (int) (this.width * scale * 0.8)); // Reduce line length by 20%

        for (int i = 0; i < lines.length; i++) {
            drawCenteredString(this.fontRenderer, lines[i], 0, -8 + (i * lineHeight), 16776960);
        }

        GL11.glPopMatrix();
    }

    private String[] splitTextIntoLines(String text, int maxWidth) {
        StringBuilder line = new StringBuilder();
        ArrayList<String> lines = new ArrayList<>();
        String[] words = text.split(" ");

        for (String word : words) {
            if (this.fontRenderer.getStringWidth(line + word + " ") > maxWidth) {
                lines.add(line.toString().trim());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        
        // Add last line if not empty
        if (line.length() > 0) {
            lines.add(line.toString().trim());
        }

        return lines.toArray(new String[0]);
    }
}
