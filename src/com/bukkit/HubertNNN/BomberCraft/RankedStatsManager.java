/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Hubert
 */
public class RankedStatsManager {

    BomberCraft bc;
    FileConfiguration statsFile;
	private File fStats;

    public RankedStatsManager(BomberCraft bc) {
        this.bc = bc;
        fStats = new File(bc.getDataFolder(), "stats.yml");
        statsFile = YamlConfiguration.loadConfiguration(fStats);
      
        //  statsFile.load();
      try {
		statsFile.save(fStats);
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
    
    public void AddKill(Player p)
    {
        int points = statsFile.getInt("stats.players."+p.getName()+".kills", 0) + 1;
        statsFile.set("stats.players."+p.getName()+".kills", points);
        try {
    		statsFile.save(fStats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void AddWin(Player p)
    {
        int points = statsFile.getInt("stats.players."+p.getName()+".wins", 0) + 1;
        statsFile.set("stats.players."+p.getName()+".wins", points);
        try {
    		statsFile.save(fStats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void AddDeath(Player p)
    {
        int points = statsFile.getInt("stats.players."+p.getName()+".deaths", 0) + 1;
        statsFile.set("stats.players."+p.getName()+".deaths", points);
        try {
    		statsFile.save(fStats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    //TODO:
    public void Clear()
    {
        
    }

    
    public void CheckPlayer(CommandSender cs, String name)
    {
        int wins = statsFile.getInt("stats.players."+name+".wins", 0);
        int kills = statsFile.getInt("stats.players."+name+".kills", 0);
        int deaths = statsFile.getInt("stats.players."+name+".deaths", 0);
        
        cs.sendMessage("BomberCraft stats for player: "+name);
        cs.sendMessage("Wins: "+wins+/*"  Kills: "+ kills+*/"  Deaths: "+deaths);
    }


    public void Top(CommandSender cs, int num)
    {
        cs.sendMessage("Best BomberCraft players:");


       List<String> node = statsFile.getStringList("stats.players");
        if(node==null) return;
        
        for(String name : node)
        {

            //ConfigurationNode n = node.getNode(name);
            //int wins = n.getInt("wins", 0);
            //int kills = n.getInt("kills", 0);
            //int deaths = n.getInt("deaths", 0);
            int wins = statsFile.getInt("stats.player." + name + ".wins", 0);
            int kills = statsFile.getInt("stats.player." + name + ".kills", 0);
            int deaths = statsFile.getInt("stats.player." + name + ".deaths", 0);
            
        	cs.sendMessage(name+"\t Wins: "+wins+/*"  Kills: "+ kills+*/"  Deaths: "+deaths);
        }

    }

    public void Top(CommandSender cs)
    {
        Top(cs, 10);
    }



}
