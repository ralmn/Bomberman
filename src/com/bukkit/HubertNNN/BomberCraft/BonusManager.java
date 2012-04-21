/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import net.minecraft.server.MobEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hubert
 */
public class BonusManager {
    BomberCraft bc;

    public BonusManager(BomberCraft bc) {
        this.bc = bc;
    }
    
    public ItemStack DropRandom()
    {
        int num = bc.rand.nextInt(100);
        if(num<10) return new ItemStack(Material.REDSTONE, 1);
        if(num<20) return new ItemStack(Material.TNT, 1);
        if(num<30) return new ItemStack(Material.BONE, 1);
        //if(num<90) return new ItemStack(Material.BREAD, 1);
        if(num==91) return new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        if(num==92) return new ItemStack(Material.IRON_BOOTS, 1);
        if(num==93) return new ItemStack(Material.BREAD, 1);
        return null;
    }

    public void AddNegativeEffect(Player p)
    {
        if(p instanceof CraftPlayer)
        {
            int r = bc.rand.nextInt(2);
            //r = -1;
            
            
            int id = 0;
            int time = 20*10; //10 sec
            int modifier = 0;

            if(r==0){id = 2; modifier = 3;}  //Slow
            if(r==1){id = 15; modifier = 3;} //Blind


            CraftPlayer cp = (CraftPlayer)p;
            cp.getHandle().addEffect(new MobEffect(id,time,modifier));          //id, time, modifier
        }

        

       
        

    }
    public void AddHealth(Player p)
    {
        if(p.getHealth()<=18) p.setHealth(p.getHealth()+2);

    }
}
