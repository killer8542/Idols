package com.octagami.idols.listener;


//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.player.PlayerJoinEvent;
//
//import com.octagami.idols.IdolsPerms;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

public class LoginListener implements Listener {

	private final IdolsPlugin	plugin;

	public LoginListener(IdolsPlugin plugin) {

		this.plugin = plugin;

	}

//	@EventHandler
//	public void onPlayerJoin(PlayerJoinEvent event) {
//
//		Player player = event.getPlayer();
//
//		if (IdolsPerms.canPoke(player)) {
//
//			player.sendMessage(ChatColor.GOLD + "Idols has given you the power to poke. Use it wisely!");
//		}
//
//	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if(!plugin.isEnabled()) 
			return;

		IdolsPlayerManager.playerQuit(event.getPlayer());

	}

}
