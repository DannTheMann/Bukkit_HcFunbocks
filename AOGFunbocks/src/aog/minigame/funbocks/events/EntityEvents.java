package aog.minigame.funbocks.events;

import java.util.ArrayList;
import java.util.List;

import me.confuser.barapi.BarAPI;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.instance.FBPlayer;

public class EntityEvents implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void damage(EntityDamageByEntityEvent e){
		
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			
			Player atk = (Player) e.getDamager();
			Player ent = (Player) e.getEntity();
			
			if(Core.playerWasInFunbocks(atk) && Core.playerWasInFunbocks(ent)){			
				
				if(e.isCancelled()){
					e.setCancelled(false);
				}
				
				
				if(Main.currentGame != null && !Main.currentGame.isPvpOn()){
					e.setCancelled(true);
					//atk.sendMessage(ChatColor.RED + "You cannot damage this player while PvP is disabled during this round.");
				}
				
			}
			
		}else if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow){
			
			Arrow arr = (Arrow) e.getDamager();
			Player ent = (Player) e.getEntity();
			// Need to replace this at some point, find alternative.
			if(arr.getShooter() instanceof Player){
			Player atk = (Player) arr.getShooter();
			
			if(Core.playerWasInFunbocks(atk) && Core.playerWasInFunbocks(ent)){
				
				if(e.isCancelled()){
					e.setCancelled(false);
				}
				
				if(Main.currentGame != null && !Main.currentGame.isPvpOn()){
					e.setCancelled(true);
					//atk.sendMessage(ChatColor.RED + "You cannot damage this player while PvP is disabled during this round.");
					}
				
				}
			}
			
		}
		
	}
	
	// Explosion Listener
	@SuppressWarnings("deprecation")
	@EventHandler
	public void quit(EntityExplodeEvent e){

		List<LivingEntity> le = new ArrayList<LivingEntity>();
		boolean fbGame = false;

		if (Main.currentGame != null && Main.currentGame.isBossRound()) {

			for (Entity en : e.getEntity().getNearbyEntities(100, 100, 100)) {

				if (en instanceof LivingEntity) {

					if (en instanceof Player) {

						Player p = (Player) en;

						if (Core.playerWasInFunbocks(p) && Main.currentGame.isStarted() 
								&& !Main.currentGame.isShopping()) {
							fbGame = true;

							le.add((LivingEntity) en);
						}

				
					}

				}

			}

			if (fbGame) {
				e.setCancelled(true);

				for (LivingEntity l : le){
					l.damage(2);
					l.getWorld().playEffect(l.getLocation(), Effect.MOBSPAWNER_FLAMES, 2004);
				}
			}

		}
	}
	
	@EventHandler
	public void denyBlockChange(org.bukkit.event.entity.EntityChangeBlockEvent e){
		//if(e.getEntity().getWorld().getName().equalsIgnoreCase("world_the_end")){
				if(Main.currentGame != null && Main.currentGame.isBossRound()){
					e.setCancelled(true);
				}
	}
		//}
	//}
	
	// Death Listener - Points, removing from list, maybe some drops. (Physical currency? Emeralds?)
	@EventHandler
	public void quit(EntityDeathEvent e){
		
		if(Main.currentGame != null && Main.currentGame.getLivingEntity().contains(e.getEntity())){
			
			if(e.getEntity().getKiller() instanceof Player){
				
				Player p = e.getEntity().getKiller();
				
				if(Core.playerWasInFunbocks(p)){
					
					FBPlayer fp = Core.getFunbocksPlayer(p);
					
					int money = 3;
					
					if(p.getItemInHand() != null && p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)){
						money = getLevelTimes(p.getItemInHand()) * 3;
					}
					
					fp.setBalance(fp.getBalance()+money);
					
					fp.setKills(fp.getKills()+1);
					
					fp.setRoundKills(fp.getRoundKills()+1);
					
					if(money > 3){
						fp.getRawUser().sendMessage(ChatColor.DARK_GREEN + " + $" + money + "! (Looting Bonus x" + getLevelTimes(p.getItemInHand()));
					}else{
						fp.getRawUser().sendMessage(ChatColor.DARK_GREEN + " + $" + money + "!");
					}
				}
				
				e.getDrops().clear();
				
			}
			
			Main.currentGame.getLivingEntity().remove(e.getEntity());
			
			Main.currentGame.getHost().updateInventory();
			
			if(Core.noMoreEnemies() && Main.currentGame.getHost().getPoints() <= 0){
				Main.currentGame.openShop();
			}
			
		}else if(Main.currentGame != null && e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player){
			
			Player p = (Player) e.getEntity();
			Player killer = (Player) e.getEntity().getKiller();
			
			if(Core.playerWasInFunbocks(p) && Core.playerWasInFunbocks(killer)){
				
				FBPlayer fpk = Core.getFunbocksPlayer(killer);
				
				int money = 15;
				
				if(p.getItemInHand() != null && p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)){
					money = getLevelTimes(p.getItemInHand()) * 3;
				}
				
				fpk.setBalance(fpk.getBalance()+money);
				
				if(money > 15){
					fpk.getRawUser().sendMessage(ChatColor.DARK_GREEN + " + $" + money + "! (Looting Bonus x" + getLevelTimes(p.getItemInHand()));
				}else{
					fpk.getRawUser().sendMessage(ChatColor.DARK_GREEN + " + $" + money + "!");
				}
				
				Core.getFunbocksPlayer(p).kill(fpk.getNamedPlayer());
				
				fpk.setRoundKills(fpk.getRoundKills()+1);
				
				Main.currentGame.pvpKill();
				
				Main.currentGame.checkLastPlayer();			
				
			}
			
		}//else if(Main.currentGame != null && e.getEntity() instanceof Player){
			
			//Player p = (Player) e.getEntity();
			
			//if(Core.playerWasInFunbocks(p)){
				
				//if(p.getKiller() != null && p.getKiller() instanceof LivingEntity)
				//	Core.getFunbocksPlayer(p).kill(WordUtils.capitalize(p.getKiller().getType().toString()).replaceAll("_", " "));
				//else
				//	Core.getFunbocksPlayer(p).kill("other causes");
				
				//Main.currentGame.checkLastPlayer();		
				//
			//}
			
		//}
		
	}
	
	@SuppressWarnings("deprecation")
	private int getLevelTimes(ItemStack is) {
		
		for(Enchantment ei : is.getEnchantments().keySet()){
			
			if(ei.getId() == 21){
				return is.getEnchantments().get(ei);
			}
			
		}
		
		return 1;
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent e){
		
		final Player p = e.getEntity();
		
		if(Core.playerWasInFunbocks(p)){
			
			e.setDeathMessage(null);
			
			Main.currentGame.checkLastPlayer();
			
			if(Main.currentGame.getPlayers().values().size() <= 1){
				return;
			}
			
			if(p.getKiller() != null && p.getKiller() instanceof LivingEntity){
				
				String killerString = "";
				
				LivingEntity le = (LivingEntity) p.getKiller();
				
				if(le.getType() == EntityType.PLAYER){
					return;
				}
				
				//if(le instanceof Player){			
				Core.getFunbocksPlayer(p).kill(WordUtils.capitalize(p.getKiller().getType().toString()).replaceAll("_", " ").toLowerCase());
			}else
				Core.getFunbocksPlayer(p).kill("other causes");
			
			Main.currentGame.checkLastPlayer();	
			
			
		}
		
	}
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e){
		
		final Player p = e.getPlayer();
		//Bukkit.broadcastMessage("DEATH");
		
		if(Core.playerWasInFunbocks(p)){
		
			BarAPI.removeBar(p);
			
			//Bukkit.broadcastMessage("IN FB");
			
			final FBPlayer fp = Core.getFunbocksPlayer(p);
			
			if(fp.isDead()){
				//Bukkit.broadcastMessage("IS DEAD");
				new BukkitRunnable() {
					
					@Override
					public void run() {
						fp.restoreLife();
						fp.restoreData();
						
						Main.currentGame.remove(p.getUniqueId().toString());
						Main.currentGame.checkLastPlayer();	
						
						Main.currentGame.getHost().showHostToPlayer(p);
					}
				}.runTaskLater(Main.p, 5);
			}
			
		}else{
			//Bukkit.broadcastMessage("NOT IN FB");
		}
		
	}
	
}
