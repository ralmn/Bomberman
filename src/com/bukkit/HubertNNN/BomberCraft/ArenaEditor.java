/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hubert
 */
public class ArenaEditor {
    BomberCraft bc;

    LinkedList<Arena> editedArenas = new LinkedList<Arena>();
    LinkedList<Player> editors = new LinkedList<Player>();

    public ArenaEditor(BomberCraft bc) {
        this.bc = bc;
    }


    void CreateArenaFile(Arena a)
    {
        
    }

    void BuildEditorModeArena(Arena a)
    {
        ArenaBulder.BuidCleanArena(a);

        BuildEditorFloor(a);
    }

    void BuildEditorFloor(Arena a)
    {
        try{

            ArenaFile caf = bc.arenaFileManager.LoadArenaFile(a);

            for(int i=0; i<a.size_x-1; i++)
            {
                for(int j=0; j< a.size_y-1;j++)
                {

                    char c = caf.get(i, j);

                    Material mat = Material.AIR;

                    if(c=='0') mat = Material.COBBLESTONE;
                    if(c=='1') mat = Material.OBSIDIAN;
                    if(c=='2') mat = Material.GLASS;
                    if(c=='3') mat = Material.DIRT;
                    if(c=='s') mat = Material.BRICK;

                    a.w.getBlockAt(a.x+i+1, a.h + 1, a.y+j+1).setType(mat);
                }
            }

        }catch(Exception ex){ex.printStackTrace();}

    }

    public void EnterEditMode(Arena a, Player p)
    {
        if(a==null)return;
        
        for(int i=0; i<a.maxPlayers; i++)
        {
            if(a.slots[i]==null) continue;
            bc.playerManager.Leave(a, a.slots[i]);
        }
        
        a.inGame = false;
        
        a.editing = true;
        bc.playerManager.Join(a, p);
        a.inGame = true;

        editedArenas.add(a);
        editors.add(p);

        p.getInventory().setItem(0, new ItemStack(Material.DIRT, 10));
        p.getInventory().setItem(1, new ItemStack(Material.COBBLESTONE, 10));
        p.getInventory().setItem(2, new ItemStack(Material.GLASS, 10));
        p.getInventory().setItem(3, new ItemStack(Material.OBSIDIAN, 10));
        p.getInventory().setItem(4, new ItemStack(Material.BRICK, 10));


        BuildEditorModeArena(a);

        p.teleport(p.getLocation().add(0, 1, 0));

        a.gameData = new int[10];
        a.gameData[0] = 0;

        SetSigns(a);

    }

    

    private void SetSigns(Arena a)
    {
        if(a.gameType == GameType.CLASSIC) SetSignsClassic(a);
        if(a.gameType == GameType.NORMAL)  SetSignsNormal(a);
        if(a.gameType == GameType.CUSTOM)  SetSignsCustom(a);
    }

    private void SetSignsClassic(Arena a)
    {
        SetSigns(a, new String[][]{
            {"Exit", "EditMode"},
            {"GameType", ""+a.gameType},
            {"Light", a.light? "ON" : "OFF"},
            {"MaxPlayers", ""+a.maxPlayers}
        });
    }

    private void SetSignsNormal(Arena a)
    {
        SetSigns(a, new String[][]{
            {"Exit", "EditMode"},
            {"GameType", ""+a.gameType},
            {"Light", a.light? "ON" : "OFF"},
            {"Name", ""+bc.arenaFileManager.LoadArenaFile(a).GetName()}
        });
    }

    private void SetSignsCustom(Arena a)
    {
        SetSigns(a, new String[][]{
            {"Exit", "EditMode"},
            {"GameType", ""+a.gameType},
            {"Light", a.light? "ON" : "OFF"},
            {"MaxPlayers", ""+a.maxPlayers},
        });
    }

