package com.octagami.idols.listener;

import me.zford.jobs.config.container.Job;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

public class BlockListener implements Listener {

	private final IdolsPlugin	plugin;

	public BlockListener(IdolsPlugin plugin) {

		this.plugin = plugin;
	}
	

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent event) {
    
        if(!plugin.isEnabled()) 
        	return;
       
        // check to make sure you can build
        if(!event.canBuild()) 
        	return;
        
        Player player = event.getPlayer();
        
        // check if in creative
        if (player.getGameMode().equals(GameMode.CREATIVE))
            return;
         
        String blockName = event.getBlock().getType().name();
        
		for (Job job : Idols.getBuilderJobs()) {
		
			if (job.getPlaceInfo().get(blockName) != null) {
				
				if ( job.getPlaceInfo().get(blockName).getXpGiven() > 0 ) {
					
					IdolsPlayerManager.getPlayer(player).enableFallImmunity(true);
					//plugin.getLogger().info("Setting fall immunity for " + player.getName() );
					break;
					
				}
					
			}

		}
	            
    }
    
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
    	
    	if(!plugin.isEnabled()) 
        	return;
    	
    	if (!event.getBlock().getType().equals(Material.DIAMOND_ORE))
    		return;
    	
    	final String playerName = event.getPlayer().getName();
    	
    	if ( IdolsPlayerManager.getPlayer(event.getPlayer()).getDiamondCounter() == 0 ) {
    		
    		IdolsPlayerManager.getPlayer(event.getPlayer()).incrementDiamondCounter();
    		
    		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
	            public void run() { 
	            	
	            	Player player = plugin.getServer().getPlayer(playerName);
	            	
	            	if (player != null) {
	            		
	            		int amountMined = IdolsPlayerManager.getPlayer(player).getDiamondCounter();
		            	int totalMined = IdolsPlayerManager.getPlayer(player).getTotalDiamonds();
		            	
		            	String alertString = player.getName() + " just broke " + Integer.toString(amountMined) + " diamond ore. " +
	                            Integer.toString(totalMined) + " total have been mined this session";
		            	
		            	plugin.getLogger().info(alertString);
		            	
		            	for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
		            		
		            		if (onlinePlayer.hasPermission(IdolsPlugin.PERMISSION_ROOT + "alert")) {
		            			
		            			ChatColor alertColor = ChatColor.GOLD;
		            			
		            			if (player.hasPermission(IdolsPlugin.PERMISSION_ROOT + "alert")) {
		            				
		            				// Player is a mod or admin
		            				alertColor = ChatColor.GREEN;
					    		}
		            			else if (player.hasPermission("xray.warned") || player.hasPermission("xray.banned")) {
		            				
		            				alertColor = ChatColor.DARK_RED;
		            				
		            			} else if (player.hasPermission("xray.suspect")) {
		            				
		            				alertColor = ChatColor.RED;
		            				
		            			} else if (player.hasPermission("xray.legit")) {
		            				
		            				alertColor = ChatColor.GREEN;
		            				
		            			} 
		            			
		            			onlinePlayer.sendMessage(alertColor + alertString);
		            			
		            		}
		                    
		                }
		            	
		            	IdolsPlayerManager.getPlayer(player).resetDiamondCounter();	
	            	}
	            	
	            }  
	            
	        }, plugin.getIdolsConfig().alertDelay * 20);	
    		
    	} else {
    		
    		IdolsPlayerManager.getPlayer(event.getPlayer()).incrementDiamondCounter();
    	}
    	
    	
    }
        

}
