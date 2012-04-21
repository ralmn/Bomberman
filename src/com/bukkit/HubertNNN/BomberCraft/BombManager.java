/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.HubertNNN.BomberCraft;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * 
 * @author Hubert
 */
public class BombManager implements Runnable {

	public static Material fireMaterial = Material.SNOW;

	private class DetonationCleaner implements Runnable {

		BombManager bm;
		Block b;
		ItemStack is;

		public DetonationCleaner(BombManager bm, Block b, ItemStack is) {
			this.bm = bm;
			this.b = b;
			this.is = is;
		}

		public void run() {
			bm.CleanDetonation(b, is);
		}

	}

	BomberCraft bc;
	List<Bomb> bombs = new LinkedList<Bomb>();

	public BombManager(BomberCraft bc) {
		this.bc = bc;
		bc.getServer().getScheduler()
				.scheduleSyncRepeatingTask(bc, this, 20, 5);
	}

	public boolean PlaceBomb(Player p, Block b) {
		if (b.getRelative(BlockFace.UP).getType() != Material.AIR)
			return false;
		if (b.getRelative(BlockFace.DOWN).getType() != Material.OBSIDIAN)
			return false;

		Bomb bomb = new Bomb();
		bomb.block = b;
		bomb.player = p;
		bomb.time = 3000;

		if (p.getInventory().getItem(1) != null)
			if (p.getInventory().getItem(1).getType() == Material.REDSTONE_TORCH_ON)
				bomb.time = 300000;

		b.getRelative(BlockFace.UP).setType(Material.FENCE);
		bombs.add(bomb);
		return true;
	}

	public void Detonate() {
		 Iterator<Bomb> it = bombs.iterator();


			while (it.hasNext()) {
				 Bomb b = it.next();
				if (b.removed) {
					it.remove();
					continue;
				}
				b.time -= 250;
				if (b.time <= 0) {
					Detonate(b);
				}
			}
		
		for (Arena a : bc.arenaManager.arenas.values()) {
			for (Player p : a.slots) {
				if (p != null) {
					TestDetonation(p);
				}
			}
		}
	}

	private void Detonate(Bomb b) {
		if (b.removed)
			return;

		b.removed = true;
		int power = 1;
		PlayerInventory inv = b.player.getInventory();
		if (inv.getItem(2) != null) {
			if (inv.getItem(2).getType() == Material.REDSTONE) {
				power += inv.getItem(2).getAmount();
			}
		}
		if (bc.IsGameArea(b.player.getLocation()))
			bc.inventoryManager.AddItem(b.player, Material.TNT);

		b.block.setType(fireMaterial);
		b.block.getRelative(BlockFace.UP).setType(Material.AIR);

		PrepareDetonationCleaner(b.block, null);

		b.block.getWorld().createExplosion(b.block.getLocation(), 0);
		Detonate(b, BlockFace.NORTH, power);
		Detonate(b, BlockFace.EAST, power);
		Detonate(b, BlockFace.SOUTH, power);
		Detonate(b, BlockFace.WEST, power);
	}

	private void Detonate(Bomb b, BlockFace bf, int power) {
		Block bb = null;
		for (int i = 0; i < power; i++) {
			bb = b.block.getRelative(bf, i + 1);
			if (bb.getType() == Material.OBSIDIAN)
				break; // OBSIDIAN
			if (bb.getType() == Material.AIR) { // AIR
				bb.setType(fireMaterial);
				PrepareDetonationCleaner(bb, null);
				continue;
			}
			if (bb.getType() == Material.TNT) // TNT
			{
				Iterator<Bomb> it = bombs.iterator();

				while (it.hasNext()) {
					Bomb b1 = it.next();
					if (b1.block.equals(bb)) {
						Detonate(b1);
						break;
					}
				}
				break;
			}
			if (bb.getType() == fireMaterial)
				continue; // FIRE

			bb.setType(fireMaterial);
			bb.getRelative(BlockFace.UP).setType(Material.AIR);
			PrepareDetonationCleaner(bb, DropRandom());
			break;
		}
	}

	public void ForceDetonate(Player p) {
		for (Bomb b : bombs) {
			if (b.player == p) {
				b.time = 0;
				return;
			}
		}
	}

	private void PrepareDetonationCleaner(Block b, ItemStack drop) {
		bc.getServer()
				.getScheduler()
				.scheduleSyncDelayedTask(bc,
						new DetonationCleaner(this, b, drop), 20);
	}

	public void CleanDetonation(Block b, ItemStack drop) {
		if (b.getType() == fireMaterial)
			b.setType(Material.AIR);
		if (drop != null)
			b.getWorld().dropItem(b.getLocation(), drop);
	}

	private ItemStack DropRandom() {
		return bc.bonusManager.DropRandom();
	}

	public void run() {
		Detonate();
		Iterator<ProtectedEntity> it = protectedEntities.iterator();
		while (it.hasNext()) {
			ProtectedEntity pe = it.next();
			if (pe.time < 0)
				it.remove();
			pe.time -= 5;
		}
	}

	public boolean IsDetonated(Player p) {
		Location l = p.getLocation();

		return (l.getBlock().getType() == fireMaterial);
	}

	class ProtectedEntity {
		public Entity e;
		public int time;

		public ProtectedEntity(Entity e, int time) {
			this.e = e;
			this.time = time;
		}

	}

	LinkedList<ProtectedEntity> protectedEntities = new LinkedList<ProtectedEntity>();

	public void TestDetonation(Entity p) {
		if (p.getLocation().getBlock().getType() == fireMaterial) {
			for (ProtectedEntity pe : protectedEntities) {
				if (pe.e == p)
					return;
			}
			if (p instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) p;
				if (le.getHealth() > 2) {
					protectedEntities.offer(new ProtectedEntity(p, 30));
					le.setHealth(le.getHealth() - 2);
					return;
				}
			}

			bc.KillEntity(p);
		}
	}
}
