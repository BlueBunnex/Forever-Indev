package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;

public abstract class Command {
	
	public static final Command[] COMMANDS = new Command[] { new CommandHelp(), new CommandGive() };
	
	public abstract String getName();

	/**
	 * 
	 * @param mc
	 * @param args the first argument is itself the command name
	 */
	public abstract void runCommand(Minecraft mc, String[] args);
	
	public abstract void showHelpMessage(Minecraft mc);
	
}
