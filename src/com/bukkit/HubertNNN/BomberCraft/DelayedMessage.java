/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import org.bukkit.command.CommandSender;

/**
 *
 * @author Hubert
 */
public class DelayedMessage implements Runnable {
    static BomberCraft bc = null;

    public static void Create(CommandSender cs, int time, String txt)
    {
        if(bc==null) return;
        bc.getServer().getScheduler().scheduleSyncDelayedTask(bc, new DelayedMessage(cs, txt), time);
    }

    public static void SetBC(BomberCraft bc1)
    {
        bc = bc1;
    }





    CommandSender cs;
    String txt;

    public DelayedMessage(CommandSender cs, String txt) {
        this.cs = cs;
        this.txt = txt;
    }

    public void run() {
        cs.sendMessage(txt);
    }

}
