package com.octagami.idols.util;

import org.bukkit.Material;

public class PhantomItem {
	
	public static String getLabel(Material material) {
		
		String itemName = material.name().replace("_", " ").toLowerCase();
		return "Phantom " + itemName;
		
	}

}
