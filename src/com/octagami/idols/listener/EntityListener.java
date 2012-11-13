package com.octagami.idols.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.util.Util;

public class EntityListener implements Listener {

	private final IdolsPlugin plugin;
	
	public EntityListener(IdolsPlugin plugin) {

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {

		if (!plugin.isEnabled())
			return;

		if (!(event.getEntity() instanceof LivingEntity))
			return;

		if (!event.getSpawnReason().equals(SpawnReason.SPAWNER))
			return;
			
		if (!plugin.getIdolsConfig().areSpawnersDisabled())
			return;

		event.setCancelled(true);

	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		
		if (!plugin.isEnabled())
			return;

		if (event.getDamager() instanceof Projectile) {
			
			Projectile projectile = (Projectile) event.getDamager();
			
			if ((projectile.getShooter() instanceof Player) &&
				(event.getEntity() instanceof LivingEntity)) {
				
					Player attacker = (Player)projectile.getShooter();
					
					if (!IdolsPlayerManager.getPlayer(attacker).canHeadshot())
						return;
					
					LivingEntity defender = (LivingEntity)event.getEntity();
					
					if ((defender instanceof Player) && (!plugin.getIdolsConfig().headshotAllowInPvp))
						return;
					
					Double eyeHeight = defender.getEyeHeight(true);
					Location entityLoc = defender.getLocation();
					
					Location arrowLoc = projectile.getLocation();
					entityLoc.setY(entityLoc.getY() + eyeHeight + 0.6);
					
					if (arrowLoc.getY() <= entityLoc.getY() + plugin.getIdolsConfig().headshotAboveThreshold && 
						arrowLoc.getY() >= entityLoc.getY() - plugin.getIdolsConfig().headshotBelowThreshold ) {
						
						double newDamageFloat = (double)event.getDamage() * plugin.getIdolsConfig().headshotCritMultiplier;
						
						int newDamage = (int)newDamageFloat;
						
						if (plugin.getIdolsConfig().DEBUG) {
							
							String arrowZ = String.format("%.2f", arrowLoc.getY());
							String headZ = String.format("%.2f", entityLoc.getY());
							attacker.sendMessage("Arrow: " + arrowZ  + "MobHead: " + headZ); 
						}

						attacker.sendMessage(ChatColor.RED + "HEADSHOT");
						
						if (IdolsPlayerManager.getPlayer(attacker).canSeeDamage())
							attacker.sendMessage(ChatColor.RED + "You have crit for " + Integer.toString(newDamage) + " damage!" );
						
						event.setDamage(newDamage);
						
					} else {
						
						if (IdolsPlayerManager.getPlayer(attacker).canSeeDamage())
							attacker.sendMessage(ChatColor.GOLD + "You have hit for " + Integer.toString(event.getDamage()) + " damage!" );
					}
			}

		} else if (event.getDamager() instanceof Player) {
			
			Player player = (Player) event.getDamager();
			
			if (IdolsPlayerManager.getPlayer(player).isBerserk()) {
				
				if ((event.getEntity() instanceof Player) && (!plugin.getIdolsConfig().berserkAllowInPvp))
					return;
				
				double newDamageFloat = (double)event.getDamage() * plugin.getIdolsConfig().berserkCritMultiplier;
				
				int newDamage = (int)newDamageFloat;
				
				if (IdolsPlayerManager.getPlayer(player).canSeeDamage())
					player.sendMessage(ChatColor.RED + "You have crit for " + Integer.toString(newDamage) + " damage!" );
				
				event.setDamage(newDamage);
				
			} else {
				
				if (IdolsPlayerManager.getPlayer(player).canSeeDamage())
					player.sendMessage(ChatColor.GOLD + "You have hit for " + Integer.toString(event.getDamage()) + " damage!" );
			}
			
		}	
		
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		
		if(!plugin.isEnabled()) 
			return;

		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();

			DamageCause cause = event.getCause();

			if (cause.equals(DamageCause.FALL)) {

				if (IdolsPlayerManager.getPlayer(player).canBeFallImmune() &&
					IdolsPlayerManager.getPlayer(player).isFallImmune() &&
					event.getDamage() >= player.getHealth()) {

					player.sendMessage(ChatColor.GOLD + "A committed builder fears no heights!");
					
					if (plugin.getIdolsConfig().emotesEnabled) {
						
						for (Player y : player.getWorld().getPlayers()) {
		                    if (y != player && Util.isNear(player.getLocation(), y.getLocation(), plugin.getIdolsConfig().emoteDistance)) {
		                        y.sendMessage(ChatColor.RED + player.getName() + " miraculously avoids falling to his death!");
		                    }
		                }
					}
					
					event.setCancelled(true);

				}

			} 
			
		}
		
	}
		
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageMonitor(EntityDamageEvent event) {
		
		if(!plugin.isEnabled()) 
			return;
		
		if (event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();

			DamageCause cause = event.getCause();

			if (cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.PROJECTILE)) {
				
				if (IdolsPlayerManager.getPlayer(player).canBerserk()) {
					
					int nextInt = Idols.random.nextInt(100);
					
					if (nextInt < plugin.getIdolsConfig().berserkFrequency) {
					    
						if (!IdolsPlayerManager.getPlayer(player).isBerserk()) {
							player.sendMessage(ChatColor.GOLD + "You go into a berserker rage!");
							
							if (plugin.getIdolsConfig().emotesEnabled) {
								
								for (Player y : player.getWorld().getPlayers()) {
				                    if (y != player && Util.isNear(player.getLocation(), y.getLocation(), plugin.getIdolsConfig().emoteDistance)) {
				                        y.sendMessage(ChatColor.RED + player.getName() + " goes into a berserker rage!");
				                    }
				                }
							}
							
							
						} else {
							player.sendMessage(ChatColor.GOLD + "You berserker rage has been extended!");
						}
						
						IdolsPlayerManager.getPlayer(player).enableBerserk(true);
						
					}
					
				}
					
			} else if (cause.equals(DamageCause.FIRE_TICK)) {
				
				if (IdolsPlayerManager.getPlayer(player).canBeFireResistant()) {
					
					if (IdolsPlayerManager.getPlayer(player).isFireResist()) {
						event.setCancelled(true);
						IdolsPlayerManager.getPlayer(player).enableFireResist(false);
					}else {
						
						if (plugin.getIdolsConfig().DEBUG)
							plugin.getLogger().info("Took fire damage. Enabling immunity to negate next fire tick");
							
						IdolsPlayerManager.getPlayer(player).enableFireResist(true);
					}

				}
				
			}
			
		}
		
	} 

}
