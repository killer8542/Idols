package com.octagami.idols.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

public class LoginListener implements Listener {

	private final IdolsPlugin	plugin;

	public LoginListener(IdolsPlugin plugin) {

		this.plugin = plugin;

	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if(!plugin.isEnabled()) 
			return;

		IdolsPlayerManager.playerQuit(event.getPlayer());

	}

}
