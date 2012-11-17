package com.octagami.idols;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.octagami.idols.exceptions.IdolsNotLoadedException;
import com.octagami.idols.util.Util;

public class Idols {

	private static IdolsPlugin	plugin;
	
	private static HashMap<String, HashMap<String, Date>> playerCooldowns = new HashMap<String, HashMap<String, Date>>();
	
	public static Random random = new Random();

	public static IdolsPlugin getPlugin() throws IdolsNotLoadedException {
		Idols.check();
		return Idols.plugin;
	}

	public static void setInstance(IdolsPlugin instance) {
		Idols.plugin = instance;
	}
	
	public static void clear() {
		
		playerCooldowns.clear();

	}

	private static void check() throws IdolsNotLoadedException {
		if (Idols.plugin == null) {
			throw new IdolsNotLoadedException();
		}
	}
	
	public static HashMap<String, HashMap<String, Date>> getAllCooldowns(){
		
		return playerCooldowns;
	}

	public static long getAbilityCooldownTimeRemaining(final String playerName, final String ability) {
		
		if (!playerCooldowns.containsKey(playerName)) {
			return 0;
		}
		
		if (!playerCooldowns.get(playerName).containsKey(ability)) {
			
			return 0;
		}
		
		return Util.getTimeRemaining( new Date(), playerCooldowns.get(playerName).get(ability), TimeUnit.SECONDS );
		
	}
	
	public static void setAbilityCooldown(final String playerName, final String ability, int refresh) {
		
		if (!playerCooldowns.containsKey(playerName))
			playerCooldowns.put(playerName, new HashMap<String, Date>());

		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, refresh);
        playerCooldowns.get(playerName).put(ability, cal.getTime());
			
	}
	
	public static void emote(Player player, String message) {
		
		if (!plugin.getIdolsConfig().emotesEnabled)
			return;
		
		for (Player y : player.getWorld().getPlayers()) {
            if (y != player && Util.isNear(player.getLocation(), y.getLocation(), plugin.getIdolsConfig().emoteDistance)) {
                y.sendMessage(ChatColor.RED + player.getName() + message);
            }
        }
		
	}
	
	public static void showDamage(Player player, int damage, boolean critical) {
		
		if (!IdolsPlayerManager.getPlayer(player).canSeeDamage())
			return;
			
		if (critical)
			player.sendMessage(ChatColor.RED + "You have crit for " + Integer.toString(damage) + " damage!" );
		else
			player.sendMessage(ChatColor.GOLD + "You have hit for " + Integer.toString(damage) + " damage!" );
		
	}
	
}
