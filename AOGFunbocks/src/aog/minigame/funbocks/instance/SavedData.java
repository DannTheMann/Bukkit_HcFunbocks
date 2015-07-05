package aog.minigame.funbocks.instance;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SavedData {
	
	private FBPlayer owner;
	private Player rawOwner;
	private HashMap<Integer, ItemStack> inventory;
	private ItemStack[] armourContents;
	private Location location;
	private float experience;
	private int experienceLevel;
	
	public SavedData(FBPlayer fp){
		Player p = fp.getRawUser();
		this.rawOwner = p;
		this.setInventory(p.getInventory());
		this.setArmourContents(p.getInventory().getArmorContents().clone());
		this.setLocation(p.getLocation());
		this.setExperience(p.getExp());
		this.setExperienceLevel(p.getLevel());
		
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		p.setExp(0);
		p.setLevel(0);
		p.setFoodLevel(20);
		p.setHealth(20.0);
	}

	public SavedData(Player p) {
		this.rawOwner = p;
		this.setInventory(p.getInventory());
		this.setArmourContents(p.getInventory().getArmorContents());
		this.setLocation(p.getLocation());
		this.setExperience(p.getExp());
		this.setExperienceLevel(p.getLevel());
		
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		p.setExp(0);
		p.setLevel(0);
		p.setFoodLevel(20);
		p.setHealth(20.0);
		
	}

	public FBPlayer getOwner() {
		return owner;
	}

	public void setOwner(FBPlayer owner) {
		this.owner = owner;
	}

	public HashMap<Integer, ItemStack> getInventory() {
		return this.inventory;
	}

	public void setInventory(Inventory inventory) {
		
		this.inventory = new HashMap<>();
		
		for(int i = 0; i < inventory.getSize(); i++)
			if(inventory.getItem(i) != null)
				this.inventory.put(i, inventory.getItem(i));
		
	}

	public ItemStack[] getArmourContents() {
		return armourContents;
	}

	public void setArmourContents(ItemStack[] armourContents) {
		this.armourContents = armourContents;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public float getExperience() {
		return experience;
	}

	public void setExperience(float experience) {
		this.experience = experience;
	}

	public int getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(int experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

	public void restoreData() {

		Player p = this.rawOwner;
		
		p.getInventory().clear();
		
		for(int i : inventory.keySet())
			p.getInventory().setItem(i, inventory.get(i));
		
		p.getInventory().setArmorContents(getArmourContents());
		p.setExp(getExperience());
		p.setLevel(getExperienceLevel());
		
		p.teleport(getLocation());
		
		p.setFoodLevel(20);
		p.setHealth(20.0);
		
	}

	public Player getRawOwner() {
		return rawOwner;
	}

	public void setRawOwner(Player rawOwner) {
		this.rawOwner = rawOwner;
	}

}
