package aog.minigame.funbocks.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcraid.weapongen.ItemGeneratorAPI;
import org.hcraid.weapongen.WeaponRarity;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.instance.FBPlayer;

public class FBWinnerEvents implements Listener{
	
	public static Player winner = null;
	public static Inventory inven = null;
	public static boolean testing = false;
	public static ArrayList<ItemStack> rewards;
	
	public static Inventory getWinningInventory(boolean test) {
		
		testing = test;
		
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Select a prize!");
				
		ArrayList<ItemStack> r = new ArrayList<ItemStack>();
		
		while(r.size() < 10){
			
			if(Math.random() >= 0.3){
				r.add(ItemGeneratorAPI.generateRandomWeapon(WeaponRarity.HEROIC));
			}else{
				r.add(ItemGeneratorAPI.generateRandomWeapon(WeaponRarity.LEGENDARY));
			}
			
		}
		
		if(!testing){
			Main.currentGame.setRewards(r);
			rewards = Main.currentGame.getRewards();
		}else{
			rewards = r;
		}
		//ItemStack display = Core.nameItemStack(new ItemStack(Material.IRON_FENCE), "");
		
		for(int i = 0; i < inv.getSize(); i++){
			inv.setItem(i, rewards.get(i));
		}
		
		inven = inv;
		
		return inv;

	}

	@EventHandler
	public void selectPrize(final InventoryClickEvent e) {

		final Player p = (Player) e.getWhoClicked();
		FBPlayer fbp = Core.getFunbocksPlayer(p);

		if ((fbp != null || testing) && p == winner) {

			e.setCancelled(true);

			if (e.getCurrentItem() != null) {

				//boolean flag = false;

				if (testing){

					if (rewards.contains(e.getCurrentItem())) {
						inven = null;

						p.closeInventory();

						// restoreData();

						Main.p.getServer()
								.getScheduler()
								.scheduleSyncDelayedTask(Main.p,
										new Runnable() {

											@Override
											public void run() {
												p.getInventory().addItem(
														e.getCurrentItem());
												p.sendMessage(ChatColor.GREEN
														+ "You won! Well done! You've been given your reward. Total kills: ");
												// Main.data
												// .setCurrentChampion(getId());

												// Main.currentGame.endGame();

												testing = false;
												p.closeInventory();
											}
										}, 10);

					}
						
					} else {

						if (Main.currentGame.getRewards().contains(
								e.getCurrentItem())) {
							fbp.win(e.getCurrentItem());
						}
					}

			}

		}

	}
	
	@EventHandler
	public void closeInventory(final InventoryCloseEvent e){
		
		new BukkitRunnable() {
			
			@Override
			public void run() {

				Player p = (Player) e.getPlayer();
				
				if(p == winner){
					
					if(inven != null){
						p.openInventory(inven);
					}
					
				}
				
			}
		}.runTaskLater(Main.p, 3);
		

	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		
		Player p = (Player) e.getPlayer();
		
		if(p == winner){
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void pickUpItem(PlayerPickupItemEvent e){
		
		Player p = (Player) e.getPlayer();
		
		if(p == winner){
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void damage(EntityDamageEvent e){
		
		if(e.getEntity() instanceof Player){
		
		Player p = (Player) e.getEntity();
		
			if(p == winner){
				e.setCancelled(true);
			}
		
		}
		
	}
	
	@EventHandler
	public void closeInventory(PlayerQuitEvent e){
		
		Player p = (Player) e.getPlayer();
		
		if(p == winner){
			inven = null;
			FBPlayer fbp = Core.getFunbocksPlayer(p);
			
			fbp.kill("quit during the winning screen.");
			
			Main.currentGame.forceEnd();
			
		}
		
	}
	
	@EventHandler
	public void closeInventory(PlayerKickEvent e){
		
		Player p = (Player) e.getPlayer();
		
		if(p == winner){
			inven = null;
			FBPlayer fbp = Core.getFunbocksPlayer(p);
			
			fbp.kill("kicked during the winning screen.");
			
			Main.currentGame.forceEnd();
			
		}
		
	}

}
