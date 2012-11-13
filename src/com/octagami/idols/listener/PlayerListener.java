package com.octagami.idols.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

	private final IdolsPlugin	plugin;
	
	public PlayerListener(IdolsPlugin plugin) {

		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDeath(PlayerDeathEvent event) {
		
		IdolsPlayerManager.playerDied(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {

    	if (!plugin.isEnabled()) return;
    	
         switch (event.getAction()) {
         
			 case RIGHT_CLICK_AIR:
			 case RIGHT_CLICK_BLOCK:
				 
			 if (event.getPlayer().getItemInHand().getType().equals(Material.POTION)) {
				 
				 Player player = event.getPlayer();
				 
				 final short potionID = player.getItemInHand().getDurability();
				 
				 PotionEffect effect = null;
				 int duration = 0;
				 
				 if (potionID == 27) {
					 
					  duration = plugin.getIdolsConfig().smoothDuration;
					  effect = new PotionEffect(PotionEffectType.NIGHT_VISION, duration * 20, 0);
					  
				 } else if (potionID == 29) {
					 
					 duration = plugin.getIdolsConfig().suaveDuration;
					  effect = new PotionEffect(PotionEffectType.NIGHT_VISION, duration * 20, 1);
					  

				 } else if (potionID == 30 || potionID == 31) {
					 
					 duration = plugin.getIdolsConfig().debonairDuration;
					  effect = new PotionEffect(PotionEffectType.NIGHT_VISION, duration * 20, 2);

				 }
				 
				 if (effect != null) {

					 final String playerName = player.getName();
					 final PotionEffect finalEffect = effect;
					 final int slot = player.getInventory().getHeldItemSlot();
					 final int potionCount =  player.getInventory().all(new ItemStack(Material.POTION, 1, potionID)).size();
							 
					 plugin.getLogger().info(playerName + " is trying to use a type " + potionID + " potion and has " + potionCount + " in his inventory");
						 
					 plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		 
		 	            public void run() { 
		 	            	
		 	            	Player thePlayer = plugin.getServer().getPlayer(playerName);
		 	            	
		 	            	if (thePlayer != null) {
		 	            		
		 	            		if (thePlayer.getItemInHand().getType().equals(Material.GLASS_BOTTLE) &&
		 	            		    thePlayer.getInventory().getHeldItemSlot() == slot) {
		 	            			
		 	            			if (thePlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION))
		 	            				thePlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
		 	            			
		 	            			finalEffect.apply(thePlayer);
		 	            			thePlayer.sendMessage(ChatColor.GOLD + "You feel ready to learn");
		 	            			
		 	            			if (thePlayer.getInventory().all(new ItemStack(Material.POTION, 1, potionID)).size() >= potionCount)
		 	            				plugin.getLogger().warning(playerName + " used a type " + potionID + " potion, but it was not removed");
		 	            			
		 	            			plugin.getLogger().info(playerName + " successfully used a type " + potionID + " potion and has " + 
		 	            			thePlayer.getInventory().all(new ItemStack(Material.POTION, 1, potionID)).size() + " remaining");
		 	            			
		 	            		} else {
		 	            			
		 	            			plugin.getLogger().info(playerName + " failed to use a type " + potionID + " potion");
		 	            		}
		 	            		
		 	            	} 
		 	            	
		 	            }  
		 	            
		 	        }, plugin.getIdolsConfig().potionPollInterval * 20);
					 
				 } 
				 
			 }
        	 
			 	break;
			 	
			 default:
				 break;
         }
         
    }

}
