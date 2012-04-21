/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Hubert
 */
public class BCPlayerListener implements Listener {
	BomberCraft bc;

	public BCPlayerListener(BomberCraft bc) {
		this.bc = bc;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event) {
		bc.playerManager.Leave(event.getPlayer());
		bc.arenaManager.CheckForWin();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKick(PlayerKickEvent event) {
		bc.playerManager.Leave(event.getPlayer());
		bc.arenaManager.CheckForWin();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {

		bc.inventoryManager.TestRecoverInventory(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {

		if (bc.arenaManager.IsGameArea(event.getPlayer().getLocation())) {
			bc.inventoryManager.AddItem(event.getPlayer(), event.getItem()
					.getItemStack().getType());
			event.getPlayer().getInventory().setItem(8, new ItemStack(1));
			event.getItem().getItemStack().setTypeId(1);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!bc.arenaManager.IsGameArea(event.getPlayer().getLocation()))
			return;

		if (bc.arenaEditor.IsEditing(event.getPlayer()))
			bc.editorEventManager.onPlayerInteract(event);
		else
			bc.gameEventManager.onPlayerInteract(event);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		if (bc.IsGameArea(event.getPlayer().getLocation())) {
			String cmd = event.getMessage().split(" ")[0].substring(1).trim();
			if (cmd.equals("bc"))
				return;
			if (cmd.equals("bombercraft"))
				return;
			if (!bc.permissionManager.CheckPermission(event.getPlayer(),
					"allowcommand." + cmd)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(
						ChatColor.RED
								+ "Using commands is blocked by BomberCraft");
				event.getPlayer().sendMessage(
						"Type " + (ChatColor.GREEN + "/bc spectate")
								+ ChatColor.WHITE + " to leave arena first");
			}
		}

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!bc.IsGameArea(event.getPlayer().getLocation()))
			return;

		if (bc.arenaEditor.IsEditing(event.getPlayer()))
			bc.editorEventManager.onPlayerMove(event);
		else
			bc.gameEventManager.onPlayerMove(event);
	}

}
