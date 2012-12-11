package com.octagami.idols.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlayer;
import com.octagami.idols.IdolsPlayerManager;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.util.EntityEquipment;
import com.octagami.idols.util.PhantomItem;
import com.octagami.idols.util.Namer;
import com.octagami.idols.util.Util;

public class EntityListener implements Listener {

	private final IdolsPlugin plugin;
	
	public EntityListener(IdolsPlugin plugin) {

		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		
		if(!plugin.isEnabled()) 
			return;
		
//		if (plugin.getIdolsConfig().DEBUG)
//			plugin.getLogger().info("EntityDamageEvent - Original damage: " + event.getDamage());

		if ( !(event.getEntity() instanceof Player) )
			return;

		Player player = (Player)event.getEntity();

		if (event.getCause().equals(DamageCause.FALL)) {

			if (IdolsPlayerManager.getPlayer(player).canBeFallImmune() &&
				IdolsPlayerManager.getPlayer(player).isFallImmune() &&
				event.getDamage() >= player.getHealth()) {

				player.sendMessage(ChatColor.RED + "You miraculously avoid falling to your death!");
				
				Idols.emote(player, " miraculously avoids falling to his death!");
					
				event.setCancelled(true);

			}

		}else if (event.getCause().equals(DamageCause.FIRE_TICK)) {
			
			if (IdolsPlayerManager.getPlayer(player).isFireResist()) {
				
				IdolsPlayerManager.getPlayer(player).enableFireResist(false);
				
				event.setCancelled(true);
			}

		}

	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageMonitor(EntityDamageEvent event) {
		
		if(!plugin.isEnabled()) 
			return;

		if ( !(event.getEntity() instanceof Player) )
			return;
		
		final Player player = (Player)event.getEntity();
		
//		if (plugin.getIdolsConfig().DEBUG) {
//
//			final LivingEntity entity = (LivingEntity)event.getEntity();
//			final int finalDamage =  event.getDamage();
//			final int entityHealth = entity.getHealth();
//			final EntityDamageEvent aEvent = event;
//			
//			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//
//				public void run() {
//					
//					double armorMultiplier = DamageCalculator.getArmourMultiplier(aEvent, player);
//					double epfMultiplier =  DamageCalculator.getEPFMultiplier(aEvent, player);
//					
//					int damageAfterArmor = (int)((double)finalDamage * armorMultiplier * epfMultiplier);
//							
//					plugin.getLogger().info("EntityDamageEvent - Modified damage: " + finalDamage);
//					plugin.getLogger().info("EntityDamageEvent - Armor multiplier: " + armorMultiplier);
//					plugin.getLogger().info("EntityDamageEvent - Enchant multiplier: " + epfMultiplier);
//					plugin.getLogger().info("EntityDamageEvent - After armor: " + damageAfterArmor);
//					plugin.getLogger().info("EntityDamageEvent - Entity health: " +entityHealth);
//					plugin.getLogger().info("EntityDamageEvent - Final health: " + entity.getHealth());
//					
//					if (entity.getHealth() == entityHealth - damageAfterArmor)
//						plugin.getLogger().info("PASS");
//					else
//						plugin.getLogger().info("FAIL");
//						
//					
//					plugin.getLogger().info("");
//				}
//
//			}, 0L);
//				
//		}
		
		if (event.getCause().equals(DamageCause.ENTITY_ATTACK) || event.getCause().equals(DamageCause.PROJECTILE)) {

			IdolsPlayer iPlayer = IdolsPlayerManager.getPlayer(player);
			
			if (iPlayer.canBerserk()) {

				int nextInt = Idols.random.nextInt(100);

				if (nextInt < plugin.getIdolsConfig().berserkFrequency) {

					if (!iPlayer.isBerserk()) {

						player.sendMessage(ChatColor.RED + "You go into a berserker rage!");

						Idols.emote(player, " goes into a berserker rage!");

					} else {
						player.sendMessage(ChatColor.RED + "You berserker rage has been extended!");
					}

					iPlayer.enableBerserk(true);
				}

			}
			
			if (iPlayer.canInnateArmor()) {
				
				///final CraftPlayerInventory cInv = (CraftPlayer)player.getInventory();
				final CraftPlayer cPlayer = (CraftPlayer)player;
				
				final PlayerInventory inventory = cPlayer.getInventory();

				final ItemStack helmet = inventory.getHelmet();
				final ItemStack chestplate = inventory.getChestplate();
				final ItemStack leggings = inventory.getLeggings();
				final ItemStack boots = inventory.getBoots();
				
				boolean innateArmorTriggered = false;
				
				if (helmet == null) {
					
					ItemStack fakeHelmet = new ItemStack(iPlayer.getInnateArmorLevel("helmet"));
					fakeHelmet = Namer.setName(fakeHelmet, PhantomItem.getLabel(fakeHelmet.getType()));
					inventory.setHelmet(fakeHelmet);
					innateArmorTriggered = true;
				}
				
				if (chestplate == null) {
					ItemStack fakeChestplate = new ItemStack(iPlayer.getInnateArmorLevel("chestplate"));
					fakeChestplate = Namer.setName(fakeChestplate, PhantomItem.getLabel(fakeChestplate.getType()));
					inventory.setChestplate(fakeChestplate);
					innateArmorTriggered = true;
				}
				
				if (leggings == null) {
					ItemStack fakeLeggings = new ItemStack(iPlayer.getInnateArmorLevel("leggings"));
					fakeLeggings = Namer.setName(fakeLeggings, PhantomItem.getLabel(fakeLeggings.getType()));
					inventory.setLeggings(fakeLeggings);
					innateArmorTriggered = true;
				}
				
				if (boots == null) {
					ItemStack fakeBoots = new ItemStack(iPlayer.getInnateArmorLevel("boots"));
					fakeBoots = Namer.setName(fakeBoots, PhantomItem.getLabel(fakeBoots.getType()));
					inventory.setBoots(fakeBoots);
					innateArmorTriggered = true;
				}
				
				if (innateArmorTriggered) {

					IdolsPlayerManager.getPlayer(player).enableInnateArmor(true);

					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

						public void run() {

							boolean innateArmorFade = false;

							if (helmet == null) {
								inventory.setHelmet(new ItemStack(Material.AIR));
								innateArmorFade = true;
							}
							if (chestplate == null) {
								inventory.setChestplate(new ItemStack(Material.AIR));
								innateArmorFade = true;
							}
							if (leggings == null) {
								inventory.setLeggings(new ItemStack(Material.AIR));
								innateArmorFade = true;
							}
							if (boots == null) {
								inventory.setBoots(new ItemStack(Material.AIR));
								innateArmorFade = true;
							}

							if (innateArmorFade)
								IdolsPlayerManager.getPlayer(player).enableInnateArmor(false);
						}

					}, plugin.getIdolsConfig().innateArmorDuration);

				}

			}

		} else if (event.getCause().equals(DamageCause.FIRE_TICK)) {

			if (IdolsPlayerManager.getPlayer(player).canBeFireResistant() && !IdolsPlayerManager.getPlayer(player).isFireResist())

				if (plugin.getIdolsConfig().DEBUG)
					plugin.getLogger().info("Took fire damage. Enabling immunity to negate next fire tick");

			IdolsPlayerManager.getPlayer(player).enableFireResist(true);
		}

	} 
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		
		if (!plugin.isEnabled()) return;
		
		if (event.getSlotType() == SlotType.ARMOR) {
			
			if (event.getWhoClicked() instanceof Player) {
				
				final Player player = (Player)event.getWhoClicked();
				
				if (IdolsPlayerManager.getPlayer(player).isInnateArmored()) {
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

						public void run() {

							PlayerInventory inventory = player.getInventory();

							for (int i = 0; i <= 8; i++) {

								ItemStack item = inventory.getItem(i);
								if (item != null && Namer.itemIsNamed(item, PhantomItem.getLabel(item.getType()))) {
									inventory.setItem(i, new ItemStack(Material.AIR));
								}
								
							}
							
						}
						
					}, 0L);

					event.setCancelled(true);
				}
					
			}

		}
			
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
					
					if (defender instanceof Player) {
						if (!plugin.getIdolsConfig().headshotAllowInPvp)
							return;
					} else if (!plugin.getIdolsConfig().isHeadShotMob(defender.getType())) {
						return;
					}
						
					Double eyeHeight = defender.getEyeHeight(true);
					Location entityLoc = defender.getLocation();
					
					Location arrowLoc = projectile.getLocation();
					entityLoc.setY(entityLoc.getY() + eyeHeight + 0.6);
					
					if (arrowLoc.getY() <= entityLoc.getY() + plugin.getIdolsConfig().headshotAboveThreshold && 
						arrowLoc.getY() >= entityLoc.getY() - plugin.getIdolsConfig().headshotBelowThreshold ) {
						
						double newDamageFloat = (double)event.getDamage() * plugin.getIdolsConfig().headshotCritMultiplier;
						
						int newDamage = (int)newDamageFloat;
						
//						if (plugin.getIdolsConfig().DEBUG) {
//							
//							String arrowZ = String.format("%.2f", arrowLoc.getY());
//							String headZ = String.format("%.2f", entityLoc.getY());
//							attacker.sendMessage("Arrow: " + arrowZ  + " MobHead: " + headZ); 
//						}

						attacker.sendMessage(ChatColor.RED + "HEADSHOT!");

						if (defender.getType().equals(EntityType.ZOMBIE) || 
								defender.getType().equals(EntityType.SKELETON) ||
								defender.getType().equals(EntityType.CREEPER)) {
							
							int nextInt = Idols.random.nextInt(100);

							if (nextInt < plugin.getIdolsConfig().headshotHeadDropChance) {
								
								ItemStack head = new ItemStack(Material.SKULL_ITEM);
								
								if (defender.getType().equals(EntityType.SKELETON))
									head.setDurability((short)0);
								else if (defender.getType().equals(EntityType.ZOMBIE))
									head.setDurability((short)2);
								else if (defender.getType().equals(EntityType.CREEPER))
									head.setDurability((short)4);
								
								attacker.sendMessage(ChatColor.RED + "Your well aimed shot decapitates your foe!");
								defender.getWorld().dropItemNaturally(defender.getLocation(), head);
								defender.setHealth(0); 
							}
							
						}
						
						event.setDamage(newDamage);
						
//						if (plugin.getIdolsConfig().DEBUG)
//							plugin.getLogger().info("EntityDamageEvent - Modified damage: " + newDamage);
						
						Idols.showDamage(attacker, newDamage, false);

					} else {
						
						Idols.showDamage(attacker, event.getDamage(), false);
					}
					
			}

		} else if (event.getDamager() instanceof Player) {
			
			Player attacker = (Player) event.getDamager();
			
			if (IdolsPlayerManager.getPlayer(attacker).isBerserk()) {
				
				if ((event.getEntity() instanceof Player) && (!plugin.getIdolsConfig().berserkAllowInPvp))
					return;
				
				double newDamageFloat = (double)event.getDamage() * plugin.getIdolsConfig().berserkCritMultiplier;
				
				int newDamage = (int)newDamageFloat;

				event.setDamage(newDamage);
				
				Idols.showDamage(attacker, newDamage, false);
				
			} else {
				
				Idols.showDamage(attacker, event.getDamage(), false);
			}
			
		}	
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		
		if(!plugin.isEnabled()) 
			return;
		
		LivingEntity livingEntity = event.getEntity();
		
		if (livingEntity.getType().equals(EntityType.ZOMBIE) || 
			livingEntity.getType().equals(EntityType.SKELETON) ||
			livingEntity.getType().equals(EntityType.CREEPER)) {
			
			if (!(livingEntity.getKiller() instanceof Player))
				return;
			
			Player player = livingEntity.getKiller();
			IdolsPlayer iPlayer = IdolsPlayerManager.getPlayer(player);
			
			if (!livingEntity.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK))
				return; 
			
			if (!Util.isWeapon(player.getItemInHand()))
				return;
			
			List<ItemStack> drops = event.getDrops();
			
			if (iPlayer.canBerserk() && iPlayer.isBerserk()) {
				
				int nextInt = Idols.random.nextInt(100);

				if (nextInt < plugin.getIdolsConfig().berserkHeadDropChance) {
					
					ItemStack head = new ItemStack(Material.SKULL_ITEM);
					
					if (livingEntity.getType().equals(EntityType.SKELETON))
						head.setDurability((short)0);
					else if (livingEntity.getType().equals(EntityType.ZOMBIE))
						head.setDurability((short)2);
					else if (livingEntity.getType().equals(EntityType.CREEPER))
						head.setDurability((short)4);
					
					player.sendMessage(ChatColor.RED + "You channel your rage and decapitate your foe!");
					drops.add(head);
				}
				
			}
			
			if (livingEntity.getType().equals(EntityType.CREEPER))
				return;
			
			if (iPlayer.canArmorBreaker()) {
				
				int nextInt = Idols.random.nextInt(100);

				if (nextInt < plugin.getIdolsConfig().armorBreakerChance) {
					
					ArrayList<ItemStack> possibleDrops = new ArrayList<ItemStack>();
					
					ItemStack mobHelmet = EntityEquipment.getHelmet(livingEntity);
					ItemStack mobChestplate = EntityEquipment.getChestplate(livingEntity);
					ItemStack mobLeggings = EntityEquipment.getPants(livingEntity);
					ItemStack mobBoots = EntityEquipment.getBoots(livingEntity);
					
					if (mobHelmet != null) {
						possibleDrops.add(mobHelmet);
					}
					
					if (mobChestplate != null) {
						possibleDrops.add(mobChestplate);
					}
					
					if (mobLeggings != null) {
						possibleDrops.add(mobLeggings);
					}	
					
					if (mobBoots != null) {
						possibleDrops.add(mobBoots);
					}
					
					if (possibleDrops.size() > 0) {
						
						int dropIndex = Idols.random.nextInt(possibleDrops.size());
						
						for (ItemStack drop : drops){
							if (Util.isArmor(drop))
								drops.remove(drop);
						}
						
						drops.add(possibleDrops.get(dropIndex));
						player.sendMessage(ChatColor.RED + "You shatter the armor of your foe!"); 
						return;
					}
					
				}

			}

			if (iPlayer.canDisarm() && (EntityEquipment.getWeapon(livingEntity) != null)) {
				
				int nextInt = Idols.random.nextInt(100);

				if (nextInt < plugin.getIdolsConfig().disarmChance) {

					ItemStack weapon = EntityEquipment.getWeapon(livingEntity);
					
					if (weapon.getType() == Material.BOW)
						return;
					
					for (ItemStack drop : drops){
						if (Util.isWeapon(drop))
							drops.remove(drop);
					}
					drops.add(weapon);
					
					player.sendMessage(ChatColor.RED + "You disarm your foe!");
					return;
				}
			}

		}
		
	}

}
