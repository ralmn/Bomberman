/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Hubert
 */
public class ArenaManager {
    BomberCraft bc;
    HashMap<Integer, Arena> arenas = new HashMap<Integer, Arena>();


    public ArenaManager(BomberCraft bc) {
        this.bc = bc;
    }


    public int CreateArena(Player p, int size_x, int size_y, boolean force)
    {

        World w = p.getWorld();

        int x = p.getLocation().getBlockX() - (size_x/2);
        int y = p.getLocation().getBlockZ() - (size_y/2);
        int h = p.getLocation().getBlockY();

        if(size_x%2==1) size_x--;
        if(size_y%2==1) size_y--;

        boolean allow = true;

        if(!force)
        {
            for(int i=x; i<=x+size_x; i++)
            {
                for(int j=y; j<=y+size_y; j++)
                {
                    for(int k=h; k<=h+4; k++)
                    {
                        if(p.getWorld().getBlockAt(i, k, j).getType()!=Material.AIR)
                        {
                            allow = false;
                            p.sendMessage("Obstackle found: "+p.getWorld().getBlockAt(i, k, j).getType());
                            p.sendMessage("Aborting arena creation");
                            p.sendMessage("To force arena creation here type");
                            p.sendMessage("/bombercraft create "+size_x+" "+size_y+" force");
                            p.sendMessage("WARNING: This will destroy nerby blocks forever.");
                        }
                        if(!allow) break;
                    }
                    if(!allow) break;
                }
                if(!allow) break;
            }
        }

        if(allow)
        {
            p.sendMessage("Generating BomberCraft arena "+size_x +":"+size_y);

            int id = CreateArena(w, x, size_x, y, size_y, h, GameType.CLASSIC, p.getName());

            p.teleport(p.getLocation().add(0, 6, 0));

            return id;

        }

        return 0;
    }

    public int CreateArena(World w, int x, int size_x, int y, int size_y, int h, GameType type, String owner)
    {
        int id = 0;
        for(int i=1; i<1024; i++)
        {
            if(arenas.containsKey(i)) continue;
            id = i;
            break;
        }
        
        Arena a = GenerateArena(id, w, x, size_x, y, size_y, h, type);

        a.owner = owner;

        SaveArena(a);

        return id;
    }

    public void LoadArenas()
    {

        File dir = new File(bc.getDataFolder(), "arenas");
        for(File f: dir.listFiles())
        {
            LoadArena(f);
        }
    }

    public void LoadArena(File f)
    {
        YamlConfiguration arenaFile = new YamlConfiguration();
        try {
            arenaFile.load(f);
        } catch (Exception ex) {
            return;
        }

        int id = arenaFile.getInt("arena.id", 0);
        String worldName = arenaFile.getString("arena.world", "");
        int x = arenaFile.getInt("arena.position.x", 0);
        int y = arenaFile.getInt("arena.position.y", 0);
        int h = arenaFile.getInt("arena.position.h", 0);
        int size_x = arenaFile.getInt("arena.position.size_x", 0);
        int size_y = arenaFile.getInt("arena.position.size_y", 0);
        String typeS = arenaFile.getString("arena.gamemode", "classic");
        GameType type = GameType.valueOf(typeS.toUpperCase());
        
        World w = bc.getServer().getWorld(worldName);
        if(w==null) return;

        Arena a = GenerateArena(id, w, x, size_x, y, size_y, h, type);
        a.fileName = f.getName();

        a.maxPlayers = arenaFile.getInt("arena.max_players", 4);
        a.light = arenaFile.getBoolean("arena.game.light_on", false);
        a.owner = arenaFile.getString("arena.game.owner", "");

        a.mapName = arenaFile.getString("arena.game.map", "none");

        ArenaBulder.BuildArena(a);
    }

    private Arena GenerateArena(int id, World w, int x, int size_x, int y, int size_y, int h, GameType type)
    {
        if(!arenas.containsKey(id)) arenas.put(id, new Arena(bc, id));

        Arena a = arenas.get(id);

        a.gameType = type;
        
        a.w = w;
        a.x = x;
        a.y = y;
        a.h = h;
        a.size_x = size_x;
        a.size_y = size_y;

        ArenaBulder.BuildArena(a);
        a.inGame = false;


        for(int i=0; i<a.maxPlayers; i++)
        {
            a.slots[i]=null;
        }

        return a;
        
    }
    
    public void SaveArena(Arena a)
    {
        File f = new File(bc.getDataFolder(), "arenas/"+a.fileName);
        YamlConfiguration arenaFile = new YamlConfiguration();

        arenaFile.set("arena.id", a.id);
        arenaFile.set("arena.world", a.w.getName());
        arenaFile.set("arena.position.x", a.x);
        arenaFile.set("arena.position.y", a.y);
        arenaFile.set("arena.position.h", a.h);
        arenaFile.set("arena.position.size_x", a.size_x);
        arenaFile.set("arena.position.size_y", a.size_y);
        arenaFile.set("arena.owner", a.owner);
        arenaFile.set("arena.gamemode", a.gameType.name().toLowerCase());
        arenaFile.set("arena.game.max_players", a.maxPlayers);
        arenaFile.set("arena.game.light_on", a.light);
        
        if(a.gameType==GameType.NORMAL)
        {
            arenaFile.set("arena.game.map", a.mapName);
        }

        
        try {
            arenaFile.save(f);
        } catch (IOException ex) {}

    }

