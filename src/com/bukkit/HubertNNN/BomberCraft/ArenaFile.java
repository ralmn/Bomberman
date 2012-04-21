/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Hubert
 */
public class ArenaFile {

    BomberCraft bc;
    //Arena a;
    int sizeX;
    int sizeY;
    char[][] points;
    String name = "";

    public ArenaFile(BomberCraft bc, Arena a) {
        this.bc = bc;
        this.sizeX = a.size_x;
        this.sizeY = a.size_y;
    }

    public ArenaFile(BomberCraft bc, int sizeX, int sizeY) {
        this.bc = bc;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void loadClassic()
    {
        name = "clasic";
        points = new char[sizeX][sizeY];
        for(int i=0; i<sizeX-1; i++)
            for(int j=0; j< sizeY-1;j++)
                if(i%2==1 & j%2==1)
                    points[i][j] = '1';
                else
                    points[i][j] = '0';
    }

    private void loadDefault()
    {
        name = "none";
        points = new char[sizeX][sizeY];
        for(int i=0; i<sizeX-1; i++)
            for(int j=0; j< sizeY-1;j++)
                points[i][j] = '0';
    }

    public void load(File f)
    {
        points = new char[sizeX][sizeY];


        try {

            YamlConfiguration file = new YamlConfiguration();
            file.load(f);

            name = file.getString("name", f.getName());

            for(int i=0; i<sizeX-1; i++)
            {
                String line = file.getString("arena."+i);
                for(int j=0; j< line.length();j++)
                {
                    if(j>(sizeY-2)) break;

                    char c = line.charAt(j);

                    points[i][j] = c;
                    
                }
            }            

        } catch (Exception ex) 
        {
            loadDefault();

        }
    }

    public void save(File f)
    {
        try {

            if(f==null) return;

            YamlConfiguration file = new YamlConfiguration();

            for(int i=0; i<sizeX-1; i++)
            {
                String line = "";
                for(int j=0; j< sizeY-1;j++)
                {
                    line = line+points[i][j];
                }
                file.set("arena."+i, line);
            }

            file.save(f);

        } catch (Exception ex) {ex.printStackTrace();}
    }


    public char get(int x, int y)
    {
        return points[x][y];
    }

    public void set(int x, int y, char c)
    {
        points[x][y] = c;
    }

    public String GetName()
    {
        return name;
    }
}
