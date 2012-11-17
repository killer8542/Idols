package com.octagami.idols.task;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.octagami.idols.Idols;
import com.octagami.idols.IdolsPlugin;
import com.octagami.idols.util.Util;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;

public class RadarTask implements Runnable {

	private final IdolsPlugin plugin;

	private String playerName = "";
	private int oreId = -1;
	private int dist = 0;

	private int taskID = -1;

	private Date expiration = null;

	public RadarTask(IdolsPlugin plugin, String playerName, int oreId, int dist, int duration) {

		this.plugin = plugin;
		this.playerName = playerName;
		this.oreId = oreId;
		this.dist = dist;

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, duration);
		this.expiration = cal.getTime();
	}

	@Override
	public void run() {

		if (!plugin.isEnabled())
			return;

		Player player = plugin.getServer().getPlayer(playerName);

		if (player == null)
			return;

		Location loc = player.getLocation();

		BukkitWorld w = new BukkitWorld(loc.getWorld());

		Vector vec1 = new Vector(loc.getX() + dist, loc.getY() + dist, loc.getZ() + dist);

		Vector vec2 = new Vector(loc.getX() - dist, loc.getY() - dist, loc.getZ() - dist);

		CuboidRegion region = new CuboidRegion(w, vec1, vec2);

		Vector oreLocation = Util.getBlockVector(region, oreId);

		if (oreLocation != null) {

			Location newLookLocation = Util.lookAtPoint(player, new Location(loc.getWorld(), oreLocation.getX(), oreLocation.getY(), oreLocation.getZ()));

			player.teleport(newLookLocation);

			player.sendMessage(ChatColor.GOLD + "You sense " + Material.getMaterial(oreId).name().replace("_", " ").toLowerCase() + " nearby!");

			final String radarPlayerName = playerName;

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				public void run() {

					Idols.emote(plugin.getServer().getPlayer(radarPlayerName), " senses ore nearby!");
				}

			}, 0L);

		}

		if (Util.getTimeRemaining(new Date(), expiration, TimeUnit.SECONDS) <= 0) {

			if (taskID > 0) {
				plugin.getServer().getScheduler().cancelTask(taskID);
				player.sendMessage(ChatColor.RED + "Your mining intuition fades");
			} else {
				plugin.getLogger().severe("Failed to cancel radar task because taskID was not set");
			}

		}

	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

}
