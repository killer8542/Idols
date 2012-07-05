package com.octagami.idols.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

import com.octagami.idols.exceptions.BadArgumentsException;

import com.octagami.idols.util.Ores;

public class Commandradar extends PlayerCommand {

	public Commandradar() {
		super("radar");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
		
		final Player player = (Player) sender;
		
		int blockId = -1;
		Material ore = null;
		
		if (args.length < 1) {
			
			player.sendMessage(ChatColor.RED + "/" + command.getName() + " <orename>");
			showHelp(player);
			
		} else if (args[0].equalsIgnoreCase("help")) {
			
			showHelp(player);
			
		}
		else if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop")){
			
			IdolsPlayerManager.getPlayer(player).enableRadar(false, -1);
			
		} else if (args[0].equalsIgnoreCase("check")){
			
			if (!player.hasPermission("idols.radar.check"))
				return;
			
			Player other = null;
			
			if (args.length > 1) {
				other = Bukkit.getServer().getPlayer(args[1]);
			} else {
				other = player;
			}
			
			
			if (other == null) {

				sender.sendMessage(ChatColor.RED + args[1] + " is not online!");

			} else{
				
				IdolsPlugin plugin;

				int radarCooldown = 0;
				
				plugin = Idols.getPlugin();
				
				if (plugin.getIdolsConfig() != null)
					radarCooldown = plugin.getIdolsConfig().radarCooldown;
				
				long cooldown = Idols.getAbilityCooldownTimeRemaining(other.getName(), "radar");
				
				long timeUsed = cooldown - radarCooldown;
				
				if (cooldown == 0) {
					
					player.sendMessage(ChatColor.GOLD + other.getName() + " hasn't used radar recently");
					
				} else {
					
					player.sendMessage(ChatColor.GOLD + other.getName() + " last used radar " + Long.toString(timeUsed / 60 * -1)  + " minute(s) and " + Long.toString(timeUsed % 60 * -1)  + " second(s) ago");
				}
				
			}
			
		} else {
			
			ore = Ores.getByName(args[0]);
			
			if (ore == null)
				throw new BadArgumentsException(args[0]);
			
			if (!player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "radar." + ore.name() )) {
				player.sendMessage(ChatColor.RED + "You don't yet have the ability to detect " + ore.name().replace("_", " ").toLowerCase());
				return;
			}
			
			blockId = ore.getId();
		}
		
		
		if (blockId == -1)
			return;
			
		long cooldown = Idols.getAbilityCooldownTimeRemaining(player.getName(), "radar");
		
		if (cooldown <= 0) {
			
			player.sendMessage(ChatColor.GOLD + "You focus your mining intuition towards locating " + ore.name().replace("_", " ").toLowerCase());
			
			IdolsPlayerManager.getPlayer(player).enableRadar(true, blockId);

		} else {
			
			player.sendMessage(ChatColor.GOLD + "You can't activate this ability for " + Long.toString(cooldown / 60) + " minute(s) and " + Long.toString(cooldown % 60) + " second(s)");
			
		}

		
	}
	
	private void showHelp (Player player) {
		
		player.sendMessage(ChatColor.GOLD + "You can detect the following ore types: ");
		
		StringBuffer oreList = new StringBuffer();
		
		for ( Material ore : Ores.ORE_TYPES ) {
			
			if ( player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "radar." + ore.name()) ) {
				
				oreList.append(ore.name().toLowerCase().replace("_ore", ""));

				oreList.append(", ");
			}

		}
		
		if ( oreList.length() > 2 ) {
			oreList.delete(oreList.length() - 2, oreList.length() - 1);
		}
		
		player.sendMessage(ChatColor.GOLD + oreList.toString());

	}

}
