package aog.minigame.funbocks.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.Serial;
import aog.minigame.funbocks.ValueComparator;
import aog.minigame.funbocks.events.FBWinnerEvents;

public class FBGame {
	
	private static final int MAX_PLAYERS_FOR_PVP = 10;
	public static List<ItemStack> item = new ArrayList<>();
	public static List<FBBoss> bosses = new ArrayList<>();
	
	private int startingBalance;
	private int hostBalance;
	private int maxPlayers;
	private int round;
	private int timer;
	private int schedulerId;
	private int count;
	private int lootCounter;
	private BukkitTask lootTask;
	private BukkitTask mobCheckingTask;
	private boolean pvpOn;
	private boolean started;
	private boolean shopping;
	private boolean automatic;
	private FBHost host;
	private HashMap<String, FBPlayer> players = new HashMap<>();
	private List<LivingEntity> livingEntity = new ArrayList<LivingEntity>();
	private List<LivingEntity> aliveBosses = new ArrayList<LivingEntity>();
	private ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
	private int originalStartingPlayers;
	private int playersToDie;
	private FBMap map;
	//
	private int lootId;
	
	public FBGame(String mapName, Player host){
		
		item = new ArrayList<ItemStack>();
		bosses = new ArrayList<>();
		
		Core.loadBosses();
		Core.loadItems();
		
		setMap(Core.getMap(mapName));
		setHost(new FBHost(host));
		setStartingBalance(getMap().getStartPoints());
		setHostBalance(getMap().getHostPoints());
		
		Bukkit.broadcastMessage("                          -=- " + Main.prefix + "  -=-           "); 
		Bukkit.broadcastMessage(ChatColor.GREEN + "A game of " + ChatColor.GOLD + "Funbocks " + ChatColor.GREEN
				 + "has been started by " + ChatColor.GOLD + host.getName() + ChatColor.GREEN + ", come join and win prizes money " +
				 		"in this all out " + ChatColor.RED + "mob killing minigame!.");
		//Bukkit.broadcastMessage(ChatColor.AQUA + "Funbocks is a fun game involving mob survival and rounds," +
				//" winning gains you prizing and fame! You don't need anything to join, so come give it a try!");
		Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GREEN + " >> To join do /fb join");
		
		teleportUserToArena(host);
		
		host.sendMessage(Main.prefix + ChatColor.GREEN + " ++ To start the game do /fb run, it's advised you wait a few minutes and " +
				"gather some players!");
		
		aliveBosses = new ArrayList<LivingEntity>();
		
