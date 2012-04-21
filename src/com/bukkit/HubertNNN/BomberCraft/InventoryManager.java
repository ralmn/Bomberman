/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
/**
 *
 * @author Hubert
 */
public class InventoryManager {
    BomberCraft bc;

    public InventoryManager(BomberCraft bc) {
        this.bc = bc;
    }


    public void PreparePlayerForGameStart(Arena a, Player p, int slot)
    {
        SavePlayerToFile(p);
        ClearPlayer(p);
        SetCloth(p, slot);
        GiveStartContent(p, a);

        
    }

    public void PreparePlayerForGameEnd(Arena a, Player p, int slot)
    {
        LoadPlayerFromFile(p);
    }
    
    private void SetCloth(Player p, int i)
    {
        int start = 298;
        if(i%4==1) start = 302;
        if(i%4==2) start = 310;
        if(i%4==3) start = 314;


        PlayerInventory inv = p.getInventory();
        inv.setHelmet(new ItemStack(start+0, 1));
        inv.setChestplate(new ItemStack(start+1, 1));
        inv.setLeggings(new ItemStack(start+2, 1));
        inv.setBoots(new ItemStack(start+3, 1));

    }

    public void AddItem(Player p, Material mat)
    {
        if(mat==Material.TNT) AddItem(p,mat,0);
        else if(mat == Material.REDSTONE_TORCH_ON) AddItem(p, mat, 1);
        else if(mat==Material.REDSTONE) AddItem(p,mat,2);
        else if(mat==Material.IRON_BOOTS) AddItem(p,mat,3);
        else if(mat==Material.BONE) bc.bonusManager.AddNegativeEffect(p);
        else if(mat==Material.BREAD) bc.bonusManager.AddHealth(p);
    }

    private void AddItem(Player p, Material mat, int position)
    {
        Inventory inv = p.getInventory();
        
        
        
        if(inv.getItem(position) != null&& inv.getItem(position).getType()==mat)
        {
            ItemStack stack = inv.getItem(position);
            stack.setAmount(stack.getAmount()+1);
        }
        else
        {
            inv.setItem(position, new ItemStack(mat, 1));
        }
    }
    

    private void ClearPlayer(Player p)
    {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setBoots(null);
        inv.setLeggings(null);
        inv.setChestplate(null);
        inv.setHelmet(null);

        p.setHealth(6);
        p.setFoodLevel(6);
        p.setExp(0.0f);
        

    }

    private void SavePlayerToFile(Player p)
    {
        try{
            YamlConfiguration playerFile = new YamlConfiguration();
            PlayerInventory inv = p.getInventory();


            for(int i=-4; i<inv.getSize(); i++)
            {
                ItemStack is;
                if(i==-4)        is = inv.getBoots();
                else if(i == -3) is = inv.getLeggings();
                else if(i == -2) is = inv.getChestplate();
                else if(i == -1) is = inv.getHelmet();
                else             is = inv.getItem(i);
                
                if(is != null){
                playerFile.set("inventory."+i+".id", is.getTypeId());
                playerFile.set("inventory."+i+".amount", is.getAmount());
                playerFile.set("inventory."+i+".data", is.getData()==null? 0: is.getData().getData());
                playerFile.set("inventory."+i+".damage", is.getDurability());
                Iterator<Entry<Enchantment, Integer>> it = is.getEnchantments().entrySet().iterator();
                while(it.hasNext())
                {
                    Entry<Enchantment, Integer> en = it.next();
                    playerFile.set("inventory."+i+".ench."+en.getKey().getId(), en.getValue());
                }
                }else{
                	playerFile.set("inventory."+i+".id", 0);
                    playerFile.set("inventory."+i+".amount", 0);
                    playerFile.set("inventory."+i+".data", 0);
                    playerFile.set("inventory."+i+".damage", 0);
                }
                
                
            }

            playerFile.set("health", p.getHealth());
            playerFile.set("food", p.getFoodLevel());
            playerFile.set("exp", p.getExp());
            File f = new File("plugins/BomberCraft/data/players/"+p.getName()+".yml");
            
            File d = new File("plugins/BomberCraft/data/players/");
            
            if(!d.exists())
            	d.mkdirs();
            
            if(!f.exists()){
            	f.createNewFile();
            }
            playerFile.save(f);
            
        }catch(Exception ex){
        	ex.printStackTrace();
        }
    }

    private void LoadPlayerFromFile(Player p)
    {
        try{
            YamlConfiguration playerFile = new YamlConfiguration();
            
            File f = new File("plugins/BomberCraft", "data/players/"+p.getName()+".yml");
            
            
            
            playerFile.load(f);

            PlayerInventory inv = p.getInventory();
            
            for(int i=-4; i<inv.getSize(); i++)
            {
                ItemStack is;
                if(playerFile.getInt("inventory."+i+".id")==0) is = null;
                else
                {
                    is= new ItemStack(
                        playerFile.getInt("inventory."+i+".id"),
                        playerFile.getInt("inventory."+i+".amount"),
                        new Short((short)playerFile.getInt("inventory."+i+".damage")),
                        new Byte((byte)playerFile.getInt("inventory."+i+".data"))
                        );
                	

                    if(playerFile.getConfigurationSection("inventory."+i).getKeys(false).contains("ench"))
                    {
                        for(String s: playerFile.getConfigurationSection("inventory."+i+".ench").getKeys(false))
                        {
                            is.addEnchantment(Enchantment.getById(Integer.parseInt(s)), playerFile.getInt("inventory."+i+".ench."+s));
                        }
                    }
                }

                if(i==-4)        inv.setBoots(is);
                else if(i == -3) inv.setLeggings(is);
                else if(i == -2) inv.setChestplate(is);
                else if(i == -1) inv.setHelmet(is);
                else             inv.setItem(i, is);
            }

            p.setHealth(playerFile.getInt("health"));
            p.setFoodLevel(playerFile.getInt("food"));
            p.setExp(((Double)playerFile.get("exp")).floatValue());
            p.setFireTicks(0);

            f.delete();


        }catch(Exception ex){}
    }

    public void TestRecoverInventory(Player p)
    {
        if(new File(bc.getDataFolder(), "data/players/"+p.getName()+".yml").exists())
            LoadPlayerFromFile(p);
    }


    private void GiveStartContent(Player p, Arena a)
    {
        p.getInventory().setItem(0, new ItemStack(Material.TNT, 1));
    }
}
