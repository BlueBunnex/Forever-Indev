package net.minecraft.client.gui;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class GuiMessage extends GuiScreen {
	
	private static final String ALLOWED_CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+,.<>;':\"/?[{]}\\|`~";
	
	private String message;
	private boolean sendMessage;

	public GuiMessage() {
		message = "";
		sendMessage = false;
	}

	public final void drawScreen(int var1, int var2, float var3) {
		
		// draw the message
		drawRect(0, this.height - 82, this.width, this.height - 70, -1442840576);
		drawCenteredString(this.fontRenderer, message, this.width / 2, this.height - 80, 16777215);
		
		super.drawScreen(var1, var2, var3);
	}

	protected final void keyTyped(char character, int keycode) {
		
		// if enter is pressed, allow the message to be sent and close the menu
		if (keycode == 28) {
			
			sendMessage = true;
			
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
		}
		
		// checks if escape was pressed
		super.keyTyped(character, keycode);
		
		// either press backspace or add a character
		if (keycode == 14) {
			if (message.length() != 0)
				message = message.substring(0, message.length() - 1);
			
		} else if (ALLOWED_CHARS.indexOf(character) != -1) {
			message += character;
		}
	}
	
	public void onGuiClosed() {
		
		if (sendMessage && !message.isBlank()) {
			
			String[] parts = message.split(" ");
			
			switch (parts[0]) {
			
				case "/give":
					// check if correct arguments have been given
					if (parts.length == 1) {
						this.mc.ingameGUI.addChatMessage("Format: /give [item ID] [opt. item amount]");
						break;
					}
					
					// parse the item
					Item item;
					try {
						item = Item.itemsList[Integer.parseInt(parts[1])];
					} catch (Exception e) {
						this.mc.ingameGUI.addChatMessage("Could not give: No item with ID " + parts[1] + " exists.");
						break;
					}
					
					// parse the count (optional)
					int count = 1;
					if (parts.length > 2) {
						try {
							count = Integer.parseInt(parts[2]);
						} catch (Exception e) {}
					}
					
					EntityItem itemEntity = new EntityItem(
							this.mc.theWorld,
							this.mc.thePlayer.posX,
							this.mc.thePlayer.posY,
							this.mc.thePlayer.posZ,
							new ItemStack(item, count)
					);
					
					itemEntity.delayBeforeCanPickup = 0;
					this.mc.theWorld.spawnEntityInWorld(itemEntity);
					
					this.mc.ingameGUI.addChatMessage("Gave player " + count + "x " + item.getName());
					break;
					
				default:
					this.mc.ingameGUI.addChatMessage(message);
					break;
			}
		}
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}

}
