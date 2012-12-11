package com.octagami.idols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.zford.jobs.bukkit.JobsPlugin;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import com.octagami.idols.executor.MasterCommandExecutor;
import com.octagami.idols.listener.*;
import com.octagami.idols.task.EffectRemoveTask;

@SuppressWarnings("unused")
public class IdolsPlugin extends JavaPlugin {

	public static final String		NAME				= "Idols";
	public static final String		PERMISSION_ROOT		= "idols.";
	public static final String		MAIN_CLASS			= IdolsPlugin.class.getName();
	public static final String		VERSION				= "0.0.9.22";
	public static final String		DESCRIPTION			= "A collection of randomly useful commands";
	public static final String[]	AUTHORS				= { "octagami" };
	public static final String		WEBSITE				= "";
	public static final boolean		DATABASE			= false;
	public static final String[]	DEPENDENCIES		= { "Vault", "Jobs", "WorldEdit"};
	public static final String[]	SOFT_DEPENDENCIES	= {};
	
    private IdolsConfig config = null;
    private JobsPlugin jobs = null;
    private Economy economy = null;

	public void initConfig() {

		config = new IdolsConfig(this);
		config.loadSettings();
		
	}

	public void initCommands() {

		for (IdolsCommands.IdolCommand command : IdolsCommands.playerCommands) {

			getCommand(command.name).setExecutor(new MasterCommandExecutor(this));
		}

		for (IdolsCommands.IdolCommand command : IdolsCommands.adminCommands) {

			getCommand(command.name).setExecutor(new MasterCommandExecutor(this));
		}

	}

	public void initEvents() {

		this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);

	}

	@Override
	public void onEnable() {
		
		Idols.setInstance(this);
		
        if (!loadJobs()) {
            getLogger().severe("==================== Idols ====================");
            getLogger().severe("Jobs is required by this plugin to operate!");
            getLogger().severe("Please install Jobs first!");
            getLogger().severe("You can find the latest version here:");
            getLogger().severe("http://dev.bukkit.org/server-mods/jobs/");
            getLogger().severe("==============================================");
            setEnabled(false);
            return;
        }
        
        if (!loadVault()) {
            getLogger().severe("==================== Idols ====================");
            getLogger().severe("Vault is required by this plugin to operate!");
            getLogger().severe("Please install Vault first!");
            getLogger().severe("You can find the latest version here:");
            getLogger().severe("http://dev.bukkit.org/server-mods/vault/");
            getLogger().severe("==============================================");
            setEnabled(false);
            return;
        }

		initConfig();
		initCommands();
		initEvents();
		
		//startPolling();

		getLogger().info("v" + this.getDescription().getVersion() + " has been enabled.");

	}

	@Override
	public void onDisable() {

		this.getServer().getScheduler().cancelTasks(this);
		
		IdolsPlayerManager.players.clear();
		
		config = null;
		jobs = null;
		economy = null;
		
		Idols.clear();
		Idols.setInstance(null);

		getLogger().info("v" + this.getDescription().getVersion() + " has been disabled.");
	}
	
	public void reload() {

		config.reload();
		
	}
	
    public IdolsConfig getIdolsConfig() {
    	
    	return config;
    }
    
    public JobsPlugin getJobsHook() {
    	
    	return jobs;
    }
    
    public Economy getEconomy() {
		return economy;
	}
    
    private boolean loadJobs() {
    	
    	Plugin test = getServer().getPluginManager().getPlugin("Jobs");
    	
    	if (test != null && test instanceof JobsPlugin) {
    		
    		jobs = (JobsPlugin)test;
    		getLogger().info("Successfully linked with Jobs.");
    	    return true;
    	    
    	} else {
    		getLogger().severe("Could not find Jobs!");
            return false;     
    	}

    }
    
    private boolean loadVault() {
    	
        Plugin test = getServer().getPluginManager().getPlugin("Vault");
        if (test == null) {
        	getLogger().severe("Could not find Vault!");
            return false;       
        }
        	
        RegisteredServiceProvider<Economy> provider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            return false;
        }
        economy = provider.getProvider();
        
        getLogger().info("Successfully linked with Vault");
        return true;
    }
    
}