    private void SetSigns(Arena a, String[][] signs)
    {
        int numPlaces = a.size_x-2;
        if(numPlaces>5) numPlaces = 5;

        int numSigns = signs.length;

        int scroll = a.gameData[0];
        if(scroll+numPlaces>numSigns+1) scroll--;
        a.gameData[0] = scroll;

        
        if(numSigns>numPlaces)
        {
            int scrollDir = 0;
            if(scroll==0) scrollDir = -1;
            if(scroll+numPlaces == numSigns+1) scrollDir = 1;

            Block b = a.w.getBlockAt(a.x+numPlaces, a.h+3,a.y+1);
            b.setTypeId(68);
            b.setData((byte)3);
            Sign s = (Sign)b.getState();
            s.setLine(0, "Scroll");
            if(scrollDir>0) s.setLine(1, "<-    ");
            if(scrollDir==0)s.setLine(1, "<-  ->");
            if(scrollDir<0) s.setLine(1, "    ->");

            s.update();
            numPlaces--;
        }

        for(int i=0; i<numPlaces; i++)
        {
            Block b = a.w.getBlockAt(a.x+i+1, a.h+3,a.y+1);
            if(scroll + i >=numSigns)
            {
                b.setTypeId(0);
                continue;
            }
            
            
            b.setTypeId(68);
            b.setData((byte)3);

            Sign s = (Sign)b.getState();
            if(signs[scroll+i].length>0) s.setLine(0, signs[scroll+i][0]);
            if(signs[scroll+i].length>1) s.setLine(1, signs[scroll+i][1]);
            if(signs[scroll+i].length>2) s.setLine(2, signs[scroll+i][2]);
            if(signs[scroll+i].length>3) s.setLine(3, signs[scroll+i][3]);

            s.update();
        }

        


    }

    public boolean IsEditing(Player p)
    {
        return editors.contains(p);
    }

    public void DoEdition(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().getRelative(BlockFace.UP).getType()==Material.GLASS)
        {
            return;
        }


        Material mat = event.getBlockPlaced().getType();
        Block b = event.getBlockPlaced().getRelative(BlockFace.DOWN);

        if(b.getType()==mat) return;
        Arena a = bc.arenaManager.FindArena(event.getPlayer());

        if(a.gameType != GameType.CUSTOM)
        {
            event.getPlayer().sendMessage("Can't edit non custom maps");
            return;
        }

        b.setType(mat);

        char c = '0';
        {

            if(mat == Material.COBBLESTONE) c='0';
            if(mat == Material.OBSIDIAN) c='1';
            if(mat == Material.GLASS) c='2';
            if(mat == Material.DIRT) c='3';
            if(mat == Material.BRICK) c='s';
        }

        ArenaFile caf = bc.arenaFileManager.LoadArenaFile(a);


        int x = b.getLocation().getBlockX()-a.x-1;
        int y = b.getLocation().getBlockZ()-a.y-1;

        caf.set(x, y, c);

        bc.arenaFileManager.SaveArena(a, caf);
    }

    public void ClickSign(Arena a, Block b, Player p, boolean leftClick)
    {
        if(a== null) return;
        if(b== null) return;
        if(b.getTypeId()!=68) return;
        
        Sign s = (Sign) b.getState();
        String typeMessage = s.getLine(0);

        if(typeMessage.equals("Exit"))
        {
            LeaveEditMode(a, p);
            bc.playerManager.Spectate(a, p);
            return;
        }

        if(typeMessage.equals("MaxPlayers"))
        {
            if(leftClick)
                a.maxPlayers++;
            else
                a.maxPlayers--;

            if(a.maxPlayers>4) a.maxPlayers = 1;
            if(a.maxPlayers<1) a.maxPlayers = 4;

        }

        if(typeMessage.equals("GameType"))
        {
            while(true)
            {
            
                int i = a.gameType.ordinal();
                if(leftClick)
                    i++;
                else
                    i--;

                if(i>=GameType.values().length) i=0;
                if(i<0) i=GameType.values().length-1;

                a.gameType = GameType.values()[i];
            
                if(a.gameType==GameType.NORMAL && !bc.arenaFileManager.IsNormalSize(a)) continue;

                break;
            }



            BuildEditorFloor(a);

        }

        if(typeMessage.equals("Light"))
        {
            a.light = !a.light;
        }

        if(typeMessage.equals("Scroll"))
        {
            if(leftClick)
                a.gameData[0]++;
            else
                a.gameData[0]--;

            if(a.gameData[0]<0)a.gameData[0]=0;
        }

        if(typeMessage.equals("Name") && a.gameType==GameType.NORMAL)
        {
            //TODO: Normal map chooser
            if(leftClick)
                a.mapName = bc.arenaFileManager.GetNextNormalArenaName(a.size_x, a.mapName);
            else
                ;

            BuildEditorFloor(a);
        }


        bc.arenaManager.SaveArena(a);
        
        SetSigns(a);
    }

    public void LeaveEditMode(Arena a, Player p)
    {
        if(a == null) return;
        if(p == null) return;

        if(!a.editing) return;

        for(int i=a.x+1; i<=a.x+a.size_x-1; i++)
        {
            a.w.getBlockAt(i, a.h+3,a.y+1).setType(Material.AIR);
        }

        a.editing = false;
        a.inGame = false;
        
        if(editors.contains(p)) editors.remove(p);
        if(editedArenas.contains(a)) editedArenas.remove(a);
    }
}




/*
 * Game data:
 * 0.Scroll value
 *
 */