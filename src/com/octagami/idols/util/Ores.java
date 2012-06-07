package com.octagami.idols.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Material;

public class Ores {
	
	private static final transient Pattern NUMPATTERN = Pattern.compile("\\d+");
	private static final Map<String, Material> ORES = new HashMap<String, Material>();

	static
	{	
		ORES.put("diamond", Material.DIAMOND_ORE);
		ORES.put("diamondore", Material.DIAMOND_ORE);
		
		ORES.put("gold", Material.GOLD_ORE);
		ORES.put("goldore", Material.GOLD_ORE);
		
		ORES.put("lapis", Material.LAPIS_ORE);
		ORES.put("lapisore", Material.LAPIS_ORE);
		ORES.put("lapislazuli", Material.LAPIS_ORE);
		ORES.put("lapislazuliore", Material.LAPIS_ORE);
		
		ORES.put("redstone", Material.REDSTONE_ORE);
		ORES.put("redstoneore", Material.REDSTONE_ORE);
		
		ORES.put("iron", Material.IRON_ORE);
		ORES.put("ironore", Material.IRON_ORE);
		
		ORES.put("coal", Material.COAL_ORE);
		ORES.put("coalore", Material.COAL_ORE);

	}
	
	public static final ArrayList<Material> ORE_TYPES = new ArrayList<Material>();
	
	static
	{
		ORE_TYPES.add(Material.COAL_ORE);
		ORE_TYPES.add(Material.IRON_ORE);
		ORE_TYPES.add(Material.REDSTONE_ORE);
		ORE_TYPES.add(Material.LAPIS_ORE);
		ORE_TYPES.add(Material.GOLD_ORE);
		ORE_TYPES.add(Material.DIAMOND_ORE);
		
	}
	
	public static Material getByName(String name) {
		
		Material material = null;;
		
		if (NUMPATTERN.matcher(name).matches()) {
			material = Material.getMaterial(Integer.parseInt(name));
		} 
		
//		else {
//			material = Material.getMaterial(name.toUpperCase(Locale.ENGLISH));
//		}
		
		if (material == null)
		{
			material = ORES.get(name.toLowerCase(Locale.ENGLISH));
		}
		
		return material;
	}
	

}
