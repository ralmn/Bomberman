/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author Hubert
 */
public class Bomb {
    public Block block;
    public int time;
    public Player player;

    public boolean removed = false;
    public void Remove()
    {
        removed = true;
    }
}
