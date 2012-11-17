package com.octagami.idols.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

public class Util {

	public static boolean isNear(Location first, Location second, double maxDistance) {
		if (!first.getWorld().equals(second.getWorld())) {
			return false;
		}

		if (first.distanceSquared(second) < (maxDistance * maxDistance)) {
			return true;
		} else {
			return false;
		}
	}

	public static long getTimeRemaining(Date currentTime, Date futureTime, TimeUnit timeUnit) {

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(currentTime);
		calendar2.setTime(futureTime);

		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();

		long diff = milliseconds2 - milliseconds1;

		switch (timeUnit) {

		case MILLISECONDS:
			return diff;
		case SECONDS:
			return diff / 1000;
		case MINUTES:
			return diff / (60 * 1000);
		case HOURS:
			return diff / (60 * 60 * 1000);
		case DAYS:
			return diff / (24 * 60 * 60 * 1000);
		default:
			return -1;

		}
	}

	/**
	 * Count the number of blocks of a list of types in a region.
	 * 
	 * @param region
	 * @param searchIDs
	 * @return
	 */
	public static int countOneBlock(CuboidRegion region, int blockID) {

		int result = 0;

		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();

		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {

					Vector pt = new Vector(x, y, z);
					if (region.getWorld().getBlockType(pt) == blockID)
						result++;
				}
			}
		}

		return result;
	}

	public static Vector getBlockVector(CuboidRegion region, int blockID) {

		Vector result = null;

		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();

		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		for (int x = minX; x <= maxX; ++x) {

			for (int y = minY; y <= maxY; ++y) {

				for (int z = minZ; z <= maxZ; ++z) {

					Vector pt = new Vector(x, y, z);
					if (region.getWorld().getBlockType(pt) == blockID) {
						result = pt;
						break;
					}

				}

				if (result != null)
					break;
			}

			if (result != null)
				break;
		}

		return result;
	}

	public static HashMap<Integer, Integer> countMultipleBlocks(CuboidRegion region, Set<Integer> searchIDs) {

		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();

		for (Integer searchID : searchIDs) {

			results.put(searchID, 0);
		}

		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();

		int minX = min.getBlockX();
		int minY = min.getBlockY();
		int minZ = min.getBlockZ();
		int maxX = max.getBlockX();
		int maxY = max.getBlockY();
		int maxZ = max.getBlockZ();

		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					Vector pt = new Vector(x, y, z);

					int id = region.getWorld().getBlockType(pt);
					if (searchIDs.contains(id)) {
						results.put(id, results.get(id) + 1);
					}
				}
			}
		}

		return results;
	}

	public static HashSet<Integer> newHashSet(Integer... ints) {
		HashSet<Integer> set = new HashSet<Integer>();

		for (Integer i : ints) {
			set.add(i);
		}
		return set;
	}

	public static Location lookAtPoint(Player player, Location point) {

		Location playerLocation = player.getLocation();
		Location eyeLocation = player.getEyeLocation();

		double xDiff = point.getX() + 0.5 - eyeLocation.getX();
		double yDiff = point.getY() + 0.5 - eyeLocation.getY();
		double zDiff = point.getZ() + 0.5 - eyeLocation.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
		double newYaw = (Math.acos(xDiff / DistanceXZ) * 180 / Math.PI);
		double newPitch = (Math.acos(yDiff / DistanceY) * 180 / Math.PI) - 90;
		if (zDiff < 0.0) {
			newYaw = newYaw + (Math.abs(180 - newYaw) * 2);
		}
		float yaw = (float) (newYaw - 90);
		float pitch = (float) newPitch;

		Location newLocation = new Location(playerLocation.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());

		newLocation.setYaw(yaw);
		newLocation.setPitch(pitch);

		return newLocation;
	}
	
	public static boolean isHelmet(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_HELMET ||
			item.getType() == Material.GOLD_HELMET ||
			item.getType() == Material.IRON_HELMET ||
			item.getType() == Material.CHAINMAIL_HELMET ||
			item.getType() == Material.LEATHER_HELMET)
			return true;
		
		return false;	
	}
	
	public static boolean isChestplate(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_CHESTPLATE ||
			item.getType() == Material.GOLD_CHESTPLATE ||
			item.getType() == Material.IRON_CHESTPLATE ||
			item.getType() == Material.CHAINMAIL_CHESTPLATE ||
			item.getType() == Material.LEATHER_CHESTPLATE)
			return true;
		
		return false;
	}
	
	public static boolean isLeggings(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_LEGGINGS ||
			item.getType() == Material.GOLD_LEGGINGS ||
			item.getType() == Material.IRON_LEGGINGS ||
			item.getType() == Material.CHAINMAIL_LEGGINGS ||
			item.getType() == Material.LEATHER_LEGGINGS)
			return true;
		
		return false;
	}
	
	public static boolean isBoots(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_BOOTS ||
			item.getType() == Material.GOLD_BOOTS ||
			item.getType() == Material.IRON_BOOTS ||
			item.getType() == Material.CHAINMAIL_BOOTS ||
			item.getType() == Material.LEATHER_BOOTS)
			return true;
		
		return false;
	}
	


	public static boolean isSword(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_SWORD ||
			item.getType() == Material.GOLD_SWORD ||
			item.getType() == Material.IRON_SWORD ||
			item.getType() == Material.WOOD_SWORD)
			return true;
		
		return false;
	}
	
	public static boolean isAxe(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_AXE ||
			item.getType() == Material.GOLD_AXE ||
			item.getType() == Material.IRON_AXE ||
			item.getType() == Material.WOOD_AXE)
			return true;
		
		return false;
	}
	
	public static boolean isPickAxe(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_PICKAXE ||
			item.getType() == Material.GOLD_PICKAXE ||
			item.getType() == Material.IRON_PICKAXE ||
			item.getType() == Material.WOOD_PICKAXE)
			return true;
		
		return false;
	}
	
	public static boolean isSpade(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_SPADE ||
			item.getType() == Material.GOLD_SPADE ||
			item.getType() == Material.IRON_SPADE ||
			item.getType() == Material.WOOD_SPADE)
			return true;
		
		return false;
	}
	
	public static boolean isHoe(ItemStack item) {
		
		if (item.getType() == Material.DIAMOND_HOE ||
			item.getType() == Material.GOLD_HOE ||
			item.getType() == Material.IRON_HOE ||
			item.getType() == Material.WOOD_HOE)
			return true;
		
		return false;
	}
	
	public static boolean isWeapon(ItemStack item) {
		
		if (isSword(item) || isAxe(item))
			return true;
		
		return false;
	}
	
	public static boolean isTool(ItemStack item) {
		
		if (isPickAxe(item) || isSpade(item) || isHoe(item))
			return true;
		
		return false;
	}
	
	public static boolean isArmor(ItemStack item) {
		
		if (isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item))
			return true;
		
		return false;
	}
	
}
