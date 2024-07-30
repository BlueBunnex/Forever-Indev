package net.minecraft.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;

public class CommandGive extends Command {

	public String getName() {
		return "/give";
	}

	public void runCommand(Minecraft mc, String[] args) {
		
		// check if correct arguments have been given
		if (args.length == 1) {
			mc.ingameGUI.addChatMessage("Format: /give [item ID] [opt. item amount]");
			return;
		}
		
		// parse the item
		Item item;
		try {
			item = Item.itemsList[Integer.parseInt(args[1])];
			
			if (item == null)
				throw new Exception();
			
		} catch (Exception e) {
			mc.ingameGUI.addChatMessage("Could not give: No item with ID " + args[1] + " exists.");
			return;
		}
		
		// parse the count (optional)
		int count = 1;
		if (args.length > 2) {
			try {
				count = Integer.parseInt(args[2]);
			} catch (Exception e) {}
		}
		
		EntityItem itemEntity = new EntityItem(
				mc.theWorld,
				mc.thePlayer.posX,
				mc.thePlayer.posY,
				mc.thePlayer.posZ,
				new ItemStack(item, count)
		);
		
		itemEntity.delayBeforeCanPickup = 0;
		mc.theWorld.spawnEntityInWorld(itemEntity);
		
		mc.ingameGUI.addChatMessage("Gave player " + count + "x " + item.getName());
	}

	public void showHelpMessage(Minecraft mc) {
		mc.ingameGUI.addChatMessage("/give [item ID] [opt. item amount]");
		mc.ingameGUI.addChatMessage("    Gives the player the specified item.");
	}

}
