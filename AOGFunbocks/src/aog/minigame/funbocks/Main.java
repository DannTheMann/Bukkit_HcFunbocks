package aog.minigame.funbocks;

import java.io.File;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import aog.minigame.funbocks.events.EntityEvents;
import aog.minigame.funbocks.events.FBWinnerEvents;
import aog.minigame.funbocks.events.HostEvents;
import aog.minigame.funbocks.events.PlayerEvents;
import aog.minigame.funbocks.events.ShopEvents;
import aog.minigame.funbocks.instance.FBData;
import aog.minigame.funbocks.instance.FBGame;
import aog.minigame.funbocks.instance.Loader;

public class Main extends JavaPlugin{
	
	public static Main p;
	public static FBGame currentGame;
	public static FBData data;
	public static final String prefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Funbocks" + ChatColor.GOLD + "] " + ChatColor.GRAY;
	public static String dir;
    public static Economy economy = null;
    public static boolean automatic = false;
    public static final String sep = File.separator;
    private Loader loader = new Loader();
	
	public void onEnable(){
		log("Enabled.");
		
		registerCommands();
		registerEvents();
		p = this;
		setupEconomy();
		dir = getDataFolder().getAbsoluteFile().getAbsolutePath() + sep;
		
		File f = new File(dir);
		
		getConfig().addDefault("Automatic", true);
		
		getConfig().options().copyDefaults(true);
		
		saveConfig();
		
		if(!f.exists()){
			f.mkdir();
			
			data = new FBData();
			log("Created a new instance of Data.");
		}else{
			data = Serial.loadDataFile();
		}
		
		if(data == null){
			data = new FBData();
		}
		
		automatic = getConfig().getBoolean("Automatic");
		
		if(automatic){
			
			final int seconds = (20*60)*1;
			
			log("Automatically scheduling a game to start for funbocks, game will begin in " + (seconds / 20) + " seconds.");
			
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				
				@Override
				public void run() {
					startNewGame();
				}

			},seconds);			
		}
		
		log("Finished!");
		
	}
	
	
	protected void startNewGame() {
		
		if(data.getMaps().isEmpty()){
			log("Failed to automatically start a new game, no maps found.");
			return;
		}
		
		if(currentGame != null){
			log("Failed to automatically start a new game, a game is already started.");
			return;
		}
		
		currentGame = new FBGame(data.getRandomMaps());
		
	}


	private void registerCommands() {
		getServer().getPluginCommand("fb").setExecutor(loader.getFBCommand());		
	}


	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EntityEvents(), this);
		pm.registerEvents(new FBWinnerEvents(), this);
		pm.registerEvents(new HostEvents(), this);
		pm.registerEvents(new PlayerEvents(), this);
		pm.registerEvents(new ShopEvents(), this);
	}


	public void onDisable(){
		
		if(Main.currentGame != null)
			Main.currentGame.forceEnd();
		
		Serial.saveDataFile();
		log("Disabled.");
	}

	public static void log(String string) {
		System.out.println("[Funbocks] " + string);
	}
	
    private boolean setupEconomy()
    {
        if(getServer().getPluginManager().getPlugin("Vault") == null){
        	System.out.println("Vault not found!");
        	return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
        	System.out.println("RSP not found!");
        	return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }	

}
