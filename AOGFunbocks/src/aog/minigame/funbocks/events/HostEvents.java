package aog.minigame.funbocks.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;

public class HostEvents implements Listener {
	
	@EventHandler
	public void hostInventoryClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(Core.playerIsHost(p))
			e.setCancelled(true);

	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player){
			if(e.getEntity() instanceof Monster){
				Player p = (Player)e.getTarget();
				if(Core.playerIsHost(p))
					e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void hostBreakBlock(BlockBreakEvent e){
		Player p = (Player) e.getPlayer();
		if(Core.playerIsHost(p))
			e.setCancelled(true);
	}


	
	public void hostDamage(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			if(Core.playerIsHost(p))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void hostDamageByOthers(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(Core.playerIsHost(p))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void hostDropItem(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if(Core.playerIsHost(p))
			e.setCancelled(true);
	}

	@EventHandler
	public void hostPickUpItem(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		if(Core.playerIsHost(p))
			e.setCancelled(true);
	}

	@EventHandler
	public void hostFly(PlayerInteractEvent e){
		final Player p = e.getPlayer();
		if(Core.playerIsHost(p)){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				e.setCancelled(true);
				return;
			}
			if(p.isFlying()){
				return;
			}
			p.setAllowFlight(true);
			p.setFlying(true);
		}
	}
	
	private static final int MAX_MOBS_ALLOWED = 50;
	private static final int RADIUS = 25;
	
	@EventHandler
	public void SpawnCreature(PlayerInteractEvent e){
		Player p = e.getPlayer();	
		Action a = e.getAction();
		if(Core.playerIsHost(p) && Core.gameIsOn()){
			if(p.getItemInHand().getType() == Material.MONSTER_EGG){

				int i = 1;
				
				if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)
					i = 10;
				
				if(!checkBalance(p, Core.getCostPoints(p.getItemInHand().getDurability()) * i))
					return;
				
				@SuppressWarnings("deprecation")
				Location loc = p.getTargetBlock(null, 100).getLocation();
				loc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ());
					
				int mobCount = 0;
				
				@SuppressWarnings("deprecation")
				EntityType et = EntityType.fromId(p.getItemInHand().getDurability());
				
				for(Entity e2 : p.getNearbyEntities(RADIUS, RADIUS, RADIUS)){
					
					if(e2.getType() == et){
						mobCount++;
					}
					
				}
				
				if(mobCount > MAX_MOBS_ALLOWED){
					p.sendMessage(Main.prefix + ChatColor.RED + " - There are " + mobCount + 
							et.toString().toLowerCase().replaceAll("_", " ") + " near here." +
									" You can only have 50 in a radius of " + RADIUS + ".");
					return;
				}
				
				int cost = Core.getCostPoints(p.getItemInHand().getDurability()) * i;
				
				for(int x = 0; x < i; x++)
					spawnMob(p.getItemInHand().getDurability(), loc);

				Main.currentGame.getHost().updateInventory(cost);
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void spawn(CreatureSpawnEvent e){
		
		if(Main.currentGame != null){
			
			if (Main.currentGame.getMap().getArena().getBukkitLocation()
					.getWorld().getName().equalsIgnoreCase(e.getEntity().getLocation().getWorld().getName())) {

				if (e.isCancelled()) {
					e.setCancelled(false);
				}

			}
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void spawnMob(short mobType, Location location) {
		
		LivingEntity le = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.fromId(mobType));
		
		Main.currentGame.getLivingEntity().add(le);
		
		
	}

	private boolean checkBalance(Player p, int i) {
		
		if(Core.playerIsHost(p)){
			
			if(Core.getFunbocksHost().getPoints() >= i)
				return true;
		}
		
		return false;
	}
	
	
	
}
