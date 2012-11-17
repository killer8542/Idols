package com.octagami.idols;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class IdolsConfig {
	
	private IdolsPlugin plugin;
	
	public boolean DEBUG = false;
	
	@SuppressWarnings("unused")
	private String configVersion = "";
	
	public final String GENERAL_CONFIG_NAME = "generalConfig.yml";
	public final String ENCHANT_CONFIG_NAME = "enchantsConfig.yml";
	
	public int innateArmorDuration = 5;
	public boolean berserkAllowInPvp = false;
	public int berserkFrequency = 10;
	public int berserkDuration = 30;
	public double berserkCritMultiplier = 1.25;
	public int berserkHeadDropChance = 1;
	
	public int disarmChance = 50;
	public int armorBreakerChance = 50;
	
    
	public int fallImmunityDuration = 30;
	
	public int potionPollInterval = 4;
	
	public int smoothCost = 100;
	public int smoothDuration = 1800;
	
	public int suaveCost = 250;
	public int suaveDuration = 1800;
	
	public int debonairCost = 500;
	public int debonairDuration = 900;
	
	public int radarDistance = 5;
	
	public int radarFrequency = 10;
	
	public int radarDuration = 300;
	
	public int radarCooldown = 1800;
	
	public int homieCooldown = 3600;
	
	public int alertDelay = 8;
	
	public boolean headshotAllowInPvp = false;
	public double headshotCritMultiplier = 1.25;
	public double headshotAboveThreshold = 0.5;
	public double headshotBelowThreshold = 0.2;
	public int headshotHeadDropChance = 1;
	private ArrayList<EntityType> headshotMobs = new ArrayList<EntityType>();
    
    public boolean emotesEnabled = true;
    
	public int emoteDistance = 10;

    public IdolsConfig(IdolsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void reload() {
    	
    	loadSettings();
    }
    
    public void loadSettings(){
    	
    	if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    	
        File f = new File(plugin.getDataFolder(), GENERAL_CONFIG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to create " + GENERAL_CONFIG_NAME + "! No settings were loaded!");
                return;
            }
        }
        YamlConfiguration conf = new YamlConfiguration();
        conf.options().pathSeparator('/');
        conf.options().header(new StringBuilder().append("General configuration").toString());
        try {
            conf.load(f);
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("==================== " + plugin.getName() + " ====================");
            plugin.getServer().getLogger().severe("Unable to load " + GENERAL_CONFIG_NAME);
            plugin.getServer().getLogger().severe("Check your config for formatting issues!");
            plugin.getServer().getLogger().severe("No config options were loaded!");
            plugin.getServer().getLogger().severe("Error: "+e.getMessage());
            plugin.getServer().getLogger().severe("====================================================");
            return;
        }
        
        // Check if debug is on
        if (!conf.contains("debug"))
        	conf.set("debug", false);
        DEBUG = conf.getBoolean("debug");
        
        // Get config version
        if (!conf.contains("version"))
        	conf.set("version", 0.1);
        configVersion = conf.getString("version");
        
        // Alchemist
        ConfigurationSection alchemistSection = conf.getConfigurationSection("Alchemist");
        if (alchemistSection == null) alchemistSection = conf.createSection("Alchemist");
        
        if (!alchemistSection.contains("poll-interval")) {
        	alchemistSection.set("poll-interval", 4);
        }
        potionPollInterval = alchemistSection.getInt("poll-interval");
        
        ConfigurationSection potionSection  = alchemistSection.getConfigurationSection("Potion");
        if (potionSection == null) potionSection = alchemistSection.createSection("Potion");
        
        ConfigurationSection smoothSection  = potionSection.getConfigurationSection("Smooth");
        if (smoothSection == null) smoothSection = potionSection.createSection("Smooth");

        if (!smoothSection.contains("cost")) {
        	smoothSection.set("cost", 100);
        }
        smoothCost = smoothSection.getInt("cost");
        
        if (!smoothSection.contains("duration")) {
        	smoothSection.set("duration", 1800);
        }
        smoothDuration = smoothSection.getInt("duration");
        
        ConfigurationSection suaveSection  = potionSection.getConfigurationSection("Suave");
        if (suaveSection == null) suaveSection = potionSection.createSection("Suave");

        if (!suaveSection.contains("cost")) {
        	suaveSection.set("cost", 250);
        }
        suaveCost = suaveSection.getInt("cost");
        
        if (!suaveSection.contains("duration")) {
        	suaveSection.set("duration", 1800);
        }
        suaveDuration = suaveSection.getInt("duration");
       
        ConfigurationSection debonairSection  = potionSection.getConfigurationSection("Debonair");
        if (debonairSection == null) debonairSection = potionSection.createSection("Debonair");

        if (!debonairSection.contains("cost")) {
        	debonairSection.set("cost", 500);
        }
        debonairCost = debonairSection.getInt("cost");
        
        if (!debonairSection.contains("duration")) {
        	debonairSection.set("duration", 900);
        }
        debonairDuration = debonairSection.getInt("duration");
        
        // Builder
        ConfigurationSection builderSection = conf.getConfigurationSection("Builder");
        if (builderSection == null) builderSection = conf.createSection("Builder");
         
        ConfigurationSection fallImmunitySection  = builderSection.getConfigurationSection("Fall-Immunity");
        if (fallImmunitySection == null) fallImmunitySection = builderSection.createSection("Fall-Immunity");
        
        if (!fallImmunitySection.contains("duration")) {
        	fallImmunitySection.set("duration", 60);
        }
        fallImmunityDuration = fallImmunitySection.getInt("duration");
        
        // Miner
        ConfigurationSection minerSection = conf.getConfigurationSection("Miner");
        if (minerSection == null) minerSection = conf.createSection("Miner");
        
        ConfigurationSection radarSection  = minerSection.getConfigurationSection("Radar");
        if (radarSection == null) radarSection = minerSection.createSection("Radar");
        
        if (!radarSection.contains("distance")) {
        	radarSection.set("distance", 5);
        }
        radarDistance = radarSection.getInt("distance");
        
        if (!radarSection.contains("frequency")) {
        	radarSection.set("frequency", 10);
        }
        radarFrequency = radarSection.getInt("frequency");
        
        if (!radarSection.contains("duration")) {
        	radarSection.set("duration", 300);
        }
        radarDuration = radarSection.getInt("duration");
        
        if (!radarSection.contains("cooldown")) {
        	radarSection.set("cooldown", 1800);
        }
        radarCooldown = radarSection.getInt("cooldown");
        
        // Soldier
        ConfigurationSection soldierSection = conf.getConfigurationSection("Soldier");
        if (soldierSection == null) soldierSection = conf.createSection("Soldier");
        
        if (!soldierSection.contains("disarm-chance")) {
        	soldierSection.set("disarm-chance", 50);
        }
        disarmChance = soldierSection.getInt("disarm-chance");
        
        if (!soldierSection.contains("armor-breaker-chance")) {
        	soldierSection.set("armor-breaker-chance", 50);
        }
        armorBreakerChance = soldierSection.getInt("armor-breaker-chance");

        ConfigurationSection innateArmorSection  = soldierSection.getConfigurationSection("Innate-Armor");
        if (innateArmorSection == null) innateArmorSection = soldierSection.createSection("Innate-Armor");
        
        if (!innateArmorSection.contains("duration-ticks")) {
        	innateArmorSection.set("duration-ticks", 5);
        }
        innateArmorDuration = innateArmorSection.getInt("duration-ticks");
        
        ConfigurationSection berserkSection  = soldierSection.getConfigurationSection("Berserk");
        if (berserkSection == null) berserkSection = soldierSection.createSection("Berserk");
        
        if (!berserkSection.contains("allow-in-pvp")) {
        	berserkSection.set("allow-in-pvp", false);
        }
        berserkAllowInPvp = berserkSection.getBoolean("allow-in-pvp");
        
        if (!berserkSection.contains("frequency")) {
        	berserkSection.set("frequency", 10);
        }
        berserkFrequency = berserkSection.getInt("frequency");
        
        if (!berserkSection.contains("duration")) {
        	berserkSection.set("duration", 30);
        }
        berserkDuration = berserkSection.getInt("duration");
        
        if (!berserkSection.contains("crit-multiplier")) {
        	berserkSection.set("crit-multiplier", 1.25);
        }
        berserkCritMultiplier = berserkSection.getDouble("crit-multiplier");
        
        if (!berserkSection.contains("head-drop-chance")) {
        	berserkSection.set("head-drop-chance", 1);
        }
        berserkHeadDropChance = berserkSection.getInt("head-drop-chance");
        
        // Woodsman
        ConfigurationSection woodsmanSection = conf.getConfigurationSection("Woodsman");
        if (woodsmanSection == null) woodsmanSection = conf.createSection("Woodsman");

        ConfigurationSection headshotSection  = woodsmanSection.getConfigurationSection("Headshot");
        if (headshotSection == null) headshotSection = woodsmanSection.createSection("Headshot");
        
        if (!headshotSection.contains("allow-in-pvp")) {
        	headshotSection.set("allow-in-pvp", false);
        }
        headshotAllowInPvp = headshotSection.getBoolean("allow-in-pvp");
        
        if (!headshotSection.contains("crit-multiplier")) {
        	headshotSection.set("crit-multiplier", 1.25);
        }
        headshotCritMultiplier = headshotSection.getDouble("crit-multiplier");
        
        if (!headshotSection.contains("above-threshold")) {
        	headshotSection.set("above-threshold", 0.5);
        }
        headshotAboveThreshold = headshotSection.getDouble("above-threshold");
        
        if (!headshotSection.contains("below-threshold")) {
        	headshotSection.set("below-threshold", 0.2);
        }
        headshotBelowThreshold = headshotSection.getDouble("below-threshold");
        
        if (!headshotSection.contains("head-drop-chance")) {
        	headshotSection.set("head-drop-chance", 1);
        }
        headshotHeadDropChance = headshotSection.getInt("head-drop-chance");
        
        if (headshotSection.getStringList("headshot-mobs").isEmpty()) 
        	headshotSection.set("headshot-mobs", new ArrayList<String>( Arrays.asList("Zombie", "Skeleton", "Creeper") ) );
        
        for (String mobName : headshotSection.getStringList("headshot-mobs")) {
        	
            EntityType entity = EntityType.fromName(mobName);
            if (entity == null) {
                try {
                    entity = EntityType.valueOf(mobName.toUpperCase());
                } catch (IllegalArgumentException e) {}
            }
            
            if (entity != null && entity.isAlive())
                headshotMobs.add(entity);
            else
            	plugin.getLogger().warning("Invalid entity type: " + mobName);
        }
        
        // Misc
        ConfigurationSection miscSection = conf.getConfigurationSection("Misc");
        if (miscSection == null) miscSection = conf.createSection("Misc");
        
        ConfigurationSection homieSection  = miscSection.getConfigurationSection("Homie");
        if (homieSection == null) homieSection = miscSection.createSection("Homie");
        
        if (!homieSection.contains("cooldown")) {
        	homieSection.set("cooldown", 3600);
        }
        homieCooldown = homieSection.getInt("cooldown");
        
        ConfigurationSection alertsSection  = miscSection.getConfigurationSection("Alerts");
        if (alertsSection == null) alertsSection = miscSection.createSection("Alerts");
        
        if (!alertsSection.contains("alert-delay")) {
        	alertsSection.set("alert-delay", 8);
        }
        alertDelay = alertsSection.getInt("alert-delay");
        
        ConfigurationSection emotesSection  = miscSection.getConfigurationSection("Emotes");
        if (emotesSection == null) emotesSection = miscSection.createSection("Emotes");
        
        if (!emotesSection.contains("enabled")) {
        	emotesSection.set("enabled", true);
        }
        emotesEnabled = emotesSection.getBoolean("enabled");
        
        if (!emotesSection.contains("distance")) {
        	emotesSection.set("distance", 10);
        }
        emoteDistance = emotesSection.getInt("distance");
        
        try {
            conf.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean isHeadShotMob(EntityType entity) {
    	
    	if (headshotMobs.contains(entity))
    		return true;
    	
    	return false;
    }

}
