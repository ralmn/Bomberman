/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;

/**
 *
 * @author Hubert
 */
public class ArenaFileManager {
    BomberCraft bc;

    public ArenaFileManager(BomberCraft bc) {
        this.bc = bc;
    }


    public ArenaFile LoadArenaFile(Arena a)
    {
        if(a.gameType == GameType.CLASSIC) return GetClassicArena(a.size_x, a.size_y);
        if(a.gameType == GameType.CUSTOM) return GetCustomArena(a);
        if(a.gameType == GameType.NORMAL)
        {
            if((a.size_x == 10)&&(a.size_y==10))   return GetNormalArena(10,  a.mapName);
            if((a.size_x == 20)&&(a.size_y==20))   return GetNormalArena(20,  a.mapName);
            if((a.size_x == 50)&&(a.size_y==50))   return GetNormalArena(50,  a.mapName);
            if((a.size_x == 100)&&(a.size_y==100)) return GetNormalArena(100, a.mapName);
        }


        return null;
    }

    private ArenaFile GetClassicArena(int sizeX, int sizeY)
    {
        ArenaFile af = new ArenaFile(bc, sizeX, sizeY);
        af.loadClassic();
        return af;
    }

    private ArenaFile GetCustomArena(Arena a)
    {
        ArenaFile af = new ArenaFile(bc, a);
        af.load(new File(bc.getDataFolder(), "templates/custom/" + a.fileName));
        return af;
    }

    private ArenaFile GetNormalArena(int size, String name)
    {
        ArenaFile af = new ArenaFile(bc, size, size);
        af.load(new File(bc.getDataFolder(), "templates/normal/"+size+"x"+size+"/" + name));
        return af;
    }

    public String GetNextNormalArenaName(int size, String previous)
    {
        File dir = new File(bc.getDataFolder(), "templates/normal/"+size+"x"+size+"/");

        String newName = null;
        boolean found = false;

        for(String s : dir.list())
        {
            if(s.endsWith(".yml"))
            {
                if(newName==null) newName = s;
                if(found)
                {
                    newName = s;
                    break;
                }
                if(previous.equals(s)) found = true;
            }
        }

        if(newName==null) return "none";
        return newName;
    }

    public boolean SaveArena(Arena a, ArenaFile af)
    {
        if(a.gameType==GameType.CUSTOM)
        {
            af.save(new File(bc.getDataFolder(), "templates/custom/" + a.fileName));
            return true;
        }

        return false;
    }


    public boolean IsNormalSize(Arena a)
    {
            if((a.size_x == 10)&&(a.size_y==10))   return true;
            if((a.size_x == 20)&&(a.size_y==20))   return true;
            if((a.size_x == 50)&&(a.size_y==50))   return true;
            if((a.size_x == 100)&&(a.size_y==100)) return true;

        return false;
    }

}
