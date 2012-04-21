/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * 
 * @author Hubert
 */
public class GameEventManager {
	BomberCraft bc;

	public GameEventManager(BomberCraft bc) {
		this.bc = bc;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlockPlaced().getType() == Material.TNT) {
			if (!bc.bombManager.PlaceBomb(event.getPlayer(),
					event.getBlockPlaced()))
				event.setCancelled(true);
		} else
			event.setCancelled(true);
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {

			if (event.getPlayer().getInventory().getItem(1) != null)
				if (event.getPlayer().getInventory().getItem(1).getType() == Material.REDSTONE_TORCH_ON)
					bc.bombManager.ForceDetonate(event.getPlayer());
		}
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		bc.playerManager.PlayerMovementEvent(event.getPlayer());
		bc.bombManager.TestDetonation(event.getPlayer());
	}

}
