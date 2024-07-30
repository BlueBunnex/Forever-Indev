package net.minecraft.client.gui;

import net.minecraft.client.commands.Command;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class GuiMessage extends GuiScreen {
	
	private static final String ALLOWED_CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+,.<>;':\"/?[{]}\\|`~";
	
	private String message;
	private boolean sendMessage = false;

	public GuiMessage(boolean isCommand) {
		message = isCommand ? "/" : "";
	}

	public final void drawScreen(int mouseX, int mouseY) {
		
		// draw the message
		drawRect(0, this.height - 82, this.width, this.height - 70, -1442840576);
		drawCenteredString(this.fontRenderer, message, this.width / 2, this.height - 80, 16777215);
		
		super.drawScreen(mouseX, mouseY);
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
		
		if (!sendMessage || message == null || message.trim().isEmpty())
			return;
		
		if (message.charAt(0) == '/') {
			
			// run command if it exists
			String[] parts = message.split(" ");
			
			for (Command command : Command.COMMANDS) {
				
				if (command.getName().equals(parts[0])) {
					command.runCommand(mc, parts);
					return;
				}
			}
			
			this.mc.ingameGUI.addChatMessage("Unknown command. Use /help for help.");
			
		} else {
			// send message
			this.mc.ingameGUI.addChatMessage(message);
		}
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}

}
