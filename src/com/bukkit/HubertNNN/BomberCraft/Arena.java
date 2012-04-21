/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Hubert
 */
public class Arena {

    public int id = 0;
    public String fileName = "";
    public String mapName = "Test";

    public int x, y, size_x, size_y, h;
    public World w;
    
    public boolean inGame = false;
    public boolean isPublic = true;
    public GameType gameType = GameType.CLASSIC;
    
    public Player[] slots = new Player[16];
    public int maxPlayers = 4;

    public boolean light = false;
    public boolean editing = false;

    public String owner = "";

    int[] costs = null;

    public int[] gameData;

    public Arena(BomberCraft bc, int id) {
        this.id = id;
        fileName = "arena"+id+".yml";
    }

    public int GetPlayerSlot(Player p)
    {
        for(int i=0; i<maxPlayers; i++)
        {
            if(slots[i]==p) return i;
        }
        return -1;
    }

    public void Teleport(Player p, int slot)
    {
        if(slot==0) p.teleport(new Location(w, x+1.5, h+1, y+1.5));
        if(slot==3) p.teleport(new Location(w, x+size_x-0.5, h+1, y+1.5));
        if(slot==2) p.teleport(new Location(w, x+1.5, h+1, y+size_y-0.5));
        if(slot==1) p.teleport(new Location(w, x+size_x-0.5, h+1, y+size_y-0.5));
    }


    public void JoinSpectators(Player p)
    {
        p.teleport(new Location(w, x+(size_x/2), h+5, y+(size_y/2)));
    }

    public boolean  IsGameArea(Location loc)
    {
        if((loc.getBlockX()>=x)&&(loc.getBlockX()<=x+size_x)&&(loc.getBlockZ()>=y)&&(loc.getBlockZ()<=y+size_y)&&(loc.getBlockY()>=h)&&(loc.getBlockY()<=h+4)) return true;

        return false;
    }


}
