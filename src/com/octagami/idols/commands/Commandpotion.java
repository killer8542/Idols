package com.octagami.idols.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.exceptions.IdolsNotLoadedException;

public class Commandpotion extends PlayerCommand {

	public Commandpotion() {
		super("potion");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
		
		final Player player = (Player) sender;
		
		
		if (args.length < 1) {
			
			player.sendMessage(ChatColor.RED + "/" + command.getName() + " <potion>");
			showHelp(player);
			
		} else if (args[0].equalsIgnoreCase("help")) {
			
			showHelp(player);
			
		}
		else {
			
			short potionData = 0;
			int potionCost = 0;
			boolean noPermission = false;
			
			if (args[0].equalsIgnoreCase("smooth")) {
				
				if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.smooth") ) {
					potionData = 27;
					potionCost= Idols.getPlugin().getIdolsConfig().smoothCost;
				} else{
					noPermission = true;
				}
				
			}
			else if (args[0].equalsIgnoreCase("suave")) {
				
				if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.suave") ) {
					potionData = 29;
					potionCost= Idols.getPlugin().getIdolsConfig().suaveCost;
				} else{
					noPermission = true;
				}
				
			}
			else if (args[0].equalsIgnoreCase("debonair")) {
				
				if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.debonair") ) {
					potionData = 30;
					potionCost= Idols.getPlugin().getIdolsConfig().debonairCost;
				} else{
					noPermission = true;
				}
				
			}
			else {
				
				player.sendMessage(ChatColor.RED + "/" + command.getName() + " <potion>");
				showHelp(player);
			}
			
			if (potionData > 0) {
				
				double balance = Idols.getPlugin().getEconomy().getBalance(player.getName());

				if (balance > potionCost) {
					
					player.getInventory().addItem(new ItemStack(373, 1, potionData));
					
					player.sendMessage(ChatColor.GOLD + "You spent " + potionCost + " to purchase a " + args[0] + " potion");
					Idols.getPlugin().getEconomy().depositPlayer(player.getName(), -potionCost);
					
				} else {

					player.sendMessage(ChatColor.RED + "A " + args[0] + " potion costs " + potionCost + ". You can't afford one.");
				}

			}
			
			if (noPermission) {
				
				player.sendMessage(ChatColor.RED + "You don't yet have the ability to create that potion");
			}
	
				
		}
			
	}
	
	private void showHelp (Player player) throws IdolsNotLoadedException {
		
		player.sendMessage(ChatColor.GOLD + "You can create the following jobs experience bonus potions: ");

		if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.smooth") ) {
			
			player.sendMessage(ChatColor.GOLD + "Smooth   (+10% 30m): $" + Integer.toString(Idols.getPlugin().getIdolsConfig().smoothCost));
		}
		
		if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.suave") ) {
			
			player.sendMessage(ChatColor.GOLD + "Suave    (+25% 30m): $" + Integer.toString(Idols.getPlugin().getIdolsConfig().suaveCost));
		}
		
		if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "potion.debonair") ) {
	
			player.sendMessage(ChatColor.GOLD + "Debonair (+50% 15m): $" + Integer.toString(Idols.getPlugin().getIdolsConfig().debonairCost));
		}

	}

}
