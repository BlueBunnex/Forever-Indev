package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;

public class CommandHelp extends Command {
	
	public String getName() {
		return "/help";
	}

	public void runCommand(Minecraft mc, String[] args) {
		
		for (Command command : Command.COMMANDS) {
			command.showHelpMessage(mc);
		}
	}

	public void showHelpMessage(Minecraft mc) {
		mc.ingameGUI.addChatMessage("/help");
		mc.ingameGUI.addChatMessage("    Shows all commands.");
	}

}
