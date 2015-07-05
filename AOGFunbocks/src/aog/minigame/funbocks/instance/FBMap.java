package aog.minigame.funbocks.instance;

import java.io.Serializable;

public class FBMap implements Serializable{

	private static final long serialVersionUID = -7290732307207891317L;
	private String name;
	private GridLocation shop;
	private GridLocation arena;
	private GridLocation spectate;
	private int startPoints;
	private int hostPoints;
	
	public FBMap(){
		this.startPoints = 30;
		this.hostPoints = 100;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GridLocation getShop() {
		return shop;
	}

	public void setShop(GridLocation shop) {
		this.shop = shop;
	}

	public GridLocation getArena() {
		return arena;
	}

	public void setArena(GridLocation arena) {
		this.arena = arena;
	}

	public int getStartPoints() {
		return startPoints;
	}

	public void setStartPoints(int startPoints) {
		this.startPoints = startPoints;
	}

	public int getHostPoints() {
		return hostPoints;
	}

	public void setHostPoints(int hostPoints) {
		this.hostPoints = hostPoints;
	}

	public boolean isValid() {
		
		if(getShop() != null && getArena() != null){
			return true;
		}
		
		return false;
	}

	public GridLocation getSpectate() {
		return spectate;
	}

	public void setSpectate(GridLocation spectate) {
		this.spectate = spectate;
	}

}
