package com.bukkit.HubertNNN.BomberCraft;

import java.io.File;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Hubert
 */
public class BomberCraft extends JavaPlugin {

	public static BomberCraft singelton;

	public boolean IsGameArea(Location loc) {
		return arenaManager.IsGameArea(loc);
	}

	public void KillEntity(Entity e) {
		if (e instanceof Player) {
			Player p = (Player) e;

			rankedStatsManager.AddDeath(p);

			playerManager.Leave(p);
			arenaManager.CheckForWin();
		}
	}

	public static Random rand = new Random();

	BCBlockListener blockListener;
	BCEntityListener entityListener;
	BCServerListener serverListener;
	BCPlayerListener playerListener;

	ArenaManager arenaManager;
	BombManager bombManager;
	PlayerManager playerManager;
	InventoryManager inventoryManager;
	BonusManager bonusManager;
	ArenaEditor arenaEditor;
	GameEventManager gameEventManager;
	EditorEventManager editorEventManager;
	ArenaFileManager arenaFileManager;

	RankedStatsManager rankedStatsManager;
	PermissionManager permissionManager;

	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		arenaManager.ClearAllArenas();
	}

	public void onEnable() {
		singelton = this;
		Install();
		// throw new UnsupportedOperationException("Not supported yet.");
		
		ArenaBulder.SetBc(this);

		blockListener = new BCBlockListener(this); // OK
		entityListener = new BCEntityListener(this); // OK, need improvement
		serverListener = new BCServerListener(this); // OK
		playerListener = new BCPlayerListener(this); // OK

		arenaManager = new ArenaManager(this); // OK
		bombManager = new BombManager(this); // OK
		playerManager = new PlayerManager(this); // OK, new improvement
		inventoryManager = new InventoryManager(this);
		bonusManager = new BonusManager(this);
		permissionManager = new PermissionManager(this);// OK
		arenaEditor = new ArenaEditor(this);
		rankedStatsManager = new RankedStatsManager(this);
		gameEventManager = new GameEventManager(this);
		editorEventManager = new EditorEventManager(this);
		arenaFileManager = new ArenaFileManager(this);

		DelayedMessage.SetBC(this);

		PluginManager pm = getServer().getPluginManager();

		
		registerEvent(pm);

		getCommand("bombercraft").setExecutor(serverListener);
		getCommand("bc").setExecutor(serverListener);

		arenaManager.LoadArenas();

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");
		

	}

	private void registerEvent(PluginManager pm) {

		pm.registerEvents(blockListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);

	}

	private void Install() {
		File f = null;
		f = new File(getDataFolder(), "templates/custom");
		if (!f.exists())
			f.mkdirs();
		f = new File(getDataFolder(), "templates/normal/10x10");
		if (!f.exists())
			f.mkdirs();
		f = new File(getDataFolder(), "templates/normal/20x20");
		if (!f.exists())
			f.mkdirs();
		f = new File(getDataFolder(), "templates/normal/50x50");
		if (!f.exists())
			f.mkdirs();
		f = new File(getDataFolder(), "arenas");
		if (!f.exists())
			f.mkdirs();
	}

	public void log(String s) {
		System.out.println("BomberCraft: " + s);
	}
}
