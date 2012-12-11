package com.octagami.idols.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.IdolsPlayer;
import com.octagami.idols.IdolsPlayerManager;

public class Commandspeed extends PlayerCommand {

	public Commandspeed() {
		super("speed");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {

		Player player = (Player) sender;

		IdolsPlayer iPlayer = IdolsPlayerManager.getPlayer(player);
		
		//plugin.getLogger().info("" + player.getWalkSpeed());
			
		if (args.length < 1) {
			
			if (iPlayer.isSpeedEnabled()) {
				
				iPlayer.enableSpeed(false);
				player.sendMessage(ChatColor.RED + "You slow your pace");
				
			}else {
				
				iPlayer.enableSpeed(true);
				player.sendMessage(ChatColor.RED + "You quicken your pace");
			}
			
		}else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")){
			
			if (!iPlayer.isSpeedEnabled()) {
				
				iPlayer.enableSpeed(true);
				player.sendMessage(ChatColor.RED + "You quicken your pace");
				
			}else {
				
				player.sendMessage(ChatColor.RED + "You're already moving quickly");
			}
			
		}else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")){
			
			if (iPlayer.isSpeedEnabled()) {
				
				iPlayer.enableSpeed(false);
				player.sendMessage(ChatColor.RED + "You slow your pace");
				
			}else {
				
				player.sendMessage(ChatColor.RED + "You're already moving normally");
			}
			
		}

	}

}
