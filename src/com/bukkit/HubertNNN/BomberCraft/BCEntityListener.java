/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * 
 * @author Hubert
 */
public class BCEntityListener implements Listener {

	BomberCraft bc;

	public BCEntityListener(BomberCraft bc) {
		this.bc = bc;
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (bc.IsGameArea(event.getLocation())
				&& event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityExplode(EntityExplodeEvent event) {

		if (bc.IsGameArea(event.getLocation()))
			if (event.blockList().size() > 0)
				event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event) {
		if (bc.IsGameArea(event.getEntity().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent event) {

		if (bc.IsGameArea(event.getEntity().getLocation())) {
			event.setCancelled(true); // TODO: Custom mobs = custom targeting
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityCombust(EntityCombustEvent event) {

		if (bc.IsGameArea(event.getEntity().getLocation())) {
			event.setCancelled(true);
		}
	}
}
