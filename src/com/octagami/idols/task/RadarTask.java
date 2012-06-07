package com.octagami.idols.task;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.exceptions.IdolsNotLoadedException;
import com.octagami.idols.util.Util;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;

public class RadarTask implements Runnable {
	
	private final IdolsPlugin plugin;
	
	private String playerName = "";
	private int oreId = -1;
	private int dist = 0;
	
  
    public RadarTask(IdolsPlugin plugin, String playerName, int oreId, int dist) {
        
    	this.plugin = plugin;
    	this.playerName = playerName;
    	this.oreId = oreId;
    	this.dist = dist;
    }

    @Override
    public void run() {
    	
    	if (!plugin.isEnabled())
			return;
    
    	Player player = plugin.getServer().getPlayer(playerName);
    	
    	if (player == null)
    		return;
    
		Location loc = player.getLocation();
		
		BukkitWorld w = new BukkitWorld(loc.getWorld());
		
		Vector vec1 = new Vector(loc.getX() + dist , loc.getY() + dist, loc.getZ() + dist);
		
		Vector vec2 = new Vector(loc.getX() - dist , loc.getY() - dist, loc.getZ() - dist);
		
		CuboidRegion region = new CuboidRegion(w, vec1, vec2);
		
		Vector oreLocation = Util.getBlockVector(region, oreId);
		
		if (oreLocation != null) {
			
			Location newLookLocation = Util.lookAtPoint( player, new Location( loc.getWorld(), oreLocation.getX(), oreLocation.getY(), oreLocation.getZ() ) );
			
			player.teleport(newLookLocation);
			
			player.sendMessage(ChatColor.GOLD + "You sense " + Material.getMaterial(oreId).name().replace("_", " ").toLowerCase() + " nearby!");
			
			int distance = 10;
			boolean emotesEnabled = true;
			
			try {
				emotesEnabled = Idols.getPlugin().getIdolsConfig().emotesEnabled;
				distance = Idols.getPlugin().getIdolsConfig().emoteDistance;
			} catch (IdolsNotLoadedException e) {
				e.printStackTrace();
			}
			
			if (emotesEnabled) {
				
				for (Player y : player.getWorld().getPlayers()) {
	                if (y != player && Util.isNear(player.getLocation(), y.getLocation(), distance)) {
	                    y.sendMessage(ChatColor.RED + player.getName() + " senses ore nearby!");
	                }
	            }
			}

		}
    	
    }
    
}
