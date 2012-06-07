package com.octagami.idols.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;

import com.octagami.idols.exceptions.NotEnoughArgumentsException;

public class Commandcake extends IdolsCommand {

	public Commandcake() {
		super("cake");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		Player player = null;
		
		if (sender.hasPermission("idols.cake")) {
			
			if (args.length > 0) {

				player = Bukkit.getServer().getPlayer(args[0]);

				if (player == null) {

					sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return;
				}

			} else if (sender instanceof Player) {

				player = (Player) sender;

			} else {

				throw new NotEnoughArgumentsException(command, commandLabel);
			}


			PlayerInventory inventory = player.getInventory();

			ItemStack cake = new ItemStack(Material.CAKE, 1);

			inventory.addItem(cake);
			player.sendMessage(ChatColor.GOLD + "Welcome to Monuments & Mayhem! Have some cake!");
			
		}

	}

}