		getHost().hideHost();
	}
	
	public FBGame(FBMap map){
		
		item = new ArrayList<ItemStack>();
		bosses = new ArrayList<>();
		
		Core.loadBosses();
		Core.loadItems();
		
		setMap(map);
		//setHost(new FBHost(host));
		setStartingBalance(getMap().getStartPoints());
		setHostBalance(getMap().getHostPoints());
		
		Bukkit.broadcastMessage("             -=- " + Main.prefix + "  -=-           "); 
		Bukkit.broadcastMessage(Main.prefix + ChatColor.GREEN + "A game of " + ChatColor.GOLD + "Funbocks " + ChatColor.GREEN
				 + "has been started " + ChatColor.GOLD + "automatically" + ChatColor.GREEN + ".");
		//Bukkit.broadcastMessage(ChatColor.AQUA + "Funbocks is a fun game involving mob survival and rounds," +
				//" winning gains you prizing and fame! You don't need anything to join, so come give it a try!");
		Bukkit.broadcastMessage(ChatColor.BOLD + "" + ChatColor.GREEN + " >> To join do /fb join");
		
		//teleportUserToArena(host);
		
		//host.sendMessage(Main.prefix + ChatColor.GREEN + " ++ To start the game do /fb run, it's advised you wait a few minutes and " +
				//"gather some players!");
		
		automatic = true;
		
		count = 0;
		
		setTimer(Main.p.getServer().getScheduler().scheduleSyncRepeatingTask(Main.p, new Runnable() {
			
			@Override
			public void run() {
				
				if(getPlayers().size() <= 10){
					count++;
					
					if(count > 3){						
						endGame();
					}
					
				}else{
			
					pickRandomHost();
					
				}
				
			}
		}, 20, (20*60)*2));
		
	}

	protected void pickRandomHost() {
		
		FBPlayer[] values = (FBPlayer[]) players.values().toArray();
		FBPlayer randomValue = values[new Random().nextInt(values.length)];
		
		host = new FBHost(randomValue.getRawUser());
		
		randomValue.message("You've been selected as the host of Funbocks!");
		
		///////players.remove(host);
		
	}

	public void teleportUserToArena(Player user) {
		
		user.teleport(getMap().getArena().getBukkitLocation());
		
		user.sendMessage(ChatColor.GREEN + "Teleported you to Arena.");
		
	}
	
	public void teleportUserToShop(Player user){
		
		if(user == null)
			Bukkit.broadcastMessage("User is null");
		if(getMap() == null)
			Bukkit.broadcastMessage("Map is null");
		if(getMap().getShop().getBukkitLocation() == null)
			Bukkit.broadcastMessage("Shop is null");
		
		user.teleport(getMap().getShop().getBukkitLocation());
		
		user.sendMessage(ChatColor.GREEN + "Teleported you to Shop.");
		
	}
	

	public void teleportUserToSpawn(Player user) {
	
		user.teleport(Main.data.getSpawn().getBukkitLocation());
			
	}

	public int getStartingBalance() {
		return startingBalance;
	}

	public void setStartingBalance(int startingBalance) {
		this.startingBalance = startingBalance;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isPvpOn() {
		return pvpOn;
	}

	public void setPvpOn(boolean pvpOn) {
		this.pvpOn = pvpOn;
	}

	public FBHost getHost() {
		return host;
	}

	public void setHost(FBHost host) {
		this.host = host;
	}

	public FBMap getMap() {
		return map;
	}

	public void setMap(FBMap map) {
		this.map = map;
	}

	public int getHostBalance() {
		return hostBalance;
	}

	public void setHostBalance(int hostBalance) {
		this.hostBalance = hostBalance;
	}

	public int getOriginalStartingPlayers() {
		return originalStartingPlayers;
	}

	public void setOriginalStartingPlayers(int originalStartingPlayers) {
		this.originalStartingPlayers = originalStartingPlayers;
	}

	public HashMap<String, FBPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(HashMap<String, FBPlayer> players) {
		this.players = players;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void announceAllPlayers(String message) {
		
		for(FBPlayer fp : getPlayers().values())
			fp.getPlayer().sendMessage(message);
		
		getHost().getRawPlayer().sendMessage(message);
		
	}

	public void run() {
		
		setStarted(true);
		
		announceAllPlayers(Main.prefix + ChatColor.DARK_GREEN + 
				"Funbocks has started! You've got 30 seconds to purchase your equipment to fight off the round." +
				" Your starting balance for this map is " + ChatColor.YELLOW + "$" + getStartingBalance() + ChatColor.GREEN + "." +
						" Good Luck!" );
		
		openShop();
		
		setOriginalStartingPlayers(getPlayers().size());
		
	}

	public void openShop() {
		
		aliveBosses.clear();
		
		showHighestKills();
		
		if(mobCheckingTask != null){
			mobCheckingTask.cancel();
		}
		
		for(FBPlayer p : getPlayers().values()){
			p.message(ChatColor.GREEN + "Balance: $" + p.getBalance());
			BarAPI.removeBar(p.getRawUser());
		}
		
		BarAPI.removeBar(getHost().getRawPlayer());
		
		for(LivingEntity le : getLivingEntity())
			le.remove();
		
		getLivingEntity().clear();
		
		for(LivingEntity le : aliveBosses)
			le.remove();
		
		aliveBosses.clear();
		
		for(FBPlayer fp : Main.currentGame.getPlayers().values()){
			fp.setBalance(fp.getBalance()+getStartingBalance());
			fp.setRewardMoney(fp.getRewardMoney()+1000);
			//fp.getRewards().add(new ItemStack(Material.DIAMOND, 3));
			fp.restoreLife();
		}
		
		if(getRound() > 0){
			for(FBPlayer fp : Main.currentGame.getPlayers().values()){
				if(fp.getPlayer() != null && !fp.getPlayer().isDead()){
					teleportUserToShop(fp.getPlayer());
				}
			}
		}
		
		announceAllPlayers(ChatColor.GOLD + "You can left-click to buy items from signs, and right-click to see pricing and what they are!");
			
		getHost().getRawPlayer().getInventory().clear();
		teleportUserToShop(getHost().getRawPlayer());
		
		setCount(0);
		setShopping(true);
		setPvpOn(false);
		
		if(getRound() > 1){
			for (FBPlayer f : getPlayers().values()) {
				f.getRawUser().sendMessage(
						ChatColor.YELLOW + " > Total Kills: " + f.getKills());
				f.getRawUser().sendMessage(
						ChatColor.YELLOW + " > Round Kills: " + f.getRoundKills());
				f.getRawUser().sendMessage(
						ChatColor.YELLOW + " > Winnings $" + (f.getRoundKills()*3+30));
				f.getRawUser().sendMessage(
						ChatColor.YELLOW + " > Balance: $" + f.getBalance());
			}
		}
		

		for (FBPlayer fp : getPlayers().values()) {
			fp.setRoundKills(0);
		}
		
		setSchedulerId(Main.p.getServer().getScheduler().scheduleSyncRepeatingTask(Main.p, new Runnable() {
			
			@Override
			public void run() {
				
				setCount(getCount()+1);
				
				for(FBPlayer p : getPlayers().values()){
					BarAPI.setMessage(p.getRawUser(), ChatColor.GREEN + "Time to Shop " + (30-getCount()) + "s", calculateTimePercentage());
				}
				
				BarAPI.setMessage(getHost().getRawPlayer(), ChatColor.GREEN + "Time to Shop " + (30-getCount()) + "s", calculateTimePercentage());				
				
				//if(getCount() % 15 == 0)
					//if((60-getCount()) != 0)
					//announceAllPlayers(Main.prefix + ChatColor.YELLOW + (60-getCount()) + " seconds until next round commences.");
				
				if(getCount() > 30){
					setShopping(false);
					nextRound();
				}
				
			}
		}, 0, 20));
		
	}

	protected float calculateTimePercentage() {
		
		int count = getCount();
		
		float max = 100f;
		
		float value = max - ((max / 30) * count);
		
		if(value < 1){
			return 1f;
		}
		
		return value;
		
	}

	public void nextRound() {
		
		if(lastPlayer()){
			checkLastPlayer();
			return;
		}
		
		mobCheckingTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(getLivingEntity() == null){
					livingEntity = new ArrayList<LivingEntity>();
				}
				
				ArrayList<LivingEntity> remove = new ArrayList<LivingEntity>();
				
				
				for(LivingEntity le : aliveBosses){
					if(le.isDead() || le == null){
						remove.add(le);
					}
				}
				
				for(LivingEntity le : getLivingEntity()){
					if(le.isDead() || le == null){
						remove.add(le);
					}
				}
				
				for(LivingEntity le : remove){
					getLivingEntity().remove(le);
					getHost().updateInventory();
				}
				
				if(lastPlayer()){
					checkLastPlayer();
					cancel();
					return;
				} 
				
				if(getLivingEntity().isEmpty() && (getHost().getPoints() <= 0 || isBossRound() || pvpOn)){
					if(aliveBosses.isEmpty() && playersToDie <= 0){
						openShop();
					}
				}
				
			}
		}.runTaskTimer(Main.p, 200, 100);
		
		for(FBPlayer p : getPlayers().values()){
			BarAPI.removeBar(p.getRawUser());
		}	
		
		BarAPI.removeBar(getHost().getRawPlayer());
		
		Main.p.getServer().getScheduler().cancelTask(getSchedulerId());
			
		
		for(FBPlayer fp : Main.currentGame.getPlayers().values())
			fp.restoreLife();
		
		setRound(getRound()+1);
		
		announceAllPlayers(Main.prefix + ChatColor.YELLOW + "Round " + getRound());
		
		for(FBPlayer fp : Main.currentGame.getPlayers().values())
			teleportUserToArena(fp.getPlayer());
		
		teleportUserToArena(getHost().getRawPlayer());
		
		getHost().setPoints(getMap().getHostPoints()*getRound());
		
		if(getRound() % 3 == 0)
			dropLoot();
		
		if(getRound() % 5 == 0)
			bossRound();
		else{
			
			if(Core.generateRandomNumber(10) == 1 && getPlayers().size() > MAX_PLAYERS_FOR_PVP && getRound() > 1){
				pvpRound();
				return;
			}			
			
			
			Main.p.getServer().getScheduler().scheduleSyncDelayedTask(Main.p, new Runnable() {
				
				@Override
				public void run() {
					getHost().setUpInventory();
					
					if(getRound() % 5 == 0 || pvpOn){
						getHost().setPoints(0);
						getHost().updateInventory();
					}
					
					announceAllPlayers(Main.prefix + ChatColor.YELLOW + "Fight!");

				}
			}, 60);
			
		}
		
	}

	private void showHighestKills() {
		
		if (getRound() <= 1) {
			return;
		}

		HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (FBPlayer f : getPlayers().values())
			map.put(f.getNamedPlayer(), f.getRoundKills());

		ValueComparator bvc = new ValueComparator(map);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

		sorted_map.putAll(map);

		String s = sorted_map.toString();
		//String t = "";

		//System.out.println("T size = " + t.split(",").length);

		int maxPlayers = 5;

		if (maxPlayers > getPlayers().values().size())
			maxPlayers = getPlayers().size();

		// ================ Top Killers for Round 1 ================
		// ================

		for (FBPlayer f : getPlayers().values()) {
			f.getRawUser().sendMessage("");
			f.getRawUser().sendMessage(
					ChatColor.GOLD + "                  Top "
							+ ChatColor.YELLOW + "Killers" + ChatColor.GOLD
							+ " for Round " + getRound() + "");

			for (int i = 0; i < maxPlayers; i++) {
				f.getRawUser().sendMessage(
						ChatColor.YELLOW
								+ "     "
								+ (i + 1)
								+ ". "
								+ s.split(",")[i].split("=")[0].replace("{",
										" ")
								+ ", Kills: "
								+ s.split(",")[i].split("=")[1]
										.replace("}", ""));

			}
			f.getRawUser().sendMessage("");
		}

		getHost().getRawPlayer().sendMessage("");
		getHost().getRawPlayer().sendMessage(
				ChatColor.GOLD + "                  Top " + ChatColor.YELLOW
						+ "Killers" + ChatColor.GOLD + " for Round "
						+ getRound() + "");
		for (int i = 0; i < maxPlayers; i++) {
			getHost().getRawPlayer().sendMessage(
					ChatColor.YELLOW + "     " + (i + 1) + ". "
							+ s.split(",")[i].split("=")[0].replace("{", " ")
							+ ", Kills: "
							+ s.split(",")[i].split("=")[1].replace("}", ""));
			getHost().getRawPlayer().sendMessage("");
		}
		
	}

	private void pvpRound() {

		Main.p.getServer().getScheduler().scheduleSyncDelayedTask(Main.p, new Runnable() {

			@Override
			public void run() {

				setPvpOn(true);

				int playersToKill = getPlayers().size()/MAX_PLAYERS_FOR_PVP;

				announceAllPlayers(Main.prefix + ChatColor.RED + "PVP ROUND! " + playersToKill + " players must die to end the round.");

				setPlayersToDie(playersToKill);

			}
		}, 60);

	}
	
	public void checkLastPlayer(){
		
		if(getPlayers().size() == 1){
			
			FBPlayer p = null;
			
			for(FBPlayer f : getPlayers().values())
				if(f != null)
					p = f;
			
			declareWinner(p);
			
		}else if(getPlayers().size() == 0){
			
			getHost().getRawPlayer().sendMessage(ChatColor.RED + " >> All players left the game, what a bunch of cowards.");
			
			forceEnd();
			
		}
		
	}
	
	public boolean lastPlayer(){
		
		if(getAlivePlayers().size() <= 1)
			return true;
		
		return false;
		
	}

	private ArrayList<FBPlayer> getAlivePlayers() {
		
		ArrayList<FBPlayer> players = new ArrayList<FBPlayer>();
		
		for(FBPlayer fp : this.players.values()){
			if(!fp.isDead()){
				players.add(fp);
			}
		}
		
		return players;
		
	}

	private boolean won;
	
	public void declareWinner(final FBPlayer fb) {
		
		mobCheckingTask.cancel();
		
		for(FBPlayer p : getPlayers().values()){
			BarAPI.removeBar(p.getRawUser());
		}
		
		for(LivingEntity le : getLivingEntity())
			le.remove();
		
		if(won){
			return;
		}
		
		won = true;
		
		getLivingEntity().clear();	
		
		getHost().restoreData();
		
		fb.restoreLife();
		
		Bukkit.broadcastMessage(Main.prefix + ChatColor.GREEN + fb.getNamedPlayer() + " has Won the game of Funbocks! Survivng a total of " +
				getRound() + " Rounds! " + getOriginalStartingPlayers() + " Players originally entered and " + fb.getNamedPlayer() + " stood "
				+ "above them all in the game of Funbocks!");
		
		
		Main.p.getServer().getScheduler().scheduleSyncDelayedTask(Main.p, new Runnable() {
			
			@Override
			public void run() {
				
				Core.showWinningMenu(fb);
				
			}
		}, 20);
		
	}

	private void bossRound() {

		final FBBoss boss = bosses.get(Core.generateRandomNumber(bosses.size()-1));
		
		announceAllPlayers(Main.prefix + ChatColor.RED + "Boss Round! Get Ready!");
		
		aliveBosses.clear();
		
		Main.p.getServer().getScheduler().scheduleSyncDelayedTask(Main.p, new Runnable() {
			
			@Override
			public void run() {
				announceAllPlayers(Main.prefix + "A wild " + ChatColor.RED + boss.getName() + " has appeared!");
				
				aliveBosses = boss.spawn();
			}
		},100);
		
	}

	private void dropLoot() {
		
		announceAllPlayers(Main.prefix + ChatColor.GREEN + "Loot Round!");

		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for(final FBPlayer fp : getPlayers().values()){
					
					if(fp != null && fp.getRawUser() != null){
						
					fp.getRawUser().getWorld().dropItem(new Location(fp.getRawUser().getWorld(), fp.getRawUser().
							getLocation().getBlockX(), fp.getRawUser().getLocation().getBlockY()+5,
							fp.getRawUser().getLocation().getZ()), item.get(Core.generateRandomNumber(item.size()-1)));
					
					}
					
				}
				
				lootCounter++;
				
				if(lootCounter >= 3){
					cancel();
				}
				
			}
		}.runTaskTimer(Main.p, 100, 60);
		
					

	}

	public boolean isShopping() {
		return shopping;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public void forceEnd() {
	
		if(mobCheckingTask != null){
			mobCheckingTask.cancel();
		}
		
		for(FBPlayer p : getPlayers().values()){
			BarAPI.removeBar(p.getRawUser());
		}
		
		announceAllPlayers(Main.prefix + ChatColor.RED + ChatColor.BOLD + "Game has been forcefully ended, sorry about that. :/");
		
		Main.p.getServer().getScheduler().cancelTask(getSchedulerId());
		
		ArrayList<FBPlayer> players = new ArrayList<>();
		
		for(FBPlayer fp : Main.currentGame.getPlayers().values()){
			players.add(fp);
		}
		
		for(FBPlayer fp : players){
			fp.restoreData();
		}
		
		getHost().restoreData();
		
		getHost().showHost();
		
		for(LivingEntity le : getLivingEntity())
			le.remove();
		
		getLivingEntity().clear();
		
		Main.currentGame = null;
		FBWinnerEvents.winner = null;
		FBWinnerEvents.inven = null;
		
	}

	public List<LivingEntity> getLivingEntity() {
		return livingEntity;
	}

	public void setLivingEntity(List<LivingEntity> livingEntity) {
		this.livingEntity = livingEntity;
	}

	public int getPlayersToDie() {
		return playersToDie;
	}

	public void setPlayersToDie(int playersToDie) {
		this.playersToDie = playersToDie;
	}

	public void pvpKill() {
		
		this.playersToDie--;
		
		if(this.playersToDie <= 0){
			
			pvpOn = false;
			
			announceAllPlayers(ChatColor.DARK_GREEN + "PvP Round is over. You survived, well done.");
			
			new BukkitRunnable() {
				
				@Override
				public void run() {			
					openShop();
				}
			}.runTaskLater(Main.p, 100);

		}
		
	}

	public ArrayList<ItemStack> getRewards() {
		return rewards;
	}

	public void setRewards(ArrayList<ItemStack> rewards) {
		this.rewards = rewards;
	}

	public void endGame() {
		
		mobCheckingTask.cancel();
		
		for(FBPlayer p : getPlayers().values()){
			BarAPI.removeBar(p.getRawUser());
		}
		
		Main.p.getServer().getScheduler().cancelTask(getSchedulerId());
		Main.p.getServer().getScheduler().cancelTask(getTimer());
		
		if(getPlayers().size() > 1){
			Bukkit.broadcastMessage(Main.prefix + "The game has ended, and the winner was - " + 
					ChatColor.GREEN + FBWinnerEvents.winner.getName() + "!");
		}
		
		for(FBPlayer fp : getPlayers().values()){
			fp.restoreData();
		}
		
		if(getHost() != null){
			getHost().restoreData();
			getHost().showHost();
		}
		
		for(LivingEntity le : getLivingEntity()){
			le.remove();
		}
		
		getLivingEntity().clear();
		
		if (FBWinnerEvents.winner != null) {
			if (!(Main.data.getHighestRound() == null)
					&& !Main.data.getHighestRound().containsKey(getRound())) {
				Main.data.getHighestRound().put(getRound(),
						FBWinnerEvents.winner.getUniqueId().toString());
			}
			if (Main.data.getHighestKills() != null
					&& !Main.data.getHighestKills().containsKey(
							Core.getFunbocksPlayer(FBWinnerEvents.winner)
									.getKills())) {
				Main.data.getHighestKills().put(
						Core.getFunbocksPlayer(FBWinnerEvents.winner)
								.getKills(),
						FBWinnerEvents.winner.getUniqueId().toString());
			}
		}
		
		FBWinnerEvents.winner = null;
		FBWinnerEvents.inven = null;
		Main.currentGame = null;
		
		Serial.saveDataFile();
		
	}

	public int getLootId() {
		return lootId;
	}

	public void setLootId(int lootId) {
		this.lootId = lootId;
	}

	public int getLootCounter() {
		return lootCounter;
	}

	public void setLootCounter(int lootCounter) {
		this.lootCounter = lootCounter;
	}

	public boolean isAutomatic() {
		return automatic;
	}

	public void remove(String id) {
		players.remove(id);
	}

	public boolean isBossRound() {
		
		return round % 5 == 0;
		
	}
	
	

}
