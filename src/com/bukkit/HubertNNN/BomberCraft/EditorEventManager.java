/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Hubert
 */
public class EditorEventManager {
    BomberCraft bc;

    public EditorEventManager(BomberCraft bc) {
        this.bc = bc;
    }

    public void onBlockPlace(BlockPlaceEvent event)
    {
        event.setCancelled(true);
        bc.arenaEditor.DoEdition(event);
    }

    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            if(event.getClickedBlock().getTypeId()== 68)
                bc.arenaEditor.ClickSign(bc.arenaManager.FindArena(event.getPlayer()), event.getClickedBlock(),event.getPlayer(), true);

        }

        if(event.getAction()==Action.RIGHT_CLICK_BLOCK)
        {
            if(event.getClickedBlock().getTypeId()== 68)
                bc.arenaEditor.ClickSign(bc.arenaManager.FindArena(event.getPlayer()), event.getClickedBlock(),event.getPlayer(), false);

        }
    }

    public void onPlayerMove(PlayerMoveEvent event)
    {
        
    }
}
