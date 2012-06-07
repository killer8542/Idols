package com.octagami.idols.commands;

import org.bukkit.entity.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;


public class Commandhomie extends PlayerCommand {

	public Commandhomie() {
		super("homie");
	}

	@Override
	public void runCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) throws Exception {
	
		Player player = (Player) sender;
		final String playerName = player.getName();
		
		long cooldown = Idols.getAbilityCooldownTimeRemaining(player.getName(), "homie");

		if (cooldown <= 0) {

			Location loc = player.getLocation();
			
			@SuppressWarnings("unchecked")
			final LivingEntity entity = player.getWorld().spawn(loc, (Class<Villager>)EntityType.VILLAGER.getEntityClass());
			
			if (entity == null)
			{
				throw new Exception("Unable to spawn your homie =(");
			}
			
			int cooldownTime = 3600;
			
			IdolsPlugin plugin;
			
			plugin = Idols.getPlugin();
			
			if (plugin.getIdolsConfig() != null) 
				cooldownTime = plugin.getIdolsConfig().homieCooldown;
				
			Idols.setAbilityCooldown(playerName, "homie", cooldownTime);
			
		} else {
			
			player.sendMessage(ChatColor.GOLD + "You can't chill with your homie for another " + Long.toString(cooldown / 60) + " minute(s) and " + Long.toString(cooldown % 60) + " second(s)");
		}

	}

}
