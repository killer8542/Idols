package com.octagami.idols;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class IdolsPlayerManager {

	public static HashMap<String, IdolsPlayer>	players	= new HashMap<String, IdolsPlayer>();

	public static void playerQuit(Player player) {
		
		if (players.containsKey(player.getName())) {
			
			IdolsPlayerManager.getPlayer(player).cancelTasks();
			IdolsPlayerManager.players.remove(player.getName());
		}
	}
	
	public static void playerDied(Player player) {
		
		if (players.containsKey(player.getName())) {
			IdolsPlayerManager.getPlayer(player).cancelTasks();
		}
	}

	public static IdolsPlayer getPlayer(Player player) {
		IdolsPlayer user = IdolsPlayerManager.players.get(player.getName());
		if (user == null) {
			user = new IdolsPlayer(player);
			IdolsPlayerManager.players.put(player.getName(), user);
		}
		return user;
	}
	
	
	
	
}
