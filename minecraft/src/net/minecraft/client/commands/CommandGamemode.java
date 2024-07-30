package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;

public class CommandGamemode extends Command {

	public String getName() {
		return "/gamemode";
	}

	public void runCommand(Minecraft mc, String[] args) {
		
		try {
			int gamemode = Integer.parseInt(args[1]);
			mc.thePlayer.isCreativeMode = gamemode == 1;
			
			mc.ingameGUI.addChatMessage("Set gamemode to " + (gamemode == 1 ? "creative." : "survival."));
			
		} catch (Exception e) {
			mc.ingameGUI.addChatMessage("Invalid argument. Use 0 for survival, 1 for creative.");
			return;
		}
	}

	public void showHelpMessage(Minecraft mc) {
		mc.ingameGUI.addChatMessage("/gamemode [0=survival/1=creative]");
		mc.ingameGUI.addChatMessage("    Changes the player's gamemode.");
	}

}
