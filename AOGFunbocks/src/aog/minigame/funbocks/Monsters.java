package aog.minigame.funbocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Monsters {

	public static ItemStack getZombie() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 54);
		is = Core.nameItemStack(is, ChatColor.RED + "Zombie [1 p]");	
		return is;
	}

	public static ItemStack getSkeleton() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 51);
		is = Core.nameItemStack(is, ChatColor.RED + "Skeleton [2 p]");		
		return is;
		
	}

	public static ItemStack getSpider() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 52);
		is = Core.nameItemStack(is, ChatColor.RED + "Spider [6 p]");		
		return is;
		
	}

	public static ItemStack getCaveSpider() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 59);
		is = Core.nameItemStack(is, ChatColor.RED + "Cave Spider [9 p]");		
		return is;
		
	}

	public static ItemStack getBlaze() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 61);
		is = Core.nameItemStack(is, ChatColor.RED + "Blaze [10 p]");		
		return is;
		
	}
 
	public static ItemStack getMagmaCube() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 62);
		is = Core.nameItemStack(is, ChatColor.RED + "Magma Cube [30 p]");		
		return is;
		
	}

	public static ItemStack getWitch() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 66);
		is = Core.nameItemStack(is, ChatColor.RED + "Witch [45 p]");		
		return is;
		
	}

	public static ItemStack getGiant() {
		ItemStack is = new ItemStack(Material.MONSTER_EGG);
		is.setDurability((short) 53);
		is = Core.nameItemStack(is, ChatColor.RED + "Giant [100 p]");		
		return is;
		
	}

	public static ItemStack getBalance(int points) {
		ItemStack is = new ItemStack(Material.BEACON);
		is.setDurability((short) 55);
		is = Core.nameItemStack(is, ChatColor.GREEN + "Balance: " + points);
		Core.loreItemStack(is, ChatColor.RED + "Round: " + Main.currentGame.getRound());
		Core.loreItemStack(is, "Total Mobs: " + Main.currentGame.getLivingEntity().size());
		Core.loreItemStack(is, "Players Alive: " + Main.currentGame.getPlayers().size() +
				"/" + Main.currentGame.getOriginalStartingPlayers());
		return is;
	}
	
}
