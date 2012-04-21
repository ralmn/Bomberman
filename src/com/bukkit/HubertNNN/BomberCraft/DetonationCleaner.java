/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hubert
 */
public class DetonationCleaner implements Runnable{

    BombManager bm;
    Block b;
    ItemStack is;

    public DetonationCleaner(BombManager bm, Block b, ItemStack is)
    {
        this.bm = bm;
        this.b = b;
        this.is = is;
    }

    public void run() {
        bm.CleanDetonation(b, is);
    }

}
