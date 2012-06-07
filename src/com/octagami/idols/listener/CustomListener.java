package com.octagami.idols.listener;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.event.CustomEvent;

public class CustomListener implements Listener {

	@SuppressWarnings("unused")
	private final IdolsPlugin	plugin;

	public CustomListener(IdolsPlugin plugin) {

		this.plugin = plugin;

	}

	@EventHandler
	public void onCustom(CustomEvent event) {

		Bukkit.getServer().broadcastMessage(event.getMessage());

	}

}
