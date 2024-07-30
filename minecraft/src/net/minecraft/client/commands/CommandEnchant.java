package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.item.enchant.Enchant;
import net.minecraft.game.item.enchant.EnchantType;

public class CommandEnchant extends Command {

	public String getName() {
		return "/enchant";
	}

	public void runCommand(Minecraft mc, String[] args) {
		
		// check if there are enough arguments
		if (args.length == 1) {
			mc.ingameGUI.addChatMessage("Format: /enchant [enchant type] [opt. enchant level]");
			return;
		}
		
		// check if enchantID is valid
		int enchantID;
				
		try {
			enchantID = Integer.parseInt(args[1]);
			
		} catch (Exception e) {
			mc.ingameGUI.addChatMessage("Enchant type must be a positive integer.");
			return;
		}
		
		if (enchantID < 0 || enchantID >= EnchantType.values().length) {
			mc.ingameGUI.addChatMessage(enchantID + " is not associated with an existing enchant type.");
			return;
		}
		
		// check if there's a held item to enchant
		ItemStack held = mc.thePlayer.inventory.getCurrentItem();
		
		if (held == null) {
			mc.ingameGUI.addChatMessage("No item held to enchant.");
			return;
		}
		
		// get enchant level
		int enchantLevel = 1;
		
		if (args.length > 2) {
			
			try {
				enchantLevel = Integer.parseInt(args[2]);
				
			} catch (Exception e) {
				mc.ingameGUI.addChatMessage("Enchant level must be a positive non-zero integer.");
				return;
			}
			
			if (enchantLevel <= 0) {
				mc.ingameGUI.addChatMessage("Enchant level must be a positive non-zero integer.");
				return;
			}
		}
		
		// add enchant
		Enchant enchant = held.addEnchant(EnchantType.values()[enchantID], enchantLevel);
		mc.ingameGUI.addChatMessage("Enchanted " + held.getItem().getName() + " with " + enchant);
	}

	public void showHelpMessage(Minecraft mc) {
		mc.ingameGUI.addChatMessage("/enchant [enchant type] [opt. enchant level]");
		mc.ingameGUI.addChatMessage("    Enchants the held item with the provided enchant.");
	}

}
