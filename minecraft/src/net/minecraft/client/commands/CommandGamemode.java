package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;

public class CommandGamemode extends Command {

	public String getName() {
		return "/gamemode";
	}

	public void runCommand(Minecraft mc, String[] args) {
		if (args.length < 2) {
			mc.ingameGUI.addChatMessage("Invalid argument. Use /gamemode [0/s/survival=Survival, 1/c/creative=Creative].");
			return;
		}
		
		try {
			String gamemodeArg = args[1].toLowerCase();
			boolean isCreative = false;

			switch (gamemodeArg) {
				case "1":
				case "c":
				case "creative":
					isCreative = true;
					break;
				case "0":
				case "s":
				case "survival":
					isCreative = false;
					break;
				default:
					throw new IllegalArgumentException();
			}
			
			mc.thePlayer.isCreativeMode = isCreative;
			mc.ingameGUI.addChatMessage("Set gamemode to " + (isCreative ? "creative." : "survival."));
			
		} catch (Exception e) {
			mc.ingameGUI.addChatMessage("Invalid argument. Use /gamemode [0/s/survival=Survival, 1/c/creative=Creative].");
		}
	}

	public void showHelpMessage(Minecraft mc) {
		mc.ingameGUI.addChatMessage("/gamemode [0/s/survival=Survival, 1/c/creative=Creative]");
		mc.ingameGUI.addChatMessage("    Changes the player's gamemode.");
	}
}
