package aog.minigame.funbocks.instance;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import aog.minigame.funbocks.FBCommand;
import aog.minigame.funbocks.events.EntityEvents;
import aog.minigame.funbocks.events.FBWinnerEvents;
import aog.minigame.funbocks.events.HostEvents;
import aog.minigame.funbocks.events.PlayerEvents;
import aog.minigame.funbocks.events.ShopEvents;

public class Loader {
	
	private Listener entityEvents;
	private Listener fbWinnerEvents;
	private Listener hostEvents;
	private Listener playerEvents;
	private Listener shopEvents;
	private CommandExecutor commands;
	
	public Loader(){
		entityEvents = new EntityEvents();
		fbWinnerEvents = new FBWinnerEvents();
		hostEvents = new HostEvents();
		playerEvents = new PlayerEvents();
		shopEvents = new ShopEvents();
		commands = new FBCommand();
	}
	
	public Listener getEntityEvents() { return this.entityEvents; }
	public Listener getFBWinnerEvents() { return this.fbWinnerEvents; }
	public Listener getHostEvents() { return this.hostEvents; }
	public Listener getPlayerEvents() { return this.playerEvents; }
	public Listener getShopEvents() { return this.shopEvents; }

	public CommandExecutor getFBCommand() { return this.commands; }
	

}
