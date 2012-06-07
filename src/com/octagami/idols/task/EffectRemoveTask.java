package com.octagami.idols.task;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.util.Util;


public class EffectRemoveTask implements Runnable {
	
	private IdolsPlugin plugin = null;
  
    public EffectRemoveTask(IdolsPlugin plugin) {
        
    	this.plugin = plugin;
    }


    @SuppressWarnings("deprecation")
	@Override
    public void run() {
    	
    	HashMap<String, HashMap<String, Date>> playerCooldowns = Idols.getAllCooldowns();
    	
    	for (String player : playerCooldowns.keySet()) {
    		
    		HashMap<String, Date> cooldowns = playerCooldowns.get(player);
    		
    		if (!cooldowns.containsKey("jobs-bonus")) 
    			continue;
    			
    		Date jobsExpEffectEndTime = cooldowns.get("jobs-bonus");
    		
    		long cooldown = Util.getTimeRemaining(new Date(), jobsExpEffectEndTime, TimeUnit.SECONDS);
    		
    		if (cooldown <= 0) {
    			
    			Player onlinePlayer = plugin.getServer().getPlayer(player);
    			
    			if (onlinePlayer != null) {
    				onlinePlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
    				onlinePlayer.sendMessage(ChatColor.GOLD + "The learning effect fades");
    			}
    			
    			cooldowns.remove("jobs-bonus");
	            
    		}
    		
    		
    	}
    }
    
   
    
}
