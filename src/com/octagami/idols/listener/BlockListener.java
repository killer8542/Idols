package com.octagami.idols.listener;

import me.zford.jobs.bukkit.actions.BlockActionInfo;
import me.zford.jobs.container.ActionType;
import me.zford.jobs.container.JobProgression;
import me.zford.jobs.container.JobsPlayer;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

public class BlockListener implements Listener {

	private final IdolsPlugin	plugin;

	public BlockListener(IdolsPlugin plugin) {

		this.plugin = plugin;
	}
	
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent event) {
    
        if(!plugin.isEnabled()) return;
       
        // check to make sure you can build
        if(!event.canBuild()) return;
        
        Player player = event.getPlayer();
        
        if (player == null) return;
        
        // check if in creative
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
    
        if (IdolsPlayerManager.getPlayer(player).canBeFallImmune()) {
        	
        	JobsPlayer worker = plugin.getJobsHook().getPlayerManager().getJobsPlayer(player.getName());
			
			if (worker == null) return;
			
        	for (JobProgression job : worker.getJobProgression()) {

				if ( !job.getJob().getName().equals("Builder") && !job.getJob().getbaseJob().equals("Builder") )
					continue;

    			Double blockXP = job.getJob().getExperience(new BlockActionInfo(event.getBlock(), ActionType.PLACE), 1, 1);
    				
    			if (blockXP != null && blockXP > 0) {
    				
    				IdolsPlayerManager.getPlayer(player).enableFallImmunity(true);
    				
    				if (plugin.getIdolsConfig().DEBUG)
    					plugin.getLogger().info("Setting fall immunity for " + player.getName() );
    			}

    			break;
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
    	
    	if ( IdolsPlayerManager.getPlayer(event.getPlayer()).getDiamondBreakCounter() == 0 ) {
    		
    		IdolsPlayerManager.getPlayer(event.getPlayer()).incrementDiamondCounter();
    		
    		final int alertThreshold = plugin.getIdolsConfig().alertThreshold;

    		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
	            public void run() { 
	            	
	            	Player player = plugin.getServer().getPlayer(playerName);

	            	if (player != null) {

	            		int amountMined = IdolsPlayerManager.getPlayer(player).getDiamondBreakCounter();
		            	int totalMined = IdolsPlayerManager.getPlayer(player).getTotalDiamondsBroken();
		            	
		            	if (totalMined <= alertThreshold) {
		            		IdolsPlayerManager.getPlayer(player).resetDiamondCounter();
		            		return;
		            	}
		        			
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
		            			else if (player.hasPermission("warned.xray") || player.hasPermission("banned.xray")) {
		            				
		            				alertColor = ChatColor.DARK_RED;
		            				
		            			} else if (player.hasPermission("suspect.xray")) {
		            				
		            				alertColor = ChatColor.RED;
		            				
		            			} else if (player.hasPermission("legit.xray")) {
		            				
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