    public void ClearAllArenas()
    {
        Iterator<Arena> it = arenas.values().iterator();

        while(it.hasNext())
        {
            Arena a = it.next();

            for(Player p : a.slots)
                if(p!=null) bc.playerManager.Leave(p);

            ArenaBulder.ClearArena(a);
            it.remove();
        }
    }

    public void ClearArena(int id)
    {
        Arena a = arenas.get(id);
        ClearArena(a);
    }

    public void ClearArena(Arena a)
    {
        if(a==null) return;
        
        ArenaBulder.ClearArena(a);
        for(Player p : a.slots)
            if(p!=null) bc.playerManager.Leave(p);

        arenas.remove(a.id);
    }
    
    public void RemoveArena(int id)
    {
        Arena a = arenas.get(id);
        RemoveArena(a);
    }


    public void RemoveArena(Arena a)
    {
        File f = new File(bc.getDataFolder(), "arenas/"+a.fileName);
        if(f.exists())f.delete();

        ClearArena(a);
    }

    public void StartGame()
    {
        //TODO
    }

    public void StartGame(int id)
    {
        StartGame(arenas.get(id));
    }

    private class ArenaStarter implements Runnable
    {
        Arena a;
        public ArenaStarter(Arena a) {
            this.a = a;
        }

        public void run() {
                for(int i=a.x; i<=a.x+a.size_x; i++)
                {
                    for(int j=a.y; j<=a.y+a.size_y; j++)
                    {
                        for(int k=a.h+1; k<=a.h+3; k++)
                        {
                            if(a.w.getBlockAt(i, k, j).getType()==Material.GLASS)
                                    a.w.getBlockAt(i, k, j).setType(Material.AIR);
                        }
                    }
                }
                a.inGame = true;
        }

    }

    public void StartGame(Arena a)
    {
        
        if(a==null) return;

        for(int i=0;i<a.maxPlayers;i++)
        {
            if(a.slots[i]==null) continue;
            DelayedMessage.Create(a.slots[i], 10, "3");
            DelayedMessage.Create(a.slots[i], 20, "2");
            DelayedMessage.Create(a.slots[i], 30, "1");
            DelayedMessage.Create(a.slots[i], 40, "Lets play BomberCraft");
        }

        bc.getServer().getScheduler().scheduleSyncDelayedTask(bc, new ArenaStarter(a), 40);

    }

    public boolean IsReadyForStart(Arena a)
    {
        if(a==null) return false;

        for(int i=0;i<a.maxPlayers;i++)
        {
            if(a.slots[i]==null) return false;
        }

        return true;
    }


    //TODO:
    public void CheckForWin()
    {
        for(int i : arenas.keySet())
        {
            CheckForWin(i);
        }
    }

    //TODO:
    public boolean CheckForWin(int id)
    {
        Arena a = arenas.get(id);
        if(a==null) return false;

        if(a.editing) return false;

        int players = 0;
        for(int i=0; i<a.maxPlayers; i++)
        {
            if(a.slots[i]!=null) players++;
        }

        if(players>1) return false;

        Player p = null;
        for(int i=0; i<a.maxPlayers; i++)
        {
            if(a.slots[i]!=null) p = a.slots[i];
        }

        if(p!=null)
        {
            bc.getServer().broadcastMessage("BomberCraft game finished");
            bc.getServer().broadcastMessage("and the winner is "+p.getDisplayName());

            bc.rankedStatsManager.AddWin(p);

        }

        if(p!=null) bc.playerManager.Leave(p);
        ArenaBulder.BuildArena(a);
        return true;
    }

    public Arena FindArena(Player p)
    {
        for(Arena a : arenas.values())
        {
            if(a.GetPlayerSlot(p)>=0) return a;
        }
        return null;
    }

    public boolean IsGameArea(Location loc)
    {
        for(Arena a : arenas.values())
        {
            if(a.IsGameArea(loc)) return true;
        }
        return false;
    }

    public Arena FindArena(int id)
    {
        return arenas.get(id);
    }

    public Arena FindFreeSlot()
    {
        for(Arena a : arenas.values())
        {
            if(a.inGame) continue;
            if(a.isPublic)
            for(int i=0; i<a.maxPlayers; i++)
            {
                if(a.slots[i]==null) return a;
            }
        }
        return null;
    }


    public void ListArenas(CommandSender cs, boolean includePrivate)
    {
        cs.sendMessage(ChatColor.GOLD+"Arenas for bombercraft:");
        for(Arena a : arenas.values())
        {
            if(!(includePrivate || a.isPublic)) continue;
            int players = 0;
            int maxPlayers = a.maxPlayers;
            for(Player p :a.slots)
            {
                if(p!=null) players++;
            }


            boolean canJoin = true;
            if(players>= maxPlayers) canJoin = false;
            if(a.inGame) canJoin = false;

            boolean pvp = true;
            //if(a.gameType==GameType.CUSTOM_PVE) pvp = false;
            //if(a.gameType==GameType.NORMAL_PVE) pvp = false;

            cs.sendMessage((canJoin ? ChatColor.BLUE : ChatColor.GRAY)+(""+a.id+". "+a.size_x+"x"+a.size_y+" "+(a.isPublic ? "PUBLIC " : "PRIVATE")+" "+ a.gameType+" "+ (pvp ? "PVP" : "PVE") +" "+players+"/"+maxPlayers+" " + (a.inGame ? "STARTED" : "WAITING")));

        }
    }

    public void SetLight(Arena a, boolean ON)
    {
        ArenaBulder.SetLight(a, ON);
    }

}
