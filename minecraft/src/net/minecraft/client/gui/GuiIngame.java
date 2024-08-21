package net.minecraft.client.gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.gui.container.SlotAccessory;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.entity.RenderItem;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemQuiver;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.enchant.Enchant;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public final class GuiIngame extends Gui {

    private static RenderItem itemRenderer = new RenderItem();
    private List<ChatLine> chatMessageList = new ArrayList<ChatLine>();
    private Random rand = new Random();
    private Minecraft mc;
    private int updateCounter = 0;

    // Cached OS information
    private String cachedOSInfo = null;

    public GuiIngame(Minecraft mc) {
        this.mc = mc;
    }

    private String getOSInfo() {
        if (cachedOSInfo == null) {
            try {
                Properties props = System.getProperties();
                String osName = props.getProperty("os.name");
                String osVersion = getWindowsBuildVersion();
                String osArch = props.getProperty("os.arch");
                cachedOSInfo = osName + " " + osVersion + " (" + osArch + ")";
            } catch (Exception e) {
                e.printStackTrace();
                cachedOSInfo = "Unknown OS";
            }
        }
        return cachedOSInfo;
    }

    public final void renderGameOverlay(float var1) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);

        int width = scaledResolution.getScaledWidth(),
            height = scaledResolution.getScaledHeight();

        FontRenderer fontRend = this.mc.fontRenderer;

        this.mc.entityRenderer.setupOverlayRendering();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        InventoryPlayer var5 = this.mc.thePlayer.inventory;
        this.zLevel = -90.0F;
        this.drawTexturedModalRect(width / 2 - 91, height - 22, 0, 0, 182, 22);
        this.drawTexturedModalRect(width / 2 - 91 - 1 + var5.currentItem * 20, height - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
        this.drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(GL11.GL_BLEND);
        boolean var20 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
        if (this.mc.thePlayer.heartsLife < 10) {
            var20 = false;
        }

        this.rand.setSeed((long) (this.updateCounter * 312871));

        // draw HUD
        if (this.mc.playerController.shouldDrawHUD()) {

            int x, y;

            // render armor damage
            x = width / 2 - 102;

            if (this.mc.thePlayer.inventory.getPlayerArmorValue() > 0) {
                for (int i = 0; i < 4; i++) {
                    y = height - 9 - (i << 3);

                    ItemStack armorPiece = this.mc.thePlayer.inventory.armorInventory[i];
                    if (armorPiece != null) {
                        // Determine the type of armor and set color
                        if (armorPiece.getItem() == Item.helmetIron || armorPiece.getItem() == Item.chestplateIron || armorPiece.getItem() == Item.leggingsIron || armorPiece.getItem() == Item.bootsIron) {
                            GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F); // Gray for Iron
                        } else if (armorPiece.getItem() == Item.helmetGold || armorPiece.getItem() == Item.chestplateGold || armorPiece.getItem() == Item.leggingsGold || armorPiece.getItem() == Item.bootsGold) {
                            GL11.glColor4f(1.0F, 0.85F, 0.0F, 1.0F); // Gold color
                        } else if (armorPiece.getItem() == Item.helmetDiamond || armorPiece.getItem() == Item.chestplateDiamond || armorPiece.getItem() == Item.leggingsDiamond || armorPiece.getItem() == Item.bootsDiamond) {
                            GL11.glColor4f(0.5F, 0.8F, 1.0F, 1.0F); // Light blue for Diamond
                        } else {
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Default white
                        }
                    } else {
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Default white for empty slot
                    }

                    int armorPieceDura = this.mc.thePlayer.inventory.getPlayerArmorValue(i);

                    if (armorPieceDura == 1)
                        y += this.rand.nextInt(2);

                    this.drawTexturedModalRect(x, y, 16 + armorPieceDura * 9, 18 + (3 - i) * 9, 9, 9);
                }
                // Reset color to white after rendering armor
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

         // Render health with sprint-based visual effects
            if (!this.mc.thePlayer.isCreativeMode) {
                int playerHealth = this.mc.thePlayer.health;
                int playerPrevHealth = this.mc.thePlayer.prevHealth;
                float staminaRemaining = MovementInputFromOptions.staminaRemaining; // Access current stamina
                float maxStamina = MovementInputFromOptions.maxStamina;
                int redHearts = (int) Math.ceil((staminaRemaining / maxStamina) * 10); // Calculate number of hearts affected by stamina

                // Calculate if stamina is low enough to trigger flashing (e.g., below 20%)
                boolean shouldFlashFourthHeart = (staminaRemaining / maxStamina) < 0.2f;
                // Use System.currentTimeMillis() for time-based flashing effect
                boolean flashOn = (System.currentTimeMillis() / 500) % 2 == 0; // Toggle every 500ms (half a second)

                for (int i = 0; i < 10; i++) {
                    byte var26 = 0;
                    if (var20) {
                        var26 = 1;
                    }

                    x = width / 2 - 91 + (i << 3); // Calculate x position for each heart
                    y = height - 32; // Base y position for hearts

                    // Render background heart (default empty heart)
                    this.drawTexturedModalRect(x, y, 16 + var26 * 9, 0, 9, 9);

                    // Handle the overlay of full and half hearts based on the player's previous health state
                    if (var20) {
                        if ((i << 1) + 1 < playerPrevHealth) {
                            this.drawTexturedModalRect(x, y, 70, 0, 9, 9);
                        }

                        if ((i << 1) + 1 == playerPrevHealth) {
                            this.drawTexturedModalRect(x, y, 79, 0, 9, 9);
                        }
                    }

                    // Render the fourth heart with a flashing effect if stamina is low
                    if (i == 3 && shouldFlashFourthHeart && !flashOn) {
                        // Skip rendering to create the flashing effect
                        continue;
                    }

                 // Render hearts based on current health with stamina overlay effects
                    for (int i1 = 0; i1 < 10; i1++) {
                        int heartX = width / 2 - 91 + (i1 * 8); // Calculate x position for each heart
                        int heartY = height - 32; // Base y position for hearts

                        // Render background heart (default empty heart)
                        this.drawTexturedModalRect(heartX, heartY, 16, 0, 9, 9);

                        // Determine if we are dealing with full or half hearts
                        int heartHealthValue = i1 * 2; // Each heart index represents 2 health points (full heart)

                        if (heartHealthValue + 1 < playerHealth) {
                            // The current heart is a full heart
                            if (staminaRemaining <= heartHealthValue) {
                                // Render the full heart with the stamina overlay (light gray)
                                GL11.glColor4f(0.75F, 0.8F, 0.8F, 1.0F); // Light gray
                                this.drawTexturedModalRect(heartX, heartY, 52, 0, 9, 9);
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Reset to white after drawing
                            } else {
                                // Render a normal full heart
                                this.drawTexturedModalRect(heartX, heartY, 52, 0, 9, 9);
                            }
                        } else if (heartHealthValue + 1 == playerHealth) {
                            // The current heart is a half heart
                            if (staminaRemaining <= heartHealthValue) {
                                // Render the half heart with the stamina overlay (light gray)
                                GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F); // Light gray
                                this.drawTexturedModalRect(heartX, heartY, 61, 0, 9, 9);
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Reset to white after drawing
                            } else {
                                // Render a normal half heart
                                this.drawTexturedModalRect(heartX, heartY, 61, 0, 9, 9);
                            }
                        }
                    }

                    // Reset color to default after drawing each heart
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                }
            }
            

            // render air bubbles on GUI
            if (this.mc.thePlayer.isInsideOfWater() && !this.mc.thePlayer.isCreativeMode) {

                int a = (int) Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
                int b = (int) Math.ceil((double)this.mc.thePlayer.air       * 10.0D / 300.0D) - a;

                for (int c = 0; c < a + b; c++) {
                    if (c < a) {
                        this.drawTexturedModalRect(width / 2 - 91 + (c << 3), height - 32 - 9, 16, 9, 9, 9);
                    } else {
                        this.drawTexturedModalRect(width / 2 - 91 + (c << 3), height - 32 - 9, 25, 9, 9, 9);
                    }
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        // Enable transparency for all items that have it
        // render hotbar
        for (int i = 0; i < 9; i++) {

            int var25 = width / 2 - 90 + i * 20 + 2;
            int var21 = height - 16 - 3;

            ItemStack var22 = this.mc.thePlayer.inventory.mainInventory[i];

            if (var22 != null) {
                float var9 = (float) var22.animationsToGo - var1;
                if (var9 > 0.0F) {
                    GL11.glPushMatrix();
                    float var26 = 1.0F + var9 / 5.0F;
                    GL11.glTranslatef((float) (var25 + 8), (float) (var21 + 12), 0.0F);
                    GL11.glScalef(1.0F / var26, (var26 + 1.0F) / 2.0F, 1.0F);
                    GL11.glTranslatef((float) (-(var25 + 8)), (float) (-(var21 + 12)), 0.0F);
                }

                // Enable blending to use the texture's alpha values
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                // Render the item using the texture's own alpha channel
                itemRenderer.renderItemIntoGUI(this.mc.renderEngine, var22, var25, var21);
                if (var9 > 0.0F) {
                    GL11.glPopMatrix();
                }

                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, var22, var25, var21);

                // Disable blending after rendering
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_NORMALIZE);

        // Player coordinates
        double playerX = this.mc.thePlayer.posX;
        double playerY = this.mc.thePlayer.posY;
        double playerZ = this.mc.thePlayer.posZ;

        // Always draw "Minecraft Forever Indev"
        String text1 = "Minecraft ";
        String text2 = "Forever Indev";

        // Draw "Minecraft" in white
        fontRend.drawStringWithShadow(text1, 2, 2, 16777215);

        // Draw "Forever Indev" in cyan
        fontRend.drawStringWithShadow(text2, 2 + fontRend.getStringWidth(text1), 2, 0x00FFFF);

        // Show FPS (basically debug/F3 menu)
        if (this.mc.currentScreen == null && this.mc.options.showFPS) {
            String text3 = " (" + this.mc.debug + ")";

            // Draw the remaining text in white
            fontRend.drawStringWithShadow(text3, 2 + fontRend.getStringWidth(text1 + text2), 2, 16777215);

            Minecraft var23 = this.mc;
            fontRend.drawStringWithShadow(var23.renderGlobal.getDebugInfoRenders(), 2, 12, 16777215);

            var23 = this.mc;
            fontRend.drawStringWithShadow(var23.renderGlobal.getDebugInfoEntities(), 2, 22, 16777215);

            var23 = this.mc;
            fontRend.drawStringWithShadow("P: " + var23.effectRenderer.getStatistics() + ". T: " + var23.theWorld.debugSkylightUpdates(), 2, 32, 16777215);

            // Draw held item ID
            ItemStack currItem = this.mc.thePlayer.inventory.getCurrentItem();
            fontRend.drawStringWithShadow("Held ID: " + (currItem == null ? "N/A" : currItem.itemID), 2, 42, 16777215);

            // Memory calculations
            long maxMem   = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem  = Runtime.getRuntime().freeMemory();
            long what     = maxMem - freeMem;

            String var18 = "Free memory: " + what * 100L / maxMem + "% of " + maxMem / 1024L / 1024L + "MB";
            drawString(fontRend, var18, width - fontRend.getStringWidth(var18) - 2, 2, 16777215);

            var18 = "Allocated memory: " + totalMem * 100L / maxMem + "% (" + totalMem / 1024L / 1024L + "MB)";
            drawString(fontRend, var18, width - fontRend.getStringWidth(var18) - 2, 12, 16777215);

            // Fetch OS information only when needed
            String os = getOSInfo();

            // Fetch GPU information using LWJGL
            String gpu = GL11.glGetString(GL11.GL_RENDERER);

            // Fetch CPU architecture information
            String cpuArch = System.getProperty("os.arch");

            // Determine if the game is in fullscreen or windowed mode
            String screenMode = Display.isFullscreen() ? "(Fullscreen)" : "(Windowed)";

            // Fetch display resolution
            DisplayMode displayMode = Display.getDisplayMode();
            String resolution = displayMode.getWidth() + "x" + displayMode.getHeight() + " " + screenMode;

            String coordinates = String.format("XYZ: %.1f / %.1f / %.1f", playerX, playerY, playerZ);

            float lookingPitch = this.mc.thePlayer.rotationPitch;
            float lookingYaw = this.mc.thePlayer.rotationYaw;

            String direction = "";
            String axis = "";
            float yaw = this.mc.thePlayer.rotationYaw;
            yaw = yaw % 360;
            if (yaw < 0) {
                yaw += 360;
            }

            if (yaw >= 45 && yaw < 135) {
                direction = "north";
                axis = "Towards negative Z";
            } else if (yaw >= 135 && yaw < 225) {
                direction = "east";
                axis = "Towards positive X";
            } else if (yaw >= 225 && yaw < 315) {
                direction = "south";
                axis = "Towards positive Z";
            } else {
                direction = "west";
                axis = "Towards negative X";
            }

            String lookingPosition = String.format("Facing: %s (%s) (Yaw: %.1f / Pitch: %.1f)", direction, axis, lookingYaw, lookingPitch);

            // Drawing additional debug information
            fontRend.drawStringWithShadow("OS: " + os, 2, 52, 16777215);
            fontRend.drawStringWithShadow("GPU: " + gpu, 2, 62, 16777215);
            fontRend.drawStringWithShadow("CPU Arch: " + cpuArch, 2, 72, 16777215);  // Display CPU architecture
            fontRend.drawStringWithShadow("Resolution: " + resolution, 2, 82, 16777215);  // Display screen resolution
            fontRend.drawStringWithShadow(coordinates, 2, 92, 16777215);
            fontRend.drawStringWithShadow(lookingPosition, 2, 102, 16777215);

            // Draw current date and time in "Month day, Year @ time" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy @ h:mm a");
            String dateTime = dateFormat.format(new Date());
            drawString(fontRend, "Current time: " + dateTime, 2, 112, 16777215);
        }

        InventoryPlayer inventoryPlayer = this.mc.thePlayer.inventory;
        int arrowCount = 0;

        // Check if a quiver is equipped in slot 36
        ItemStack quiverStack = inventoryPlayer.getStackInSlot(36);
        boolean isQuiverEquipped = quiverStack != null && quiverStack.getItem() == Item.quiver;

        if (isQuiverEquipped) {
            // Check if arrows are in slot 37
            ItemStack arrowStack = inventoryPlayer.getStackInSlot(37);
            if (arrowStack != null && arrowStack.getItem() == Item.arrow) {
                arrowCount = arrowStack.stackSize;
            }
        }

        // Display arrow count only if the quiver is equipped
        String arrowString = "Arrows: " + arrowCount;

        if (isQuiverEquipped) {
            fontRend.drawStringWithShadow(arrowString, width / 2 + 8, height - 33, 0xFFFFFF);
        }

        // Render chat messages
        int chatLineHeight = 10; // Adjust height according to font size
        int maxVisibleLines = 10; // Number of visible chat lines, adjust as needed

        for (int i = 0; i < Math.min(this.chatMessageList.size(), maxVisibleLines); i++) {
            ChatLine chatline = this.chatMessageList.get(i);
            String message = chatline.message;

            int xPos = 2; // X position for chat messages
            int yPos = height - 40 - (i * chatLineHeight); // Y position based on index and height

            fontRend.drawStringWithShadow(message, xPos, yPos, 16777215); // Render the chat message in white
        }
    }

    private String getWindowsBuildVersion() {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c ver");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String windowsVersion = stringBuilder.toString();
            return windowsVersion.replaceAll("[^0-9.]", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public final void updateChatMessages() {
        this.updateCounter++;

        for (int i = 0; i < this.chatMessageList.size(); i++) {
            ChatLine chatline = this.chatMessageList.get(i);

            if (chatline.updateCounter > 200) {
                this.chatMessageList.remove(this.chatMessageList.size() - 1);
            } else {
                chatline.updateCounter++;
            }
        }
    }

    public final void addChatMessage(String message) {
        chatMessageList.add(0, new ChatLine(message));
    }
}
