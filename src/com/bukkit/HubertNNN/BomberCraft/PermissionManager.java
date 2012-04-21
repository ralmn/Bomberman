/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.entity.Player;

/**
 *
 * @author Hubert
 */
public class PermissionManager {

    BomberCraft bc;

    public PermissionManager(BomberCraft bc) {
        this.bc = bc;
    }


    public boolean CheckPermission(Player p, String s)
    {
        return p.hasPermission("bombercraft."+s);
    }


}
