package aog.minigame.funbocks.instance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FBData implements Serializable{
	
	private static final long serialVersionUID = 8654208269974326055L;
	private HashMap<String, FBMap> maps = new HashMap<>();
	private HashMap<Integer, String> highestKills = new HashMap<>(); // Key = Score, Value = Name
	private HashMap<Integer, String> highestRound = new HashMap<>(); // VALUE = Round winner.
	private ArrayList<String> bannedPlayers = new ArrayList<String>();
	private String currentChampion = null;
	
	private GridLocation spawn;
	
	public HashMap<Integer, String> getHighestRound() {
		return highestRound;
	}
	public void setHighestRound(HashMap<Integer, String> highestRound) {
		this.highestRound = highestRound;
	}
	public HashMap<Integer, String> getHighestKills() {
		return highestKills;
	}
	public void setHighestKills(HashMap<Integer, String> highestKills) {
		this.highestKills = highestKills;
	}
	public HashMap<String, FBMap> getMaps() {
		return maps;
	}
	public void setMaps(HashMap<String, FBMap> maps) {
		this.maps = maps;
	}
	public GridLocation getSpawn() {
		return spawn;
	}
	public void setSpawn(GridLocation spawn) {
		this.spawn = spawn;
	}
	public String getCurrentChampion() {
		return currentChampion;
	}
	public void setCurrentChampion(String currentChampion) {
		this.currentChampion = currentChampion;
	}
	public ArrayList<String> getBannedPlayers() {
		return bannedPlayers;
	}
	public void setBannedPlayers(ArrayList<String> bannedPlayers) {
		this.bannedPlayers = bannedPlayers;
	}
	public FBMap getRandomMaps() {
		
		if(maps.isEmpty()){
			return null;
		}
		
		return maps.get(new Random().nextInt(maps.size()));
	
	}
	public boolean mapIsValid(String a2) {
		
		FBMap map = getMaps().get(a2);
		
		if(map != null){
			
			if(map.isValid()){
				return true;
			}
			
		}
		
		return false;
	}

}
