package aog.minigame.funbocks.events;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import aog.minigame.funbocks.Core;
import aog.minigame.funbocks.Main;
import aog.minigame.funbocks.instance.FBPlayer;

public class ShopEvents implements Listener{
	
	@EventHandler
	  public void onSignChange(SignChangeEvent e){
	  Player user = e.getPlayer();
	    if ((e.getBlock().getTypeId() == 68) || (e.getBlock().getTypeId() == 63))
	    {
	      if (e.getLine(0).equalsIgnoreCase("[FB Shop]"))
	      {
	        if (!user.isOp())
	        {
	          e.getBlock().breakNaturally();
	          
	        }else{
	        	e.setLine(0, ChatColor.GREEN + "[FB Shop]");
	        }
	      }
	    }
	  }
	
	@EventHandler
	public void useShop(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		FBPlayer fbp = Core.getFunbocksPlayer(p);
		
		if(Main.currentGame != null && Core.playerWasInFunbocks(p)
				&& e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.SIGN 
				|| e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN)){
			
			Sign sign = (Sign) e.getClickedBlock().getState();
			
			if(!Main.currentGame.isShopping()){
				fbp.message(ChatColor.RED + "Game has not started yet, you can't buy anything!");
				return;
			}
			
			if(!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[FB Shop]")){
				//Bukkit.broadcastMessage("Not fb sign.");
				return;
			}
			
			//[FB Shop]
			// ItemID:Dura
			// Amount
			// Buying:Selling
			
			ItemStack item = null;
			int buyPrice = 0;
			int sellingPrice = 0;
			
			try{
			
			 buyPrice = Core.signGetBuyingPrice(sign);
			 sellingPrice = Core.signGetSellingPrice(sign);
			 item = Core.signGetItemStack(sign);
			
			}catch(Exception e2){
				e2.printStackTrace();
				fbp.message(ChatColor.RED + "Error: This sign seems to be ill configured, please report it to an Administrator.");
				return;
			}
			
			if(item == null || buyPrice <= 0){
				fbp.message(ChatColor.RED + "Error: This sign seems to be ill configured, please report it to an Administrator.");
				return;
			}
			
			/**
			 * Buying from shop
			 */
				
				/**
				 * Buying item
				 */
				if(e.getAction() == Action.LEFT_CLICK_BLOCK){
					
					if(fbp.getBalance() >= buyPrice){
						
						if(Core.playerHasRoom(p, item)){
							
							fbp.withdrawMoney(buyPrice);
							
							p.getInventory().addItem(item);
							
							fbp.message("You purchased '" + ChatColor.YELLOW + WordUtils.capitalize(item.getType().name().replaceAll("_", " ")) + 
							"' x" + item.getAmount() + ChatColor.GRAY + " for " +  ChatColor.DARK_GREEN + buyPrice + " points.");
							
							fbp.message("Balance $" + fbp.getBalance());
						
						}else
							fbp.message(ChatColor.RED + "You don't have space for this item.");
						
					}else
						fbp.message(ChatColor.RED + "You can't buy '" + WordUtils.capitalize(item.getType().name().replaceAll("_", " ")) + 
								"' x" + item.getAmount());
					
					
				}
				/**
				 * Checking price
				 */
				else
				{
					
					String canBuy = ChatColor.RED + "You can't buy '" + WordUtils.capitalize(item.getType().name().replaceAll("_", " ")) + 
							"' x" + item.getAmount();
					String hasSpace = ChatColor.RED + "You don't have space for this item.";
					
					
					if(Core.playerHasRoom(p, item))
						hasSpace = ChatColor.DARK_GREEN + "You have space for this item.";
					
					if(fbp.getBalance() >= buyPrice)
						canBuy = ChatColor.GREEN + "You can buy '" + WordUtils.capitalize(item.getType().name().replaceAll("_", " ")) + 
						"' x" + item.getAmount() ;
					
					fbp.getRawUser().sendMessage(ChatColor.YELLOW + "-==================================================-");
					fbp.getRawUser().sendMessage(" " + canBuy);
					fbp.getRawUser().sendMessage(hasSpace);
					fbp.getRawUser().sendMessage(ChatColor.GREEN + " Buying Price: " + buyPrice);
					fbp.getRawUser().sendMessage(ChatColor.GREEN + " Amount: " + item.getAmount());
					fbp.getRawUser().sendMessage(ChatColor.YELLOW + "-==================================================-");
					fbp.getRawUser().sendMessage(ChatColor.GRAY + "Left Click this sign to purchase.");
					
				}
				
			
			
			
		}
		
	}

}
