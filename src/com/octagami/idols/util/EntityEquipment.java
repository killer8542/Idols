package com.octagami.idols.util;

import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Class, that allows setting and getting equipment of mob.
 */
public class EntityEquipment {

	public static void setEquipment(LivingEntity mob, ItemStack item, int slot) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(slot, itemStack);
	}

	public static ItemStack getEquipment(LivingEntity mob, int slot) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(slot) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(slot));
	}

	public static void setWeapon(LivingEntity mob, ItemStack item) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(0, itemStack);
	}

	public static ItemStack getWeapon(LivingEntity mob) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(0) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(0));
	}

	public static void setHelmet(LivingEntity mob, ItemStack item) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(4, itemStack);
	}

	public static ItemStack getHelmet(LivingEntity mob) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(4) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(4));
	}

	public static void setChestplate(LivingEntity mob, ItemStack item) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(3, itemStack);
	}

	public static ItemStack getChestplate(LivingEntity mob) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(3) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(3));
	}

	public static void setPants(LivingEntity mob, ItemStack item) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(2, itemStack);
	}

	public static ItemStack getPants(LivingEntity mob) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(2) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(2));
	}

	public static void setBoots(LivingEntity mob, ItemStack item) {
		if (!isApplicable(mob))
			return;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		net.minecraft.server.ItemStack itemStack = ((CraftItemStack) item).getHandle();
		ent.setEquipment(1, itemStack);
	}

	public static ItemStack getBoots(LivingEntity mob) {
		if (!isApplicable(mob))
			return null;
		EntityLiving ent = ((CraftLivingEntity) mob).getHandle();
		
		if (ent.getEquipment(1) == null)
			return null;
		
		return new CraftItemStack(ent.getEquipment(1));
	}

	private static boolean isApplicable(LivingEntity entity) {
		switch (entity.getType()) {
		case ZOMBIE:
		case SKELETON:
			return true;
		default:
			return false;
		}
	}
}