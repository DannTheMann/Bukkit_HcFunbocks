package aog.minigame.funbocks.instance;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.Monsters;

public class FBHost {
	
	private String name;
	private int points;
	private Player rawPlayer;
	private SavedData data;
	
	public FBHost(Player p){
		this.setName(p.getName());
		this.setRawPlayer(p);
		this.setData(new SavedData(p));
		rawPlayer.setAllowFlight(true);
		rawPlayer.setFlying(true);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Player getRawPlayer() {
		return rawPlayer;
	}

	public void setRawPlayer(Player rawPlayer) {
		this.rawPlayer = rawPlayer;
	}

	public SavedData getData() {
		return data;
	}

	public void setData(SavedData data) {
		this.data = data;
	}

	public void restoreData() {
		
		SavedData sd = getData();
		
		getRawPlayer().sendMessage(ChatColor.DARK_GRAY + "Restored your data.");
		
		sd.restoreData();
		
		getRawPlayer().setAllowFlight(true);
		getRawPlayer().setFlying(false);
		
	}

	public void setUpInventory() {

		getRawPlayer().getInventory().setItem(0, Monsters.getBalance(getPoints()));
		
		for(int i = 1; i < Main.currentGame.getRound()+1 && i <= 8; i++){
			getRawPlayer().getInventory().addItem(Core.getHostItem(i));
		}
		
	}

	@SuppressWarnings("deprecation")
	public void updateInventory(int cost) {
		
		this.points = this.points - cost;
		
		getRawPlayer().getInventory().setItem(0, Monsters.getBalance(this.points));
		
		getRawPlayer().updateInventory();
		
		updateBar();
		
	}

	private void updateBar() {
		
		if(points > 0){
			
			float max = 100f;
			
			float value = max - ((max / (Main.currentGame.getHostBalance() * Main.currentGame.getRound())) * points);
			
			if(value > 100f){
				value = 100f;
			}else if( value < 0){
				value = 0f;
			}
			
			//System.out.println("Host Points: " + points);
			//System.out.println("Value for Host : "+ value );
			//System.out.println("MaxPoints: " + (Main.currentGame.getHostBalance() * Main.currentGame.getRound()));	
			
			for(FBPlayer p : Main.currentGame.getPlayers().values()){
				BarAPI.setMessage(p.getRawUser(), ChatColor.GOLD + "Funbocks Host Power", value);
			}
			
		}else{
			int mobs = Main.currentGame.getLivingEntity().size();
			for(FBPlayer p : Main.currentGame.getPlayers().values()){
				BarAPI.setMessage(p.getRawUser(), ChatColor.RED + "No power left, " + mobs + " left to kill.", (float) 0f);
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	public void updateInventory() {
		
		getRawPlayer().getInventory().setItem(0, Monsters.getBalance(this.points));
		
		getRawPlayer().updateInventory();
		
		updateBar();
		
	}
	
	public void showHost(){
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.canSee(rawPlayer)){
				p.hidePlayer(rawPlayer);
			}
		}
		
	}
	
	public void hideHost(){
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.canSee(rawPlayer)){
				p.showPlayer(rawPlayer);
			}
		}
		
	}
	
	public void showHostToPlayer(Player show){
		
		if(!show.canSee(rawPlayer)){
			show.showPlayer(rawPlayer);
		}
		
	}
	
	

}
