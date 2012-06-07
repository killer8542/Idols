package com.octagami.idols;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import me.zford.jobs.Jobs;
import me.zford.jobs.config.container.Job;

import com.octagami.idols.exceptions.IdolsNotLoadedException;
import com.octagami.idols.util.Util;

public class Idols {

	private static IdolsPlugin	instance;
	
	private static ArrayList<Job> builderJobs = new ArrayList<Job>();
	private static ArrayList<Job> soldierJobs = new ArrayList<Job>();
	
	private static HashMap<String, HashMap<String, Date>> playerCooldowns = new HashMap<String, HashMap<String, Date>>();
	
	public static Random random = new Random();

	public static IdolsPlugin getPlugin() throws IdolsNotLoadedException {
		Idols.check();
		return Idols.instance;
	}

	public static void setInstance(IdolsPlugin instance) {
		Idols.instance = instance;
	}
	
	public static void clear() {
		
		playerCooldowns.clear();
		builderJobs.clear();
		soldierJobs.clear();
	}

	private static void check() throws IdolsNotLoadedException {
		if (Idols.instance == null) {
			throw new IdolsNotLoadedException();
		}
	}
	
	public static ArrayList<Job> getBuilderJobs() { 
	
		return builderJobs;
	}
	
	public static ArrayList<Job> getSoldierJobs() { 
		
		return soldierJobs;
	}
	
	public static void setJobs(Jobs jobs) {
		
		final Job builder = jobs.getJobConfig().getJob("Builder");
		final Job mason = jobs.getJobConfig().getJob("Mason");
		final Job engineer = jobs.getJobConfig().getJob("Engineer");
		
		builderJobs.add(builder);
		builderJobs.add(mason);
		builderJobs.add(engineer);
		
		
		final Job soldier = jobs.getJobConfig().getJob("Soldier");
		final Job centurion = jobs.getJobConfig().getJob("Centurion");
		final Job samurai = jobs.getJobConfig().getJob("Samurai");
		
		
		soldierJobs.add(soldier);
		soldierJobs.add(centurion);
		soldierJobs.add(samurai);

	}
	
	public static HashMap<String, HashMap<String, Date>> getAllCooldowns(){
		
		return playerCooldowns;
	}

	public static long getAbilityCooldownTimeRemaining(final String playerName, final String ability) {
		
		if (!playerCooldowns.containsKey(playerName)) {
			return 0;
		}
		
		if (!playerCooldowns.get(playerName).containsKey(ability)) {
			
			return 0;
		}
		
		return Util.getTimeRemaining( new Date(), playerCooldowns.get(playerName).get(ability), TimeUnit.SECONDS );
		
	}
	
	
	public static void setAbilityCooldown(final String playerName, final String ability, int refresh) {
		
		if (!playerCooldowns.containsKey(playerName))
			playerCooldowns.put(playerName, new HashMap<String, Date>());

		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, refresh);
        playerCooldowns.get(playerName).put(ability, cal.getTime());
			
	}
	
}
