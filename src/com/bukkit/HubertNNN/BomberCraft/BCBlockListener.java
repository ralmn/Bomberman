/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * 
 * @author Hubert
 */
public class BCBlockListener implements Listener {

	BomberCraft bc;

	public BCBlockListener(BomberCraft bc) {
		this.bc = bc;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (bc.IsGameArea(event.getBlock().getLocation())) {
			if (bc.arenaEditor.IsEditing(event.getPlayer()))
				bc.editorEventManager.onBlockPlace(event);
			else
				bc.gameEventManager.onBlockPlace(event);
			
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) {
		if (bc.IsGameArea(event.getBlock().getLocation()))
			event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (bc.IsGameArea(event.getBlock().getLocation()))
			event.setCancelled(true);
	}

}
