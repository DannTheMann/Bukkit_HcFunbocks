package aog.minigame.funbocks.events;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.instance.FBPlayer;

public class PlayerEvents implements Listener{
	
	// Player Quit
	@EventHandler
	public void quit(PlayerQuitEvent e){
		
		if(Core.playerWasInFunbocks(e.getPlayer())){
			
			Core.getFunbocksPlayer(e.getPlayer()).leaveGame();
			
		}
		
	}
	
	// Player Leave
	@EventHandler
	public void quit(PlayerKickEvent e){
		
		if(Core.playerWasInFunbocks(e.getPlayer())){
			
			Core.getFunbocksPlayer(e.getPlayer()).forceLeaveGame();
			
		}
		
	}
	
	// Player death
	@EventHandler
	public void quit(PlayerRespawnEvent e){
		
		if(Core.playerWasInFunbocks(e.getPlayer())){
			
			FBPlayer fp = Core.getFunbocksPlayer(e.getPlayer());
			
			fp.getData().restoreData();
			
			if(Main.currentGame != null)
				Main.currentGame.getPlayers().remove(fp.getId());
			
		}
		
	}
	// Player commands
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e){
		
		if( (Core.playerWasInFunbocks(e.getPlayer()) || (Main.currentGame != null && Main.currentGame.isAutomatic() && Core.playerIsHost(e.getPlayer())) )
				
				&& (!(e.getMessage().split(" ")[0].equalsIgnoreCase("/fb"))) ){
			
			if(e.getPlayer().hasPermission("Funbocks.Mod")){
				e.getPlayer().sendMessage(Main.prefix + "You can use commands, " +
						"however be careful. Do not leave the funbocks arena unless it's an emergency otherwise you'll break things.");
				return;
			}
			
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + "You can only execute /fb commands during a funbocks match.");
			
		}
		
	}
	
	// Player Teleport
	@EventHandler
	public void teleport(EntityTeleportEvent e){
		
		if(e.getEntity() instanceof Player){
		
			Player p = (Player) e.getEntity();
			
			if(Core.playerWasInFunbocks(p) || (Main.currentGame != null && Main.currentGame.isAutomatic() && Core.playerIsHost(p))){
			
				e.setCancelled(true);
			
			}
			
		}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void championTalk(AsyncPlayerChatEvent e){
		
		if(e.getPlayer().getUniqueId().toString().equalsIgnoreCase(Main.data.getCurrentChampion())){
			
			if(e.isCancelled())
				return;
			else{
				e.setMessage(ChatColor.GOLD + "[Champion] " + ChatColor.RESET + "" + e.getMessage());
			}
			
		}
		
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		
		Player p = e.getPlayer();
		FBPlayer fbp = Core.getFunbocksPlayer(p);
	
		if(fbp != null && Main.currentGame != null && Main.currentGame.isShopping()){
			e.setCancelled(true);
		}
		
	}
	
	// Interaction Events (Anvils, Tables, Signs)
	@EventHandler
	public void interact(PlayerInteractEvent e){

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK
				|| e.getAction() == Action.LEFT_CLICK_BLOCK){

			Player p = e.getPlayer();

			if(e.getClickedBlock() != null && Main.currentGame != null && Core.playerWasInFunbocks(p)){

				if(e.getClickedBlock().getType() == Material.ANVIL && (Main.currentGame.getRound() < 5)){

					e.getClickedBlock().setType(Material.ANVIL);
					e.setCancelled(true);
					p.sendMessage(Main.prefix + ChatColor.YELLOW + "You can only use Anvils after round 5.");

				}else if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE && (Main.currentGame.getRound() < 3)){

					e.setCancelled(true);
					p.sendMessage(Main.prefix + ChatColor.YELLOW + "You can only use Enchantment Tables after round 3.");

				}else if(e.getClickedBlock().getType() == Material.EMERALD_BLOCK && Main.currentGame != null){
					
					String id = p.getUniqueId().toString();
					p.sendMessage(Main.prefix + ChatColor.GREEN + "Balance $" + Main.currentGame.getPlayers().get(id).getBalance());
					
				}
			}

		}


		Player p = e.getPlayer();
		FBPlayer fp = Core.getFunbocksPlayer(p);
		
		if(fp == null){
			return;
		}
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){

			if(e.getClickedBlock().getType() == Material.SIGN
					|| e.getClickedBlock().getType() == Material.SIGN_POST){

				Sign sign = (Sign) e.getClickedBlock().getState();

				if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[FBShop]")){

					int cost = Integer.parseInt(sign.getLine(2));

					if(cost <= fp.getBalance()){

						int itemId = 0;
						short durability = 0;
						int amount = 1;

						if(sign.getLine(1).split(":").length > 0){
							durability = Short.parseShort(sign.getLine(1).split(":")[1]);
							itemId = Integer.parseInt(sign.getLine(1).split(":")[0]);
						}else
							itemId = Integer.parseInt(sign.getLine(1));

						if(sign.getLine(1).split(",").length > 0)
							amount = Integer.parseInt(sign.getLine(1).split(",")[1]);
						else
							itemId = Integer.parseInt(sign.getLine(1));

						@SuppressWarnings("deprecation")
						ItemStack is = new ItemStack(itemId, amount);
						is.setDurability(durability);

						fp.withdrawMoney(cost);

						p.getInventory().addItem(is);

						p.sendMessage(ChatColor.GREEN + "[Shop] You purchased '" + WordUtils.capitalize(is.getType().toString())
								+ "' for " + cost + " token(s).");

					}else
						p.sendMessage(ChatColor.RED + "[Shop] You can't afford this item.");
				}

			}

		}else if(e.getAction() == Action.LEFT_CLICK_BLOCK){

			if(e.getClickedBlock().getType() == Material.SIGN
					|| e.getClickedBlock().getType() == Material.SIGN_POST){

				Sign sign = (Sign) e.getClickedBlock().getState();

				if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[FBShop]")){

					int cost = Integer.parseInt(sign.getLine(2));

					int itemId = 0;
					short durability = 0;
					int amount = 1;

					if(sign.getLine(1).split(":").length > 0){
						durability = Short.parseShort(sign.getLine(1).split(":")[1]);
						itemId = Integer.parseInt(sign.getLine(1).split(":")[0]);
					}else
						itemId = Integer.parseInt(sign.getLine(1));

					if(sign.getLine(1).split(",").length > 0)
						amount = Integer.parseInt(sign.getLine(1).split(",")[1]);
					else
						itemId = Integer.parseInt(sign.getLine(1));

					@SuppressWarnings("deprecation")
					ItemStack is = new ItemStack(itemId, amount);
					is.setDurability(durability);

					p.sendMessage(ChatColor.YELLOW + "[Shop] " + WordUtils.capitalize(is.getType().toString()
							) + ":" + is.getDurability()
							+ " x" + is.getAmount() + " for " + cost + " token(s).");
				}

			}

		}

	}
	
	
}
