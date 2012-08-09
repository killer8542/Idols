package com.octagami.idols.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;

public class Util {
	
	
	
    public static boolean isNear(Location first, Location second, double maxDistance) {
        if (!first.getWorld().equals(second.getWorld())) {
            return false;
        }

        if (first.distanceSquared(second) < (maxDistance * maxDistance)) {
            return true;
        }
        else {
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
	    		return  diff / 1000;
	    	case MINUTES:
	    		return  diff / (60 * 1000);
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
    
    public static HashMap<Integer,Integer> countMultipleBlocks(CuboidRegion region, Set<Integer> searchIDs) {
    	
    	HashMap<Integer,Integer> results = new HashMap<Integer,Integer>();
    	
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


}
