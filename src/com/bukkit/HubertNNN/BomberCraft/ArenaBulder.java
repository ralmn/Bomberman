/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author Hubert
 */
public class ArenaBulder {
    private static BomberCraft bc;
    
    public static void SetBc(BomberCraft bc1)
    {
        bc = bc1;
    }

    public static void ClearArena(Arena a)
    {
        for(int i=a.x; i<=a.x+a.size_x; i++)
        {
            for(int j=a.y; j<=a.y+a.size_y; j++)
            {
                for(int k=a.h; k<a.h+5; k++)
                {
                    a.w.getBlockAt(i, k, j).setType(Material.AIR);
                }
            }
        }
    }

    public static void BuildClassicArena(Arena a)
    {
        int x = a.x;
        int y = a.y;
        int h = a.h;
        int size_x = a.size_x;
        int size_y = a.size_y;
        World w = a.w;

        for(int i=x+1; i<x+size_x; i++)
        {
            for(int j=y+1; j<y+size_y; j++)
            {

                //Pylons
                if(((x+i)%2==0 && (y+j)%2==0))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.OBSIDIAN);
                    w.getBlockAt(i, h + 2, j).setType(Material.OBSIDIAN);
                    w.getBlockAt(i, h + 3, j).setType(Material.AIR);
                    continue;
                }

                //Fix KonckBack
                if((i==x+1||i==x+size_x-1)&&(j==y+1||j==y+size_y-1))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 2, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 3, j).setType(Material.GLASS);
                    continue;
                }

                //Block Starting area
                if((i==x+1||i==x+2||i==x+size_x-1||i==x+size_x-2)&&(j==y+1||j==y+2||j==y+size_y-1||j==y+size_y-2))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.GLASS);
                    w.getBlockAt(i, h + 2, j).setType(Material.GLASS);
                    w.getBlockAt(i, h + 3, j).setType(Material.AIR);
                    continue;
                }


                if(BomberCraft.singelton.rand.nextBoolean())
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.DIRT);
                    w.getBlockAt(i, h + 2, j).setType(Material.DIRT);
                    w.getBlockAt(i, h + 3, j).setType(Material.AIR);
                }
                else
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 2, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 3, j).setType(Material.AIR);
                }
            }
        }

        a.inGame = false;

    }

    public static void BuildArenaFromFile(Arena a, ArenaFile af)
    {
        int x = a.x;
        int y = a.y;
        int h = a.h;
        World w = a.w;
        
        BuidCleanArena(a);


        for(int i=0; i<a.size_x-1; i++)
        {
            for(int j=0; j< a.size_y-1;j++)
            {
                if(w.getBlockAt(x+i+1, h + 1, y+j+1).getType()==Material.GLASS) continue;
                if(w.getBlockAt(x+i+1, h + 3, y+j+1).getType()==Material.GLASS) continue;

                char c = af.get(i, j);

                Material mat = Material.AIR;

                if(c=='0' && bc.rand.nextBoolean()) mat = Material.DIRT;
                if(c=='1') mat = Material.OBSIDIAN;
                if(c=='2') mat = Material.AIR;
                if(c=='3') mat = Material.DIRT;
                //if(c=='s') mat = Material.BEDROCK;

                w.getBlockAt(x+i+1, h + 1, y+j+1).setType(mat);
                w.getBlockAt(x+i+1, h + 2, y+j+1).setType(mat);
                w.getBlockAt(x+i+1, h + 3, y+j+1).setType(Material.AIR);
            }
        }


        BlockStartingArea(a);


        a.inGame = false;

    }

    public static void BuidCleanArena(Arena a)
    {
        int x = a.x;
        int y = a.y;
        int h = a.h;
        int size_x = a.size_x;
        int size_y = a.size_y;
        World w = a.w;


        for(int i=x; i<=x+size_x; i++)
        {
            for(int j=y; j<=y+size_y; j++)
            {
                w.getBlockAt(i, h, j).setType(Material.OBSIDIAN);
                w.getBlockAt(i, h + 4, j).setType(Material.GLASS);

                //Corners
                if(((j==y)||(j==y+size_y))&&((i==x)||(i==x+size_x)))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.CHEST);
                    w.getBlockAt(i, h + 2, j).setType(Material.CHEST);
                    w.getBlockAt(i, h + 3, j).setType(Material.OBSIDIAN);
                    continue;
                }

                //Borders
                if((j==y)||(j==y+size_y)||(i==x)||(i==x+size_x))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.OBSIDIAN);
                    w.getBlockAt(i, h + 2, j).setType(Material.OBSIDIAN);
                    w.getBlockAt(i, h + 3, j).setType(Material.OBSIDIAN);
                    continue;
                }


                {
                    w.getBlockAt(i, h + 1, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 2, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 3, j).setType(Material.AIR);
                }
            }
        }
    }

    public static void BlockStartingArea(Arena a)
    {

        int x = a.x;
        int y = a.y;
        int h = a.h;
        int size_x = a.size_x;
        int size_y = a.size_y;
        World w = a.w;


        for(int i=x; i<=x+size_x; i++)
        {
            for(int j=y; j<=y+size_y; j++)
            {

                //Fix KonckBack
                if((i==x+1||i==x+size_x-1)&&(j==y+1||j==y+size_y-1))
                {
                    w.getBlockAt(i, h + 1, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 2, j).setType(Material.AIR);
                    w.getBlockAt(i, h + 3, j).setType(Material.GLASS);
                    continue;
                }

                //Block Starting area
                if((i==x+1||i==x+2||i==x+size_x-1||i==x+size_x-2)&&(j==y+1||j==y+2||j==y+size_y-1||j==y+size_y-2))
                {
                    if(w.getBlockAt(i, h + 1, j).getType()!=Material.OBSIDIAN)
                        w.getBlockAt(i, h + 1, j).setType(Material.GLASS);
                    
                    if(w.getBlockAt(i, h + 2, j).getType()!=Material.OBSIDIAN)
                        w.getBlockAt(i, h + 2, j).setType(Material.GLASS);
                    
                    continue;
                }
            }
        }

    }


    public static void SetLight(Arena a, boolean ON)
    {
        Material m = Material.OBSIDIAN;
        if(ON) m = Material.GLOWSTONE;

        for(int i=a.x+1; i<=a.x+a.size_x-1; i++)
        {
            for(int j=a.y+1; j<=a.y+a.size_y-1; j++)
            {
                Block b = a.w.getBlockAt(i, a.h + 2, j);
                if(b.getType()==Material.OBSIDIAN || b.getType()==Material.GLOWSTONE) b.setType(m);
            }
        }
    }


    public static int GetConstInt(GameType gameType, ConstType value)
    {
        if(gameType==GameType.CLASSIC && value==ConstType.MAX_PLAYERS) return 4;
        if(gameType==GameType.CUSTOM && value==ConstType.MAX_PLAYERS) return 4;
        

        if(value==ConstType.STARTING_TNT) return 1;
        if(value==ConstType.STARTING_REDSTONE) return 0;
        return 0;
    }

    public static void BuildArena(Arena a, GameType type)
    {
        BuidCleanArena(a);

        BuildArenaFromFile(a, bc.arenaFileManager.LoadArenaFile(a));


        if(a.light) SetLight(a, true);
    }

    public static void BuildArena(Arena a)
    {
        BuildArena(a, a.gameType);
    }

}
