package aog.minigame.funbocks.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;

public class FBPlayer {
	
	private String id;
	private String name;
	private int kills;
	private int roundKills;
	private int balance;
	private int rewardMoney;
	private Player player;
	private SavedData data;
	private List<ItemStack> rewards = new ArrayList<>();
	
	public FBPlayer(Player p){
		this.player = p;
		this.name = p.getName();
		this.id = p.getUniqueId().toString();
		this.setData(new SavedData(this));
		//this.balance = Main.currentGame.getStartingBalance();
		
		Main.currentGame.teleportUserToShop(getPlayer());
		
		Main.currentGame.announceAllPlayers(Main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GREEN
				+ " joined Funbocks!");
		
		message(ChatColor.GREEN + " > Welcome to Funbocks! This is a mob survival minigame, earn money by surviving rounds " +
				"and killings mob then spend this money on items to better equip yourself for the round. Win to gain awesome goodies!");
	}
	
	public Player getRawUser() {
		return Bukkit.getPlayer(UUID.fromString(id));
	}
	
	public String getNamedPlayer(){
		return this.name;
	}
	
	public String getId(){
		return id;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public SavedData getData() {
		return data;
	}

	public void setData(SavedData data) {
		this.data = data;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void leaveGame() {
		
		BarAPI.removeBar(getRawUser());
		
		Main.currentGame.getPlayers().remove(id);
		
		SavedData sd = getData();
		
		getRawUser().sendMessage(ChatColor.DARK_GRAY + "Restored your data.");
		
		sd.restoreData();
		
		Core.ClearPotionEffects(getRawUser());
		
		getRawUser().sendMessage(Main.prefix + ChatColor.RED + "You quit the game. Your kills were not saved.");
		
		Main.currentGame.announceAllPlayers(Main.prefix + ChatColor.GOLD + getRawUser().getName() + ChatColor.RED + " quit Funbocks!");
		
		if(Main.currentGame.isStarted()){
			Main.currentGame.checkLastPlayer();
		}
		
	}
	
	public void forceLeaveGame() {
		
		BarAPI.removeBar(getRawUser());
		
		Main.currentGame.getPlayers().remove(id);
		
		SavedData sd = getData();
		
		getRawUser().sendMessage(ChatColor.DARK_GRAY + "Restored your data.");
		
		sd.restoreData();
		
		getRawUser().sendMessage(Main.prefix + ChatColor.RED + "You were forced to leave the game.");
		
		if(Main.currentGame.isStarted()){
			Main.currentGame.checkLastPlayer();
		}
		
	}

	public void restoreData() {

		Main.currentGame.getPlayers().remove(id);
		
		SavedData sd = getData();
		
		if(getKills() > 10)
		Main.data.getHighestKills().put(getKills(), getId());
		
		Player p = getPlayer();
		
		Core.ClearPotionEffects(p);
		
		getRawUser().sendMessage(ChatColor.DARK_GRAY + "Restored your data.");
		
		sd.restoreData();
		
		Main.currentGame.getHost().showHostToPlayer(p);
		
	}

	public void restoreLife() {
	
		Player p = getPlayer();
		
		p.setHealth(20.0);
		p.setFoodLevel(20);
		
		Core.ClearPotionEffects(p);
		
	}

	public List<ItemStack> getRewards() {
		return rewards;
	}

	public void setRewards(List<ItemStack> rewards) {
		this.rewards = rewards;
	}

	public int getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(int rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public void withdrawMoney(int cash) {
		
		this.balance -= cash;
		
	}

	private boolean dead;
	
	public void kill(String reason) {
		
		BarAPI.removeBar(getRawUser());
		
		//Main.currentGame.getPlayers().remove(id);
		
		Main.currentGame.announceAllPlayers(Main.prefix + ChatColor.RED + getNamedPlayer() + " was killed by " + 
		reason + "!");
		
		dead = true;
		
		getRawUser().sendMessage(Main.prefix + ChatColor.RED + "Bad luck! You were killed! Total kills: " + getKills() + ". You won $" + rewardMoney);
		
		Main.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(id)), rewardMoney);
		
		if(getKills() > 10)
			Main.data.getHighestKills().put(getKills(), getId());
		
		if(Main.currentGame.isStarted()){
			Main.currentGame.checkLastPlayer();
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (Main.currentGame != null) {
					Main.currentGame.checkLastPlayer();

					Main.currentGame.remove(getRawUser().getUniqueId().toString());
				}
			}
		}.runTaskLater(Main.p, 1200);
	}

	public void message(String msg) {
		if(Bukkit.getPlayer(UUID.fromString(id)) != null)
			Bukkit.getPlayer(UUID.fromString(id)).sendMessage(Main.prefix + msg);
	}

	public void win(final ItemStack prize) {
		
		getRawUser().closeInventory();
		
		//getRawUser().closeInventory();
		
		Main.p.getServer().getScheduler().scheduleSyncDelayedTask(Main.p, new Runnable() {
			
			@Override
			public void run() {			
				restoreData();
				getRawUser().getInventory().addItem(prize);
				message(ChatColor.GREEN + "You won! Well done! You've been given your reward. Total kills: " + getKills());
				Main.data.setCurrentChampion(getId());
				
				Main.currentGame.endGame();
				getRawUser().closeInventory();
				
			}
		},10);
	
	}

	public int getRoundKills() {
		return roundKills;
	}

	public void setRoundKills(int roundKills) {
		this.roundKills = roundKills;
	}

	public boolean isDead() {
		return dead;
	}
	

}
