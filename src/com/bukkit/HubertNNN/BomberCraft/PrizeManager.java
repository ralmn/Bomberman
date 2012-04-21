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
public class PrizeManager {
    BomberCraft bc;

    public PrizeManager(BomberCraft bc) {
        this.bc = bc;
    }

    private void ModifyMoney(Player p, double amount)
    {
        
    }

    private void DecodeCosts(Arena a, String s)
    {
        String[] ss = s.split(":");
        int[] prizes = new int[6];
        for(int i=0; i<ss.length; i++)
        {
            if(i>=6) break;
        }
    }

    public void AddPrize(Arena a, Player p)
    {
        if(a.costs==null) return;

    }

    public void SendCost(Arena a, Player p)
    {
        if(a.costs == null) return;

    }

    public boolean Afford(Arena a, Player p)
    {
        if(a.costs==null) return true;


        return true;
    }
    
}
