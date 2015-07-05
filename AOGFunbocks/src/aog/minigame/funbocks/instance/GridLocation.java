package aog.minigame.funbocks.instance;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GridLocation implements Serializable{
	
	private static final long serialVersionUID = 6486968235249387267L;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private String worldName;
	private String name;
	
	public GridLocation(String world, double x, double y, double z, String name){
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldName = world;
		this.yaw = 0;
		this.pitch = 0;
		this.setName(name);
	}
	
	public GridLocation(String world, double x, double y, double z, float pitch, float yaw, String name){
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldName = world;
		this.yaw = yaw;
		this.pitch = pitch;
		this.setName(name);
	}
	
	public GridLocation(Location l) {
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.worldName = l.getWorld().getName();
		this.yaw = l.getYaw();
		this.pitch = l.getPitch();
		this.setName("");
	}

	public Location getBukkitLocation(){		
		Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);
		loc.setYaw(yaw);
		loc.setPitch(pitch);	
		return loc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return "x: " + x +", y: " + y + ", z: " + z + ", world: " + worldName; 
	}

}
