/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 *
 * @author Hubert
 */


public class BCServerListener implements CommandExecutor {

    BomberCraft bc;

    public BCServerListener(BomberCraft bc) {
        this.bc = bc;
    }

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(strings.length<=0) return true;

        if(strings[0].equals("create"))
        {
            if(cs instanceof Player)
            {
                boolean force = false;

                if(!bc.permissionManager.CheckPermission((Player)cs, "admin.create")) return true;
                int size_x = 10;
                int size_y = 10;
                if(strings.length>2)
                {
                    size_x = Integer.parseInt(strings[1]);
                    size_y = Integer.parseInt(strings[2]);
                    if(strings.length>3)
                    {

                        if(strings[3].equals("force"))
                        {
                            if(!bc.permissionManager.CheckPermission((Player)cs, "admin.forcecreate")) return true;
                            force = true;
                        }
                    }
                }
            

                bc.arenaManager.CreateArena((Player)cs, size_x, size_y, force);
            }
            
        }

        if(strings[0].equals("join"))
        {
            if(cs instanceof Player)
            {
                if(!bc.permissionManager.CheckPermission((Player)cs, "play.join")) return true;
                if(strings.length<=1)
                {
                    bc.playerManager.Join((Player)cs);
                }
                else
                {
                    bc.playerManager.Join(bc.arenaManager.FindArena(Integer.parseInt(strings[1])), (Player)cs);
                }
            }

        }

        if(strings[0].equals("spectate"))
        {
            if(cs instanceof Player)
            {
                if(!bc.permissionManager.CheckPermission((Player)cs, "play.spectate")) return true;
                if(strings.length<=1)
                {
                    bc.playerManager.Spectate(bc.arenaManager.FindArena((Player)cs), (Player)cs);
                }
                else
                {
                    if(bc.arenaManager.FindArena((Player)cs)!=null)
                        bc.playerManager.Spectate(bc.arenaManager.FindArena((Player)cs), (Player)cs);
                    
                    bc.playerManager.Spectate(bc.arenaManager.FindArena(Integer.parseInt(strings[1])), (Player)cs);
                }
            }

        }

        if(strings[0].equals("clear"))
        {
            if(cs instanceof Player)
                if(!bc.permissionManager.CheckPermission((Player)cs, "admin.clear")) return true;

            if(strings.length<=1)
            {
                bc.arenaManager.ClearAllArenas();
            }
            else
            {
                bc.arenaManager.RemoveArena(Integer.parseInt(strings[1]));
            }
        }


        if(strings[0].equals("start"))
        {

            if(cs instanceof Player)
            {

                
                if(strings.length<=1)
                {
                    Arena a = null;
                    a = bc.arenaManager.FindArena((Player)cs);
                    if(a!=null)
                    {
                        if(!bc.permissionManager.CheckPermission((Player)cs, "play.start")) return true;
                        bc.arenaManager.StartGame(a);

                    }else{
                        
                        if(!bc.permissionManager.CheckPermission((Player)cs, "admin.start")) return true;
                        bc.arenaManager.StartGame();
                    }
                }
                else
                {
                    if(!bc.permissionManager.CheckPermission((Player)cs, "admin.start")) return true;

                    bc.arenaManager.StartGame(Integer.parseInt(strings[1]));
                }

                
                
            }
        }

        if(strings[0].equals("stop"))
        {
            if(cs instanceof Player)
            {
                if(!bc.permissionManager.CheckPermission((Player)cs, "admin.stop")) return true;
                if(strings.length<=1)
                {
                    bc.playerManager.StopGame(bc.arenaManager.FindArena((Player)cs));
                }
                else
                {
                    bc.playerManager.StopGame(bc.arenaManager.FindArena(Integer.parseInt(strings[1])));
                }
            }

        }

        if(strings[0].equals("list"))
        {
            if(cs instanceof Player)
            {
                if(!bc.permissionManager.CheckPermission((Player)cs, "play.list")) return true;
                boolean listPrivate = bc.permissionManager.CheckPermission((Player)cs, "play.listprivate");

                bc.arenaManager.ListArenas(cs, listPrivate);

            }
        }


        if(strings[0].equals("edit"))
        {

            if(cs instanceof Player)
            {
                if(!bc.permissionManager.CheckPermission((Player)cs, "admin.edit")) return true;

                bc.arenaEditor.EnterEditMode(bc.arenaManager.FindArena(Integer.parseInt(strings[1])), (Player)cs);

            }
        }


        if(strings[0].equals("top"))
        {
            if(!bc.permissionManager.CheckPermission((Player)cs, "play.top")) return true;

            bc.rankedStatsManager.Top(cs);
        }

        if(strings[0].equals("help"))
        {
            cs.sendMessage("BomberCraft commands:");
            if(bc.permissionManager.CheckPermission((Player)cs, "admin.create"))cs.sendMessage("/bc create <X> <Y>");
            if(bc.permissionManager.CheckPermission((Player)cs, "admin.clear")) cs.sendMessage("/bc clear <ID>");

            if(bc.permissionManager.CheckPermission((Player)cs, "play.join"))   cs.sendMessage("/bc join");
            if(bc.permissionManager.CheckPermission((Player)cs, "play.join"))   cs.sendMessage("/bc join <ID>");
            if(bc.permissionManager.CheckPermission((Player)cs, "play.spectate")) cs.sendMessage("/bc spectate <ID>");
            
            if(bc.permissionManager.CheckPermission((Player)cs, "play.start"))  cs.sendMessage("/bc start");
            if(bc.permissionManager.CheckPermission((Player)cs, "admin.start")) cs.sendMessage("/bc start <ID>");
            if(bc.permissionManager.CheckPermission((Player)cs, "admin.stop")) cs.sendMessage("/bc stop <ID>");
            
            if(bc.permissionManager.CheckPermission((Player)cs, "play.list"))   cs.sendMessage("/bc list");
            if(bc.permissionManager.CheckPermission((Player)cs, "admin.edit"))  cs.sendMessage("/bc edit");
            
        }
        
        return true;
    }

}
