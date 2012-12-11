package com.octagami.idols;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.octagami.idols.exceptions.IdolsNotLoadedException;
import com.octagami.idols.task.RadarTask;

public class IdolsPlayer {
	
	private Player player;

	private boolean speed = false;
	
	private boolean	fallImmunity = false;
	private int fallImmunityTimerTaskId = -1;
	
	private boolean	berserk = false;
	private boolean	innateArmor = false;
	private int berserkTimerTaskId = -1;
	
	private boolean	radar = false;
	private int radarTimerTaskId = -1;
	
	private boolean	fireResist = false;
	
	private int diamondBreakCounter = 0;
	
	private int totalDiamondsBroken = 0;

	public IdolsPlayer(Player player) {

		this.player= player;
	}
	
	public int getDiamondBreakCounter() {
		
		return diamondBreakCounter;
	}
	
	public void resetDiamondCounter() {
		
		diamondBreakCounter = 0;
	}
	
	public void incrementDiamondCounter() {
		
		diamondBreakCounter++;
		totalDiamondsBroken++;
	}
	
	public int getTotalDiamondsBroken() {
		
		return totalDiamondsBroken;
	}
	
	public void hasQuit() {

		cancelTasks();
		
		player.setWalkSpeed((float)0.2);
		this.speed = false;
		
		this.innateArmor = false;
		this.fallImmunity = false;
		this.berserk = false;
		this.fireResist = false;
		this.radar = false;
	}
	
	public void hasDied() {
		
		cancelTasks();
		
		this.innateArmor = false;
		this.fallImmunity = false;
		this.berserk = false;
		this.fireResist = false;
		this.radar = false;
	}
	
