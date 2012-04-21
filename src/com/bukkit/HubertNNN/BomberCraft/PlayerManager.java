/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import net.minecraft.server.MobEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Hubert
 */
public class PlayerManager {
    BomberCraft bc;

    public PlayerManager(BomberCraft bc) {
        this.bc = bc;
    }

    public void Join(Player p)
    {
        Arena a = bc.arenaManager.FindFreeSlot();
        if(a==null)
        {
            p.sendMessage("There are no free slots in any public game");
        }else{
            Join(a, p);
        }
    }

    public void Join(Arena a, Player p)
    {
        int slot = FindSlot(a, p);

        if(slot<0)
        {
            Spectate(a, p);
            return;
        }

        a.slots[slot]=p;
        a.Teleport(p, slot);

        bc.inventoryManager.PreparePlayerForGameStart(a, p, slot);

        if(bc.arenaManager.IsReadyForStart(a)) bc.arenaManager.StartGame(a);
    }

    public void Leave(Player p)
    {
        Leave(bc.arenaManager.FindArena(p), p);
    }

    public void Leave(Arena a, Player p)
    {
        if(a==null) return;
        if(p==null) return;

        int slot = a.GetPlayerSlot(p);
        if(slot==-1) return;

        bc.inventoryManager.PreparePlayerForGameEnd(a, p, slot);

        a.slots[slot]=null;
        a.JoinSpectators(p);
    }


    
    public void Spectate(Arena a, Player p)
    {
        int slot = a.GetPlayerSlot(p);
        if(slot>=0)
        {
            Leave(a, p);
            bc.arenaManager.CheckForWin(a.id);
        }
        else a.JoinSpectators(p);
    }

    public void StopGame(Arena a)
    {
        if(a==null)return;

        for(int i=0; i<a.maxPlayers; i++)
        {
            if(a.slots[i]==null) continue;
            Leave(a, a.slots[i]);
        }

        bc.arenaManager.CheckForWin(a.id);
    }



    private int FindSlot(Arena a, Player p)
    {
       if(a.inGame) return -1;

        int slot = -1;
        for(int i=0; i<a.maxPlayers; i++)
        {
            if(a.slots[i]==p) return -2;
            if(a.slots[i]==null)
            {
                slot = i;
                break;
            }
        }
        return slot;
    }

    public void PlayerMovementEvent(Player p)
    {
        if(p.getFoodLevel()<5) p.setFoodLevel(6);
        
        PlayerInventory inv = p.getInventory();
        
        if(inv.getItem(3) == null){
        	return;
        }
        
        if(inv.getItem(3).getType()==Material.IRON_BOOTS)
        {
            if(p instanceof CraftPlayer)
            {
                CraftPlayer cp = (CraftPlayer)p;
                cp.getHandle().addEffect(new MobEffect(1,30,inv.getItem(3).getAmount()-1));                //id, time, modifier
            }
        }
    }
    
}