	public void cancelTasks() {
		
		IdolsPlugin plugin;
		try {
			plugin = Idols.getPlugin();
			
			if (fallImmunityTimerTaskId >= 0) {
				
				plugin.getServer().getScheduler().cancelTask(fallImmunityTimerTaskId);
			}
			
			if (berserkTimerTaskId >= 0) {
				
				plugin.getServer().getScheduler().cancelTask(berserkTimerTaskId);
			}
			
			if (radarTimerTaskId >= 0) {
				
				plugin.getServer().getScheduler().cancelTask(radarTimerTaskId);
			}
			
		} catch (IdolsNotLoadedException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean canUseSpeed() {
		
		return player.hasPermission("idols.speed");
	}
	
	public boolean isSpeedEnabled() {
		
		return this.speed;
	}	
	
	public void enableSpeed(boolean value) {
		
		if (value) {
			
			IdolsPlugin plugin;
			
			try {
				plugin = Idols.getPlugin();
			} catch (IdolsNotLoadedException e1) {
				e1.printStackTrace();
				return;
			}
			
			float speedMultiplier = (float)plugin.getIdolsConfig().walkSpeedRank1;
					
			if (player.hasPermission("idols.speed.rank2")) {
				speedMultiplier = (float)plugin.getIdolsConfig().walkSpeedRank2;
			}
			
			float speed = (float)(0.2f * speedMultiplier);

			player.setWalkSpeed(speed);
			this.speed = true;
			
		} else {
			
			player.setWalkSpeed((float)0.2);
			this.speed = false;
		}

	}	
	
	public boolean canSeeDamage() {
		
		return player.hasPermission("idols.display.damage");
	}
	
	public boolean canBeFireResistant() {
		
		return player.hasPermission("idols.fireresist");
	}
	
	public boolean isFireResist() {
		
		return this.fireResist;
	}
	
	public boolean canHeadshot() {
		
		return player.hasPermission("idols.headshot");
	}
	
	public boolean canDisarm() {
		
		return player.hasPermission("idols.disarm");
	}

	public boolean canArmorBreaker() {

		return player.hasPermission("idols.armorbreaker");
	}	
	
	public void enableFireResist(boolean value) {
		
		this.fireResist = value;
	}
	
	public boolean canBeFallImmune() {
		
		return player.hasPermission("idols.fallimmunity");
	}
	
	public boolean isFallImmune() {
		
		return this.fallImmunity;
	}

	public void enableFallImmunity(boolean value) {

		this.fallImmunity = value;
		
		if (value) {
			
			IdolsPlugin plugin;
			try {
				
				plugin = Idols.getPlugin();
				
				// If there is an existing timer set, cancel it
				if (fallImmunityTimerTaskId >= 0) {
					plugin.getServer().getScheduler().cancelTask(fallImmunityTimerTaskId);
					//plugin.getLogger().info("Canceling old fall immunity timer, id = " + fallImmunityTimerTaskId);
				}
				
				// Create and run a new timer which will disable immunity when it executes
				fallImmunityTimerTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
		            public void run() { 
		            	
		            	IdolsPlayerManager.getPlayer(player).enableFallImmunity(false);
		            	fallImmunityTimerTaskId = -1;
		            	
		            	if ( player != null) {
		            		//player.sendMessage("Fall immunity has ended");
		            	}
		            }  
		            
		        }, plugin.getIdolsConfig().fallImmunityDuration * 20);	
				
				//plugin.getLogger().info("Created new fall immunity timer, id = " + fallImmunityTimerTaskId);
				
			} catch (IdolsNotLoadedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public boolean canInnateArmor() {
		
		return player.hasPermission("idols.armor");
	}
	
	public boolean isInnateArmored() {
		
		return this.innateArmor;
	}
	
	public void enableInnateArmor(boolean value) {

		this.innateArmor = value;
	}
	
	public Material getInnateArmorLevel(String slot) {

		if (player.hasPermission("idols.armor.diamond")) {
			
			if (slot.equals("helmet"))
				return Material.DIAMOND_HELMET;
			else if (slot.equals("chestplate"))
				return Material.DIAMOND_CHESTPLATE;
			else if (slot.equals("leggings"))
				return Material.DIAMOND_LEGGINGS;
			else if (slot.equals("boots"))
				return Material.DIAMOND_BOOTS;
		}
		else if (player.hasPermission("idols.armor.iron")) {
			
			if (slot.equals("helmet"))
				return Material.IRON_HELMET;
			else if (slot.equals("chestplate"))
				return Material.IRON_CHESTPLATE;
			else if (slot.equals("leggings"))
				return Material.IRON_LEGGINGS;
			else if (slot.equals("boots"))
				return Material.IRON_BOOTS;
		}
		else if (player.hasPermission("idols.armor.chain")) {
			
			if (slot.equals("helmet"))
				return Material.CHAINMAIL_HELMET;
			else if (slot.equals("chestplate"))
				return Material.CHAINMAIL_CHESTPLATE;
			else if (slot.equals("leggings"))
				return Material.CHAINMAIL_LEGGINGS;
			else if (slot.equals("boots"))
				return Material.CHAINMAIL_BOOTS;
		}
		else if (player.hasPermission("idols.armor.leather")) {
			
			if (slot.equals("helmet"))
				return Material.LEATHER_HELMET;
			else if (slot.equals("chestplate"))
				return Material.LEATHER_CHESTPLATE;
			else if (slot.equals("leggings"))
				return Material.LEATHER_LEGGINGS;
			else if (slot.equals("boots"))
				return Material.LEATHER_BOOTS;
		}
	
		return Material.AIR;
	}
	
	public boolean canBerserk() {
		
		return player.hasPermission("idols.berserk");
	}
	
	public boolean isBerserk() {
		
		return this.berserk;
	}
	
	public void enableBerserk(boolean value) {

		this.berserk = value;
		
		if (value) {
			
			IdolsPlugin plugin;
			try {
				
				plugin = Idols.getPlugin();
				
				// If there is an existing timer set, cancel it
				if (berserkTimerTaskId >= 0) {
					plugin.getServer().getScheduler().cancelTask(berserkTimerTaskId);
					//plugin.getLogger().info("Canceling old berserk cooldown timer, id = " + berserkTimerTaskId);
				}
				
				// Create and run a new timer which will disable berserk when it executes
				berserkTimerTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
		            public void run() { 
		            	
		            	IdolsPlayerManager.getPlayer(player).enableBerserk(false); 
		            	berserkTimerTaskId = -1;
		            	
		            	if ( player != null) {
			            	player.sendMessage(ChatColor.RED + "Your berserker rage has subsided.");
		            	} 	
		            }  
		            
		        }, plugin.getIdolsConfig().berserkDuration * 20);
				
				//plugin.getLogger().info("Created new berserk cooldown timer, id = " + berserkTimerTaskId);
				
			} catch (IdolsNotLoadedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public boolean canRadar() {
		
		return player.hasPermission("idols.radar");
	}
	
	public boolean isRadarEnabled() {
		
		return this.radar;
	}
	
	public void enableRadar(boolean value, int blockId) {

		IdolsPlugin plugin;
		
		try {
			plugin = Idols.getPlugin();
		} catch (IdolsNotLoadedException e1) {
			e1.printStackTrace();
			return;
		}
		
		this.radar = value;
		
		if (value) {
			
			// If there is an existing timer set, cancel it
			if (radarTimerTaskId >= 0) {
				plugin.getServer().getScheduler().cancelTask(radarTimerTaskId);
				//plugin.getLogger().info("Canceling old radar cooldown timer, id = " + radarTimerTaskId);
			}
			
			int distance = 0;
			int frequency = 10;
			int duration = 300;
			int cooldown = 3600;

			if (plugin.getIdolsConfig() != null) {
				
				distance = plugin.getIdolsConfig().radarDistance;
				frequency = plugin.getIdolsConfig().radarFrequency;
				duration = plugin.getIdolsConfig().radarDuration;
				cooldown = plugin.getIdolsConfig().radarCooldown;
				
			}
			
			RadarTask radarTask = new RadarTask(plugin, player.getName(), blockId, distance, duration);
			
			radarTimerTaskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, radarTask, 60L, frequency * 20);
			radarTask.setTaskID(radarTimerTaskId);
			
//			// Create and run a new timer which will disable radar when it executes
//			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//				
//	            public void run() { 
//	            	
//	            	try {
//	            		
//						Idols.getPlugin().getServer().getScheduler().cancelTask(radarTimerTaskId);
//						radarTimerTaskId = -1;
//						
//						if (player != null) {
//							player.sendMessage(ChatColor.GOLD + "Your mining intuition fades");
//						}
//						
//					} catch (IdolsNotLoadedException e) {
//						e.printStackTrace();
//					}
//	            	
//	            }  
//	            
//	        }, duration * 20);
			
			Idols.setAbilityCooldown(player.getName(), "radar", cooldown);
				
		} else {
			
			if (radarTimerTaskId >= 0) {
				
				plugin.getServer().getScheduler().cancelTask(radarTimerTaskId);
				//plugin.getLogger().info("Manually canceling radar cooldown timer, id = " + radarTimerTaskId);
				player.sendMessage(ChatColor.RED + "You disregard your mining intuition");
				
			} else {
				
				player.sendMessage(ChatColor.RED + "The radar ability is not currently active");
			}
			
		}
		
	}

}
